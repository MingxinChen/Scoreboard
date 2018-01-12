package Scoreboard;

public class Task {
	Processor processor = null;
	int size;
	public Task(String[] ins){
		size = ins.length;
		System.out.println(size);
		Instruction[] insSet = new Instruction[size];
		for(int i=0;i<size;i++){
			insSet[i] = new Instruction(ins[i]);
		}
		processor = new Processor(insSet, size);
		System.out.println(processor.InsBuffer[0].ins.op);
	}
	
	public void takeOneStep(){
		processor.takeStep();
	}
	
	public void finish(){
		while(!processor.finish()){
			processor.takeStep();
		}
	}
	
	public String[] getRegStatus(){
		String[] data = new String[processor.RegStatus.registerNum];
		for(int i=0;i<processor.RegStatus.registerNum;i++){
			data[i] = "".concat(processor.RegStatus.Table[i].status);
		}
		return data;
	}
	
	public String[][] getInsBuffer(){
		String[][] data = new String[processor.bufferSize][4];
		for(int i=0;i<processor.bufferSize;i++){
			if(processor.InsBuffer[i].d!=0) data[i][0] = "C" + processor.InsBuffer[i].d;
			else data[i][0] = "-";
			if(processor.InsBuffer[i].s!=0) data[i][1] = "C" + processor.InsBuffer[i].s;
			else data[i][1] = "-";
			if(processor.InsBuffer[i].x!=0) data[i][2] = "C" + processor.InsBuffer[i].x;
			else data[i][2] = "-";
			if(processor.InsBuffer[i].w!=0) data[i][3] = "C" + processor.InsBuffer[i].w;
			else data[i][3] = "-";
		}
		return data;
	}
	
	public String[][] getFUTable(){
		String[][] data = new String[5][7];
		for(int i=0;i<processor.FUTable.FUNum;i++){
				if(processor.FUTable.Table[i].available) data[i][0] = "    √";
				else data[i][0] = "    ×";
				data[i][1] = "".concat(processor.FUTable.Table[i].op);
				data[i][2] = "".concat(processor.FUTable.Table[i].r);
				data[i][3] = "".concat(processor.FUTable.Table[i].r1);
				data[i][4] = "".concat(processor.FUTable.Table[i].r2);
				data[i][5] = "".concat(processor.FUTable.Table[i].t1);
				data[i][6] = "".concat(processor.FUTable.Table[i].t2);
		}
		return data;
	}
	
	public int getClock(){
		return processor.currentClock;
	}
	
	public boolean getFinish(){
		return processor.finish();
	}
	
}