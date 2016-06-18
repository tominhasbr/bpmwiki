package br.ufpb.dcx.bpmwiki.xmlparser.wikicomposite;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.ufpb.dcx.bpmwiki.wikiwriter.Writer;
import br.ufpb.dcx.bpmwiki.xmlparser.wikielement.WikiElement;

public class WikiElementComposite extends WikiElement {
	
	protected List<WikiElement> children;

	public WikiElementComposite(String id, String label, String idFather) {
		super(id, label);
		this.children = new ArrayList<WikiElement>();
	}
	
	public void addChild(WikiElement element){
		this.children.add(element);
	}
	
	public List<WikiElement> getEdgeEvents() {
		return this.children;
	}
	
	public void writeDirectory(File fileBase) {
		fileBase.mkdir();
	}
	
	public void writeIndex(String path) throws Exception{
		Writer.getInstance().writeIndex(this, path);
	}
	
	public void writeStartEvents(String path) throws Exception{
		List<WikiElement> startEvents = new LinkedList<WikiElement>();
		for(WikiElement w:this.children){
			try{
				if(w.getType() == "BPStartEvent")
					startEvents.add(w);
			}catch(Exception ex){
				continue;
			}
		}
		Writer.getInstance().writeStartEvents(this,startEvents, path);
	}
}
