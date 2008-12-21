package io;

import java.io.Serializable;

public enum LoggerLevel implements Serializable
{
	 ERROR (0, 1),
	 WARNING (1, 2), 
	 DEBUG (2, 4), 
	 INFO (3, 8), 
	 LOG (4, 16);

	 private int position;
	 private int level;

	 LoggerLevel(int position, int level)
	 {
		this.position = position;
		this.level = level;
	 }
	 public int position() { return this.position; }
	 public int level() { return this.level; }
}
