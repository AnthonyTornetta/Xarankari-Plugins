package com.cornchipss.buildbattlesmini.arenas;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class ArenaCleaner implements Runnable
{
	ArrayList<Block> blocksToClear;
	Thread thread = new Thread(this);
	
	public ArenaCleaner(ArrayList<Block> blocksToClear)
	{
		this.blocksToClear = blocksToClear;
	}
	
	public void start()
	{
		thread.start();
	}

	@Override
	public void run() 
	{
		if(blocksToClear.size() <= 1000)
		{
			clear();
		}
		else
		{
			ArrayList<Block> passOn = new ArrayList<>();
			while(blocksToClear.size() > 1000)
			{
				passOn.add(blocksToClear.remove(1001));
			}
			ArenaCleaner ac = new ArenaCleaner(passOn);
			ac.start();
			clear();
		}
	}
	
	private void clear()
	{
		for(Block b: blocksToClear)
		{
			b.setType(Material.AIR);
		}
	}
}
