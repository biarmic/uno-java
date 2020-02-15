package windows;

import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;

import managers.GameManager;

import java.awt.Color;

public abstract class Window extends JLayeredPane {
	public static final Color BACKGROUND_COLOR = new Color(19, 60, 125);
	public static LineBorder BORDER;

	public Window(int width, int height) {
		this();
		setBounds(GameManager.calculateRectangle((1920 - width) / 2, (1080 - height) / 2, width, height));
		setPreferredSize(getSize());
	}

	private Window() {
		setBackground(BACKGROUND_COLOR);
		setBorder(BORDER);
		setOpaque(true);
	}
}