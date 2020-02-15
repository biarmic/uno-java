package windows;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class EndWindow extends Window {
	private Text message;
	private boolean isWin;

	public EndWindow() {
		super(1100, 400);
		message = new Text("", JLabel.CENTER, 50, 50, 50, 1000, 100);
		Button newGame = new Button(100, 200, 400, 100, "NEW GAME");
		Button menu = new Button(600, 200, 400, 100, "MENU");
		add(message, JLayeredPane.DEFAULT_LAYER);
		add(newGame, JLayeredPane.DEFAULT_LAYER);
		add(menu, JLayeredPane.DEFAULT_LAYER);
		newGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.addToGlassPane(Screen.play);
			}
		});
		menu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.showMenu();
			}
		});
	}

	public boolean isWin() {
		return isWin;
	}

	public void updateText(boolean isWin) {
		this.isWin = isWin;
		message.setText(isWin ? "YOU WIN" : "YOU LOST");
	}

}