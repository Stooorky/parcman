package io;

import java.lang.reflect.Field;
import java.io.Serializable;

public class IOColor implements Serializable 
{
	private static final long serialVersionUID = 42L;

	public static final String NOCOLOR =		"\033[0m";
	public static final String BLACK =		"\033[30m";
	public static final String RED =		"\033[31m";
	public static final String GREEN =		"\033[32m";
	public static final String BROWN =		"\033[33m";
	public static final String BLUE =		"\033[34m";
	public static final String PURPLE =		"\033[35m";
	public static final String CYAN =		"\033[36m";
	public static final String LIGHT_GREY =		"\033[37m";
	public static final String DARK_GREY =		"\033[30;1m";
	public static final String LIGHT_RED =		"\033[31;1m";
	public static final String LIGHT_GREEN =	"\033[32;1m";
	public static final String YELLOW =		"\033[33;1m";
	public static final String LIGHT_BLUE =		"\033[34;1m";
	public static final String LIGHT_PURPLE =	"\033[35;1m";
	public static final String LIGHT_CYAN =		"\033[36;1m";
	public static final String WHITE =		"\033[37;1m";

	public static String getColor(String name)
	{
		try
		{
			Class<?> c = IOColor.class;
			Field constant = c.getDeclaredField(name);
			return (String) constant.get(new IOColor());
		}
		catch (Exception e)
		{
			return "\033[0m";
		}
	}
}
