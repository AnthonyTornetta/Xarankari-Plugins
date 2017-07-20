package com.cornchipss.buildbattle;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.meta.FireworkMeta;

public class CornyListener implements Listener
{
	BuildBattle bb;
	
	Color[] colors = 
		{
			Color.AQUA,
			Color.BLACK,
			Color.BLUE,
			Color.FUCHSIA,
			Color.GRAY,
			Color.GREEN,
			Color.LIME,
			Color.MAROON,
			Color.NAVY,
			Color.OLIVE,
			Color.ORANGE,
			Color.PURPLE,
			Color.RED,
			Color.SILVER,
			Color.TEAL,
			Color.WHITE,
			Color.YELLOW
		};
		
	
	Material blackListedBlocks[] =
		{
			Material.BEDROCK,
			Material.BARRIER
		};
	
	Material storageBlocks[] =
		{
			Material.CHEST,
			Material.FURNACE,
			Material.WORKBENCH, // The clear thing doesn't apply to this
			Material.ANVIL,
			Material.DROPPER,
			Material.DISPENSER,
			Material.HOPPER,
			Material.STORAGE_MINECART,
			Material.HOPPER_MINECART,
			Material.BREWING_STAND,
			Material.BEACON,
			Material.ENDER_CHEST,
			Material.BLACK_SHULKER_BOX,
			Material.BLUE_SHULKER_BOX,
			Material.BROWN_SHULKER_BOX,
			Material.CYAN_SHULKER_BOX,
			Material.GRAY_SHULKER_BOX,
			Material.GREEN_SHULKER_BOX,
			Material.LIGHT_BLUE_SHULKER_BOX,
			Material.LIME_SHULKER_BOX,
			Material.MAGENTA_SHULKER_BOX,
			Material.ORANGE_SHULKER_BOX,
			Material.PINK_SHULKER_BOX,
			Material.PURPLE_SHULKER_BOX,
			Material.RED_SHULKER_BOX,
			Material.SILVER_SHULKER_BOX,
			Material.WHITE_SHULKER_BOX,
			Material.YELLOW_SHULKER_BOX
		};
	
