package loginserver;

import java.security.*;
import java.io.*;
import java.math.*;
import java.rmi.*;

import io.Logger;

/**
 * Classe di supporto per la criptazione delle password.
 *
 * @author Parcman Tm
 */
public class PasswordService
{
	/**
	 * Logger
	 */
	private Logger logger;

	/**
	 * Instanza del singleton.
	 */
	private static PasswordService instance;
	
	/**
	 * oggetto MessageDigest usato per la criptazione.
	 */
	private MessageDigest algorithm;

	/**
	 * Costruttore, privato utilizzato da getInstance().
	 *
	 * @throws RemoteException Algoritmo di criptazione sconosciuto.
	 */
	private PasswordService() throws 
		RemoteException
	{
		this.logger = Logger.getLogger("server-side");

		String algorithmName = "SHA-512";
		try
		{
			this.algorithm = MessageDigest.getInstance(algorithmName);
		}
		catch (NoSuchAlgorithmException e)
		{
			logger.error("Algoritmo di criptazione " + algorithmName + " mancante.");
			throw new RemoteException();
		}
	}

	/**
	 * Ritorna un'istanza del oggetto. 
	 *
	 * @throws RemoteException Algoritmo di criptazione sconosciuto.
	 */
	public static PasswordService getInstance() throws 
		RemoteException
	{
		if (instance == null)
		{
			instance = new PasswordService();
		}
		
		return instance;
	}

	/**
	 * cripta una password e ne ritorna la forma criptata.
	 *
	 * @param password Password da criptare.
	 * @return Una stringa contenente la password criptata.
	 * @throws RemoteException Encoding sconosciuto per la conversione della password a byte array.
	 */
	public String encrypt(String password) throws 
		RemoteException
	{
		String encoding = "UTF-8";
		try
		{
			this.algorithm.update(password.getBytes(encoding));
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("Encoding " + encoding + "non supportato");
			throw new RemoteException();
		}
		byte digest[] = this.algorithm.digest();
		BigInteger number = new BigInteger(1, digest);
		return number.toString(16);
	}
}

