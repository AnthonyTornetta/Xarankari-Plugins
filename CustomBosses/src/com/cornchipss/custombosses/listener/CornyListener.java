package com.cornchipss.custombosses.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.custombosses.Debug;
import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.boss.handler.BossHandler;
import com.cornchipss.custombosses.boss.spawner.BossSpawnArea;
import com.cornchipss.custombosses.commands.InventoryHelper;
import com.cornchipss.custombosses.listener.events.BossDeathEvent;
import com.cornchipss.custombosses.listener.events.BossSpawnEvent;
import com.cornchipss.custombosses.util.Helper;
import com.cornchipss.custombosses.util.Reference;

public class CornyListener extends Debug implements Listener
{
	private BossHandler bossHandler;
	// Because in 1.9 there are two hands, and I only have access to one (1.8), I must ignore the second call because that's for the off hand
	// So I store a list of players that have called it and if they are in said list I ignore the event and remove them
	// TODO: Update to 1.12.2
	private List<Player> playerThatInteracted = new ArrayList<>();
	
	private Map<LivingBoss, List<Player>> playersListening = new HashMap<>();
	
	public CornyListener(BossHandler handler)
	{		
		this.bossHandler = handler;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void playerInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if(playerThatInteracted.contains(p))
		{
			playerThatInteracted.remove(p);
			return;
		}
		else
		{
			playerThatInteracted.add(p);
		}
		
		if(e.isCancelled())
			return;
		
		ItemStack itemHeld = p.getInventory().getItemInMainHand();
		
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK && itemHeld != null)
			return;
		
