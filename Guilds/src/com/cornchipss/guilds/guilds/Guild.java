package com.cornchipss.guilds.guilds;

import java.util.List;
import java.util.UUID;

public class Guild 
{
	private String name;
	private List<UUID> members;
	
	public Guild(String name, List<UUID> members)
	{
		this.name = name;
		this.members = members;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public List<UUID> getMembers() { return members; }
	public void setMembers(List<UUID> members) { this.members = members; }
}
