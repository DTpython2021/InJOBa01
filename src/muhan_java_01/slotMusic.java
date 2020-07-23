package muhan_java_01;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

// 음원 소스 https://www.wavsource.com/sfx/sfx2.htm

public class slotMusic extends Applet implements Runnable {

	public void run() {
		AudioClip sound;
		try {
			while (true) {
			URL url = new URL("file:///D:/abc/MySources/image/ImageSlot/projector.wav");
			sound = Applet.newAudioClip(url);
			sound.play();
			Thread.sleep(5000); }
		} catch (Exception e) {
			System.out.println("URL 주소 틀림 (예외처리)");
		}
	}
}
