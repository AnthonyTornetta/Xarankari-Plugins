package com.cornchipss.buildbattlesmini.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.cornchipss.buildbattlesmini.Reference;

public class Config 
{
	public static final int BAD_VAL = -999;
		
	private List<String> lines = new ArrayList<>();
	private File file;
	
	public Config(String defaultHeader, String path)
	{
		file = new File(path);
		
		try
		{
			if(file.createNewFile()) // Creates a file if it doesn't exist
			{
				lines.add(defaultHeader);
				save();
			}
			else
			{
				reloadLines();
			}
		}
		catch (IOException e) 
		{
			Bukkit.getLogger().info("Error reading the config file! (" + path + ")");
			e.printStackTrace();
		}
	}
	
	public int getInt(String key)
	{
		key = key.toLowerCase();
		
		for(int i = 0; i < lines.size(); i++)
		{
			String line = lines.get(i);
			
			line = stripComments(line);
			if(line.equals(""))
				continue;
			
			String[] split = line.split(":");
			split[0] = split[0].toLowerCase();
			
			if(split.length < 2)
			{
				continue;
			}
			
			if(split[0].equals(key))
			{
				if(split[1].charAt(0) == ' ')
				{
					split[1] = split[1].substring(1); // Remove the space at the start
				}
				
				if(Reference.isInt(split[1]))
				{
					return Integer.parseInt(split[1]);
				}
			}
		}
		return BAD_VAL;
	}
	
	public double getDouble(String key)
	{
		key = key.toLowerCase();
		
		for(int i = 0; i < lines.size(); i++)
		{
			String line = lines.get(i);
			
			line = stripComments(line);
			if(line.equals(""))
				continue;
			
			String[] split = line.split(":");
			split[0] = split[0].toLowerCase();
			
			if(split.length < 2)
			{
				continue;
			}
			
			if(split[0].equals(key))
			{
				if(split[1].charAt(0) == ' ')
				{
					split[1] = split[1].substring(1); // Remove the space at the start
				}
				
				if(Reference.isDouble(split[1]))
				{
					return Double.parseDouble(split[1]);
				}
			}
		}
		return BAD_VAL;
	}
	
	public String getString(String key)
	{
		key = key.toLowerCase();
		
		for(int i = 0; i < lines.size(); i++)
		{
			String line = lines.get(i);
			
			line = stripComments(line);
			if(line.equals(""))
				continue;
			
			String[] split = line.split(":");
			split[0] = split[0].toLowerCase();
			
			if(split.length < 2)
			{
				continue;
			}
			
			if(split[0].equals(key))
			{
				if(split[1].charAt(0) == ' ')
				{
					split[1] = split[1].substring(1); // Remove the space at the start
				}
				return split[1];
			}
		}
		return null;
	}
	
	public void setInt(String key, int val)
	{
		key = key.toLowerCase();
		
		for(int i = 0; i < lines.size(); i++)
		{
			String line = stripComments(lines.get(i));
			String[] split = line.split(":");
			if(split[0].equals(key))
			{
				lines.set(i, key + ": " + val);
				break;
			}
		}
		lines.add(key + ": " + val);
	}
	
	public void setInt(String comment, String key, int val)
	{
		key = key.toLowerCase();
		
		for(int i = 0; i < lines.size(); i++)
		{
			String line = stripComments(lines.get(i));
			String[] split = line.split(":");
			if(split[0].equals(key))
			{
				lines.set(i, "# " + comment + "\n" + key + ": " + val);
				break;
			}
		}
		lines.add("# " + comment + "\n" + key + ": " + val);
	}
	
	public void setDouble(String key, double val)
	{
		key = key.toLowerCase();
		
		for(int i = 0; i < lines.size(); i++)
		{
			String line = stripComments(lines.get(i));
			String[] split = line.split(":");
			if(split[0].equals(key))
			{
				lines.set(i, key + ": " + val);
				break;
			}
		}
		lines.add(key + ": " + val);
	}
	
	public void setDouble(String comment, String key, double val)
	{
		key = key.toLowerCase();
		
		for(int i = 0; i < lines.size(); i++)
		{
			String line = stripComments(lines.get(i));
			String[] split = line.split(":");
			if(split[0].equals(key))
			{
				lines.set(i, "# " + comment + "\n" + key + ": " + val);
			}
		}
	}
	
	public void setString(String key, String val)
	{
		key = key.toLowerCase();
		
		for(int i = 0; i < lines.size(); i++)
		{
			String line = stripComments(lines.get(i));
			String[] split = line.split(":");
			if(split[0].equals(key))
			{
				lines.set(i, key + ": " + val);
				break;
			}
		}
		lines.add(key + ": " + val);
	}
	
	public void setString(String comment, String key, String val)
	{
		key = key.toLowerCase();
		
		for(int i = 0; i < lines.size(); i++)
		{
			String line = stripComments(lines.get(i));
			String[] split = line.split(":");
			if(split[0].equals(key))
			{
				lines.set(i, "# " + comment + "\n" + key + ": " + val);
				break;
			}
		}
		
		lines.add("# " + comment + "\n" + key + ": " + val);
	}
	
	private String stripComments(String line)
	{
		if(line == null)
			return null;
		return line.split("#")[0];
	}
	
	public boolean save()
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			bw.write("");
			for(int i = 0; i < lines.size(); i++)
			{
				String line = lines.get(i) + "\n";
				bw.append(line);
			}
			
			bw.close();
			
			reloadLines();
			return true;
		}
		catch(IOException ex)
		{
			Bukkit.getLogger().info("Error saving config file for Build Battles!");
			ex.printStackTrace();
			return false;
		}
	}
	
	private void reloadLines()
	{
		lines.clear();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			for(String line = br.readLine(); line != null; line = br.readLine())
			{
				lines.add(line);
			}
			
			br.close();
		}
		catch(IOException ex)
		{
			Bukkit.getLogger().info("Error reading Build Battle config file " + file.getName() + "!");
			ex.printStackTrace();
		}
	}
}
