package com.shopping.model;

import java.util.ArrayList;
import java.util.List;


public class Customer {
	private int id;
	private String email;
	private String password;
	private List<Order> orders;

	private static int idCounter = 0;

	public static void setCounter(int id) {
		Customer.idCounter = id;
	}
	public static synchronized int createID(){
		idCounter += 1;
	    return idCounter;
	}
	
	public Customer(String email, String password) {
		super();
		this.id = Customer.createID();
		this.email = email;
		this.password = password;
		this.orders =  new ArrayList<Order>();
	}


	public Customer(int id, String email, String password, List<Order> orders) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.orders = orders;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Order> getOrders() {
		return this.orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	public List<Order> newOrder(Order o) {
		this.orders.add(o);
		return this.orders;
	}
	@Override
	public String toString() {
		String ret =  "Customer [id=" + id + ", email=" + email + ", password=" + password + "]\n";
		for (Order o : this.orders) {
			ret += o.toString() + "\n";
		}
		return ret ;
	}	
}
