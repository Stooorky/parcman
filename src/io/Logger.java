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
import java.util.Hashtable;
import java.util.Properties;
import java.util.Arrays;
import java.lang.Throwable;
import java.lang.StackTraceElement;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.StringBuffer;
import java.lang.reflect.Field;

public class Logger implements Serializable
{
	private static final long serialVersionUID = 42L;

	private static Hashtable<String, Logger> map;

	private Properties prop;
	private boolean _enable_;
	private Pattern p; 
	private SimpleDateFormat df;
	private int[] levels;
	private int depth = 2;

	// TODO: trasformare LOG_LEVEL in enum o aggiungere a LoggerLevel
	public static final String LOG_CLASS = "%C";
	public static final String LOG_METHOD = "%M";
	public static final String LOG_FILE = "%F";
	public static final String LOG_LINE = "%L";
	public static final String LOG_DATE = "%D";
	public static final String LOG_LEVEL = "%T";
	public static final String LOG_MESSAGE = "%S";
	public static final String LOG_EXCEPTION = "%E";

	protected Logger(Properties prop, int depth)
	{
		this._enable_ = "true".equals(prop.getProperty(LoggerProperties.ENABLE)) ? true : false;
		setActiveLevels(prop.getProperty(LoggerProperties.LEVEL));
		this.prop = prop;
		this.depth = depth;
		this.p = Pattern.compile(	LOG_CLASS + "|" + LOG_METHOD + "|" 
						+ LOG_FILE + "|" + LOG_LINE + "|"
						+ LOG_DATE + "|" + LOG_LEVEL + "|"
						+ LOG_MESSAGE + "|" + LOG_EXCEPTION);
		this.df = new SimpleDateFormat(prop.getProperty(LoggerProperties.DATE_FORMAT));
	}

	public static Logger getLogger(String channel, int depth)
	{
		return Logger.getLogger(channel, null, depth);
	}

	public static Logger getLogger(String channel, Properties prop)
	{
		return Logger.getLogger(channel, prop, 2);
	}

	public static Logger getLogger(String channel)
	{
		return Logger.getLogger(channel, null, 2);
	}

	public static Logger getLogger(String channel, Properties prop, int depth)
	{
		if (map == null)
			map = new Hashtable<String, Logger>();
		Logger log = null;
		if (!map.containsKey(channel))
		{
			if (prop == null)
				prop = new LoggerProperties().getDefaults();
			log = new Logger(prop, depth);
			map.put(channel, log);
		}
		else 
		{
			log = map.get(channel);
		}

		return log;
	}

