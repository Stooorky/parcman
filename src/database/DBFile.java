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

package database;

/**
 * Interfaccia database di gestione file.
 *
 * @author Parcman Tm
 */
public interface DBFile
{
	/**
	 * Salva il database su file.
	 */
	public void save();

	/**
	 * Carica in memoria il database.
	 */
	public void load();

	/**
	 * Restituisce il file gestito dal database.
	 *
	 * @return Path del file
	 */
	public String getDbFile();

	/**
	 * Setta il file gestito dal database.
	 *
	 * @param dbFile Path del file 
	 */
	public void setDbFile(String dbFile);
}
