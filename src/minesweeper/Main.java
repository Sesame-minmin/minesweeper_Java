package minesweeper;

import static minesweeper.Alignment.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class Main extends JPanel {
	public static final int N = 8; // board size N x N cells
	public static final int M = N; // #mines
	public static final int S = 16; // cell size

	public static final int NONE = 0;
	public static final int PLAYER = 1;
	public static final int ENEMY = 2;

	// modeling game board
	boolean[][] mines = new boolean[N][N];
	boolean[][] isOpen = new boolean[N][N];
	int opened = 0;
	int winner = NONE;  // one of NONE, PLAYER, ENEMY

	public Main() {
		setMines();

		JPanel canvas = new JPanel() {
			@Override
			public void paint(Graphics g) {
				for (int y = 0; y < N; y++) {
					for (int x = 0; x < N; x++) {
						g.drawRect(x * S, y * S, S, S);
						if (isOpen[y][x])
							drawCount(x, y, g);
					}
				}
				if (winner == ENEMY)
					drawEnding(g, "Bang!");
				else if (winner == PLAYER)
					drawEnding(g, "Win!");
			}
		};
		canvas.setFocusable(true);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX() / S;
				int y = e.getY() / S;
				if (!isOnBoard(x, y))
					return;
				if (mines[y][x]) {
					winner = ENEMY;
					openAll();
				}
				openZero(x, y);
				if (opened + M == N*N) {
					winner = PLAYER;
					openAll();
				}
				repaint();
			}
		});
		canvas.setPreferredSize(new Dimension(S*N, S*N));
		add(canvas);
	}

	// 地雷の設置
	void setMines() {
		mines = new boolean[N][N];
		isOpen = new boolean[N][N];
		for (int i = 0; i < M; i++) {
			int x, y;
			do {
				x = (int) (Math.random() * N);
				y = (int) (Math.random() * N);
			} while (isMine(x, y));
			setMine(x, y, true);
		}
	}

	// (x, y) is on board?
	boolean isOnBoard(int x, int y) {
		return (0 <= y && y < N && 0 <= x && x < N);
	}

	// Is there a mine at (x, y)?
	boolean isMine(int x, int y) {
		if (isOnBoard(x, y))
			return mines[y][x];
		return false;
	}

	// set a mine at (x, y)
	void setMine(int x, int y, boolean isMine) {
		if (isOnBoard(x, y))
			mines[y][x] = isMine;
	}

	int getCounts(int x, int y) {
		int c = 0;
		for (int dy = -1; dy <= 1; dy++) {
			for (int dx = -1; dx <= 1; dx++) {
				if (dx == 0 && dy == 0)
					continue;
				if (isMine(x + dx, y + dy))
					c++;
			}
		}
		return c;
	}

	void drawCount(int x, int y, Graphics g) {
		String s = (mines[y][x]) ? "*" : "" + getCounts(x, y);
		g.drawString(s, center(s, x * S + S / 2, g),
				middle(y * S + S / 2, g));
	}

	// エンディング表示
	void drawEnding(Graphics g, String msg) {
		g.setColor(Color.red);
		g.drawString(msg, center(msg, N * S / 2, g),
				middle(N * S / 2, g));
	}

	// すべて開く
	void openAll() {
		for (int y = 0; y < N; y++)
			for (int x = 0; x < N; x++)
				open(x, y);
	}

	// 隣接する0をすべて開ける
	void openZero(int x, int y) {
		if (!isOnBoard(x, y) || isOpen[y][x])
			return;
		open(x, y);
		if (getCounts(x, y) > 0)
			return;
		for (int dy = -1; dy <= 1; dy++)
			for (int dx = -1; dx <= 1; dx++)
				openZero(x + dx, y + dy); // dx=dy=0はチェック済み
	}

	// 本当に開ける操作
	void open(int x, int y) {
		if (! isOpen[y][x]) {
			isOpen[y][x] = true;
			opened++;
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainPane.createFrame("Mine Sweeper", new Main());
			}
		});
	}
}
