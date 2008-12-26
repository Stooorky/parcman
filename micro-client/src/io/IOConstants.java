package io;

import java.io.Serializable;

public class IOConstants implements Serializable
{
	private static final long serialVersionUID = 42L;

	public static final String LIST_TYPE_INCREMENTAL = "INCREMENTAL";
	public static final String LIST_TYPE_SYMBOL = "SYMBOL";
	public static final String LIST_TYPE_INLINE = "INLINE";

	public static final int TABLE_ROTATE_NO = 0;
	public static final int TABLE_ROTATE_LEFT = 1;
	public static final int TABLE_ROTATE_RIGHT = 2;
	public static final int TABLE_ROTATE_BOTTOM = 3;

}
