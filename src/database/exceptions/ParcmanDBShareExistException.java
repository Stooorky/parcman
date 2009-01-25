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

package database.exceptions;

/**
 * Il file condviso e' gia' esistente.
 *
 * @author Parcman Tm
 */
public class ParcmanDBShareExistException
	extends Exception
{
	/**
	 * Serial Version UID per il check di compatibilita'.
	 */
	private static final long serialVersionUID = 42L;

	/** 
	 * Costruttore.
	 */
	public ParcmanDBShareExistException()
	{
		super();
	}

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 */
	public ParcmanDBShareExistException(String message)
	{
		super(message);
	}

	/** 
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 * @param cause La causa scatenante.
	 */
	public ParcmanDBShareExistException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Costruttore.
	 *
	 * @param cause La causa scatenante.
	 */
	public ParcmanDBShareExistException(Throwable cause)
	{
		super(cause);
	}
}
