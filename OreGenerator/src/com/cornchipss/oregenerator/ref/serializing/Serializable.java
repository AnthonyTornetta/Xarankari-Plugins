package com.cornchipss.oregenerator.ref.serializing;

import java.util.Map;

public interface Serializable 
{
	Map<String, String> serialze();
	void deserialize(Map<String, String> serializedData);
}
