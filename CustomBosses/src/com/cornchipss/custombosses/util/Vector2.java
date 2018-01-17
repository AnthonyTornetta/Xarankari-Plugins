package com.cornchipss.custombosses.util;

public class Vector2<A, B>
{
	private A x;
	private B y;
	
	public Vector2(A x, B y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2()
	{
		this.x = null;
		this.y = null;
	}
	
	@Override
	public String toString() { return "Vector2 [" + getX() + ", " + getY() + "]"; }
	
	public A getX() { return x; }
	public B getY() { return y; }
	
	public void setX(A x) { this.x = x; }
	public void setY(B y) { this.y = y; }
}
