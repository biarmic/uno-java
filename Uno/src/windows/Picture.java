package windows;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import managers.GameManager;

public class Picture extends ImageIcon {
	private String fileName;

	public Picture(String fileName) {
		this.fileName = fileName;
		readFile();
	}

	public void readFile() {
		try {
			setImage(ImageIO.read(MainMenu.class.getResource(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Rectangle scaledFrame = GameManager.calculateRectangle(0, 0, getIconWidth(), getIconHeight());
		setImage(getImage().getScaledInstance(scaledFrame.width, scaledFrame.height, Image.SCALE_SMOOTH));
	}
}