	public CornyListener(BuildBattle bb)
	{
		this.bb = bb;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		if(p.getName().toLowerCase().equals("joey_dev"))
		{
			Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Welcome the Great and Powerful " + ChatColor.GREEN + "OZ" + ChatColor.GOLD + "!");
			
			for(int i = 0; i < 10; i++)
			{
				//Spawn the Firework, get the FireworkMeta.
	            Firework fw = (Firework)p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
	            FireworkMeta fwm = fw.getFireworkMeta();
	           
	            //Our random generator
	            Random r = new Random();
	            
	            //Get the type
	            int rt = r.nextInt(4) + 1;
	            Type type = Type.BALL;       
	            if (rt == 1) type = Type.BALL;
	            if (rt == 2) type = Type.BALL_LARGE;
	            if (rt == 3) type = Type.BURST;
	            if (rt == 4) type = Type.CREEPER;
	            if (rt == 5) type = Type.STAR;
	           
	            //Get our random colours   
	            int r1i = r.nextInt(17);
	            int r2i = r.nextInt(17);
	            Color c1 = colors[r1i];
	            Color c2 = colors[r2i];
	           
	            //Create our effect with this
	            FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
	           
	            //Then apply the effect to the meta
	            fwm.addEffect(effect);
	           
	            //Generate some random power and set it
	            int rp = r.nextInt(2) + 1;
	            fwm.setPower(rp);
	           
	            //Then apply this to our rocket
	            fw.setFireworkMeta(fwm);
			}
		}
		
		if(p.getName().toLowerCase().equals("cornchipss"))
		{
			Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "! The Almighty Corn" + ChatColor.YELLOW + "chip" + ChatColor.GOLD + " has joined the server !");
			
			for(int i = 0; i < 10; i++)
			{
				//Spawn the Firework, get the FireworkMeta.
	            Firework fw = (Firework)p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
	            FireworkMeta fwm = fw.getFireworkMeta();
	           
	            //Our random generator
	            Random r = new Random();
	            
	            //Get the type
	            int rt = r.nextInt(4) + 1;
	            Type type = Type.BALL;       
	            if (rt == 1) type = Type.BALL;
	            if (rt == 2) type = Type.BALL_LARGE;
	            if (rt == 3) type = Type.BURST;
	            if (rt == 4) type = Type.CREEPER;
	            if (rt == 5) type = Type.STAR;
	           
	            //Get our random colours   
	            int r1i = r.nextInt(17);
	            int r2i = r.nextInt(17);
	            Color c1 = colors[r1i];
	            Color c2 = colors[r2i];
	           
	            //Create our effect with this
	            FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
	           
	            //Then apply the effect to the meta
	            fwm.addEffect(effect);
	           
	            //Generate some random power and set it
	            int rp = r.nextInt(2) + 1;
	            fwm.setPower(rp);
	           
	            //Then apply this to our rocket
	            fw.setFireworkMeta(fwm);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLeave(PlayerQuitEvent e)
	{
		if(e.getPlayer().getName().toLowerCase().equals("joey_dev"))
		{
			Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "The Powerful " + ChatColor.GREEN + "OZ" + ChatColor.GOLD + " has Spoken!");
		}
		if(bb.getPlayers().contains(e.getPlayer()) && bb.isRunning())
		{
			Player p = e.getPlayer();
			p.setGameMode(GameMode.SURVIVAL);
			p.getInventory().clear();
			p.getInventory().setContents(bb.getPlayerInventories().get(p));
			
			bb.getPlayers().remove(p);
			bb.getPlayerInventories().remove(p);
			bb.getPlotsAssigned().remove(p);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerSendCommand(PlayerCommandPreprocessEvent e)
	{
		if(!bb.getPlayers().contains(e.getPlayer()) || e.getPlayer().hasPermission("bb.sendcommand"))
			return; // Player isn't in the build battle
		
		if(bb.isRunning())
		{
			if(!e.getMessage().split(" ")[0].equals("/bb") && !e.getMessage().split(" ")[0].equals("/buildbattle"))
			{
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "You cannot execute non-buildbattle commands during a build battle!");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onEnterPortal(PlayerPortalEvent e) // We don't want dimension hopping during a build battle :/
	{
        if(bb.getPlayers().contains(e.getPlayer()) && bb.isRunning())
        {
        	e.getPlayer().sendMessage(ChatColor.RED + "Focus on building, not dimensionional warping.");
        	e.setCancelled(true);
        }
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent e)
	{ 
        if(e.getCause().equals(TeleportCause.ENDER_PEARL) && bb.getPlayers().contains(e.getPlayer()) && bb.isRunning())
        {
        	// How rude of them to try and teleport out...
        	e.getPlayer().sendMessage(ChatColor.GOLD + "Corn" + ChatColor.YELLOW + "chip" + ChatColor.DARK_PURPLE + " spoke with the ender dragon. She says you can't teleport right now.");
            e.setCancelled(true);
        }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDropItem(PlayerDropItemEvent e)
	{
		if(!bb.getPlayers().contains(e.getPlayer()))
			return; // Player isn't in the build battle
		
		if(bb.isRunning())
		{
			e.getItemDrop().remove(); // Remove it instantly, Don't want them throwing items on the ground from gmc ;)
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if(bb.isRunning())
		{
			if(!bb.getPlayers().contains(e.getPlayer()))
				return;
			
			Action a = e.getAction();
			
			if(e.getItem() == null)
				return;
			
			if(a.equals(Action.RIGHT_CLICK_BLOCK))
			{
				Material bm = e.getClickedBlock().getType();
				for(Material m : storageBlocks)
				{
					if(m.equals(bm))
					{
						e.getPlayer().sendMessage(ChatColor.RED + "You cannot access any storage containers during a build battle!");
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			if(((Player)e.getEntity()).getName().toLowerCase().equals("cornchipss") || ((Player)e.getEntity()).getName().toLowerCase().equals("joey_dev"))
			{
				Player p = (Player)e.getEntity();
				Location l = p.getLocation();
				World w = p.getWorld();
				
				for(Particle part : Particle.values())
				{
					w.spawnParticle(part, l, 20);
				}
				
				int radius = 20;
		        
		        double x;
		        double y = l.getY();
		        double z;
		               
		        for (double i = 0.0; i < 360.0; i += 2) 
		        {
		        	double angle = i * Math.PI / 180;
		            x = (int)(l.getX() + radius * Math.cos(angle));
		            z = (int)(l.getZ() + radius * Math.sin(angle));
		     
		            w.strikeLightning(new Location(w, x, y, z));
		        }
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBreak(BlockBreakEvent e)
	{
		if(bb.isRunning())
		{
			if(!bb.getPlayers().contains(e.getPlayer()))
				return;
			
			Block b = e.getBlock();
			Player p = e.getPlayer();
						
			for(int i = 0; i < blackListedBlocks.length; i++)
			{
				if(b.getType().equals(blackListedBlocks[i]))
				{
					break;
				}
				if(i + 1 == blackListedBlocks.length)
					return; // It's not blacklisted so we don't care
			}
			
			if(bb.getPlayers().contains(p))
			{
				if(!p.hasPermission("bb.break.blacklisted"))
				{
					p.sendMessage(ChatColor.RED + "You cannot break that block during a build battle");
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPlace(BlockPlaceEvent e)
	{
		if(bb.isRunning())
		{
			Block b = e.getBlock();
			Player p = e.getPlayer();
						
			for(int i = 0; i < blackListedBlocks.length; i++)
			{
				if(b.getType().equals(blackListedBlocks[i]))
				{
					break;
				}
				if(i + 1 == blackListedBlocks.length)
					return; // It's not blacklisted so we don't care
			}
			
			if(bb.getPlayers().contains(p))
			{
				if(!p.hasPermission("bb.place.blacklisted"))
				{
					p.sendMessage(ChatColor.RED + "You cannot place that block during a build battle");
					e.setCancelled(true);
				}
			}
		}
	}
}
