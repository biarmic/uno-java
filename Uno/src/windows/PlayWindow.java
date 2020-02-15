package windows;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class PlayWindow extends Window {
	public PlayWindow() {
		super(900, 500);
		Text numberLabel = new Text("CHOOSE THE NUMBER OF PLAYERS", JLabel.CENTER, 50, 50, 80, 800, 100);
		add(numberLabel, JLayeredPane.DEFAULT_LAYER);
		Button two = new Button(150, 270, 100, 100, "2");
		Button three = new Button(400, 270, 100, 100, "3");
		Button four = new Button(650, 270, 100, 100, "4");
		two.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.createPlayground(2);
				Screen.removeFromGlassPane(PlayWindow.this);
				two.setBorder(null);
			}
		});
		three.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.createPlayground(3);
				Screen.removeFromGlassPane(PlayWindow.this);
				three.setBorder(null);
			}
		});
		four.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.createPlayground(4);
				Screen.removeFromGlassPane(PlayWindow.this);
				four.setBorder(null);
			}
		});
		add(two, JLayeredPane.DEFAULT_LAYER);
		add(three, JLayeredPane.DEFAULT_LAYER);
		add(four, JLayeredPane.DEFAULT_LAYER);

	}
}