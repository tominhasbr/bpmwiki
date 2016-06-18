package br.ufpb.dcx.bpmwiki.wikipage;

import java.io.Serializable;

import br.ufpb.dcx.bpmwiki.wikiwriter.Writer;
import br.ufpb.dcx.bpmwiki.xmlparser.wikielement.WikiElement;

@SuppressWarnings("serial")
public class Page extends WikiElement implements Serializable {
	
	private String peformer;
	private String type;
	private String documentation;
	
	public Page(String id, String label, String type, String documentation, String performer) {
		super(id, label);
		this.type = type;
		this.documentation = documentation;
		this.peformer = performer;
		
	}
	
	public String getDoc() {
		return documentation;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getPerformer(){
		return this.peformer;
	}

	public void writePage(String path) throws Exception{
		Writer.getInstance().write(this, path);
	}
}
