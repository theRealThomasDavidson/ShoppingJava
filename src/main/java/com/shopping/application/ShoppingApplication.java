package com.shopping.application;

import java.sql.Connection;
import com.shopping.utility.ConnectionManager;
import com.shopping.utility.ConsolePrinterUtility;

public class ShoppingApplication {
	static Connection conn = ConnectionManager.getConnection();

	public static void main(String args[]) {
		ConsolePrinterUtility.menu("main");
	}
}
