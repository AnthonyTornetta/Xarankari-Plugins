package com.cornchipss.main;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyManager extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		loadConfig();
	}

	@Override
	public void onDisable()
	{
		
	}
	
	/**
	 * Loads the config file and sets it up if it doesn't exist
	 */
	private void loadConfig() 
	{
		FileConfiguration cfg = getConfig();
		
	}
}
