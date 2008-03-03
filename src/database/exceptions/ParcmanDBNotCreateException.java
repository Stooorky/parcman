package database.exceptions;

public class ParcmanDBNotCreateException
    extends Exception
{
    private static final long serialVersionUID = 42L;

    public ParcmanDBNotCreateException (String message)
    {
        super(message);
    }
}

