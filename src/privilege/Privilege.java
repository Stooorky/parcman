package privilege;

/**
 * Gestore dei privilegi.
 *
 * @author Parcman Tm
 */
public class Privilege
{
    /**
     * Ritorna i privilegi utente.
     *
     * @return Privilegi utente
     */
    public static String getUserPrivilege()
    {
        return "NormalUser";
    }

    /**
     * Ritorna i privilegi di amministratore.
     *
     * @return Privilegi di amministratore
     */
    public static String getAdminPrivilege()
    {
        return "AdminPrivilege";
    }
}

