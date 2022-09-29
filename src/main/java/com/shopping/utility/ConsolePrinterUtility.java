package com.shopping.utility;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.shopping.controller.ShoppingController;
import com.shopping.model.Customer;
import com.shopping.model.Item;
import com.shopping.model.Line;
import com.shopping.model.Order;
import com.shopping.utility.ProcessingMethod;

interface ProcessingMethod {
	   public void method();
	}

public class ConsolePrinterUtility {
private static Scanner sc = new Scanner(System.in);
	
	public static void test() {
		Item d = new Item("whole duck", 47.21);
		ShoppingController.newItem(d);
		Customer c;
		
		c = inputCustomer();
		ShoppingController.newCustomer(c);
		System.out.println(c.toString());
		System.out.println();
		Order o = new Order(c);
		c.newOrder(o);
		ShoppingController.newOrder(o);
		Line l = inputLine(o);
		Line l2 = inputLine(o);
		
		System.out.println(c);
		System.out.println("DONE");
	}
	
	
	public static String inputString(String message) {
		System.out.print(message);
		String input = sc.nextLine();
		return input;
	}
	
	
	public static Integer inputInt(String message) {
		while(true) {
			System.out.print(message);
			String input = sc.nextLine();
			try {
				int returnValue = Integer.parseInt(input);
				System.out.print("\n");
				return returnValue;
			}
			catch (Exception e) {
				System.out.println("Error. Please try again.\n" + message);
				continue;
			}
		}
	}
	
	
	public static float inputFloat(String message) {
		while(true) {
			System.out.print(message);
			String input = sc.nextLine();
			try {
				float returnValue = Float.parseFloat(input);
				System.out.print("\n");
				return returnValue;
			}
			catch (Exception e) {
				System.out.println("Error. Please try again.\n" + message);
				continue;
			}
		}
	}
	public static Customer inputCustomer() {
		System.out.println("New Customer Form");
		
		String name = inputString("Email:\t");
		String pass = inputString("Password:\t");
		Customer newGuy = new Customer(name, pass);
		ShoppingController.newCustomer(newGuy);
		return newGuy;
	}
	public static Item inputItem() {
		System.out.println("New Item Form");
		
		String name = inputString("Item Name:\t");
		Float price = inputFloat("price:\t");
		Item newGuy = new Item(name, price);
		ShoppingController.newItem(newGuy);
		return newGuy;
	}
	
	public static Line inputLine(Order o) {
		System.out.println("New Item in Cart Form");
		Item i;
		do{
			String name = inputString("Item Name:\t");
			i =  ShoppingController.getItemByName(name);
		}
		while (Objects.isNull(i));
		int qty = inputInt("how many:\t");
		Line newGuy = new Line(qty, i);
		o.newLine(newGuy);
		ShoppingController.newLine(newGuy, o);
		return newGuy;
	}
	
	public static void menu(String name) {
		while(true) {
			Map<String, ProcessingMethod> options;
			switch (name) {
			case "main":	options = main_menu();
			default: options = main_menu();
			}
			Map<Integer, String> choices = new HashMap<Integer, String>();
			Integer ndx = 1;
			for (String i : options.keySet()) {
				  choices.put(ndx, i);
				  ndx += 1;  
			}
			choices.put(0, "exit");
			
			String message = "Please enter the number of your choice below.\n";
			for (Integer i : choices.keySet()) {
				  message += choices.get(i)+":\t"+ i+"\n";
			}
			
			int choicenum = -1;
			do{
				choicenum = inputInt(message);
			}
			while(!choices.containsKey(choicenum));
			if(choicenum == 0 ) {
				return;
			}
			options.get(choices.get(choicenum)).method();
		}
	
	}
	
