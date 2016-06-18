package br.ufpb.dcx.bpmwiki.pagedocumentation;

public class Model {
	
	private String ID;
	private String type;
	private String documantation;

	public Model(String type, String documantation, String ID) {
		this.type = type;
		this.documantation = documantation;
		this.ID = ID;
	}

	public String getType() {
		return type;
	}

	public String getDocumantation() {
		return documantation;
	}
	
	public String getID() {
		return ID;
	}

}
