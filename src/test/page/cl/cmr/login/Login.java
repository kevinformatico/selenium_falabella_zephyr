package cl.cmr.login;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import cl.cmr.fwk.customer.BaseTest;
import cl.cmr.fwk.customer.FuncionesBase;
import cl.cmr.fwk.manager.PropertiesManager;

public class Login extends BaseTest{
	
	
	@SuppressWarnings("deprecation")
	public static String OpenUrl(String browser, String sistema, String ambiente)
	{
		String msg = "OK", url = null;
		try {
			
			System.setProperty(FuncionesBase.retornaDriver(browser), "src/test/resources/driver/" + FuncionesBase.retornaRutaDriver(browser));
			url = PropertiesManager.getInstance().getFrameworkProperty(sistema, "framework");
//			url = PropertiesManager.getInstance().getFrameworkProperty(sistema + "_" + ambiente, "framework");
			if (!url.equals("")) {
				
				if (browser.replaceAll(" ", "").toLowerCase().equals("ie")) {
					
					DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					driver = new InternetExplorerDriver(capabilities);
					
				}else if (browser.replaceAll(" ", "").toLowerCase().equals("chrome")) {
					
					DesiredCapabilities capabilities = DesiredCapabilities.chrome();
					ChromeOptions options = new ChromeOptions();
					
//					options.addArguments("test-type");
					options.addArguments("--start-maximized");
//					options.addArguments("--disable-web-security");
//					options.addArguments("--allow-running-insecure-content");
					capabilities.setCapability(ChromeOptions.CAPABILITY, options);
					
					driver = new ChromeDriver(capabilities);
//					driver = new RemoteWebDriver(new URL("http://localhost:5599/wd/hub"), capabilities);
					
					
				}else if (browser.replaceAll(" ", "").toLowerCase().equals("firefox")) {
					
					FirefoxOptions firefoxOptions = new FirefoxOptions();
					firefoxOptions.setCapability("mationette", true);
					
					driver = new FirefoxDriver(firefoxOptions);
					
				}
				
				driver.manage().window().setSize(new Dimension(1366, 768));
				driver.navigate().to(url);
				driver.manage().window().maximize();
				
//				if(driver.getTitle().contains("Esta página no se puede mostrar") || 
//				   driver.getTitle().contains("No se puede acceder a esta página") ) {
//				   msg = "La url '" + url + "' no se ha cargado correctamente";
//				}
				
			}else{
				msg = "La url '" + url + "' no se ha encontrado en el archivo framework.properties";
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			msg = "Ha ocurrido un error al abrir la url '" + url + "'";
		}
		return msg;
		
	}
	
	public static String loginWebFalabella(String user, String pass) {

		String msg = "OK";

		try {
			
			msg = FuncionesBase.clickObjeto("Link mi cuenta", "class", "re-design-cl__logged");
			if(!msg.equals("OK")) {
				return msg;
			}

			msg = FuncionesBase.setTextoObjeto("Usuario", "id", "emailAddress", user, false);
			if(!msg.equals("OK")) {
				return msg;
			}

			msg = FuncionesBase.setTextoObjeto("Password", "name", "password", pass, false);
			if(!msg.equals("OK")) {
				return msg;
			}
			Thread.sleep(3000);
			WebElement boton = driver.findElement(By.xpath("//*[@id=\"header-login-modal\"]/div[2]/div/div/div[2]/button[1]"));
			msg = FuncionesBase.clickObjetoElement("Botón Ingresar", boton);
			if(!msg.equals("OK")) {
				return msg;
			}
			

		}catch(Exception e) {
			msg = "Ha ocurrido un error al intentar realizar login en la Web Falabella: " + e.getMessage();
		}

		return msg;
	}
	
	
}
