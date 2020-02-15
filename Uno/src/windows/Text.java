package windows;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import managers.GameManager;

public class Text extends JLabel {
	private int fontSize;

	public Text(String text, int align, int fontSize, int x, int y, int width, int height) {
		super("<html><div align=\"center\">" + text + "</div></html>", align);
		this.fontSize = fontSize;
		setBounds(GameManager.calculateRectangle(x, y, width, height));
		setFont(new Font("arial", Font.BOLD, (int) (fontSize * GameManager.HEIGHT_SCALE)));
		setForeground(Color.white);
	}

	public int getFontSize() {
		return fontSize;
	}
}
