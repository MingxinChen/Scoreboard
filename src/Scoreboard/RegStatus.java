package Scoreboard;

class RegStatusUnit{
	String reg;
	String status;
	public RegStatusUnit(String reg){
		this.reg = reg;
		status = "-";
	}
}
public class RegStatus {
	int registerNum = 6;
	RegStatusUnit[] Table = new RegStatusUnit[registerNum];
	public RegStatus(){
		Table[0] = new RegStatusUnit("f0");
		Table[1] = new RegStatusUnit("f1");
		Table[2] = new RegStatusUnit("f2");
		Table[3] = new RegStatusUnit("r0");
		Table[4] = new RegStatusUnit("r1");
		Table[5] = new RegStatusUnit("r2");
	}
	
	public void print(){
		for(int i=0;i<registerNum;i++){
			System.out.println(Table[i].reg + "   \t" + Table[i].status);
		}
		System.out.println("");
	}
	
	public String WAW(String r){
		int index = regIndex(r);
		return Table[index].status;
	}
	
	public void D(Instruction ins, FUTable FU){
		if(ins.r.compareTo("-")!=0){
			int index = regIndex(ins.r);
			Table[index].status = FU.Table[FU.currentFU(ins)].name;
		}
	}
	
	public void S(Instruction ins){
		return;
	}
	
	public void X(Instruction ins){
		return;
	}

	public void W(Instruction ins, FUTable FU){
		if(ins.r.compareTo("-")!=0){
			int index = regIndex(ins.r);
			FU.clean(Table[index].reg, Table[index].status);
			Table[index].status = "-";
		}
	}
	
	private int regIndex(String reg){
		for(int i=0;i<registerNum;i++){
			if(Table[i].reg.compareTo(reg)==0) return i;
		}
		return -1;
	}
}
