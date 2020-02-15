package windows;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import gamestuff.CardColor;

public class ColorWindow extends Window {
	public static final Color[] COLORS = { new Color(85, 85, 255), new Color(255, 85, 85), new Color(255, 170, 0), new Color(85, 170, 85) };

	public ColorWindow() {
		super(1050, 400);
		Text label = new Text("CHOOSE A COLOR", JLabel.CENTER, 50, 50, 50, 950, 100);
		Button blue = new Button(50, 200, 200, 100, COLORS[0]);
		Button red = new Button(300, 200, 200, 100, COLORS[1]);
		Button yellow = new Button(550, 200, 200, 100, COLORS[2]);
		Button green = new Button(800, 200, 200, 100, COLORS[3]);
		add(label, JLayeredPane.DEFAULT_LAYER);
		add(blue, JLayeredPane.DEFAULT_LAYER);
		add(red, JLayeredPane.DEFAULT_LAYER);
		add(yellow, JLayeredPane.DEFAULT_LAYER);
		add(green, JLayeredPane.DEFAULT_LAYER);
		blue.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.playground.setWildColor(CardColor.blue);
				Screen.removeFromGlassPane(ColorWindow.this);
				blue.setBorder(null);
			}
		});
		red.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.playground.setWildColor(CardColor.red);
				Screen.removeFromGlassPane(ColorWindow.this);
				red.setBorder(null);
			}
		});
		yellow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.playground.setWildColor(CardColor.yellow);
				Screen.removeFromGlassPane(ColorWindow.this);
				yellow.setBorder(null);
			}
		});
		green.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.playground.setWildColor(CardColor.green);
				Screen.removeFromGlassPane(ColorWindow.this);
				green.setBorder(null);
			}
		});
	}
}