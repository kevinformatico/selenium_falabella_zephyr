package cl.cmr.fwk.customer;

import java.util.ArrayList;

import javax.ws.rs.client.Client;
import cl.cmr.fwk.customer.ClientJira;

public class StepZephyr {

	public static ArrayList<String> stepTest = new ArrayList<String>(), listaPasos, listaResultados;
	static Client client;
	
	public static void main(String[] args) {
		
		try {
			
			//Describir nombres de pasos
			stepTest.add("Abrir URL Web BanFalabella_A");
			stepTest.add("Ingresar al Login Web BanFalabella_A");
			stepTest.add("Seleccionar Categoria_a");
			stepTest.add("Seleccionar Item_a");
//			stepTest.add("Modulo Cartera ActivaCMR5Prueba");
//			stepTest.add("Validar Marco de Actuacion PersonalCMR6Prueba");
			
			//Crea los pasos en Zephyr(JIRA)
			client = ClientJira.getInstance();
			String idIssue = ClientJira.retornaIdIssue("MT","CDP-Hector");
			
			//Recuperamos la cantidad de pasos asociados al caso
			int size = ClientJira.retornaCantidadPasosTestJIRA(idIssue);
			
			//Si contiene pasos, debemos eliminar
			if(size > 0) {
				listaPasos = ClientJira.retornaListaPasosTestJIRA(idIssue);
				ClientJira.eliminaPasosTestJIRA(idIssue, listaPasos);
			}
			
			//Creamos los pasos en Zephyr en base a la lista ingresada
			for (int i = 0; i < stepTest.size(); i++) {
				ClientJira.crearPasoTestJIRA(stepTest.get(i), "", "", idIssue);
			}
			
//			System.out.println("Trae IdPaso");
//			
//			for (int i = 0; i < stepTest.size(); i++) {
//				System.out.println(ClientJira.crearPasoTestJIRA(stepTest.get(i), "", "", idIssue));
//			}
//			
//			
//			System.out.println("Trae IdPaso");
			
		
		}catch(Exception e) {
			System.out.println("Error al setear pasos al test: " + e.getMessage());
		}
		
	}

}
