package br.ufpb.dcx.bpmwiki.xmlparser.wikicomposite;

public class SubProcess extends WikiElementComposite {

	private String model;
	
	public SubProcess(String id, String label, String idFather, String model) {
		super(id, label, idFather);
		this.model = model;
	}
	
	public String getModel(){
		return this.model;
	}

}