		for(Boss b : bossHandler.getLoadedBosses())
		{
			ItemStack spawnItem = b.getSpawnItem();		
			
			if(Helper.equiv(itemHeld, spawnItem))
			{
				LivingBoss newBoss = b.createLivingBoss();
				BossSpawnEvent bossSpawnEvent = new BossSpawnEvent(newBoss);
				
				Bukkit.getPluginManager().callEvent(bossSpawnEvent);
				if(bossSpawnEvent.isCancelled())
					return;
				
				// Make sure to spawn it before adding it, because it needs the location to serialize it
				newBoss.spawn(e.getClickedBlock().getLocation().add(0.0, 1.0, 0.0));
				debug("playerInteract", 87, newBoss.getEntity());
				bossHandler.addLivingBoss(newBoss);
				
				debug("entityDeath", bossHandler.getLivingBosses().size());
				
				itemHeld.setAmount(itemHeld.getAmount() - 1);
				break;
			}
		}
	}

	// For use with the command invenotries
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerClickInventory(InventoryClickEvent e)
	{
		Inventory inv = e.getInventory();
		Player p = (Player)e.getWhoClicked();
		
		if(inv.getName().equals(Reference.BOSS_LOCATIONS_GUI))
			e.setCancelled(true);
		else if(inv.getName().equals(Reference.BOSS_EGG_MENU_NAME))
		{
			e.setCancelled(true); // Don't want them taking my blocks >:(
			if(e.getCurrentItem() == null)
				return;
							
			if(e.getCurrentItem().getType() == Material.BARRIER)
				p.closeInventory();
			else
			{
				for(Boss b : bossHandler.getLoadedBosses())
				{
					if(b.getSpawnItem().equals(e.getCurrentItem()))
					{
						p.getInventory().addItem(e.getCurrentItem());
					}
				}
			}
		}
		else if(inv.getName().equals(Reference.BOSS_SPAWN_AREA_GUI))
		{
			e.setCancelled(true);
			if(e.getCurrentItem() == null)
				return;
			if((e.getCurrentItem().getDurability() == (short)13) && (e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE))) // 13 = green = ok
				p.closeInventory();
			else if((e.getCurrentItem().getDurability() == (short)14) && (e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE))) // 14 = red = discard
			{
				ItemStack bossSpawnArea = e.getInventory().getItem(InventoryHelper.BOSS_AREA_ITEMSTACK_SLOT_POSITION);
				List<String> lore = bossSpawnArea.getItemMeta().getLore();
				String[] locString1 = lore.get(0).split(", ");
				String[] locString1ZWorld = locString1[2].split("; ");
				
				Location l1 = new Location(Bukkit.getWorld(locString1ZWorld[1]), Double.parseDouble(locString1[0]), Double.parseDouble(locString1[1]), Double.parseDouble(locString1ZWorld[0]));
				
				String[] locString2 = lore.get(1).split(", ");
				String[] locString2ZWorld = locString2[2].split("; ");
				
				Location l2 = new Location(Bukkit.getWorld(locString2ZWorld[1]), Double.parseDouble(locString2[0]), Double.parseDouble(locString2[1]), Double.parseDouble(locString2ZWorld[0]));
				
				for(int i = 0; i < bossHandler.getSpawnAreas().size(); i++)
				{
					BossSpawnArea area = bossHandler.getSpawnAreas().get(i);
					Location areaLoc1 = area.getLocationX();
					Location areaLoc2 = area.getLocationY();
					
					if(l1.equals(areaLoc1) && l2.equals(areaLoc2))
					{
						bossHandler.removeSpawnArea(area);
						p.closeInventory();
						p.sendMessage(ChatColor.GOLD + "Boss Spawn Area Removed.");
						break;
					}
				}
			}
		}
		else if(inv.getName().equals(Reference.BOSS_SPAWN_AREAS_GUI))
		{
			if(e.getCurrentItem().getType() == Material.MAP)
			{
				e.setCancelled(true);
				ItemStack bossSpawnArea = e.getCurrentItem();
				List<String> lore = bossSpawnArea.getItemMeta().getLore();
				String[] locString1 = lore.get(0).split(", ");
				String[] locString1ZWorld = locString1[2].split("; ");
				
				Location l1 = new Location(Bukkit.getWorld(locString1ZWorld[1]), Double.parseDouble(locString1[0]), Double.parseDouble(locString1[1]), Double.parseDouble(locString1ZWorld[0]));
				
				String[] locString2 = lore.get(1).split(", ");
				String[] locString2ZWorld = locString2[2].split("; ");
				
				Location l2 = new Location(Bukkit.getWorld(locString2ZWorld[1]), Double.parseDouble(locString2[0]), Double.parseDouble(locString2[1]), Double.parseDouble(locString2ZWorld[0]));
				
				BossSpawnArea area = null;
				
				for(int i = 0; i < bossHandler.getSpawnAreas().size(); i++)
				{
					area = bossHandler.getSpawnAreas().get(i);
					Location areaLoc1 = area.getLocationX();
					Location areaLoc2 = area.getLocationY();
					
					if(!l1.equals(areaLoc1) || !l2.equals(areaLoc2))
						area = null;
					else
						break;
				}
				
				if(area != null)
				{
					InventoryHelper.openBossSpawnAreaGUI(area, p);
				}
				else
				{
					System.out.println("err: area == null");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void entityDeath(EntityDeathEvent e)
	{
		LivingEntity ent = e.getEntity(); // More of a DeadEntity now (ba dum tss)
		for(LivingBoss livingBoss : bossHandler.getLivingBosses())
		{
			if(livingBoss.getEntity().equals(ent))
			{
				Entity killer = ent.getKiller();
				Player pKiller = null;
				
				if(killer instanceof Player)
					pKiller = (Player)killer;
				
				BossDeathEvent bossDeathEvent = new BossDeathEvent(livingBoss, pKiller);
				Bukkit.getPluginManager().callEvent(bossDeathEvent);
				
				e.setDroppedExp(livingBoss.getBoss().getXpDropped());
				e.getDrops().clear();
				
				for(ItemStack i : bossDeathEvent.getDrops())
				{
					e.getDrops().add(i);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void entityDamagedByEntity(EntityDamageByEntityEvent e)
	{
		Entity damager = e.getDamager();
		Entity damaged = e.getEntity();
		
		if(damager instanceof Projectile)
		{
			Projectile projectile = (Projectile)damager;
			damager = (Entity)projectile.getShooter();
		}
		
		LivingBoss b = null;
		
		if(damager instanceof Player)
		{
			Player p = (Player)damager;
						
			for(LivingBoss lb : bossHandler.getLivingBosses())
			{
				b = lb;
				if(damaged.equals(b.getEntity()))
				{
					b.setTimeSinceLastHit(0);
					b.getBossBar().addPlayer(p);
				}
				break;
			}
		}
		else
		{			
			for(LivingBoss lb : bossHandler.getLivingBosses())
			{
				b = lb;
				if(b.getEntity().equals(damager))
				{
					if(b.getBoss().getDamagePerHit() >= 0)
					{
						e.setDamage(b.getBoss().getDamagePerHit());
					}
				}
				break;
			}
		}
		
		if(b != null)
		{
			double maxHealth = b.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			double newHealth = b.getEntity().getHealth() - e.getFinalDamage();
			
			b.getBossBar().setProgress(Helper.clamp(newHealth, 0, maxHealth) / maxHealth);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onChunkLoad(ChunkLoadEvent e)
	{
		//System.out.println("Chunk Loaded");
		for(Entity ent : e.getChunk().getEntities())
		{
			if(bossHandler.isUUIDNotLoaded(ent.getUniqueId()))
			{
				debug("onChunkLoad", ent);
				debug("onChunkLoad", "Boss Entity Found!!!");
				bossHandler.createBossFromPreviousEntity(ent);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void bossSpawn(BossSpawnEvent e)
	{
		LivingBoss boss = e.getLivingBoss();
		Bukkit.broadcastMessage(ChatColor.GOLD + "The boss " + boss.getBoss().getDisplayName() + ChatColor.GOLD + " has spawned!");
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void bossDeath(BossDeathEvent e)
	{
		LivingBoss boss = e.getLivingBoss();
		playersListening.remove(boss);
		String deathCmd = boss.getBoss().getDeathCommand();
		String deathMessage = boss.getBoss().getDeathMessage();
		
		if(e.getKiller() != null)
		{
			if(!deathCmd.isEmpty())
			{
				deathCmd = deathCmd.replace("%player%", ChatColor.stripColor(e.getKiller().getName()));
				deathCmd = deathCmd.replace("%display%", e.getKiller().getDisplayName());
				deathCmd = deathCmd.replace("%boss%", boss.getBoss().getDisplayName());
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), deathCmd);
			}
			
			if(!deathMessage.isEmpty())
			{
				deathMessage = deathMessage.replace("%player%", ChatColor.stripColor(e.getKiller().getName()));
				deathMessage = deathMessage.replace("%display%", e.getKiller().getDisplayName());
				deathMessage = deathMessage.replace("%boss%", boss.getBoss().getDisplayName());
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', deathMessage));
			}
		}
	
		boss.getBossBar().setVisible(false);
		for(Player p : boss.getBossBar().getPlayers())
		{
			boss.getBossBar().removePlayer(p);
		}
		bossHandler.removeLivingBoss(boss);
	}
}