	public static Map<String, ProcessingMethod> main_menu() {
	Map<String, ProcessingMethod> main_menu = new HashMap<String, ProcessingMethod>();
		
		main_menu.put("Register", new ProcessingMethod() {
		   public void method() { 
			   Customer newGuy = inputCustomer();
			   System.out.println(newGuy.toString());
			   String email = inputString("Email:\t");
			   String pass = inputString("Password:\t");
			   if( ShoppingController.login(new Customer(email, pass))) {
				   System.out.println("Logged in.");   
			   }
			   else {System.out.println("Didn't log in.");}
			   }
		});
		if (ShoppingController.hasCustomer()) {
			main_menu.put("Logout", new ProcessingMethod() {
			   public void method() {
				   ShoppingController.logout();
			   }
			});
			main_menu.put("Buy An Item", new ProcessingMethod() {
			   public void method() { 
				   Item i = null;
				   for(Item i2: ShoppingController.findItems()) {
					   System.out.println(i2);
				   }
				   do {
					   String name = inputString("Item name:\t");
				   
					   i = ShoppingController.getItemByName(name);
				   }
				   while (Objects.isNull(i));
				   Order o = new Order(ShoppingController.getLoggedIn());
				   Line l = o.newLine(new Line(1, i));
				   System.out.println(o.getCustomer().getId());
				   ShoppingController.newOrder(o);
				   ShoppingController.newLine(l,o);
				   System.out.println(o);
			   }
			});
			main_menu.put("Return An Item", new ProcessingMethod() {
			   public void method() { 
				   Map<Integer, Order> map = ShoppingController.findOrdersByCustomer(
			           ShoppingController.getLoggedIn()).stream()
					   .collect(Collectors.toMap(Order::getId, o1 -> o1));
				   for (Order o: ShoppingController.findOrdersByCustomer(
				           ShoppingController.getLoggedIn()) ) {
					   System.out.println();
					   System.out.println(o);
					   
				   }
				   int id;
				   do {
					   id = inputInt("Order id (press 0 to cancel):\t");
					   if (id == 0) {
						   return;
					   }
				   }
				   while (map.containsValue(id));
				   Order o = map.get(id);
				   int y = Math.abs(Integer.parseInt(o.getDate().substring(0, 4)));
				   int M = Math.abs(Integer.parseInt(o.getDate().substring(4, 7)));
				   int d = Math.abs(Integer.parseInt(o.getDate().substring(8, 10)));
				   int H = Math.abs(Integer.parseInt(o.getDate().substring(11, 13)));
				   int m = Math.abs(Integer.parseInt(o.getDate().substring(14, 16)));
				   int s = Math.abs(Integer.parseInt(o.getDate().substring(17, 19)));
				   LocalDateTime time = LocalDateTime.of(y,M,d,H,m,s);
				   LocalDateTime now = LocalDateTime.now();
				   if (ChronoUnit.MINUTES.between(time, now) < 60*24*15) {
					   System.out.println("Returning Item");
					   ShoppingController.deleteOrderById(o.getId());
				   }
				   else {
					   System.out.println("Could not in good conscience Return Item. It's made a home with you by now.");
				   }System.out.println(o);
			   }
			});
		}
		else {
			main_menu.put("Login", new ProcessingMethod() {
			   public void method() {
				   String email = inputString("Email:\t");
				   String pass = inputString("Password:\t");
				   if( ShoppingController.login(new Customer(email, pass))) {
					   System.out.println("Logged in.");
					   System.out.println();
					   for(Item i2: ShoppingController.findItems()) {
						   System.out.println(i2);
					   }
					   
				   }
				   else {System.out.println("Didn't log in.");}
			   }
			});
			main_menu.put("Buy An Item", new ProcessingMethod() {
				   public void method() { 
					   System.out.println("Log in before you do that!");   
				   }
				});
			main_menu.put("Return An Item", new ProcessingMethod() {
				   public void method() { 
					   System.out.println("Log in before you do that!");   
				   }
				});
		}
		
	return main_menu;
	}
	
}
