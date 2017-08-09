package dao;

import java.sql.Connection;

import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionManager {

	private ConnectionManager(){}
	
	
	private static final String DATABASE = "localhost:3306/webforum";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";
	
	private static BasicDataSource CONNECTION_PULL;
	
	
	public static void init() {
		if(CONNECTION_PULL != null) destroy();
		
		System.out.println("Initializing connection to database...");
		
		try {
			CONNECTION_PULL = new BasicDataSource();
			CONNECTION_PULL.setDriverClassName("com.mysql.jdbc.Driver");
			CONNECTION_PULL.setUrl("jdbc:mysql://" + DATABASE);
			CONNECTION_PULL.setUsername(USER_NAME);
			CONNECTION_PULL.setPassword(PASSWORD);
			
			System.out.println("Done!");
		} catch (Exception e) {
			System.out.println("Failed!");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void destroy(){
		
		System.out.println("Closing connection to database...");
	
	try {
		CONNECTION_PULL.close();
		
		
	} catch (Exception e) {
		System.out.println("Failed!");
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
	}
	
	
	public static Connection getConnection() throws Exception{
		if(CONNECTION_PULL == null) init();
		return CONNECTION_PULL.getConnection();
	}
}
