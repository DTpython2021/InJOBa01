package muhan_java_01;
import java.applet.*;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/////////
//test

public class SlotMachine extends Applet implements Runnable, ActionListener {
	public static final int SCREEN_WIDTH = 780;
	public static final int SCREEN_HEIGHT = 1200;
	public static final int ITEM_SIZE = 200;
	Thread clock;
	
	Image off;
	Graphics offG;
	
	Image slot;
	Image machine;
	
	Random r;
	
	int[] loc;
	int[] speed;
	int[] hit;
	int slotNum;
	boolean[] stopSlot;
	boolean[] moveSlot;
	
	Button startButton, stopButton, homeButton;
//	Panel buttonPanel;
	GridLayout grid = new GridLayout(3, 1);
	Panel buttonPanel = new Panel(grid);


	public void init() {

		//메모리상의 가상화면 만들기
		off = createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
		offG = off.getGraphics();
		offG.setColor(Color.white);
		offG.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
		
		// 이미지 로드
		MediaTracker tracker = new MediaTracker(this);
		
		slot = Toolkit.getDefaultToolkit().getImage("D:\\abc\\MySources\\image\\ImageSlot\\slotsymbol7777.png");
		tracker.addImage(slot,  0);
		
		machine = Toolkit.getDefaultToolkit().getImage("D:\\abc\\MySources\\image\\ImageSlot\\machine_Bigsize.png"); //투명한 이미지 사용할 것!
		tracker.addImage(machine, 0);

//		// 배경 음악
//		Runnable slotmusic = new slotMusic();
//		Thread musicThread = new Thread(slotmusic);
//		musicThread.start();
		
		try {
			tracker.waitForAll();
		} catch (InterruptedException ie) {	}
		
		while ((tracker.statusAll(true) & MediaTracker.COMPLETE) == 0) {}

		//GUI
		setLayout(new BorderLayout());

//		Frame buttonFrame = new Frame();
		buttonPanel = new Panel(grid);
//		buttonFrame.setBounds(600, 300, 200, 600);
//		buttonPanel.setBounds(600,300, 190, 500 );
//		buttonFrame.add(buttonPanel);

//		buttonFrame.add(buttonPanel);
//		buttonFrame.setVisible(true);	
		
		startButton = new Button("당겨라!");
//		startButton.setBounds(50, 50, 180, 100 );
		startButton.addActionListener(this);
		buttonPanel.add(startButton);
		
		stopButton = new Button("선  택!");
//		stopButton.setBounds(50, 200, 180, 100 );
		stopButton.addActionListener(this);
		buttonPanel.add(stopButton);

		homeButton = new Button("집가자!");
//		homeButton.setBounds(50, 350, 180, 100 );
		homeButton.addActionListener(this);
		buttonPanel.add(homeButton);
		
		add("East", buttonPanel);
		
		loc = new int[3];
		speed = new int[3];
		hit = new int[3];
		stopSlot = new boolean[3];
		moveSlot = new boolean[3];
		
		r = new Random();
		
		for(int i = 0; i < 3; i++) {
			loc[i] = Math.abs(r.nextInt() % 8) * ITEM_SIZE;
			speed[i] = Math.abs(r.nextInt() % 8) * 9 + 9;
			stopSlot[i] = true;
			moveSlot[i] = false;
		}
		slotNum = 0;
	}
	
	public void start() {
		if(clock==null) {
			clock = new Thread(this);
			clock.start(); //시계시작
		}
	}
	
	public void paint(Graphics g) {
		//가상화면을 실제 화면에 출력
		g.drawImage(off,  0,  0,  this);
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	public void run() {

		while(true) {
			try {
				clock.sleep(30);
			} catch (InterruptedException ie) {}
		
			offG.setColor(Color.white);
			offG.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			
//			playMusic();

			drawSlot();
			offG.drawImage(machine,  0,  0,  this);
			
			repaint(); // paint() 호출
			
		}
	}
	
//	public void playMusic() {
//		// 배경 음악	
//		AudioClip sound;
//		try {
//			URL url = new URL("file:///D:/abc/MySources/image/ImageSlot/1_bgm.wav");
//			sound = Applet.newAudioClip(url);
//			sound.play();
//		} catch (Exception ie) {}
//	}
	
	public void drawSlot() {
		for(int i = 0; i < 3; i++) {
			if(moveSlot[i]) {
				if(loc[i] < (ITEM_SIZE  * 9) ) {
					loc[i] += speed[i];
//					System.out.println("moveSlot loc[" + i + "] = " + loc[i]);
				} else {
					loc[i] = 0;
				}
			}
			if(stopSlot[i]) {
				if((loc[i]/ITEM_SIZE) == hit[i]) {
//					System.out.println("stopSlot loc[" + i + "] = " + loc[i]);
					loc[i] = loc[i]/ITEM_SIZE * ITEM_SIZE;
					moveSlot[i] = false;
				}
			}
			
			if(loc[i] < (ITEM_SIZE * 6)) {
				offG.drawImage(slot,  i*ITEM_SIZE,  0 - loc[i], this);
//				System.out.println("if location loc[" + i + "] = " + loc[i]);
			} else {
				offG.drawImage(slot,  i*ITEM_SIZE,  0 - loc[i], this);
				offG.drawImage(slot,  i*ITEM_SIZE,  (ITEM_SIZE * 8) - loc[i],  this);
//				System.out.println("else location loc[" + i + "] = " + loc[i]);
			}
		}
	}
	@SuppressWarnings("null")
	public void actionPerformed(ActionEvent e) {
		Runnable slotmusic ;
		Thread musicThread = null ;

		if(e.getSource() == startButton) {
			stopSlot[0] = stopSlot[1] = stopSlot[2] = false;
			moveSlot[0] = moveSlot[1] = moveSlot[2] = true;
			slotNum = 0;
//			// 배경 음악
			slotmusic = new slotMusic();
			musicThread = new Thread(slotmusic);
			musicThread.start();
			
		} else if (e.getSource() == stopButton) {
			hit[slotNum] = Math.abs(r.nextInt() % 8);
			stopSlot[slotNum] = true;
			
			if(slotNum < 2) {
				slotNum ++;
			} else {
				slotNum = 0;
				slotmusic = new slotMusic();
				musicThread = new Thread(slotmusic);
				try {
					musicThread.sleep(1);
					musicThread.interrupt();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}
		} else if (e.getSource() == homeButton) {
//			stop();
			destroy();
		} 
	}
	
	public void stop() {
		if(( clock != null) && (clock.isAlive())) {
			clock = null; //시계정지(없앰)
		}
	}
	
//	public void destroy() {
//		// 종료 루틴
//	}
}
		
	