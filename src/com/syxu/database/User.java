package com.syxu.database;



public class User {
	public final static String COACH="coach";
	public final static String STUDENT="student";
	
	private String id;
	private String name;
	private String password;
	private String  role; 
	
	public User(){};
	public User(String name, String password){
		this.name = name;
		this.password = password;
		this.id = genId();
	}
	public User(String name, String password, String role){
		this.name = name;
		this.password = password;
		this.role = role;
		this.id = genId();
	}
	

	//getters
	public String getId(){
		return id;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public String getRole() {
		return role;
	}
	//setters
	public void setName(String name){
		this.name = name;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public void setRole(String role){
		this.role = role;
	}
	
	
	private String genId(){
		return (name+password).hashCode() + "";
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password
				+ ", role=" + role + "]";
	}
	
	
	
}
