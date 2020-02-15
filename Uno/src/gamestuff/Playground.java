package gamestuff;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import managers.AudioManager;
import managers.GameManager;
import windows.Button;
import windows.ColorWindow;
import windows.Screen;
import windows.Text;

public class Playground extends JLayeredPane {
	public static final Integer PLAYER_LAYER = new Integer(1);
	public static final Integer COLOR_LAYER = new Integer(2);
	public static final Integer BUTTON_LAYER = new Integer(3);
	public static final Integer DECK_LAYER = new Integer(4);
	public static final Integer PILE_LAYER = new Integer(5);
	public static final Integer HAND_LAYER = new Integer(6);
	public static Point DECK_POINT;
	public static Point PILE_POINT;
	private Timer background = new Timer();
	private Player[] players;
	private Stack<Card> deck = new Stack<Card>();
	private Stack<Card> pile = new Stack<Card>();
	private CardColor nextColor = null;
	private int numPlayers;
	private int turnIndex = 0;
	private boolean isClockwise = true;
	private int drawSum = 1;
	private boolean gameOver = false;
	private boolean turnEnded = false;
	private Button actionButton;

	public Playground(int numPlayers) {
		setOpaque(true);
		setBounds(0, 0, GameManager.WIDTH, GameManager.HEIGHT);
		background.schedule(new TimerTask() {
			private float hue = 0;

			@Override
			public void run() {
				hue = ++hue % 360;
				setBackground(Color.getHSBColor(hue / 360, (float) 0.9, (float) 0.25));
			}
		}, 0, 500);
		Text colorText = new Text("NEXT COLOR", JLabel.CENTER, 50, 300, 320, 400, 100);
		JLabel colorBox = new JLabel();
		colorBox.setBounds(GameManager.calculateRectangle(400, 450, 200, 200));
		colorBox.setOpaque(true);
		colorBox.setBorder(Button.BORDER);
		add(colorText, COLOR_LAYER);
		add(colorBox, COLOR_LAYER);
		colorText.setVisible(false);
		colorBox.setVisible(false);
		actionButton = new Button(1250, 300, 350, 80, "DRAW", "END TURN");
		Button sortButton = new Button(1250, 420, 350, 80, "SORT");
		Button settingsButton = new Button(1250, 540, 350, 80, "SETTINGS");
		Button menuButton = new Button(1250, 660, 350, 80, "MAIN MENU");
		add(actionButton, BUTTON_LAYER);
		add(sortButton, BUTTON_LAYER);
		add(settingsButton, BUTTON_LAYER);
		add(menuButton, BUTTON_LAYER);
		actionButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (turnIndex == 0) {
					if (actionButton.index() == 1) {
						int drawSum = Playground.this.drawSum;
						drawCards();
						if (drawSum > 1)
							endTurn();
					} else
						endTurn();
				} else {
					actionButton.setIndex(0);
					event.consume();
				}
			}
		});
		sortButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				players[0].sort();
			}
		});
		settingsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.addToGlassPane(Screen.settings);
			}
		});
		menuButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Screen.showMenu();
			}
		});
		this.numPlayers = numPlayers;
		players = new Player[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			players[i] = new Player(i != 0, Direction.values()[i]);
			add(players[i], PLAYER_LAYER);
		}
		for (CardColor color : CardColor.values())
			for (CardType type : CardType.values()) {
				int count = 2;
				if (color == CardColor.wild && (type == CardType.draw || type == CardType.wild))
					count = 4;
				else if (color == CardColor.wild || type == CardType.wild)
					continue;
				else if (type == CardType.zero)
					count = 1;
				for (int i = 0; i < count; i++) {
					Card card = new Card(color, type);
					deck.add(card);
					card.setLocation(DECK_POINT);
					add(card, HAND_LAYER);
				}
			}
		Collections.shuffle(deck);
		for (int i = 0; i < numPlayers; i++) {
			drawSum = 7;
			drawCards();
			turnIndex = ++turnIndex % numPlayers;
		}
		turnIndex = 0;
		players[1].setBackground(null);
		players[0].setBackground(Color.white);
		playCard(deck.pop());
		turnEnded = pile.peek().getType() == CardType.skip;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isTurnEnded() {
		return turnEnded;
	}

	public int getTurnIndex() {
		return turnIndex;
	}

	public void setWildColor(CardColor color) {
		nextColor = color;
		for (Component comp : getComponentsInLayer(COLOR_LAYER)) {
			if (!(comp instanceof Text))
				comp.setBackground(ColorWindow.COLORS[color.index()]);
			comp.setVisible(true);
		}
		endTurn();
	}

	public boolean canPlay(CardColor color, CardType type) {
		if (drawSum > 1)
			return type == CardType.draw;
		if (nextColor == null)
			return color == CardColor.wild || color == pile.peek().getColor() || type == pile.peek().getType() || pile.peek().getColor() == CardColor.wild;
		else
			return color == CardColor.wild || color == nextColor || type == pile.peek().getType();
	}

	public void playCard(Card card) {
		AudioManager.playSound("play");
		card.moveTo(PILE_POINT, true);
		setLayer(card, PILE_LAYER, 0);
		card.setRevealed(true);
		card.setOwner(null);
		pile.add(card);
		nextColor = null;
		actionButton.setIndex(0);
		for (Component comp : getComponentsInLayer(COLOR_LAYER))
			comp.setVisible(false);
		if (card.getType() == CardType.reverse)
			isClockwise = !isClockwise;
		else if (card.getType() == CardType.skip)
			turnIndex = Math.floorMod(turnIndex + 1 * (isClockwise ? 1 : -1), numPlayers);
		else if (card.getType() == CardType.draw) {
			drawSum += (card.getColor() == CardColor.wild ? 4 : 2) - (drawSum == 1 ? 1 : 0);
		}
	}

	public void drawCards() {
		AudioManager.playSound("draw");
		for (int i = 0; i < drawSum; i++)
			try {
				Card card = deck.pop();
				players[turnIndex].draw(card);
				if (turnIndex == 0)
					card.setRevealed(true);
			} catch (EmptyStackException e) {
				Card top = pile.pop();
				for (int j = 0; j < pile.size(); j++) {
					Card flip = pile.pop();
					flip.setRevealed(false);
					setLayer(flip, PILE_LAYER, 0);
					flip.moveTo(DECK_POINT, true);
					deck.push(flip);
				}
				Collections.shuffle(deck);
				pile.push(top);
				i--;
			}
		drawSum = 1;
	}

	public void endTurn() {
		for (Player player : players) {
			if (player.cardCount() == 0) {
				Screen.end.updateText(!player.isComputer());
				gameOver = true;
			}
			player.setBackground(null);
		}
		turnIndex = Math.floorMod(turnIndex + 1 * (isClockwise ? 1 : -1), numPlayers);
		players[turnIndex].setBackground(Color.white);
		turnEnded = true;
	}

	public void nextTurn() {
		turnEnded = false;
		if (players[turnIndex].isComputer()) {
			Card card = players[turnIndex].findPlayable();
			if (card != null)
				players[turnIndex].play(card);
			else {
				drawCards();
				endTurn();
			}
		}
	}
}