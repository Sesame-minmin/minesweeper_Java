package minesweeper;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * JPanelのサブクラスとしてアプリを作成した場合
 */
public class MainPane extends JPanel {
	public MainPane() {
		//マインスイーパーオンラインっぽく改造する
		setLayout(new FlowLayout());
		add(new JLabel("Hello"));
		add(new JLabel("Java"));
		add(new JLabel("Swing"));
		add(new JLabel("World"));
		setPreferredSize(new Dimension(320, 240));
	}

	public static JFrame createFrame(JPanel panel) {
		return createFrame("Java Swing App", panel);
	}

	public static JFrame createFrame(String title, JPanel panel) {
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainPane.createFrame("MainPane", new MainPane());
			}
		});
	}
}
