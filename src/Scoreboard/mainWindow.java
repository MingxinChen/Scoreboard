package Scoreboard;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class mainWindow extends JFrame {
	private JDesktopPane  window = new JDesktopPane();
	private MouseOption mouseOption = new MouseOption();
	private JPanel insBuffer, regStatus, FUtable, clockJP;
	private JTable insBufferT, regStatusT, FUtableT;
	private JButton clock, clean, start, finish, example1, example2, example3, example4;
	private JTextArea noteT;
	private JLabel clockJL;
	private JScrollPane note;
	private Task task = null;
	
	public static void main(String arg[]){
		new Note();
		new mainWindow();
	}
	
	public mainWindow(){
		super("Scoreboard");
		setWindow(1000, 610);
		insBufferSetting(Color.white, Color.darkGray, 17, 5, 10, 10, 400, 560);
		FUtableSetting(Color.white, Color.darkGray, 6, 8, 420, 10, 400, 200);
		regStatusSetting(Color.white, Color.darkGray, 8, 2, 420, 320, 160, 250);
		noteSetting(Color.white, Color.darkGray, 590, 320, 400, 250);
		clockSetting(Color.white, Color.darkGray, 830, 30, 150, 150);
		setButton(start, "START", Color.white, 670, 240, 85, 20);
		setButton(finish, "FINISH", Color.white, 670, 270, 85, 20);
		setButton(clock, "CLOCK", Color.white, 770, 240, 85, 20);
		setButton(clean, "CLEAN", Color.white, 770, 270, 85, 20);
		setButton(example1, "Example1", Color.white, 870, 180, 85, 20);
		setButton(example2, "Example2", Color.white, 870, 210, 85, 20);
		setButton(example3, "Example3", Color.white, 870, 240, 85, 20);
		setButton(example4, "Example4", Color.white, 870, 270, 85, 20);
		setLabel("Scoreboard", Font.BOLD, 31, Color.GRAY, 440, 220, 200, 50);
		setLabel("@Chen Mingxin", Font.ITALIC, 15, Color.gray, 510, 270, 200, 25);
		this.getContentPane().add(window, BorderLayout.CENTER);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);
	    this.setVisible(true); 
	}
	
	private void setWindow(int width, int height){
	    this.setLayout(new BorderLayout());
	    this.setSize(width, height);
	    JLabel backgroundImage = new JLabel();
	    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = image.createGraphics();
	    BufferedImage ad = null;
	    try {
	        ad = ImageIO.read(new FileInputStream("res/background.jpg"));
	    } catch (IOException e) {           
	        e.printStackTrace();
	    }
        g.drawImage(ad, 0, 0, this.getWidth(), this.getHeight(), this);  
	    ImageIcon img = new ImageIcon(image);
	    backgroundImage.setIcon(img);
	    backgroundImage.setBounds(new Rectangle(0, 0, width, height));
	    window.add(backgroundImage, new Integer(Integer.MIN_VALUE));      
	}
	
	private void setButton(JButton jb, String name, Color c, int x1, int y1, int width, int height){
		jb = new JButton(name);
		jb.setBackground(c);
		jb.setBounds(x1, y1, width, height);
		jb.setContentAreaFilled(true);
	    jb.setBorderPainted(true);
	    jb.addMouseListener(mouseOption);
	    jb.setText(name);
	    window.add(jb, Integer.MIN_VALUE + 1);
	}
	
	private void setLabel(String name, int style, int size, Color c, int x1, int y1, int width, int height){
		JLabel jlb = new JLabel(name);
		jlb.setBounds(x1, y1, width, height);
		jlb.setForeground(c);
		Font font=new Font("楷体_GB2312", style, size);
		jlb.setFont(font);
		window.add(jlb);
	}
	
	private void insBufferSetting(Color c, Color b, int row, int col, int x1, int y1, int width, int height){
		insBuffer = new JPanel();
		insBuffer.setBounds(x1, y1, width, height);
		insBuffer.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, c));//设置面板边框颜色insBufferT = new JTable(11, 5);
	    String[] columnNames = {"Instruction", "D", "S", "X", "W"};
		Object[][] cellData = {{"Instruction", "D", "S", "X", "W"},{"", "", "", "", ""},{"", "", "", "", ""},{"", "", "", "", ""},{"", "", "", "", ""}
			,{"", "", "", "", ""},{"", "", "", "", ""},{"", "", "", "", ""},{"", "", "", "", ""},{"", "", "", "", ""},{"", "", "", "", ""},{"", "", "", "", ""}
			,{"", "", "", "", ""},{"", "", "", "", ""},{"", "", "", "", ""},{"", "", "", "", ""},{"", "", "", "", ""},{"", "", "", "", ""}};
		insBufferT = new MyTable(cellData, columnNames) { // 设置jtable的单元格为透明的
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (c instanceof JComponent)  ((JComponent) c).setOpaque(false);
				return c;
			}
		};
		insBufferT.setForeground(c);
		insBufferT.setOpaque(false);
		insBuffer.setOpaque(false);
		insBufferT.setRowHeight(30);
		TableColumn firsetColumn = insBufferT.getColumnModel().getColumn(0);
		firsetColumn.setPreferredWidth(150);
		firsetColumn.setMaxWidth(150);
		firsetColumn.setMinWidth(150);
		for(int i=1;i<col;i++){
			TableColumn column = insBufferT.getColumnModel().getColumn(i);
			column.setPreferredWidth(50);
			column.setMaxWidth(50);
			column.setMinWidth(50);
		}
		insBuffer.add(insBufferT, "Center");
	    window.add(insBuffer, Integer.MIN_VALUE + 1);
	}
	
	private void FUtableSetting(Color c, Color b, int row, int col, int x1, int y1, int width, int height){
		FUtable = new JPanel();
		FUtable.setBounds(x1, y1, width, height);
		FUtable.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, c));//设置面板边框颜色insBufferT = new JTable(11, 5);
	    String[] columnNames = {"FU", "Busy", "OP", "R", "R1", "R2", "T1", "T2"};
	    Object[][] cellData = {{"FU", "Busy", "OP", "R", "R1", "R2", "T1", "T2"},{"ALU", "", "", "", "", "", "", ""},{"LD", "", "", "", "", "", "", ""},{"ST", "", "", "", "", "", "", ""},{"FP1", "", "", "", "", "", "", ""},{"FP2", "", "", "", "", "", "", ""}};
		FUtableT = new MyTable(cellData, columnNames){
			@Override
			public boolean isCellEditable(int row, int column) {
		    	  if(row==0||column==0) return false;
		    	  else return true;
		     }
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (c instanceof JComponent)  ((JComponent) c).setOpaque(false);
				return c;
			}
		};
		FUtableT.setForeground(c);
		FUtable.setOpaque(false);
		FUtableT.setOpaque(false);
		FUtableT.setRowHeight(30);
		for(int i=0;i<col;i++){
			TableColumn column = FUtableT.getColumnModel().getColumn(i);
			column.setPreferredWidth(45);
			column.setMaxWidth(45);
			column.setMinWidth(45);
		}
		FUtable.add(FUtableT, "Center");
	    window.add(FUtable, Integer.MIN_VALUE + 1);
	}
	
	private void noteSetting(Color c, Color b, int x1, int y1, int width, int height){
		noteT = new JTextArea();
		noteT.setSelectedTextColor(c);
		noteT.setLineWrap(true);        //激活自动换行功能 
		noteT.setWrapStyleWord(true);            // 激活断行不断字功能
		noteT.setEditable(true);    //设置可编辑  
		noteT.setBounds(0 ,0 ,width,height);    //设置 JTextArea 宽100,高500  
		note = new JScrollPane(noteT);
		note.setBounds(x1, y1, width, height);
		note.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.DARK_GRAY));//设置面板边框颜色insBufferT = new JTable(11, 5);
		note.setOpaque(false);
		window.add(note, Integer.MIN_VALUE + 1);
		noteT.setText(Note.log);
	}
	
	private void clockSetting(Color c, Color b, int x1, int y1, int width, int height){
		clockJP = new JPanel();
		clockJP.setBounds(x1, y1, width, height);
		clockJP.setOpaque(false);
		clockJL = new JLabel("00");
		clockJL.setForeground(c);
		Font font=new Font("楷体_GB2312", Font.BOLD, 60);
		clockJL.setFont(font);
		clockJL.setBounds(x1, y1, width, height);
		clockJP.add(clockJL);
		window.add(clockJP, Integer.MIN_VALUE + 1);
	}
	
	private void regStatusSetting(Color c, Color b, int row, int col, int x1, int y1, int width, int height){
		regStatus = new JPanel();
		regStatus.setBounds(x1, y1, width, height);
		regStatus.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, c));//设置面板边框颜色insBufferT = new JTable(11, 5);
	    String[] columnNames = {"Reg", "Status"};
	    Object[][] cellData = {{"Reg", "Status"}, {"F0", ""}, {"F1", ""}, {"F2", ""}, {"R0", ""}, {"R1", ""}, {"R2", ""}};
	    regStatusT = new MyTable(cellData, columnNames){
			@Override
			public boolean isCellEditable(int row, int column) {
		    	  if(row==0||column==0) return false;
		    	  else return true;
		     }
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (c instanceof JComponent)  ((JComponent) c).setOpaque(false);
				return c;
			}
		};
		regStatusT.setForeground(c);
		regStatus.setOpaque(false);
		regStatusT.setOpaque(false);
		regStatusT.setRowHeight(30);
	    regStatusT.setRowHeight(30);
		TableColumn column = regStatusT.getColumnModel().getColumn(0);
		column.setPreferredWidth(50);
		column.setMaxWidth(50);
		column.setMinWidth(50);
		column = regStatusT.getColumnModel().getColumn(1);
		column.setPreferredWidth(80);
		column.setMaxWidth(80);
		column.setMinWidth(80);
		regStatus.add(regStatusT, "Center");
	    window.add(regStatus, Integer.MIN_VALUE + 1);
	}
	
	private void initilize(int mode){
		if(mode==0){
			String[] ins = getValue(insBufferT, "COL", 0);
			task = new Task(ins);
			clockJL.setText("00");
			Clean("-", ins.length);
		}
		else if(mode==1){
			String[] ins = {"ldf z(r1),f1", "mulf f0,f1,f2", "stf f2,z(r1)", "addi r1,4,r1", "ldf z(r1),f1", "mulf f0,f1,f2", "stf f2,z(r1)"};
			for(int i=0;i<ins.length;i++){
				insBufferT.setValueAt(ins[i], i + 1, 0);
			}
		}
		else if(mode==2){
			String[] ins = {"ldf z(r0),f1", "ld z(r1),f2", "mulf f1,f2,f0", "addi r0,4,r0", "ldf z(r0),f1", "mulf f1,f2,f2", "stf f2,z(r2)", "sub r1,r1,r1", "sub r0,r0,r0", "addi r0,10,r1"};
			for(int i=0;i<ins.length;i++){
				insBufferT.setValueAt(ins[i], i + 1, 0);
			}
		}
		else if(mode==3){
			String[] ins = {"ldf z(r0),f1", "addf f1,f0,f2", "ld z(r1),f2", "div r1,r0,r2", "mulf f1,f2,f0", "addi r0,4,r0", "ldf z(r0),f1", "st r2,z(r0)", "mulf f1,f2,f2", "stf f2,z(r2)", "sub r1,r1,r1", "subf f0,f1,f2", "sub r0,r0,r0", "st r2,z(r1)", "addi r0,10,r1"};
			for(int i=0;i<ins.length;i++){
				insBufferT.setValueAt(ins[i], i + 1, 0);
			}
		}
		else if(mode==4){
			String[] ins = {"addi r0,1400,r0", "sub r1,r2,r2", "ldf z(r0),f1", "st r2,z(r0)", "addf f1,f0,f2", "ldf z(r0),f1", "mulf f1,f2,f0", "addi r0,4,r0", "ld z(r1),f2", "div r1,r0,r2", "mulf f1,f2,f2", "stf f2,z(r2)", "sub r1,r1,r1", "subf f0,f1,f2", "sub r0,r0,r0", "st r2,z(r1)", "addi r0,10,r1"};
			for(int i=0;i<ins.length;i++){
				insBufferT.setValueAt(ins[i], i + 1, 0);
			}
		}
	}
	
	private String[] getValue(JTable table, String mode, int pos){
		if(mode.compareTo("COL")==0){
			int row = table.getRowCount();
			int size = 0;
			String[] member = new String[row];
			for(int i=0;i<row;i++){
				String s = (String)table.getValueAt(i, pos);
				member[i] = s;
				if(member[i].compareTo("")!=0) size++;
			}
			size--;
			String[] buffer = new String[size];
			for(int i=0;i<size;i++) buffer[i] = "".concat(member[i+1]);
			return buffer;
		}
		return null;
	}
	
	private void Clean(String s, int insLen){
		// insBuffer
		int temp = 5;
		if(s.compareTo("")!=0) temp=4;
		int size = task.processor.bufferSize;
		for(int i=0;i<size;i++){
			for(int j=0;j<temp;j++){
				if(s.compareTo("")!=0) insBufferT.setValueAt(s, i + 1, j+1);
				else insBufferT.setValueAt(s, i + 1, j);
			}
		}
		// FUTable
		for(int i=0;i<5;i++){
			for(int j=0;j<7;j++){
				FUtableT.setValueAt(s, i+1, j+1);
			}
		}
		// regStatus
		for(int i=0;i<5;i++){
			regStatusT.setValueAt(s, i +1, 1);
		}
	}
	
	private void write(){
		// insBuffer
		String[][] data = task.getInsBuffer();
		for(int i=0;i<task.size;i++){
			for(int j=0;j<4;j++){
				insBufferT.setValueAt(data[i][j], i + 1, j + 1);
			}
		}
		// FUTable
		data = task.getFUTable();
		for(int i=0;i<5;i++){
			for(int j=0;j<7;j++){
				FUtableT.setValueAt(data[i][j], i+1, j+1);
			}
		}
		// regStatus
		String[] data2 = task.getRegStatus();
		for(int i=0;i<5;i++){
			regStatusT.setValueAt(data2[i], i +1, 1);
		}
		// clock
		if(task.getFinish()) {
			clockJL.setText("End!");
		}
		else{
			if(task.getClock()<10) clockJL.setText("0" + task.getClock());
			else if(task.getClock()>99) clockJL.setText("OutOfBound");
			else clockJL.setText("" + task.getClock());
		}
		// note
		noteT.setText(Note.log);
	}
	
	private class MyTable extends JTable{
		public MyTable(Object[][] obj, String[] str){
			super(obj, str);
		}
		@Override
		public boolean isCellEditable(int row, int column) {
	    	  if(row==0) return false;
	    	  else return true;
	     }
	}
	
	private class MouseOption extends MouseAdapter{
	    private Timer timer = new Timer();
	    private String choice = null;
	    private class MyTimerTask extends TimerTask {
	        JButton button = null;
	        public MyTimerTask(JButton button) {
	            this.button = button;                
	        }
	        @Override
	        public void run() {
	        	switch(choice) {
	        		case "CLOCK":
	        			task.takeOneStep();
	        			write();
	        			break;
	        		case "START":
	        			initilize(0);
	        			break;
	        		case "FINISH":
	        			task.finish();
	        			write();
	        			break;
	        		case "CLEAN":
	        			Clean("", 13);
	        			task = null;
	        			break;
	        		case "Example1":
	        			initilize(1);
	        			break;
	        		case "Example2":
	        			initilize(2);
	        			break;
	        		case "Example3":
	        			initilize(3);
	        			break;
	        		case "Example4":
	        			initilize(4);
	        			break;
	        	}
	        	button.setContentAreaFilled(false);
	        }        
	    }
	    @Override
	    public void mouseClicked(MouseEvent e) {
	        JButton button = (JButton) e.getSource();
	        button.setContentAreaFilled(true);
	        choice = button.getText();
	        timer.schedule(new MyTimerTask(button), 400);            
	    }
	}
}