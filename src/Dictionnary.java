import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.RDFHandlerBase;

public class Dictionnary {
	
	private HashSet<String> wordsSet;
	private HashMap<Integer, String> wordsMap;
	private HashMap<String, Integer> reverseWordsMap;
	

	public Dictionnary() {
		this.wordsSet = new HashSet<>();
		this.wordsMap = new HashMap<>();
		this.reverseWordsMap = new HashMap<>();
		
	}
	
	public void add(String word) {
		this.wordsSet.add(word);
		this.mapSetUp();

	}
	
	public void add(List<String> words) {
		this.wordsSet.addAll(words);
		this.mapSetUp();
	}
	
	public void remove(String word) {
		this.wordsSet.remove(word);
		this.mapSetUp();

	}
	
	public void remove(List<String> words) {
		this.wordsSet.removeAll(words);
		this.mapSetUp();
	}
	
	private void mapSetUp() {
		List<String> list = new ArrayList<String>(this.wordsSet);
		Collections.sort(list);
		
		this.wordsMap.clear();
		int index = 1;
		for(String word : list) {
			this.wordsMap.put(index, word);
			this.reverseWordsMap.put(word, index);
			index++;
		}
	}
	
	public int get(String s) {
		if(this.reverseWordsMap.containsKey(s)) {
			return this.reverseWordsMap.get(s);			
		}
		return (Integer) null;
	}
	
	public String get(int s) {
		if(this.wordsMap.containsKey(s)) {
			return this.wordsMap.get(s);			
		}
		return null;
	}
	

	
	
	public static void main(String[] args) {
		

	}

}
