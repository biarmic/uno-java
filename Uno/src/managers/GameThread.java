package managers;

import windows.Screen;

public class GameThread extends Thread {
	@Override
	public void run() {
		try {
			while (!Screen.playground.isGameOver()) {
				if (Screen.playground.isTurnEnded()) {
					Screen.playground.nextTurn();
				}
				sleep(1000);
			}
			AudioManager.playSound(Screen.end.isWin() ? "win" : "lose");
			Screen.addToGlassPane(Screen.end);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}