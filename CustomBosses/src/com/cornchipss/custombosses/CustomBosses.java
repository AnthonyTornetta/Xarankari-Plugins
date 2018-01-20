package com.cornchipss.custombosses;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.custombosses.boss.handler.BossHandler;
import com.cornchipss.custombosses.boss.spawner.BossSpawner;
import com.cornchipss.custombosses.commands.CommandManager;
import com.cornchipss.custombosses.listener.CornyListener;

// https://dev.bukkit.org/projects/supplies/pages/material-list

public class CustomBosses extends JavaPlugin
{
	private BossHandler bossHandler;
	private int repeatingTask;
	private long spawnEveryXSeconds;
	
	@Override
	public void onEnable()
	{
		try
		{
			bossHandler = new BossHandler(this.getDataFolder());
			if(!getConfig().contains("boss-spawn-delay-seconds"))
			{
				getConfig().set("boss-spawn-delay-seconds", 30);
				saveConfig();
			}
			spawnEveryXSeconds = getConfig().getLong("boss-spawn-delay-seconds");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			getLogger().info("Could not load boss info - disabling");
			this.getServer().getPluginManager().disablePlugin(this);
		}
		
		CornyListener cl = new CornyListener(this.getBossHandler());
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(cl, this);
		
		setupSpawning();
	}
	
	private void setupSpawning()
	{
		repeatingTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BossSpawner(getBossHandler()), 0L, 20L * spawnEveryXSeconds);
	}
	
	public void onDisable()
	{
		try
		{
			bossHandler.saveLivingBosses(bossHandler.getLivingBosses());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			getLogger().info("Could not save boss info");
		}
		Bukkit.getScheduler().cancelTask(repeatingTask);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return CommandManager.runThroughCommands(command, sender, args, this);	
	}

	public BossHandler getBossHandler() { return bossHandler; }
}
