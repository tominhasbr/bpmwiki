package br.ufpb.dcx.bpmwiki;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

import br.ufpb.dcx.bpmwiki.wikiwriter.WikiWriter;
import br.ufpb.dcx.bpmwiki.xmlparser.Parser;

import com.rodrigor.util.ResourceReader;

public class Wiki {
	
	private static Properties config;
	
	static{
		config = new Properties();
		URL url = Wiki.class.getResource(".");
		File configFile = new File(url.getPath()+"/"+"bpmwiki.properties");
		try {
			config.load(new FileInputStream(configFile));
		} catch (Exception e) {
			throw new RuntimeException("Problemas na leitura do bpmwiki.properties: "+configFile+"!!!");
		}
	}
		
	private static ResourceReader reader = new ResourceReader("/tests/br/ufpb/dcx/bpmwiki/xmlparser/resources");

	public static void main(String[] args) throws Exception {	
		File file = reader.readFile("OGE_1.xml");
		WikiWriter.writeProject(Parser.getInstance(file.getPath()).getProject(), config.getProperty("pagesDir"));
	}
	
	public static Properties getConfigTest(){
		return config;
	}
	
}
