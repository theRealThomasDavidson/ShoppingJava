package com.shopping.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.shopping.model.Customer;
import com.shopping.model.Item;
import com.shopping.model.Line;
import com.shopping.model.Order;
import com.shopping.utility.ConnectionManager;



public class ShoppingController {
	private static Connection conn = ConnectionManager.getConnection();
	private static Customer loggedIn = null;
	static {
		Customer.setCounter(getCustIdRange()+1);
		Item.setCounter(getItemIdRange()+1);
		Order.setCounter(getOrderIdRange()+1); 
	}
	public static Customer getLoggedIn(){
		return loggedIn;
	}
	public static boolean hasCustomer(){		
		return !Objects.isNull(ShoppingController.loggedIn);
	}
	public static int getCustIdRange() {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select max(id) as max from customer");
			int max = 0;
			if(rs.next()) {
				max = rs.getInt("max");
			}
			return max;
		} catch(SQLException e) {
			System.out.println("Could NOT set customer ids in D/B :(");
		}
		
		return 0;
	}
	public static int getItemIdRange() {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select max(id) as max from `item`");
			int max = 0;
			if(rs.next()) {
				max = rs.getInt("max");
			}
			return max;
		} catch(SQLException e) {
			System.out.println("Could NOT set customer ids in D/B :(");
		}
		
		return 0;
	}
	public static int getOrderIdRange() {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select max(id) as max from `order`");
			int max = 0;
			if(rs.next()) {
				max = rs.getInt("max");
			}
			return max;
		} catch(SQLException e) {
			System.out.println("Could NOT set customer ids in D/B :(");
		}
		
		return 0;
	}
	
	public static Customer newCustomer(Customer customer) {
		try {
			PreparedStatement pstmt = conn.prepareStatement("insert into customer"
					+ "(id, `email`, `password`)"
					+ "VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE password = ?");

			pstmt.setInt(1, customer.getId());
			pstmt.setString(2, customer.getEmail());
			pstmt.setString(3, customer.getPassword());
			pstmt.setString(4, customer.getPassword());
			int count = pstmt.executeUpdate();
			if(count > 0) {
				return customer;
			}
			return null;
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Could NOT create new customer in D/B :(");
		}
		return null;
	}
	public static Order newOrder(Order order) {
		try {
			PreparedStatement pstmt = conn.prepareStatement("insert into `order`"
					+ "(id, `cust_id`,  `date`)"
					+ "VALUES(?, ?, now()) ON DUPLICATE KEY UPDATE cust_id = ?;");

			pstmt.setInt(1, order.getId());
			pstmt.setInt(2, order.getCustomer().getId());
			pstmt.setInt(3, order.getCustomer().getId());
			int count = pstmt.executeUpdate();
			if(count == 0) {
				return null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Could NOT create new order in D/B :(");
			return null;
		}
		for (Line line : order.getLines()) {
		try {
			PreparedStatement pstmt = conn.prepareStatement("insert into `order_item` "
					+ "(oid, iid, qty) values "
					+ "(?, ?, ?);");

			pstmt.setInt(1, order.getId());
			pstmt.setInt(2, line.type.getId());
			pstmt.setInt(2, line.qty);
			int count = pstmt.executeUpdate();
			if(count == 0) {
				return null;
			}
		} catch(SQLException e) {
			//System.out.println("Could NOT add item to order in D/B :(");
		}
		}
		return order;
	}
	public static Item newItem(Item item) {
		try {
			PreparedStatement pstmt = conn.prepareStatement("insert into `item`"
					+ "(id, `name`, `price`)"
					+ "VALUES(?, ?, ?)"
					+ "ON DUPLICATE KEY UPDATE price = ?;");

			pstmt.setInt(1, item.getId());
			pstmt.setString(2, item.getName());
			pstmt.setDouble(3, item.getPrice());
			pstmt.setDouble(4, item.getPrice());
			int count = pstmt.executeUpdate();
			if(count > 0) {
				return item;
			}
			return null;
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Could NOT create new item in D/B :(");
		}
		return null;
	}
	public static Line newLine(Line l, Order o) {
		try {
			PreparedStatement pstmt = conn.prepareStatement("insert into `order_item` "
					+ "(iid, oid, qty)"
					+ "VALUES(?, ?, ?) "
					+ "ON DUPLICATE KEY UPDATE qty = ?;");

			pstmt.setInt(1, l.type.getId());
			pstmt.setInt(2, o.getId());
			pstmt.setInt(3, l.qty);
			pstmt.setInt(4, l.qty);
			int count = pstmt.executeUpdate();
			if(count > 0) {
				return l;
			}
			return null;
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Could NOT create new order item in D/B :(");
		}
		return null;
	}
	public static Customer getCustomerByEmail(String email){
		try {
			PreparedStatement pstmt = conn.prepareStatement("select * from customer c where c.email = ?");
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			Customer cust;
			if(rs.next()) {
				cust = new Customer(
						rs.getInt("id"), 
						rs.getString("email"), 
						rs.getString("password"), 
						new ArrayList<Order>());
			}
			else {return null;}
			List<Order> o = findOrdersByCustomer(cust);
			cust.setOrders(o);
			return cust;
			
		} catch(SQLException e) {
			System.out.println("Could NOT find customer in D/B :(");
		}
		return null;
	}
		public static Customer getCustomerById(int id){
		try {
			PreparedStatement pstmt = conn.prepareStatement("select * from customer c where c.id = ?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			Customer cust;
			if(rs.next()) {
				cust = new Customer(
						rs.getInt("id"), 
						rs.getString("email"), 
						rs.getString("password"), 
						new ArrayList<Order>());
			}
			else {return null;}
			List<Order> o = findOrdersByCustomer(cust);
			cust.setOrders(o);
			return cust;
			
		} catch(SQLException e) {
			System.out.println("Could NOT find customer in D/B :(");
		}
		return null;
	}
	public static List<Order> findOrdersByCustomer(Customer cust) {
		try {
			PreparedStatement pstmt = conn.prepareStatement("select * from `order` o where o.cust_id = ?");
			pstmt.setInt(1, cust.getId());
			ResultSet rs = pstmt.executeQuery();
			List<Order> ret = new ArrayList<Order>();
			while(rs.next()) {
				ret.add(new Order(rs.getInt("id"), cust, rs.getString("date")));
			}
			for (Order o : ret){
				o.setLines(findLinesByOrder(o));
			}
			return ret;
		}
		catch(SQLException e) {
			System.out.println("Could NOT find accounts in D/B :(");
		}
		return null;
		
	}
	public static List<Item> findItems() {
		try {
			PreparedStatement pstmt = conn.prepareStatement("select * from `item`");
			ResultSet rs = pstmt.executeQuery();
			List<Item> ret = new ArrayList<Item>();
			while(rs.next()) {
				ret.add(new Item(rs.getInt("id"),
									rs.getString("name"),
									rs.getFloat("price")));
			}
			return ret;
		}
		catch(SQLException e) {
			System.out.println("Could NOT find accounts in D/B :(");
		}
		return null;
		
	}
	public static List<Line> findLinesByOrder(Order order) {
		try {
			PreparedStatement pstmt = conn.prepareStatement("select * from `order_item` o join "
			+ "`item` i on o.iid = i.id where o.oid = ?");
			pstmt.setInt(1, order.getId());
			ResultSet rs = pstmt.executeQuery();
			List<Line> ret = new ArrayList<Line>();
			while(rs.next()) {
				Item i = new Item(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"));
				Line l = new Line(rs.getInt("qty"),  i);
				ret.add(l);
			}
			return ret;
		}
		catch(SQLException e) {
			System.out.println("Could NOT find accounts in D/B :(");
		}
		return null;
		
	}
	public static Item getItemByName(String name){
		try {
			PreparedStatement pstmt = conn.prepareStatement("select * from item where lower(`name`) like lower(?)");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			Item i;
			if(rs.next()) {
				i = new Item(
						rs.getInt("id"), 
						rs.getString("name"), 
						rs.getFloat("price"));
			}
			else {return null;}
			return i;
			
		} catch(SQLException e) {
			System.out.println("Could NOT find customer in D/B :(");
		}
		return null;
	}
	
	public static Order getOrderById(int id){
		try {
			PreparedStatement pstmt = conn.prepareStatement("select * from `order` where id = ?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			Order o;
			if(rs.next()) {
				o = new Order(
						rs.getInt("id"), 
						ShoppingController.getCustomerById(rs.getInt("cust_id")),
						rs.getString("date"));
				o.setLines(ShoppingController.findLinesByOrder(o));		
			}
			else {return null;}
			return o;
			
		} catch(SQLException e) {
			System.out.println("Could NOT find order in D/B :(");
		}
		return null;
	}
		public static void deleteOrderById(int id){
		try {
			PreparedStatement pstmt = conn.prepareStatement("delete from `order` where id = ?");
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			
		} catch(SQLException e) {
			System.out.println("Could NOT find order in D/B :(");
		}
	}
	public static boolean login(Customer c){
		Customer found = getCustomerByEmail(c.getEmail());
		if (Objects.isNull(found)){
			return false;
		}
		if (c.getPassword().equals(found.getPassword())){
			ShoppingController.loggedIn = found;
			return true;
		}
		return false;
	}
	public static void logout(){
		ShoppingController.loggedIn = null;
	}
}
