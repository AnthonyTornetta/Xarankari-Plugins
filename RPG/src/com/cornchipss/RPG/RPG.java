package com.cornchipss.rpg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class RPG extends JavaPlugin
{
	CornyListener cl;
	@Override
	public void onEnable()
	{
		cl = new CornyListener(this);
		loadConfig();
		getServer().getPluginManager().registerEvents(new CornyListener(this), this);
	}
	
	@Override
	public void onDisable()
	{
		// TODO: Make sure no exploded blocks are left exploded before turning off (Not working atm so I just cancel the explode event)
		
	}
	
	private void loadConfig()
	{
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return CommandRegistry.check(sender, command, label, args, this);
	}
	
	public static ArrayList<Block> getSurrounding(Block b)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		BlockFace[] faces =
		{
			BlockFace.UP,
			BlockFace.DOWN,
			BlockFace.NORTH,
			BlockFace.EAST,
			BlockFace.SOUTH,
			BlockFace.WEST
		};
		
		Material[] mats = 
		{
			Material.OBSIDIAN,
			Material.IRON_BLOCK,
			Material.GOLD_BLOCK,
			Material.WOOD,
			Material.REDSTONE_BLOCK,
			Material.LAPIS_BLOCK,
			Material.EMERALD_BLOCK,
			Material.DIAMOND_BLOCK,
			Material.BRICK,
			Material.NETHER_BRICK,
			Material.NETHER_WART_BLOCK,
			Material.SMOOTH_BRICK
		};
		
		List<Material> whitelistedMats = Arrays.asList(mats);
		
		for(BlockFace bf : faces)
		{
			Block bRel = b.getRelative(bf);
			if(whitelistedMats.contains(bRel.getType()))
				blocks.add(bRel);
		}
		return blocks;
	}
}
