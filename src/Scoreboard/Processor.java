package Scoreboard;

import java.util.Scanner;

class InstructionBufferUnit{
	Instruction ins;
	int d, s, x, w;
	int xFinish;
	boolean finish;
	public InstructionBufferUnit(Instruction ins){
		this.ins = ins;
		d = 0; s = 0; x = 0; w = 0;
		xFinish = 0;
		finish = false;
	}
}

public class Processor {
	FUTable FUTable = null;
	RegStatus RegStatus = null;
	InstructionBufferUnit[] InsBuffer = null;
	int currentClock;
	int bufferSize;
	
	public Processor(Instruction[] insSet, int size){
		currentClock = 0;
		bufferSize = size;
		FUTable = new FUTable();
		RegStatus = new RegStatus();
		InsBuffer = new InstructionBufferUnit[bufferSize];
		for(int i=0;i<bufferSize;i++){
			InsBuffer[i] = new InstructionBufferUnit(insSet[i]);
			InsBuffer[i].ins.print();
		}
	}
	
	public void print(){
		for(int i=0;i<bufferSize;i++){
			if(InsBuffer[i].d==0) break;
			System.out.println(InsBuffer[i].ins.op + " " + InsBuffer[i].d + " " 
					+ InsBuffer[i].s + " " + InsBuffer[i].x + " " + InsBuffer[i].w);
		}
	}
	
	public boolean finish(){
		for(int i=0;i<bufferSize;i++){
			if(!InsBuffer[i].finish) return false;
		}
		Note.log = Note.log.concat("\nFinish\n");
		return true;
	}
	
	public void takeStep(){
//		RegStatus.reflash();
		currentClock++;
		int[] handledIns = getHandledIns();
		for(int i=0;i<handledIns.length;i++){
			// W&X+
			if(InsBuffer[handledIns[i]].d!=0&&InsBuffer[handledIns[i]].s!=0&&InsBuffer[handledIns[i]].x!=0&&InsBuffer[handledIns[i]].w==0){
				if(InsBuffer[handledIns[i]].xFinish!=InsBuffer[handledIns[i]].ins.clockXNeeded){
					FUTable.X(InsBuffer[handledIns[i]].ins);
					RegStatus.X(InsBuffer[handledIns[i]].ins);
					InsBuffer[handledIns[i]].xFinish++;
					Note.log = Note.log.concat(InsBuffer[handledIns[i]].ins.toString() + "   continues eXecution\n");
				}
				else{
					if(Wable(InsBuffer[handledIns[i]], handledIns[i])){
						FUTable.W(InsBuffer[handledIns[i]].ins, RegStatus);
						RegStatus.W(InsBuffer[handledIns[i]].ins, FUTable);
						InsBuffer[handledIns[i]].w = currentClock;
						InsBuffer[handledIns[i]].finish = true;
						Note.log = Note.log.concat(InsBuffer[handledIns[i]].ins.toString() + "   Write back\n");
					}
				}
			}
			// X
			if(InsBuffer[handledIns[i]].d!=0&&InsBuffer[handledIns[i]].s!=0&&InsBuffer[handledIns[i]].x==0){
				if(Xable(InsBuffer[handledIns[i]])){
					FUTable.X(InsBuffer[handledIns[i]].ins);
					RegStatus.X(InsBuffer[handledIns[i]].ins);
					InsBuffer[handledIns[i]].x = currentClock;
					InsBuffer[handledIns[i]].xFinish++;
					Note.log = Note.log.concat(InsBuffer[handledIns[i]].ins.toString() + "   starts eXecution\n");
				}
			}
			// S
			if(InsBuffer[handledIns[i]].d!=0&&InsBuffer[handledIns[i]].s==0){
				if(Sable(InsBuffer[handledIns[i]])){
					FUTable.S(InsBuffer[handledIns[i]].ins);
					RegStatus.S(InsBuffer[handledIns[i]].ins);
					InsBuffer[handledIns[i]].s = currentClock;
					Note.log = Note.log.concat(InsBuffer[handledIns[i]].ins.toString() + "  iSsue\n");
				}
			}
			// D
			if(InsBuffer[handledIns[i]].d==0){
				if(Dable(InsBuffer[handledIns[i]])){
					FUTable.D(InsBuffer[handledIns[i]].ins, RegStatus);
					RegStatus.D(InsBuffer[handledIns[i]].ins, FUTable);
					InsBuffer[handledIns[i]].d = currentClock;
					Note.log = Note.log.concat(InsBuffer[handledIns[i]].ins.toString() + "   Dispatch\n");
				}
			}
		}
		FUTable.print();
		RegStatus.print();
	}
	
	private boolean Dable(InstructionBufferUnit insUnit){
		// check Structure hazard, check WAW
		if(FUTable.structureHazard(insUnit.ins)) {
			Note.log = Note.log.concat(insUnit.ins.toString() + "   Structure hazard -> cannot Dispatch\n");
			return false;
		}
		if(insUnit.ins.r.compareTo("-")!=0){
			if(RegStatus.WAW(insUnit.ins.r).compareTo("-")!=0) {
				Note.log = Note.log.concat(insUnit.ins.toString() + "   WAW -> cannot Dispatch\n");
				return false;
			}
		}
		return true;
	}
	
	private boolean Sable(InstructionBufferUnit insUnit){
		// check RAW(FUtable)
		if(FUTable.RAW(insUnit.ins)) {
			Note.log = Note.log.concat(insUnit.ins.toString() + "   RAW -> cannot iSsue\n");
			return false;
		}
		else return true;
	}
	
	private boolean Xable(InstructionBufferUnit insUnit){
		// no check
		return true;
	}
	
	private boolean Wable(InstructionBufferUnit insUnit, int index){
		// check WAR
		for(int i=index-1;i>=0;i--){
			if(!InsBuffer[i].finish){
				if(FUTable.WAR(insUnit.ins.r, InsBuffer[i].ins)&&InsBuffer[i].xFinish!=InsBuffer[i].ins.clockXNeeded) {
					Note.log = Note.log.concat(insUnit.ins.toString() + "   WAR -> cannot Write back\n");
					return false;
				}
			}
		}
		return true;
	}
	
	private int[] getHandledIns(){
		int last = 0;
		for(int i=0;i<bufferSize;i++){
			last = i;
			if(InsBuffer[i].d==0) break;
		}
		int[] handledIns = new int[last+1];
		for(int i=0;i<last+1;i++) handledIns[i] = i;
		return handledIns;
	}
}