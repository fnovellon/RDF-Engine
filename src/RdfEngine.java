import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

public class RdfEngine {
	private Dictionnary dico;

	private Index pos;
	private Index ops;
	
	private RDFListener parserHandler;

	RdfEngine() {
		this.dico = new Dictionnary();
		this.pos = new Index();
		this.ops = new Index();
		this.parserHandler = new RDFListener();
	}

	public void readFile(String path) throws FileNotFoundException, Exception, IOException {

		Reader reader = new FileReader(path);		

		RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		rdfParser.setRDFHandler(this.parserHandler);

		rdfParser.parse(reader, "");

		reader.close();

	}
	public void print(String s) {
		System.out.println(s);
	}
	
	public ArrayList<String> query(String pStr, String oStr) {
		ArrayList<String> res = new ArrayList<>();
		ArrayList<Integer> resInt;
		int p, o;
		
		try {
			p = dico.get(pStr);
			o = dico.get(oStr);
			resInt = pos.getIndex().get(p).get(o);
			for (Integer idWord : resInt) {
				res.add(dico.get(idWord));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return res;
				
	}
	
	private class RDFListener extends RDFHandlerBase {
		
		private List<String> list;

		
		@Override
		public void startRDF() throws RDFHandlerException {
			super.startRDF();
			this.list = new ArrayList<String>();
		}

		
		@Override
		public void handleStatement(Statement st) {
			//getContext = null
			//getObject
			//getPredicate = assoc
			//getSubject
			
			//System.out.println("\n"+st.getObject()+"\t"+st.getPredicate()+"\t"+st.getSubject());
			this.list.add(st.getObject().toString());
			this.list.add(st.getPredicate().toString());
			this.list.add(st.getSubject().toString());
		}
		

		@Override
		public void endRDF() throws RDFHandlerException {
			super.endRDF();
			dico.add(this.list);
			for(int i = 0; i < this.list.size(); i+=3) {
				int s = dico.get(this.list.get(i));
				int p = dico.get(this.list.get(i+1));
				int o = dico.get(this.list.get(i+2));
				pos.add(p, o, s);
				ops.add(o, p, s);
			}
			this.list.clear();
		}
		
		

	};
	
	public static void testQuery(RdfEngine engine) {
		ArrayList<String> resQuery = engine.query("http://purl.org/stuff/rev#reviewer", "http://db.uwaterloo.ca/~galuc/wsdbm/Review1489");
		
		System.out.println("Resultat de la requête : ?x http://purl.org/stuff/rev#reviewer http://db.uwaterloo.ca/~galuc/wsdbm/Review1489 :");
		for (String s : resQuery) {
			System.out.println(s);
		}
	}
	
	public static void main(String[] args) {
		RdfEngine engine = new RdfEngine();
		try {
			engine.readFile("res/100K.rdfxml");

			testQuery(engine);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
