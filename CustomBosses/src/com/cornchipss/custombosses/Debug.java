package com.cornchipss.custombosses;

import com.cornchipss.custombosses.util.Helper;

public abstract class Debug 
{
	protected void debug(String location, String msg)
	{
		String className = this.getClass().getName();
		int index = Helper.reverseIndexOf(className, ".");
		if(index != -1)
			className = className.substring(index, className.length());
		
		System.out.println(className + "[" + location + "]: " + msg);
	}
	
	protected void debug(String location, int lineNum, String msg)
	{
		String className = this.getClass().getName();
		int index = Helper.reverseIndexOf(className, ".");
		if(index != -1)
			className = className.substring(index, className.length());
		
		System.out.println(className + "[" + location + ":" + lineNum + "]: " + msg);
	}
	
	protected void debug(String location, Object thing)
	{
		if(thing == null)
			debug(location, "null");
		else
			debug(location, thing.toString());
	}
	
	protected void debug(String location, int lineNum, Object thing)
	{
		if(thing == null)
			debug(location, lineNum, "null");
		else
			debug(location, lineNum, thing.toString());
	}
	
	protected static void debug(String className, String location, int lineNum, String msg)
	{
		System.out.println(className + "[" + location + ":" + lineNum + "]: " + msg);
	}
	
	protected static void debug(String className, String location, String msg)
	{
		System.out.println(className + "[" + location + "]: " + msg);
	}
	
	protected static void debug(String className, String location, Object thing)
	{
		if(thing == null)
			debug(className, location, "null");
		else
			debug(className, location, thing.toString());
	}
	
	protected static void debug(String className, String location, int lineNum, Object thing)
	{
		if(thing == null)
			debug(className, location, lineNum, "null");
		else
			debug(className, location, lineNum, thing.toString());
	}
}
