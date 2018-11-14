import java.util.ArrayList;

public class Join {
	public static ArrayList<Integer> toJoin(ArrayList<Integer> t1, ArrayList<Integer> t2) {
		ArrayList<Integer> join = new ArrayList<>();
		int sizeT1 = t1.size();
		int sizeT2 = t2.size();
		if(sizeT1 == 0 || sizeT2 == 0) {
			return join;
		}
		int i = 0, j = 0, current = -1, idWord;
		String side = "L";
		while((i < sizeT1 || side == "R") && (j < sizeT2 || side =="L")) {
			if(side.equals("L")) {
				idWord = t1.get(i);
				if (idWord == current) {
					join.add(current);
					//System.out.println("Add L");
					if(i+1 < sizeT1) {
						current = t1.get(i+1);
					}
					i = i+2;
					side = "R";
				}
				else if(idWord < current) {
					i++;
				}
				else {
					current = idWord;
					i++;
					side = "R";
				}
			}
			else {
				idWord = t2.get(j);
				if (idWord == current) {
					join.add(current);
					//System.out.println("Add R");
					if(j+1 < sizeT2) {
						current = t2.get(j+1);
					}
					j = j+2;
					side = "L";
				}
				else if(idWord < current) {
					j++;
				}
				else {
					current = idWord;
					j++;
					side = "L";
				}
			}
			//System.out.println("current : "+current+" -- i : "+i+ " -- j : "+j+" -- side : "+side);
		}
		
		return join;
	}
}
