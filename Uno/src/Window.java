import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Window extends JFrame {
	private static Window window;
	private GUI gui = new GUI(this);
	public Window() {
		super("UNO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1600,960);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int)(screenSize.width-1600)/2,(int)(screenSize.height-960)/2);
		setResizable(false);
		setLayeredPane(gui);
		setVisible(true);
	}
	public void newGame() {
		setVisible(false);
		dispose();
		window = new Window();
	}
	public static void main(String[] args) {
		window = new Window();
	}
}
