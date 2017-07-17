package com.cornchipss.rpg;

import org.bukkit.plugin.java.JavaPlugin;

public class RPG extends JavaPlugin
{
	CornyListener cl;
	@Override
	public void onEnable()
	{
		cl = new CornyListener(this);
		loadConfig();
		
		getServer().getPluginManager().registerEvents(cl, this);
	}
	
	@Override
	public void onDisable()
	{
		cl.regenAllBlocks();
	}
	
	private void loadConfig()
	{
		
	}
}
