package io;

import java.util.Properties;
import java.io.Serializable;

public class LoggerProperties implements Serializable
{
	private static final long serialVersionUID = 42L;

	public static final String ACTIVE_CHANNELS = "logger-active-channels"; // list: [channels of your log]
	public static final String ACTIVE_LEVELS = "logger-active-levels"; // list: [ERROR|WARNING|DEBUG|INFO|LOG]
	// come per i permessi unix. es: LEVEL=31 => ERROR+WARNING+DEBUG+INFO+LOG
	public static final String LEVEL = "logger-level" ;// int: [ERROR=1|WARNING=2|DEBUG=4|INFO=8|LOG=16] 
	public static final String ENABLE = "logger-enable"; // boolean: [true|false]
	public static final String FILE_ENABLE = "logger-file-enable"; // boolean: [true|false]
	public static final String FILE_OUTPUT = "logger-file-output"; // string: [path/to/file.log]
	public static final String DATE_FORMAT = "logger-date-format"; // string: [java standard date format]

	// format valid espressions:
	// -- %D: indica la data al momento del esecuzione del log
	// -- %F: indica il nome del file che ha lanciato il log
	// -- %L: indica la linea da cui e` stato invocato il log
	// -- %C: indica la classe d'origine del log
	// -- %M: indica il metodo d'origine del log
	// -- %T: indica il livello del log
	// -- %E: indica l'eccezione lanciata
	public static final String FORMAT_GLOBAL = "logger-format-global"; // string: [%D - %F:%L - %C.%M: ]
	public static final String FORMAT_ERROR = "logger-format-error"; // string: [%D - %F:%L - %C.%M: ]
	public static final String FORMAT_WARNING = "logger-format-warning"; // string: [%D - %F:%L - %C.%M: ]
	public static final String FORMAT_DEBUG = "logger-format-debug"; // string: [%D - %F:%L - %C.%M: ]
	public static final String FORMAT_INFO = "logger-format-info"; // string: [%D - %F:%L - %C.%M: ]
	public static final String FORMAT_LOG = "logger-format-log"; // string: [%D - %F:%L - %C.%M: ]

	public static final String COLOR_LEVEL_GLOBAL		= "logger-color-level-global"; 	// string: [color name as in IOColor]
	public static final String COLOR_LEVEL_ERROR 		= "logger-color-level-error";  	// string: [color name as in IOColor]
	public static final String COLOR_LEVEL_WARNING 	= "logger-color-level-warning";  	// string: [color name as in IOColor]
	public static final String COLOR_LEVEL_DEBUG 		= "logger-color-level-debug";  	// string: [color name as in IOColor]
	public static final String COLOR_LEVEL_INFO 		= "logger-color-level-info"; 		// string: [color name as in IOColor]
	public static final String COLOR_LEVEL_LOG 		= "logger-color-level-log"; 		// string: [color name as in IOColor]

	public static final String COLOR_DATE_GLOBAL		= "logger-color-date-global"; 	// string: [color name as in IOColor]
	public static final String COLOR_DATE_ERROR 		= "logger-color-date-error";  	// string: [color name as in IOColor]
	public static final String COLOR_DATE_WARNING 	= "logger-color-date-warning";  	// string: [color name as in IOColor]
	public static final String COLOR_DATE_DEBUG 		= "logger-color-date-debug";  	// string: [color name as in IOColor]
	public static final String COLOR_DATE_INFO 		= "logger-color-date-info"; 		// string: [color name as in IOColor]
	public static final String COLOR_DATE_LOG 		= "logger-color-date-log"; 		// string: [color name as in IOColor]

	public static final String COLOR_FILE_GLOBAL		= "logger-color-file-global"; 	// string: [color name as in IOColor]
	public static final String COLOR_FILE_ERROR 		= "logger-color-file-error";  	// string: [color name as in IOColor]
	public static final String COLOR_FILE_WARNING 	= "logger-color-file-warning";  	// string: [color name as in IOColor]
	public static final String COLOR_FILE_DEBUG 		= "logger-color-file-debug";  	// string: [color name as in IOColor]
	public static final String COLOR_FILE_INFO 		= "logger-color-file-info"; 		// string: [color name as in IOColor]
	public static final String COLOR_FILE_LOG 		= "logger-color-file-log"; 		// string: [color name as in IOColor]

	public static final String COLOR_LINE_GLOBAL		= "logger-color-line-global"; 	// string: [color name as in IOColor]
	public static final String COLOR_LINE_ERROR 		= "logger-color-line-error";  	// string: [color name as in IOColor]
	public static final String COLOR_LINE_WARNING 	= "logger-color-line-warning";  	// string: [color name as in IOColor]
	public static final String COLOR_LINE_DEBUG 		= "logger-color-line-debug";  	// string: [color name as in IOColor]
	public static final String COLOR_LINE_INFO 		= "logger-color-line-info"; 		// string: [color name as in IOColor]
	public static final String COLOR_LINE_LOG 		= "logger-color-line-log"; 		// string: [color name as in IOColor]

