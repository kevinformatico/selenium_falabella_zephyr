package cl.cmr.fwk.manager;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesManager {

	private static PropertiesManager instance;

	public static PropertiesManager getInstance() {
		
		if (instance == null) {
			instance = new PropertiesManager();
		}
		
		return instance;
		
	}

	public String getFrameworkProperty(String property, String file) throws IOException {
		
		Properties p = new Properties();
		p.load(new FileReader(Paths.get(System.getProperty("user.dir"), "src/main/resources/config/" + file + ".properties").toString()));
		return p.getProperty(property);
		
	}

}
