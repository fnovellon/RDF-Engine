
import java.util.ArrayList;
import java.util.HashMap;

public class Index {
	
	protected HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> index; 
	
	public Index() {
		this.index = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();
	}
	
	public void add(int first, int second, int thrid) {
		Boolean containsP = this.index.containsKey(first);
		if(containsP) {
			HashMap<Integer, ArrayList<Integer>> hashP = this.index.get(first);
			Boolean containsO = hashP.containsKey(second);
			if(containsO) {
				ArrayList<Integer> arrayPO = hashP.get(second);
				this.sortedAdd(arrayPO, thrid);
				
			} else {
				this.index.get(first).put(second, createSecond(thrid));
			}
		} else {
			this.index.put(first, createFirst(second, thrid));
		}
	}
	
	protected HashMap<Integer, ArrayList<Integer>> createFirst(int o, int s) {
		HashMap<Integer, ArrayList<Integer>> first = new HashMap<Integer, ArrayList<Integer>>();
		
		first.put(o, createSecond(s));
		
		return first;
	}
	
	protected ArrayList<Integer> createSecond(int s) {
		ArrayList<Integer> second = new ArrayList<Integer>();
		
		second.add(s);
		
		return second;
	}
	
	protected void sortedAdd(ArrayList<Integer> list, int newInt) {
		int index = 0;
		for(; index < (list.size()) && (list.get(index) < newInt); index++) {;}
		
		if( (index == 0) || (index == list.size()) || (list.get(index) != newInt) ) {
			list.add(index, newInt);
		}
	}
	
	public HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> getIndex() {
		return this.index;
	}
}