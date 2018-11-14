import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

public class QueryRawParser {
	
	private ArrayList<File> files;
	
	public QueryRawParser(String path) throws IOException {
		
		this.readDir(path);
	}
	
	public void readDir(String path) {
		File dir = new File(path);
		if(dir.isDirectory()) {
			FilenameFilter ff = new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".queryset");
				}
			};
			this.files = new ArrayList<>(Arrays.asList(dir.listFiles(ff)));
		}
	}
	
	public ArrayList<Query> parseDir() throws IOException{
		ArrayList<Query> queries = new ArrayList<>();
		for (File f : this.files) {
			queries.addAll(this.parseFile(f));
		}
		return queries;
	}
	
	public ArrayList<Query> parseFile(File f) throws IOException {
		ArrayList<Query> queries = new ArrayList<>();
		String filename = f.getName();
		String query = "";
		BufferedReader reader = new BufferedReader(
			    new InputStreamReader(
			        new FileInputStream(f),
			        Charset.forName("UTF-8")));
		int c;
		while((c = reader.read()) != -1) {
			char character = (char) c;
			query += character;
			if (character == '}') {
				query = query.trim();
				query = query.replaceAll("\r\n", "");
				query = query.replaceAll("\n", "");
				queries.add(new Query(query, filename));
				query = "";
				
			}
		}
		
		reader.close();
		return queries; 
	}
}
