package com.cornchipss.guilds;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.guilds.cmds.CommandMgr;
import com.cornchipss.guilds.config.Config;
import com.cornchipss.guilds.guilds.GuildManager;
import com.cornchipss.guilds.ref.Reference;

public class Guilds extends JavaPlugin
{
	private GuildManager guildMgr;
	private ArrayList<Player> guildChatters = new ArrayList<>();
	private ArrayList<Player> socialSpies = new ArrayList<>();
	
	private Config guildsCfg;
	
	@Override
	public void onEnable()
	{
		init();
		
		getLogger().info(Reference.NAME + " plugin by " + Reference.AUTHOR + " V" + Reference.VERSION + " is ready for action!");
	}
	
	@Override
	public void onDisable()
	{
		guildsCfg.save();
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
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new CornyListener(this), this);
		
		guildsCfg = new Config(this.getDataFolder() + "\\guilds.yml");
		//guildsCfg.save();
		//guildMgr = new GuildManager(this);
		
		updateTabList(); // For reloads
	}
	
	public void updateTabList()
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			int guildId = guildMgr.getGuildIDFromUUID(p.getUniqueId());
			
			if(guildId != -1)
			{
				String guild = guildMgr.getGuildNameFromID(guildId);
				p.setPlayerListName(ChatColor.AQUA + "[" + guild + ChatColor.AQUA + "]" + ChatColor.RESET + " " + p.getDisplayName());
			}
		}
	}
	
	// Getters & Setters \\
	public GuildManager getGuildManager() { return guildMgr; }

	public ArrayList<Player> getGuildChatters() { return guildChatters; }
	public ArrayList<Player> getSocialSpies() { return socialSpies; }
}
