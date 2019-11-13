package test;

import org.junit.Test;

import cl.cmr.fwk.customer.BaseTest;
import cl.cmr.fwk.customer.FuncionesBase;
import cl.cmr.login.Login;

public class Falla_prueba extends BaseTest{
	
	@Test
	public void test()
	{
		try {
			
			estado = Login.OpenUrl("chrome", "web_banFalabella", "qa");
			if(!FuncionesBase.estadoPaso("Abrir URL Web BanFalabella", estado, false).equals("OK")) {
				this.exitTest();
			}
						  
		}catch(Exception e) {
			System.out.println("Error Test '" + nombreTest + "':" + e.getMessage());
			this.exitTest();
		}

	}

}
