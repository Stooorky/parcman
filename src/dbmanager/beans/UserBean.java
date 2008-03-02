package dbmanager.beans;

import dbmanager.xmlhandlers.*;

/**
 * Bean DataBase Utenti.
 *
 * @author Parcman Tm
 */
public class UserBean
{
    /**
     * Nome utente.
     */
    private String name;

    /**
     * Password utente.
     */
    private String password;

    /**
     * Privilegi utente.
     */
    private String privilege;


    /**
     * Restituisce il nome utente.
     *
     * @return Nome utente.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Assegna il nome utente.
     *
     * @param name Nome utente.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Restituisce la password utente.
     *
     * @return Nome utente.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Assegna la password utente.
     *
     * @param password Password utente.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Restituisce i privilegi utente.
     *
     * @return Privilegi utente.
     */
    public String getPrivilege()
    {
        return privilege;
    }

    /**
     * Assegna i privilegi utente.
     *
     * @param privilege Privilegi utente.
     */
    public void setPrivilege(String privilege)
    {
        this.privilege = privilege;
    }
}

