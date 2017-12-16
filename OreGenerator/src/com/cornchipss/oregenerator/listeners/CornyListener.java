package com.cornchipss.oregenerator.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.oregenerator.OreGeneratorPlugin;
import com.cornchipss.oregenerator.generators.Generator;
import com.cornchipss.oregenerator.generators.GeneratorUtils;
import com.cornchipss.oregenerator.generators.types.CoalGenerator;
import com.cornchipss.oregenerator.generators.types.DiamondGenerator;
import com.cornchipss.oregenerator.generators.types.EmeraldGenerator;
import com.cornchipss.oregenerator.generators.types.GoldGenerator;
import com.cornchipss.oregenerator.generators.types.IronGenerator;
import com.cornchipss.oregenerator.generators.types.LapisGenerator;
import com.cornchipss.oregenerator.generators.types.RedstoneGenerator;
import com.cornchipss.oregenerator.ref.Reference;
import com.cornchipss.oregenerator.upgrades.types.SpeedUpgrade;

public class CornyListener implements Listener
{	
	private OreGeneratorPlugin plugin;
	
	public CornyListener(OreGeneratorPlugin plugin) 
	{
		this.plugin = plugin;
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new  Runnable()
		{
			public void run()
			{
			    plugin.getGeneratorHandler().tickGenerators();
			}
		}, 20l, 20l);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPlaceBlock(BlockPlaceEvent e)
	{
		Player p = e.getPlayer();
		ItemStack itemPlaced = p.getItemInHand(); // Because it technically hasn't been placed yet
		Block b = e.getBlock();
		
		if(!plugin.getGeneratorMaterials().contains(b.getType()))
			return;
		
		if(e.isCancelled())
			return;
		
		int genId = GeneratorUtils.getGeneratorType(itemPlaced.getItemMeta());
		
		Generator gen = null;
		
		switch(genId)
		{
		case GeneratorUtils.GENERATOR_COAL_ID:
			gen = new CoalGenerator(b, plugin);
			break;
		case GeneratorUtils.GENERATOR_IRON_ID:
			gen = new IronGenerator(b, plugin);
			break;
		case GeneratorUtils.GENERATOR_REDSTONE_ID:
			gen = new RedstoneGenerator(b, plugin);
			break;
		case GeneratorUtils.GENERATOR_LAPIS_ID:
			gen = new LapisGenerator(b, plugin);
			break;
		case GeneratorUtils.GENERATOR_GOLD_ID:
			gen = new GoldGenerator(b, plugin);
			break;
		case GeneratorUtils.GENERATOR_DIAMOND_ID:
			gen = new DiamondGenerator(b, plugin);
			break;
		case GeneratorUtils.GENERATOR_EMERALD_ID:
			gen = new EmeraldGenerator(b, plugin);
			break;	
		default:
			// not a valid generator
			break;
		}
		
		if(gen != null)
		{
			plugin.getGeneratorHandler().addGenerator(gen);
			gen.addUpgrade(new SpeedUpgrade());
			gen.addUpgrade(new SpeedUpgrade());
			GeneratorUtils.serialize(gen);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerBreakBlock(BlockBreakEvent e)
	{
		Block b = e.getBlock();
		
		if(!plugin.getGeneratorMaterials().contains(b.getType()))
			return;
		
		if(e.isCancelled())
			return;
		
		//Generator gen = plugin.getGeneratorHandler().getGenerator(i);

		for(int i = 0; i < plugin.getGeneratorHandler().generatorAmount(); i++)
		{
			if(plugin.getGeneratorHandler().getGenerator(i).getGeneratorBlock().equals(b))
			{
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockExplode(BlockExplodeEvent e)
	{
		Block b = e.getBlock();
		
		if(!plugin.getGeneratorMaterials().contains(b.getType()))
			return;
		
		if(e.isCancelled())
			return;
		
		//Generator gen = plugin.getGeneratorHandler().getGenerator(i);

		for(int i = 0; i < plugin.getGeneratorHandler().generatorAmount(); i++)
		{
			if(plugin.getGeneratorHandler().getGenerator(i).getGeneratorBlock().equals(b))
			{
				e.setCancelled(true);
			}
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
				giveGenerator(p, GeneratorUtils.GENERATOR_COAL_ID);
				break;
			case IRON_ORE:
				giveGenerator(p, GeneratorUtils.GENERATOR_IRON_ID);
				break;
			case REDSTONE_ORE:
				giveGenerator(p, GeneratorUtils.GENERATOR_REDSTONE_ID);
				break;
			case LAPIS_ORE:
				giveGenerator(p, GeneratorUtils.GENERATOR_LAPIS_ID);
				break;
			case GOLD_ORE:
				giveGenerator(p, GeneratorUtils.GENERATOR_GOLD_ID);
				break;
			case DIAMOND_ORE:
				giveGenerator(p, GeneratorUtils.GENERATOR_DIAMOND_ID);
				break;
			case EMERALD_ORE:
				giveGenerator(p, GeneratorUtils.GENERATOR_EMERALD_ID);
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
		ItemStack is = GeneratorUtils.createGeneratorItemStack(type, plugin.getGeneratorMaterial(type));
		p.getInventory().addItem(is);
	}
}
