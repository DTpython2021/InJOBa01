package test26.exam01;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

//import javafx.scene.control.Button;


public class SlotMachine extends Applet implements Runnable, ActionListener {
	Thread clock;
	Image off;
	Graphics offG;
	Image slot;
	Image machine;
	
	Random r;
	int[] loc; //슬롯위치
	int[] speed; //슬롯속도
	int[] hit;
	int slotNum;
	boolean[] stopSlot;
	boolean[] moveSlot;
	
	Button startButton, stopButton;
	Panel buttonPanel;
	
	public void init() {
		//메모리상의 가상화면 만들기
		off= createImage(350,350);
		offG =off.getGraphics();
		offG.setColor(Color.white);
		offG.fillRect(0, 0, 350, 350);
		//이미지 로드
		MediaTracker tracker = new MediaTracker(this);
		slot = Toolkit.getDefaultToolkit().getImage("D:/abc/source/Test26/src/Image/money4.png");
		tracker.addImage(slot, 0);
		
		machine = Toolkit.getDefaultToolkit().getImage("D:/abc/source/Test26/src/Image/image3.png"); 
		tracker.addImage(machine, 0);
		
		try {
			tracker.waitForAll();
		} catch (InterruptedException ie) {}
		
		while ((tracker.statusAll(true)& MediaTracker.COMPLETE)==0) {}
		
		//gui
		setLayout(new BorderLayout());
		buttonPanel = new Panel();
		startButton = new Button("당겨!");
		startButton.addActionListener(this);
		buttonPanel.add(startButton);
		stopButton = new Button("STOP!!");
		stopButton.addActionListener(this);
		buttonPanel.add(stopButton);
		add("South",buttonPanel);
		
		loc = new int[3];
		speed = new int[3];
		hit = new int [3];
		stopSlot = new boolean[3];
		moveSlot = new boolean[3];
		
		r = new Random();
		for (int i = 0; i < 3; i++) {
			loc[i] = Math.abs(r.nextInt()% 9) * 64;
			speed[i] = Math.abs(r.nextInt()% 9) * 10 + 10 ;
			stopSlot[i] = true;
			moveSlot[i] = false;			
			
		}
		slotNum = 0;
		
		
	}
	
	public void start () {
		if (clock==null) {
			clock = new Thread(this);
			clock.start();
		}
	}
	
	public void paint(Graphics g) {
		// 가상화면을 출력
		g.drawImage(off, 0, 0, this);
		
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				clock.sleep(30);
			} catch(InterruptedException ie) {}
			
			offG.setColor(Color.white);
			offG.fillRect(0, 0, 350,350);
			drawSlot();
			offG.drawImage(machine, 0, 0, this);
			repaint();
		}
		
	}
	
	public void drawSlot() {
		for (int i = 0; i <3; i++) {
			if (moveSlot[i]) {
				if(loc[i]<432) {
					loc[i] += speed[i];
				}
			}else {
				loc[i] = 0;
			}
		
		if(stopSlot[i]) {
			if((loc[i]/64)==hit[i]) {
				loc[i] = loc[i]/64*64;
			}
		}
		if(loc[i] < 320) {
			offG.drawImage(slot, i*64, 0-loc[i], this);
		} else {
			offG.drawImage(slot, i*64, 0-loc[i], this);
			offG.drawImage(slot, i*64, 448-loc[i], this);
			
		}
	}
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== startButton) {
			stopSlot[0] = stopSlot[1] = stopSlot[2] = false;
			moveSlot[0] = moveSlot[1] = moveSlot[2] = true;
			slotNum = 0;
			
		} else if(e.getSource()== stopButton) {
			hit[slotNum] = Math.abs(r.nextInt() % 9);
			stopSlot[slotNum] = true;
			if (slotNum<2) {
				slotNum ++;
			} else {
				slotNum = 0;
			}
			
		}
	}
	
	public void stop() {
		if((clock!= null)&&(clock.isAlive())) {
			clock= null;//시계정지
					}
		
	}
	
	public void destroy() {
		//종료루틴
	}
	
		
}
	
	
	


