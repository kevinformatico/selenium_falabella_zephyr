package cl.cmr.page;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import cl.cmr.fwk.customer.BaseTest;
import cl.cmr.fwk.customer.FuncionesBase;

public class FondosMutuos extends BaseTest{

	public static String ctaCreada;

	public static String seleccionarFondo(String fondo) {

		String msg = "OK";
		
		try {

			FuncionesBase.esperarObjetoXpath("table", "sort-by", "fondosClienteOrden", 240);
			msg = FuncionesBase.clickObjetoXpath("Fondo " + fondo, "td", "text", fondo);
			Thread.sleep(3000);
			
		}catch(Exception e) {
			msg = "Ha ocurrido un error al seleccionar un fondo: " + e.getMessage();
		}

		return msg;

	}

	public static String operarFondo(String fondo) {

		String msg = "OK";
		WebElement obj = null;
		List<WebElement> listaObj = null, listaTd;
		boolean flag = false;
		
		try {

			FuncionesBase.esperarObjetoXpath("table", "sort-by", "fondosClienteOrden", 120);
			obj = FuncionesBase.retornaObjetoXpath("table", "sort-by", "fondosClienteOrden");
	
			listaObj = obj.findElements(By.tagName("tr")); 

			if(listaObj.size() > 0) {
				for (int i = 0; i < listaObj.size(); i++) {
					if(listaObj.get(i).getText().contains(fondo)) {
						listaObj.get(i).click();
						listaTd = listaObj.get(i).findElements(By.tagName("td"));
						listaTd.get(listaTd.size() -1).click();
						flag = true;
						break;
					}
				}
			}else {
				msg = "El objeto 'Boton Operar' no se ha encontrado";
			}
			
			if(!flag) {
				msg = "El cliente no posee el fondo '" + fondo + "'";
			}
			

		}catch(Exception e) {
			msg = "Ha ocurrido un error al intentar operar fondos: " + e.getMessage();
		}

		return msg;

	}
	
	public static String operarFondoReinversion(String fondo) {

		String msg = "OK";
		WebElement obj = null;
		List<WebElement> listaObj = null, listaTd;
		boolean flag = false;
		
		try {

			FuncionesBase.esperarObjetoXpath("table", "class", "responsive", 120);
			obj = FuncionesBase.retornaObjetoXpath("table", "class", "responsive");
	
			listaObj = obj.findElements(By.tagName("tr")); 

			if(listaObj.size() > 0) {
				for (int i = 0; i < listaObj.size(); i++) {
					if(listaObj.get(i).getText().contains(fondo)) {
						listaObj.get(i).click();
						listaTd = listaObj.get(i).findElements(By.tagName("td"));
						listaTd.get(listaTd.size() -1).click();
						flag = true;
						break;
					}
				}
			}else {
				msg = "El objeto 'Boton Seleccionar' no se ha encontrado";
			}
			
			if(!flag) {
				msg = "El cliente no posee el fondo '" + fondo + "'";
			}
			

		}catch(Exception e) {
			msg = "Ha ocurrido un error al intentar operar fondos: " + e.getMessage();
		}

		return msg;

	}
	
