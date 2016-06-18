package br.ufpb.dcx.bpmwiki.xmlparser;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import com.rodrigor.util.ResourceReader;

public class ParserTest {
		
	private static ResourceReader reader;
	private static File file;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		reader = new ResourceReader("/tests/br/ufpb/dcx/bpmwiki/xmlparser/resources");
	}

	@Test
	public void testFile() throws Exception {
		file = reader.readFile("CasoTesteSimples.xml");
		assertTrue(file.exists());
		assertTrue(file.isFile());
		
		Parser parser = Parser.getInstance(file.getPath());
		parser.getProject();
		assertEquals(parser.getElementsTest().size(), 5);
	}
}
