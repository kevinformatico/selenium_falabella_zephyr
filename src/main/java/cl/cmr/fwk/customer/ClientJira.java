package cl.cmr.fwk.customer;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Collections;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.json.JSONArray;
import org.json.JSONObject;
import cl.cmr.fwk.manager.PropertiesManager;

public class ClientJira {

	public static String pathJira;
	public static Client client;
	
	public static Client getInstance() throws Exception {	
		
		try {
			
			if(client == null) {
				HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(PropertiesManager.getInstance().getFrameworkProperty("jira_user", "framework"),
																					PropertiesManager.getInstance().getFrameworkProperty("jira_pass", "framework"));
			    client = ClientBuilder.newClient();
				client.register(feature);
				pathJira = PropertiesManager.getInstance().getFrameworkProperty("jira_path", "framework");
				
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al crear cliente jira: " + e.getMessage());
		}
		
		return client;
		
	}
	
	public Client getClient() {
		return ClientJira.client;
	}
	
	public static String retornaIdProyectoJIRA(String keyProyecto) {
		
		String idProyecto = null, strJSON;
		Response response;
		
		try {
				
			response = ClientJira.getInstance().target(pathJira + "/rest/api/2/project/" + keyProyecto)
			   .request(MediaType.APPLICATION_JSON_TYPE)
			   .get(); 		
			
			strJSON = response.readEntity(String.class);

			if(response.getStatus() == 200) {
				JSONObject json = new JSONObject(strJSON);
				idProyecto = json.get("id").toString();
			}else {
				System.out.println("No se ha encontrado el proyecto con key: " + keyProyecto);
			}

		}catch(Exception e) {
			System.out.println("Error al retornar id de proyecto: " + e.getMessage());
		}
		
		return idProyecto;
	}
	
	public static String crearEjecucionJIRA(String idCiclo, String idIssue, String idProyecto, String version) {
		
		Entity<?> payload;
		Response response;
		String idEjecucion = null, strJSON;
		
		try {
		
			payload = Entity.json("{ \"cycleId\": \"" + idCiclo + "\", \"issueId\": \"" + idIssue + "\" , \"projectId\" : " + idProyecto + "," + 
								  " \"versionId\": \"" + version + "\", \"assigneeType\": \"assignee\",  \"assignee\": \"jonathan.santos\" }");
			
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/execution")
					.request(MediaType.APPLICATION_JSON_TYPE)
					.post(payload);
			
			strJSON = response.readEntity(String.class);
			
			if(response.getStatus() == 200) {
				JSONObject json = new JSONObject(strJSON);
				JSONArray names = json.names();
				idEjecucion = names.get(0).toString();
			}else {
				System.out.println("La instancia de ejecución no se ha creado correctamente");
			}
			
		}catch(Exception e) {
			System.out.println("La ejecucion no se ha creado correctamente: " + e.getMessage());
		}
		
		return idEjecucion;
		
	}
	
	public static String retornaIdCicloJIRA(String idProyecto, String nomCiclo, String version) {
		
		String idCiclo = null;
		Response response;
		
		try {
			
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/cycle?projectId=" + idProyecto + "&versionId=" + version)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .get();
			
			String strJSON = response.readEntity(String.class);
					
			if(response.getStatus() == 200) {

				JSONObject json = new JSONObject(strJSON);
				JSONArray names = json.names();
				
				ObjectMapper mapper = new ObjectMapper();
				//Realizar un for para recorrer al usuario asignado
				for(int i = 0; i < names.length(); i++) {

				   if(!names.getString(i).equals("recordsCount")) {
					   
					   JsonNode node = mapper.readTree(strJSON).path(names.getString(i));
					   System.out.println(node.get("name").toString());
					   if(node.get("name").toString().replaceAll("\"", "").contains(nomCiclo)) {
						   idCiclo = names.getString(i);
						   break;
					   }
				   
				   }
				}
				
			}else {
				System.out.println("No se ha encontrado el ciclo: " + nomCiclo);
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al retornar el id de ciclo: " + e.getMessage());
		}
		
		return idCiclo;

	}
	
	public static void updateExecution(String idEjecucion, String estado) {
		
		Entity<?> payload;
		Response response;
		
		try {
			
			payload = Entity.json("{  \"status\": \"" + estado + "\"}");
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/execution/" + idEjecucion + "/execute")
					.request(MediaType.APPLICATION_JSON_TYPE)
					.put(payload);
			
			if(response.getStatus() != 200) {
				System.out.println("No se ha logrado actualizar el estado de la ejecución: " + response.getEntityTag());
			}
		
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al actualizar el estado de ejecución");
		}
		
	}
	
	public static String retornaIdIssue(String proyecto, String nombreTest) {
		
		String idIssue = null;
		String[] nomTestJIRA;

		try {
		
			String idProyecto = ClientJira.retornaIdProyectoJIRA(proyecto);
			nomTestJIRA = ClientJira.idIssue(idProyecto, nombreTest).split("-");
			idIssue = nomTestJIRA[0];

		}catch(Exception e) {
			System.out.println("Error al retornar id de issue: " + e.getMessage());
		}
		
		return idIssue;
		
	}
	
	public static String retornaIDIssueOld(String proyecto, String keytest) {
		
		String idIssue = null, strJSON;
		Response response;
		
		try {
			
			response = ClientJira.getInstance().target(pathJira + "/rest/api/2/issue/" + keytest)
					  .request(MediaType.APPLICATION_JSON_TYPE)
					  .get();
			
			strJSON = response.readEntity(String.class);

			if(response.getStatus() == 200) {
				JSONObject json = new JSONObject(strJSON);
				idIssue = json.get("id").toString();
			}else {
				System.out.println("No se ha encontrado el issue con key: " + keytest);
			}
			
		}catch(Exception e) {
			System.out.println("Error al retornar id de issue: " + e.getMessage());
		}
		
		return idIssue;	

	}
	
	public static String idIssue(String proyecto, String idTest){
		
		String strJSON, idIssue, nomIssue, idTestCase = null;
		
		try {
			
			Response response = ClientJira.getInstance().target(pathJira + "/rest/api/2/search?jql=project=" + proyecto + "&fields=issue,status,name&startAt=0&maxResults=8000")
					.request(MediaType.APPLICATION_JSON_TYPE)
					.get();
			
			strJSON = response.readEntity(String.class);
			
			if(response.getStatus() == 200) {
				
				JSONObject jsonBase = new JSONObject(strJSON);
				JSONArray arrIssues = jsonBase.optJSONArray("issues");
				
				for (int i = 0; i < arrIssues.length(); i++) {
					
					JSONObject json = (JSONObject) arrIssues.get(i);
					idIssue = json.get("id").toString();
					//JsonPath jp = JsonPath.read(strJSON, "$.issues[?(@.id==" + idIssue + ")]");
					
					nomIssue = retornaNombreIssue(idIssue);
					
					if(nomIssue.contains(idTest)) {
						idTestCase = idIssue;
						System.out.println("----"+nomIssue+"----");
						break;
					}
				}
				
			}else {
				System.out.println("No se ha encontrado el id para el issue: " + idTest);
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al retornar lista de casos del proyecto  " + proyecto + ":" + e.getMessage());
		}
		
		return idTestCase;
		
	}
	
	public static String retornaNombreIssue(String idIssue) {
		
		String strJSON, nomIssue = null;
		
		try {
			
			Response response = ClientJira.getInstance().target(pathJira + "/rest/api/2/issue/" + idIssue)
			   .request(MediaType.APPLICATION_JSON_TYPE)
			   .get(); 
	
				strJSON = response.readEntity(String.class);

				if(response.getStatus() == 200) {
					JSONObject json = new JSONObject(strJSON);
					JSONObject nom = json.optJSONObject("fields");
					nomIssue = nom.get("summary").toString();
					
				}
				
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al retornar el nombre de issue:  " + e.getMessage());
		}
		
		return nomIssue;
	}
	
	public static String retornaVersionIdJIRA(String idProyecto) {
		
		String versionID = null, strJSON;
		Response response;
		
		try {
			
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/util/versionBoard-list?projectId=" + idProyecto)
					  .request(MediaType.APPLICATION_JSON_TYPE)
					  .get();
			
			strJSON = response.readEntity(String.class);
			
			if(response.getStatus() == 200) {
				JSONObject json = new JSONObject(strJSON);
				if(!strJSON.contains("Versión")) {
					versionID = "-1";
				}else {
					versionID = json.getJSONArray("unreleasedVersions").getJSONObject(1).get("value").toString();
				}
			}else {
				System.out.println("No se ha encontrado la versión del proyecto id: " + idProyecto);
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al retornar la versión del proyecto: " + e.getMessage());
		}
		
		return versionID;
		
	}
	
	public static String crearPasoTestJIRA(String nombrePaso, String dato, String resEsperado, String idIssue) {
		
		Response response;
		Entity<?> payload;
		String strJSON, idPaso = null;
		
		try{
			
			payload = Entity.json("{  \"step\": \"" + nombrePaso + "\",  \"data\": \"" + dato + "\" , \"result\": \"" + resEsperado + " \"}");
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/teststep/" + idIssue)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .post(payload);

			strJSON = response.readEntity(String.class);
			
			if(response.getStatus() == 200) {
				JSONObject json = new JSONObject(strJSON);
				idPaso = json.get("id").toString();
			}else {
				System.out.println("No se ha encontrado el issue id: " + idIssue);
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al crear pasos: " + e.getMessage());
		}
		
		return idPaso;
		
	}
	
	public static String actualizaCreaEstadoPasoTestJIRA(String idPaso, String idIssue, String idEjecucion, String estado) {
		
		Response response;
		Entity<?> payload;
		String strJSON = null, idStep = null;
		
		try {

			payload = Entity.json("{ \"stepId\": \"" + idPaso + "\", \"issueId\": \"" + idIssue + "\", \"executionId\": \"" + idEjecucion + "\", \"status\": \"" + estado + "\"}");
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/stepResult")
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .post(payload);

			strJSON = response.readEntity(String.class);
			
			if(response.getStatus() == 200) {
				JSONObject jsonarray = new JSONObject(strJSON);
				idStep = jsonarray.get("id").toString();
			}else {
				System.out.println(strJSON);
				System.out.println("No se ha logrado actualizar el estado del paso id " + idPaso + " : " + strJSON);
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al actualizar estado de pasos: " + e.getMessage());
		}
		
		return idStep;
		
	}
	
	public static String actualizaEstadoPasoTestJIRA(String idPaso, String estado, String comentario) {
		
		Response response;
		Entity<?> payload;
		String strJSON = null, idStep = null;
		
		try {

			payload = Entity.json("{  \"status\": \"" + estado + "\" , \"comment\": \"" + comentario + " \"}");
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/stepResult/" + idPaso)
					  .request(MediaType.APPLICATION_JSON_TYPE)
					  .put(payload);
			
			strJSON = response.readEntity(String.class);
			
			if(response.getStatus() == 200) {
				JSONObject jsonarray = new JSONObject(strJSON);
				idStep = jsonarray.get("id").toString();
			}else {
				System.out.println(strJSON);
				System.out.println("No se ha logrado actualizar el estado del paso id " + idPaso + " : " + strJSON);
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al actualizar estado de pasos: " + e.getMessage());
		}
		
		return idStep;
		
	}
	
	public static int retornaCantidadPasosTestJIRA(String idIssue) {
		
		Response response;
		int cantidad = 0;
		String strJSON;
		String variable;
		
		try {
			
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/teststep/" + idIssue)
					  .request(MediaType.APPLICATION_JSON_TYPE)//MediaType.APPLICATION_JSON_TYPE
					  .get();

			strJSON = response.readEntity(String.class);
			System.out.println("--------inicio----------");
			System.out.println("resultado: "+strJSON.toString());
			System.out.println("-----------fin------------");

//	        for (int i = 0; i < arr.length(); i++) {
//	            String post_id = arr.getJSONObject(i).getString("id");
//	            System.out.println(post_id);
//	        }


			if(response.getStatus() == 200) {
				JSONObject obj = new JSONObject(strJSON);
		        JSONArray arr = obj.getJSONArray("stepBeanCollection");
		        System.out.println(arr.length());
		        
//				JSONObject jsonBase = new JSONObject(strJSON);
//				System.out.println(jsonBase.length());
//				JSONArray arrIssues = (JSONArray) jsonBase.get("orderId");
//				JSONArray jsonarray = new JSONArray(response);
//				
//				cantidad = jsonarray.length();
				cantidad = arr.length();
			}else{
				System.out.println("No se ha logrado contar la cantidad de pasos del issue id: " + idIssue);
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al extraer la cantidad de pasos: " + e.getMessage());
		}
		
		return cantidad;
		
	}
	
	public static ArrayList<String> retornaListaPasosTestJIRA(String idIssue) {
		
		Response response;
		ArrayList<String> idPasos = new ArrayList<String>();
		String strJSON;
		
		try {
			
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/teststep/" + idIssue)
					  .request(MediaType.APPLICATION_JSON_TYPE)
					  .get();
			
			strJSON = response.readEntity(String.class);
			
			if(response.getStatus() == 200) {
				JSONObject obj = new JSONObject(strJSON);
		        JSONArray arr = obj.getJSONArray("stepBeanCollection");
//				JSONArray jsonarray = new JSONArray(strJSON);
				System.out.println(arr.length());
				for (int i = 0; i < arr.length() ; i++) {
					idPasos.add(arr.getJSONObject(i).get("id").toString());
				}
//				for (int i = 0; i < jsonarray.length() ; i++) {
//					idPasos.add(jsonarray.getJSONObject(i).get("id").toString());
//				}
			}else{
				System.out.println("No se ha logrado contar la cantidad de pasos del issue id:" + idIssue);
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al extraer la cantidad de pasos: " + e.getMessage());
		}
		
		return idPasos;
		
	}
	
	
	public static ArrayList<String> retornaNombrePasosTestJIRA(String idIssue) {
		
		Response response;
		ArrayList<String> nomPasos = new ArrayList<String>();
		String strJSON;
		
		try {
			
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/teststep/" + idIssue)
					  .request(MediaType.APPLICATION_JSON_TYPE)
					  .get();
			
			strJSON = response.readEntity(String.class);
			
			if(response.getStatus() == 200) {
				JSONArray jsonarray = new JSONArray(strJSON);
				for (int i = 0; i < jsonarray.length() ; i++) {
					nomPasos.add(jsonarray.getJSONObject(i).get("step").toString());
				}
			}else{
				System.out.println("No se ha logrado extraer el nombre de los pasos para el issue id:" + idIssue);
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al extraer los nombres de pasos: " + e.getMessage());
		}
		
		return nomPasos;
		
	}
	
	public static ArrayList<String> retornaResultadoEsperadoPasosTestJIRA(String idIssue) {
		
		Response response;
		ArrayList<String> desResultado = new ArrayList<String>();
		String strJSON;
		
		try {
			
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/teststep/" + idIssue)
					  .request(MediaType.APPLICATION_JSON_TYPE)
					  .get();
			
			strJSON = response.readEntity(String.class);
			
			if(response.getStatus() == 200) {
				JSONArray jsonarray = new JSONArray(strJSON);
				for (int i = 0; i < jsonarray.length() ; i++) {
					desResultado.add(jsonarray.getJSONObject(i).get("result").toString());
				}
			}else{
				System.out.println("No se ha logrado extraer el resultado esperado de los pasos para el issue id:" + idIssue);
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al extraer el resultado esperado de los pasos: " + e.getMessage());
		}
		
		return desResultado;
		
	}
	
	public static void attachmenEjecucionJIRA(String idEntity, String pathFile, String entityType) {
		
		String outputString;
		
		try {

			String user = PropertiesManager.getInstance().getFrameworkProperty("jira_user", "framework");
			String pass = PropertiesManager.getInstance().getFrameworkProperty("jira_pass", "framework");
			
//			Process pro = Runtime.getRuntime().exec("curl -D- -u " + user + ":" + pass + " -X POST -H \"X-Atlassian-Token:no-check\" -F \"file=@" + 
//			pathFile + " \" " + pathJira + "/rest/zapi/latest/attachment?entityId=" + idEntity + "&entityType=" + entityType); //stepresult - execution
			
			Process pro = Runtime.getRuntime().exec("curl -D- -u " + user + ":" + pass + " -X POST -H \"X-Atlassian-Token:no-check\" -F \"file=@" + 
					  pathFile + " \" " + pathJira + "/rest/zapi/latest/attachment?entityId=" + idEntity + "&entityType=" + entityType);
			
			DataInputStream curlIn = new DataInputStream(pro.getInputStream());
			while ((outputString = curlIn.readLine()) != null) {
			System.out.println(outputString + " ");
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al adjuntar archivos al paso la cantidad de pasos:" + e.getMessage());
		}
	}
	
	public static void eliminaPasosTestJIRA(String idIssue, ArrayList<String> listaPasos) throws Exception {
		
		//Response response;
		
		for (int i = 0; i < listaPasos.size(); i++) {
			
			//response = client.target(pathJira + "/rest/zapi/latest/teststep/" + idIssue + "/" + listaPasos.get(i))
			//	         .request(MediaType.APPLICATION_JSON_TYPE)
			//			 .delete();
			
			ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/teststep/" + idIssue + "/" + listaPasos.get(i))
					  .request(MediaType.APPLICATION_JSON_TYPE)
					  .delete();
			
		}

	}
	
	public static void elminaEjecucionJIRA(String idEjecucion) {
		
		Response response;
		
		try {
			
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/execution/" + idEjecucion)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .delete();
			
			if(response.getStatus() != 200) {
				System.out.println("No existe la ejecución con id: " + idEjecucion);
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al eliminar la ejecución: " + e.getMessage());
		}
	
	}
		
	public static ArrayList<String> retornaListaResultadosPasosJIRA(String idEjecucion){
		
		Response response;
		ArrayList<String> listaPasos = new ArrayList<String>();
		String strJSON = null;
		
		try {
		
			response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/stepResult?executionId=" + idEjecucion)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .get();
			
			strJSON = response.readEntity(String.class);
			
			if(response.getStatus() == 200) {
				JSONArray json = new JSONArray(strJSON);
				for (int i = 0; i < json.length(); i++) {
					listaPasos.add(json.getJSONObject(i).get("id").toString());
				}
			}else {
				System.out.println("No existen pasos con resultados asociados al id de ejecución: " + idEjecucion);
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al retornas lista de resultados de pasos: " + e.getMessage());
		}
		
		Collections.sort(listaPasos);
		return listaPasos;
		
	}
	
	public static void eliminaAttachment(String id) throws Exception {
		
		//Response response;	
		//response = client.target(pathJira + "/rest/zapi/latest/attachment/" + id)
		//           .request(MediaType.APPLICATION_JSON_TYPE)
		//			 .delete();
		
		ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/attachment/" + id)
				  .request(MediaType.APPLICATION_JSON_TYPE)
				  .delete();
		
	}
	
	public static String retornaIdAttachment(String id, String tipoEntidad) throws Exception {
		
		Response response;
		String idAttachment = null, strJSON = null;
		
		response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/attachment/attachmentsByEntity?entityId=" + id + "&entityType=" + tipoEntidad)
				
				  .request(MediaType.APPLICATION_JSON_TYPE)
				  .get();

		strJSON = response.readEntity(String.class);
		
		if(response.getStatus() == 200 && !strJSON.equals("{\"data\":[]}") ) {
			JSONArray json = new JSONArray(strJSON);
			idAttachment = json.getJSONObject(0).get("fileId").toString();
		}else {
			System.out.println("No existen attachment asociado a la entidad id: " + id);
		}		
		
		return idAttachment;
		
	}
	
	@SuppressWarnings("deprecation")
	public static String crearTestCaseJIRA(String idProyecto) {
		
		String idIssue = null, outputString = null;
		
		try {
			
			String user = PropertiesManager.getInstance().getFrameworkProperty("jira_user", "framework");
			String pass = PropertiesManager.getInstance().getFrameworkProperty("jira_pass", "framework");
				
			String gd = "--data { \"fields\":{\"project\":{\"id\":\"" + idProyecto + "\"},\"summary\":\"Test Prueba\",\"description\":\"Creating of an issue using project keys\",\"issuetype\":{\"id\":\"1\"}} } "  
			+ pathJira + "/rest/api/2/issue/";
			
			Process pro = Runtime.getRuntime().exec("curl -D- -u " + user + ":" + pass + " -X POST -H \"Content-Type: application/json\" " + gd);

			System.out.println(gd);
		    DataInputStream curlIn = new DataInputStream(pro.getInputStream());
		 
	        while ((outputString = curlIn.readLine()) != null) {
	            System.out.println(outputString + " ");
	        }
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al crear un nuevo test:  " + e.getMessage());
		}
		
		return idIssue;
		
	}
	
	public static String retornaIdFolder(String idCiclo, String idProyecto, String version) {
		
		String idFolder = null, strJSON;
		
		try {
			
			Response response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/cycle/" + idCiclo + "/folders?projectId=" + idProyecto + "&versionId=" + version + "&limit=0&offset=0")
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .get();
			
			System.out.println("status: " + response.getStatus());
			strJSON = response.readEntity(String.class);
			
			System.out.println(strJSON);
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al retornar el id de folder: " + e.getMessage());
		}
		
		return idFolder;
		
	}
	
	public static void copiarEjecuciones(String idProyecto, String version, String idCiclo) {
		
		try {
			
			Entity<?> payload = Entity.json("{  \"executions\": [\"2119\",\"2420\"],  \"projectId\": \"" + idProyecto + "\",  \"versionId\": \"" + version + "\",  \"clearStatusFlag\": true,  \"clearDefectMappingFlag\": true}");
			Response response = ClientJira.getInstance().target(pathJira + "/rest/zapi/latest/cycle/" + "313" + "/copy")
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .put(payload);

			String strJSON = response.readEntity(String.class);
			
			System.out.println(strJSON);
			
			System.out.println("status: " + response.getStatus());
			System.out.println("headers: " + response.getHeaders());
			System.out.println("body:" + response.readEntity(String.class));
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}	
}
