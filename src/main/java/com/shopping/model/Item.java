package com.shopping.model;


public class Item {

	private int id;
	private String name;
	private double price;

	static int idCounter = 0;
	
	public static void setCounter(int id) {
		Item.idCounter = id;
	}
	public static synchronized int createID(){
		idCounter += 1;
	    return idCounter;
	}
	public Item(String name, double price) {
		super();
		this.id = Item.createID();
		this.name = name;
		this.price = price;
	}
	public Item(int id, String name, double price) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String toString() {
		return String.format("Item: " + this.getName() + "\t price: %.02f" , this.price);
	};
	
}
