package com.cornchipss.oregenerator.listeners;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.oregenerator.OreGeneratorPlugin;
import com.cornchipss.oregenerator.generators.Generator;
import com.cornchipss.oregenerator.ref.Reference;

import net.md_5.bungee.api.ChatColor;

public class CornyListener implements Listener
{
	private OreGeneratorPlugin plugin;
	private Random rdm = new Random();
	
	public CornyListener(OreGeneratorPlugin plugin) 
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerPlaceBlock(BlockPlaceEvent e)
	{
		Player p = e.getPlayer();
		ItemStack itemPlaced = p.getItemInHand(); // Because it technically hasn't been placed yet
		Block b = e.getBlock();
		Block against = e.getBlockAgainst();
		
		if(b.getType() == plugin.getGeneratorMaterial())
		{
			int genType = Generator.getGeneratorType(itemPlaced.getItemMeta());
			
			if(genType == -1 && genType <= Generator.GENERATOR_DIAMOND_ID)
				return; // No something we care about, let the other plugins deal with it
			
			// Make sure it is a 3x3 of stone to make sure they are not wasting it :)
			for(int x = -1; x <= 1; x++)
			{
				for(int z = -1; z <= 1; z++)
				{
					if(against.getRelative(x, 0, z).getType() != Material.STONE)
					{
						p.sendMessage(ChatColor.RED + "You want to place this block in a 3x3 area filled with stone to turn that stone into the ore you want");
						e.setCancelled(true);
						return;
					}
				}
			}
			
			
			int blocksTransmuted = 0;
			
			for(int x = -1; x <= 1; x++)
			{
				for(int z = -1; z <= 1; z++)
				{					
					Block transmutableBlock = against.getRelative(x, 0, z);
					if(rdm.nextInt(genType + 1) == 0)
					{
						switch(genType)
						{
							case Generator.GENERATOR_COAL_ID:
								transmutableBlock.setType(Material.COAL_ORE);
								break;
							case Generator.GENERATOR_IRON_ID:
								transmutableBlock.setType(Material.IRON_ORE);
								break;
							case Generator.GENERATOR_GOLD_ID:
								transmutableBlock.setType(Material.GOLD_ORE);
								break;
							case Generator.GENERATOR_REDSTONE_ID:
								transmutableBlock.setType(Material.REDSTONE_ORE);
								break;
							case Generator.GENERATOR_LAPIS_ID:
								transmutableBlock.setType(Material.LAPIS_ORE);
								break;
							case Generator.GENERATOR_DIAMOND_ID:
								transmutableBlock.setType(Material.DIAMOND_ORE);
								break;
							case Generator.GENERATOR_EMERALD_ID:
								transmutableBlock.setType(Material.EMERALD_ORE);
								break;
						}
						blocksTransmuted++;
					}
				}
			}
			
			// Such a nice guy
			if(blocksTransmuted == 0)
			{
				p.sendMessage(ChatColor.GREEN + "Wow... 0 blocks transmuted... that's pretty sad. Let's try that again :D");
				e.setCancelled(true);
			}
			b.setType(Material.AIR); // Remove it because it has now been used
		}
	}
	
	// For use with the command invenotries
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerClickInventory(InventoryClickEvent e)
	{
		Inventory i = e.getInventory();
		Player p = (Player)e.getWhoClicked();

		if((i.getName().equals(Reference.CMD_WINDOW_GET_GENERATOR_TITLE)))
		{
			e.setCancelled(true); // Don't want them taking my blocks
			if(e.getCurrentItem() == null)
				return;
			
			switch(e.getCurrentItem().getType())
			{
			case COAL_ORE:
				giveGenerator(p, Generator.GENERATOR_COAL_ID);
				break;
			case IRON_ORE:
				giveGenerator(p, Generator.GENERATOR_IRON_ID);
				break;
			case REDSTONE_ORE:
				giveGenerator(p, Generator.GENERATOR_REDSTONE_ID);
				break;
			case LAPIS_ORE:
				giveGenerator(p, Generator.GENERATOR_LAPIS_ID);
				break;
			case GOLD_ORE:
				giveGenerator(p, Generator.GENERATOR_GOLD_ID);
				break;
			case DIAMOND_ORE:
				giveGenerator(p, Generator.GENERATOR_DIAMOND_ID);
				break;
			case EMERALD_ORE:
				giveGenerator(p, Generator.GENERATOR_EMERALD_ID);
				break;
			case BARRIER:
				p.closeInventory();
				break;
			default:
				break;
			}
		}
	}
	
	private void giveGenerator(Player p, int type)
	{
		System.out.println("RAN");
		ItemStack is = Generator.createGenerator(type, plugin.getGeneratorMaterial());
		p.getInventory().addItem(is);
	}
}
