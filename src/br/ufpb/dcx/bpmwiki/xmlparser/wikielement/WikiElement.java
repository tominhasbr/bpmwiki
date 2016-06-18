package br.ufpb.dcx.bpmwiki.xmlparser.wikielement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WikiElement {
	
	protected String id;
	protected String label;
	private List<WikiElement> orig;
	private List<WikiElement> dest;
	private boolean hasElementFather;
	private WikiElement elementFather;
	private static final String ERROR_MESSAGE1 = "Funcao invalida. Nao eh uma instancia de Page.";
	private static final String ERROR_MESSAGE2 = "Funcao invalida. Nao eh uma instancia de WikiElementComposite.";
	
	public WikiElement(String id, String label){
		this.id = id;
		this.label = label;
		this.orig = new ArrayList<WikiElement>();
		this.dest = new ArrayList<WikiElement>();
	}
	
	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}
	
	public String getPerformer() throws Exception{
		throw new Exception(ERROR_MESSAGE2);
	}
	
	public WikiElement getElementFather() {
		return elementFather;
	}

	public void setElementFather(WikiElement pageFather) {
		this.elementFather = pageFather;
		this.hasElementFather = true;
	}
	
	public boolean hasElementFather(){
		return this.hasElementFather;
	}
	
	public String getType() throws Exception{
		throw new Exception(ERROR_MESSAGE2);
	}

	public String getDoc() throws Exception{
		throw new Exception(ERROR_MESSAGE2);
	}
	
	public String getModel() throws Exception{
		throw new Exception("Nao eh uma instancia de SubProcess.");
	}
	
	public List<WikiElement> orig() {
		return orig;
	}

	public List<WikiElement> dest() {
		return dest;
	}

	public void addOrig(WikiElement element){
		this.orig.add(element);
	}
	
	public void addDest(WikiElement element){
		this.dest.add(element);
	}
	
	public void addChild(WikiElement element) throws Exception{
		throw new Exception(ERROR_MESSAGE1);
	}
	
	public List<WikiElement> getEdgeEvents() throws Exception{
		throw new Exception(ERROR_MESSAGE1);
	}
	
	public void writeDirectory(File fileBase) throws Exception{
		throw new Exception(ERROR_MESSAGE1);
	}
	
	public void writePage(String path) throws Exception{
		throw new Exception(ERROR_MESSAGE2);
	}
	
	public void writeIndex(String path) throws Exception{
		throw new Exception(ERROR_MESSAGE1);
	}
	
	public void writeStartEvents(String path) throws Exception{
		throw new Exception(ERROR_MESSAGE1);
	}
}
