package cl.cmr.fwk.customer;

import java.sql.Connection;
import java.sql.DriverManager;
import cl.cmr.fwk.manager.PropertiesManager;

public class ConexionDatos {
	
	public static Connection conectarOra11(){
		
		Connection conn = null;
		 
		try {
					
			String user = PropertiesManager.getInstance().getFrameworkProperty("ora11_user", "framework");
			String pass = PropertiesManager.getInstance().getFrameworkProperty("ora11_pass", "framework");
			
		    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		    conn = DriverManager.getConnection("jdbc:oracle:thin:@server:puerto/ora11", user, pass);
		    
		    if (conn == null) {
		        System.out.println("Ha ocurrido un error al conectar ORA11LAB");
		    } 
		    
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return conn;	
		
	}
	
	public static Connection conectarXBD(){
			
		Connection connection = null;
		 
		try {
					
			String user = PropertiesManager.getInstance().getFrameworkProperty("bdX_user", "framework");
			String pass = PropertiesManager.getInstance().getFrameworkProperty("bdX_pass", "framework");
					    
	        String connectionUrl =
	                "jdbc:sqlserver://server:puerto;"
	                        + "databaseName=nombre;"
	                        + "user=" + user + ";"
	                        + "password=" + pass + ";";

	        connection = DriverManager.getConnection(connectionUrl);
			
		    if (connection == null) {
		        System.out.println("Ha ocurrido un error al conectar con BD");
		    } 
		    
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return connection;
		
	}
		
	public static Connection conectarOra9(){
        
        Connection conn = null;
        
        try {
                             
               String user = PropertiesManager.getInstance().getFrameworkProperty("ora9_user", "framework");
               String pass = PropertiesManager.getInstance().getFrameworkProperty("ora9_pass", "framework");
               
               DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
               conn = DriverManager.getConnection("jdbc:oracle:thin:@server:puerto/ORA9", user, pass);
            
	            if (conn == null) {
	                System.out.println("Ha ocurrido un error al conectar ORA9");
	            } 
            
        }catch(Exception e) {
               System.out.println(e.getMessage());
        }
        
        return conn; 
        
	}
  

	public static void closeConnection(Connection cnn) {
		
		try {
			
			 if (cnn != null) {
				 cnn.close();
			 }
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al cerrar sesión: " + e.getMessage());
		}
		
	}
	
}
