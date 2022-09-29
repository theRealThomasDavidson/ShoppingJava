package com.shopping.model;

public class Line{
    public int qty; 
    public com.shopping.model.Item type; 
    
    public Line(int q, Item t) {
    	this.qty = q;
    	this.type = t;
    }
    public double price() {
    	return this.type.getPrice() * this.qty; 
    }
    public boolean equals(Line other) {
    	return this.type.getName() == other.type.getName();
    }
	@Override
	public String toString() {
		return String.format("Item: " + type.getName() + "\t count: " + qty + "\t cost: %.02f" , price());
	};
};