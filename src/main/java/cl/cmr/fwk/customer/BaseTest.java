package cl.cmr.fwk.customer;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class BaseTest {

	public static WebDriver driver;
	public static String estado, nombreTest;
	public static ExtentTest extent;
	public static ExtentReports extentReports;
	String partNombreTest[];
	public static String[][][] matrizEvi;
	public static String[][] datosClientes;
	public static int filaM;
	public static ArrayList<String> arrPasos = new ArrayList<String>(), listaPasos, listaResultado;
	public static ZipUtils appZip = null;
	private String proyecto, ciclo;
	public static boolean jira, datos;
	
	public BaseTest() {
		super();
	}

	@Before
	public void beforeClass() {

		try {

			proyecto = "MT";
			ciclo = "PruebaDomingo";
			jira = true;
			datos = false;
			filaM = 0;

			partNombreTest = (this.getClass().getName().substring(this.getClass().getPackage().getName().length() + 1, 
							  this.getClass().getName().length())).split("_");
			nombreTest = partNombreTest[0] + "-" + partNombreTest[1];
			FuncionesBase.beforeTest(proyecto, partNombreTest[0], nombreTest);
			
			System.out.println("Iniciando ejecución del test " + nombreTest + "....");
			
		}catch(Exception e) {
			System.out.println("Error BeforeTest Test '" + nombreTest + "': " + e.getMessage());
		}

	}

	@After
	public void afterClass(){

		try {

			FuncionesBase.afterTest(proyecto, ciclo);
			System.out.println("Test ejecutado correctamente " + nombreTest + "...\n\n");

		}catch(Exception e) {
			System.out.println("Error AfterTest Test '" + nombreTest + "': " + e.getMessage());
		}

	}

	public void exitTest() {
		Assert.fail("Expected exit");
	}

}