	protected String format(StackTraceElement ste, long time, String level, String msg, String format, Throwable e)
	{
		Matcher m = p.matcher(format);
		StringBuffer sb = new StringBuffer();
		while (m.find())
		{
			String found = m.group(0);
			if (LOG_CLASS.equals(found))
				m.appendReplacement(sb, getColor(level, "CLASS") + ste.getClassName() + IOColor.NOCOLOR);
			else if (LOG_METHOD.equals(found))
				m.appendReplacement(sb, getColor(level, "METHOD") + ste.getMethodName() + IOColor.NOCOLOR);
			else if (LOG_FILE.equals(found))
				m.appendReplacement(sb, getColor(level, "FILE") + ste.getFileName() + IOColor.NOCOLOR);
			else if (LOG_LINE.equals(found))
				m.appendReplacement(sb, getColor(level, "LINE") + ste.getLineNumber() + IOColor.NOCOLOR);
			else if (LOG_DATE.equals(found))
				m.appendReplacement(sb, getColor(level, "DATE") + df.format(new Date(time)) + IOColor.NOCOLOR);
			else if (LOG_LEVEL.equals(found))
				m.appendReplacement(sb, getColor(level, "LEVEL") + level + IOColor.NOCOLOR);
			else if (LOG_MESSAGE.equals(found))
				m.appendReplacement(sb, getColor(level, "MESSAGE") + msg + IOColor.NOCOLOR);
			else if (e != null && LOG_EXCEPTION.equals(found))
			{
				StringBuffer sbst = new StringBuffer();
				String ename = e.getClass().getName();
				StackTraceElement[] st = e.getStackTrace();
				sbst.append("\n\t" + ename + "\n");
				for (int i=0; i<st.length; i++)
				{
					sbst.append("\t\t" + st[i] + "\n");
				}
				sbst.append("\n");
				m.appendReplacement(sb, getColor(level, "EXCEPTION") + sbst.toString() + IOColor.NOCOLOR);
			}
			else 
			{
				m.appendReplacement(sb, "");
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	protected void setActiveLevels(String level)
	{
		levels = new int[LoggerConstants.LEVELS];
		Arrays.fill(levels, 0);
		int ll = Integer.parseInt(level);
		for (LoggerLevel l : LoggerLevel.values())
		{
			levels[l.position()] = (ll & l.level()) != 0 ? 1 : 0;
		}
	}

	protected boolean isActive(String level)
	{
		if (!_enable_) 
			return false;
		if (levels[LoggerLevel.valueOf(level).position()] == 0)
			return false;
		return true;
	}

	protected String getColor(String level, String section)
	{
		String color = IOColor.NOCOLOR;
		try
		{
			Class<?> c = LoggerProperties.class;
			Field f = c.getDeclaredField("COLOR_" + section + "_" + level);
			color = IOColor.getColor(prop.getProperty((String) f.get(new LoggerProperties())));
		}
		catch (Exception e) {}
		return color;
	}

	public void makeLog(String level, String[] msg, Throwable e)
	{
		if (!isActive(level)) return;
		Throwable t = new Throwable();
		long time = System.currentTimeMillis();
		StackTraceElement[] stack = t.getStackTrace();
		String out = format(stack[depth], time, level, msg[0], prop.getProperty(LoggerProperties.FORMAT_ERROR), e);
		for (int i=1; i<msg.length; i++)
		{
			out += "\t" + msg[i];
		}
		System.out.println(out);
	}

	public void makeLog(String level, String msg, Throwable e)
	{
		if (!isActive(level)) return;
		Throwable t = new Throwable();
		long time = System.currentTimeMillis();
		StackTraceElement[] stack = t.getStackTrace();
		String out = format(stack[depth], time, level, msg, prop.getProperty(LoggerProperties.getPropertyName("FORMAT_" + level)), e);
		System.out.println(out);
	}

	public void error(String msg)
	{
		makeLog(LoggerConstants.LEVEL_ERROR, msg, null);
	}

	public void warning(String msg)
	{
		makeLog(LoggerConstants.LEVEL_WARNING, msg, null);
	}

	public void debug(String msg)
	{
		makeLog(LoggerConstants.LEVEL_DEBUG, msg, null);
	}

	public void info(String msg)
	{
		makeLog(LoggerConstants.LEVEL_INFO, msg, null);
	}

	public void log(String msg)
	{
		makeLog(LoggerConstants.LEVEL_LOG, msg, null);
	}

	public void error(String msg, Throwable e)
	{
		makeLog(LoggerConstants.LEVEL_ERROR, msg, e);
	}

	public void warning(String msg, Throwable e)
	{
		makeLog(LoggerConstants.LEVEL_WARNING, msg, e);
	}

	public void debug(String msg, Throwable e)
	{
		makeLog(LoggerConstants.LEVEL_DEBUG, msg, e);
	}

	public void info(String msg, Throwable e)
	{
		makeLog(LoggerConstants.LEVEL_INFO, msg, e);
	}

	public void log(String msg, Throwable e)
	{
		makeLog(LoggerConstants.LEVEL_LOG, msg, e);
	}

	public void error(String[] msg)
	{
		makeLog(LoggerConstants.LEVEL_ERROR, msg, null);
	}

	public void warning(String[] msg)
	{
		makeLog(LoggerConstants.LEVEL_WARNING, msg, null);
	}

	public void debug(String[] msg)
	{
		makeLog(LoggerConstants.LEVEL_DEBUG, msg, null);
	}

	public void info(String[] msg)
	{
		makeLog(LoggerConstants.LEVEL_INFO, msg, null);
	}

	public void log(String[] msg)
	{
		makeLog(LoggerConstants.LEVEL_LOG, msg, null);
	}

	public void error(String[] msg, Throwable e)
	{
		makeLog(LoggerConstants.LEVEL_ERROR, msg, e);
	}

	public void warning(String[] msg, Throwable e)
	{
		makeLog(LoggerConstants.LEVEL_WARNING, msg, e);
	}

	public void debug(String[] msg, Throwable e)
	{
		makeLog(LoggerConstants.LEVEL_DEBUG, msg, e);
	}

	public void info(String[] msg, Throwable e)
	{
		makeLog(LoggerConstants.LEVEL_INFO, msg, e);
	}

	public void log(String[] msg, Throwable e)
	{
		makeLog(LoggerConstants.LEVEL_LOG, msg, e);
	}

	public static void main(String[] args) throws 
		Exception 
	{
		PropertyManager.getInstance().register("logger", "logger.properties");
		Logger log = Logger.getLogger("default", PropertyManager.getInstance().get("logger"));

		log.error(LoggerConstants.LEVEL_ERROR);
		log.warning(LoggerConstants.LEVEL_WARNING);
		log.debug(LoggerConstants.LEVEL_DEBUG);
		log.info(LoggerConstants.LEVEL_INFO);
		log.log(LoggerConstants.LEVEL_LOG);

		Throwable e = new Exception();
		log.error(LoggerConstants.LEVEL_ERROR, e);
		log.warning(LoggerConstants.LEVEL_WARNING, e);
		log.debug(LoggerConstants.LEVEL_DEBUG, e);
		log.info(LoggerConstants.LEVEL_INFO, e);
		log.log(LoggerConstants.LEVEL_LOG, e);

	}
}
