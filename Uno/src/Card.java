import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Card extends JLabel {
	private static ImageIcon back;
	private ImageIcon front;
	private Color color;
	private Type type;
	private Timer timer;
	private TimerTask task;
	public Card(Color color, Type type, ImageIcon back, ImageIcon front) {
		this.color = color;
		this.type = type;
		Card.back = back;
		this.front = front;
		setBounds(0,0,100,150);
		close();
	}
	public String toString() {
		return "Card: "+type+" "+color;
	}
	public Color getColor() {
		return color;
	}
	public Type getType() {
		return type;
	}
	public void open() {
		setIcon(front);
	}
	public void close() {
		setIcon(back);
	}
	public void goToPosition(int x, int y) {
		Card card = this;
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				int currentX = getX();
				int currentY = getY();
				int xDiff = x - currentX;
				int yDiff = y - currentY;
				int hypotenuse = (int) Math.sqrt(Math.pow(xDiff,2)+Math.pow(yDiff,2));
				if(hypotenuse>=10) {
					int xDist = 10 * xDiff / hypotenuse;
					int yDist = 10 * yDiff / hypotenuse;
					card.setLocation(currentX+xDist,currentY+yDist);
				}else {
					card.setLocation(x,y);
					timer.cancel();
				}
			}
		};
		timer.schedule(task,5,5);
	}
}
