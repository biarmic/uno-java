package managers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import gamestuff.Card;
import gamestuff.Playground;
import windows.Button;
import windows.MainMenu;
import windows.Picture;
import windows.Screen;
import windows.Text;
import windows.Window;

public class GameManager {
	public static boolean FULLSCREEN;
	public static Resolution RESOLUTION;
	public static int WIDTH;
	public static int HEIGHT;
	public static Dimension DIMENSION;
	public static double WIDTH_SCALE;
	public static double HEIGHT_SCALE;
	private static GameThread thread;

	public GameManager() {
		Container container = new Container();
		container.setPreferredSize(GameManager.DIMENSION);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Screen.SCREEN_WIDTH = (int) screenSize.getWidth();
		Screen.SCREEN_HEIGHT = (int) screenSize.getHeight();
		readSettings();
	}
	
	public static void start() {
		if(thread != null)
			thread.stop();
		thread = new GameThread();
		thread.start();
	}

	public static void writeSettings() {
		try {
			String str = "";
			str += FULLSCREEN + "\n";
			str += RESOLUTION.indexOf() + "\n";
			str += AudioManager.SOUND + "\n";
			FileWriter fw = new FileWriter(new File("src/others/settings.txt"));
			fw.write(str);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void readSettings() {
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get("src/others/settings.txt"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FULLSCREEN = Boolean.parseBoolean(lines.get(0));
		RESOLUTION = Resolution.values()[Integer.parseInt(lines.get(1))];
		WIDTH = RESOLUTION.width();
		WIDTH_SCALE = WIDTH / 1920.0;
		HEIGHT = RESOLUTION.height();
		HEIGHT_SCALE = HEIGHT / 1080.0;
		DIMENSION = new Dimension(WIDTH, HEIGHT);
		AudioManager.SOUND = Boolean.parseBoolean(lines.get(2));
		Card.loadCards();
		Playground.DECK_POINT = calculatePoint(810, 465);
		Playground.PILE_POINT = calculatePoint(1010, 465);
		Button.BORDER = new LineBorder(Button.BORDER_COLOR, (int) (4 * (double) GameManager.HEIGHT_SCALE));
		Window.BORDER = new LineBorder(Color.white, (int) (4 * (double) GameManager.HEIGHT_SCALE));
	}

	public static Rectangle calculateRectangle(int x, int y, int width, int height) {
		return new Rectangle((int) (x * WIDTH_SCALE), (int) (y * HEIGHT_SCALE), (int) (width * WIDTH_SCALE), (int) (height * HEIGHT_SCALE));
	}

	public static Rectangle calculateRectangle(Rectangle rect) {
		return calculateRectangle(rect.x, rect.y, rect.width, rect.height);
	}

	public static Point calculatePoint(int x, int y) {
		return new Point((int) (x * WIDTH_SCALE), (int) (y * HEIGHT_SCALE));
	}

	public static ArrayList<Component> getAllComponents(final Container c) {
		ArrayList<Component> compList = new ArrayList<Component>();
		if (c != null) {
			Component[] comps = c.getComponents();
			for (Component comp : comps) {
				compList.add(comp);
				if (comp instanceof Container)
					compList.addAll(getAllComponents((Container) comp));
			}
		}
		return compList;
	}

	public static void updateComponents(double prevWidthScale, double prevHeightScale) {
		if (Screen.screen != null)
			if (GameManager.FULLSCREEN)
				Screen.screen.setExtendedState(JFrame.MAXIMIZED_BOTH);
			else {
				Container container = new Container();
				container.setPreferredSize(GameManager.DIMENSION);
				Screen.screen.setContentPane(container);
				Screen.screen.pack();
				Screen.screen.setLocation((Screen.SCREEN_WIDTH - GameManager.WIDTH) / 2, (Screen.SCREEN_HEIGHT - GameManager.HEIGHT) / 2);
			}
		ArrayList<Component> compList = getAllComponents(Screen.mainMenu);
		compList.add(Screen.settings);
		compList.add(Screen.play);
		compList.add(Screen.color);
		compList.add(Screen.end);
		compList.add(Screen.playground);
		compList.addAll(getAllComponents(Screen.settings));
		compList.addAll(getAllComponents(Screen.play));
		compList.addAll(getAllComponents(Screen.color));
		compList.addAll(getAllComponents(Screen.end));
		compList.addAll(getAllComponents(Screen.playground));
		for (Component comp : compList)
			if (comp != null) {
				Rectangle curr = comp.getBounds();
				Rectangle orig = new Rectangle((int) (curr.x / prevWidthScale), (int) (curr.y / prevHeightScale), (int) (curr.width / prevWidthScale), (int) (curr.height / prevHeightScale));
				comp.setBounds(calculateRectangle(orig));
				if (comp instanceof Card)
					((Card) comp).setIcon();
				else if (comp instanceof Button)
					((Button) comp).setFont(new Font("arial", Font.BOLD, comp.getHeight() / 2));
				else if (comp instanceof Text)
					((Text) comp).setFont(new Font("arial", Font.BOLD, (int) (((Text) comp).getFontSize() * GameManager.HEIGHT_SCALE)));
				else if (comp instanceof Window) {
					((Window) comp).setPreferredSize(comp.getSize());
					((Window) comp).setBorder(Window.BORDER);
				} else if (comp.getParent() instanceof MainMenu && comp instanceof JLabel && ((JLabel) comp).getIcon() != null && ((JLabel) comp).getIcon() instanceof Picture) {
					((Picture) ((JLabel) comp).getIcon()).readFile();
					((JLabel) comp).setIcon(((Picture) ((JLabel) comp).getIcon()));
				}
				comp.repaint();
			}
		Screen.screen.repaint();
	}
}