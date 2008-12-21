package io;

import java.io.Serializable;

class Pair<E,F> implements Serializable
{
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
