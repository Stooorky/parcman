package io;

import java.io.Serializable;

public class LoggerCostants implements Serializable
{
	private static final long serialVersionUID = 42L;

	public static final int LEVELS = 5;

	public static final String LEVEL_ERROR 		= "ERROR";
	public static final String LEVEL_WARNING 	= "WARNING";
	public static final String LEVEL_DEBUG		= "DEBUG";
	public static final String LEVEL_INFO 		= "INFO";
	public static final String LEVEL_LOG 		= "LOG";
}
