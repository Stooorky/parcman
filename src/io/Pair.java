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

package io;

import java.io.Serializable;

class Pair<E,F> implements Serializable
{
	private static final long serialVersionUID = 42L;

	protected E first;
	protected F second;

	public Pair()
	{}

	public Pair(E first, F second)
	{
		this.first = first;
		this.second = second;
	}

	public E getFirst()
	{
		return first;
	}

	public void setFirst(E first)
	{
		this.first = first;
	}

	public F getSecond()
	{
		return second;
	}

	public void setSecond(F second)
	{
		this.second = second;
	}
	
	public String toString()
	{
		return "('" + first + "', '" + second + "')";
	}
}
