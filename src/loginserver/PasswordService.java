/*
 * Parcman Project, the Italian Arcade Network
 * Copyright (C) 2008-2009 Parcman Tm (Marchi Sirio, Marcantoni Francesco, Emanuele Dona')
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 *
 * E-Mail:	ramyel [at] gmail [dot] com
 * 			sirio.marchi [at] gmail [dot] com
 * 			emanuele.dona [at] gmail [dot] com
 */

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

