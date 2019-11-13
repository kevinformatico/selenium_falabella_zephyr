package cl.cmr.categorias;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cl.cmr.fwk.customer.BaseTest;
import cl.cmr.fwk.customer.FuncionesBase;

public class Categorias extends BaseTest{
	
	public static String seleccionaCategoria(String categoria) {

		String msg = "OK";

		try {
			
			Thread.sleep(2000);
			msg = FuncionesBase.clickObjeto("Click en menu", "id", "hamburgerMenu");
			if(!msg.equals("OK")) {
				return msg;
			}			
			Thread.sleep(3000);			
			msg = FuncionesBase.clickObjeto("click en categoria", "id", "item-4");
			if(!msg.equals("OK")) {
				return msg;
			}
			Thread.sleep(4000);
			WebElement link = driver.findElement(By.xpath("//*[@id=\"header\"]/nav/div[1]/div[1]/div[1]/div/section[1]/div/section[5]/div/div/ul[3]/div[1]/div[1]/a"));
			msg = FuncionesBase.clickObjetoElement("Menaje Cocina", link);
			if(!msg.equals("OK")) {
				return msg;
			}			
			

		}catch(Exception e) {
			msg = "Ha ocurrido un error al intentar seleccionar la categoria "+categoria+": " + e.getMessage();
		}

		return msg;
	}

	public static String seleccionarItem()
	{
		String msg = "OK";
		try {
			
			WebElement item = driver.findElement(By.xpath("//*[@id=\"testId-categoryBanner\"]/div/div/div/div/ul/li[2]/a"));
			msg = FuncionesBase.clickObjetoElement("clik en item", item);
			if(!msg.equals("OK")) {
				return msg;
			}
			
			
		} catch (Exception e) {
			
			msg = "Ha ocurrido un error al seleccionar un item: "+e.getMessage(); 
		}
		
		return msg;
	}
}
