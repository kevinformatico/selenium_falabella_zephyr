package cl.cmr.fwk.manager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import cl.cmr.fwk.customer.BaseTest;
import cl.cmr.fwk.customer.FuncionesBase;
import cl.cmr.fwk.customer.FuncionesGenericas;

public class HtmlReportManager extends BaseTest {

	private static HtmlReportManager instance;
	private static ExtentReports extentReports;
	static Calendar calendario = Calendar.getInstance();
	public static Path folderPath;
	
	public static HtmlReportManager getInstance() throws Exception {
		
		if (instance == null) {

			instance = new HtmlReportManager();

			String hh = FuncionesGenericas.retornaHora().replaceAll(":", "");
			String pathEvi = FuncionesGenericas.retornaFechaYYYYMMDD().replace("/", "-") + "\\" + hh + "\\";
			folderPath = Paths.get(PropertiesManager.getInstance().getFrameworkProperty("report_folder_path", "framework"), pathEvi);
			Files.createDirectories(folderPath);
		
			Path reportPath = Paths.get(folderPath.toString(), "Evidencia.html");

			ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPath.toString());
			htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
			htmlReporter.config().setChartVisibilityOnOpen(false);
			htmlReporter.config().setTheme(Theme.STANDARD);
			htmlReporter.config().setDocumentTitle(PropertiesManager.getInstance().getFrameworkProperty("report_document_title", "framework"));
			htmlReporter.config().setEncoding("utf-8");
			htmlReporter.config().setReportName(PropertiesManager.getInstance().getFrameworkProperty("report_name", "framework"));

			HtmlReportManager.extentReports = new ExtentReports();
			
			instance.setHierarchy();
			HtmlReportManager.extentReports.attachReporter(htmlReporter);

		}

		return instance;
		
	}

	public ExtentReports getHtmlReport() {
		return HtmlReportManager.extentReports;
	}

	public ExtentTest createTest(String name, String description) {
		return HtmlReportManager.extentReports.createTest(name, description);
	}

	public ExtentTest createChildTest(ExtentTest extentTest, String name) {
		return extentTest.createNode(name);
	}

	public ExtentTest createFeature(String name, String description) {
		return HtmlReportManager.extentReports.createTest(Feature.class, name, description);
	}

	public ExtentTest createScenario(ExtentTest extentTest, String name) {
		return extentTest.createNode(Scenario.class, name);
	}


	public void addCategory(ExtentTest extentTest, String category) {
		String[] categories = category.split(",");
		extentTest.assignCategory(categories);
	}

	public void addAuthor(ExtentTest extentTest, String author) {
		String[] authors = author.split(",");
		extentTest.assignAuthor(authors);
	}

	public void addSystemInfo(ExtentTest extentTest, String info) {
		String[] infos = info.split(",");
		extentTest.assignCategory(infos);
	}

	public void setSystemInfo(ExtentReports extentReports, String info, String value) {
		extentReports.setSystemInfo(info, value);
	}

	public void setInfoBrowser(ExtentReports extentReports, String navegador, String version) {
		extentReports.setSystemInfo("Navegador", navegador);
		extentReports.setSystemInfo("Version", version);
	}

	public void setInfoTest(ExtentReports extentReports, String ambiente, String url) {
		extentReports.setSystemInfo("Ambiente", ambiente);
		extentReports.setSystemInfo("URl", url);
	}

	public void addMessage(WebDriver driver, ExtentTest extentTest, Status status, String message, boolean capture, boolean arrScreen)
			throws Exception {

		try {

			if(!(driver == null)) {
				FuncionesBase.waitForPageLoad();
			}

			if (capture) {
				extentTest.log(status, message, this.addScreenCapture(driver, arrScreen));
			} else {
				extentTest.log(status, message);
			}

		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private void setHierarchy() {
		
		List<Status> statusHierarchy = Arrays.asList(
				Status.FATAL,
				Status.FAIL,
				Status.ERROR,
				Status.SKIP,
				Status.PASS,
				Status.WARNING,
				Status.INFO,
				Status.DEBUG
				);
		HtmlReportManager.extentReports.config().statusConfigurator().setStatusHierarchy(statusHierarchy);
		
	}

	private MediaEntityModelProvider addScreenCapture(WebDriver driver, boolean flag) throws Exception {

		Random random = new Random();
		String imageName = String.valueOf(random.nextInt(10000));

		Path imagePath = Paths.get(imageName + ".png");
		Path reportPath = Paths.get(HtmlReportManager.folderPath.toString(), imagePath.toString());
		
		String screenFolder = HtmlReportManager.folderPath.toString().substring(HtmlReportManager.folderPath.toString().length() - 6,
				HtmlReportManager.folderPath.toString().length());
		
		screenFolder = "..\\" + screenFolder + "\\";
		
		File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screen, new File(reportPath.toString()));
		
		if(flag) {
			matrizEvi[BaseTest.filaM][2][0] = matrizEvi[BaseTest.filaM][2][0] + ";" + reportPath.toString();
		}else {
			matrizEvi[BaseTest.filaM][2][0] = reportPath.toString();	
		}
		
		return MediaEntityBuilder.createScreenCaptureFromPath(screenFolder + imagePath.toString()).build();

	}
	
	public static void saveHtmlReport() {
		try {
			
			extentReports.flush(); 
			instance = null;
			
		}catch(Exception e) {
			System.out.println("Ha ocurrido un error al guardar el reporte: " + e.getMessage());
		}
	}

}