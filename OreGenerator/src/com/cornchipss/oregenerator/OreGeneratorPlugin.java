package com.cornchipss.oregenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.oregenerator.commands.CommandManager;
import com.cornchipss.oregenerator.config.Config;
import com.cornchipss.oregenerator.generators.GeneratorHandler;
import com.cornchipss.oregenerator.generators.GeneratorItemForge;
import com.cornchipss.oregenerator.listeners.CornyListener;
import com.cornchipss.oregenerator.ref.Reference;
import com.google.gson.Gson;


public class OreGeneratorPlugin extends JavaPlugin
{
	private GeneratorHandler genHandler = new GeneratorHandler();
	
	private Config cfg;
	
	private CornyListener cl;
		
	// Generator blocks 'n stuff
	private Material transmutableBlock;
	private Material[] generatorMaterials;
	
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
			saveGenerators();
		}
		catch(IOException ex)
		{
			getLogger().info("Error saving generators! This is REALLY bad!");
			ex.printStackTrace();
		}
	}
	
	private String initConfig()
	{
		cfg = new Config(this.getDataFolder() + "\\oregen-config.yml");
		
		cfg.setString(Reference.CFG_VERSION_KEY, Reference.PLUGIN_VERSION); // make sure the version is up to date (can be used in case we change the way we store things in later versions and want to convert the file to the new format)
		
		String generatorBlocks = cfg.getOrSetString(Reference.CFG_GENERATOR_MATERIALS_KEY, "COAL_BLOCK, IRON_BLOCK, REDSTONE_BLOCK, LAPIS_BLOCK, GOLD_BLOCK, DIAMOND_BLOCK, EMERALD_BLOCK");
		generatorBlocks = generatorBlocks.replaceAll(" ", "");
		String[] splitBlocks = generatorBlocks.split(",");
		
		generatorMaterials = new Material[7];
		
		if(splitBlocks.length < generatorMaterials.length)
			return "CRITICAL ERROR: Generator Materials don't store 6 values! (" + cfg.getString(Reference.CFG_GENERATOR_MATERIALS_KEY) + ")";
		
		for(int i = 0; i < splitBlocks.length; i++)
		{
			generatorMaterials[i] = Material.getMaterial(splitBlocks[i]);
			if(generatorMaterials[i] == null)
			{
				return "CRITICAL ERROR: Invalid material: '" + splitBlocks[i] + "'.  Make sure everything is written correctly; for example for coal ore: COAL_ORE";
			}
		}
		
		transmutableBlock = Material.getMaterial(cfg.getOrSetString(Reference.CFG_TRANSMUTABLE_BLOCK_KEY, "STONE"));
		if(transmutableBlock == null)
		{
			return "CRITICAL ERROR: Invalid transmutable block: '" + transmutableBlock + "'; must be a valid material, such as STONE";
		}
		
		try 
		{
			cfg.save();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return "CRITICAL ERROR: Could not save config file!";
		}
		return "Config File Successfully read";
	}
	
	private void saveGenerators() throws IOException
	{
		BufferedWriter genCfg = new BufferedWriter(new FileWriter(new File(this.getDataFolder() + "\\generator-storage.yml")));
		Gson gson = new Gson();
		
		for(int i = 0; i < genHandler.generatorAmount(); i++)
		{
			genCfg.write(gson.toJson(genHandler.getGenerator(i)));
			genCfg.newLine();
		}
		genCfg.close();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return CommandManager.runThroughCommands(command, sender, args, this);	
	}
	
	public Material getGeneratorMaterial(int genId) 
	{ 
		switch(genId)
		{
			case GeneratorItemForge.GENERATOR_COAL_ID:
				return generatorMaterials[0];
			case GeneratorItemForge.GENERATOR_IRON_ID:
				return generatorMaterials[1];
			case GeneratorItemForge.GENERATOR_REDSTONE_ID:
				return generatorMaterials[2];
			case GeneratorItemForge.GENERATOR_LAPIS_ID:
				return generatorMaterials[3];
			case GeneratorItemForge.GENERATOR_GOLD_ID:
				return generatorMaterials[4];
			case GeneratorItemForge.GENERATOR_DIAMOND_ID:
				return generatorMaterials[5];
			case GeneratorItemForge.GENERATOR_EMERALD_ID:
				return generatorMaterials[6];
			default:
				return null;
		}
	}
	
	public List<Material> getGeneratorMaterials()
	{
		List<Material> mats = new ArrayList<>();
		for(int i = 0; i < generatorMaterials.length; i++)
			mats.add(generatorMaterials[i]);
		return mats;
	}
	public Material getTransmutableBlock() { return transmutableBlock; }
	
	public GeneratorHandler getGeneratorHandler() { return this.genHandler; }
}