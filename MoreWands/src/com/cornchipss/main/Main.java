package com.cornchipss.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.main.helpers.OnTick;
import com.cornchipss.main.items.Items;

public class Main extends JavaPlugin
{
	Effects effects;
	Items items;
	Main main;
	@Override
	public void onEnable()
	{
		log("Hola");
		PluginManager pm = getServer().getPluginManager();
		PluginListener listener = new PluginListener(this);
		pm.registerEvents(listener, this);
		main = this;
		items = new Items();
		
		effects = new Effects(this);
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new OnTick(this), 1l, 1l);
	}
	
	@Override
	public void onDisable()
	{
		log("Goodbye ;(");
	}
	
	public void log(String msg)
	{
		getLogger().info(msg);
	}
	
	public void log(boolean b)
	{		
		if(b)
			getLogger().info("true");
		else
			getLogger().info("false");
	}
	
	public void log(int integer) 
	{
		getLogger().info(integer + "");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return CommandRegistry.check(sender, command, label, args, this);
	}
	
	public Items getItems() { return items; }
	public Effects getEffects() { return effects; }
}