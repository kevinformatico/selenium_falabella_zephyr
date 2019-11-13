package cl.cmr.fwk.customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class RunDatos {

//	public static void main(String[] args) throws SQLException {
//
//		String[][] matriz = new String[5][2];
//		matriz[0][0] = "Estado del Cliente";
//		matriz[0][1] = "Activo";
//		
//		matriz[1][0] = "Estado del Fondo";
//		matriz[1][1] = "Abierto";
//		
//		matriz[2][0] = "Tipo de Cuenta";
//		matriz[2][1] = "Plan normal cta existente";
//		
//		matriz[3][0] = "Saldo";
//		matriz[3][1] = "Con saldo";
//		
//		matriz[4][0] = "Monto del Aporte";
//		matriz[4][1] = "Sin aporte mínimo";
//		
//		String query = FFMM.armaQueryBase(matriz);
//		consultarDatos(query);
//		
//	}
	
	public static String[][] consultarDatos(String query){
		
		String[][] matriz = new String[1][8];
		
		try {
			
			Connection conn = ConexionDatos.conectarOra11();
		    Statement stmt = conn.createStatement();
		    ResultSet rset =  stmt.executeQuery(query);
		   	    
		    if(rset.next()) {
		    	System.out.println(rset.getString("RUT_CLI"));
		    	matriz[0][0] = rset.getString("RUT_CLI");
		    	
		    	System.out.println(rset.getString("SUB_RUT"));
		    	matriz[0][1] = rset.getString("SUB_RUT");
		    	
		    	System.out.println(rset.getString("NOM_FONDO"));
		    	matriz[0][2] = rset.getString("NOM_FONDO");
		    	
		    	System.out.println(rset.getString("CTA"));
		    	matriz[0][3] = rset.getString("CTA");
		    	
		    	System.out.println(rset.getString("TIPO_PLAN"));
		    	matriz[0][4] = rset.getString("TIPO_PLAN");
		    	
		    	System.out.println(rset.getString("CUOTA_INI_DIA"));
		    	matriz[0][5] = rset.getString("CUOTA_INI_DIA");
		    	
		    	System.out.println(rset.getString("APORTE_MINIMO"));
		    	matriz[0][6] = rset.getString("APORTE_MINIMO");
		    	
		    	System.out.println(rset.getString("CUOTA_INI_DIA"));
		    	matriz[0][7] = rset.getString("CUOTA_INI_DIA");
		    	
		    }
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al consultar datos: " + e.getMessage());
		}
		
		return matriz;
		
	}

}