	public static String ingresarDatosAporte(String cuenta, String monto, boolean familiar, String clave) {

		String msg = "OK";
		WebElement obj = null;
		List<WebElement> listaObj;
		int indexCuenta = 0;
		
		if(monto.equals("0")) {
			monto = "1000";
		}
		
		try {

			FuncionesBase.esperarObjetoXpath("table", "sort-by", "fondosClienteOrden", 60);
			
			if(cuenta.toUpperCase().contains("CUENTA NUEVA")) {
				
				obj = FuncionesBase.retornaObjetoXpath("table", "sort-by", "fondosClienteOrden");
				
				if(obj.getText().contains("Cuenta Nueva")) {
					
					listaObj = driver.findElements(By.xpath("//span[contains(text(),'Aportar')]"));
					if(listaObj.size() > 0) {
						FuncionesBase.enfocaObjeto(listaObj.get(listaObj.size() - 1));
						listaObj.get(listaObj.size() - 1).click();
					}else {
						msg = "El boton 'Aportar' no existe en la cuenta '" + cuenta + "'";
						return msg;
					}
					
				}else {
					msg = "La opcion aporte en cuenta nueva no existe";
					return msg;
				}
				
			}else {
				
				obj = FuncionesBase.retornaObjetoXpath("table", "sort-by", "fondosClienteOrden");
				indexCuenta = FuncionesBase.retornaFilaContenidoTabla(obj, "class", "tableContenido--cuentas info--detalle ng-scope", cuenta);
		
				if(!(indexCuenta > -1)) {
					msg =  "La cuenta '" + cuenta + "' no existe dentro de la cartera de fondos";
					return msg;
				}
				
				listaObj = driver.findElements(By.xpath("//span[contains(text(),'Aportar')]"));
		
				if(listaObj.size() > 0) {
					FuncionesBase.enfocaObjeto(listaObj.get(indexCuenta));
					listaObj.get(indexCuenta).click();
				}else {
					msg = "El boton 'Aportar' no existe en la cuenta '" + cuenta + "'";
				}

			}
			
			if(familiar) {
				msg = FuncionesBase.clickObjetoXpath("Plan Familiar de Fondos", "md-switch", "aria-label", "Checkbox 1");
				if(!msg.equals("OK")) {
					return msg;
				}
			}
			
			msg = FuncionesBase.setTextoObjeto("Monto", "name", "monto_aporte", monto, false);
			if(!msg.equals("OK")) {
				return msg;
			}

			Thread.sleep(1500);
			msg = FuncionesBase.clickObjetoXpath("Aceptar Terminos", "md-checkbox", "aria-label", "Checkbox 1");
			if(!msg.equals("OK")) {
				return msg;
			}

			msg = FuncionesBase.setTextoObjeto("Clave de Internet", "name", "claveInternet", clave, false);
			if(!msg.equals("OK")) {
				return msg;
			}

			Thread.sleep(2000);
			FuncionesBase.scrollUp("50");
			
		}catch(Exception e) {
			msg = "Ha ocurrido un error al ingresar datos del aporte: " + e.getMessage();
		}
		return msg;

	}

	public static String ingresarDatosAporteEmp(String cuenta, String monto, boolean familiar, String clave) {

		String msg = "OK";
		WebElement obj = null;
		List<WebElement> listaObj;
		int indexCuenta = 0;
		
		if(monto.equals("0")) {
			monto = "1000";
		}
		
		try {

			FuncionesBase.esperarObjetoXpath("table", "sort-by", "fondosClienteOrden", 60);
			
			if(cuenta.toUpperCase().contains("CUENTA NUEVA")) {
				
				obj = FuncionesBase.retornaObjetoXpath("table", "sort-by", "fondosClienteOrden");
				
				if(obj.getText().contains("Cuenta Nueva")) {
					
					listaObj = driver.findElements(By.xpath("//span[contains(text(),'Aportar')]"));
					if(listaObj.size() > 0) {
						FuncionesBase.enfocaObjeto(listaObj.get(listaObj.size() - 1));
						listaObj.get(listaObj.size() - 1).click();
					}else {
						msg = "El boton 'Aportar' no existe en la cuenta '" + cuenta + "'";
						return msg;
					}
					
				}else {
					msg = "La opcion aporte en cuenta nueva no existe";
					return msg;
				}
				
			}else {
				
				obj = FuncionesBase.retornaObjetoXpath("table", "sort-by", "fondosClienteOrden");
				indexCuenta = FuncionesBase.retornaFilaContenidoTabla(obj, "class", "tableContenido--cuentas info--detalle ng-scope", cuenta);
		
				if(!(indexCuenta > -1)) {
					msg =  "La cuenta '" + cuenta + "' no existe dentro de la cartera de fondos";
					return msg;
				}
				
				listaObj = driver.findElements(By.xpath("//span[contains(text(),'Aportar')]"));
		
				if(listaObj.size() > 0) {
					FuncionesBase.enfocaObjeto(listaObj.get(indexCuenta));
					listaObj.get(indexCuenta).click();
				}else {
					msg = "El boton 'Aportar' no existe en la cuenta '" + cuenta + "'";
				}

			}
			
			if(familiar) {
				msg = FuncionesBase.clickObjetoXpath("Plan Familiar de Fondos", "md-checkbox", "aria-label", "Checkbox 1");
				if(!msg.equals("OK")) {
					return msg;
				}
			}
			
			Thread.sleep(1500);
			msg = FuncionesBase.setTextoObjeto("Monto", "name", "monto_aporte", monto, false);
			if(!msg.equals("OK")) {
				return msg;
			}

			Thread.sleep(1500);
			msg = FuncionesBase.clickObjetoXpath("Aceptar Terminos", "md-checkbox", "aria-label", "Checkbox 1");
			if(!msg.equals("OK")) {
				return msg;
			}
			
			msg = FuncionesBase.clickObjetoXpath("Enviar Clave", "span", "text", "ENVIAR CLAVE");
			if(!msg.equals("OK")) {
				return msg;
			}
			
			FuncionesBase.esperarObjetoXpath("div", "class", "md-dialog-container ng-scope' and @tabindex='-1", 30);
			WebElement objD = driver.findElement(By.xpath("//div[@class='md-dialog-container ng-scope' and @tabindex='-1']"));
			
			Actions at = new Actions(driver);
			at.moveToElement(objD, 0, 100).click().build().perform();
			
			Thread.sleep(2000);
			FuncionesBase.scrollUp("50");
			
		}catch(Exception e) {
			msg = "Ha ocurrido un error al ingresar datos del aporte: " + e.getMessage();
		}
		return msg;

	}
	
