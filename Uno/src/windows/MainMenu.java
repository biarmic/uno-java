package windows;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import managers.GameManager;

public class MainMenu extends JLayeredPane {
	private static Integer BUTTON_LAYER = new Integer(1);

	public MainMenu() {
		JLabel background = new JLabel();
		JLabel logo = new JLabel();
		background.setBounds(GameManager.calculateRectangle(0, 0, 1920, 1080));
		logo.setBounds(GameManager.calculateRectangle(1000, 300, 700, 440));
		background.setIcon(new Picture("/images/background.png"));
		logo.setIcon(new Picture("/images/logo.png"));
		Button single = new Button(200, 315, 500, 110, "PLAY");
		Button settings = new Button(200, 475, 500, 110, "SETTINGS");
		Button exit = new Button(200, 635, 500, 110, "EXIT");
		single.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.addToGlassPane(Screen.play);
			}
		});
		settings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.addToGlassPane(Screen.settings);
			}
		});
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				System.exit(0);
			}
		});
		add(background, JLayeredPane.DEFAULT_LAYER);
		add(logo, BUTTON_LAYER);
		add(single, BUTTON_LAYER);
		add(settings, BUTTON_LAYER);
		add(exit, BUTTON_LAYER);
		setOpaque(true);
		setBackground(Window.BACKGROUND_COLOR);
		setBounds(GameManager.calculateRectangle(0, 0, 1920, 1080));
	}
}