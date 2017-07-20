package com.cornchipss.maprenderer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CornyListener implements Listener 
{
	
	
	
	@EventHandler
	public void onMapInitialize(MapInitializeEvent e)
	{
		MapView map = e.getMap();
		
		for(MapRenderer r : map.getRenderers())
		{
			map.removeRenderer(r);
		}
		
		map.setScale(MapView.Scale.FARTHEST);
		
		map.addRenderer(new CustomMapRenderer());
		
		
		
	}
}
