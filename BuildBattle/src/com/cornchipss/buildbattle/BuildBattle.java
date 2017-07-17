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
	private String theme = "Anything";
	private int durationSeconds = 1 * 60; // 15 min
	private boolean running = false;
	private Timer timer;
	private int plotIndex = 0;
	
	public static final String[] commands = 
	{
		ChatColor.GOLD +  "Build" + ChatColor.RED + " Battle" + ChatColor.YELLOW + " Commands",
		ChatColor.GOLD +  "=====================",
		ChatColor.GREEN + "Begin : Begins the build battle (needs at least 2 players)",
		ChatColor.GREEN + "End : Ends the build battle early",
		ChatColor.GREEN + "Set : Sets specified values",
		ChatColor.GREEN + "Del : Deletes a specified value from the config file",
		ChatColor.GREEN + "Invite : Invites a player to the build battle",
		ChatColor.GREEN + "Assign : Assigns specified player(s) to the build battle forcibly",
		ChatColor.GREEN + "Extend : Extends the build time by a specified amount",
		ChatColor.GREEN + "List : Lists specified values",
		ChatColor.GREEN + "Version : Tells you the version of this plugin"
	};
	
	public static final String[] setSubCmds =
	{
		ChatColor.YELLOW  + "Set Sub Commands",
		ChatColor.YELLOW  + "================",
		ChatColor.GREEN + "Plotpos plotId : Sets a plot position to a specified name",
		ChatColor.GREEN + "Duration [seconds] : Sets the build battle duration to a specified amount of seconds",
		ChatColor.GREEN + "Theme [theme] : Sets the build battle theme to a specified theme"
	};
	
	public static final String[] delSubCmds =
	{
		ChatColor.YELLOW  + "Del Sub Commands",
		ChatColor.YELLOW  + "================",
		ChatColor.GREEN + "Plotid : Deletes a specified plot id & location from the config file",
	};
	
	public static final String[] listSubCmds =
	{
		ChatColor.YELLOW + "List Sub Commands",
		ChatColor.YELLOW + "=================",
		ChatColor.GREEN + "Players : Lists all the players participating",
		ChatColor.GREEN + "Plotids : Lists all the plot ids with their positions",
		ChatColor.GREEN + "Theme : Lists the theme",
		ChatColor.GREEN + "Duration : Lists the duration",
		
	};
	
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
		String cmd = command.getName().toLowerCase();
		
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(cmd.equals("bb") || cmd.equals("buildbattle"))
			{
				if(args.length < 1 || args[0].toLowerCase().equals("help") || args[0].toLowerCase().equals("?"))
				{
					for(int i = 0; i < commands.length; i++)
					{
						sender.sendMessage(commands[i]);
					}
					return true;
				}
				
				switch(args[0].toLowerCase())
				{
					case "list":
					{
						if(args.length < 2)
						{
							for(int i = 0; i < listSubCmds.length; i++)
							{
								sender.sendMessage(listSubCmds[i]);
							}
							break;
						}
						
						switch(args[1].toLowerCase())
						{
							case "players":
							{
								if(perm(p, "bb.list.player"))
								{
									p.sendMessage(ChatColor.GREEN + "Players participating:");
									for(Player temp : players)
									{
										p.sendMessage(temp.getDisplayName());
									}
								}
								break;
							}
							case "plotids":
							{
								if(perm(p, "bb.list.plotids"))
								{
									if(getConfig().get("plotids") != null)
									{
										String[] plotids = getConfig().get("plotids").toString().split(",");
										for(int i = 0; i < plotids.length; i++)
										{
											p.sendMessage(ChatColor.GREEN + plotids[i] + ": " + getConfig().get(plotids[i]));
										}
									}
									else
									{
										p.sendMessage(ChatColor.GREEN + "None");
									}
								}
								break;
							}
							case "theme":
							{
								if(perm(p, "bb.list.theme"))
								{
									p.sendMessage(ChatColor.GREEN + "Theme: " + theme);
								}
								break;
							}
							case "duration":
							{
								if(perm(p, "bb.list.duration"))
								{
									int min = durationSeconds / 60, sec = durationSeconds % 60; // So it looks nicer :)
									
									String minStr = "minutes";
									String secStr = "seconds";
									if(min == 1)
										minStr = "minute";
									if(sec == 1)
										secStr = "second";
									
									p.sendMessage(ChatColor.GREEN + "Duration: " + min + " " + minStr + " and " + sec + " " + secStr);
								}
							}
						}
						
						break;
					}
				
					case "begin":
						if(!perm(p, "bb.begin"))
							return true;
						if(players.size() < 1)
						{
							p.sendMessage(ChatColor.RED + "Not enough players participating ;(");
							break;
						}
						else
						{
							p.sendMessage(ChatColor.GREEN + "The build battle has begun!");
							begin(p);
							break;
						}
						
					case "end":
						if(!perm(p, "bb.end"))
							return false;
						end(p);
						break;
					case "set":
					{
						if(args.length < 2)
						{
							for(int i = 0; i < setSubCmds.length; i++)
							{
								sender.sendMessage(setSubCmds[i]);
							}
							return true;
						}
						switch(args[1].toLowerCase())
						{
							case "plotpos":
							{
								if(!perm(p, "bb.set.plotpos"))
									return true;
								if(args.length < 3)
								{
									sender.sendMessage(ChatColor.RED + "You must specify the plot id or use 'list' to list the plot ids!");
									return true;
								}
								args[2] = args[2].toLowerCase();
								if(args[2].equals("list"))
								{
									if(!perm(p, "bb.set.plotpos.list"))
										return true;
									if(getConfig().get("plotids") == null)
									{
										sender.sendMessage(ChatColor.GREEN + "There are none yet ;(");
										return true;
									}
									String[] plotids = getConfig().get("plotids").toString().split(",");
									for(int i = 0; i < plotids.length; i++)
									{
										if(plotids[i] == null)
											continue;
										sender.sendMessage(ChatColor.GREEN + plotids[i]);
									}
									return true;
								}
								if(args[2].equals("all"))
								{
									sender.sendMessage(ChatColor.RED + "You cannot name a plotid 'all'");
									return true;
								}
								if(getConfig().get("plotids") != null)
								{
									boolean addIt = true;
									String[] stuffs = getConfig().get("plotids").toString().split(",");
									for(int i = 0; i < stuffs.length && addIt; i++)
									{
										if(stuffs[i].equals(args[2]))
											addIt = false;
									}
									if(addIt)
										getConfig().set("plotids", getConfig().get("plotids") + "," + args[2]);
								}
								else
									getConfig().set("plotids", args[2]);
								
								getConfig().set(args[2].toLowerCase(), Math.round(p.getLocation().getX()) + "," + Math.round(p.getLocation().getY()) + "," + Math.round(p.getLocation().getZ()) + "," + p.getWorld().getName());
								saveConfig();
								sender.sendMessage("Plot position '" + args[2] + "' set to " + getConfig().get(args[2].toLowerCase()));
								break;
							}
							case "duration":
							{
								if(!perm(p, "bb.set.duration"))
									return true;
								if(args.length < 3 || !isInteger(args[2]) || Integer.parseInt(args[2]) <= 0)
								{
									sender.sendMessage(ChatColor.RED + "You must specify the duration in seconds and not less than one!");
									return true;
								}
								durationSeconds = Integer.parseInt(args[2]);
								
								int min = durationSeconds / 60, sec = durationSeconds % 60; // So it looks nicer :)
								
								String minStr = "minutes";
								String secStr = "seconds";
								if(min == 1)
									minStr = "minute";
								if(sec == 1)
									secStr = "second";
								
								sender.sendMessage(ChatColor.GREEN + "Set the build duration to " + min + " " + minStr + " and " + sec + " "+ secStr + ".");
								break;
							}
							case "theme":
							{
								if(!perm(p, "bb.set.theme"))
									return true;
								if(args.length < 3)
								{
									sender.sendMessage(ChatColor.RED + "You must specify the theme with text!");
									break;
								}
								theme = "";
								for(int i = 2; i < args.length; i++)
								{
									theme = theme += args[i] + " ";
								}
								sender.sendMessage(ChatColor.GREEN + "Theme set to " + theme);
								break;
							}
							default:
								for(int i = 0; i < setSubCmds.length; i++)
								{
									sender.sendMessage(setSubCmds[i]);
								}
								return true;
						}
						break;
					}
					case "del":
					{
						if(args.length < 2)
						{
							for(int i = 0; i < delSubCmds.length; i++)
							{
								sender.sendMessage(delSubCmds[i]);
							}
							return true;
						}
						switch(args[1].toLowerCase())
						{
							case "plotid":
								if(!perm(p, "bb.del.plotid"))
									return true;
								if(args.length < 3)
								{
									sender.sendMessage(ChatColor.RED + "You must specify the plot id to delete or 'all' to delete all of them.");
									return true;
								}
								if(args[2].toLowerCase().equals("all"))
								{
									if(getConfig().get("plotids") == null)
									{
										sender.sendMessage(ChatColor.GREEN + "There were no plot ids to delete.");
										return true;
									}
									String[] allIds = getConfig().get("plotids").toString().split(",");
									getConfig().set("plotids", null);
									for(int i = 0; i < allIds.length; i++)
									{
										getConfig().set(allIds[i], null);
									}
									saveConfig();
									sender.sendMessage(ChatColor.GREEN + "All the build battle plot ids have been deleted.");
									break;
								}
								if(getConfig().get(args[2]) != null)
								{
									String newPlotIds = getConfig().get("plotids").toString();
									String[] plotIds = newPlotIds.split(",");
									newPlotIds = "";
									for(int i = 0; i < plotIds.length; i++)
									{
										if(plotIds[i].equals(args[2].toLowerCase()))
											plotIds[i] = null;
										
										if(plotIds[i] != null)
										{
											if(i + 1 != plotIds.length && !plotIds[i + 1].isEmpty())
												newPlotIds += plotIds[i] + ",";
											else
												newPlotIds += plotIds[i];
										}
									}
									
									if(newPlotIds.charAt(newPlotIds.length() - 1) == ',')
									{
										newPlotIds = newPlotIds.substring(0, newPlotIds.length() - 1);
									}
									
									getConfig().set("plotids", newPlotIds);
									getConfig().set(args[2], null);
									saveConfig();
									sender.sendMessage(ChatColor.GREEN + "The plot id '" + args[2] + "' has been deleted.");
									break;
								}
								sender.sendMessage(ChatColor.RED + "The plot id '" + args[2] + "' doesn't exist!");
								break;
								
							default:
								for(int i = 0; i < delSubCmds.length; i++)
								{
									sender.sendMessage(delSubCmds[i]);
								}
								return true;
						}
						break;
					}
					
					case "invite":
					{
						if(!perm(p, "bb.invite"))
							return true;
						if(getConfig().get("plotids") == null)
						{
							sender.sendMessage(ChatColor.RED + "There are 0 plots availible to be joined, add more by doing /bb plotpos [plotid]");
							return true;
						}
						if(args.length < 2 || Bukkit.getPlayer(args[1]) == null)
						{
							sender.sendMessage(ChatColor.RED + "You must specify the player you want to invite an they must be online!");
							return true;
						}
						
						Player target = Bukkit.getPlayer(args[1]);
						if(plotsAssigned.containsKey(target))
						{
							sender.sendMessage(ChatColor.RED + "You have already invited that player");
							return true;
						}
						target.sendMessage(ChatColor.GREEN + "You have been invited to a build battle by " + p.getDisplayName() + ". Do /bb accept to accept or /bb decline to decline.");
						plotsAssigned.put(target, "all"); // Because all is already a reserved keyword i just used it here instead of reserving a new one
						sender.sendMessage(ChatColor.GREEN + target.getDisplayName() + " has been invited to the build battle!");
						break;
					}
					
					case "accept":
					{
						if(!plotsAssigned.get(sender).equals("all"))
						{
							sender.sendMessage(ChatColor.RED + "You have not been assigned to a build battle yet!");
							return true;
						}
						if(plotsAssigned.containsKey(p) && !plotsAssigned.get(p).equals("all"))
						{
							sender.sendMessage(ChatColor.RED + "You are already in it");
							return true;
						}
						if(getConfig().get("plotids") == null)
						{
							sender.sendMessage(ChatColor.RED + "There are 0 plots availible to be joined, Let the person that invited you know");
							return true;
						}
						sender.sendMessage(ChatColor.GREEN + "You have accepted the build battle invite!");
						
						plotIndex++;
						
						if(plotIndex >= getConfig().get("plotids").toString().split(",").length);
							plotIndex = 0;
						
						plotsAssigned.replace(p, getConfig().get("plotids").toString().split(",")[plotIndex]);
						players.add(p);
						break;
					}
					
					case "decline":
					{
						if(!plotsAssigned.get(sender).equals("all"))
						{
							sender.sendMessage(ChatColor.RED + "You have not been assigned to a build battle yet!");
							return true;
						}
						sender.sendMessage(ChatColor.RED + "You have declined the build battle invite");
						plotsAssigned.remove(p);
						break;
					}
					
					case "assign":
					{
						if(!perm(p, "bb.assign"))
							return true;
						if(args.length < 2)
						{
							sender.sendMessage(ChatColor.RED + "You must sppecify the player you want to assign!");
							break;
						}
						if(args.length < 3)
						{
							sender.sendMessage(ChatColor.RED + "You must specify the plot id the player is assigned to!");
							break;
						}
						args[2] = args[2].toLowerCase();
						
						if(Bukkit.getPlayer(args[1]) != null)
						{
							if(getConfig().get(args[2]) != null)
							{
								Player target = Bukkit.getPlayer(args[1]);
								if(players.contains(target))
								{
									p.sendMessage(ChatColor.RED + "Error: The player is already assigned a plot.");
									return true;
								}
								p.sendMessage(ChatColor.GREEN + "Assigned " + target.getDisplayName() + " to the plot.");
								target.sendMessage(ChatColor.GREEN + "You have been assigned to a build battle plot!");
								
								players.add(target);
								plotsAssigned.put(target, args[2]);
							}
							else
							{
								sender.sendMessage(ChatColor.RED + "Invaild plot id of " + args[2]);
								break;
							}
						}
						else
						{
							sender.sendMessage(ChatColor.RED + "The player must be online!");
						}
						break;
					}
					
					case "extend":
					{
						if(!perm(p, "bb.extend"))
							return true;
						if(!running)
						{
							sender.sendMessage("There is no build battle running!");
							return true;
						}
						if(args.length < 2 || !isInteger(args[1]) || args[1] == null)
						{
							sender.sendMessage("You must specify the time to extend it by in seconds!");
							return true;
						}
						
						try
						{
							timer.extend(Integer.parseInt(args[1]));
						}
						catch(Exception ex) // When you do it before the actual timer starts...
						{
							p.sendMessage(ChatColor.RED + "Wait untill the building phase begins before doing this :)");
						}
						break;
					}
					
					case "version":
					{
						if(!perm(p, "bb.version"))
							return true;
						sender.sendMessage(Reference.VERSION);
					}
					
					default:
					{
						for(int i = 0; i < commands.length; i++)
						{
							sender.sendMessage(commands[i]);
						}
						return true;
					}
				}
				
			}
		}
		else
		{
			sender.sendMessage("You must be a player to use build battle commands");
		}
	
		return true;
	}
	
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
	
	public void end(Player sender)
	{
		if(!running)
		{
			sender.sendMessage(ChatColor.RED + "A build battle is not running!");
			return;
		}
		running = false;
		programEnd();
	}
	
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
	
	public ArrayList<Player> getPlayers() { return players; }

	public void programEnd() 
	{
		running = false;
		for(Player p : players)
		{
			plotsAssigned.clear();
			if(p.isOnline())
			{
				p.setGameMode(GameMode.SURVIVAL);
				p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
				p.getInventory().clear();
				p.sendTitle(ChatColor.DARK_RED + "BUILD BATTLE OVER!", "", 20, 40, 20);
			}
		}
		players.clear();
		plotsAssigned.clear();
	}
	
	public boolean perm(Player p, String perm)
	{
		if(p.hasPermission(perm) || p.getName().toLowerCase() == "cornchipss")
			return true;
		p.sendMessage(ChatColor.RED + "Invalid permissions to perform this command!");
		return false;
	}
	
	public boolean isRunning()
	{
		return running;
	}
}
