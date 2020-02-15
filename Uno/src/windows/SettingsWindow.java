package windows;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import managers.AudioManager;
import managers.GameManager;
import managers.Resolution;

public class SettingsWindow extends Window {
	private boolean changed = false;

	public SettingsWindow() {
		super(800, 550);
		add(new Text("FULLSCREEN", JLabel.LEFT, 50, 70, 60, 400, 100), JLayeredPane.DEFAULT_LAYER);
		add(new Text("RESOLUTION", JLabel.LEFT, 50, 70, 160, 400, 100), JLayeredPane.DEFAULT_LAYER);
		add(new Text("SOUND", JLabel.LEFT, 50, 70, 260, 400, 100), JLayeredPane.DEFAULT_LAYER);
		Button fullscreen = new Button(490, 75, 220, 70, GameManager.FULLSCREEN ? 0 : 1, "ON", "OFF");
		Button resolution = new Button(490, 175, 220, 70, GameManager.RESOLUTION.indexOf(), "1920 × 1080", "1600 × 900", "1366 × 768", "1280 × 720", "640 × 360");
		Button sound = new Button(490, 275, 220, 70, AudioManager.SOUND ? 0 : 1, "ON", "OFF");
		Button close = new Button(290, 400, 220, 100, "CLOSE");
		add(fullscreen, JLayeredPane.DEFAULT_LAYER);
		add(resolution, JLayeredPane.DEFAULT_LAYER);
		add(sound, JLayeredPane.DEFAULT_LAYER);
		add(close);
		fullscreen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				GameManager.FULLSCREEN = !GameManager.FULLSCREEN;
				if (GameManager.FULLSCREEN) {
					GameManager.RESOLUTION = Resolution.values()[0];
					resolution.setIndex(0);
				}
				GameManager.writeSettings();
				resolution.setDisabled(GameManager.FULLSCREEN);
				changed = true;
			}
		});
		resolution.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (!GameManager.FULLSCREEN) {
					GameManager.RESOLUTION = Resolution.values()[resolution.index()];
					GameManager.writeSettings();
					changed = true;
				}
			}
		});
		sound.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				AudioManager.SOUND = !AudioManager.SOUND;
				GameManager.writeSettings();
				changed = true;
			}
		});
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.removeFromGlassPane(SettingsWindow.this);
				((JLabel) event.getSource()).setBorder(null);
				if (changed) {
					changed = false;
					double widthScale = GameManager.WIDTH_SCALE;
					double heightScale = GameManager.HEIGHT_SCALE;
					GameManager.writeSettings();
					GameManager.readSettings();
					GameManager.updateComponents(widthScale, heightScale);
				}
			}
		});
		resolution.setDisabled(GameManager.FULLSCREEN);
	}
}
