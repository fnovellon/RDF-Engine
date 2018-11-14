import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Query {
	ArrayList<ConditionsQuery> initConditions = new ArrayList<>();
	ArrayList<ConditionsQuery> conditions = new ArrayList<>();
	String query;
	String filename;
	String joinOrder;
	ArrayList<String> results = new ArrayList<>();
	
	
	public Query(String query, String filename) {
		this.query = query;
		this.filename = filename;
		this.joinOrder ="";
		parseQuery();
		
	}
	
	private void parseQuery() {
		Pattern  pattern = Pattern.compile("\\{(.*)\\}");
		Matcher matcher = pattern.matcher(this.query);
		matcher.find();
		String where = matcher.group(1);
		
		String[] conds = where.split(" \\.");
		
		int index = 1;
		for(String cond : conds ) {
			cond = cond.trim();
			if(!cond.isEmpty()) {
				cond = cond.replaceAll("<|>", "");
				parseConditions(index, cond);
				index++;
			}
			
		}
	}
	
	public void parseConditions(int index, String cond) {
		String[] param = cond.split(" ");		
		
		this.initConditions.add(new ConditionsQuery(index, param[0], param[1], param[2]));
		this.conditions.add(new ConditionsQuery(index, param[0], param[1], param[2]));
	}

	public void sortConditions() {
		Collections.sort(conditions);
	}
	
	public String toString() {
		String result = "";
		result += "Fichier source : "+String.valueOf(this.filename+"\n");
		for(ConditionsQuery cq : initConditions) {
			result += "t"+cq.getIndex()+" : "+cq.toString() + "\n";
		}
		result += "\n";
		result += "Ordre jointure : "+this.joinOrder+"\n";
		
		for(ConditionsQuery c : conditions) {
			result += "t"+c.getIndex()+"["+c.getStat()+"] : "+c.toString() + "\n";
		}
		result += "\n";
		result += "Resultats :\n";
		if(results.size() == 0) 
			return result+"Aucun resultat... \n";
		
		for(String s : results) {
			result += s + "\n";
		}
		return result;
	}
}