package com.cornchipss.custombosses.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.boss.handler.BossHandler;
import com.cornchipss.custombosses.listener.events.BossDeathEvent;
import com.cornchipss.custombosses.listener.events.BossSpawnEvent;
import com.cornchipss.custombosses.util.Reference;

public class CornyListener implements Listener
{
	private BossHandler bossHandler;
	
	public CornyListener(BossHandler handler)
	{		
		this.bossHandler = handler;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void playerInteract(PlayerInteractEvent e)
	{
		if(e.isCancelled())
			return;
		
		Player p = e.getPlayer();
		ItemStack itemHeld = p.getItemInHand();
		
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK && itemHeld != null)
			return;
		
		System.out.println("Ayye lmao 1");
		
		for(Boss b : bossHandler.getLoadedBosses())
		{
			ItemStack spawnItem = b.getSpawnItem();		
			
			if(Reference.equiv(itemHeld, spawnItem))
			{
				System.out.println("Ayye lmao 1.5");
				LivingBoss newBoss = b.createLivingBoss();
				BossSpawnEvent bossSpawnEvent = new BossSpawnEvent(newBoss);
				
				System.out.println("Ayye lmao 2");
				
				Bukkit.getPluginManager().callEvent(bossSpawnEvent);
				if(bossSpawnEvent.isCancelled())
					return;
				
				System.out.println("Ayye lmao 3");
				
				bossHandler.addLivingBoss(newBoss);
				newBoss.spawn(e.getClickedBlock().getLocation().add(0.0, 1.0, 0.0));
				
				System.out.println("Ayye lmao 4");
				break;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerJoin(PlayerJoinEvent e)
	{
		e.getPlayer().getInventory().addItem(bossHandler.getLoadedBosses().get(0).getSpawnItem());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void entityDeath(EntityDeathEvent e)
	{
		LivingEntity ent = e.getEntity(); // More of a DeadEntity now (ba dum tss)
		
		for(LivingBoss b : bossHandler.getLivingBosses())
		{
			if(b.getEntity().equals(ent))
			{
				BossDeathEvent bossDeathEvent = new BossDeathEvent(b);
				Bukkit.getPluginManager().callEvent(bossDeathEvent);
				break;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void entityDamagedByEntity(EntityDamageByEntityEvent e)
	{
		Entity damager = e.getDamager();
		Entity thingCausingDamage = damager;
		
		if(damager instanceof Projectile)
		{
			Projectile projectile = (Projectile)damager;
			thingCausingDamage = (Entity)projectile.getShooter();
			System.out.println("DAMAGE TYPE = PROJECTILE");
		}
		
		for(LivingBoss b : bossHandler.getLivingBosses())
		{
			if(b.getEntity().equals(thingCausingDamage))
			{
				System.out.println("RAN DAMAGE CHANGER");
				e.setDamage(b.getBoss().getDamagePerHit());
				System.out.println("Damage: " + e.getDamage());
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
		bossHandler.removeAliveBoss(boss);
	}
}