	public static final String COLOR_CLASS_GLOBAL		= "logger-color-class-global"; 	// string: [color name as in IOColor]
	public static final String COLOR_CLASS_ERROR 		= "logger-color-class-error";  	// string: [color name as in IOColor]
	public static final String COLOR_CLASS_WARNING 	= "logger-color-class-warning";  	// string: [color name as in IOColor]
	public static final String COLOR_CLASS_DEBUG 		= "logger-color-class-debug";  	// string: [color name as in IOColor]
	public static final String COLOR_CLASS_INFO 		= "logger-color-class-info"; 		// string: [color name as in IOColor]
	public static final String COLOR_CLASS_LOG 		= "logger-color-class-log"; 		// string: [color name as in IOColor]

	public static final String COLOR_METHOD_GLOBAL		= "logger-color-method-global"; 	// string: [color name as in IOColor]
	public static final String COLOR_METHOD_ERROR 		= "logger-color-method-error";  	// string: [color name as in IOColor]
	public static final String COLOR_METHOD_WARNING 	= "logger-color-method-warning";  	// string: [color name as in IOColor]
	public static final String COLOR_METHOD_DEBUG 		= "logger-color-method-debug";  	// string: [color name as in IOColor]
	public static final String COLOR_METHOD_INFO 		= "logger-color-method-info"; 		// string: [color name as in IOColor]
	public static final String COLOR_METHOD_LOG 		= "logger-color-method-log"; 		// string: [color name as in IOColor]

	public static final String COLOR_MESSAGE_GLOBAL		= "logger-color-message-global"; 	// string: [color name as in IOColor]
	public static final String COLOR_MESSAGE_ERROR 		= "logger-color-message-error";  	// string: [color name as in IOColor]
	public static final String COLOR_MESSAGE_WARNING 	= "logger-color-message-warning";  	// string: [color name as in IOColor]
	public static final String COLOR_MESSAGE_DEBUG 		= "logger-color-message-debug";  	// string: [color name as in IOColor]
	public static final String COLOR_MESSAGE_INFO 		= "logger-color-message-info"; 		// string: [color name as in IOColor]
	public static final String COLOR_MESSAGE_LOG 		= "logger-color-message-log"; 		// string: [color name as in IOColor]

	public static final String COLOR_EXCEPTION_GLOBAL		= "logger-color-exception-global"; 	// string: [color name as in IOColor]
	public static final String COLOR_EXCEPTION_ERROR 		= "logger-color-exception-error";  	// string: [color name as in IOColor]
	public static final String COLOR_EXCEPTION_WARNING 	= "logger-color-exception-warning";  	// string: [color name as in IOColor]
	public static final String COLOR_EXCEPTION_DEBUG 		= "logger-color-exception-debug";  	// string: [color name as in IOColor]
	public static final String COLOR_EXCEPTION_INFO 		= "logger-color-exception-info"; 		// string: [color name as in IOColor]
	public static final String COLOR_EXCEPTION_LOG 		= "logger-color-exception-log"; 		// string: [color name as in IOColor]

