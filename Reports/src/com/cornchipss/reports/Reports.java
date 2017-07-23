package com.cornchipss.reports;

import org.bukkit.plugin.java.JavaPlugin;

public class Reports extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		
	}
	
	@Override
	public void onDisable()
	{
		saveConfig();
	}
}