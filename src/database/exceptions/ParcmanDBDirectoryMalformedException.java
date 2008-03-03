package database.exceptions;

public class ParcmanDBDirectoryMalformedException
    extends Exception
{
    private static final long serialVersionUID = 42L;

    public ParcmanDBDirectoryMalformedException (String message)
    {
        super(message);
    }
}

