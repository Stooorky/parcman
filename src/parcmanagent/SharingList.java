package parcmanagent;

import java.io.Serializable;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import database.beans.ShareBean;

/**
 * Contenitore dati per il trasporto dei nuovi file condivisi
 * tra client e server attraverso ParcmanAgent.
 *
 * @author Parcman Tm
 */
public class SharingList
	implements Serializable
{
	/**
	 * Serial Version UID per il check di compatibilita`.
	 */
	private static final long serialVersionUID = 42L;

    /**
     * Lista nuovi file condivisi.
     */
    private Vector<ShareElement> shareList;

    /**
     * Stringa di versione.
     */
    private String version;

    /**
     * Costruttore.
     */
    public SharingList()
    {
        shareList = new Vector<ShareElement>();
    }

    /**
     * Ritorna la lista dei nuovi file condivisi.
     *
     * @return Lista dei nuovi file condivisi
     */
    public Vector<ShareElement> getShareList()
    {
        return this.shareList;
    }

    /**
     * Setta la lista dei nuovi file condivisi.
     *
     * @param shareList Lista dei nuovi file condivisi
     */
    public void setShareList(Vector<ShareElement> shareList)
    {
        this.shareList = shareList;
    }

    /**
     * Ritorna la stringa di versione.
     *
     * @return stringa di versione
     */
    public String getVersion()
    {
        return this.version;
    }

    /**
     * Setta la stringa di versione.
     *
     * @param version Stringa di versione
     */
    public void setVersion(String version)
    {
        this.version = version;
    }
}

/**
 * Contenitore dati per un nuovo file condiviso.
 *
 * @author Parcman Tm
 */
class ShareElement
	implements Serializable
{
	/**
	 * Serial Version UID per il check di compatibilita`.
	 */
	private static final long serialVersionUID = 42L;


    /**
     * ShareBean del nuovo file condiviso.
     */
    private ShareBean shareBean;
 
    /**
     * Ritorna i dati del nuovo file condiviso.
     *
     * @return ShareBean del nuovo file condiviso
     */
    public ShareBean getShareBean()
    {
        return this.shareBean;
    }

    /**
     * Setta i dati del nuovo file condiviso.
     *
     * @param shareBean ShareBean del file condiviso
     */
    public void setShareBean(ShareBean shareBean)
    {
        this.shareBean = shareBean;
    }
}

