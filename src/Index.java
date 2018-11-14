
import java.util.HashSet;
import java.util.HashMap;

public class Index {
	
	protected HashMap<Integer, HashMap<Integer, HashSet<Integer>>> index; 
	
	public Index() {
		this.index = new HashMap<Integer, HashMap<Integer, HashSet<Integer>>>();
	}
	
	public void add(int first, int second, int thrid) {
		Boolean containsP = this.index.containsKey(first);
		if(containsP) {
			HashMap<Integer, HashSet<Integer>> hashP = this.index.get(first);
			Boolean containsO = hashP.containsKey(second);
			if(containsO) {
				HashSet<Integer> arrayPO = hashP.get(second);
				//this.sortedAdd(arrayPO, thrid);
				arrayPO.add(thrid);
				
			} else {
				this.index.get(first).put(second, createSecond(thrid));
			}
		} else {
			this.index.put(first, createFirst(second, thrid));
		}
	}
	
	protected HashMap<Integer, HashSet<Integer>> createFirst(int o, int s) {
		HashMap<Integer, HashSet<Integer>> first = new HashMap<Integer, HashSet<Integer>>();
		
		first.put(o, createSecond(s));
		
		return first;
	}
	
	protected HashSet<Integer> createSecond(int s) {
		HashSet<Integer> second = new HashSet<Integer>();
		
		second.add(s);
		
		return second;
	}
	
	/*
	protected void sortedAdd(HashSet<Integer> list, int newInt) {
		int index = 0;
		for(; index < (list.size()) && (list.get(index) < newInt); index++) {;}
		
		if( (index == 0) || (index == list.size()) || (list.get(index) != newInt) ) {
			list.add(index, newInt);
		}
	}
	*/
	
	public HashMap<Integer, HashMap<Integer, HashSet<Integer>>> getIndex() {
		return this.index;
	}
}