package com.cornchipss.oregenerator.ref.serializing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Serializer 
{
	public String serialize(Serializable serializeableObject)
	{
		Map<String, String> data = serializeableObject.serialze();
		
		List<String> keys = new ArrayList<>();
		
		for(String k : data.keySet())
			keys.add(k);
		
		String serializedData = "";
		
		for(int i = 0; i < data.size(); i++)
		{
			String key = keys.get(i);
			String serializeableData = data.get(key);
			
			key.replaceAll(Pattern.quote("\\"), Matcher.quoteReplacement("\\\\"));
			key.replaceAll(Pattern.quote(";"), Matcher.quoteReplacement("\\;"));
			key.replaceAll(Pattern.quote("="), Matcher.quoteReplacement("\\="));
			
			if(i != 0)
				serializedData += ";";
			
			serializedData += key + "=" + serializeableData;
		}
		
		return serializedData;
	}
	
	public Map<String, String> deserialize(String serializedData)
	{
		Map<String, String> data = new HashMap<>();
		
		List<String> splitValues = new ArrayList<>();
		
		String curSplit = "";
		boolean valLegit = true;
		
		for(int i = 0; i < serializedData.length(); i++)
		{
			char charAt = serializedData.charAt(i);
			if(charAt == '\\')
			{
				if(valLegit)
				{
					valLegit = false;
				}
				else
				{
					valLegit = true;
					curSplit += charAt;
				}
			}
			else if(charAt == ';' && valLegit)
			{
				splitValues.add(curSplit);
				curSplit = "";
			}
			else
			{
				valLegit = true;
				curSplit += charAt;
			}
			
			if(i + 1 == serializedData.length())
			{
				if(!curSplit.isEmpty())
					splitValues.add(curSplit);
			}
		}
		
		for(int i = 0; i < splitValues.size(); i++)
		{
			String val = splitValues.get(i);
			String key = "", value = "";
			for(int j = 0; j < val.length(); j++)
			{
				char charAt = val.charAt(i);
				if(charAt == '\\')
				{
					if(valLegit)
					{
						valLegit = false;
					}
					else
					{
						valLegit = true;
						curSplit += charAt;
					}
				}
				else if(charAt == ';' && valLegit)
				{
					key = curSplit;
					val = val.substring(curSplit.length() + 1, val.length());
					break;
				}
				else
				{
					valLegit = true;
					curSplit += charAt;
				}
			}
			
			data.put(key, value);
			
			System.out.println("Key: " + key + "; Val: " + val);
		}
		
		return data;
	}
}
