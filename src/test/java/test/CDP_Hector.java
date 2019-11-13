package test;

import org.junit.Test;

import cl.cmr.categorias.Categorias;
import cl.cmr.fwk.customer.BaseTest;
import cl.cmr.fwk.customer.FuncionesBase;
import cl.cmr.login.Login;

public class CDP_Hector extends BaseTest{
	
	@Test
	public void test()
	{
		try {
			
			estado = Login.OpenUrl("chrome", "web_banFalabella", "qa");
			if(!FuncionesBase.estadoPaso("Abrir URL Web BanFalabella", estado, false).equals("OK")) {
				this.exitTest();
			}
			
			estado = Login.loginWebFalabella("castillorengo@gmail.com", "hicc2011");
			if(!FuncionesBase.estadoPaso("Ingresar al Login Web BanFalabella", estado, false).equals("OK")) {
				this.exitTest();
			}
			
			estado = Categorias.seleccionaCategoria("Decohogar");
			if(!FuncionesBase.estadoPaso("Seleccionar Categoria", estado, false).equals("OK")) {
				this.exitTest();
			}
			
			estado = Categorias.seleccionarItem();
			if(!FuncionesBase.estadoPaso("Seleccionar Item", estado, false).equals("OK")) {
				this.exitTest();
			}
						  
		}catch(Exception e) {
			System.out.println("Error Test '" + nombreTest + "':" + e.getMessage());
			this.exitTest();
		}

	}

}
