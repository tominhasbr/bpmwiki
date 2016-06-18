package br.ufpb.dcx.bpmwiki.xmlparser;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.ufpb.dcx.bpmwiki.pagedocumentation.Model;
import br.ufpb.dcx.bpmwiki.wikipage.Page;
import br.ufpb.dcx.bpmwiki.xmlparser.flow.Flow;
import br.ufpb.dcx.bpmwiki.xmlparser.wikicomposite.Project;
import br.ufpb.dcx.bpmwiki.xmlparser.wikicomposite.SubProcess;
import br.ufpb.dcx.bpmwiki.xmlparser.wikicomposite.Task;
import br.ufpb.dcx.bpmwiki.xmlparser.wikicomposite.WikiElementComposite;
import br.ufpb.dcx.bpmwiki.xmlparser.wikielement.WikiElement;

public class Parser {
	
	private static Parser single;
	private DocumentBuilderFactory documentBuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document document;
	private WikiElement project;
	private Map<String, WikiElement> subProcesses;
	private Map<String, WikiElement> elements;
	private Map<String, Model> pageDocs;
	private List<Flow> flows;

	private Parser(String nameFile){
		
		try {
			this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
			this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
			this.document = documentBuilder.parse(nameFile);	
			this.project = new Project(this.loadRoot().getAttribute("Name"));
			this.subProcesses = new HashMap<String,WikiElement>();
			this.elements = new HashMap<String,WikiElement>();
			this.pageDocs = new HashMap<String, Model>();
			this.flows = new LinkedList<Flow>();
			
		} catch (ParserConfigurationException e) {
			System.out.println("Algum problema na leitura do XML.");
		} catch (IOException e) {
			System.out.println("Algum problema na leitura do XML.");
		} catch (SAXException e) {
			System.out.println("Algum problema na leitura do XML.");
		}
	}
	
	public static Parser getInstance(String nameFile){
		if(single == null)
			single = new Parser(nameFile);
		return single;
	}
	
	/**
	 * Carrega o documento XML a partir da raíz (tag Project)
	 * @return
	 */
	
	private Element loadRoot(){
		return document.getDocumentElement();
	}
	
	/**
	 * Recupera todos os elementos com a tag "BusinessProcessDiagram"
	 * e retorna uma lista de elementos (NodeList)
	 * @return
	 */
	
	private NodeList getElementsDiagrams(){
		Element diagramsTag = (Element) this.loadRoot().getElementsByTagName("Diagrams").item(0);
		return diagramsTag.getElementsByTagName("BusinessProcessDiagram");
	}
	
	/**
	 * Captura as tags de documentação dos elementos (Documentation_plain)
	 * e adiciona numa lista. Os objetos derivados de Page serão setados
	 * com essa documentação, caso haja relacao entre a tag Model dessa
	 * Page com o ID de algum Model presente na lista de documentções. 
	 * @param modelsChild
	 */
	
	private  void setPageDoc(NodeList modelsChild){		
		for(int i = 0;i < modelsChild.getLength();i++){
			try{
				Element n = (Element) modelsChild.item(i);
				String tag = n.getNodeName();
				Model m = null;

				if(tag == "FromSimpleRelationships"|tag == "ToSimpleRelationships"|tag == "MasterView"|tag == "ModelRelationshipContainer")
					continue;
				else{
					this.setPageDoc(n.getChildNodes());
					m = new Model(n.getNodeName(), n.getAttribute("Documentation_plain"), n.getAttribute("Id"));
				}
				this.addPageDoc(m);
			}catch(Exception ex){
				continue;
			}
		}
	}
	
	private void callCreateElements(Element e, WikiElement fatherElement, String namePerformer){
		for(int i = 0; i < e.getChildNodes().getLength();i++ ){
			try{
				this.createElements((Element) e.getChildNodes().item(i), fatherElement, namePerformer);
			}catch(Exception ex){
				continue;
			}
		}
	}
	
	private void createChildsOfElements(Element e, WikiElement fatherElement, String namePerformer) throws Exception{
		Element dec = ((Element)e.getElementsByTagName("DiagramElementChildren").item(0));
		if(dec != null)
			this.callCreateElements(dec, fatherElement, namePerformer);
		return;
	}
	
	/**
	 * Cria elementos de um determinado tipo (Tasks, SubProcess, WikiElement simples)
	 * e seta a documentação, caso os elementos serão derivados de Page.
	 * @param child
	 * @param fatherElement
	 * @param namePerformer
	 * @throws Exception
	 */
	
	private void createElements(Element child, WikiElement fatherElement, String namePerformer) throws Exception{		
		
		this.setPageDoc(this.loadRoot().getElementsByTagName("Models").item(0).getChildNodes());
		
		WikiElement element = null;
		String tag = child.getNodeName();
		Model m = this.pageDocs.get(child.getAttribute("Model"));
		
		if(tag == "BPTextAnnotation"|tag == "TextBox"|tag == "BPDataInput"|tag == "BPDataOutput"|tag == "BPDataObject")
			return;
				
		switch(tag){
			case "BPSubProcess":
				element = new SubProcess(child.getAttribute("Id").toLowerCase(),
				child.getAttribute("Name"),fatherElement.getId(), child.getAttribute("Model"));
				this.createChildsOfElements(child, element, namePerformer);
				break;
			case "BPTask":
				element = new Task(child.getAttribute("Id").toLowerCase(), child.getAttribute("Name"),
				m.getType(), m.getDocumantation(), namePerformer);
				this.createChildsOfElements(child, element, namePerformer);
				break;
			default:
				element = new Page(child.getAttribute("Id").toLowerCase(), child.getAttribute("Name"),
				m.getType(), m.getDocumantation(), namePerformer);
				if(fatherElement instanceof Task|fatherElement instanceof SubProcess)
					element.setElementFather(fatherElement);
				break;
		}
		fatherElement.addChild(element);
		this.addElement(element);	
	}
	
