package xmlhandlers;

import org.xml.sax.*;

import beans.*;

import java.util.*;

/**
 * Xml ContentHandler per il parsing degli users.
 *
 * @author Parcman Tm
 */
public class UserContentHandler 
    implements ContentHandler
{
    private boolean inName, inPass, inPriv;
    private UserBean bean;
    private Vector<UserBean> users;

    public UserContentHandler(Vector<UserBean> user)
    {
	this.users = users;
    }

    public void characters(char[] ch, int start, int length)
	throws SAXException 
    {
	if (this.inName)
	{
	    this.bean.setName(new String(ch));
	    this.inName = false;
	}
	else if (this.inPass) 
	{
	    this.bean.setPassword(new String(ch));
	    this.inPass = false;
	}
	else if (this.inPriv) 
	{
	    this.bean.setPrivilege(new String(ch));
	    this.inPriv = false;
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
	if ("USER".equals(localName))
	{
	    this.users.add(this.bean);
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
	else if ("USER".equals(localName))
	{
	    this.bean = new UserBean();
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
