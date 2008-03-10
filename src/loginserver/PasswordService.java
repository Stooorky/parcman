package loginserver;

import java.security.*;
import java.io.*;
import java.math.*;
import java.rmi.*;

import plog.*;

public class PasswordService
{
	private static PasswordService instance;
	private MessageDigest algorithm;

	private PasswordService() throws 
		RemoteException
	{
		String algorithmName = "SHA-512";
		try
		{
			this.algorithm = MessageDigest.getInstance(algorithmName);
		}
		catch (NoSuchAlgorithmException e)
		{
			PLog.err(e, "PasswordService", "Algoritmo di crirptazione " + algorithmName + " mancante.");
			throw new RemoteException();
		}
	}

	public static PasswordService getInstance() throws 
		RemoteException
	{
		if (instance == null)
		{
			instance = new PasswordService();
		}
		
		return instance;
	}

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
			PLog.err(e, "PasswordService.encrypt", "Encoding " + encoding + "non supportato");
			throw new RemoteException();
		}
		byte digest[] = this.algorithm.digest();
		BigInteger number = new BigInteger(1, digest);
		return number.toString(16);
	}
}
