package com.cornchipss.guilds.guilds;

public enum GuildRank 
{
	PEON(0),
	KNIGHT(1),
	COMMANDER(2),
	KING(3);
	
	private int rankNumber;
	
	GuildRank(int rankNum)
	{
		this.rankNumber = rankNum;
	}
	
	public boolean lessThan(GuildRank other)
	{
		return this.getValue() < other.getValue();
	}
	
	public boolean greaterThan(GuildRank other)
	{
		return this.getValue() > other.getValue();
	}
	
	public int getValue()
	{
		return rankNumber;
	}
}
