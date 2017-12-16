package com.cornchipss.oregenerator.ref;

public class Vector3 
{
	private int x, y, z;
	public Vector3(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3 add(int x, int y, int z)
	{
		return new Vector3(getX() + x, getY() + y, getZ() + z);
	}
	public Vector3 subtract(int x, int y, int z)
	{
		return add(-x, -y, -z);
	}
	
	public Vector3 multiply(int x, int y, int z)
	{
		return new Vector3(getX() * x, getY() * y, getZ() * z);
	}
	public Vector3 divide(int x, int y, int z)
	{
		return new Vector3(getX() / x, getY() / y, getZ() / z);
	}
	
	public Vector3 sign()
	{
		return new Vector3(Helper.sign(getX()), Helper.sign(getY()), Helper.sign(getZ()));
	}
	
	public int getX() {	return x; }
	public void setX(int x) { this.x = x; }
	
	public int getY() { return y; }
	public void setY(int y) { this.y = y; }
	
	public int getZ() { return z; }
	public void setZ(int z) { this.z = z; }
}
