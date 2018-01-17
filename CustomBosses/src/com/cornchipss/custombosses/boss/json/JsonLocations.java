package com.cornchipss.custombosses.boss.json;

import java.util.List;
import java.util.Map;

public class JsonLocations 
{
	private Map<String, List<Integer>> locations;
	
	public Map<String, List<Integer>> getSerializedLocations() { return this.locations; }
	public void setSerializedLocations(Map<String, List<Integer>> locs) { this.locations = locs; }
}
