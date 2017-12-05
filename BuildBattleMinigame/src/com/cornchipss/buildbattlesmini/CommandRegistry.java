package com.cornchipss.buildbattlesmini;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandRegistry 
{
	private static HashMap<Player, Location> plotSettingCoords = new HashMap<>();
	private static final int ROWS = 3;
	
	// A list of build battle commands for the player to see
	public static final String[] commands = 
	{
		ChatColor.GOLD  +  "Build" + ChatColor.RED + " Battle" + ChatColor.YELLOW + " Commands",
		ChatColor.GOLD  +  "=====================",
		ChatColor.GREEN + "Begin : Begins the build battle (needs at least 1 player)",
		ChatColor.GREEN + "Stop : Stops the build battle early",
		ChatColor.GREEN + "Set : Sets specified values",
		ChatColor.GREEN + "Del : Deletes a specified value from the config file",
		ChatColor.GREEN + "Invite : Invites a player to the build battle",
		ChatColor.GREEN + "Assign : Assigns the specified player to the build battle",
		ChatColor.GREEN + "Extend : Extends the build time during a build battle by a specified amount",
		ChatColor.GREEN + "List : Lists specified values",
		ChatColor.GREEN + "Version : Tells you the version of this plugin"
	};
	
	// A list of set sub commands for the player to see
	public static final String[] setSubCmds =
	{
		ChatColor.YELLOW + "Set Sub Commands",
		ChatColor.YELLOW + "================",
		ChatColor.GREEN  + "Plotpos [plotName] : Sets a plot position to a specified name",
		ChatColor.GREEN  + "Duration [seconds] : Sets the build battle duration to a specified amount of seconds",
		ChatColor.GREEN  + "Theme [theme] : Sets the build battle theme to a specified theme"
	};
	
	// A list of del sub commands for the player to see
	public static final String[] delSubCmds =
	{
		ChatColor.YELLOW  + "Del Sub Commands",
		ChatColor.YELLOW  + "================",
		ChatColor.GREEN   + "Plot : Deletes the plot you are standing in",
		ChatColor.GREEN   + "Plot [id] : Deletes a specified plot id & location"
	};
	
	// A list of list sub commands for the player to see ;)
	public static final String[] listSubCmds =
	{
		ChatColor.YELLOW + "List Sub Commands",
		ChatColor.YELLOW + "=================",
		ChatColor.GREEN  + "Players : Lists all the players participating",
		ChatColor.GREEN  + "plots : Lists all the plot with their positions",
		ChatColor.GREEN  + "Theme : Lists the theme",
		ChatColor.GREEN  + "Duration : Lists the duration"
	};
	
	// A list of list sub commands for the player to see ;)
	public static final String[] createSubCmds =
	{
		ChatColor.YELLOW + "Create Sub Commands",
		ChatColor.YELLOW + "=================",
		ChatColor.GREEN  + "Plot: Creates a build battle plot"
	};
	
	/**
	 * Runs through every build battle command
	 * @param command The command passed in by the sender
	 * @param sender The command sender
	 * @param args The arguments to the command
	 * @param bb The java plugin class
	 * @return True if it was successful
	 */
	public static boolean runThruCommands(Command command, CommandSender sender, String[] args, BuildBattleMini bb)
	{
		String bbCmd = command.getName().toLowerCase();
		
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(bbCmd.equals("bb") || bbCmd.equals("buildbattle") && bb.perm(p, "bb.commandgui"))
			{
				if(args.length == 0)
				{
					openCmdGUI(p);
					return true;
				}
				else
				{
					String cmd = args[0].toLowerCase();
					if(cmd.equals("help") || cmd.equals("?"))
					{
						
					}
				}
			}
		}
		else
		{
			// Almost all build battle commands are player-only so I just made sure only players can use them
			sender.sendMessage("You must be a player to use build battle commands");
		}
		return true;
	}
	
	public static void openCmdGUI(Player p)
	{
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, ChatColor.DARK_GREEN + "Build Battle Commands");
		
		genBorders(ROWS, inv);
		
		ItemStack begin = new ItemStack(Material.GREEN_GLAZED_TERRACOTTA);
		ItemMeta beginMeta = begin.getItemMeta();
		beginMeta.setDisplayName(ChatColor.GREEN + "Start the build battle");
		begin.setItemMeta(beginMeta);
		
		ItemStack stop = new ItemStack(Material.RED_GLAZED_TERRACOTTA);
		ItemMeta stopMeta = stop.getItemMeta();
		stopMeta.setDisplayName(ChatColor.RED + "Stop the build battle");
		stop.setItemMeta(stopMeta);
		
		ItemStack set = new ItemStack(Material.REDSTONE_TORCH_ON);
		ItemMeta setMeta = set.getItemMeta();
		setMeta.setDisplayName(ChatColor.GOLD + "Set...");
		set.setItemMeta(setMeta);
		
		ItemStack del = new ItemStack(Material.FLINT_AND_STEEL);
		ItemMeta delMeta = del.getItemMeta();
		delMeta.setDisplayName(ChatColor.RED + "Delete...");
		del.setItemMeta(delMeta);
		
		ItemStack assign = new ItemStack(Material.BRICK);
		ItemMeta assignMeta = assign.getItemMeta();
		assignMeta.setDisplayName(ChatColor.GOLD + "Assign Player");
		assign.setItemMeta(assignMeta);
		
		ItemStack list = new ItemStack(Material.BOOK);
		ItemMeta listMeta = list.getItemMeta();
		listMeta.setDisplayName(ChatColor.GOLD + "Display Information");
		list.setItemMeta(listMeta);
		
		ItemStack version = new ItemStack(Material.PAPER);
		ItemMeta versionMeta = version.getItemMeta();
		versionMeta.setDisplayName(ChatColor.GREEN + "Version " + Reference.VERSION);
		version.setItemMeta(versionMeta);
		
		inv.setItem(9 + 0, begin);
		inv.setItem(9 + 1, stop);
		inv.setItem(9 + 3, set);
		inv.setItem(9 + 4, del);
		inv.setItem(9 + 6, assign);
		inv.setItem(9 + 7, list);
		inv.setItem(9 + 8, version);
		
		p.openInventory(inv);
	}

	public static void openSetGUI(Player p) 
	{
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.BB_CMD_SET_WINDOW_TITLE);
		
		genBorders(ROWS, inv);
		
		ItemStack plot = new ItemStack(Material.GRASS);
		ItemMeta plotMeta = plot.getItemMeta();
		if(!plotSettingCoords.containsKey(p))
		{
			plotMeta.setDisplayName(ChatColor.GREEN + "Create the first plot corner here");
		}
		else
		{
			plotMeta.setDisplayName(ChatColor.GREEN + "Create the second plto corner here");
		}
		plot.setItemMeta(plotMeta);
		
		ItemStack duration = new ItemStack(Material.WATCH);
		ItemMeta durationMeta = duration.getItemMeta();
		durationMeta.setDisplayName(ChatColor.GOLD + "Set the build battle's duration");
		duration.setItemMeta(durationMeta);
		
		ItemStack theme = new ItemStack(Material.PAPER);
		ItemMeta themeMeta = duration.getItemMeta();
		themeMeta.setDisplayName(ChatColor.GOLD + "Set the build battle's theme");
		theme.setItemMeta(themeMeta);
		
		inv.setItem(9 + 1, plot);
		inv.setItem(9 + 4, duration);
		inv.setItem(9 + 7, theme);
		
		p.openInventory(inv);
	}

	public static void openDelGUI(Player p) 
	{
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.BB_CMD_SET_WINDOW_TITLE);
		genBorders(ROWS, inv);
		p.openInventory(inv);
	}
	
	public static void openAssignGUI(Player p) 
	{
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.BB_CMD_SET_WINDOW_TITLE);
		genBorders(ROWS, inv);
		p.openInventory(inv);
	}

	public static void openListGUI(Player p) 
	{
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.BB_CMD_SET_WINDOW_TITLE);
		genBorders(ROWS, inv);
		p.openInventory(inv);
	}
	
	public static void setPlotCorner(Player p) 
	{
		if(plotSettingCoords.containsKey(p))
		{
			Location corner1 = plotSettingCoords.get(p);
			Location corner2 = p.getLocation();
			if(!corner1.getWorld().equals(corner2.getWorld()))
			{
				p.sendMessage(ChatColor.RED + "The plot corners must be in the same world!");
				return;
			}
		}
		else
		{
			plotSettingCoords.put(p, p.getLocation());
			p.sendMessage(ChatColor.GREEN + "The first plot corner has been set to your location");
		}
	}

	public static void openDurationGUI(Player p) 
	{
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.BB_CMD_SET_WINDOW_TITLE);
		genBorders(ROWS, inv);
		p.openInventory(inv);
	}

	public static void openThemeGUI(Player p) 
	{
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.BB_CMD_SET_WINDOW_TITLE);
		genBorders(ROWS, inv);
		p.openInventory(inv);
	}
	
	private static void genBorders(final int ROWS, Inventory inv)
	{
		ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)13);
		ItemMeta greenGlassMeta = greenGlass.getItemMeta();
		greenGlassMeta.setDisplayName("-=-");
		greenGlass.setItemMeta(greenGlassMeta);
		
		ItemStack close = new ItemStack(Material.BARRIER);
		ItemMeta closeMeta = close.getItemMeta();
		closeMeta.setDisplayName(ChatColor.RED + "Close");
		close.setItemMeta(closeMeta);
		
		// Give it a border
		for(int i = 0; i < 9; i++)
		{
			if(i == 8)
				inv.setItem(i, close);
			else
				inv.setItem(i, greenGlass);
			inv.setItem(i + ((ROWS - 1) * 9), greenGlass);
		}
	}
}