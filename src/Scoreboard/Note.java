package Scoreboard;

public class Note {
	static String log = "";
	public Note(){
		log = log.concat("This program is aimed at simulating the order of the execution of instructions in the processor with Scoreboard.\n");
		log = log.concat("The set of options supported is {st, ld, add, addi, sub, subi, div, divi, mul, muli, stf, ldf, addf, subf, divf, mulf}.\n");
		log = log.concat("You can enter the set by yourself or pick up one of the examples on the right.\n");
		log = log.concat("Version 4 Dec 18, 2016\n\n");
		log = log.concat("Example/Enter -> START -> CLOCK/FINISH -> CLEAN -> ...\n");
		log = log.concat("eg. 'ld z(r0),f0'\n\n");
	}
}
