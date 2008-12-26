package io;

import java.util.Properties;
import java.io.Serializable;

public class IOProperties implements Serializable 
{
	private static final long serialVersionUID = 42L;

	public static final String PROP_TAB_SPACE = "tab-space";
	public static final String PROP_COLOR_DEFAULT = "color-default";
	public static final String PROP_COLOR_INFO = "color-info";
	public static final String PROP_COLOR_DEBUG = "color-debug";
	public static final String PROP_COLOR_WARNING = "color-warning";
	public static final String PROP_COLOR_ERROR = "color-error";
	public static final String PROP_FILL_WITH = "fill-with";
	public static final String PROP_INPUT_COLOR = "input-color";

	public static final String PROP_LIST_DEFAULT_TYPE = "list-default-type";
	public static final String PROP_LIST_SYMBOL = "list-symbol";
	public static final String PROP_LIST_SEPARATOR_INCREMENTAL = "list-separator-incremental";
	public static final String PROP_LIST_SEPARATOR_SYMBOL = "list-separator-symbol";
	public static final String PROP_LIST_SEPARATOR_INLINE = "list-separator-inline";
	public static final String PROP_LIST_COLOR_CAPTION = "list-color-caption";
	public static final String PROP_LIST_COLOR_PROPERTIES = "list-color-properties";
	public static final String PROP_LIST_COLOR_SYMBOL = "list-color-symbol";
	public static final String PROP_LIST_COLOR_SEPARATOR = "list-color-separator";
	public static final String PROP_LIST_SHOW_CAPTION = "list-show-caption";
	public static final String PROP_LIST_SHOW_PROPERTIES = "list-show-properties";

	public static final String PROP_TABLE_VERTICAL_SEPARATOR = "table-vertical-separator";
	public static final String PROP_TABLE_HORIZONTAL_SEPARATOR = "table-horizontal-separator";
	public static final String PROP_TABLE_CROSS_SEPARATOR = "table-cross-separator";
	public static final String PROP_TABLE_HEAD_HORIZONTAL_SEPARATOR = "table-head-horizontal-separator";
	public static final String PROP_TABLE_COLOR_ROW_EVEN = "table-color-row-even";
	public static final String PROP_TABLE_COLOR_ROW_ODD = "table-color-row-odd";
	public static final String PROP_TABLE_COLOR_CAPTION = "table-color-caption";
	public static final String PROP_TABLE_COLOR_PROPERTIES = "table-color-properties";
	public static final String PROP_TABLE_COLOR_HEAD_ROW = "table-color-head-row";
	public static final String PROP_TABLE_SHOW_HEADER_LINE_SEPARATOR = "table-show-header-line-separator";
	public static final String PROP_TABLE_SHOW_ROW_LINE_SEPARATOR = "table-show-row-line-separator";
	public static final String PROP_TABLE_SHOW_CAPTION = "table-show-caption";
	public static final String PROP_TABLE_SHOW_PROPERTIES = "table-show-properties";

	public static final String PROP_MAP_HORIZONTAL_SEPARATOR = "map-horizontal-separator";
	public static final String PROP_MAP_VERTICAL_SEPARATOR = "map-vertical-separator";
	public static final String PROP_MAP_CROSS_SEPARATOR = "map-cross-separator";
	public static final String PROP_MAP_COLOR_LEFT = "map-color-left";
	public static final String PROP_MAP_COLOR_RIGHT = "map-color-right";
	public static final String PROP_MAP_COLOR_CAPTION = "map-color-caption";
	public static final String PROP_MAP_SHOW_LINE_SEPARATOR = "map-show-line-separator";
	public static final String PROP_MAP_SHOW_CAPTION = "map-show-caption";
	
	//public static final String PROP_ = "";

	private static Properties prop;

	public static Properties getDefault()
	{
		if (prop == null)
		{
			prop = new Properties();
			prop.setProperty(PROP_TAB_SPACE, "    ");
			prop.setProperty(PROP_COLOR_DEFAULT, IOColor.NOCOLOR);
			prop.setProperty(PROP_COLOR_INFO, IOColor.CYAN);
			prop.setProperty(PROP_COLOR_DEBUG, IOColor.BROWN);
			prop.setProperty(PROP_COLOR_WARNING, IOColor.YELLOW);
			prop.setProperty(PROP_COLOR_ERROR, IOColor.LIGHT_RED);
			prop.setProperty(PROP_FILL_WITH, " ");
			prop.setProperty(PROP_INPUT_COLOR, IOColor.WHITE);

			prop.setProperty(PROP_LIST_DEFAULT_TYPE, IOConstants.LIST_TYPE_SYMBOL);
			prop.setProperty(PROP_LIST_SYMBOL, "-");
			prop.setProperty(PROP_LIST_SEPARATOR_INCREMENTAL, ";");
			prop.setProperty(PROP_LIST_SEPARATOR_SYMBOL, ";");
			prop.setProperty(PROP_LIST_SEPARATOR_INLINE, ",");
			prop.setProperty(PROP_LIST_COLOR_CAPTION, IOColor.NOCOLOR);
			prop.setProperty(PROP_LIST_COLOR_PROPERTIES, IOColor.NOCOLOR);
			prop.setProperty(PROP_LIST_COLOR_SYMBOL, IOColor.NOCOLOR);
			prop.setProperty(PROP_LIST_COLOR_SEPARATOR, IOColor.NOCOLOR);
			prop.setProperty(PROP_LIST_SHOW_CAPTION, "true");
			prop.setProperty(PROP_LIST_SHOW_PROPERTIES, "true");

			prop.setProperty(PROP_TABLE_VERTICAL_SEPARATOR, "|");
			prop.setProperty(PROP_TABLE_HORIZONTAL_SEPARATOR, "-");
			prop.setProperty(PROP_TABLE_CROSS_SEPARATOR, "+");
			prop.setProperty(PROP_TABLE_HEAD_HORIZONTAL_SEPARATOR, "=");
			prop.setProperty(PROP_TABLE_COLOR_ROW_EVEN, IOColor.RED);
			prop.setProperty(PROP_TABLE_COLOR_ROW_ODD, IOColor.GREEN);
			prop.setProperty(PROP_TABLE_COLOR_CAPTION, IOColor.NOCOLOR);
			prop.setProperty(PROP_TABLE_COLOR_PROPERTIES, IOColor.NOCOLOR);
			prop.setProperty(PROP_TABLE_COLOR_HEAD_ROW, IOColor.NOCOLOR);
			prop.setProperty(PROP_TABLE_SHOW_HEADER_LINE_SEPARATOR, "true");
			prop.setProperty(PROP_TABLE_SHOW_ROW_LINE_SEPARATOR, "true");
			prop.setProperty(PROP_TABLE_SHOW_CAPTION, "true");
			prop.setProperty(PROP_TABLE_SHOW_PROPERTIES, "true");

			prop.setProperty(PROP_MAP_HORIZONTAL_SEPARATOR, "-");
			prop.setProperty(PROP_MAP_VERTICAL_SEPARATOR, "|");
			prop.setProperty(PROP_MAP_CROSS_SEPARATOR, "+");
			prop.setProperty(PROP_MAP_COLOR_LEFT, IOColor.WHITE);
			prop.setProperty(PROP_MAP_COLOR_RIGHT, IOColor.NOCOLOR);
			prop.setProperty(PROP_MAP_COLOR_CAPTION, IOColor.NOCOLOR);
			prop.setProperty(PROP_MAP_SHOW_LINE_SEPARATOR, "true");
			prop.setProperty(PROP_MAP_SHOW_CAPTION, "true");
		}

		return prop;
	}
}
