package com.cornchipss.oregenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.oregenerator.commands.CommandManager;
import com.cornchipss.oregenerator.config.Config;
import com.cornchipss.oregenerator.generators.GeneratorHandler;
import com.cornchipss.oregenerator.generators.GeneratorUtils;
import com.cornchipss.oregenerator.listeners.CornyListener;
import com.cornchipss.oregenerator.ref.Reference;
import com.cornchipss.oregenerator.upgrades.UpgradeUtils;

import net.milkbowl.vault.economy.Economy;


public class OreGeneratorPlugin extends JavaPlugin
{
	private GeneratorHandler genHandler = new GeneratorHandler();
	
	private Config cfg;
	
	private CornyListener cl;
		
	// Generator blocks 'n stuff
	private Material transmutableBlock;
	private Material[] generatorMaterials;
	private int[] timesBetween;
	private double[] generatorPrices;
	
	// Upgrades
	private Material[] upgradeMaterials;
	private double[] upgradePrices;
	private int maxUpgrades;
	
	// Vault stuff
	Economy eco = null;
	
	@Override
	public void onEnable()
	{
		String logMe = initConfig();
		
		if (!setupEconomy()) 
		{
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
				
		try
		{
			readGenerators(genHandler);
		}
		catch(IOException ex)
		{
			try
			{
				saveGenerators(); // Probs just hasn't been made yet, so write a blank file (since there are no generators to worry about)
			}
			catch(IOException exx)
			{
				getLogger().info("CRITICAL ERROR: Could not read generators storage file - Disabling!");
			}
		}
		
		getLogger().info(logMe);
		
		if(logMe.contains("CRITICAL ERROR"))
		{
			getLogger().info("Disabling due to a critical error when reading the config!");
            getServer().getPluginManager().disablePlugin(this);
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
		
		if(cfg.isEmpty())
		{
			List<String> txt = new ArrayList<>();
			txt.add("# The version of the plugin - please do not manually change");
			txt.add(Reference.CFG_VERSION_KEY + ": ");
			txt.add("");
			txt.add("##################");
			txt.add("### Generators ###");
			txt.add("##################");
			txt.add("");
			txt.add("# Materials to be used for the generators");
			txt.add(Reference.CFG_GENERATOR_MATERIALS_KEY + ": ");
			txt.add("# The time it takes w/out any upgrades for each generator to transmute");
			txt.add(Reference.CFG_GENERATOR_TIME_BETWEEN_TRANSMUTES + ": ");
			txt.add("# The block that will be turned into the ore");
			txt.add(Reference.CFG_TRANSMUTABLE_BLOCK_KEY + ": ");
			txt.add("# The prices each generator will cost");
			txt.add(Reference.CFG_GENERATOR_PRICES_KEY + ": ");
			txt.add("");
			txt.add("################");
			txt.add("### Upgrades ###");
			txt.add("################");
			txt.add("");
			txt.add("# Upgrade Materials");
			txt.add(Reference.CFG_UPGRADE_MATERIALS + ": ");
			txt.add("# Upgrade Prices");
			txt.add(Reference.CFG_UPGRADE_PRICES_KEY + ": ");
			txt.add("# The max amounts of upgrades a generator can have");
			txt.add(Reference.CFG_MAX_UPGRADES_KEY + ": ");
			cfg.setText(txt);
			
			try
			{
				cfg.save();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				return "CRITICAL ERROR: Could not save config file!";
			}
		}
		
		cfg.setString(Reference.CFG_VERSION_KEY, Reference.PLUGIN_VERSION); // make sure the version is up to date (can be used in case we change the way we store things in later versions and want to convert the file to the new format)
		
		
		/////////////////////////
		//// Generator Setup ////
		/////////////////////////
		
		String[] defualtGeneratorMaterials = { "COAL_BLOCK", "IRON_BLOCK", "REDSTONE_BLOCK", "LAPIS_BLOCK", "GOLD_BLOCK", "DIAMOND_BLOCK", "EMERALD_BLOCK" };
		String[] generatorMaterialsConfig = cfg.getOrSetStringArray(Reference.CFG_GENERATOR_MATERIALS_KEY, defualtGeneratorMaterials);
		
		int[] defaultTimesBetweenTransmutes = {10, 20, 30, 40, 50, 60, 70};
		timesBetween = cfg.getOrSetIntArray(Reference.CFG_GENERATOR_TIME_BETWEEN_TRANSMUTES, defaultTimesBetweenTransmutes);
		
		generatorMaterials = new Material[GeneratorUtils.MAX_GENERATOR_ID + 1];
		
		if(generatorMaterialsConfig.length < generatorMaterials.length)
			cfg.setStringArray(Reference.CFG_GENERATOR_MATERIALS_KEY, generatorMaterialsConfig); // Probs updated and have more gens now
		if(timesBetween.length != generatorMaterialsConfig.length)
			cfg.setStringArray(Reference.CFG_GENERATOR_TIME_BETWEEN_TRANSMUTES, defualtGeneratorMaterials); // Probs updated and have more gens now		
		
		// Parse each material read in and put it in the generator materials array
		for(int i = 0; i < generatorMaterialsConfig.length; i++)
		{
			generatorMaterials[i] = Material.getMaterial(generatorMaterialsConfig[i]);
			if(generatorMaterials[i] == null)
			{
				return "CRITICAL ERROR: Invalid material: '" + generatorMaterialsConfig[i] + "'.  Make sure everything is written correctly; for example for a coal block: COAL_BLOCK";
			}
		}
		
		transmutableBlock = Material.getMaterial(cfg.getOrSetString(Reference.CFG_TRANSMUTABLE_BLOCK_KEY, "STONE"));
		if(transmutableBlock == null)
		{
			return "CRITICAL ERROR: Invalid transmutable block: '" + transmutableBlock + "'; must be a valid material, such as STONE";
		}
		
		double[] defaultGeneratorPrices = { 10000.0, 20000.0, 40000.0, 80000.0, 160000.0, 320000.0, 640000.0 };
		double[] generatorPricesConfig = cfg.getOrSetDoubleArray(Reference.CFG_GENERATOR_PRICES_KEY, defaultGeneratorPrices);
		generatorPrices = new double[generatorMaterials.length];
		if(generatorPricesConfig.length != generatorPrices.length)
			cfg.setDoubleArray(Reference.CFG_GENERATOR_PRICES_KEY, defaultGeneratorPrices);
		
		generatorPrices = cfg.getDoubleArray(Reference.CFG_GENERATOR_PRICES_KEY);
		
		////////////////////////
		//// Upgrades Setup ////
		////////////////////////
		
		String[] defaultUpgradeMaterials = { "SUGAR, RECORD_10, RECORD_9, RECORD_8" };
		String[] upgradeMaterialsConfig = cfg.getOrSetStringArray(Reference.CFG_UPGRADE_MATERIALS, defaultUpgradeMaterials);

		upgradeMaterials = new Material[UpgradeUtils.MAX_UPGRADE_ID + 1];
		
		if(upgradeMaterialsConfig.length != upgradeMaterials.length)
		{
			cfg.setStringArray(Reference.CFG_UPGRADE_MATERIALS, defaultUpgradeMaterials);
		}
		upgradeMaterials = new Material[upgradeMaterials.length];
		for(int i = 0; i < upgradeMaterialsConfig.length; i++)
		{
			upgradeMaterials[i] = Material.getMaterial(upgradeMaterialsConfig[i]);
			if(upgradeMaterials[i] == null)
			{
				return "CRITICAL ERROR: Invalid material: '" + upgradeMaterials[i] + "'.  Make sure everything is written correctly; for example for a coal block: COAL_BLOCK";
			}
		}
		
		double[] defaultUpgradePrices = { 10000.0, 10000.0, 10000.0, 10000.0, 10000.0 };
		double[] upgradePricesConfig = cfg.getOrSetDoubleArray(Reference.CFG_UPGRADE_PRICES_KEY, defaultUpgradePrices);
		upgradePrices = new double[upgradeMaterials.length];
		if(upgradePricesConfig.length != generatorPrices.length)
			cfg.setDoubleArray(Reference.CFG_UPGRADE_PRICES_KEY, defaultUpgradePrices);
		
		upgradePrices = cfg.getDoubleArray(Reference.CFG_UPGRADE_PRICES_KEY);
		
		maxUpgrades = cfg.getOrSetInt(Reference.CFG_MAX_UPGRADES_KEY, 10);
		
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
		BufferedWriter genCfg = new BufferedWriter(new FileWriter(new File(this.getDataFolder() + "\\generator-storage.gen")));
		
		for(int i = 0; i < genHandler.generatorAmount(); i++)
		{
			genCfg.write(GeneratorUtils.serialize(genHandler.getGenerator(i)));
			genCfg.newLine();
		}
		genCfg.close();
	}
	
	private void readGenerators(GeneratorHandler genHandler) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(new File(this.getDataFolder() + "\\generator-storage.gen")));
		for(String s = br.readLine(); s != null; s = br.readLine())
		{
			if(!s.isEmpty())
			{
				genHandler.addGenerator(GeneratorUtils.deserialize(s, this));
			}
		}
		br.close();
	}
	
	private boolean setupEconomy() 
	{
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return CommandManager.runThroughCommands(command, sender, args, this);	
	}
	
	public Material getGeneratorMaterial(int genId) { return generatorMaterials[genId]; }
	public int getGeneratorTimeBetween(int genId)   { return timesBetween[genId]; }
	public double getGeneratorPrice(int genId)      { return generatorPrices[genId]; }
	
	public Material getUpgradeMaterial(int upId) { return upgradeMaterials[upId]; }
	public double getUpgradePrice(int upId)      { return upgradePrices[upId]; }
	
	public List<Material> getGeneratorMaterials()
	{
		List<Material> mats = new ArrayList<>();
		for(int i = 0; i < generatorMaterials.length; i++)
			mats.add(generatorMaterials[i]);
		return mats;
	}
	
	public Material getTransmutableBlock() { return transmutableBlock; }
	
	public GeneratorHandler getGeneratorHandler() { return this.genHandler; }

	public int getMaxUpgrades() { return maxUpgrades; }
	
	public Economy getEco() { return this.eco; }

	
}
