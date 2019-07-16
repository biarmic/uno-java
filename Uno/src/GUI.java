import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class GUI extends JLayeredPane {
	private Logic logic = new Logic(this);
	private JLabel draw = new JLabel();
	private JLabel skip = new JLabel();
	private JLabel blue = new JLabel();
	private JLabel red = new JLabel();
	private JLabel green = new JLabel();
	private JLabel yellow = new JLabel();
	private JLabel colorLabel = new JLabel();
	private JLabel wildBlue = new JLabel();
	private JLabel wildRed = new JLabel();
	private JLabel wildGreen = new JLabel();
	private JLabel wildYellow = new JLabel();
	private JLabel wildLabel = new JLabel();
	private boolean gameOver = false;
	public GUI(Window window) {
		JLabel background = new JLabel();
		JLabel newGame = new JLabel();
		try {
			background.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/background.png"))));
			newGame.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/new-game-button.png"))));
			draw.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/draw-button.png"))));
			skip.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/skip-button.png"))));
			blue.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/color-blue.png"))));
			red.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/color-red.png"))));
			green.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/color-green.png"))));
			yellow.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/color-yellow.png"))));
			colorLabel.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/choose-a-color.png"))));
			wildBlue.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/wild-blue.png"))));
			wildRed.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/wild-red.png"))));
			wildGreen.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/wild-green.png"))));
			wildYellow.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/wild-yellow.png"))));
			wildLabel.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/play-this-color.png"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		background.setBounds(0,0,1600,960);
		newGame.setBounds(690,247,220,100);
		draw.setBounds(725,604,150,100);
		skip.setBounds(725,604,150,100);
		blue.setBounds(451,604,173,96);
		red.setBounds(626,604,173,96);
		green.setBounds(801,604,173,96);
		yellow.setBounds(976,604,173,96);
		colorLabel.setBounds(610,710,376,32);
		wildBlue.setBounds(580,435,100,100);
		wildRed.setBounds(580,435,100,100);
		wildGreen.setBounds(580,435,100,100);
		wildYellow.setBounds(580,435,100,100);
		wildLabel.setBounds(540,410,186,17);
		add(background,JLayeredPane.DEFAULT_LAYER);
		add(newGame,JLayeredPane.PALETTE_LAYER);
		add(draw,JLayeredPane.PALETTE_LAYER);
		add(skip,JLayeredPane.PALETTE_LAYER);
		draw.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(logic.getTurn()==Position.South && !gameOver)
					logic.draw();
			}
		});
		skip.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(logic.getTurn()==Position.South && !gameOver) {
					logic.skip();
					showDraw();
				}
			}
		});
		newGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				window.newGame();
			}
		});
		blue.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(!gameOver) {
					logic.setWildColor(Color.Blue);
					showDraw();
				}
			}
		});
		red.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(!gameOver) {
					logic.setWildColor(Color.Red);
					showDraw();
				}
			}
		});
		green.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(!gameOver) {
					logic.setWildColor(Color.Green);
					showDraw();
				}
			}
		});
		yellow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(!gameOver) {
					logic.setWildColor(Color.Yellow);
					showDraw();
				}
			}
		});
	}
	public void showColors() {
		if(draw.isShowing())
			remove(draw);
		else if(skip.isShowing())
			remove(skip);
		add(blue,JLayeredPane.PALETTE_LAYER);
		add(red,JLayeredPane.PALETTE_LAYER);
		add(green,JLayeredPane.PALETTE_LAYER);
		add(yellow,JLayeredPane.PALETTE_LAYER);
		add(colorLabel,JLayeredPane.PALETTE_LAYER);
		repaint();
	}
	public void showDraw() {
		if(!draw.isShowing())
			add(draw,JLayeredPane.PALETTE_LAYER);
		if(skip.isShowing())
			remove(skip);
		else
			remove(blue);
			remove(red);
			remove(green);
			remove(yellow);
			remove(colorLabel);
		repaint();
	}
	public void showSkip() {
		remove(draw);
		add(skip,JLayeredPane.PALETTE_LAYER);
		repaint();
	}
	public void showWildColor(Color color) {
		hideWildColor();
		if(color==Color.Blue)
			add(wildBlue,JLayeredPane.PALETTE_LAYER);
		else if(color==Color.Red)
			add(wildRed,JLayeredPane.PALETTE_LAYER);
		else if(color==Color.Green)
			add(wildGreen,JLayeredPane.PALETTE_LAYER);
		else if(color==Color.Yellow)
			add(wildYellow,JLayeredPane.PALETTE_LAYER);
		add(wildLabel,JLayeredPane.PALETTE_LAYER);
	}
	public void hideWildColor() {
		if(wildBlue.isShowing())
			remove(wildBlue);
		if(wildRed.isShowing())
			remove(wildRed);
		if(wildGreen.isShowing())
			remove(wildGreen);
		if(wildYellow.isShowing())
			remove(wildYellow);
		if(wildLabel.isShowing())
			remove(wildLabel);
		repaint();
	}
	public void gameOver(boolean isWin) {
		gameOver = true;
		JLabel gameOverLabel = new JLabel();
		try {
			gameOverLabel.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/you-"+(isWin ? "won" : "lost")+".png"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		gameOverLabel.setBounds(380,370,882,233);
		add(gameOverLabel,new Integer(110));
	}
}
