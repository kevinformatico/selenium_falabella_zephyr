package cl.cmr.fwk.customer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.Status;
//import cl.cmr.datos.one.ONE;
//import cl.cmr.datos.wbch.FFMM;
import cl.cmr.fwk.manager.HtmlReportManager;
import cl.cmr.fwk.manager.PropertiesManager;

public class FuncionesBase extends BaseTest{

	public static int timeOut = 50, timeOutPage = 60;
	public static String idIssue;
	
	public static String estadoPaso(String nomPaso, String estado, boolean scroll) {

		String status = "OK";
		double largo = 0;
		int numScroll = 0, aumentoScroll = 500;

		try {
			
			if(!(driver == null)) {
				numScroll = aumentoScroll;
				largo = Math.rint(driver.manage().window().getSize().width / 500);
			}

			if(!estado.equals("OK")) {

				HtmlReportManager.getInstance().addMessage(driver, extent, Status.INFO, "Error en el paso '" + nomPaso + "' : [" + estado + "]", false, true);
				FuncionesBase.scrollUp("10000");

				for (int i = 0; i < largo; i++) {
					try {
						HtmlReportManager.getInstance().addMessage(driver, extent, Status.FAIL, "", true, true);
						FuncionesBase.scrollDown(Integer.toString(numScroll));
						numScroll = numScroll + aumentoScroll;
					}catch(Exception e) {
						
					}
				}
				
				HtmlReportManager.getInstance().addMessage(driver, extent, Status.FAIL, "", true, true);
				status = estado;

			}else{

				HtmlReportManager.getInstance().addMessage(driver, extent, Status.INFO, nomPaso, false, false);

				if(scroll) {
					
					FuncionesBase.scrollUp("10000");
					
					for (int i = 0; i < largo; i++) {
						try {
							HtmlReportManager.getInstance().addMessage(driver, extent, Status.PASS, "", true, true);
							FuncionesBase.scrollDown(Integer.toString(numScroll));
							numScroll = numScroll + aumentoScroll;
						}catch(Exception e) {
							
						}
					}
					
					HtmlReportManager.getInstance().addMessage(driver, extent, Status.PASS, "", true, true);
					
				}else {
					HtmlReportManager.getInstance().addMessage(driver, extent, Status.PASS, "", true, false);
				}

			}

		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al actualizar pasos " + e.getMessage());
		}
		
		if(BaseTest.jira) {
			matrizEvi[BaseTest.filaM][0][0] = nomPaso;
			matrizEvi[BaseTest.filaM][1][0] = estado;
		}
		BaseTest.filaM++;

		return status;

	}
	
