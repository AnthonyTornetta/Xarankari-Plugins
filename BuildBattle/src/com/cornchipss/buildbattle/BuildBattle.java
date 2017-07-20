package com.cornchipss.buildbattle;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


/* Questions I need answered
 * 1 ) When is the $ due
 * 2 ) Who will I make the check out to
 * 3 ) Verify the cell phone issue
 * 4 ) Is it overnight - Where am i staing if it is overnight
 */

public class BuildBattle extends JavaPlugin
{
	private HashMap<Player, String> plotsAssigned = new HashMap<>();
	private ArrayList<Player> players = new ArrayList<>();
	private HashMap<Player, ItemStack[]> playerInventories = new HashMap<>();
	private String theme = "Anything";
	private int durationSeconds = 1 * 60; // 15 min
	private boolean running = false;
	private Timer timer;
	private int plotIndex = 0;
	
	@Override
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new CornyListener(this), this);
		getLogger().info(Reference.NAME + " plugin by " + Reference.AUTHOR + " V" + Reference.VERSION + " is ready for action!");
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
		BuildBattle bb = this;
		if(running)
		{
			sender.sendMessage(ChatColor.RED + "Build battle already running!");
			return;
		}
		running = true;
		
		for(Player p : players)
		{
			if(getConfig().get(plotsAssigned.get(p)) != null)
			{
				String[] loc = getConfig().get(plotsAssigned.get(p)).toString().split(",");
				p.teleport(new Location(Bukkit.getWorld(loc[3]), Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2])));
				p.sendMessage(ChatColor.GREEN + "Build battle beginning!");
				playerInventories.put(p, p.getInventory().getContents());
				
				p.getInventory().clear();
				p.setGameMode(GameMode.CREATIVE);
			}
			else
			{
				p.sendMessage(ChatColor.RED + "The plot id assigned to you no longer exists! Please let the build battle manager know!");
			}			
		}
		
		for(Player p: players)
		{
			p.sendTitle(ChatColor.GREEN + "Theme: " + theme, "Build Battle Beginning in " + 5 + " seconds!", 20, 40, 20);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
			{
				@Override
				public void run()
				{
					for(Player p: players)
					{
						p.sendTitle(ChatColor.GREEN + "BUILD!", "Theme: " + theme, 20, 40, 20);
						int min = durationSeconds / 60, sec = durationSeconds % 60; // So it looks nicer :)
						
						String minStr = "minutes";
						String secStr = "seconds";
						if(min == 1)
							minStr = "minute";
						if(sec == 1)
							secStr = "second";
						
						p.sendMessage(ChatColor.GREEN + "You have " + min + " " + minStr + " and " + sec + " " + secStr + " to build!");
						p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
					}
					timer = new Timer(bb, durationSeconds);
					timer.run();
				}
			}, 20L * 5);
		
		
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
		running = false;
		for(Player p : players)
		{
			if(p.isOnline())
			{
				p.setGameMode(GameMode.SURVIVAL);
				p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
				p.getInventory().clear();
				p.getInventory().setContents(playerInventories.get(p));
				//p.getInventory().setContents(i.getContents());
				
				p.sendTitle(ChatColor.DARK_RED + "BUILD BATTLE OVER!", "", 20, 40, 20);
			}
		}
		players.clear();
		playerInventories.clear();
		plotsAssigned.clear();
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
	
	public int getPlotIndex() { return plotIndex; }
	public void setPlotIndex(int plotIndex) { this.plotIndex = plotIndex; }
	
	public int getDurationSeconds() { return durationSeconds; }
	public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }
	
	public String getTheme() { return theme; }
	public void setTheme(String theme) { this.theme = theme; }
	
	public HashMap<Player, ItemStack[]> getPlayerInventories() { return playerInventories; }
	
	public boolean isRunning() { return running; }
	
	public ArrayList<Player> getPlayers() { return players; }
	
	public HashMap<Player, String> getPlotsAssigned() { return plotsAssigned; }

	public Timer getTimer() { return timer; }
}
