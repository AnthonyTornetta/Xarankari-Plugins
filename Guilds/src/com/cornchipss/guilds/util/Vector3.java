package com.cornchipss.guilds.util;

public class Vector3<A, B, C> extends Vector2<A, B>
{
	private C z;
	
	public Vector3(A x, B y, C z)
	{
		super(x, y);
		this.z = z;
	}
	
	public Vector3()
	{
		super();
		this.z = null;
	}

	public C getZ() { return z; }
	public void setZ(C z) { this.z = z; }
}
