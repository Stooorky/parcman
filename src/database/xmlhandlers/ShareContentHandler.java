package database.xmlhandlers;

import java.util.*;
import java.net.MalformedURLException;
import org.xml.sax.*;
import java.lang.NumberFormatException;

import database.beans.*;
import plog.*;

/**
 * Xml Contenthandler per il parsing dei file condivisi.
 *
 * @author Parcman Tm
 */
public class ShareContentHandler
	implements ContentHandler
{
	private static final String ELEMENT_FILE = "FILE";
	private static final String ELEMENT_DB= "DB_SHARINGS";
	private static final String ELEMENT_ID = "ID";
	private static final String ELEMENT_URL= "URL";
	private static final String ELEMENT_OWNER = "OWNER";
	private static final String ELEMENT_HASH = "HASH";
	private static final String ELEMENT_KEYWORDS = "KEYWORDS";

	private static final String KEYWORD_SEPARATOR = ",";

	private boolean inId;
	private boolean inUrl;
	private boolean inOwner;
	private boolean inHash;
	private boolean inKeywords;
	private ShareBean bean;
	private Vector<ShareBean> sharings;

	public ShareContentHandler(Vector<ShareBean> sharings)
	{
		this.sharings = sharings;
	}

	public void characters(char[] ch, int start, int length)
		throws SAXException
	{
		String data = new String(ch, start, length);
		if (this.inId)
		{
			int id = 0;
			try
			{
				id = new Integer(new String(ch, start, length)).intValue();
			} 
			catch (NumberFormatException e) 
			{
				throw new SAXException(e);
			}

			this.bean.setId(id);
			this.inId = false;
		}
		else if (this.inUrl)
		{
			try 
			{
				this.bean.setUrl(data);
			}
			catch (MalformedURLException e)
			{
				throw new SAXException(e);
			}

			this.inUrl= false;
		}
		else if (this.inOwner)
		{
			this.bean.setOwner(data);
			this.inOwner = false;
		}
		else if (this.inHash)
		{
			this.bean.setHash(data);
			this.inHash = false;
		}
		else if (this.inKeywords)
		{
			this.bean.setKeywords(this.parseKeywords(data));
			this.inKeywords = false;
		}
		else 
		{
			throw new SAXException("Elemento non riconosciuto (" + data + ").");
		}
	}

	public void endDocument() 
		throws SAXException 
	{

	}

	public void endElement(String uri, String localName, String qName) 
		throws SAXException 
	{
		if (ShareContentHandler.ELEMENT_FILE.equals(localName)
			&& !this.sharings.contains(this.bean))
		{
			this.sharings.add(this.bean);
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
		if (ShareContentHandler.ELEMENT_ID.equals(localName))
		{
			this.inId = true;
		}
		else if (ShareContentHandler.ELEMENT_URL.equals(localName))
		{
			this.inUrl= true;
		}
		else if (ShareContentHandler.ELEMENT_OWNER.equals(localName))
		{
			this.inOwner = true;
		}
		else if (ShareContentHandler.ELEMENT_HASH.equals(localName))
		{
			this.inHash = true;
		}
		else if (ShareContentHandler.ELEMENT_KEYWORDS.equals(localName))
		{
			this.inKeywords = true;
		}
		else if (ShareContentHandler.ELEMENT_FILE.equals(localName))
		{
			this.bean = new ShareBean();
		}
		else if (ShareContentHandler.ELEMENT_DB.equals(localName))
		{

		}
		else 
		{
			throw new SAXException("Elemento non riconosciuto (" + localName + ").");
		}
	}

	public void startPrefixMapping(String prefix, String uri) 
		throws SAXException 
	{

	}

	public Vector<String> parseKeywords(String data)
	{
		Vector<String> keywords = new Vector<String>();
		String[] k = data.split(ShareContentHandler.KEYWORD_SEPARATOR);
		for (int i=0; i<k.length; i++)
		{
			keywords.add(k[i].trim());
		}
		return keywords;
	}

}
