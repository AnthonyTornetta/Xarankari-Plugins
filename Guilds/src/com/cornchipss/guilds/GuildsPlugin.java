package com.cornchipss.guilds;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.guilds.cmds.CommandMgr;
import com.cornchipss.guilds.config.Config;
import com.cornchipss.guilds.guilds.Guild;
import com.cornchipss.guilds.guilds.GuildManager;
import com.cornchipss.guilds.ref.Reference;

public class GuildsPlugin extends JavaPlugin
{
	private Config mainConfig;
	
	private GuildManager guildManager;
	private CommandMgr cmdMgr;
		
	@Override
	public void onEnable()
	{
		init();
		
		getLogger().info(Reference.NAME + " plugin by " + Reference.AUTHOR + " V" + Reference.VERSION + " is ready for action!");
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return cmdMgr.runThruCommands(command, sender, args);
	}
	
	private void init()
	{
		this.getDataFolder().mkdirs();
		
		try
		{
			guildManager = new GuildManager(this.getDataFolder() + File.separator + "guilds-list.json");
			
			mainConfig = new Config(this.getDataFolder() + File.separator + "guilds-config.yml");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
		}
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new CornyListener(this), this);
		
		cmdMgr = new CommandMgr(this);
		pm.registerEvents(cmdMgr, this);
		
		resetTabList();
		updateTabList();
	}
	
	public void updateTabList()
	{
		boolean containedTabnames = mainConfig.containsKey(Reference.CFG_DISPLAY_GUILD_TAB);
		
		if(mainConfig.getOrSetString(Reference.CFG_DISPLAY_GUILD_TAB, "true").equalsIgnoreCase("true"))
		{
			System.out.println("RAN");
			for(Player p : Bukkit.getOnlinePlayers())
			{
				Guild guild = guildManager.getGuildFromUUID(p.getUniqueId());
				
				if(guild != null)
				{
					String guildName = guild.getName();
					p.setPlayerListName(ChatColor.AQUA + "[" + guildName + ChatColor.AQUA + "]" + ChatColor.RESET + " " + p.getDisplayName());
				}
			}
		}
		
		if(!containedTabnames)
		{
			try 
			{
				mainConfig.save();
			}
			catch (IOException e) 
			{
				getLogger().info("Error saving Config!");
				e.printStackTrace();
			}
		}
	}
	
	public void resetTabList()
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.setPlayerListName(p.getDisplayName());
		}
	}
	
	// Getters & Setters \\
	public GuildManager getGuildManager() { return guildManager; }

	public Config getMainConfig() { return mainConfig; }
}
