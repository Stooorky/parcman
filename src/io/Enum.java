package io;

import java.lang.reflect.Method;

public class Enum
{
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
