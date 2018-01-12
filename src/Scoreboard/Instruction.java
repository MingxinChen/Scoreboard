package Scoreboard;

public class Instruction {
	String ins;
	String op;
	String r1, r2, r;
	int clockXNeeded;
	boolean finish;
	
	public Instruction(String ins){
		int[] pos = new int[3];
		int posSum = 0;
		for(int i=0;i<3;i++) pos[i] = 0;
		for(int i=0;i<ins.length();i++){
			if(ins.charAt(i)==' '||ins.charAt(i)==',') {
				pos[posSum] = i;
				posSum++;
			}
		}
		finish = false;
		this.ins = ins;
		this.op = ins.substring(0, pos[0]);
		if(op.compareTo("ldf")==0||op.compareTo("ld")==0){
			this.r1 = ins.substring(pos[0]+3, pos[1]-1); 
			this.r2 = "-";
			this.r = ins.substring(pos[1]+1);
			clockXNeeded = 1;
		}
		else if(op.compareTo("stf")==0||op.compareTo("st")==0){
			this.r1 = ins.substring(pos[0]+1, pos[1]); 
			this.r2 = ins.substring(pos[1]+3, ins.length()-1);
			this.r = "-"; 
			clockXNeeded = 1;
		}
		else if(op.compareTo("add")==0||op.compareTo("sub")==0||op.compareTo("div")==0||op.compareTo("mul")==0){
			this.r1 = ins.substring(pos[0]+1, pos[1]); 
			this.r2 = ins.substring(pos[1]+1, pos[2]);
			this.r = ins.substring(pos[2]+1); 
			clockXNeeded = 1;
		}
		else if(op.compareTo("addi")==0||op.compareTo("subi")==0||op.compareTo("divi")==0||op.compareTo("muli")==0){
			this.r1 = ins.substring(pos[0]+1, pos[1]);
			this.r2 = "-";
			this.r = ins.substring(pos[2]+1); 
			clockXNeeded = 1;
		}
		else if(op.compareTo("mulf")==0||op.compareTo("divf")==0||op.compareTo("subf")==0||op.compareTo("addf")==0){
			this.r1 = ins.substring(pos[0]+1, pos[1]);
			this.r2 = ins.substring(pos[1]+1, pos[2]);
			this.r = ins.substring(pos[2]+1); 
			clockXNeeded = 3;
		}
	}
	
	public void print(){
		System.out.println(op + " " + r1 + " "+ r2 + " " + r);
	}
	
	@Override
	public String toString(){
		return ins;
		
	}
	public String FUNeed(){
		if(op.compareTo("st")==0||op.compareTo("stf")==0) return "ST";
		if(op.compareTo("ld")==0||op.compareTo("ldf")==0) return "LD";
		char f = op.charAt(op.length()-1);
//		System.out.println(f);
		if(f=='f') return "FP";
		else return "ALU";
	}
	
	
	
}
