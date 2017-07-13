package com.cornchipss.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.cornchipss.main.helpers.Helper;
import com.cornchipss.main.helpers.LoreParser;
import com.cornchipss.main.items.Items;

public class PluginListener implements Listener
{
	Main main;
	Random rdm;
	private List<Integer> bullets = new ArrayList<>();
	
	public PluginListener(Main main)
	{
		this.main = main;
	}
	
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        
    }
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e)
	{
		
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		ItemStack core = p.getInventory().getItemInOffHand();
		if(LoreParser.getId(core) == Items.MAGIC_HOLDER_ID)
		{
			double distTraveled = Helper.vectorDistance(e.getFrom(), e.getTo());
			int amount = (int) Math.round(distTraveled * 5 * distTraveled);
			LoreParser.addMana(p, amount);
		}
		
		// FOR SLOW FALLING!
		// Levitation will shoot you up at 127
		// Levitation will work backwards at 128
		// Then the higher you put it the slower you fall
		// At 255 you just dont fall or go up
		ItemStack boots = p.getInventory().getBoots();
		if(LoreParser.getId(boots) == Items.LESS_GRAV_ID)
		{
			
			Block block, control;
			Vector dir = e.getPlayer().getVelocity().setY(2);
			if(e.getTo().getY() > e.getFrom().getY())
			{
				if(LoreParser.takeMana(LoreParser.getMana(boots), p))
				{
					block = p.getWorld().getBlockAt(new Location(p.getWorld(), e.getTo().getX(), e.getTo().getY() + 2, e.getTo().getZ()));
					control = p.getWorld().getBlockAt(new Location(p.getWorld(), e.getTo().getX(), e.getTo().getY() - 2, e.getTo().getZ()));
				    if(!(block.getType() != Material.AIR || control.getType() == Material.AIR))
					{
						e.getPlayer().setVelocity(dir);
					}
				}
			}
			
			// They are falling
			if(e.getTo().getY() < e.getFrom().getY())
			{
				if(LoreParser.takeMana(LoreParser.getMana(boots), p))
				{
					p.removePotionEffect(PotionEffectType.LEVITATION); // So it doesnt turn off and on noticably
					p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 1 * 20, 251));
					p.setFallDistance(0);
				}
			}
			else
			{
				p.removePotionEffect(PotionEffectType.LEVITATION);
			}
			
		}
	}
	
	@EventHandler
    public void onProjectileHit(ProjectileHitEvent e) 
	{
        // If the projectile was a snowball
        if(e.getEntity() instanceof Snowball) 
        {
        	if(e.getHitEntity() != null)
        	{
	            int entityId = e.getEntity().getEntityId();
	         
	            // If this snowball is a bullet
	            if(bullets.contains(entityId)) 
	            {
	                // Clear this entity id
	                bullets.remove((Integer) entityId);
	             
	                // Create an explosion where the snowball hit
	                //e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 2F);
	                if(e.getHitEntity().getType() != EntityType.PLAYER)
	                	e.getHitEntity().addPassenger(e.getHitEntity().getWorld().spawnEntity(e.getHitEntity().getLocation(), e.getHitEntity().getType()));
	            }
        	}
        }
    }
	
    @EventHandler
    public void checkDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) 
        {
        	Player p = (Player)e.getEntity();
        	if(e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.FIRE_TICK || e.getCause() == DamageCause.LAVA) 
            {
            	if(p.getInventory().getChestplate() != null)
            	{
            		if(p.getInventory().getChestplate().getItemMeta().getLore() != null)
            		{
            			if(LoreParser.getId(p.getInventory().getChestplate()) == Items.FIRE_RESIST_ID)
            			{
            				if(LoreParser.takeMana(p.getInventory().getChestplate(), p))
            				{
	            				p.setFireTicks(0);
	            				e.setCancelled(true);
            				}
            			}
            		}
            	}
            }
            
        	if(e.getCause() == DamageCause.LIGHTNING || e.getCause() == DamageCause.BLOCK_EXPLOSION || e.getCause() == DamageCause.ENTITY_EXPLOSION) 
            {
            	if(p.getInventory().getHelmet() != null)
            	{
            		if(p.getInventory().getHelmet().getItemMeta().getLore() != null)
            		{
            			if(LoreParser.getId(p.getInventory().getHelmet()) == Items.LIGHTNING_RESIST_ID)
            			{
            				LoreParser.takeMana(p.getInventory().getHelmet(), p);
            				e.setCancelled(true);
            			}
            		}
            	}
            }
        	
        	if(e.getCause() == DamageCause.FALL)
        	{
        		if(p.getInventory().getBoots() != null)
        		{
        			if(p.getInventory().getBoots().getItemMeta().getLore() != null)
        			{
        				if(LoreParser.getId(p.getInventory().getBoots()) == Items.NO_FALL_ID)
        				{
        					if(LoreParser.takeMana(p.getInventory().getBoots(), p))
        					{
        						e.setCancelled(true);
        					}
        				}
        			}
        		}
        	}
        }
    }
    
    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e)
    {
    	
    }
	
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		if(LoreParser.getId(item) == Items.MAGIC_HOLDER_ID)
		{
			e.setCancelled(true);
			return;
		}
		runThroughEverything(e, p, item);
	}
	
	@SuppressWarnings("deprecation")
	private void runThroughEverything(PlayerInteractEvent e, Player p, ItemStack item)
	{
		if(item == null || item.getType() == Material.AIR)
			return;
		if(LoreParser.getId(item) == Items.MAGIC_HOLDER_ID)
			return;
		if(item.getType() == Material.DIAMOND_HOE)
		{
			if(item.getItemMeta().getLore() != null)
			{
				if(LoreParser.takeMana(item, p))
				{
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 0.3f, 1f);
					if(LoreParser.getId(item) == Items.LIGHTNING_ID)
					{
						p.getWorld().strikeLightning(getPlayerSightLocation(p, 100).getLocation());
						updateItem(item);
					}
					else if(LoreParser.getId(item) == Items.EXPLOSION_ID)
					{
						p.getWorld().createExplosion(bf(getPlayerSightLocation(p, 50)).getLocation(), 5.0f);
						updateItem(item);

					}
					else if(LoreParser.getId(item) == Items.SHEEP_ID)
					{
						//p.getWorld().spawnEntity(getPlayerSightLocation(p, 50).getLocation(), EntityType.SHEEP);
						
						//BlockFace face = getClosestFace((float)Math.toDegrees(Math.atan2(p.getLocation().getBlockX() - getPlayerSightLocation(p, 50).getX(), getPlayerSightLocation(p, 50).getZ() - p.getLocation().getBlockZ())));
						BlockFace face = getPlayerSightLocation(p, 50).getFace(p.getLocation().getBlock());
						p.getWorld().spawnEntity(blockFaceHelper(face, getPlayerSightLocation(p, 50)), EntityType.SHEEP);
						updateItem(item);
					}
					else if(LoreParser.getId(item) == Items.REGEN_ID)
					{
						p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 20, 3));
						updateItem(item);
					}
					else if(LoreParser.getId(item) == Items.SPEED_ID)
					{
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30 * 20, 3));
						updateItem(item);
					}
					else if(LoreParser.getId(item) == Items.INVIS_ID)
					{
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30 * 20, 0));
						updateItem(item);
					}
					else if(LoreParser.getId(item) == Items.INVIS_ID)
					{
						p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 30 * 20, 1));
						updateItem(item);
					}
					else if(LoreParser.getId(item) == Items.HEALTH_ID)
					{
						p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1)); // 1 = 4 hearts
						updateItem(item);
					}
					else if(LoreParser.getId(item) == Items.JUMP_ID)
					{
						p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 20, 3));
						updateItem(item);
					}
					else if(LoreParser.getId(item) == Items.WATER_BREATH_ID)
					{
						p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 30 * 20, 0));
						updateItem(item);
					}
					else if(LoreParser.getId(item) == Items.LEVITATE_ID)
					{
						p.setFallDistance(0);
						if(p.hasPotionEffect(PotionEffectType.LEVITATION))
						{
							p.removePotionEffect(PotionEffectType.LEVITATION);
							return;
						}
						p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 10 * 20, 3));
						updateItem(item);
					}
					else if(LoreParser.getId(item) == Items.FLY_ID)
					{
						if(p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR)
						{
							// MUST do it like this or it gives you an error because the player isnt allowed to fly
							if(p.getAllowFlight())
							{
								p.setFlying(false);
								p.setAllowFlight(false);
							}
							else
							{
								p.setAllowFlight(true);
								p.setFlying(true);
							}
						}
					}
					else if(LoreParser.getId(item) == Items.MINING_ID)
					{
						Block b = getPlayerSightLocation(p, 50);
						if(b.getType() != Material.BEDROCK && b.getType() != Material.AIR)
						{
							BlockBreakEvent bbe = new BlockBreakEvent(b, p);
							Bukkit.getPluginManager().callEvent(bbe);
							if(!bbe.isCancelled())
								b.breakNaturally();
						}
						updateItem(item);
					}
					else if(LoreParser.getId(item) == Items.GUN_ID)
					{
						Snowball bullet = p.launchProjectile(Snowball.class, p.getLocation().getDirection());
						bullets.add(bullet.getEntityId());
					}
					else if(LoreParser.getId(item) == Items.GROWTH_ID)
					{
						Block b = getPlayerSightLocation(p, 50);
						byte grown = 0x7; // Note that beatroot is stupid and their byte value is 0x3.
						Material t = b.getType();
						if(t == Material.CROPS || t == Material.PUMPKIN_STEM || t == Material.CARROT || t == Material.POTATO || t == Material.MELON_STEM)
						{
							b.setData(grown);
						}
						else if (t == Material.BEETROOT_BLOCK)
						{
							b.setData((byte) 0x3);
						}
						else
						{
							LoreParser.addMana(p, LoreParser.getMana(item));
						}
						updateItem(item);
					}
				}
				e.setCancelled(true);
			}
		}
	}
	
	// TODO fix this
	// This doesnt work that well either ;(
	private Location blockFaceHelper(BlockFace bf, Block block)
	{
		if(bf == BlockFace.WEST)
			return (block.getLocation().subtract(0, 0, 1));
		if(bf == BlockFace.SOUTH)
			return (block.getLocation().subtract(0, 0, 1));
		if(bf == BlockFace.NORTH)
			return (block.getLocation().add(0, 0, 1));
		if(bf == BlockFace.EAST)
			return (block.getLocation().add(0, 1, 0));
		if(bf == BlockFace.UP)
			return (block.getLocation().subtract(0, 1, 0));
		else
			return (block.getLocation().add(0, 1, 0));
	}
	
	// TODO Finish This
	// Thanks for making me NOT want to die: https://bukkit.org/threads/from-which-direction-is-player-looking-at-the-block.14153/ - Crash
	/*
	private BlockFace getClosestFace(float direction)
	{
        direction = direction % 360;

        if(direction < 0)
            direction += 360;

        direction = Math.round(direction / 45);

        switch((int)direction){

            case 0:
                return BlockFace.WEST;
            case 1:
                return BlockFace.NORTH_WEST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.NORTH_EAST;
            case 4:
                return BlockFace.EAST;
            case 5:
                return BlockFace.SOUTH_EAST;
            case 6:
                return BlockFace.SOUTH;
            case 7:
                return BlockFace.SOUTH_WEST;
            default:
                return BlockFace.WEST;
        }
    }
	*/
	private Block bf(Block b)
	{
		return b.getRelative(BlockFace.UP);
	}
	
	private void updateItem(ItemStack item)
	{
		item.setDurability((short)(LoreParser.getId(item) + 20));
	}
	
	@SuppressWarnings("deprecation")
	private Block getPlayerSightLocation(Player p, int range)
	{
		return p.getTargetBlock((HashSet<Byte>)null, range);
	}
}
