package br.ufpb.dcx.bpmwiki.wikiwriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import br.ufpb.dcx.bpmwiki.wikipage.Page;
import br.ufpb.dcx.bpmwiki.xmlparser.wikielement.WikiElement;

import com.rodrigor.util.ResourceReader;

public class Writer {
	
	private static final ResourceReader reader = new ResourceReader("/tests/br/ufpb/dcx/bpmwiki/templates");
	private static Writer single;
	
	private Writer() { }
	
	public static Writer getInstance(){
		if(single == null)
			single = new Writer();
		return single;
	}
	
	public void write(WikiElement wiki, String path) throws Exception{
			
		BufferedReader br = new BufferedReader(new FileReader(reader.readFile("templateWiki.txt")));
		BufferedWriter bw = new BufferedWriter(new FileWriter(path+wiki.getId()+".txt"));
		
		String grandString;
		String nomeDaPagina = "$NomeDaPagina$";
		String origens = "$origens$", destinos = "$destinos$", eventos = "$eventos$";
		String performer = "$performer$", tipo = "$tipo$", descricao = "$descricao$";
		String id = "<idPagina>", nomePagina = "<NomePagina>";
		String subPages = "";

		while ((grandString = br.readLine()) != null) {
			if(!grandString.contains("$"))
				bw.write(grandString);
			
			StringBuffer buffer = new StringBuffer();
			
			if (grandString.contains(nomeDaPagina)){
				buffer.append(grandString.replace(nomeDaPagina, wiki.getLabel()));
				bw.write(buffer.toString()+"\n");
				continue;
			}
			if (grandString.contains(performer)){
				buffer.append(grandString.replace(performer, wiki.getPerformer()));
				bw.write(buffer.toString()+"\n");
				continue;
			}
			if(grandString.contains(tipo)){
				buffer.append(grandString.replace(tipo, wiki.getType()));
				bw.write(buffer.toString()+"\n");
				continue;
			}
			if(grandString.contains(descricao)){
				buffer.append(grandString.replace(descricao, wiki.getDoc()));
				bw.write(buffer.toString()+"\n");
				continue;
			}

			boolean estadoOrigemDestino = false;
			
			if (grandString.contains(origens)) {
				subPages = origens;
				estadoOrigemDestino = true;
			}
			if (grandString.contains(destinos)) {
				subPages = destinos;
				estadoOrigemDestino = true;
			}
			if (estadoOrigemDestino) {
				String templateLink = br.readLine();
				List<WikiElement> links;
				if (subPages == origens)
					links = wiki.orig();
				else
					links = wiki.dest();
				for (WikiElement plink : links) {
					if(plink.hasElementFather()){
						String newLink = templateLink.replace(id, plink.getElementFather().getId());
						buffer.append(newLink.replace(nomePagina,plink.getElementFather().getLabel())+"\n");
					}else{
						String newLink = templateLink.replace(id, plink.getId());
						buffer.append(newLink.replace(nomePagina,plink.getLabel())+"\n");
					}
				}
			}
			if (grandString.contains(eventos)) {
				try{
					for (WikiElement edgeEvent:wiki.getEdgeEvents()) {
						for(WikiElement dest:edgeEvent.dest()){
							buffer.append("  * [["+dest.getId()+"|"+dest.getLabel()+"]]\n");
						}
					}
				}catch(Exception ex){
					
				}
			}
			bw.write(buffer.toString()+"\n");
		}
		bw.close();
		br.close();
	}
	
	public void writeIndex(WikiElement wiki, String path) throws Exception {
								
		BufferedReader br = new BufferedReader(new FileReader(reader.readFile("templateIndex.txt")));
		BufferedWriter bw = new BufferedWriter(new FileWriter(path+"index.txt"));
		
		String str;
		String diretorio = "$Diretorio$";
		String elements = "$elements$";
		String performer = "<Performer>";

		while ((str = br.readLine()) != null) {

			if(!str.contains("$"))
				bw.write(str);
			
			StringBuffer buffer = new StringBuffer();
			
			if (str.contains(diretorio)) {
				buffer.append(str.replace(diretorio, wiki.getLabel()));
				bw.write(buffer.toString() + "\n");
				continue;
			}
			if (str.contains(elements)) {
				String templateLink = br.readLine();
				for (WikiElement child : wiki.getEdgeEvents()) {
					if(child instanceof Page)
						continue;
					else
						buffer.append(templateLink.replace(performer, child.getLabel())+"\n");
				}
			}
			bw.write(buffer.toString() + "\n");
		}	
		bw.close();
		br.close();
	}

	public void writeStartEvents(WikiElement wiki, List<WikiElement> startEvents, String path) throws Exception {
		
		if(startEvents.size() == 0)
			return;
		
		BufferedReader br = new BufferedReader(new FileReader(reader.readFile("templateStartEvent.txt")));
		BufferedWriter bw = new BufferedWriter(new FileWriter(path+"startIndex.txt"));
		
		String str;
		String diretorio = "$Diretorio$";
		String starts = "$startEvents$";
		String id = "<idStartEvent>", name = "<nameStartEvent>";
		
		while ((str = br.readLine()) != null) {

			if(!str.contains("$"))
				bw.write(str);
			
			StringBuffer buffer = new StringBuffer();
			
			if (str.contains(diretorio)) {
				buffer.append(str.replace(diretorio, wiki.getLabel()));
				bw.write(buffer.toString() + "\n");
				continue;
			}
			if (str.contains(starts)) {
				String templateLink = br.readLine();
				for (WikiElement start:startEvents) {
					String newLink = templateLink.replace(id, start.getId());
					buffer.append(newLink.replace(name, start.getLabel())+"\n");
				}
			}
			bw.write(buffer.toString() + "\n");
		}	
		bw.close();
		br.close();
	}
}