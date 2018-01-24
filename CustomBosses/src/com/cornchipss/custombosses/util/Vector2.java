package com.cornchipss.custombosses.util;

public class Vector2<A, B>
{
	private A minDropRate;
	private B maxDropRate;
	
	public Vector2(A x, B y)
	{
		this.minDropRate = x;
		this.maxDropRate = y;
	}
	
	public Vector2()
	{
		this.minDropRate = null;
		this.maxDropRate = null;
	}
	
	public Vector2<A, B> clone()
	{
		return new Vector2<>(this.getX(), this.getY());
	}
	
	@Override
	public String toString() { return "Vector2 [" + getX() + ", " + getY() + "]"; }
	
	public A getX() { return minDropRate; }
	public B getY() { return maxDropRate; }
	
	public void setX(A x) { this.minDropRate = x; }
	public void setY(B y) { this.maxDropRate = y; }
}
