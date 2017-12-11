package com.cornchipss.oregenerator;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.oregenerator.commands.CommandManager;
import com.cornchipss.oregenerator.config.Config;
import com.cornchipss.oregenerator.listeners.CornyListener;
import com.cornchipss.oregenerator.ref.Reference;


public class OreGeneratorPlugin extends JavaPlugin
{
	private Config cfg;
	
	private Material generatorMaterial;
	private CornyListener cl;
	
	@Override
	public void onEnable()
	{		
		String logMe = initConfig();
		
		getLogger().info(logMe);
		
		if(logMe.contains("CRITICAL ERROR"))
		{
			getLogger().info(ChatColor.RED + "Shutting down due to a critical error when reading the config!");
			return;
		}
		
		cl = new CornyListener(this);
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(cl, this);
	}
	
	@Override
	public void onDisable()
	{
		try
		{
			cfg.save();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private String initConfig()
	{
		cfg = new Config(this.getDataFolder() + "\\oregen-config.yml");
		
		if(!cfg.containsKey(Reference.CFG_VERSION_KEY))
		{
			cfg.setString(Reference.CFG_VERSION_KEY, Reference.PLUGIN_VERSION);
		}
		
		if(!cfg.containsKey(Reference.CFG_GENERATOR_BLOCK_KEY))
		{
			cfg.setString(Reference.CFG_GENERATOR_BLOCK_KEY, "BEDROCK");
		}
		
		generatorMaterial = Material.getMaterial(cfg.getString(Reference.CFG_GENERATOR_BLOCK_KEY));
		
		if(generatorMaterial == null)
		{
			return ChatColor.RED + "CRICICAL ERROR: Generator Material is null! (" + cfg.getString(Reference.CFG_GENERATOR_BLOCK_KEY) + ")";
		}
		
		return ChatColor.GREEN + "Config File Successfully read";
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return CommandManager.runThroughCommands(command, sender, args, this);	
	}
	
	public Material getGeneratorMaterial() { return generatorMaterial; }
}
