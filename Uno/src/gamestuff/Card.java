package gamestuff;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import managers.GameManager;

public class Card extends JLabel implements Comparable<Card> {
	private static ImageIcon[][] colorCards = new ImageIcon[4][14];
	private static ImageIcon[] wildCards = new ImageIcon[2];
	private static ImageIcon cardBack;
	public static Rectangle FRAME;
	private Timer timer = new Timer();
	private TimerTask task;
	private boolean isMoving = false;
	private CardColor color;
	private CardType type;
	private int id;
	private Player owner = null;
	private boolean isRevealed = false;

	public Card(CardColor color, CardType type) {
		this.color = color;
		this.type = type;
		id = color.index() * 100 + type.index();
		setIcon();
		addMouseListener(new MouseAdapter() {
			private boolean isActive = true;

			@Override
			public void mouseEntered(MouseEvent event) {
				if (!isMoving && owner != null && !owner.isComputer())
					setLocation(getLocation().x, getLocation().y - (int) (40 * GameManager.HEIGHT_SCALE));
				else if (isMoving)
					isActive = false;
			}

			@Override
			public void mouseExited(MouseEvent event) {
				if (isActive && !isMoving && owner != null && !owner.isComputer())
					setLocation(getLocation().x, getLocation().y + (int) (40 * GameManager.HEIGHT_SCALE));
				isActive = true;
			}

			@Override
			public void mouseClicked(MouseEvent event) {
				if (!isMoving && owner != null && !owner.isComputer() && ((Playground) getParent()).getTurnIndex() == 0)
					owner.play(Card.this);
			}
		});
	}

	@Override
	public int compareTo(Card compare) {
		return (int) Math.signum(id - compare.id);
	}

	public CardColor getColor() {
		return color;
	}

	public CardType getType() {
		return type;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public void setRevealed(boolean isRevealed) {
		this.isRevealed = isRevealed;
		setIcon();
	}

	public void setIcon() {
		if (!isRevealed)
			setIcon(cardBack);
		else if (color == CardColor.wild)
			setIcon(wildCards[type.index() / 11]);
		else
			setIcon(colorCards[color.index()][type.index()]);
		setBounds(getLocation().x, getLocation().y, FRAME.width, FRAME.height);
	}

	public void moveTo(Point destination, boolean isForced) {
		if (task != null)
			task.cancel();
		task = new TimerTask() {
			@Override
			public void run() {
				Point current = getLocation();
				double distance = Point.distance(current.x, current.y, destination.x, destination.y);
				if (distance > 6) {
					isMoving = true;
					int xDiff = destination.x - current.x;
					int yDiff = destination.y - current.y;
					if (isForced)
						setLocation((int) (current.x + (distance / 5) * xDiff / distance), (int) (current.y + (distance / 5) * yDiff / distance));
					else
						setLocation((int) (current.x + 5 * xDiff / distance), (int) (current.y + 5 * yDiff / distance));
				} else {
					setLocation(destination);
					isMoving = false;
					cancel();
					task = null;
				}
			}
		};
		timer.schedule(task, 5, 5);
	}

	public static void loadCards() {
		FRAME = GameManager.calculateRectangle(0, 0, 100, 150);
		try {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 13; j++) {
					colorCards[i][j] = new ImageIcon(ImageIO.read(Card.class.getResource("/cards/" + CardColor.values()[i] + "-" + CardType.values()[j] + ".png")));
					colorCards[i][j] = new ImageIcon(colorCards[i][j].getImage().getScaledInstance(FRAME.width, FRAME.height, Image.SCALE_SMOOTH));
				}
			}
			wildCards[0] = new ImageIcon(ImageIO.read(Card.class.getResource("/cards/wild-draw.png")));
			wildCards[1] = new ImageIcon(ImageIO.read(Card.class.getResource("/cards/wild.png")));
			cardBack = new ImageIcon(ImageIO.read(Card.class.getResource("/cards/back.png")));
			wildCards[0] = new ImageIcon(wildCards[0].getImage().getScaledInstance(FRAME.width, FRAME.height, Image.SCALE_SMOOTH));
			wildCards[1] = new ImageIcon(wildCards[1].getImage().getScaledInstance(FRAME.width, FRAME.height, Image.SCALE_SMOOTH));
			cardBack = new ImageIcon(cardBack.getImage().getScaledInstance(FRAME.width, FRAME.height, Image.SCALE_SMOOTH));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}