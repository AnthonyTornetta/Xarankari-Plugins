package com.cornchipss.guilds.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class Util 
{
	private static List<Player> adminModes = new ArrayList<>();
	
	public static List<Player> getAdminModes() { return adminModes; }
	public static void addAdminPlayer(Player p) { getAdminModes().add(p); }
	public static void removeAdminPlayer(Player p) { getAdminModes().remove(p); }
	public static boolean isPlayerAdmin(Player p) { return getAdminModes().contains(p); }
	public static void setAdminModes(List<Player> adminModes) { Util.adminModes = adminModes; }
	public static boolean invertAdmin(Player p)
	{
		if(isPlayerAdmin(p))
		{
			removeAdminPlayer(p);
			return false;
		}
		else
		{
			addAdminPlayer(p);
			return true;
		}
	}
}
