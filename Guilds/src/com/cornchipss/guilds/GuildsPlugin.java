package com.cornchipss.guilds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.guilds.cmds.CommandMgr;
import com.cornchipss.guilds.guilds.Guild;
import com.cornchipss.guilds.guilds.GuildManager;
import com.cornchipss.guilds.ref.Reference;

public class GuildsPlugin extends JavaPlugin
{
	private GuildManager guildManager;
	private ArrayList<Player> guildChatters = new ArrayList<>();
	private ArrayList<Player> socialSpies = new ArrayList<>();
		
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
		return CommandMgr.runThruCommands(command, sender, args, this);	
	}
	
	public boolean tabDisplayGuild()
	{
		if(getConfig().contains(Reference.CFG_DISPLAY_GUILD_TAB))
			return getConfig().getBoolean(Reference.CFG_DISPLAY_GUILD_TAB);
		else
		{
			getConfig().set(Reference.CFG_DISPLAY_GUILD_TAB, "true");
			saveConfig();
			return true;
		}
	}
	
	private void init()
	{
		this.getDataFolder().mkdirs();
		
		try
		{
			guildManager = new GuildManager(this.getDataFolder() + File.separator + "guilds.yml");
		} 
		catch (IOException e) 
		{
			Bukkit.getPluginManager().disablePlugin(this);
			e.printStackTrace();
		}
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new CornyListener(this), this);
				
		updateTabList();
	}
	
	public void updateTabList()
	{
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
	
	// Getters & Setters \\
	public GuildManager getGuildManager() { return guildManager; }

	public ArrayList<Player> getGuildChatters() { return guildChatters; }
	public ArrayList<Player> getSocialSpies() { return socialSpies; }
}