	public static String clickObjeto(String nombreObj, String buscarPor, String valorPropiedad) {

		String msg = "OK";
		WebElement obj = null;
		
		try {

			switch (buscarPor) {

			case "id":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.id(valorPropiedad)));
				break;
			case "name":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.name(valorPropiedad)));
				break;
			case "class":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.className(valorPropiedad)));
				break;
			case "link":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(valorPropiedad)));
				break;
			case "tag":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.tagName(valorPropiedad)));
				break;
			case "linkRE":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(valorPropiedad)));
				break;
			case "css":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(valorPropiedad)));
				break;
			default:
				break;

			}

			if(obj.isDisplayed()) {

				FuncionesBase.enfocaObjeto(obj);
				obj.click();
				
			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				
				if(obj.isDisplayed()) {
					obj.click();
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado ";
				}
				
			}

			waitForPageLoad();
			
		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;

	}
	
	public static String clickObjetoXpathRE(String nombreObj, String nombreClass, String propiedad, String valorPropiedad) {

		String msg = "OK";
		WebElement obj = null;
		
		try {

			System.out.println("//" + nombreClass + "[translate(@" + propiedad + ", 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz','" + valorPropiedad + "')]");
			obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//" + nombreClass + "[translate(@" + "aria-label" + ", 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='" + valorPropiedad + "')]")));
			
			if(obj.isDisplayed()) {

				FuncionesBase.enfocaObjeto(obj);
				obj.click();
				
			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				if(obj.isDisplayed()) {
					obj.click();
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				}
				
			}

			waitForPageLoad();
			
		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado: " + e.getMessage();
		}

		return msg;

	}
	
	public static String clickObjetoLista(String nombreObj, String nombreClass, String propiedad, String valorPropiedad) {
	
		String msg = "OK";
		List<WebElement> listObj = null;
		WebElement obj = null;
		
		valorPropiedad = valorPropiedad.replace("-", "");
		
		try {
			
			listObj = (List<WebElement>) (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName(nombreClass)));
			
			for (int i = 0; i < listObj.size(); i++) {
				if(listObj.get(i).getText().toLowerCase().contains(valorPropiedad.toLowerCase())){
					obj = listObj.get(i);
					break;
				}
			}
			
			if(obj.isDisplayed()) {

				FuncionesBase.enfocaObjeto(obj);
				obj.click();
				
			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				if(obj.isDisplayed()) {
					obj.click();
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				}
				
			}

			waitForPageLoad();
		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado: " + e.getMessage();
		}
		
		return msg;
	}
	
	public static String clickObjetoXpath(String nombreObj, String nombreClass, String propiedad, String valorPropiedad) {

		String msg = "OK";
		WebElement obj = null;
		
		try {

			if(propiedad.equals("text")) {
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[contains(" + propiedad + "(),'" + valorPropiedad + "')]")));
			}else {
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[@" + propiedad + "='" + valorPropiedad + "']")));
			}

			if(obj.isDisplayed()) {

				FuncionesBase.enfocaObjeto(obj);
				obj.click();
				
			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				if(obj.isDisplayed()) {
					obj.click();
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				}
				
			}

			waitForPageLoad();
			
		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado: " + e.getMessage();
		}

		return msg;

	}
	
	public static String clickObjetoElement(String nombreObj, WebElement obj) {

		String msg = "OK";
		
		try {

			if(obj.isDisplayed()) {

//				FuncionesBase.enfocaObjeto(obj);
				obj.click();
				
			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				if(obj.isDisplayed()) {
					obj.click();
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				}
				
			}

			waitForPageLoad();
			
		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;

	}
	
	public static String doubleClickObjeto(String nombreObj, String buscarPor, String valorPropiedad) {

		String msg = "OK";
		WebElement obj = null;
		Actions action;

		try {

			switch (buscarPor) {

			case "id":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.id(valorPropiedad)));
				break;
			case "name":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.name(valorPropiedad)));
				break;
			case "class":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.className(valorPropiedad)));
				break;
			case "link":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(valorPropiedad)));
				break;
			case "tag":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.tagName(valorPropiedad)));
				break;
			case "linkRE":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(valorPropiedad)));
				break;
			case "css":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(valorPropiedad)));
				break;
			default:
				break;

			}

			if(obj.isDisplayed()) {
				
				FuncionesBase.enfocaObjeto(obj);
				action = new Actions(driver);
				action.doubleClick(obj).build().perform();
				
			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				if(obj.isDisplayed()) {
					action = new Actions(driver);
					action.doubleClick(obj).build().perform();
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				}
				
			}
			
			waitForPageLoad();

		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;

	}
	
	public static String doubleClickObjetoElement(WebElement obj, String nombreObj) {

		String msg = "OK";
		Actions action;

		try {

			if(obj.isDisplayed()) {
				
				FuncionesBase.enfocaObjeto(obj);
				action = new Actions(driver);
				action.doubleClick(obj).build().perform();
				
			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				if(obj.isDisplayed()) {
					action = new Actions(driver);
					action.doubleClick(obj).build().perform();
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				}
				
			}
			
			waitForPageLoad();

		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;

	}
	
	public static String doubleClickObjetoXpath(String nombreObj, String nombreClass, String propiedad, String valorPropiedad) {

		String msg = "OK";
		WebElement obj = null;
		Actions action;

		try {

			if(propiedad.equals("text")) {
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[contains(" + propiedad + "(),'" + valorPropiedad + "')]")));
			}else {
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[@" + propiedad + "='" + valorPropiedad + "']")));
			}

			if(obj.isDisplayed()) {
				
				FuncionesBase.enfocaObjeto(obj);
				action = new Actions(driver);
				action.moveToElement(obj).doubleClick(obj).build().perform();
				
			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				if(obj.isDisplayed()) {
					action = new Actions(driver);
					action.doubleClick(obj).build().perform();
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado ";
				}
				
			}
			
			waitForPageLoad();

		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;

	}
	
	public static String setTextoObjeto(String nombreObj, String buscarPor, String valorPropiedad, String texto, boolean limpiar) {

		String msg = "OK";
		WebElement obj = null;

		try {

			switch (buscarPor) {

			case "id":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.id(valorPropiedad)));
				break;
			case "name":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.name(valorPropiedad)));
				break;
			case "class":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.className(valorPropiedad)));
				break;
			case "tag":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.tagName(valorPropiedad)));
				break;
			case "css":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(valorPropiedad)));
				break;
			default:
				break;
			}

			if(obj.isDisplayed()) {
				
				FuncionesBase.enfocaObjeto(obj);
				
				if(limpiar) {obj.clear();}
				obj.sendKeys(texto);

			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				
				if(obj.isDisplayed()) {
					if(limpiar) {obj.clear();}
					obj.sendKeys(texto);
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				}
				
			}

		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;

	}
	
	public static String setTextoObjetoXpath(String nombreObj, String nombreClass, String propiedad, String valorPropiedad,String texto, boolean limpiar) {

		String msg = "OK";
		WebElement obj = null;

		try {

			obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[@" + propiedad + "='" + valorPropiedad + "']")));

			if(obj.isDisplayed()) {

				FuncionesBase.enfocaObjeto(obj);
				
				if(limpiar) {obj.clear();}
				obj.sendKeys(texto);

			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				
				if(obj.isDisplayed()) {
					if(limpiar) {obj.clear();}
					obj.sendKeys(texto);
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				}
				
			}

		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;

	}
	
	public static String setTextoObjetoElement(String nombreObj, WebElement obj, String texto, boolean limpiar) {

		String msg = "OK";

		try {

			if(obj.isDisplayed()) {
				
				FuncionesBase.enfocaObjeto(obj);
				
				if(limpiar) {obj.clear();}
				obj.sendKeys(texto);

			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				
				if(obj.isDisplayed()) {
					if(limpiar) {obj.clear();}
					obj.sendKeys(texto);
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				}
				
			}

		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;

	}
	
	public static String seleccionarCombobox(String nombreObj, String buscarPor, String valorPropiedad, String valor) {

		String msg = "OK", valorLista;
		Select obj = null;
		WebElement obj2 = null;
		boolean flag = false;
		List<WebElement> listaValores = null;

		try {

			switch (buscarPor) {

			case "id":
				obj = new Select((new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.id(valorPropiedad))));
				obj2 = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.id(valorPropiedad)));
				break;
			case "name":
				obj = new Select((new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.name(valorPropiedad))));
				break;
			case "class":
				obj = new Select((new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.className(valorPropiedad))));
				break;
			case "tag":
				obj = new Select((new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.tagName(valorPropiedad))));
				break;
			case "css":
				obj = new Select((new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(valorPropiedad))));
				break;
			default:
				break;

			}

			listaValores = obj2.findElements(By.tagName("option"));

			if(listaValores.size() > 0) {

				for (int i = 0; i < listaValores.size(); i++) {

					valorLista = listaValores.get(i).getText();

					if(valorLista.equals(valor)) {

						FuncionesBase.enfocaObjeto((WebElement) obj);
						((Select) obj2).selectByValue(valorLista);
						flag = true;

					}
				}

				if(!flag) {
					msg = "El objeto '" + nombreObj + "' no contiene el valor: '" + valor + "'";
				}

			}else {
				msg = "El objeto '" + nombreObj + "' no contiene opciones de selección";
			}

		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado ";
		}

		return msg;

	}
	
	public static int retornaFilaContenidoTabla(WebElement tabla, String propTr, String valorProp, String valorBuscado) {
	
		int index = 0;
		List<WebElement> listaTr;
		boolean flag = false;
		
		try {
			
			listaTr = tabla.findElements(By.xpath("//tr[@" + propTr +"='" + valorProp + "']"));
			
			for (int i = 0; i < listaTr.size(); i++) {
				if(listaTr.get(i).getText().contains(valorBuscado)) {
					index = i;
					flag = true;
					break;
				}
			}
	
			if(!flag) {
				index = - 1;
			}
			
		}catch(Exception e) {
			index = -1;
		}
		
		return index;
	}
		
	public static boolean buscarObjeto(String nombreObj, String buscarPor, String valorPropiedad) {

		boolean msg = false;
		WebElement obj = null;

		waitForPageLoad();

		try {

			switch (buscarPor) {

			case "id":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.id(valorPropiedad)));
				break;
			case "name":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.name(valorPropiedad)));
				break;
			case "class":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.className(valorPropiedad)));
				break;
			case "tag":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.tagName(valorPropiedad)));
				break;
			case "css":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(valorPropiedad)));
				break;
			default:
				break;
			}

			if(obj.isDisplayed()) {
				msg = true;
			}

		}catch(Exception e) {
			msg = false;
		}

		return msg;

	}

	public static boolean buscarObjetoXpath(String nombreClass, String propiedad, String valorPropiedad) {

		boolean msg = false;
		WebElement obj = null;

		waitForPageLoad();

		try {

			if(propiedad.equals("text")) {
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[contains(" + propiedad + "(),'" + valorPropiedad + "')]")));
			}else {
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[@" + propiedad + "='" + valorPropiedad + "']")));
			}
			
			if(obj.isDisplayed()) {
				msg = true;
			}

		}catch(Exception e) {
			msg = false;
		}

		return msg;

	}

	public static boolean buscarObjetoXpathTime(String nombreClass, String propiedad, String valorPropiedad, int tiempo) {

		boolean msg = false;
		WebElement obj = null;

		waitForPageLoad();

		try {

			if(propiedad.equals("text")) {
				obj = (new WebDriverWait(driver, tiempo)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[contains(" + propiedad + "(),'" + valorPropiedad + "')]")));
			}else {
				obj = (new WebDriverWait(driver, tiempo)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[@" + propiedad + "='" + valorPropiedad + "']")));
			}
			
			if(obj.isDisplayed()) {
				msg = true;
			}

		}catch(Exception e) {
			msg = false;
		}

		return msg;

	}
	
	public static boolean buscarObjetoElement(String nombreObj, WebElement obj) {

		boolean msg = false;
		waitForPageLoad();

		try {

			if(obj.isDisplayed()) {
				msg = true;
			}

		}catch(Exception e) {
			msg = false;
		}

		return msg;

	}
	
	public static boolean esperarObjetoXpath(String nombreClass, String propiedad, String valorPropiedad, int time) {

		boolean msg = false;
		WebElement obj = null;
		
		try {

			for (int j = 0; j < time; j++) {
				Thread.sleep(1000);
				try {
					
					if(propiedad.equals("text")) {
						obj = driver.findElement(By.xpath("//" + nombreClass + "[contains(" + propiedad + "(),'" + valorPropiedad + "')]"));
					}else {
						obj = driver.findElement(By.xpath("//" + nombreClass + "[@" + propiedad + "='" + valorPropiedad + "']"));
					}
					
					if(obj.isDisplayed()) {
						msg = true;
						break;
					}
				
				}catch(Exception e) {
					
				}
			}
			
		}catch(Exception e) {
			msg = false;
		}

		return msg;

	}
	
	public static String hoverObjetoXpath(String nombreObj, String nombreClass, String propiedad, String valorPropiedad) {

		String msg = "OK";
		WebElement obj = null;
		
		try {

			if(propiedad.equals("text")) {
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[contains(" + propiedad + "(),'" + valorPropiedad + "')]")));
			}else {
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[@" + propiedad + "='" + valorPropiedad + "']")));
			}

			if(obj.isDisplayed()) {

				FuncionesBase.enfocaObjeto(obj);
				Actions action = new Actions(driver); 
				action.moveToElement(obj).build().perform();
				
			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				if(obj.isDisplayed()) {
					obj.click();
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				}
				
			}

			waitForPageLoad();
			
		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado: " + e.getMessage();
		}

		return msg;

	}

	public static String hoverObjeto(String nombreObj, String buscarPor, String valorPropiedad) {

		String msg = "OK";
		WebElement obj = null;
		
		try {

			switch (buscarPor) {

			case "id":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.id(valorPropiedad)));
				break;
			case "name":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.name(valorPropiedad)));
				break;
			case "class":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.className(valorPropiedad)));
				break;
			case "link":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(valorPropiedad)));
				break;
			case "tag":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.tagName(valorPropiedad)));
				break;
			case "linkRE":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(valorPropiedad)));
				break;
			case "css":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(valorPropiedad)));
				break;
			default:
				break;

			}

			if(obj.isDisplayed()) {
				
				FuncionesBase.enfocaObjeto(obj);
				Actions action = new Actions(driver); 
				action.moveToElement(obj).build().perform();
				
			} else {
				
				FuncionesBase.enfocaObjeto(obj);
				
				if(obj.isDisplayed()) {

					Actions action = new Actions(driver); 
					action.moveToElement(obj).build().perform();
					
				}else {
					msg = "El objeto '" + nombreObj + "' no se ha encontrado ";
				}
				
			}

			waitForPageLoad();
			
		}catch(Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;

	}
	
	public static boolean esperarObjetoElement(WebElement obj, int time) {

		boolean msg = false;

		try {

			for (int j = 0; j < time; j++) {
				
				Thread.sleep(1000);
				
				try {
					
					if(obj.isDisplayed()) {
						msg = true;
						break;
					}
				
				}catch(Exception e) {
					
				}
			}
			
		}catch(Exception e) {
			msg = false;
		}

		return msg;

	}
	
	public static WebElement retornaObjeto(String nombreObj, String buscarPor, String valorPropiedad) {

		WebElement obj = null;

		try {

			switch (buscarPor) {

			case "id":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.id(valorPropiedad)));
				break;
			case "name":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.name(valorPropiedad)));
				break;
			case "class":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.className(valorPropiedad)));
				break;
			case "link":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(valorPropiedad)));
				break;
			case "tag":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.tagName(valorPropiedad)));
				break;
			case "linkRE":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(valorPropiedad)));
				break;
			case "css":
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(valorPropiedad)));
				break;
			default:
				break;

			}

		}catch(Exception e) {
			System.out.println("El objeto '" + nombreObj + "' no se ha encontrado");
		}

		return obj;

	}

	public static WebElement retornaObjetoXpath(String nombreClass, String propiedad, String valorPropiedad) {

		WebElement obj = null;

		try {

			if(propiedad.equals("text")) {
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[contains(" + propiedad + "(),'" + valorPropiedad + "')]")));
			}else {
				obj = (new WebDriverWait(driver, timeOut)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + nombreClass + "[@" + propiedad + "='" + valorPropiedad + "']")));
			}

		}catch(Exception e) {
			obj = null;
		}

		return obj;

	}

	public static List<WebElement> retornaListaObjetoXpath(WebElement obj, String nombreClass, String propiedad, String valorPropiedad) {

		List<WebElement> listaObj = null;

		try {

			if(propiedad.equals("text")) {
				
				listaObj = obj.findElements(By.xpath("//" + nombreClass + "[contains(" + propiedad + "(),'" + valorPropiedad + "')]"));
			}else {
				listaObj = obj.findElements(By.xpath("//" + nombreClass + "[@" + propiedad + "='" + valorPropiedad + "']"));
			}

		}catch(Exception e) {
			listaObj = null;
		}

		return listaObj;

	}
	
	public static List<WebElement> retornaListaObjeto(WebElement obj, String tagName) {

		List<WebElement> listaObj = null;

		try {

				listaObj = obj.findElements(By.tagName(tagName));

		}catch(Exception e) {
			listaObj = null;
		}

		return listaObj;

	}
	
	public static String cambioDeVentana(int numeroVentanas) {

		String msg = "OK";
		String handle = "";

		try {

			Set<String> ventanas = driver.getWindowHandles();
			Iterator<String> it = ventanas.iterator();

			for (int i = 0; i < numeroVentanas; i++) {
				handle = it.next();
			}

			driver.switchTo().window(handle);
			driver.manage().window().setSize(new Dimension(1366, 768));
			driver.manage().window().maximize();
			
		}catch(Exception e) {
			msg = "Ha ocurrido un erro al cambiar de ventana";
		}

		return msg;

	}

	public static String esperarVentanas(int numeroVentanas) {

		String msg = "OK";
		int contador = 0;

		try {

			while(driver.getWindowHandles().size() != numeroVentanas) {

				Thread.sleep(1000);

				if(contador > 29) {

					numeroVentanas = driver.getWindowHandles().size();
					msg = "La cantidad de ventanas esperadas no se presentan correctamente";
					return msg;

				}

				contador++;
			}

			cambioDeVentana(numeroVentanas);

		}catch(Exception e) {
			msg = "La cantidad de ventanas esperadas no se presentan correctamente";
		}

		return msg;
	}

	public static void waitForPageLoad() {

		try {

			Wait<WebDriver> wait = new WebDriverWait(driver, timeOutPage);

			wait.until(new Function<WebDriver, Boolean>() {

				@Override
				public Boolean apply(WebDriver driver) {
					return String
							.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
							.equals("complete");
				}

			});

		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al esperar la carga del browser");
		}

	}

	public static String retornaDriver(String navegador) {

		String confDriver = null;

		switch (navegador.toLowerCase()) {

		case "ie":
			confDriver = "webdriver.ie.driver";
			break;

		case "firefox":
			confDriver = "webdriver.gecko.driver";
			break;

		case "chrome":
			confDriver = "webdriver.chrome.driver";
			break;

		default:
			break;
		}

		return confDriver;
	}

	public static String retornaRutaDriver(String navegador) {

		String path = null;

		switch (navegador.toLowerCase()) {

		case "ie":
			path = "iedriverserver.exe";
			break;

		case "firefox":
			path = "geckodriver.exe";
			break;

		case "chrome":
			path = "chromedriver.exe";
			break;

		default:
			break;
		}

		return path;

	}

	public static void setInfoBrowser(WebDriver driver) {

		String browserName, version;
		Capabilities cap;

		try {

			cap = ((RemoteWebDriver) driver).getCapabilities();
			browserName = cap.getBrowserName().toLowerCase();
			version = cap.getVersion().toUpperCase();

			HtmlReportManager.getInstance().setInfoBrowser(extentReports, browserName, version);

		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al obtener informacion del browser");
		}

	}

	public static void setInfoUrl(String ambiente) {

		try {
			
			String url = driver.getCurrentUrl();
			HtmlReportManager.getInstance().setInfoTest(extentReports, ambiente, url);
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al obtener informacion del browser");
		}

	}

	public static void scrollDown(String px) {

		JavascriptExecutor jse = (JavascriptExecutor) driver;

		String numScroll;

		if(px.replace(" ", "").equals("")) {
			numScroll = "1000";
		}else {
			numScroll = px;
		}

		jse.executeScript("window.scrollBy(0," + numScroll + ")", "");

	}

	public static void scrollUp(String px) {

		String numScroll;
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		
		if(px.replace(" ", "").equals("")) {
			numScroll = "1000";
		}else {
			numScroll = px;
		}

		jse.executeScript("window.scrollBy(0,-" + numScroll + ")", "");

	}

	public static void enfocaObjeto(WebElement obj) {
		
		try {
			
			int posObj = 0;
			String js;
			
			posObj = obj.getLocation().getY();
			js = String.format("window.scroll(0, %s)", posObj - 200);
			((JavascriptExecutor) driver).executeScript(js);
			
		}catch(Exception e) {
			System.out.println("Error al enfocar objeto: " + e.getMessage());
		}
		
	}
	
	public static void scrollInterno(WebElement obj) {
		try {
			
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", obj);
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al realizar scroll: " + e.getMessage());
		}
	}
	
	public static void actualizaResultadoTestJIRA(String nomTest, String idIssue, String proyecto, String nomCiclo) {
		
		try {
			
			ArrayList<String> listaPasos = new ArrayList<String>();
			String estadoPaso = null, estadoEjecucion = "1";
			
			String idProyecto = ClientJira.retornaIdProyectoJIRA(proyecto);
			String versionProyecto = ClientJira.retornaVersionIdJIRA(idProyecto);
			System.out.println("--idproyecto--"+ idProyecto + "--Vproyecto--" + versionProyecto);
			String idCiclo = ClientJira.retornaIdCicloJIRA(idProyecto, nomCiclo, versionProyecto);
			String idEjecucion = ClientJira.crearEjecucionJIRA(idCiclo, idIssue, idProyecto, versionProyecto);
			ClientJira.elminaEjecucionJIRA(idEjecucion);
			
			idEjecucion = ClientJira.crearEjecucionJIRA(idCiclo, idIssue, idProyecto, versionProyecto);
			listaPasos = ClientJira.retornaListaResultadosPasosJIRA(idEjecucion);

			for (int j = 0; j < BaseTest.matrizEvi.length; j++) {

				estadoPaso = FuncionesBase.retornaCodEstadoPaso(matrizEvi[j][1][0]);
				String screenShot = matrizEvi[j][2][0];
				String comentario = matrizEvi[j][1][0];
				
				if(comentario.equals("OK")) {
					comentario = "";
				}
				
				String idStepResult = ClientJira.actualizaEstadoPasoTestJIRA(listaPasos.get(j), estadoPaso, comentario);
				
				if(screenShot.contains(";")) {
					String[] arrScreen = screenShot.split(";");
					for (int i = 0; i < arrScreen.length; i++) {
						ClientJira.attachmenEjecucionJIRA(idStepResult, arrScreen[i], "stepresult");
					}
				}else {
					ClientJira.attachmenEjecucionJIRA(idStepResult, screenShot, "stepresult");
				}
				
				if(!estadoPaso.equals("1")) {
					estadoEjecucion = "2";
					break;
				}
				
			}

			ClientJira.updateExecution(idEjecucion, estadoEjecucion);
			ClientJira.attachmenEjecucionJIRA(idEjecucion, HtmlReportManager.folderPath.toString() + "\\EvidenciaTest.zip", "execution");
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al reportar estados de ejecución en JIRA:  " + e.getMessage());
		}
		
	}
	
	public static String retornaCodEstadoPaso(String estado) {
		
		String estadoPaso = "";
		
		switch (estado) {
		
			case "OK":
				estadoPaso = "1";		
				break;

		default:
			estadoPaso = "2";	
			break;
		}
		
		return estadoPaso;
	}
	
	public static String valorMatriz(String[][] matriz, String valorTag) {
		
		String value = null;
		String[] valueSplit;
		
		try {
			
			for (int i = 0; i < matriz.length; i++) {
				for (int j = 0; j < matriz[i].length; j++) {
					if(matriz[i][j].contains(valorTag)) {
						valueSplit = matriz[i][j].split(":");
						value = valueSplit[1];
						break;
					}
				}
				if(value!="") {
					break;
				}
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al retornar datos de la matriz: " + e.getMessage());
		}
	
		return value;
		
	}
	
	public static String[][] matrizCondiciones() {
		
		String[][] matriz = null;
		String condiciones;
		
		try {
			
			String[] nTest = nombreTest.split("-");
			
			condiciones = PropertiesManager.getInstance().getFrameworkProperty(nTest[0], "condiciones");
			
			if(condiciones != null) {
				String[] listaCondiciones = condiciones.split(";");
				matriz = new String[listaCondiciones.length][2];
				
				for (int i = 0; i < listaCondiciones.length; i++) {
					String[] parteCondicion = listaCondiciones[i].split(":");
					matriz[i][0] = parteCondicion[0];
					matriz[i][1] = parteCondicion[1];
				}
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al llenar matriz de datos: " + e.getMessage());
		}
		
		return matriz;
	}

	public static String unirQueryMatriz(String[][] matriz, int indexColumna) {
		
		String strQuery = "", separador = ",", value;
		
		try {
		
			for (int i = 0; i <= matriz.length - 1; i++) {
				if(i == matriz.length - 1) {
					separador = "";
				}
				
				value = matriz[i][indexColumna].replace(".", "").replace("-", "");
				value = value.substring(0, value.length() - 1);
				
				strQuery = strQuery + "'" + value + "'" + separador; 
				
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al unir query: " + e.getMessage());
		}
		
		return strQuery;
		
	}
	
	public static String unirQueryArrayList(ArrayList<String> lista) {
		
		String strQuery = "", separador = ",", value;
		
		try {
		
			for (int i = 0; i <= lista.size() - 1; i++) {
				if(i == lista.size() - 1) {
					separador = "";
				}
				
				value = lista.get(i).replace(".", "").replace("-", "");
				value = value.substring(0, value.length() - 1);
				strQuery = strQuery + "'" + value + "'" + separador;
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al unir query: " + e.getMessage());
		}
		System.out.println(strQuery);
		return strQuery;
		
	}

	public static int totalRS(ResultSet rs) {
		
		int countRS = 0;
		
		try {
			
			while (rs.next()) {
				countRS++;
			}
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al retornar dimesion del resultset: " + e.getMessage());
		}
		
		return countRS;
		
	}
	
	public static void setEvidencia(String nomTest) throws Exception {
		extentReports = HtmlReportManager.getInstance().getHtmlReport();
		extent = HtmlReportManager.getInstance().createTest(nomTest, "Test Automatizado");
		HtmlReportManager.getInstance().addAuthor(extent, System.getProperty("user.name"));
	}
	
	public static void setJIRA(String idTest, String keyProyecto) {
		idIssue = ClientJira.retornaIdIssue(keyProyecto, idTest);
		int size = ClientJira.retornaCantidadPasosTestJIRA(idIssue);
		if(size == 0) {
			matrizEvi = new String[30][3][1];
		}else {
			matrizEvi = new String[size][3][1];
		}
	}
	
	public static void beforeTest(String keyProyecto, String idTest, String nomTest) throws Exception {
		if(BaseTest.jira)
		{
			setJIRA(nomTest, keyProyecto);
		}
		setEvidencia(nomTest);
//		if(BaseTest.datos) {datosClientes = FuncionesBase.retornaClientes();}
	}
	
	public static void afterTest(String keyProyecto, String ciclo) {
		
		quitDriver();
		guardarEvidencia();
		
		if(BaseTest.jira) {
			actualizaResultadoTestJIRA(nombreTest, idIssue, keyProyecto, ciclo);
			closeClientJIRA();
		}
		
	}
	
	public static void quitDriver() {
		if(!(driver == null)) {
			driver.quit();
		}
	}
	
	public static void closeClientJIRA() {
		try {
			ClientJira.getInstance().close();
			ClientJira.client = null;
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al cerrar Client Jira: " + e.getMessage());
		}
	}
	
	public static void guardarEvidencia() {
		try {
			HtmlReportManager.saveHtmlReport(); 
			appZip = new ZipUtils();
		    appZip.generateFileList(HtmlReportManager.folderPath.toFile(), HtmlReportManager.folderPath.toString()); 
		    appZip.zipIt(HtmlReportManager.folderPath.toString() + "\\EvidenciaTest.zip", HtmlReportManager.folderPath.toString());
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al guardar la evidencia: " + e.getMessage());
		}
	}
}
