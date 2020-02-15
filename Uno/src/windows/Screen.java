package windows;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import gamestuff.Playground;
import managers.AudioManager;
import managers.GameManager;

public class Screen extends JFrame {
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static Screen screen;
	private static GameManager gameManager = new GameManager();
	private static AudioManager audioManager = new AudioManager();
	private static JLayeredPane currentPane;
	public static final MainMenu mainMenu = new MainMenu();
	public static final SettingsWindow settings = new SettingsWindow();
	public static final PlayWindow play = new PlayWindow();
	public static final ColorWindow color = new ColorWindow();
	public static final EndWindow end = new EndWindow();
	public static Playground playground;
	private static JPanel glass;
	private static GridBagConstraints gbc = new GridBagConstraints();

	public Screen() {
		screen = this;
		glass = ((JPanel) getGlassPane());
		setUndecorated(true);
		if (GameManager.FULLSCREEN)
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		else {
			Container container = new Container();
			container.setPreferredSize(GameManager.DIMENSION);
			setContentPane(container);
			pack();
			setLocation((SCREEN_WIDTH - GameManager.WIDTH) / 2, (SCREEN_HEIGHT - GameManager.HEIGHT) / 2);
		}
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new Picture("/images/icon-logo.png").getImage());
		currentPane = mainMenu;
		setLayeredPane(currentPane);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ESCAPE)
					if (isInGlassPane(settings))
						removeFromGlassPane(settings);
					else if (isInGlassPane(play))
						removeFromGlassPane(play);
					else if (currentPane == mainMenu && ((JPanel) screen.getGlassPane()).getComponentCount() == 0)
						System.exit(0);
			}
		});
		getGlassPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getSource() instanceof JLabel && ((JLabel) event.getSource()).getParent() != getGlassPane())
					event.consume();
			}
		});
		glass.setLayout(new GridBagLayout());
		setVisible(true);
	}

	public static void addToGlassPane(JLayeredPane pane) {
		for (Component comp : glass.getComponents()) {
			glass.remove(comp);
			currentPane.add(comp, new Integer(currentPane.highestLayer() + 1));
		}
		glass.add(pane, gbc);
		glass.setVisible(true);
		currentPane.repaint();
		glass.repaint();
	}

	public static void removeFromGlassPane(JLayeredPane pane) {
		glass.remove(pane);
		for (Component comp : currentPane.getComponentsInLayer(currentPane.highestLayer()))
			if (comp instanceof Window) {
				currentPane.remove(comp);
				glass.add(comp, gbc);
				break;
			}
		currentPane.repaint();
		glass.repaint();
		glass.setVisible(glass.getComponents().length != 0);
	}

	public static boolean isInGlassPane(Component search) {
		for (Component comp : glass.getComponents())
			if (search == comp)
				return true;
		return false;
	}

	public static void clearGlassPane() {
		for (Component comp : glass.getComponents())
			glass.remove(comp);
		currentPane.repaint();
		glass.repaint();
		glass.setVisible(false);
	}

	public static void setClickable(boolean clickable) {
		glass.setVisible(!clickable);
	}

	private static void updatePane() {
		currentPane.repaint();
		screen.setLayeredPane(currentPane);
		screen.repaint();
	}

	public static void showMenu() {
		clearGlassPane();
		currentPane = mainMenu;
		updatePane();
	}

	public static void createPlayground(int numPlayers) {
		playground = new Playground(numPlayers);
		currentPane = playground;
		updatePane();
		GameManager.start();
	}

	public static void main(String[] args) {
		new Screen();
	}
}