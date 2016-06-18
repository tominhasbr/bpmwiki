package com.rodrigor.util;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class ResourceReaderTest {

	@Test
	public void testReadFile() {
		ResourceReader reader = new ResourceReader();
		File f = reader.getBaseDir();
		assertTrue(f.exists());
		assertTrue(f.isDirectory());
	}
	
	@Test
	public void testWorkingDir(){
		ResourceReader reader = new ResourceReader("/tests");
		File f = reader.getWorkingDir();
		assertTrue(f.getPath().endsWith("tests"));
		
		reader.setWorkingDir("/tests/com/rodrigor/util/resources");
		assertTrue(reader.getWorkingDir().getPath().endsWith("/tests/com/rodrigor/util/resources"));
		
		File resource = reader.readFile("testResource.txt");
		assertTrue(resource.exists());
		assertTrue(resource.isFile());

		File resource2 = reader.readFile("naoexiste.txt");
		assertFalse(resource2.exists());
	}

}
