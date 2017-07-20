package com.cornchipss.rpg;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.citizensnpcs.api.CitizensAPI;

public class RPG extends JavaPlugin
{
	CornyListener cl;
	@Override
	public void onEnable()
	{
		cl = new CornyListener(this);
		loadConfig();
		
		CitizensAPI.registerEvents(cl);
	}
	
	@Override
	public void onDisable()
	{
		// Make sure no exploded blocks are left exploded before turning off (Not working atm so I just cancel the explode event)
		cl.regenAllBlocks();
	}
	
	private void loadConfig()
	{
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return CommandRegistry.check(sender, command, label, args, this);
	}
}