	public static String ingresarDatosReinversion(String cuenta, String tipoReinversion, String monto, String fondoReinversion, String ctaReinversion, String clave) {

		String msg = "OK";
		WebElement obj = null, objTr = null;
		List<WebElement> listaBtn, listaTr;
		
		try {
				
			FuncionesBase.esperarObjetoXpath("table", "sort-by", "fondosClienteOrden", 60);
			
			obj = FuncionesBase.retornaObjetoXpath("table", "sort-by", "fondosClienteOrden");
			listaTr = obj.findElements(By.xpath("//tr[@class='tableContenido--cuentas info--detalle ng-scope']"));
			
			for (int i = 0; i < listaTr.size(); i++) {
				if(listaTr.get(i).getText().contains(cuenta)) {
					objTr = listaTr.get(i);
					FuncionesBase.enfocaObjeto(objTr);
					objTr.click();
					break;
				}
			}
			
			if(objTr != null) {
				listaBtn = objTr.findElements(By.tagName("button"));
				
				if(listaBtn.size() == 2) {
					FuncionesBase.enfocaObjeto(listaBtn.get(0));
					listaBtn.get(0).click();
	
				}else {
					msg = "El botón 'Rescatar' no existe en la cuenta '" + cuenta + "'";
					return msg;
				}
			}else {
				msg = "El cliente no posee la cuenta '" + cuenta + "'";
				return msg;
			}
			
			FuncionesBase.esperarObjetoXpath("md-radio-button", "aria-label", "Total", 60);

			if(tipoReinversion.replaceAll(" ", "").toUpperCase().equals("TOTAL")) {
				
				msg = FuncionesBase.clickObjetoXpath("Rescate Total", "md-radio-button", "aria-label", "Total");
				if(!msg.equals("OK")) {
					return msg;
				}
				
			}else {
			
				msg = FuncionesBase.setTextoObjeto("Monto", "name", "monto", monto, false);
				if(!msg.equals("OK")) {
					return msg;
				}

			}
			
			msg = FuncionesBase.clickObjetoXpath("Destino del Rescate", "md-select", "name", "pagarDesde");
			if(!msg.equals("OK")) {
				return msg;
			}
			
			Thread.sleep(2500);
			
			msg = FuncionesBase.clickObjetoXpath("Reinversion de Fondos Mutuos", "div", "text", "Reinversión de Fondos Mutuos");
			if(!msg.equals("OK")) {
				return msg;
			}
			
			FuncionesBase.esperarObjetoXpath("p", "class", "paginacion--parrafo ng-binding", 90);
			
			WebElement objPaginador = FuncionesBase.retornaObjetoXpath("p", "class", "paginacion--parrafo ng-binding");
			
			String[] textoPaginador = objPaginador.getText().split("de");
			int index = Integer.parseInt(textoPaginador[1].replaceAll(" ", ""));
			boolean flag = false;
			
			for (int i = 0; i < index; i++) {

				if(!FuncionesBase.retornaObjetoXpath("table", "class", "responsive").getText().contains(fondoReinversion)) {
					FuncionesBase.clickObjetoXpath("Paginador Fondos de Reinversion", "button", "aria-label", "Adelante");
				}else {
					flag = true;
					break;
				}
			}
			
			if(!flag) {
				msg = "El cliente no posee el fondo de renversion '" + fondoReinversion + "'";
				return msg;
			}
			
			msg = FondosMutuos.operarFondoReinversion(fondoReinversion);
			if(!msg.equals("OK")) {
				return msg;
			}
			
			obj = FuncionesBase.retornaObjetoXpath("div", "class", "tablaMaster ng-scope");
			listaTr = obj.findElements(By.xpath("//tr[@class='tableContenido--cuentas info--detalle ng-scope']"));
			
			for (int i = 0; i < listaTr.size(); i++) {
				if(!listaTr.get(i).getText().contains(cuenta)) {
					objTr = listaTr.get(i);
					//FuncionesBase.enfocaObjeto(listaTr.get(i));
					//listaTr.get(i).click();
					listaBtn = objTr.findElements(By.tagName("button"));
					if(listaBtn.size() > 0) {
						FuncionesBase.enfocaObjeto(listaBtn.get(0));
						FuncionesBase.doubleClickObjetoElement(listaBtn.get(0), "Aportar");
					}
					break;
				}
			}
			
			Thread.sleep(2500);
//			if(objTr != null) {
//				//listaBtn = objTr.findElements(By.tagName("td"));
//				listaBtn = objTr.findElements(By.tagName("button"));
//				if(listaBtn.size() > 0) {
//					FuncionesBase.doubleClickObjetoElement(listaBtn.get(0), "Aportar");
//					//listaBtn.get(listaBtn.size() - 1).click();
//	
//				}else {
//					msg = "El boton 'Aportar' no existe en la cuenta '" + ctaReinversion + "'";
//					return msg;
//				}
//			}else {
//				msg = "El cliente no posee la cuenta '" + ctaReinversion + "' ";
//				return msg;
//			}
			
			FuncionesBase.esperarObjetoXpath("md-checkbox", "id", "check", 30);

			msg = FuncionesBase.clickObjetoXpath("Aceptar Terminos", "md-checkbox", "id", "check");
			if(!msg.equals("OK")) {
				return msg;
			}

			msg = FuncionesBase.setTextoObjeto("Clave de Internet", "name", "claveInternet", clave, false);
			if(!msg.equals("OK")) {
				return msg;
			}

			Thread.sleep(2000);
			FuncionesBase.scrollUp("50");
			
		}catch(Exception e) {
			msg = "Ha ocurrido un error al ingresar datos del aporte: " + e.getMessage();
		}
		return msg;

	}
	
