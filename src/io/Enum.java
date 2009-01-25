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

import java.lang.reflect.Method;
import java.io.Serializable;

public class Enum implements Serializable
{
	private static final long serialVersionUID = 42L;

	public static void main(String[] args)
	{
		Enum e = new Enum();
		Method[] m = Test.class.getDeclaredMethods();
		for (int i=0; i<m.length; i++)
		{
			System.out.println(m[i].toGenericString());
		}

		for(Test t : Test.values())
		{
			System.out.println(t.values());
			System.out.println("(" + t.pos()+","+t.level()+")");
		}

		System.out.println(Test.valueOf("UNO").pos());
		System.out.println(Test.valueOf("DUE").pos());
		System.out.println(Test.valueOf("TRE").pos());


	}

	public enum Test
	{ 
		UNO (0,1), DUE (1,2), TRE (2,4), QUATTRO (3,8), CINQUE (4,16); 
		private int pos;
		private int level;
		Test(int pos, int level) {this.pos = pos; this.level = level;}
		public int pos() {return pos;}
		public int level() {return level;}
		//public int pos(int level) {return Test.level.pos();}
	}

}
