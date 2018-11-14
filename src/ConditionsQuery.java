
public class ConditionsQuery implements Comparable<ConditionsQuery>{
	private String sStr;
	private String pStr;
	private String oStr;
	private int p;
	private int o;
	private int index;
	private int stat;
	
	public ConditionsQuery(int index, String sStr, String pStr, String oStr) {
		this.sStr = sStr;
		this.pStr = pStr;
		this.oStr = oStr;
		this.index = index;
		this.stat = -1;
	}
	
	public String toString() {
		return this.sStr + " " + this.pStr + " "+ this.oStr;
	}
	
	public String toStringWithInt() {
		return this.sStr + " " + this.p + " "+ this.o;
	}

	public int getStat() {
		return stat;
	}

	public void setStat(int stat) {
		this.stat = stat;
	}

	public int getIndex() {
		return index;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public int getO() {
		return o;
	}

	public void setO(int o) {
		this.o = o;
	}

	public String getsStr() {
		return sStr;
	}

	public String getpStr() {
		return pStr;
	}

	public String getoStr() {
		return oStr;
	}

	@Override
	public int compareTo(ConditionsQuery cq) {
		if(this.stat > cq.getStat()) {
			return 1;
		}
		else if(this.stat < cq.getStat()) {
			return -1;
		}
		else {
			return 0;
		}
	}
	
	
	
	
}
