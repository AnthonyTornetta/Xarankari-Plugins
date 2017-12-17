package com.cornchipss.oregenerator.ref;

import java.util.ArrayList;
import java.util.List;

/**
 * Similar to a HashMap - Stores a list of keys that corruspond to another object in a list of objects
 * <br>
 * However, both keys and objects can be accessed by each other, instead of objects not having access to keys like in a HashMap
 * <br>
 * Works similarly to a HashMap in that it uses keys to access objects, but indexes can also be used and keys are gettable through objects and visa versa.
 * @param <K> The datatype of the keys to be used to corruspond with the objects
 * @param <O> The datatype of the objects to be stored
 */
public class DoubleList<K, O>
{
	List<K> keys;
	List<O> objs;
	
	/**
	 * Similar to a HashMap - Stores a list of keys that corruspond to another object in a list of objects
	 * <br>
	 * However, both keys and objects can be accessed by each other, instead of objects not having access to keys like in a HashMap
	 * <br>
	 * Works similarly to a HashMap in that it uses keys to access objects, but indexes can also be used and keys are gettable through objects and visa versa.
	 */
	public DoubleList()
	{
		keys = new ArrayList<>();
		objs = new ArrayList<>();
	}
	
	/**
	 * Gets an object at a given key
	 * @param key The key that corresponds to the object
	 * @return The object if the key exists, null if the key isn't present
	 */
	public O get(K key)
	{
		if(!containsKey(key))
			return null;
		int index = indexOfKey(key);
		return objs.get(index);
	}
	
	/**
	 * Gets an object based off of its index
	 * @param index The index the object is at
	 * @return the object at the index
	 * @throws IndexOutOfBoundsException if the index is out of the bounds of the DoubleList's size
	 */
	public O getObjectAtIndex(int index)
	{
		if(index < size() && index >= 0)
		{
			return objs.get(index);
		}
		throw new IndexOutOfBoundsException("Index of " + index + " >= " + size() + " (the size of the DoubleList)");
	}
	
	/**
	 * Gets the index of a given key
	 * @param key The key to find the index of
	 * @return The index of said key
	 */
	public int indexOfKey(K key)
	{
		return keys.indexOf(key);
	}
	
	/**
	 * Gets the index of a given object
	 * @param object The object to find the index of
	 * @return The index of said object
	 */
	public int indexOfObject(O object)
	{
		return objs.indexOf(object);
	}
	
	/**
	 * Gets a key at a given index
	 * @param index The index the key is located
	 * @return The key found at the index
	 * @throws IndexOutOfBoundsException if the index is out of the bounds of the DoubleList's size
	 */
	public K getKeyAtIndex(int index)
	{
		if(index < size() && index >= 0)
		{
			return keys.get(index);
		}
		throw new IndexOutOfBoundsException("Index of " + index + " >= " + size() + " (the size of the DoubleList)");
	}
	
	/**
	 * Gets the key the object corresponds to
	 * @param obj The object to find the key of
	 * @return The key it corresponds to
	 */
	public K getKey(O obj)
	{
		if(!containsValue(obj))
			return null;
		int index = indexOfObject(obj);
		return keys.get(index);
	}
	
	/**
	 * Remove a specified key from the list, along with the corresponding object
	 * @param key The key to remove
	 * @return True if the key existed, false if it didn't
	 */
	public boolean removeKey(K key)
	{
		int index = indexOfKey(key);
		boolean exists = keys.remove(key);
		if(exists)
			objs.remove(index);
		return exists;
	}
	
	/**
	 * Remove a specified object from the list, along with the corresponding key
	 * @param val The object to remove
	 * @return True if the object existed, false if it didn't
	 */
	public boolean removeByValue(O val)
	{
		int index = indexOfObject(val);
		boolean exists = objs.remove(val);
		if(exists)
			keys.remove(index);
		return exists;
	}
	
	/**
	 * Puts a given value at a corrusponding key
	 * <br>
	 * If the key already exists, it replaces the object there with the object specified
	 * <br>
	 * If the key does not exist, it adds the key along with the object
	 * @param key The key to place it at
	 * @param obj The object to set the value to
	 */
	public void put(K key, O obj)
	{
		int index = keys.indexOf(key);
		if(index != -1)
		{
			objs.set(index, obj);
		}
		
		keys.add(key);
		objs.add(obj);
	}
	
	/**
	 * Checks if the DoubleList contains the object specified
	 * @param val The object to check for
	 * @return True if it exists, false if it doesn't
	 */
	public boolean containsValue(Object val)
	{
		return objs.contains(val);
	}
	
	/**
	 * Checks if the DoubleList contains the key specified
	 * @param key The key to check for
	 * @return True if it exists, false if it doesn't
	 */
	public boolean containsKey(Object key)
	{
		return keys.contains(key);
	}
	
	/**
	 * Gets the value by the key, and if it doesn't exist returns the specified default value
	 * @param key The key to check for the value
	 * @param defaultTo The default value to return if it doesn't exist
	 * @return The default value if the key doesn't exist, or the object the key corresponds to if the key does exist
	 */
	public O getOrDefaultVal(K key, O defaultTo)
	{
		O obj = get(key);
		if(containsValue(obj))
			return obj;
		else 
			return defaultTo;
	}
	
	/**
	 * @return The size of the DoubleList
	 */
	public int size()
	{
		return keys.size();
	}

	public boolean containsKeyOrValue(Object x) 
	{
		return keys.contains(x) || objs.contains(x);
	}
}
