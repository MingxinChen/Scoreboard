package Scoreboard;

class FUunit{
	String op, name;
	String r, r1, r2, t1, t2;
	boolean available;
	public FUunit(String name){
		this.name = name;
		clean();
	}
	
	public void clean(){
		this.op = "-";
		this.r = "-";
		this.r1 = "-";
		this.r2 = "-";
		this.t1 = "-";
		this.t2 = "-";
		this.available = true;
	}
}

public class FUTable {
	int FUNum = 5;
	boolean turn = true;
	FUunit[] Table = new FUunit[FUNum];
	public FUTable(){
		Table[0] = new FUunit("ALU");
		Table[1] = new FUunit("LD");
		Table[2] = new FUunit("ST");
		Table[3] = new FUunit("FP1");
		Table[4] = new FUunit("FP2");
	}
	
	public void print(){
		for(int i=0;i<FUNum;i++){
			System.out.println(Table[i].name + "\t" + Table[i].op + "   \t" +Table[i].r + "   \t" +Table[i].r1 + "   \t" +Table[i].r2 + "   \t" 
						+ Table[i].t1 + "   \t" + Table[i].t2 + "   \t");
		}
		System.out.println("");
	}
	
	public boolean structureHazard(Instruction ins){
		int index = currentFUNeed(ins);
		if(index!=-1&&Table[index].available) return false;
		else return true;
	}
	
	public boolean WAR(String reg, Instruction ins){
		int index = currentFU(ins);
		if(Table[index].r1.compareTo(reg)==0){
			if(Table[index].t1.compareTo("-")==0) return true;
		}
		if(Table[index].r2.compareTo(reg)==0){
			if(Table[index].t2.compareTo("-")==0) return true;
		}
		return false;
	}
	
	public boolean RAW(Instruction ins){
		int index = currentFU(ins);
		if(Table[index].t1.compareTo("-")==0&&Table[index].t2.compareTo("-")==0) return false;
		else return true;
	}
	
	public void D(Instruction ins, RegStatus RS){
		int index = currentFUNeed(ins);
		if(index==3){
			if(Table[3].available&&Table[4].available){
				if(turn) {
					index = 3; turn = false;
				}
				else {
					index =4; turn = true;
				}
			}
		}
		Table[index].op = ins.op;
		Table[index].r = ins.r;
		Table[index].r1 = ins.r1;
		Table[index].r2 = ins.r2;
		if(ins.r1.compareTo("-")!=0) Table[index].t1 = RS.WAW(ins.r1);
		if(ins.r2.compareTo("-")!=0) Table[index].t2 = RS.WAW(ins.r2);
		Table[index].available = false;
	}
	
	public void S(Instruction ins){
		return;
	}
	
	public void X(Instruction ins){
		return;
	}

	public void W(Instruction ins, RegStatus RS){
		int index = currentFU(ins);
//		RS.clean(Table[index].name);
		Table[index].clean();
	}
	
	public void clean(String reg, String tag){
		for(int i=0;i<FUNum;i++){
			if(Table[i].r1.compareTo(reg)==0) {
				if(Table[i].t1.compareTo(tag)==0) Table[i].t1 = "-";
			}
			if(Table[i].r2.compareTo(reg)==0) {
				if(Table[i].t2.compareTo(tag)==0) Table[i].t2 = "-";
			}
		}
	}
	
	public int currentFU(Instruction ins){
		for(int i=0;i<FUNum;i++){
			if(Table[i].op.compareTo(ins.op)==0&&Table[i].r.compareTo(ins.r)==0
					&&Table[i].r1.compareTo(ins.r1)==0&&Table[i].r2.compareTo(ins.r2)==0) return i;
		}
		return -1;
	}
	
	private int currentFUNeed(Instruction ins){
		if(ins.FUNeed().compareTo("ALU")==0) return 0;
		else if(ins.FUNeed().compareTo("LD")==0) return 1;
		else if(ins.FUNeed().compareTo("ST")==0) return 2;
		else if(ins.FUNeed().compareTo("FP")==0) {
			if(Table[3].available) return 3;
			else if(Table[4].available) return 4;
			else return -1;
		}
		else return -1;
	}
}