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
public class UpdateList
	implements Serializable
{
	/**
	 * Serial Version UID per il check di compatibilita`.
	 */
	private static final long serialVersionUID = 42L;

    /**
     * Lista dei file da aggiungere alla condivisione.
     */
    private Vector<ShareBean> addList;

    /**
     * Lista dei file da eliminare dalla condivisione.
     */
    private Vector<Integer> removeList;

    /**
     * Versione della lista.
     */
    private int version;

    /**
     * Costruttore.
     */
    public UpdateList()
    {
        addList = new Vector<ShareBean>();
        removeList = new Vector<Integer>();
    }

    /**
     * Ritorna la lista dei nuovi file condivisi.
     *
     * @return Lista dei nuovi file condivisi
     */
    public Vector<ShareBean> getAddList()
    {
        return this.addList;
    }

    /**
     * Setta la lista dei nuovi file condivisi.
     *
     * @param shareList Lista dei nuovi file condivisi
     */
    public void setAddList(Vector<ShareBean> addList)
    {
        this.addList = addList;
    }

    /**
     * Aggiunge un nuovo ShareBean
     *
     * @param bean ShareBean
     */
    public void addShareBean(ShareBean bean)
    {
        this.addList.add(bean);
    }

    /**
     * Aggiunge un nuovo ID alla lista degli ID dei file da rimuovere.
     *
     * @param id Id di un file condiviso
     */
    public void addRemovableId(int id)
    {
        this.removeList.add(new Integer(id));
    }

    /**
     * Ritorna la lista dei file da eliminare dalla condivisione.
     *
     * @return Lista dei file da eliminare
     */
    public Vector<Integer> getRemoveList()
    {
        return this.removeList;
    }

    /**
     * Setta la lista dei file da eliminare dalla condivisione.
     *
     * @param removeList Vector dei file da eliminare
     */
    public void setRemoveList(Vector<Integer> removeList)
    {
        this.removeList = removeList;
    }

    /**
     * Ritorna la versione.
     *
     * @return versione
     */
    public int getVersion()
    {
        return this.version;
    }

    /**
     * Setta la versione.
     *
     * @param version versione
     */
    public void setVersion(int version)
    {
        this.version = version;
    }

    /**
     * Restituisce una stringa rappresentante l'oggetto.
     *
     * @return String rappresentante l'oggetto
     */
    public String toString()
    {
        String str;
        
        str = "AddList:";
        for (int i=0; i<addList.size(); i++)
            str += "\n " + addList.get(i).getName() + " Id: " + addList.get(i).getId();

        str += "\nRemoveList:";

        for (int i=0; i<removeList.size(); i++)
            str += " " + removeList.get(i).intValue();

        return str;
    }
}

