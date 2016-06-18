package br.ufpb.dcx.bpmwiki.wikiwriter;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;

import br.ufpb.dcx.bpmwiki.xmlparser.wikielement.WikiElement;

public class WikiWriter implements Serializable {

	private static final long serialVersionUID = 1L;

	public static void writeProject(WikiElement project, String path) throws Exception{
		String projectPath = path + project.getLabel() + "/";
		project.writeDirectory(new File(projectPath));
		project.writeIndex(projectPath);
		createDirectorysAndWritePages(project.getEdgeEvents(), projectPath);
	}
	
	private static void createDirectorysAndWritePages(Collection<WikiElement> elements, String path) throws Exception  {
		for(WikiElement wiki:elements){
			try{
				String elementPath = path + wiki.getLabel() + "/";
				wiki.writeDirectory(new File(elementPath));
				wiki.writeIndex(elementPath);
				wiki.writeStartEvents(elementPath);
				createDirectorysAndWritePages(wiki.getEdgeEvents(),elementPath);
			}catch(Exception ex){
				wiki.writePage(path);
			}
		}
	}
}
