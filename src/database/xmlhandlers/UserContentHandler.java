package database.xmlhandlers;

import java.util.*;
import org.xml.sax.*;

import database.beans.*;
import plog.*;

/**
 * Xml ContentHandler per il parsing degli users.
 *
 * @author Parcman Tm
 */
public class UserContentHandler 
	implements ContentHandler
{
	/** 
	 * 'true' se stiamo indagando l'elemento <NAME></NAME>.
	 */
	private boolean inName;

	/** 
	 * 'true' se stiamo indagando l'elemento <PASSWORD></PASSWORD>.
	 */
	private boolean inPass;

	/** 
	 * 'true' se stiamo indagando l'elemento <PRIVILEGE></PRIVILEGE>.
	 */
	private boolean inPriv;

	/** 
	 * 'true' se stiamo indagando l'elemento <BLACKLIST></BLACKLIST>.
	 */
	private boolean inBlack;

	/** 
	 * Oggetto bean per la memorizzazione di un utente.
	 */
	private UserBean bean;

	/** 
	 * Lista degli utenti registrati.
	 */
	private Vector<UserBean> users;

	/**
	 * Costruttore.
	 * 
	 * @param users Lista degli utenti registrati.
	 */
	public UserContentHandler(Vector<UserBean> users)
	{
		this.users = users;
	}

	public void characters(char[] ch, int start, int length)
		throws SAXException 
	{
		if (this.inName)
		{
			this.bean.setName(new String(ch, start, length));
			this.inName = false;
		}
		else if (this.inPass) 
		{
			this.bean.setPassword(new String(ch, start, length));
			this.inPass = false;
		}
		else if (this.inPriv) 
		{
			this.bean.setPrivilege(new String(ch, start, length));
			this.inPriv = false;
		}
		else if (this.inBlack)
		{
			this.bean.setBlacklist(new String(ch, start, length));
			this.inBlack = false;
		}
		else 
		{
			throw new SAXException("Elemento non riconosciuto.");
		}
	}

	public void endDocument() 
		throws SAXException 
	{

	}

	public void endElement(String uri, String localName, String qName) 
		throws SAXException 
	{
		if ("USER".equals(localName)
			&& !this.users.contains(this.bean))
		{
			this.users.add(this.bean);
			PLog.debug("UserContentHandler.endElement",
				"Caricato nuovo utente. Nome: " + this.bean.getName() + " Privilegi: " + this.bean.getPrivilege());
		}
	}

	public void endPrefixMapping(String prefix) 
		throws SAXException 
	{

	}

	public void ignorableWhitespace(char[] ch, int start, int length) 
		throws SAXException 
	{

	}

	public void processingInstruction(String target, String data) 
		throws SAXException 
	{

	}

	public void setDocumentLocator(Locator locator) 
	{

	}

	public void skippedEntity(String name) 
		throws SAXException 
	{

	}

	public void startDocument() 
		throws SAXException 
	{

	}

	public void startElement(String uri, String localName, String qName, Attributes atts) 
		throws SAXException 
	{
		if ("NAME".equals(localName))
		{
			this.inName = true;
		}
		else if ("PASSWORD".equals(localName))
		{
			this.inPass = true;
		}
		else if ("PRIVILEGE".equals(localName))
		{
			this.inPriv = true;
		}
		else if ("BLACKLIST".equals(localName))
		{
			this.inBlack = true;
		}
		else if ("USER".equals(localName))
		{
			this.bean = new UserBean();
		}
		else if ("DB_USERS".equals(localName))
		{
			
		}
		else 
		{
			throw new SAXException("Elemento non riconosciuto.");
		}
	}

	public void startPrefixMapping(String prefix, String uri) 
		throws SAXException 
	{

	}

}
