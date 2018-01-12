package com.cornchipss.custombosses;

public class UserNested 
{
	private String name, email;
	private int age;
	private boolean isDev;
	private UserAddress address;
	
	public UserNested(String name, String email, int age, boolean isDev, UserAddress address) 
	{
		this.name = name;
		this.email = email;
		this.age = age;
		this.isDev = isDev;
		this.address = address;
	}

	@Override
	public String toString() {
		return "UserNested [name=" + name + ", email=" + email + ", age=" + age + ", isDev=" + isDev + ", address="
				+ address + "]";
	}
	
	
}