	public static Properties getDefaults()
	{
		Properties p = new Properties();
		p.setProperty(LoggerProperties.ACTIVE_CHANNELS, "");
		p.setProperty(LoggerProperties.ACTIVE_LEVELS, "ERROR,WARNING,DEBUG,INFO,LOG");
		p.setProperty(LoggerProperties.LEVEL, "31");
		p.setProperty(LoggerProperties.ENABLE, "true");
		p.setProperty(LoggerProperties.FILE_ENABLE, "false");
		p.setProperty(LoggerProperties.FILE_OUTPUT, "");
		p.setProperty(LoggerProperties.DATE_FORMAT, "dd/MM/yyyy, HH:mm:ss:SSS");
		p.setProperty(LoggerProperties.FORMAT_GLOBAL, "[%T] %D - %F:%L - %C.%M: ");
		p.setProperty(LoggerProperties.FORMAT_ERROR, "[%T] %D - %F:%L - %C.%M: ");
		p.setProperty(LoggerProperties.FORMAT_WARNING, "[%T] %D - %F:%L - %C.%M: ");
		p.setProperty(LoggerProperties.FORMAT_DEBUG, "[%T] %D - %F:%L - %C.%M: ");
		p.setProperty(LoggerProperties.FORMAT_INFO, "[%T] %D - %F:%L - %C.%M: ");
		p.setProperty(LoggerProperties.FORMAT_LOG, "[%T] %D - %F:%L - %C.%M: ");

		p.setProperty(LoggerProperties.COLOR_LEVEL_GLOBAL, "NOCOLOR");		
		p.setProperty(LoggerProperties.COLOR_LEVEL_ERROR, "LIGHT_RED"); 		
		p.setProperty(LoggerProperties.COLOR_LEVEL_WARNING, "YELLOW"); 		
		p.setProperty(LoggerProperties.COLOR_LEVEL_DEBUG, "LIGHT_GREEN"); 		
		p.setProperty(LoggerProperties.COLOR_LEVEL_INFO, "LIGHT_BLUE"); 		
		p.setProperty(LoggerProperties.COLOR_LEVEL_LOG, "LIGHT_CYAN"); 		

		p.setProperty(LoggerProperties.COLOR_DATE_GLOBAL, "NOCOLOR");		
		p.setProperty(LoggerProperties.COLOR_DATE_ERROR, "LIGHT_RED"); 		
		p.setProperty(LoggerProperties.COLOR_DATE_WARNING, "YELLOW"); 		
		p.setProperty(LoggerProperties.COLOR_DATE_DEBUG, "LIGHT_GREEN"); 		
		p.setProperty(LoggerProperties.COLOR_DATE_INFO, "LIGHT_BLUE"); 		
		p.setProperty(LoggerProperties.COLOR_DATE_LOG, "LIGHT_CYAN"); 			

		p.setProperty(LoggerProperties.COLOR_FILE_GLOBAL, "NOCOLOR");		
		p.setProperty(LoggerProperties.COLOR_FILE_ERROR, "LIGHT_RED"); 		
		p.setProperty(LoggerProperties.COLOR_FILE_WARNING, "YELLOW"); 		
		p.setProperty(LoggerProperties.COLOR_FILE_DEBUG, "LIGHT_GREEN"); 		
		p.setProperty(LoggerProperties.COLOR_FILE_INFO, "LIGHT_BLUE"); 		
		p.setProperty(LoggerProperties.COLOR_FILE_LOG, "LIGHT_CYAN"); 			

		p.setProperty(LoggerProperties.COLOR_LINE_GLOBAL, "NOCOLOR");		
		p.setProperty(LoggerProperties.COLOR_LINE_ERROR, "LIGHT_RED"); 		
		p.setProperty(LoggerProperties.COLOR_LINE_WARNING, "YELLOW"); 		
		p.setProperty(LoggerProperties.COLOR_LINE_DEBUG, "LIGHT_GREEN"); 		
		p.setProperty(LoggerProperties.COLOR_LINE_INFO, "LIGHT_BLUE"); 		
		p.setProperty(LoggerProperties.COLOR_LINE_LOG, "LIGHT_CYAN"); 			

		p.setProperty(LoggerProperties.COLOR_CLASS_GLOBAL, "NOCOLOR");		
		p.setProperty(LoggerProperties.COLOR_CLASS_ERROR, "LIGHT_RED"); 		
		p.setProperty(LoggerProperties.COLOR_CLASS_WARNING, "YELLOW"); 		
		p.setProperty(LoggerProperties.COLOR_CLASS_DEBUG, "LIGHT_GREEN"); 		
		p.setProperty(LoggerProperties.COLOR_CLASS_INFO, "LIGHT_BLUE"); 		
		p.setProperty(LoggerProperties.COLOR_CLASS_LOG, "LIGHT_CYAN"); 		

		p.setProperty(LoggerProperties.COLOR_METHOD_GLOBAL, "NOCOLOR");		
		p.setProperty(LoggerProperties.COLOR_METHOD_ERROR, "LIGHT_RED"); 		
		p.setProperty(LoggerProperties.COLOR_METHOD_WARNING, "YELLOW"); 		
		p.setProperty(LoggerProperties.COLOR_METHOD_DEBUG, "LIGHT_GREEN"); 		
		p.setProperty(LoggerProperties.COLOR_METHOD_INFO, "LIGHT_BLUE"); 		
		p.setProperty(LoggerProperties.COLOR_METHOD_LOG, "LIGHT_CYAN"); 		

		p.setProperty(LoggerProperties.COLOR_MESSAGE_GLOBAL, "NOCOLOR");		
		p.setProperty(LoggerProperties.COLOR_MESSAGE_ERROR, "LIGHT_RED"); 		
		p.setProperty(LoggerProperties.COLOR_MESSAGE_WARNING, "YELLOW"); 		
		p.setProperty(LoggerProperties.COLOR_MESSAGE_DEBUG, "LIGHT_GREEN"); 		
		p.setProperty(LoggerProperties.COLOR_MESSAGE_INFO, "LIGHT_BLUE"); 		
		p.setProperty(LoggerProperties.COLOR_MESSAGE_LOG, "LIGHT_CYAN"); 		

		p.setProperty(LoggerProperties.COLOR_EXCEPTION_GLOBAL, "NOCOLOR");		
		p.setProperty(LoggerProperties.COLOR_EXCEPTION_ERROR, "LIGHT_RED"); 		
		p.setProperty(LoggerProperties.COLOR_EXCEPTION_WARNING, "YELLOW"); 		
		p.setProperty(LoggerProperties.COLOR_EXCEPTION_DEBUG, "LIGHT_GREEN"); 		
		p.setProperty(LoggerProperties.COLOR_EXCEPTION_INFO, "LIGHT_BLUE"); 		
		p.setProperty(LoggerProperties.COLOR_EXCEPTION_LOG, "LIGHT_CYAN"); 		

		return p;
	}

}
