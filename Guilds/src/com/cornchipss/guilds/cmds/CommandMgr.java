package com.cornchipss.guilds.cmds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.cornchipss.guilds.GuildsPlugin;
import com.cornchipss.guilds.guilds.Guild;
import com.cornchipss.guilds.guilds.GuildRank;
import com.cornchipss.guilds.util.Helper;

import mkremins.fanciful.FancyMessage;

public class CommandMgr implements Listener
{
	private List<Player> possibleGuildDeletions = new ArrayList<>();
	private Map<Player, Guild> guildJoinProposals = new HashMap<>();
	
	private GuildsPlugin plugin;
	
	public CommandMgr(GuildsPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Runs through every build battle command
	 * @param command The command passed in by the sender
	 * @param sender The command sender
	 * @param args The arguments to the command
	 * @return true if it was successful
	 */
	public boolean runThruCommands(Command command, CommandSender sender, String[] args)
	{
		String cmd = command.getName().toLowerCase();
		
		if(cmd.equals("guild") || cmd.equals("guilds"))
		{
			if(args.length < 1)
			{
				Player p = (Player)sender;
				Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
				
				if(g == null)
				{
					p.sendMessage(ChatColor.RED + "You are not in a guild to view your guild's stats.");
					p.sendMessage(ChatColor.RED + "Do /guilds help to view the help");
					return true;
				}
				
				p.sendMessage(ChatColor.GREEN + "= " + g.getName() + " =");
				p.sendMessage(ChatColor.GREEN + "Balance: $" + g.getBalance());
				p.sendMessage(ChatColor.GREEN + "Claims: " + g.getOwnedChunks().size() + "/" + g.getMaxClaims());
				
				Player king = null;
				List<Player> commanders = new ArrayList<>();
				List<Player> knights = new ArrayList<>();;
				List<Player> peons = new ArrayList<>();;
				
				for(UUID uuid : g.getMembers())
				{
					Player player = Bukkit.getPlayer(uuid);
					if(player == null)
						continue;
					
					switch(g.getMemberRank(uuid))
					{
					case KING:
						king = player;
						break;
					case COMMANDER:
						commanders.add(player);
						break;
					case KNIGHT:
						knights.add(player);
						break;
					case PEON:
						peons.add(player);
						break;
					default:
						break;
					}
				}
				
				if(king != null)
					p.sendMessage(ChatColor.GREEN + "King: " + king.getDisplayName());
				
				String commandersStr = "";
				for(int i = 0; i < commanders.size(); i++)
				{
					commandersStr += commanders.get(i).getDisplayName() + ChatColor.GREEN;
					if(i + 1 != commanders.size())
						commandersStr += ChatColor.GREEN + ", ";
				}
				String knightsStr = "";
				for(int i = 0; i < commanders.size(); i++)
				{
					knightsStr += commanders.get(i).getDisplayName() + ChatColor.GREEN;
					if(i + 1 != commanders.size())
						knightsStr += ChatColor.GREEN + ", ";
				}
				String peonsStr = "";
				for(int i = 0; i < commanders.size(); i++)
				{
					peonsStr += commanders.get(i).getDisplayName() + ChatColor.GREEN;
					if(i + 1 != commanders.size())
						peonsStr += ChatColor.GREEN + ", ";
				}
				
				p.sendMessage(ChatColor.GREEN + "Commanders: " + commandersStr);
				p.sendMessage(ChatColor.GREEN + "Knights: " + knightsStr);
				p.sendMessage(ChatColor.GREEN + "Peons: " + peonsStr);
				
				return true;
			}
			
			// Reset the command to args[0] because that is the new command to interpret
			cmd = args[0].toLowerCase();
			
			if(cmd.equals("new") || cmd.equals("create"))
			{				
				if(!iop(sender) || !perm(sender, "guilds.create"))
					return true;
				
				Player p = (Player)sender;
				
				if(plugin.getGuildManager().playerHasGuild(p))
				{
					p.sendMessage(ChatColor.RED + "You are already in a guild! do /guild leave to leave your guild.");
					return true;
				}
				
				if(args.length < 2)
				{
					sender.sendMessage(ChatColor.RED + "You must specify the guild name.");
					return true;
				}
				else
				{
					try
					{
						if(plugin.getGuildManager().createGuild(args[1], p))
							p.sendMessage(ChatColor.GREEN + "Your guild \"" + args[1] + "\" has been created!");
						else
						{
							p.sendMessage(ChatColor.RED + "Unable to create a guild with that name.");
						}
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
						p.sendMessage(ChatColor.RED + "There was a server error when creating your guild!");
					}
					
					plugin.updateTabList();
				}
			}
			else if(cmd.equals("delete"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					
					if(!plugin.getGuildManager().playerHasGuild(p))
					{
						p.sendMessage(ChatColor.RED + "You must be in a guild to use this.");
						return true;
					}
					
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					
					GuildRank rank = g.getMemberRank(p.getUniqueId());
					
					if(rank.lessThan(GuildRank.KING))
					{
						p.sendMessage(ChatColor.RED + "You are not qualified enough in your guild to do this.");
						return true;
					}
					
					if(!possibleGuildDeletions.contains(p))
						possibleGuildDeletions.add(p);
					p.sendMessage(ChatColor.RED + "Are you sure you want to delete your guild? Type your guild's name to confirm. " + ChatColor.DARK_RED + "WARNING:" + ChatColor.RED + " THIS CANNOT BE UNDONE!!!");
				}
			}
			else if(cmd.equals("accept"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					
					if(plugin.getGuildManager().playerHasGuild(p))
					{
						p.sendMessage(ChatColor.RED + "You are already in a guild!");
						return true;
					}
					
					if(guildJoinProposals.containsKey(p))
					{					
						Guild g = guildJoinProposals.get(p);
						try 
						{
							plugin.getGuildManager().addPlayerToGuild(p, g, GuildRank.PEON);
							p.sendMessage(ChatColor.GREEN + "Guild " + g.getName() + " successfully joined!");
						}
						catch (IOException e) 
						{
							e.printStackTrace();
							p.sendMessage(ChatColor.RED + "A server error occurred when trying to join the guild.");
						}
						
						guildJoinProposals.remove(p);
					}
					else
					{
						p.sendMessage(ChatColor.RED + "You have no pending invites.");
						return true;
					}
				}
			}
			else if(cmd.equals("deny"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					
					if(guildJoinProposals.containsKey(p))
					{					
						p.sendMessage(ChatColor.GREEN + "Guild request rejected.");
						guildJoinProposals.remove(p);
					}
					else
					{
						p.sendMessage(ChatColor.RED + "You have no pending invites.");
						return true;
					}
				}
			}
			else if(cmd.equals("chat") || cmd.equals("gc"))
			{
				return gc(sender);
			}
			else if(cmd.equals("invite"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You aren't in a guild.");
						return true;
					}
					
					GuildRank rank = g.getMemberRank(p.getUniqueId());
					
					if(rank.lessThan(GuildRank.COMMANDER))
					{
						p.sendMessage(ChatColor.RED + "You are not qualified enough in your guild to do this.");
						return true;
					}
					
					if(args.length < 2)
					{
						p.sendMessage(ChatColor.RED + "You must specify the player to invite.");
						return true;
					}
					
					Player toInvite = Bukkit.getPlayer(args[1]);
					if(toInvite == null)
					{
						p.sendMessage(ChatColor.RED + "Unable to find player " + args[1] + ".");
						return true;
					}
					
					Guild potentialGuild = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					if(potentialGuild != null)
					{
						p.sendMessage(ChatColor.RED + p.getDisplayName() + ChatColor.RED + " is already in the guild \"" + potentialGuild.getName() + "\".");
						return true;
					}
					
					if(guildJoinProposals.containsKey(p))
					{
						p.sendMessage(ChatColor.RED + "That player, " + toInvite.getDisplayName() + ChatColor.RED + ", already has a pending invite.");
						return true;
					}
					
					p.sendMessage(ChatColor.GREEN + "Guild invite sent to " + toInvite.getDisplayName() + ChatColor.GREEN + ".");
					guildJoinProposals.put(toInvite, g);
					
					List<String> acceptText = new ArrayList<>();
					acceptText.add(ChatColor.GREEN + "Accepts the guild join request.");
					acceptText.add(ChatColor.GREEN + "/guilds accept");
					
					List<String> denyText = new ArrayList<>();
					denyText.add(ChatColor.RED + "Rejects the guild join request.");
					denyText.add(ChatColor.RED + "/guilds deny");
					
					FancyMessage inviteSpeil = new FancyMessage("You have been invited to the guild \"" + g.getName() + "\".\n").color(ChatColor.GREEN);
					inviteSpeil.then(ChatColor.BOLD + "Accept").color(ChatColor.GREEN).tooltip(acceptText).suggest("/guilds accept");
					inviteSpeil.then(ChatColor.BOLD + " - ");
					inviteSpeil.then(ChatColor.BOLD + "Deny").color(ChatColor.RED).tooltip(denyText).suggest("/guilds deny");
					
					inviteSpeil.send(toInvite);
				}
			}
			else if(cmd.equals("claim"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You must be in a guild to claim land.");
						return true;
					}
					
					GuildRank rank = g.getMemberRank(p.getUniqueId());
					
					if(rank.lessThan(GuildRank.KNIGHT))
					{
						p.sendMessage(ChatColor.RED + "You are not qualified enough in your guild to do this.");
						return true;
					}
					
					Chunk c = p.getLocation().getChunk();
					if(g.getOwnedChunks().contains(c))
					{
						p.sendMessage(ChatColor.RED + "Your guild already owns this chunk.");
						return true;
					}
					else
					{
						if(g.shouldAddChunk(c))
						{
							g.addOwnedChunk(c);
							p.sendMessage(ChatColor.GREEN + "Area added to claim!");
							try 
							{
								plugin.getGuildManager().saveGuilds();
							} 
							catch (IOException e) 
							{
								e.printStackTrace();
							}
						}
						else
						{
							p.sendMessage(ChatColor.RED + "You are out of availible claims (" + g.getMaxClaims() + ").");
						}
						
						return true;
					}
				}
			}
			else if(cmd.equals("delclaim") || cmd.equals("rmclaim") || cmd.equals("unclaim"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You must be in a guild to delete a land claim.");
						return true;
					}
					
					GuildRank rank = g.getMemberRank(p.getUniqueId());
					
					if(rank.lessThan(GuildRank.KNIGHT))
					{
						p.sendMessage(ChatColor.RED + "You are not qualified enough in your guild to do this.");
						return true;
					}
					
					Chunk c = p.getLocation().getChunk();
					if(!g.getOwnedChunks().contains(c))
					{
						p.sendMessage(ChatColor.RED + "Your guild doesn't own this chunk.");
						return true;
					}
					else
					{
						g.removeOwnedChunk(c);
						p.sendMessage(ChatColor.GREEN + "Area remove from claims!");
						
						try 
						{
							plugin.getGuildManager().saveGuilds();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
						return true;
					}
				}
			}
			else if(cmd.equals("sethome"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You are not in a guild.");
						return false;
					}
					
					GuildRank rank = g.getMemberRank(p.getUniqueId());
					
					if(rank.lessThan(GuildRank.COMMANDER))
					{
						p.sendMessage(ChatColor.RED + "You are not qualified enough in your guild to do this.");
						return true;
					}
					
					Location l = p.getLocation();
					if(g.getOwnedChunks().contains(l.getChunk()))
					{
						g.setHome(l);
						p.sendMessage(ChatColor.GREEN + "Home Set");
						
						try 
						{
							plugin.getGuildManager().saveGuilds();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}
					else
					{
						p.sendMessage(ChatColor.RED + "You must set a home within your claimed territory.");
					}
				}
			}
			else if(cmd.equals("home"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You are not in a guild.");
						return false;
					}
					
					if(g.getHome() == null)
					{
						p.sendMessage(ChatColor.RED + "Your guild has no home set.");
						return true;
					}
					
					p.teleport(g.getHome());
					p.sendMessage(ChatColor.GREEN + "Woosh");
				}
			}
			else if(cmd.equals("leave"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					if(g != null)
					{
						g.removeMember(p.getUniqueId());
						p.sendMessage(ChatColor.GREEN + "You have left the guild \"" + g.getName() + "\".");
						
						try 
						{
							plugin.getGuildManager().saveGuilds();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
						
						plugin.updateTabList();
						return true;
					}
					else
					{
						p.sendMessage(ChatColor.RED + "You are not in a guild.");
					}
				}
			}
			else if(cmd.equals("borders"))
			{
				if(iop(sender))
				{
					Player p = (Player) sender;
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You are not in a guild!");
						return false;
					}
					
					g.showBorders();
				}
			}
			else if(cmd.equals("balance"))
			{
				if(iop(sender))
				{
					Player p = (Player) sender;
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You are not in a guild!");
						return true;
					}
					
					p.sendMessage(ChatColor.GREEN + "Guild Balance: $" + g.getBalance());
				}
			}
			else if(cmd.equals("deposit"))
			{
				if(iop(sender))
				{
					Player p = (Player) sender;
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You are not in a guild!");
						return true;
					}
					
					if(args.length < 2)
					{
						p.sendMessage(ChatColor.RED + "You must specify the amount to deposit.");
						return true;
					}
					
					double amt;
					
					if(!Helper.isDouble(args[1]) || (amt = Double.parseDouble(args[1])) <= 0)
					{
						p.sendMessage(ChatColor.RED + "You must give a valid amount of money.");
						return true;
					}
					
					if(!plugin.getEcononomy().withdrawPlayer((OfflinePlayer)p, amt).transactionSuccess())
					{
						p.sendMessage(ChatColor.RED + "You do not have the proper funds.");
						return true;
					}
					
					g.deposit(amt);
					p.sendMessage(ChatColor.GREEN + "$" + amt + " successfully deposited into your guild's balance.");
					
					for(UUID uuid : g.getMembers())
					{
						Player pToSend = Bukkit.getPlayer(uuid);
						if(pToSend.isOnline())
						{
							pToSend.sendMessage(ChatColor.GREEN + p.getDisplayName() + ChatColor.GREEN + " has deposited $" + amt + " into the guild's balance.");
						}
					}
					
					try 
					{
						plugin.getGuildManager().saveGuilds();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}
			else if(cmd.equals("withdraw"))
			{
				if(iop(sender))
				{
					Player p = (Player) sender;
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You are not in a guild!");
						return true;
					}
					
					GuildRank rank = g.getMemberRank(p.getUniqueId());
					
					if(rank.lessThan(GuildRank.COMMANDER))
					{
						p.sendMessage(ChatColor.RED + "You are not qualified enough in your guild to do this.");
						return true;
					}
					
					if(args.length < 2)
					{
						p.sendMessage(ChatColor.RED + "You must specify the amount to withdraw.");
						return true;
					}
					
					double amt;
					
					if(!Helper.isDouble(args[1]) || (amt = Double.parseDouble(args[1])) <= 0)
					{
						p.sendMessage(ChatColor.RED + "You must give a valid amount of money.");
						return true;
					}
					
					if(!g.canWithdrawAmount(amt))
					{
						p.sendMessage(ChatColor.RED + "Your guild does not have the proper funds.");
						return true;
					}
					
					if(plugin.getEcononomy().depositPlayer((OfflinePlayer)p, amt).transactionSuccess())
					{
						g.withdrawAmount(amt);
						p.sendMessage(ChatColor.GREEN + "$" + amt + " successfully withdrawn from the guild's balance.");
						
						for(UUID uuid : g.getMembers())
						{
							Player pToSend = Bukkit.getPlayer(uuid);
							if(pToSend.isOnline())
							{
								pToSend.sendMessage(ChatColor.GREEN + p.getDisplayName() + ChatColor.GREEN + " has withdrawn $" + amt + " from the guild's balance.");
							}
						}
					}
					else
					{
						p.sendMessage(ChatColor.RED + "Unable to withdraw money from guild's balance.");
						return true;
					}
					
					try 
					{
						plugin.getGuildManager().saveGuilds();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}
			else if(cmd.equals("demote"))
			{
				if(iop(sender))
				{
					Player p = (Player) sender;
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You are not in a guild!");
						return true;
					}
					
					GuildRank rank = g.getMemberRank(p.getUniqueId());
					
					if(rank.lessThan(GuildRank.COMMANDER))
					{
						p.sendMessage(ChatColor.RED + "You are not qualified enough in your guild to do this.");
						return true;
					}
					
					if(args.length < 2)
					{
						p.sendMessage(ChatColor.RED + "You must specify who to demote.");
						return true;
					}
					
					Player demotee = Bukkit.getPlayer(args[1]);
					if(demotee == null)
					{
						p.sendMessage(ChatColor.RED + "Unable to find " + args[1] + ".");
						return true;
					}
					
					Guild demoteeGuild = plugin.getGuildManager().getGuildFromUUID(demotee.getUniqueId());
					if(!g.equals(demoteeGuild))
					{
						p.sendMessage(ChatColor.RED + "That player isn't in your guild.");
						return true;
					}
					
					GuildRank demoteeRank = g.getMemberRank(demotee.getUniqueId());
					
					if(demoteeRank.lessThan(rank))
					{
						if(demoteeRank.getValue() == 0)
						{
							p.sendMessage(ChatColor.RED + "That player is already the lowest rank possible.");
						}
						else
						{
							g.setMemberRank(p.getUniqueId(), GuildRank.values()[demoteeRank.getValue() - 1]);
							
							try 
							{
								plugin.getGuildManager().saveGuilds();
							} 
							catch (IOException e) 
							{
								e.printStackTrace();
							}
						}
						return true;
					}
					else
					{
						p.sendMessage(ChatColor.RED + "Your rank must be lower than that person's rank to demote them.");
						return true;
					}
				}
			}
			else if(cmd.equals("promote"))
			{
				if(iop(sender))
				{
					Player p = (Player) sender;
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You are not in a guild!");
						return true;
					}
					
					GuildRank rank = g.getMemberRank(p.getUniqueId());
					
					if(rank.lessThan(GuildRank.COMMANDER))
					{
						p.sendMessage(ChatColor.RED + "You are not qualified enough in your guild to do this.");
						return true;
					}
					
					if(args.length < 2)
					{
						p.sendMessage(ChatColor.RED + "You must specify who to promote.");
						return true;
					}
					
					Player promotee = Bukkit.getPlayer(args[1]);
					if(promotee == null)
					{
						p.sendMessage(ChatColor.RED + "Unable to find " + args[1] + ".");
						return true;
					}
					
					if(promotee.equals(p))
					{
						p.sendMessage(ChatColor.RED + "I get it - you're egotistical. Sadly though, you cannot promote yourself.");
						return true;
					}
					
					Guild promoteeGuild = plugin.getGuildManager().getGuildFromUUID(promotee.getUniqueId());
					if(!g.equals(promoteeGuild))
					{
						p.sendMessage(ChatColor.RED + "That player isn't in your guild.");
						return true;
					}
					
					GuildRank promoteeRank = g.getMemberRank(promotee.getUniqueId());
					
					if(promoteeRank.lessThan(rank))
					{
						if(promoteeRank.getValue() == GuildRank.KING.getValue())
						{
							p.sendMessage(ChatColor.RED + "That player is already the highest rank possible.");
						}
						else if(promoteeRank.getValue() == GuildRank.KING.getValue() - 1)
						{
							p.sendMessage(ChatColor.RED + "You cannot promote someone to king - you must transfer ownership by using /guild transfer [player]");
							p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Be Warned" + ChatColor.RESET + "" + ChatColor.RED + "--- Once done only the new king can give you back ownership.");
						}
						else
						{
							g.setMemberRank(p.getUniqueId(), GuildRank.values()[promoteeRank.getValue() + 1]);
							p.sendMessage(ChatColor.GREEN + "You have successfully promoted " + promotee.getDisplayName() + " to " + Helper.firstLetterUpper(promoteeRank.name().toLowerCase()) + ".");
							try 
							{
								plugin.getGuildManager().saveGuilds();
							} 
							catch (IOException e) 
							{
								e.printStackTrace();
							}
						}
					}
					else
					{
						p.sendMessage(ChatColor.RED + "Your rank must be lower than that person's rank to demote them.");
						return true;
					}
				}
			}
			else if(cmd.equals("rank"))
			{
				if(iop(sender))
				{
					Player p = (Player) sender;
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You are not in a guild!");
						return true;
					}
					
					p.sendMessage(ChatColor.GREEN + "You are the rank of \"" + Helper.firstLetterUpper(g.getMemberRank(p.getUniqueId()).name().toLowerCase() + "\" in your guild."));
				}
			}
			else if(cmd.equals("help"))
			{
				if(args.length < 2)
				{
					displayHelp(sender, 1);
				}
				else
				{
					if(Helper.isInt(args[1]))
					{
						displayHelp(sender, Integer.parseInt(args[1]));
					}
					else
					{
						sender.sendMessage(ChatColor.RED + args[1] + " is not a valid page number.");
					}
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Invalid command, do /guilds help to see the help page.");
			}
		}
		
		// Guild Chat Stuff
		else if(cmd.equals("guildchat") || cmd.equals("gc") && perm(sender, "guilds.guildchat"))
		{
			return gc(sender);
		}
		
		return true;
	}
	
	private boolean gc(CommandSender sender)
	{
		if(iop(sender))
		{
			Player p = (Player)sender;
			
			if(!plugin.getGuildManager().playerHasGuild(p))
			{
				p.sendMessage(ChatColor.RED + "You are not in a guild!");
				return true;
			}
			
			if(plugin.getGuildManager().getGuildChatters().contains(p))
			{
				p.sendMessage(ChatColor.AQUA + "Guild Chat: Off");
				plugin.getGuildManager().getGuildChatters().remove(p);
			}
			else
			{
				p.sendMessage(ChatColor.AQUA + "Guild Chat: On");
				plugin.getGuildManager().getGuildChatters().add(p);
			}
		}
		return true;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		if(possibleGuildDeletions.contains(p) && plugin.getGuildManager().playerHasGuild(p))
		{
			String msg = e.getMessage();
			Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
			if(msg.equalsIgnoreCase(g.getName()))
			{
				if(plugin.getGuildManager().playerHasGuild(p))
				{
					try
					{
						plugin.getGuildManager().deleteGuild(g);
						p.sendMessage(ChatColor.GREEN + "Guild successfully deleted.");
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
						p.sendMessage(ChatColor.RED + "A server error ocurred whilst trying to delete your guild");
					}
					e.setCancelled(true);
					
					plugin.updateTabList();
				}
			}
			else
			{
				p.sendMessage(ChatColor.RED + "Guild deletion cancelled");
				e.setCancelled(true);
			}
		}
		
		possibleGuildDeletions.remove(p);
	}
	
	private static void displayHelp(CommandSender sender, int page) 
	{
		final int MAX_PAGE = 2;
		if(page > MAX_PAGE)
		{
			displayHelp(sender, MAX_PAGE);
			return;
		}
		sender.sendMessage(ChatColor.GREEN + "=== Guilds Help (" + page + "/" + MAX_PAGE + ") ===");
		
		// Note: Each page has 6
		switch(page)
		{
			case 1:
			{
				sender.sendMessage(ChatColor.GREEN + "- /gc - Enter Guild Chat (used without /guilds at the start)");
				sender.sendMessage(ChatColor.GREEN + "- new/create [name] - creates a new guild");
				sender.sendMessage(ChatColor.GREEN + "- delete - deletes your guild");
				sender.sendMessage(ChatColor.GREEN + "- invite [player] - Invites a player to your guild");
				sender.sendMessage(ChatColor.GREEN + "- claim - Claims the chunk you are standing on to your guild");
				sender.sendMessage(ChatColor.GREEN + "- unclaim - Removes the chunk you are standing on from your guild's land claims");
				sender.sendMessage(ChatColor.GREEN + "- help [pagenum]");
				break;
			}
			case 2:
			{
				sender.sendMessage(ChatColor.GREEN + "- promote - Promotes a player in your guild that is below your rank");
				sender.sendMessage(ChatColor.GREEN + "- demote - Demotes a player in your guild that is below your rank");
				sender.sendMessage(ChatColor.GREEN + "- rank - View your rank in your guild");
				sender.sendMessage(ChatColor.GREEN + "- deposit - Deposits a specified amount into your guild's vault");
				sender.sendMessage(ChatColor.GREEN + "- withdraw - Withdraws a specified amount from your guild's vault");
				sender.sendMessage(ChatColor.GREEN + "- transfer - Transfers the ownership of the guild");
				break;
			}
			default:
				displayHelp(sender, MAX_PAGE);
		}
	}

	private static boolean iop(CommandSender sender)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Sorry, but you must be a player to use this command");
			return false;
		}
		return true;
	}
	
	private static boolean perm(CommandSender p, String perm)
	{
		if(p.hasPermission(perm))
			return true;
		else
		{
			p.sendMessage(ChatColor.RED + "Insufficient permissions!");
			return false;
		}
	}
}
