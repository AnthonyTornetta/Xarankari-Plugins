package com.cornchipss.maprenderer;

import org.bukkit.plugin.java.JavaPlugin;

public class AutoRenderer extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new CornyListener(), this);
	}
}
