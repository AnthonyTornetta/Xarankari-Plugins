package com.cornchipss.maprenderer;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CustomMapRenderer extends MapRenderer
{
	final int MAP_X_DISTANCE = 2048;
	final int MAP_Z_DISTANCE = 2048;
	
	@Override
	public void render(MapView map, MapCanvas canvas, Player p) 
	{
		int centerX = map.getCenterX();
		int centerZ = map.getCenterZ();
		
		
	}
}