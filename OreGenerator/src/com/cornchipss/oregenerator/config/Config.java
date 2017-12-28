package com.cornchipss.oregenerator.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.cornchipss.oregenerator.ref.Helper;

public class Config
{
	// A bad value used if a key is not found in a get call
	public static final int BAD_VALUE = -9999;
	
	// The file to read and write to
	private File configFile;
	
	// Stores each line in the file; use this to store changes and this will be saved to the actual file once save() is called
	List<String> lines = new ArrayList<>();
	
	/**
	 * Reads and rights to a config file using the following format:
	 * <br>
	 * [key]: [data]
	 * <br>
	 * The config file will be created if it doesn't exist
	 * <br>
	 * The file saving format is of the .yml file type, thus files should be saved with a .yml extension, but you can make the extension anything you want
	 * @param cfgFile The file to read/write from
	 */
	public Config(String path)
	{
		try
		{
			configFile = new File(path);
			
			configFile.getParentFile().mkdirs();
			configFile.createNewFile();
			
		    InputStream fis = new FileInputStream(configFile);
		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(isr);
		    
		    // Read all the lines and put them into the array list
		    for(String line = br.readLine(); line != null; line = br.readLine())
		    {
		        lines.add(line);
		    }
		    br.close();
		} 
		catch (FileNotFoundException ex) 
		{
			ex.printStackTrace();
		} 
		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Checks if the configuration file contains any text
	 * @return True if all lines are empty (using String.isEmpty()) or false if one of the lines is not empty
	 */
	public boolean isEmpty()
	{
		for(String s : lines)
			if(!s.isEmpty())
				return false;
		return true;
	}
	
	/**
	 * Sets all the text in the configuration file to a specified List
	 * @param text The text to set it to; each row is equivilant to a new line
	 */
	public void setText(List<String> text)
	{
		this.lines = text;
	}
	
	/**
	 * Gets the comment out of the specified string
	 * @param line String to extract comment from
	 * @return The comment found
	 */
	public String getComment(String line)
	{
		boolean marking = false;
		@SuppressWarnings("unused")
		String comment = "";
		for(char c : line.toCharArray())
		{
			if(c == '#')
				marking = true;
			if(marking)
				comment += c;
		}
		
		return "";//comment;
	}
	
	/**
	 * If the String contains a space at the end, it cuts it off
	 * @param txt The String to check for the space
	 * @return The String without a space at the end
	 */
	public String removeTrailingSpace(final String txt)
	{
		return txt;
		/*
		if(txt == null)
			return null;
		if(txt.length() >= 1)
		{
			if(txt.substring(txt.length() - 1).equals(" "))
				return txt.substring(0, txt.length() - 1);
			return txt;
		}
		return "";*/
	}
	
	/**
	 * Removes the comment (marked by a starting '#') from a String
	 * @param txt The text to remove the comment from
	 * @return The String with no comments
	 */
	public String stripComment(final String txt)
	{
		return txt;
		/*
		int indexOf = txt.indexOf("#");
		if(indexOf == -1)
			return txt;
		String commentRemoved = txt.substring(0, indexOf);
		commentRemoved = removeTrailingSpace(commentRemoved);
		return commentRemoved;*/
	}
	
	/**
	 * Gets a specified value based off the key, or if the value doesn't exist sets it to a specified default value and returns that value
	 * @param key The key to get the value of
	 * @param defaultValue The value to set it to if it doesn't exist
	 * @return The value at the given key or the default value if the key doesn't exist
	 */
	public int getOrSetInt(String key, int defaultValue)
	{
		if(!this.containsKey(key) || getInt(key) == BAD_VALUE)
		{
			setInt(key, defaultValue);
		}
		
		return getInt(key);
	}
	
	/**
	 * Gets a specified value based off the key, or if the value doesn't exist sets it to a specified default value and returns that value
	 * @param key The key to get the value of
	 * @param defaultValue The value to set it to if it doesn't exist
	 * @return The value at the given key or the default value if the key doesn't exist
	 */
	public int[] getOrSetIntArray(String key, int[] defaultValue) 
	{
		if(!this.containsKey(key) || getIntArray(key) == null)
		{
			setIntArray(key, defaultValue);
		}
		
		return getIntArray(key);
	}

	/**
	 * Gets a specified value based off the key, or if the value doesn't exist sets it to a specified default value and returns that value
	 * @param key The key to get the value of
	 * @param defaultValue The value to set it to if it doesn't exist
	 * @return The value at the given key or the default value if the key doesn't exist
	 */
	public double getOrSetDouble(String key, double defaultValue)
	{
		if(!this.containsKey(key) || getDouble(key) == BAD_VALUE)
		{
			setDouble(key, defaultValue);
		}
		
		return getDouble(key);
	}
	
	/**
	 * Gets a specified value based off the key, or if the value doesn't exist sets it to a specified default value and returns that value
	 * @param key The key to get the value of
	 * @param defaultValue The value to set it to if it doesn't exist
	 * @return The value at the given key or the default value if the key doesn't exist
	 */
	public double[] getOrSetDoubleArray(String key, double[] defaultValue) 
	{
		if(!this.containsKey(key) || getDoubleArray(key) == null)
		{
			setDoubleArray(key, defaultValue);
		}
		
		return getDoubleArray(key);
	}
	
	/**
	 * Gets a specified value based off the key, or if the value doesn't exist sets it to a specified default value and returns that value
	 * @param key The key to get the value of
	 * @param defaultValue The value to set it to if it doesn't exist
	 * @return The value at the given key or the default value if the key doesn't exist
	 */
	public String getOrSetString(String key, String defaultValue)
	{
		if(!this.containsKey(key) || getString(key) == null)
		{
			setString(key, defaultValue);
		}
		
		return getString(key);
	}
	
	/**
	 * Gets a specified value based off the key, or if the value doesn't exist sets it to a specified default value and returns that value
	 * @param key The key to get the value of
	 * @param defaultValue The value to set it to if it doesn't exist
	 * @return The value at the given key or the default value if the key doesn't exist
	 */
	public String[] getOrSetStringArray(final String key, final String[] defaultValue) 
	{
		if(!this.containsKey(key) || getStringArray(key) == null)
		{
			setStringArray(key, defaultValue);
		}
		
		return getStringArray(key);
	}
	
	/**
	 * Sets an integer value at a given key
	 * @param key The key to set the value at
	 * @param integer The value the integer will have
	 */
	public void setInt(final String key, final int integer)
	{
		String newValue = key + ": " + integer;
		for(int i = 0; i < lines.size(); i++)
		{
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
			{
				String comment = "";
				if(split.length > 1)
					comment = getComment(split[1]);
				
				lines.set(i, newValue + " " + comment);
				return;
			}
		}
		lines.add(newValue);
	}
	
	/**
	 * Sets an integer array value at a given key
	 * @param key The key to set the value at
	 * @param arr The values the integer array will have
	 */
	public void setIntArray(final String key, final int[] arr) 
	{
		String newValue = key + ": ";
		for(int i = 0; i < arr.length; i++)
		{
			if(i == arr.length - 1)
				newValue += arr[i] + "";
			else
				newValue += arr[i] + ", ";
		}
		
		for(int i = 0; i < lines.size(); i++)
		{
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
			{
				lines.set(i, newValue);
				return;
			}
		}
		lines.add(newValue);
	}
	
	/**
	 * Sets a double value at a given key
	 * @param key The key to set the value at
	 * @param d The value the double will have
	 */
	public void setDouble(final String key, double d)
	{
		String newValue = key + ": " + d;
		for(int i = 0; i < lines.size(); i++)
		{
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
			{
				String comment = getComment(lines.get(i));
				lines.set(i, newValue + " " + comment);
				return;
			}
		}
		lines.add(newValue);
	}
	
	/**
	 * Sets a double array value at a given key
	 * @param key The key to set the value at
	 * @param arr The values the double array will have
	 */
	public void setDoubleArray(String key, double[] arr) 
	{
		String newValue = key + ": ";
		for(int i = 0; i < arr.length; i++)
		{
			if(i == arr.length - 1)
				newValue += arr[i] + "";
			else
				newValue += arr[i] + ", ";
		}
		
		for(int i = 0; i < lines.size(); i++)
		{
			if(stripComment(lines.get(i)).isEmpty())
				continue;
			
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
			{
				String comment = getComment(lines.get(i));
				lines.set(i, newValue + " " + comment);
				return;
			}
		}
		lines.add(newValue);
	}
	
	/**
	 * Sets a String value at a given key
	 * @param key The key to set the value at
	 * @param str The value the String will have
	 */
	public void setString(String key, String str)
	{
		String newValue;
		if(str == null)
			newValue = key + ": null";
		else
			newValue = key + ": " + str;
		
		for(int i = 0; i < lines.size(); i++)
		{
			if(stripComment(lines.get(i)).isEmpty())
				continue;
			
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
			{
				String comment = getComment(lines.get(i));
				lines.set(i, newValue + " " + comment);
				return;
			}
		}
		lines.add(newValue);
	}

	/**
	 * Sets a String array value at a given key
	 * @param key The key to set the value at
	 * @param arr The values the String array will have
	 */
	public void setStringArray(String key, String[] arr) 
	{
		String newValue = key + ": ";
		for(int i = 0; i < arr.length; i++)
		{
			if(i == arr.length - 1)
				newValue += arr[i] + "";
			else
				newValue += arr[i] + ", ";
		}
		
		for(int i = 0; i < lines.size(); i++)
		{
			if(stripComment(lines.get(i)).isEmpty())
				continue;
			
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
			{
				String comment = getComment(lines.get(i));
				lines.set(i, newValue + " " + comment);
				return;
			}
		}
		lines.add(newValue);
	}
	
	/**
	 * Checks if the configuration file contains a key
	 * @param key The key to check if it exists
	 * @return True if it exists; False if it doesn't
	 */
	public boolean containsKey(String key)
	{
		for(int i = 0; i < lines.size(); i++)
		{
			if(stripComment(lines.get(i)).isEmpty())
				continue;
			
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
				return true;
		}
		return false;
	}
	
	/**
	 * Gets a String value from the config file<br>Returns BAD_VALUE if the key isn't found
	 * @param key The key the value is stored at
	 * @return The String that was found or BAD_VALUE if it wasn't found
	 */
	public String getString(String key)
	{
		for(int i = 0; i < lines.size(); i++)
		{
			if(stripComment(lines.get(i)).isEmpty())
				continue;
			
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
			{
				if(split.length > 1)
				{
					String strToReturn = "";
					for(int j = 1; j < split.length; j++)
					{
						strToReturn += split[j];
					}
					return stripComment(strToReturn);
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets a String array value from the config file<br>Returns null if the key isn't found
	 * @param key The key the value is stored at
	 * @return The String that was found or null if it wasn't found
	 */
	public String[] getStringArray(String key) 
	{
		for(int i = 0; i < lines.size(); i++)
		{
			if(stripComment(lines.get(i)).isEmpty())
				continue;
			
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
			{
				if(split.length > 1)
				{
					String[] stringsSplit = stripComment(split[1]).split(", ");
					return stringsSplit;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Gets a integer value from the config file<br>Returns BAD_VALUE if the key isn't found
	 * @param key The key the value is stored at
	 * @return The integer that was found or BAD_VALUE if it wasn't found
	 */
	public int getInt(String key)
	{
		for(int i = 0; i < lines.size(); i++)
		{
			if(stripComment(lines.get(i)).isEmpty())
				continue;
			
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
			{
				if(split.length > 1)
				{
					String possibleInt = stripComment(split[1]);
					if(Helper.isInt(possibleInt))
					{
						return Integer.parseInt(possibleInt);
					}
				}
			}
		}
		return BAD_VALUE;
	}
	
	/**
	 * Gets an integer array value from the config file<br>Returns null if the key isn't found
	 * @param key The key the value is stored at
	 * @return The integer that was found or null if it wasn't found
	 */
	public int[] getIntArray(String key) 
	{
		lineSearch:
		for(int i = 0; i < lines.size(); i++)
		{
			if(stripComment(lines.get(i)).isEmpty())
				continue;
			
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
			{   
				if(split.length > 1)
				{
					String possibleInts = split[1];
					possibleInts = possibleInts.replaceAll(" ", "");
					String[] intsSplit = stripComment(possibleInts).split(",");
					int[] intArray = new int[intsSplit.length];
					for(int j = 0; j < intsSplit.length; j++)
					{
						if(!Helper.isInt(intsSplit[j]))
							continue lineSearch;
						intArray[j] = Integer.parseInt(intsSplit[j]);
					}
					return intArray;
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets a double value from the config file<br>Returns BAD_VALUE if the key isn't found
	 * @param key The key the value is stored at
	 * @return The double that was found or BAD_VALUE if it wasn't found
	 */
	public double getDouble(String key)
	{
		for(int i = 0; i < lines.size(); i++)
		{
			if(stripComment(lines.get(i)).isEmpty())
				continue;
			
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
			{
				if(split.length > 1)
				{
					String possibleInt = stripComment(split[1]);
					if(Helper.isDouble(possibleInt))
					{
						return Double.parseDouble(possibleInt);
					}
				}
			}
		}
		return BAD_VALUE;
	}
	
	/**
	 * Gets a double array value from the config file<br>Returns null if the key isn't found
	 * @param key The key the value is stored at
	 * @return The double that was found or null if it wasn't found
	 */
	public double[] getDoubleArray(String key) 
	{
		lineSearch:
		for(int i = 0; i < lines.size(); i++)
		{
			if(stripComment(lines.get(i)).isEmpty())
				continue;
			
			String[] split = lines.get(i).split(": ");
			if(split[0].equalsIgnoreCase(key))
			{
				if(split.length > 1)
				{
					String possibleDoubles = split[1];
					possibleDoubles = possibleDoubles.replaceAll(" ", "");
					String[] dsSplit = stripComment(possibleDoubles).split(",");
					double[] dArray = new double[dsSplit.length];
					for(int j = 0; j < dsSplit.length; j++)
					{
						if(!Helper.isDouble(dsSplit[j]))
							continue lineSearch;
						dArray[j] = Double.parseDouble(dsSplit[j]);
					}
					return dArray;
				}
			}
		}
		return null;
	}
	
	/**
	 * Saves the configuration file
	 * <br>
	 * Note: The config file can be saved multiple times, and there is no need to close it, just call this method to save it
	 * @throws IOException If there was some sort of error saving the file
	 */
	public void save() throws IOException
	{
		configFile.createNewFile();
		PrintWriter pw = new PrintWriter(configFile);
				
		pw.write("");
		for(int i = 0; i < lines.size(); i++)
		{
			String curLine = lines.get(i);
			pw.append(curLine);
			
			pw.append('\n');
		}
		pw.close();
	}
}
