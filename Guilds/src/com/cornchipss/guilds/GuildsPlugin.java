package com.cornchipss.guilds;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.guilds.cmds.CommandMgr;
import com.cornchipss.guilds.config.Config;
import com.cornchipss.guilds.guilds.Guild;
import com.cornchipss.guilds.guilds.GuildsManager;
import com.cornchipss.guilds.ref.Reference;

import net.milkbowl.vault.economy.Economy;

public class GuildsPlugin extends JavaPlugin
{
	private Config mainConfig;
	
	private GuildsManager guildManager;
	private CommandMgr cmdMgr;
	
	private Economy econ;
		
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
		if(!setupEconomy())
		{
			getLogger().info("Disabling due to no vault dependancy");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		
		this.getDataFolder().mkdirs();
		
		try
		{
			guildManager = new GuildsManager(this.getDataFolder() + File.separator + "guilds-list.json");
			
			if(guildManager.removeUselessGuilds())
			{
				guildManager.saveGuilds();
			}
			
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
	
	private boolean setupEconomy() 
	{
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) 
        {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) 
        {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
	}
	
	public void updateTabList()
	{
		boolean containedTabnames = mainConfig.containsKey(Reference.CFG_DISPLAY_GUILD_TAB);
		
		if(mainConfig.getOrSetString(Reference.CFG_DISPLAY_GUILD_TAB, "true").equalsIgnoreCase("true"))
		{
			for(Player p : Bukkit.getOnlinePlayers())
			{
				Guild guild = guildManager.getGuildFromUUID(p.getUniqueId());
				
				if(guild != null)
				{
					String guildName = guild.getName();
					p.setPlayerListName(ChatColor.AQUA + "[" + guildName + ChatColor.AQUA + "]" + ChatColor.RESET + " " + p.getDisplayName());
				}
				else
				{
					p.setPlayerListName(p.getDisplayName());
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
	public GuildsManager getGuildManager() { return guildManager; }

	public Config getMainConfig() { return mainConfig; }
	
	public Economy getEcononomy() { return econ; }
}
