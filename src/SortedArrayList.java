import java.util.ArrayList;

class SortedArrayList<T> extends ArrayList<T> {

	public void sortedAdd(ArrayList<Integer> list, int newInt) {
		int index = 0;
		for(; index < (list.size()) && (list.get(index) < newInt); index++) {;}
		
		if( (index == 0) || (list.get(index) != newInt) ) {
			list.add(index, newInt);
		}
	}
}
