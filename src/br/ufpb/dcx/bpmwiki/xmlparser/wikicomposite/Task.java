package br.ufpb.dcx.bpmwiki.xmlparser.wikicomposite;

import java.util.LinkedList;
import java.util.List;

import br.ufpb.dcx.bpmwiki.wikipage.Page;
import br.ufpb.dcx.bpmwiki.xmlparser.wikielement.WikiElement;

@SuppressWarnings("serial")
public class Task extends Page {
	
	private List<WikiElement> edgeEvents;

	public Task(String id, String label, String type, String documentation, String performer) {
		super(id, label, type, documentation, performer);
		this.edgeEvents = new LinkedList<WikiElement>();
	}
	
	public void addChild(WikiElement element){
		this.edgeEvents.add(element);
	}
	
	public List<WikiElement> getEdgeEvents() {
		return this.edgeEvents;
	}
}
