package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import managers.AudioManager;
import managers.GameManager;

public class Button extends JLabel {
	public static final Color BACKGROUND_COLOR = new Color(184, 6, 6);
	public static final Color BORDER_COLOR = Color.white;
	public static LineBorder BORDER;
	private String[] array;
	private int index;
	private boolean disabled = false;

	public Button(int x, int y, int width, int height, int index, String... array) {
		super(array.length == 0 ? "" : array[index], JLabel.CENTER);
		this.array = array;
		this.index = index;
		setBounds(GameManager.calculateRectangle(x, y, width, height));
		setFont(new Font("arial", Font.BOLD, getHeight() / 2));
		setOpaque(true);
		setBackground(BACKGROUND_COLOR);
		setForeground(Color.white);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent event) {
				Button.this.setBorder(BORDER);
			}

			@Override
			public void mouseExited(MouseEvent event) {
				Button.this.setBorder(null);
			}

			@Override
			public void mouseClicked(MouseEvent event) {
				AudioManager.playSound("click");
				if (!disabled && array.length != 0) {
					Button.this.index = (Button.this.index + 1) % array.length;
					Button.this.setText(array[Button.this.index]);
				}
			}
		});
	}

	public Button(int x, int y, int width, int height, String... array) {
		this(x, y, width, height, 0, array);
	}

	public Button(int x, int y, int width, int height, int index, Color color, String... array) {
		this(x, y, width, height, index, array);
		setBackground(color);
	}

	public Button(int x, int y, int width, int height, Color color, String... array) {
		this(x, y, width, height, 0, array);
		setBackground(color);
	}

	public int index() {
		return index;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public void setIndex(int index) {
		this.index = index;
		setText(array[index]);
	}
}