package com.cornchipss.custombosses;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.custombosses.config.Config;
import com.cornchipss.custombosses.config.JSONConfig;

public class CustomBosses extends JavaPlugin
{
	private Config mainCfg;
	private JSONConfig bossesCfg;
	
	@Override
	public void onEnable()
	{
		try
		{			
			getLogger().info("Custom Bosses Enabling...");
			
			mainCfg = new Config(this.getDataFolder() + "\\test.json");
			mainCfg.setInt("ASDF", 123);
			mainCfg.save();

			System.out.println(mapper.writeValueAsString(mainCfg));
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void onDisable()
	{
		
	}
}
