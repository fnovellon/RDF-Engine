import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

public class RdfEngine {
	private MyDictionary dico;

	private Index pos;

	private RDFListener parserHandler;
	
	private ArrayList<Query> queries;
	private HashMap<String, Long> filesTime;

	RdfEngine() {
		this.dico = new MyDictionary();
		this.pos = new Index();
		this.parserHandler = new RDFListener();
		this.queries = new ArrayList<>();
		this.filesTime = new HashMap<>();
	}
	
	public void readDir(String path) throws FileNotFoundException, IOException, Exception {
		File dir = new File(path);
		if(dir.isDirectory()) {
			FilenameFilter ff = new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".rdfxml");
				}
			};
			ArrayList<File> files = new ArrayList<>(Arrays.asList(dir.listFiles(ff)));
			for(File f : files) {
				this.readFile(f);
			}
			this.parserHandler.setUpDicoIndex();
		}
	}

	public void readFile(File f) throws FileNotFoundException, Exception, IOException {

		Reader reader = new FileReader(f.getAbsolutePath());		

		RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		rdfParser.setRDFHandler(this.parserHandler);

		rdfParser.parse(reader, "");

		reader.close();

	}
	
	public void print(String s) {
		System.out.println(s);
	}
	
	public ArrayList<Query> getQueries() {
		return queries;
	}
	
	public void setQueries(ArrayList<Query> queries) {
		this.queries = queries;
	}

	
	public int initStat(ConditionsQuery cq) {
		//System.out.println(cq.getpStr());
		//System.out.println(cq.getoStr());
		int p = dico.get(cq.getpStr());
		int o = dico.get(cq.getoStr());
		if(p == -1 || o == -1) {
			return -1;
			//throw new Error("Le mot n'est pas connu dans le dictionnaire");
		}
		//System.out.println(p);
		//System.out.println(o);
		cq.setP(p);
		cq.setO(o);
		if(pos.getIndex().containsKey(p)) {
			if(pos.getIndex().get(p).containsKey(o)) {
				int size = pos.getIndex().get(p).get(o).size();
				cq.setStat(size);
				if(size != 0) {
					return size;
				}
				else {
					return -1;
				}
					
			}
			else {
				//throw new Error("La property o n'est pas dans l'index");
				return -1;
			}
		}
		else {
			return -1;
			//throw new Error("La property p n'est pas dans l'index");
		}
		
	}
	
	
	public void execute(Query q) {
		int size;
		for(ConditionsQuery cq : q.conditions) {
			size = this.initStat(cq);
			if(size == -1) {
				q.joinOrder = "";
				return;
			}
		}
		q.sortConditions();
		
		String joinOrder ="";
		HashSet<Integer> result = new HashSet<>();
		//HashSet<Integer> result2 = new HashSet<>();
		HashSet<Integer> tmp = new HashSet<>();
		boolean first = true;
		for(ConditionsQuery cq : q.conditions) {
			if(joinOrder.isEmpty()) {
				joinOrder = "t"+String.valueOf(cq.getIndex())+"["+cq.getStat()+"]";
		    }
		    else {
		    	joinOrder= "("+joinOrder+" t"+String.valueOf(cq.getIndex())+"["+cq.getStat()+"])";
		    }
			
			int p = cq.getP();
			int o = cq.getO();
			
			tmp = pos.getIndex().get(p).get(o);
			
			if(first) {
				result = new HashSet<>(tmp);
				//result2 = new HashSet<>(tmp);
				first = false;
			}
			else {
				//result = Join.toJoin(result, tmp);
				result.retainAll(tmp);
				//result2.retainAll(tmp);
				if(result.size() == 0) {
					q.joinOrder = joinOrder;
					return;
				}
			}
		}
		q.joinOrder = joinOrder;
		
		for(Integer i : result) {
			q.results.add(dico.get(i));
		}
		
		/*for(Integer i : result2) {
			q.results.add(dico.get(i));
		}*/
		//System.out.println("Done");
	}
	
	public long executeAll(boolean verbose) {
		long startTimeT = System.currentTimeMillis();
		long endTime = 0;
		String currentFile = this.queries.get(0).filename;
		long startTime = System.currentTimeMillis();
		for(Query q : this.queries) {
			if(currentFile != q.filename) {
				endTime = System.currentTimeMillis();
        		filesTime.put(currentFile, endTime-startTime);
        		
        		
        		if(verbose)
    			{
    				System.out.println(currentFile);
    				System.out.println("Temps d'execution du fichier : "+(endTime-startTime)+"ms\n");
    			}
        		
        		currentFile = q.filename;  
        		startTime = System.currentTimeMillis();
        	}
			this.execute(q);
		}
		long endTimeT = System.currentTimeMillis();
		
		
		//System.out.println("All done !");
		return endTimeT-startTimeT;
	}
	
	public void save(boolean exportResults, boolean exportStats, File output) throws IOException {

		String dir = output.getAbsolutePath()+"/";
		FileWriter writerStats = null;
		FileWriter writerResults = null;
		
		String csvTimeFile = dir+"ExecTimes.csv";
        FileWriter writerTimes = new FileWriter(csvTimeFile);
        CSVUtils.writeLine(writerTimes, Arrays.asList("Filename","Exec Time (ms)"));
        
        for(Entry<String, Long> entry : filesTime.entrySet()) {
        	List<String> listTime = new ArrayList<>();
    		listTime.add(entry.getKey());
    		listTime.add(String.valueOf((entry.getValue() == 0)? 1:entry.getValue()));
    		CSVUtils.writeLine(writerTimes, listTime);
        }
        
        writerTimes.flush();
        writerTimes.close();
        
        
        if(exportStats) {
        	String csvStatFile = dir+"Stats.csv";
        	writerStats = new FileWriter(csvStatFile);
        	CSVUtils.writeLine(writerStats, Arrays.asList("Query","Order Exec","Exec Time (ms)","Nb Results"));
        }
		
        if(exportResults) {
        	 String csvResultFile = dir+"Results.csv";
        	 writerResults = new FileWriter(csvResultFile);
        	 CSVUtils.writeLine(writerResults, Arrays.asList("Query","Results (all other columns)"));
        }

        for(Query q : this.queries) {
        	
        	if(exportStats) {
        		List<String> listStat = new ArrayList<>();
	        	listStat.add(q.query);
	            listStat.add(q.joinOrder);
	            listStat.add(String.valueOf(q.results.size()));
	    		CSVUtils.writeLine(writerStats, listStat);
        	}
        	
    		if(exportResults)
    		{
    			List<String> listResults = new ArrayList<>();
	    		listResults.add(q.query);
	    		for(String res : q.results) {
	    			listResults.add(res);
	    		}
	    		CSVUtils.writeLine(writerResults, listResults);
    		}
    		
        }
        //System.out.println(currentFile +" : "+String.valueOf(cpt));
        
        if(exportStats) {
        	writerStats.flush();
        	writerStats.close();
        }
        
        if(exportResults) {
        	writerResults.flush();
            writerResults.close();
        }
        
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
			this.list.add(st.getSubject().toString());
			this.list.add(st.getPredicate().toString());
			this.list.add(st.getObject().toString());
			
		}
		

		public void setUpDicoIndex() throws RDFHandlerException {
			super.endRDF();
			dico.add(this.list);
			for(int i = 0; i < this.list.size(); i+=3) {
				int s = dico.get(this.list.get(i));
				int p = dico.get(this.list.get(i+1));
				int o = dico.get(this.list.get(i+2));
				pos.add(p, o, s);
			}
			this.list.clear();
		}
		
		

	};
	
	public String queryToString() {
		String result = "";
		for(Query q : queries) {
			if(q.results.size() != 0 && true)
				result += q.toString() + "\n";
		}
		return result;
	}	
	
	public static void main(String[] args) {
		
		String pathQueries = "";
		String data = "";
		String output = "";
		boolean exportResults = false;
		boolean exportStats = false;
		boolean workloadTime = false;
		boolean verbose = false;
		boolean hasOutput = false;
		
		for(int i=0; i< args.length;i++) {
			String arg = args[i];
			switch(arg) {
				case "-verbose":
					verbose = true;
					break;
				case "-export_results":
					exportResults = true;
					break;
				case "-export_stats":
					exportStats = true;
					break;
				case "-workload_time":
					workloadTime =true;
					break;
				case "-queries":
					if(i+1<= args.length-1) {
						pathQueries = args[i+1];
						i++;
					}
					break;
				case "-data":
					if(i+1<= args.length-1) {
						data = args[i+1];
						i++;
					}
					break;
				case "-output":
					hasOutput = true;
					if(i+1<= args.length-1) {
						output = args[i+1];
						i++;
					}
					break;
				default:
					break;

			}
		}
		
		File dirData = new File(data);
		File dirQueries = new File(pathQueries);
		File dirOutput = new File(output);
		if(!dirData.isDirectory() || !dirQueries.isDirectory() || (hasOutput && !dirOutput.isDirectory())) {
			String err = "Vous devez spécifier un repertoire existant après -queries, -data et/ou -output \n";
			err += "Rappel format : \n";
			err += "java -jar rdfstar \n";
			err += "-queries \"/chemin/vers/requetes\"\n";
			err += "-data \"/chemin/vers/donnees\"\n";
			err += "-output \"/chemin/vers/dossier/sortie\"\n";
			err += "-verbose\n";
			err += "-export_results\n";
			err += "-export_stats\n";
			err += "-workload_time\n";
			throw new Error(err);
		}
		
		RdfEngine engine = new RdfEngine();
		
		try {
			engine.readDir(data);

			QueryRawParser qrp = new QueryRawParser(pathQueries);
			engine.setQueries(qrp.parseDir());
			
			long totalTime = engine.executeAll(verbose);
			
			// Print all informations for each query
			//System.out.println(engine.queryToString());
			
			if(workloadTime) {
				System.out.println("Temps total : "+totalTime+"ms");
			}
			
			
			if(hasOutput) {
				engine.save(exportResults, exportStats, dirOutput);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


}
