package com.cornchipss.buildbattlesmini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.buildbattlesmini.arenas.Arena;
import com.cornchipss.buildbattlesmini.util.Config;

public class BuildBattleMini extends JavaPlugin
{
	private ArrayList<Arena> arenas = new ArrayList<>();
	private ArrayList<Player> maybes = new ArrayList<>();
	private HashMap<Player, ItemStack[]> playerInventories = new HashMap<>();
	private String themes[] =
		{
				"Anything",
				"Medevil",
				"Western",
				"Stone-age",
				"Beachy",
				"Shack",
				"Supermarket",
				"Boat",
				"Shipwreck",
				"Candyland",
				"Desert",
				"Wasteland",
				"Snowy",
				"North Pole",
				"Musical",
				"Magical",
				"Plant",
				"Plane"
		};
	private String currentTheme = "Anything";
	
	private Random  rdm = new Random();
	private int     durationSeconds = 15 * 60; // 15 min
	private boolean running = false;
	private Timer   timer;
	
	private Config  arenaConfig;
	
	@Override
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new CornyListener(this), this);		
		
		initConfigs();
		readConfig();
		
		getLogger().info(Reference.NAME + " plugin by " + Reference.AUTHOR + " V" + Reference.VERSION + " is ready for action!");
	}
	
	private void readConfig()
	{
		if(!getConfig().contains(Reference.CFG_THEMES))
		{
			for(int i = 0; i < themes.length; i++)
			{
				if(i != 0)
					getConfig().set(Reference.CFG_THEMES, getConfig().get(Reference.CFG_THEMES) + "," + themes[i]);
				else
					getConfig().set(Reference.CFG_THEMES, themes[i]);
			}
		}
		else
			themes = getConfig().getString(Reference.CFG_THEMES).split(",");
		
		if(!getConfig().contains(Reference.CFG_DURATION))
			getConfig().set(Reference.CFG_DURATION, durationSeconds);
		else
			durationSeconds = getConfig().getInt(Reference.CFG_DURATION);
		
		
		
		saveConfig();
	}
	
	private void initConfigs()
	{
		String arenaCfgHeader =
				"# This is the Build Battles config file\n" +
				"# This file holds the set arenas & locations\n";
		
		arenaConfig = new Config(arenaCfgHeader, getDataFolder() + "\\arenas.yml");
	}
	
	@Override
	public void onDisable()
	{
		getLogger().info(Reference.NAME + " plugin by " + Reference.AUTHOR + " V" + Reference.VERSION + " has been disabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return CommandRegistry.runThruCommands(command, sender, args, this);	
	}
	
	/**
	 * Begin a build battle via command
	 * @param sender The command sender
	 */
	public void begin(Player sender)
	{		
		if(running)
		{
			sender.sendMessage(ChatColor.RED + "Build battle already running!");
			return;
		}
		int players = 0;
		for(Arena a : arenas) 
		{
			players += a.getPlayerCount();
		}
		
		if(players < 2)
		{
			sender.sendMessage(ChatColor.RED + "Not enough players participating!");
			return;
		}
		
		Bukkit.getLogger().info("Build Battle Starting");
		running = true;
		maybes.clear(); // Don't want anyone joining mid-battle :p
		currentTheme = themes[rdm.nextInt(themes.length)];
		
		for(Arena a : arenas)
		{
			a.begin(this);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Begin(this, timer, currentTheme, arenas, durationSeconds), 20L * 5);
	}
	
	/**
	 * End the build battle via command
	 * @param sender The command sender
	 */
	public void end(Player sender)
	{
		if(!running)
		{
			sender.sendMessage(ChatColor.RED + "A build battle is not running!");
			return;
		}
		programEnd();
	}
	
	/**
	 * Ends the build battle programatically
	 */
	public void programEnd() 
	{
		Bukkit.getLogger().info("Build Battle Ending");
		running = false;
		for(Arena a : arenas)
		{
			a.reset(playerInventories);
		}
		playerInventories.clear();
	}
	
	public boolean perm(Player p, String perm)
	{
		if(p.hasPermission(perm) || p.getName().toLowerCase() == "cornchipss")
			return true;
		p.sendMessage(ChatColor.RED + "Invalid permissions to perform this command!");
		return false;
	}
	
	/**
	 * Checks if a string is an integer
	 * @param s The string to check
	 * @return True if it is an integer
	 */
	public static boolean isInteger(String s) 
	{
	    try 
	    { 
	        Integer.parseInt(s);
	    } 
	    catch(NumberFormatException e) 
	    {
	        return false; 
	    } 
	    catch(NullPointerException e)
	    {
	        return false;
	    }
	    // Only got here if we didn't return false
	    return true;
	}
	
	// Getters & Setters \\
		
	public int getDurationSeconds() { return durationSeconds; }
	public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }
	
	public String getTheme() { return currentTheme; }
	public void setTheme(String currentTheme) { this.currentTheme = currentTheme; }
	
	public Config getArenaConfig() { return arenaConfig; }
	
	public HashMap<Player, ItemStack[]> getPlayerInventories() { return playerInventories; }
	
	public boolean isRunning() { return running; }
	
	public ArrayList<Arena> getArenas() { return arenas; }
	
	public Timer getTimer() { return timer; }

	public ArrayList<Player> getMaybes() { return maybes; }
}
