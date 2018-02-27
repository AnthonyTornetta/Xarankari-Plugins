package com.cornchipss.guilds.guilds;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuildJson 
{
	private String guildName;
	private List<String> members;
	
	public GuildJson(String name, List<String> members)
	{
		this.guildName = name;
		this.members = members;
	}
	
	public Guild toGuild()
	{
		List<UUID> memberUuids = new ArrayList<>();
		for(String s : members)
		{
			memberUuids.add(UUID.fromString(s));
		}
		
		return new Guild(guildName, memberUuids);
	}
	
	public static GuildJson fromGuild(Guild g)
	{
		List<String> memberUUIDStrings = new ArrayList<>();
		for(UUID id : g.getMembers())
		{
			memberUUIDStrings.add(id.toString());
		}
		
		return new GuildJson(g.getName(), memberUUIDStrings);
	}
}
