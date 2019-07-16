import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Hand {
	private static GUI gui;
	private static Logic logic;
	private static ArrayList<Card> deck;
	private static ArrayList<Card> stack;
	private ArrayList<Card> cards = new ArrayList<Card>();
	private Position position;
	private int blueIndex = 0;
	private int redIndex = 0;
	private int greenIndex = 0;
	private int yellowIndex = 0;
	private boolean justDrawn = false;
	public Hand(GUI gui, Logic logic, ArrayList<Card> deck, ArrayList<Card> stack, Position position) {
		Hand.gui = gui;
		Hand.logic = logic;
		Hand.deck = deck;
		Hand.stack = stack;
		this.position = position;
	}
	public boolean getJustDrawn() {
		return justDrawn;
	}
	public void setJustDrawn(boolean justDrawn) {
		this.justDrawn = justDrawn;
	}
	public void draw(int numCards) {
		if(deck.size()<numCards)
			logic.reshuffle();
		for(int i = 0; i < numCards; i++) {
			Card card = deck.remove(0);
			if(position==Position.South) {
				card.open();
				card.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent event) {
						if(logic.getTurn()==Position.South && logic.canPlay(card)) {
							if(card.getType()==Type.Wild || card.getType()==Type.WildDraw)
								logic.setWildColor(Color.NotChosen);
							playCard(card);
							card.removeMouseListener(this);
						}
					}
				});
			}
			Color color = card.getColor();
			if(color==Color.Blue) {
				cards.add(blueIndex,card);
				blueIndex++;
				redIndex++;
				greenIndex++;
				yellowIndex++;
			}else if(color==Color.Red) {
				cards.add(redIndex,card);
				redIndex++;
				greenIndex++;
				yellowIndex++;
			}else if(color==Color.Green) {
				cards.add(greenIndex,card);
				greenIndex++;
				yellowIndex++;
			}else if(color==Color.Yellow) {
				cards.add(yellowIndex,card);
				yellowIndex++;
			}else
				cards.add(card);
		}
		adjustCards();
	}
	private void adjustCards() {
		for(int i = 0; i < cards.size(); i++) {
			if(position==Position.South || position==Position.North) {
				gui.setLayer(cards.get(i),new Integer(i+1));
				cards.get(i).goToPosition(750-50*(cards.size()/2)+50*i,position==Position.South ? 760 : 50);
			}else {
				gui.setLayer(cards.get(i),new Integer(i+1));
				cards.get(i).goToPosition(position==Position.East ? 1450 : 50,455-25*(cards.size()/2)+25*i);
			}
		}
	}
	private void playCard(Card card) {
		stack.add(card);
		cards.remove(card);
		gui.setLayer(card,new Integer(stack.size()));
		if(position!=Position.South)
			card.open();
		card.goToPosition(750,405);
		Color color = card.getColor();
		if(color==Color.Blue) {
			blueIndex--;
			redIndex--;
			greenIndex--;
			yellowIndex--;
		}else if(color==Color.Red) {
			redIndex--;
			greenIndex--;
			yellowIndex--;
		}else if(color==Color.Green) {
			greenIndex--;
			yellowIndex--;
		}else if(color==Color.Yellow)
			yellowIndex--;
		if(card.getType()==Type.DrawTwo) {
			logic.addDrawing(2);
			logic.setMustDraw();
		}else if(card.getType()==Type.WildDraw) {
			logic.addDrawing(4);
			logic.setMustDraw();
		}
		adjustCards();
		if(position==Position.South)
			gui.showDraw();
		if(cards.size()==0)
			gui.gameOver(position==Position.South ? true : false);
		else if(deck.size()==0) {
			logic.reshuffle();
			logic.setPlayed();
		}else
			logic.setPlayed();
	}
	public void play() {
		for(int i = 0; i < cards.size(); i++) {
			if(logic.canPlay(cards.get(i))) {
				playCard(cards.get(i));
				if(logic.getWildColor()!=null)
					logic.setWildColor(null);
				if(stack.get(stack.size()-1).getType()==Type.Wild || stack.get(stack.size()-1).getType()==Type.WildDraw)
					logic.setWildColor(Color.values()[(int) (Math.random()*4)]);
				justDrawn = false;
				return;
			}
		}
		if(!justDrawn && logic.getDrawing()==0) {
			logic.draw();
			justDrawn = true;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			play();
			return;
		}else if(!justDrawn)
			logic.draw();
		else
			justDrawn = false;
		logic.setSkipped();
	}
}
