package com.cornchipss.RPG;

import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class RPG extends JavaPlugin
{
	PermissionsEx pex;
	@Override
	public void onEnable()
	{
		loadConfig();
		pex = (PermissionsEx) getServer().getPluginManager().getPlugin("PermissionsEx");
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	private void loadConfig()
	{
		
	}
	
	public PermissionsEx getPex() { return pex; }
}
