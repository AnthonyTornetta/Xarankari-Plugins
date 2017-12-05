package com.cornchipss.buildbattle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRegistry 
{
	// A list of build battle commands for the player to see
	public static final String[] commands = 
	{
		ChatColor.GOLD +  "Build" + ChatColor.RED + " Battle" + ChatColor.YELLOW + " Commands",
		ChatColor.GOLD +  "=====================",
		ChatColor.GREEN + "Begin : Begins the build battle (needs at least 1 player)",
		ChatColor.GREEN + "End : Ends the build battle early",
		ChatColor.GREEN + "Set : Sets specified values",
		ChatColor.GREEN + "Del : Deletes a specified value from the config file",
		ChatColor.GREEN + "Invite : Invites a player to the build battle",
		ChatColor.GREEN + "Assign : Assigns specified player(s) to the build battle forcibly",
		ChatColor.GREEN + "Extend : Extends the build time during a build battle by a specified amount",
		ChatColor.GREEN + "List : Lists specified values",
		ChatColor.GREEN + "Version : Tells you the version of this plugin"
	};
	
	// A list of set sub commands for the player to see
	public static final String[] setSubCmds =
	{
		ChatColor.YELLOW  + "Set Sub Commands",
		ChatColor.YELLOW  + "================",
		ChatColor.GREEN + "Plotpos [plotId] : Sets a plot position to a specified name",
		ChatColor.GREEN + "Duration [seconds] : Sets the build battle duration to a specified amount of seconds",
		ChatColor.GREEN + "Theme [theme] : Sets the build battle theme to a specified theme"
	};
	
	// A list of del sub commands for the player to see
	public static final String[] delSubCmds =
	{
		ChatColor.YELLOW  + "Del Sub Commands",
		ChatColor.YELLOW  + "================",
		ChatColor.GREEN + "Plotid : Deletes a specified plot id & location",
		ChatColor.GREEN + "Plot: Deletes the plot you are standing in"
	};
	
	// A list of list sub commands for the player to see ;)
	public static final String[] listSubCmds =
	{
		ChatColor.YELLOW + "List Sub Commands",
		ChatColor.YELLOW + "=================",
		ChatColor.GREEN + "Players : Lists all the players participating",
		ChatColor.GREEN + "Plotids : Lists all the plot ids with their positions",
		ChatColor.GREEN + "Theme : Lists the theme",
		ChatColor.GREEN + "Duration : Lists the duration"
	};
	
	// A list of list sub commands for the player to see ;)
	public static final String[] createSubCmds =
	{
		ChatColor.YELLOW + "Create Sub Commands",
		ChatColor.YELLOW + "=================",
		ChatColor.GREEN + "Plot: Creates a build battle plot"
	};
	
	/**
	 * Runs through every build battle command
	 * @param command The command passed in by the sender
	 * @param sender The command sender
	 * @param args The arguments to the command
	 * @param bb The java plugin class
	 * @return True if it was successful
	 */
	public static boolean runThruCommands(Command command, CommandSender sender, String[] args, BuildBattle bb)
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
					case "create":
					{
						if(args.length < 2)
						{
							if(!bb.perm(p, "bb.list.create"))
							for(int i = 0; i < listSubCmds.length; i++)
							{
								sender.sendMessage(listSubCmds[i]);
							}
							if(args[1].toLowerCase() == "")
							break;
						}
					}
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
								if(bb.perm(p, "bb.list.player"))
								{
									p.sendMessage(ChatColor.GREEN + "Players participating: Plot Id");
									for(Player temp : bb.getPlayers())
									{
										p.sendMessage(temp.getDisplayName() + ChatColor.GREEN + ": " + bb.getPlotsAssigned().get(p));
									}
								}
								break;
							}
							case "plotids":
							{
								if(bb.perm(p, "bb.list.plotids"))
								{
									if(bb.getConfig().get("plotids") != null)
									{
										String[] plotids = bb.getConfig().get("plotids").toString().split(",");
										for(int i = 0; i < plotids.length; i++)
										{
											p.sendMessage(ChatColor.GREEN + plotids[i] + ": " + bb.getConfig().get(plotids[i]));
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
								if(bb.perm(p, "bb.list.theme"))
								{
									p.sendMessage(ChatColor.GREEN + "Theme: " + bb.getTheme());
								}
								break;
							}
							case "duration":
							{
								if(bb.perm(p, "bb.list.duration"))
								{
									int min = bb.getDurationSeconds() / 60, sec = bb.getDurationSeconds() % 60; // So it looks nicer :)
									
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
						if(!bb.perm(p, "bb.begin"))
							return true;
						if(bb.getPlayers().size() < 1)
						{
							p.sendMessage(ChatColor.RED + "Not enough players participating ;(");
							break;
						}
						else
						{
							p.sendMessage(ChatColor.GREEN + "The build battle has begun!");
							bb.begin(p);
							break;
						}
						
					case "end":
						if(!bb.perm(p, "bb.end"))
							return false;
						bb.end(p);
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
								if(!bb.perm(p, "bb.set.plotpos"))
									return true;
								if(args.length < 3)
								{
									sender.sendMessage(ChatColor.RED + "You must specify the plot id!");
									return true;
								}
								args[2] = args[2].toLowerCase();
								if(args[2].equals("all"))
								{
									sender.sendMessage(ChatColor.RED + "You cannot name a plotid 'all'");
									return true;
								}
								if(bb.getConfig().get("plotids") != null)
								{
									boolean addIt = true;
									String[] stuffs = bb.getConfig().get("plotids").toString().split(",");
									for(int i = 0; i < stuffs.length && addIt; i++)
									{
										if(stuffs[i].equals(args[2]))
											addIt = false;
									}
									if(addIt)
										bb.getConfig().set("plotids", bb.getConfig().get("plotids") + "," + args[2]);
								}
								else
									bb.getConfig().set("plotids", args[2]);
								
								bb.getConfig().set(args[2].toLowerCase(), Math.round(p.getLocation().getX()) + "," + Math.round(p.getLocation().getY()) + "," + Math.round(p.getLocation().getZ()) + "," + p.getWorld().getName());
								bb.saveConfig();
								sender.sendMessage("Plot position '" + args[2] + "' set to " + bb.getConfig().get(args[2].toLowerCase()));
								break;
							}
							case "duration":
							{
								if(!bb.perm(p, "bb.set.duration"))
									return true;
								if(args.length < 3 || !BuildBattle.isInteger(args[2]) || Integer.parseInt(args[2]) <= 0)
								{
									sender.sendMessage(ChatColor.RED + "You must specify the duration in seconds and not less than one!");
									return true;
								}
								bb.setDurationSeconds(Integer.parseInt(args[2]));
								
								int min = bb.getDurationSeconds() / 60, sec = bb.getDurationSeconds() % 60; // So it looks nicer :)
								
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
								if(!bb.perm(p, "bb.set.theme"))
									return true;
								if(args.length < 3)
								{
									sender.sendMessage(ChatColor.RED + "You must specify the theme with text!");
									break;
								}
								bb.setTheme("");
								for(int i = 2; i < args.length; i++)
								{
									bb.setTheme(bb.getTheme() + args[i] + " ");
								}
								sender.sendMessage(ChatColor.GREEN + "Theme set to " + bb.getTheme());
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
							{
								if(!bb.perm(p, "bb.del.plotid"))
									return true;
								if(args.length < 3)
								{
									sender.sendMessage(ChatColor.RED + "You must specify the plot id to delete or 'all' to delete all of them.");
									return true;
								}
								if(args[2].toLowerCase().equals("all"))
								{
									if(bb.getConfig().get("plotids") == null)
									{
										sender.sendMessage(ChatColor.GREEN + "There were no plot ids to delete.");
										return true;
									}
									String[] allIds = bb.getConfig().get("plotids").toString().split(",");
									bb.getConfig().set("plotids", null);
									for(int i = 0; i < allIds.length; i++)
									{
										bb.getConfig().set(allIds[i], null);
									}
									bb.saveConfig();
									sender.sendMessage(ChatColor.GREEN + "All the build battle plot ids have been deleted.");
									break;
								}
								if(bb.getConfig().get(args[2]) != null)
								{
									String newPlotIds = bb.getConfig().get("plotids").toString();
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
									
									bb.getConfig().set("plotids", newPlotIds);
									bb.getConfig().set(args[2], null);
									bb.saveConfig();
									sender.sendMessage(ChatColor.GREEN + "The plot id '" + args[2] + "' has been deleted.");
									return true;
								}
								sender.sendMessage(ChatColor.RED + "The plot id '" + args[2] + "' doesn't exist!");
								return true;
							}
								
							default:
							{
								for(int i = 0; i < delSubCmds.length; i++)
								{
									sender.sendMessage(delSubCmds[i]);
								}
								return true;
							}
						}
						break;
					}
					
					case "invite":
					{
						if(!bb.perm(p, "bb.invite"))
							return true;
						if(bb.getConfig().get("plotids") == null)
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
						if(bb.getPlotsAssigned().containsKey(target))
						{
							sender.sendMessage(ChatColor.RED + "You have already invited that player");
							return true;
						}
						target.sendMessage(ChatColor.GREEN + "You have been invited to a build battle by " + p.getDisplayName() + ChatColor.GREEN + ". Do /bb accept to accept or /bb decline to decline.");
						bb.getPlotsAssigned().put(target, "all"); // Because all is already a reserved keyword i just used it here instead of reserving a new one
						sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " has been invited to the build battle!");
						break;
					}
					
					case "accept":
					{
						if(!bb.getPlotsAssigned().get(sender).equals("all"))
						{
							sender.sendMessage(ChatColor.RED + "You have not been assigned to a build battle yet!");
							return true;
						}
						if(bb.getPlotsAssigned().containsKey(p) && !bb.getPlotsAssigned().get(p).equals("all"))
						{
							sender.sendMessage(ChatColor.RED + "You are already in it");
							return true;
						}
						if(bb.getConfig().get("plotids") == null)
						{
							sender.sendMessage(ChatColor.RED + "There are 0 plots availible to be joined, Let the person that invited you know");
							return true;
						}
						sender.sendMessage(ChatColor.GREEN + "You have accepted the build battle invite!");
						
						bb.setPlotIndex(bb.getPlotIndex() + 1);
						
						if(bb.getPlotIndex() >= bb.getConfig().get("plotids").toString().split(",").length)
							bb.setPlotIndex(0);
						
						Bukkit.broadcastMessage(p.getDisplayName() + ChatColor.GREEN + " has joined the build battle!");
						
						bb.getPlotsAssigned().replace(p, bb.getConfig().get("plotids").toString().split(",")[bb.getPlotIndex()]);
						bb.getPlayers().add(p);
						break;
					}
					
					case "decline":
					{
						if(!bb.getPlotsAssigned().get(sender).equals("all"))
						{
							sender.sendMessage(ChatColor.RED + "You have not been assigned to a build battle yet!");
							return true;
						}
						sender.sendMessage(ChatColor.RED + "You have declined the build battle invite");
						bb.getPlotsAssigned().remove(p);
						break;
					}
					
					case "assign":
					{
						if(!bb.perm(p, "bb.assign"))
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
							if(bb.getConfig().get(args[2]) != null)
							{
								Player target = Bukkit.getPlayer(args[1]);
								if(bb.getPlayers().contains(target))
								{
									p.sendMessage(ChatColor.RED + "Error: The player is already assigned a plot.");
									return true;
								}
								p.sendMessage(ChatColor.GREEN + "Assigned " + target.getDisplayName() + ChatColor.GREEN + " to the plot.");
								target.sendMessage(ChatColor.GREEN + "You have been assigned to a build battle plot!");
								
								Bukkit.broadcastMessage(target.getDisplayName() + ChatColor.GREEN + " has joined the build battle!");
								
								bb.getPlayers().add(target);
								bb.getPlotsAssigned().put(target, args[2]);
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
						if(!bb.perm(p, "bb.extend"))
							return true;
						if(!bb.isRunning())
						{
							sender.sendMessage("There is no build battle bb.isRunning()!");
							return true;
						}
						if(args.length < 2 || !BuildBattle.isInteger(args[1]) || args[1] == null)
						{
							sender.sendMessage("You must specify the time to extend it by in seconds!");
							return true;
						}
						
						try
						{
							bb.getTimer().extend(Integer.parseInt(args[1]));
						}
						catch(Exception ex) // When you do it before the actual timer starts...
						{
							p.sendMessage(ChatColor.RED + "Wait until the building phase begins before doing this :)");
						}
						break;
					}
					
					case "version":
					{
						if(!bb.perm(p, "bb.version"))
							return true;
						sender.sendMessage(ChatColor.GREEN + Reference.NAME + " V" + Reference.VERSION + " by " + Reference.AUTHOR);
						break;
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
			// Almost all build battle commands are player-only so I just made sure only players can use them
			sender.sendMessage("You must be a player to use build battle commands");
		}
		return true;
	}
}