	public static String ingresarDatosRescateEmp(String cuenta, String monto, String tipoRescate, String clave) {

		String msg = "OK";
		WebElement obj = null, objTr = null;
		List<WebElement> listaBtn;
		
		try {

			obj = FuncionesBase.retornaObjetoXpath("table", "sort-by", "fondosClienteOrden");
			List<WebElement> listaTr = obj.findElements(By.xpath("//tr[@class='tableContenido--cuentas info--detalle ng-scope']"));
			
			for (int i = 0; i < listaTr.size(); i++) {
				if(listaTr.get(i).getText().contains(cuenta)) {
					objTr = listaTr.get(i);
					break;
				}
			}
			
			if(objTr != null) {
				listaBtn = objTr.findElements(By.tagName("button"));
				
				if(listaBtn.size() == 2) {
					FuncionesBase.enfocaObjeto(listaBtn.get(0));
					listaBtn.get(0).click();
	
				}else {
					msg = "El boton 'Rescatar' no existe en la cuenta '" + cuenta + "'";
					return msg;
				}
			}else {
				msg = "El cliente no posee la cuenta '" + cuenta + "'";
				return msg;
			}
			
			FuncionesBase.esperarObjetoXpath("md-radio-button", "aria-label", "Total", 60);
			
			if(tipoRescate.replaceAll(" ", "").toUpperCase().equals("TOTAL")) {
				
				msg = FuncionesBase.clickObjetoXpath("Rescate Total", "md-radio-button", "aria-label", "Total");
				if(!msg.equals("OK")) {
					return msg;
				}
				
			}else {
			
				msg = FuncionesBase.setTextoObjeto("Monto", "name", "monto", monto, false);
				if(!msg.equals("OK")) {
					return msg;
				}

			}

			msg = FuncionesBase.clickObjetoXpath("Aceptar Terminos", "md-checkbox", "id", "check");
			if(!msg.equals("OK")) {
				return msg;
			}

			msg = FuncionesBase.clickObjetoXpath("Enviar Clave", "span", "text", "ENVIAR CLAVE");
			if(!msg.equals("OK")) {
				return msg;
			}
			
			FuncionesBase.esperarObjetoXpath("div", "class", "md-dialog-container ng-scope' and @tabindex='-1", 30);
			WebElement objD = driver.findElement(By.xpath("//div[@class='md-dialog-container ng-scope' and @tabindex='-1']"));
			
			Actions at = new Actions(driver);
			at.moveToElement(objD, 0, 100).click().build().perform();
			

			Thread.sleep(2000);
			FuncionesBase.scrollUp("50");
			
		}catch(Exception e) {
			msg = "Ha ocurrido un error al ingresar datos del rescate: " + e.getMessage();
		}
		return msg;

	}
	
