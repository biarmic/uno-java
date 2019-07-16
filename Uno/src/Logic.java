import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Logic {
	private GUI gui;
	private ArrayList<Card> deck = new ArrayList<Card>();
	private ArrayList<Card> stack = new ArrayList<Card>();
	private Hand[] hands = new Hand[4];
	private Timer timer = new Timer();;
	private TimerTask task;
	private Position turn = Position.South;
	private boolean isReversed = false;
	private int drawing = 0;
	private boolean mustDraw = false;
	private boolean played = false;
	private boolean skipped = false;
	private Color wildColor = null;
	public Logic(GUI gui) {
		this.gui = gui;
		ArrayList<ImageIcon> black = new ArrayList<ImageIcon>();
		ArrayList<ImageIcon> blue = new ArrayList<ImageIcon>();
		ArrayList<ImageIcon> red = new ArrayList<ImageIcon>();
		ArrayList<ImageIcon> green = new ArrayList<ImageIcon>();
		ArrayList<ImageIcon> yellow = new ArrayList<ImageIcon>();
		for(int i = 0; i < 4; i++)
			hands[i] = new Hand(gui,this,deck,stack,Position.values()[i]);
		try {
			black.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/back.png"))));
			black.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/wild.png"))));
			black.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/wild-draw.png"))));
			for(int i = 0; i < 10; i++)
				blue.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/blue-"+i+".png"))));
			blue.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/blue-skip.png"))));
			blue.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/blue-reverse.png"))));
			blue.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/blue-draw.png"))));
			for(int i = 0; i < 10; i++)
				red.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/red-"+i+".png"))));
			red.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/red-skip.png"))));
			red.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/red-reverse.png"))));
			red.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/red-draw.png"))));
			for(int i = 0; i < 10; i++)
				green.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/green-"+i+".png"))));
			green.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/green-skip.png"))));
			green.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/green-reverse.png"))));
			green.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/green-draw.png"))));
			for(int i = 0; i < 10; i++)
				yellow.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/yellow-"+i+".png"))));
			yellow.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/yellow-skip.png"))));
			yellow.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/yellow-reverse.png"))));
			yellow.add(new ImageIcon(ImageIO.read(getClass().getResource("cards/yellow-draw.png"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 13; j++) {
				for(int k = 0; k < (j==0 ? 1 : 2); k++)
					deck.add(new Card(Color.values()[i],Type.values()[j],black.get(0),i==0 ? blue.get(j) : i==1 ? red.get(j) : i==2 ? green.get(j) : yellow.get(j)));
			}
		}
		for(int i = 0; i < 4; i++) {
			deck.add(new Card(Color.Black,Type.Wild,black.get(0),black.get(1)));
			deck.add(new Card(Color.Black,Type.WildDraw,black.get(0),black.get(2)));
		}
		Collections.shuffle(deck);
		for(int i = 0; i < deck.size(); i++) {
			gui.add(deck.get(i),new Integer(deck.size()-i));
			deck.get(i).setLocation(900,405);
		}
		for(int i = 0; i < 4; i++)
			hands[i].draw(7);
		while(deck.get(0).getType()==Type.DrawTwo || deck.get(0).getType()==Type.WildDraw || deck.get(0).getType()==Type.Wild)
			Collections.shuffle(deck);
		stack.add(deck.remove(0));
		stack.get(0).open();
		gui.setLayer(stack.get(0),new Integer(stack.size()));
		stack.get(0).goToPosition(750,405);
		if(stack.get(0).getType()==Type.Skip)
			skipped = true;
		else if(stack.get(0).getType()==Type.Reverse)
			isReversed = true;
		task = new TimerTask() {
			@Override
			public void run() {
				if(played || skipped) {
					if(wildColor==Color.NotChosen)
						gui.showColors();
					else {
						if(played) {
							played = false;
							nextPlayer(stack.get(stack.size()-1));
						}else if(skipped) {
							skipped = false;
							nextPlayer();
						}
					}
				}
			}
		};
		timer.schedule(task,1000,1500);
	}
	public Position getTurn() {
		return turn;
	}
	public Color getWildColor() {
		return wildColor;
	}
	public void setMustDraw() {
		mustDraw = true;
	}
	public int getDrawing() {
		return drawing;
	}
	public void setPlayed() {
		played = true;
	}
	public void setSkipped() {
		skipped = true;
	}
	public void setWildColor(Color color) {
		wildColor = color;
		if(color==null || color==Color.NotChosen)
			gui.hideWildColor();
		else
			gui.showWildColor(color);
	}
	public void addDrawing(int add) {
		drawing += add;
	}
	public void draw() {
		if(turn==Position.South) {
			if(!hands[0].getJustDrawn() && drawing==0) {
				hands[0].draw(1);
				gui.showSkip();
			}else if(!hands[0].getJustDrawn()) {
				hands[0].draw(drawing);
				drawing = 0;
				mustDraw = false;
				skipped = true;
			}else {
				hands[0].setJustDrawn(false);
				skipped = true;
			}
		}else {
			if(drawing==0)
				hands[Position.indexOf(turn)].draw(1);
			else {
				hands[Position.indexOf(turn)].draw(drawing);
				mustDraw = false;
				drawing = 0;
			}
		}
	}
	public boolean canPlay(Card card) {
		Card top = stack.get(stack.size()-1);
		if(wildColor==null) {
			if(top.getType()==Type.DrawTwo || top.getType()==Type.WildDraw) {
				if(mustDraw && (card.getType()==Type.DrawTwo || card.getType()==Type.WildDraw))
					return true;
				else if(!mustDraw && (card.getType()==top.getType() || card.getColor()==top.getColor()))
					return true;
			}else {
				if(card.getType()==top.getType() || card.getColor()==top.getColor()) 
					return true;
				else if(card.getColor()==Color.Black)
					return true;
			}
			return false;
		}else {
			if(mustDraw && top.getType()==Type.WildDraw && (card.getType()==Type.DrawTwo || card.getType()==Type.WildDraw))
				return true;
			else if(mustDraw && top.getType()==Type.WildDraw)
				return false;
			else if(!mustDraw && (card.getColor()==wildColor || card.getColor()==Color.Black || card.getType()==top.getType()))
				return true;
			return false;
		}
	}
	public void nextPlayer(Card played) {
		if(played.getType()==Type.Reverse) {
			if(isReversed)
				isReversed = false;
			else
				isReversed = true;
		}
		if(played.getType()==Type.Skip)
			turn = Position.nextPosition(turn,isReversed);
		nextPlayer();
	}
	public void nextPlayer() {
		turn = Position.nextPosition(turn,isReversed);
		if(turn==Position.West)
			hands[1].play();
		else if(turn==Position.North)
			hands[2].play();
		else if(turn==Position.East)
			hands[3].play();
	}
	public void skip() {
		skipped = true;
	}
	public void reshuffle() {
		while(stack.size()==1) {
			stack.get(0).close();
			stack.get(0).goToPosition(900,405);
			deck.add(stack.remove(0));
		}
		Collections.shuffle(deck);
	}
}
