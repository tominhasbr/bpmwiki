package br.ufpb.dcx.bpmwiki.xmlparser.flow;

public class Flow {
	
	private String from;
	private String to;

	public Flow(String from, String to) {
		this.from = from.toLowerCase();
		this.to = to.toLowerCase();
	}

	public String from() {
		return from;
	}

	public String to() {
		return to;
	}
}
