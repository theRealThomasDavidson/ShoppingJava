package com.shopping.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.shopping.model.Line;


 
public class Order {
	private int id;
	private List<Line> lines;
	private Customer customer;
	private String date;

	static int idCounter = 0;

	public static void setCounter(int id) {
		Order.idCounter = id;
	}
	public static synchronized int createID(){
		idCounter += 1;
	    return idCounter;
	}
	
	public Order(List<Line> lines, Customer customer) {
		super();
		this.id = Order.createID();
		this.lines = lines;
		this.customer = customer;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.date = dtf.format(LocalDateTime.now());
	}
	
	public Order(int id, List<Line> lines, Customer customer) {
		super();
		this.id = id;
		this.lines = lines;
		this.customer = customer;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.date = dtf.format(LocalDateTime.now());
	}
	public Order(int id, Customer customer) {
		super();
		this.id = id;
		this.lines = new ArrayList<Line>();
		this.customer = customer;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.date = dtf.format(LocalDateTime.now());
	}
	public Order(int id, Customer customer, String date) {
		super();
		this.id = id;
		this.lines = new ArrayList<Line>();
		this.customer = customer;
		this.date = date;
	}
	public Order(Customer customer) {
		//super();
		this.id = Order.createID();
		this.lines = new ArrayList<Line>();
		this.customer = customer;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.date = dtf.format(LocalDateTime.now());
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<Line> getLines() {
		return this.lines;
	}
	public void setLines(List<Line> list) {
		this.lines = list;
	}
	public Line newLine(Line l) {
		int ndx = 0;
		for(Line l2: this.lines) {
			if (l2.type.getName().equals(l.type.getName())) {
				this.lines.set(ndx, l);
				return l;
			}
			ndx += 1;
		}
		this.lines.add(l);
		return l;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public static int getIdCounter() {
		return idCounter;
	}
	public double getTotal() {
		double t = 0.;
		for (Line l : this.lines) {
			t += l.price();
		}
		return t;
	}
	@Override
	public String toString() {
		String ret = "Order [id=" + id + " Customer= " + this.customer.getEmail()+ "]\n";
		ret += "[date= "+date+"]\n";
		for (Line l : this.lines) {
			ret += l.toString() + "\n";
		}
		ret += String.format("Total: %.02f", this.getTotal());
		return ret ;
	}
	
}
