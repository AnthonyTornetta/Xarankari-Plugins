package com.cornchipss.oregenerator.ref;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class Reference 
{
	public static final Color[] COLORS = 
		{
			Color.AQUA,
			Color.BLACK,
			Color.BLUE,
			Color.FUCHSIA,
			Color.GRAY,
			Color.GREEN,
			Color.LIME,
			Color.MAROON,
			Color.NAVY,
			Color.OLIVE,
			Color.ORANGE,
			Color.PURPLE,
			Color.RED,
			Color.SILVER,
			Color.TEAL,
			Color.WHITE,
			Color.YELLOW
		};
	
	public static final String PLUGIN_NAME = "Ore Generator";
	public static final String PLUGIN_VERSION = "1.0";
	public static final String PLUGIN_AUTHOR = "Cornchip";
	
	public static final String CFG_TRANSMUTABLE_BLOCK_KEY = "transmutable-block";
	public static final String CFG_GENERATOR_MATERIALS_KEY = "generator-blocks";
	public static final String CFG_DEFAULT_TIME_BETWEEN_TRANSMUTES = "default-time-between-transmutes";
	
	public static final String CFG_UPGRADE_MATERIALS = "upgrade-materials";

	public static final String CFG_VERSION_KEY = "version";
	
	public static final String CMD_WINDOW_GET_GENERATOR_TITLE = ChatColor.DARK_GREEN + "Give Generator";	
	public static final String GENERATOR_INVENTORY_NAME = ChatColor.DARK_GREEN + "Generator";
	public static final String CFG_GENERATOR_PRICES_KEY = "generator-prices";
	public static final String CFG_MAX_UPGRADES_KEY = "max-upgrades-amount";
	
	public static void fanfare(Player p)
	{		
		for(int i = 0; i < 10; i++)
		{
			//Spawn the Firework, get the FireworkMeta.
            Firework fw = (Firework)p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
           
            //Our random generator
            Random r = new Random();
            
            //Get the type
            int rt = r.nextInt(4) + 1;
            Type type = Type.BALL;       
            if (rt == 1) type = Type.BALL;
            if (rt == 2) type = Type.BALL_LARGE;
            if (rt == 3) type = Type.BURST;
            if (rt == 4) type = Type.CREEPER;
            if (rt == 5) type = Type.STAR;
           
            //Get our random colours   
            int r1i = r.nextInt(17);
            int r2i = r.nextInt(17);
            Color c1 = COLORS[r1i];
            Color c2 = COLORS[r2i];
           
            //Create our effect with this
            FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
           
            //Then apply the effect to the meta
            fwm.addEffect(effect);
           
            //Generate some random power and set it
            int rp = r.nextInt(2) + 1;
            fwm.setPower(rp);
           
            //Then apply this to our rocket
            fw.setFireworkMeta(fwm);
		}
	}
}