	/**
	 * Cria todos os elementos de ligação e guarda-os numa lista unica.
	 * @param fatherObject
	 * @param nameSonObject
	 * @throws Exception
	 */
	
	private void createFlows(Element fatherObject, String nameSonObject) throws Exception{
		int index = 0;
		NodeList sonsObjects = fatherObject.getElementsByTagName(nameSonObject);
		Element sons;
		
		while((sons = (Element) sonsObjects.item(index)) != null){
			Flow flow = new Flow(sons.getAttribute("From"),sons.getAttribute("To"));
			this.flows.add(flow);
			index++;
		}		
	}
	
	/**
	 * Cria todos os elementos a partir das iterações realizadas
	 * sobre a tag Diagrams. Esse método é responsável por chamar
	 * todos os outros.
	 * @throws Exception
	 */
	
	private void createDiagrams() throws Exception{
		NodeList diagrams = this.getElementsDiagrams();
		
		for(int i = 0; i < diagrams.getLength();i++){
			Element index = (Element) diagrams.item(i);
			WikiElement diagram = new WikiElementComposite(index.getAttribute("Id").toLowerCase(), 
			index.getAttribute("Name"), index.getAttribute("ParentModel"));
						
			if(index.hasAttribute("ParentModel")){
				this.subProcesses.put(index.getAttribute("ParentModel"), diagram);
			}
			else
				this.project.addChild(diagram);			
			this.createPools(index, diagram);
			Element connectorsTag = (Element) index.getElementsByTagName("Connectors").item(0);
			this.createFlows(connectorsTag,"BPSequenceFlow");
			this.createFlows(connectorsTag,"BPMessageFlow");
		}
		this.toLinkElements();
		this.setSubProcess(this.project.getEdgeEvents());
	}
	
	/**
	 * Cria os elementos de Pools em cada um dos elementos Diagrams,
	 * caso existam. Representa os setores nos modelos BPM.
	 * @param diagramElem
	 * @param diagram
	 * @throws Exception
	 */
	
	private void createPools(Element diagramElem, WikiElement diagram) throws Exception{
		
		NodeList bpdChildren = diagramElem.getElementsByTagName("Shapes").item(0).getChildNodes();
		for(int i = 0;i < bpdChildren.getLength();i++){
			try{
				Element index = (Element)bpdChildren.item(i);
				if(index.getNodeName() == "BPPool"){
					WikiElement pool = new WikiElementComposite(index.getAttribute("Id").toLowerCase(), index.getAttribute("Name"), 
					diagramElem.getAttribute("Id").toLowerCase());
					
					if(index.getElementsByTagName("DiagramElementChildren").item(0) == null){
						this.addElement(pool);
						diagram.addChild(pool);
						continue;
					}
					diagram.addChild(pool);
					this.setLanePerformer(index, pool);
				}
				else
					this.createElements(index, diagram, diagram.getLabel());
			}catch(Exception ex){
				continue;
			}
		}
	}
	
	/**
	 * Recupera e seta nos elementos as suas respectivas Lanes.
	 * As Lanes aqui, na representação BPM, são os atores 
	 * responsáveis por um processo.
	 * @param poolElem
	 * @param pool
	 * @throws Exception
	 */
	
	private void setLanePerformer(Element poolElem, WikiElement pool) throws Exception{
		NodeList lanes = ((Element) poolElem.getElementsByTagName("DiagramElementChildren").item(0)).getElementsByTagName("BPLane");
		
		if(lanes.getLength() == 0){
			Element e = (Element) poolElem.getElementsByTagName("DiagramElementChildren").item(0);
			this.callCreateElements(e, pool, pool.getLabel());
		}
		else{
			for(int j = 0; j < lanes.getLength();j++){
				Element index = (Element) lanes.item(j);
				Element e = (Element) index.getElementsByTagName("DiagramElementChildren").item(0);
				this.callCreateElements(e, pool, index.getAttribute("Name"));
			}
		}
	}
	
	/**
	 * Seta os objetos SubProcess nos seus respectivos parentes.
	 * Os parentes podem ser um Pool, ou o Project, desde que
	 * esses subprocessos tenham a referência da tag Model.
	 * @param collection
	 */
	
	private void setSubProcess(Collection<WikiElement> collection) {		
		for(WikiElement w:collection){
			try {
				if(this.subProcesses.containsKey(w.getModel())){
					w.addChild(this.subProcesses.get(w.getModel()));}
				this.setSubProcess(w.getEdgeEvents());
			} catch (Exception ex1) {
				try {
					this.setSubProcess(w.getEdgeEvents());
				} catch (Exception ex2) {
					continue;
				}
			}		
		}
	}
	
	/**
	 * Liga os elementos presentes na lista de Flows.
	 * @throws Exception
	 */

	private void toLinkElements() throws Exception{	
			
		for(Flow flow:this.flows){
			WikiElement from = this.elements.get(flow.from());
			WikiElement to = this.elements.get(flow.to());
						
			if(from == null | to == null)
				throw new RuntimeException(flow.from());
			to.addOrig(from);
			from.addDest(to);
		}
	}
	
	private void addElement(WikiElement p){
		this.elements.put(p.getId(), p);
	}
	
	private void addPageDoc(Model m){
		this.pageDocs.put(m.getID(), m);
	}
	
	/**
	 * Captura o projeto com todos os objetos instanciados
	 * @return
	 * @throws Exception
	 */
	
	public WikiElement getProject() throws Exception{
		this.createDiagrams();
		return this.project;
	}
	
	public Map<String,WikiElement> getElementsTest(){
		return this.elements;
	}
}