	public static String ingresarDatosRescate(String cuenta, String monto, String tipoRescate, String clave) {

		String msg = "OK";
		WebElement obj = null, objTr = null;
		List<WebElement> listaBtn;
		
		try {

			obj = FuncionesBase.retornaObjetoXpath("table", "sort-by", "fondosClienteOrden");
			List<WebElement> listaTr = obj.findElements(By.xpath("//tr[@class='tableContenido--cuentas info--detalle ng-scope']"));
			
			for (int i = 0; i < listaTr.size(); i++) {
				if(listaTr.get(i).getText().contains(cuenta)) {
					objTr = listaTr.get(i);
					break;
				}
			}
			
			if(objTr != null) {
				listaBtn = objTr.findElements(By.tagName("button"));
				
				if(listaBtn.size() == 2) {
					FuncionesBase.enfocaObjeto(listaBtn.get(0));
					listaBtn.get(0).click();
	
				}else {
					msg = "El boton 'Rescatar' no existe en la cuenta '" + cuenta + "'";
					return msg;
				}
			}else {
				msg = "El cliente no posee la cuenta '" + cuenta + "'";
				return msg;
			}
			
			FuncionesBase.esperarObjetoXpath("md-radio-button", "aria-label", "Total", 60);
			
			if(tipoRescate.replaceAll(" ", "").toUpperCase().equals("TOTAL")) {
				
				msg = FuncionesBase.clickObjetoXpath("Rescate Total", "md-radio-button", "aria-label", "Total");
				if(!msg.equals("OK")) {
					return msg;
				}
				
			}else {
			
				msg = FuncionesBase.setTextoObjeto("Monto", "name", "monto", monto, false);
				if(!msg.equals("OK")) {
					return msg;
				}

			}

			msg = FuncionesBase.clickObjetoXpath("Aceptar Terminos", "md-checkbox", "id", "check");
			if(!msg.equals("OK")) {
				return msg;
			}

			msg = FuncionesBase.setTextoObjeto("Clave de Internet", "name", "claveInternet", clave, false);
			if(!msg.equals("OK")) {
				return msg;
			}

			Thread.sleep(2000);
			FuncionesBase.scrollUp("50");
			
		}catch(Exception e) {
			msg = "Ha ocurrido un error al ingresar datos del rescate: " + e.getMessage();
		}
		return msg;

	}
	
	public static String aportarCuentaExistente() {

		String msg = "OK";
		
		try {

		
			msg = FuncionesBase.clickObjetoXpath("Aportar", "span", "text", "Aportar");

			if (!FuncionesBase.esperarObjetoXpath("span", "text", "Ver comprobante", 30)) {
				msg = "El comprobante no se ha generado correctamente";
			} 

			FuncionesBase.waitForPageLoad();

		}catch(Exception e) {
			msg = "Ha ocurrido un error al intentar aportar: " + e.getMessage();
		}

		return msg;

	}
	
	public static String rescatarCuentaExistente() {

		String msg = "OK";
		
		try {

			msg = FuncionesBase.clickObjetoXpath("Rescatar", "span", "text", " RESCATAR ");
			
			if (!FuncionesBase.esperarObjetoXpath("span", "text", "Ver comprobante", 50)) {
				msg = "El comprobante no se ha generado correctamente";
			} 

			FuncionesBase.waitForPageLoad();

		}catch(Exception e) {
			msg = "Ha ocurrido un error al rescatar: " + e.getMessage();
		}

		return msg;

	}
	
