package com.cornchipss.rpg.helper;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Helper 
{
	public static final int MAX_BLOCKS = 1000;
	
	
	public static final Material[] mats = 
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
	
	public static final List<Material> bridgeMats = Arrays.asList(mats);
	
	public static Vector3 getBlockFaceDirection(BlockFace bf)
	{
		if(bf.equals(BlockFace.DOWN))
			return new Vector3(0, -1, 0);
		if(bf.equals(BlockFace.UP))
			return new Vector3(0, 1, 0);
		
		if(bf.equals(BlockFace.NORTH))
			return new Vector3(0, 0, -1);
		if(bf.equals(BlockFace.SOUTH))
			return new Vector3(0, 0, 1);
		
		if(bf.equals(BlockFace.WEST))
			return new Vector3(-1, 0, 0);
		if(bf.equals(BlockFace.EAST))
			return new Vector3(1, 0, 0);
		
		return new Vector3(0, 0, 0);
	}
	
	public static int sign(int number)
	{
		if(number < 0)
			return -1;
		else
			return 1;
	}
	
	/**
	 * Checks if a string is an integer
	 * @param s The string to check
	 * @return True if it is an integer
	 */
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
	
}
