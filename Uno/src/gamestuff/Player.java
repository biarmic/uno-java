package gamestuff;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import managers.AudioManager;
import managers.GameManager;
import windows.Screen;

public class Player extends JLabel {
	private final ArrayList<Card> hand = new ArrayList<Card>();
	private boolean isComputer;
	private Direction direction;

	public Player(boolean isComputer, Direction direction) {
		this.isComputer = isComputer;
		this.direction = direction;
		int x = direction == Direction.east ? 1915 : 0;
		int y = direction == Direction.south ? 1075 : 0;
		setOpaque(true);
		setBackground(null);
		setBounds(GameManager.calculateRectangle(x, y, direction.isPole() ? GameManager.WIDTH : 5, direction.isPole() ? 5 : GameManager.HEIGHT));
	}

	public boolean isComputer() {
		return isComputer;
	}

	public int cardCount() {
		return hand.size();
	}

	public void draw(Card card) {
		card.setOwner(this);
		hand.add(card);
		swipeCards();
	}

	public void play(Card card) {
		if (Screen.playground.canPlay(card.getColor(), card.getType())) {
			hand.remove(card);
			swipeCards();
			Screen.playground.playCard(card);
			if (card.getColor() == CardColor.wild)
				if (isComputer) {
					int[] count = new int[4];
					for (Card counting : hand)
						if (card.getColor() != CardColor.wild)
							count[counting.getColor().index()]++;
					int max = 0;
					int maxIndex = -1;
					for (int i = 0; i < 4; i++)
						if (count[i] >= max) {
							max = count[i];
							maxIndex = i;
						}
					Screen.playground.setWildColor(CardColor.values()[maxIndex]);
				} else
					Screen.addToGlassPane(Screen.color);
			else
				Screen.playground.endTurn();

		}
	}

	public Card findPlayable() {
		int[] count = new int[4];
		for (Card card : hand)
			if (card.getColor() != CardColor.wild)
				count[card.getColor().index()]++;
		CardColor[] colors = new CardColor[5];
		colors[4] = CardColor.wild;
		int index = 0;
		while (index < 4) {
			int max = 0;
			int maxIndex = -1;
			for (int i = 0; i < 4; i++) {
				boolean found = false;
				for (int j = 0; j < index; j++)
					if (colors[j].index() == i) {
						found = true;
						break;
					}
				if (found)
					continue;
				if (max <= count[i]) {
					max = count[i];
					maxIndex = i;
				}
			}
			colors[index++] = CardColor.values()[maxIndex];
		}
		for (CardColor color : colors)
			for (Card card : hand)
				if (card.getColor() == color && ((Playground) getParent()).canPlay(color, card.getType()))
					return card;
		return null;
	}

	public void sort() {
		hand.sort(null);
		AudioManager.playSound("swipe");
		swipeCards();
	}

	public void swipeCards() {
		Point center = direction.getPoint();
		int size = hand.size();
		for (int i = 0; i < size; i++) {
			Card card = hand.get(i);
			((JLayeredPane) card.getParent()).setLayer(card, Playground.HAND_LAYER, 0);
			if (direction == Direction.north || direction == Direction.south)
				card.moveTo(new Point(center.x + (int) (38 * GameManager.WIDTH_SCALE) * (i - size / 2), center.y), true);
			else
				card.moveTo(new Point(center.x, center.y + (int) (20 * GameManager.HEIGHT_SCALE) * (i - size / 2)), true);
		}
	}

}