	public static String reinvertirFondoMutuo() {

		String msg = "OK";
		WebElement obj = null;
		List<WebElement> listaObj;
		
		try {

			if(FuncionesBase.buscarObjeto("Boton Reinvertir", "id", "rescateFFMMConfirm")) {
				
				obj = FuncionesBase.retornaObjeto("Boton Reinvertir", "id", "rescateFFMMConfirm");
				listaObj = obj.findElements(By.xpath("//span[contains(text(), ' REINVERTIR ')]"));
				
				if(listaObj.size() > 0) {
					Actions act = new Actions(driver);
					FuncionesBase.enfocaObjeto(listaObj.get(0));
					act.doubleClick(listaObj.get(0)).build().perform();
				}else {
					msg = "El objeto 'Boton Reinvertir' no se ha encontrado";
				}
				
			}else {
				msg = "El objeto 'Boton Reinvertir' no se ha encontrado";
			}

			if (!FuncionesBase.esperarObjetoXpath("span", "text", "Ver comprobante", 30)) {
				msg = "El comprobante no se ha generado correctamente";
			} 

			FuncionesBase.waitForPageLoad();

		}catch(Exception e) {
			msg = "Ha ocurrido un error al rescatar: " + e.getMessage();
		}

		return msg;

	}
	
	public static String verComprobante() {

		String msg = "OK";
		WebElement cuenta;

		try {

			msg = FuncionesBase.doubleClickObjetoXpath("Ver Comprobante", "span", "text", "Ver comprobante");
			if(!msg.equals("OK")){
				return msg;
			}
			
			if (!FuncionesBase.esperarObjetoXpath("img", "alt", "bancmr",50)) {
				msg = "El comprobante no se ha desplegado correctamente";
			} 
			
			if(!FuncionesBase.buscarObjetoXpath("img", "alt", "bancmr")) {
				msg = "No se ha desplegado correctamente el comprobante de solicitud de aporte a fondo mutuo";
			}else {
				cuenta = driver.findElement(By.id("input_11"));
				ctaCreada = cuenta.getAttribute("value");
			}

		}catch(Exception e) {
			msg = "Ha ocurrido un error al intentar ver el comprobante: " + e.getMessage();
		}

		return msg;

	}

	public static String botonVolver() {

		String msg = "OK";

		try {

			msg = FuncionesBase.clickObjetoXpath("Boton Volver", "span", "text", "Volver");

		}catch(Exception e) {
			msg = "Ha ocurrido un error al presionar el boton volver: " + e.getMessage();
		}

		return msg;

	}

	public static String validarAporteFondo(String cuenta) {

		String msg = "OK";
		List<WebElement> listaObj;
		WebElement tabla;
		boolean flag = false;
		int indiceCuenta = 0;
		
		try {

			tabla = FuncionesBase.retornaObjetoXpath("div", "class", "tablaMaster");
			listaObj = tabla.findElements(By.xpath("//*[@class='tableContenido--cuentas info--detalle ng-scope']"));
			
			for (int i = 0; i < listaObj.size(); i++) {
				if(listaObj.get(i).getText().contains(cuenta)) {
					indiceCuenta = i;
					flag = true;
					break;
				}
			}
			
			if(!flag) {
				msg = "No se ha creado correctamente la cuenta";
			}else {
				FuncionesBase.enfocaObjeto(listaObj.get(indiceCuenta));
			}

			ctaCreada = "";

		}catch(Exception e) {
			msg = "Ha ocurrido un error al validar la creacion de la " + cuenta + " : " + e.getMessage();
		}
		
		return msg;
		
	}

	public static String validaCargaListaFondos() {
		
		String msg = "OK";
	
		try {
			
			if(!FuncionesBase.buscarObjetoXpath("h3", "text", "Mi cartera de Fondos Mutuos")) {
				msg = "La pantalla mi cartera de fondos mutuos no se ha desplegado correctamente";
			}
			
		}catch(Exception e) {
			msg = "Ha ocurrido un error al ingresar a la pantalla de mi cartera de fondos mutuos: " + e.getMessage();
		}
		
		return msg;
	}

}