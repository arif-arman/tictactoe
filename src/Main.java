import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Scanner;

import jdk.nashorn.internal.ir.CallNode.EvalArgs;

import javax.swing.*;


public class Main extends JFrame implements ActionListener {
	public static final int COMPUTER = 1;
	public static final int USER = 2;
	JPanel panel;
	JButton [][] fields;
	private Board board;
	private int turn;

	public Main(Board board) {
		super("Tic Tac Toe");
		setSize(600,600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.board = board;
		updateFrame();
		Random rand = new Random();
		int posx = rand.nextInt(3);
		int posy = rand.nextInt(3);
		Point p = new Point(posx, posy);
		board.updateBoard(p, COMPUTER);
		turn = USER;
		//System.out.println(board);
		updateFrame();


	}

	public void updateFrame() {
		if(panel != null) remove(panel);
		panel = new JPanel();
		panel.setLayout(new GridLayout(3,3));
		fields = null;
		fields = new JButton[3][3];
		int [][] blocks = board.getBlocks();
		String symbol;
		for (int i=0;i<3;i++) {
			for (int j=0;j<3;j++) {
				if (blocks[i][j] == 1) symbol = "X";
				else if (blocks[i][j] == 2) symbol = "O";
				else symbol = "";
				fields[i][j] = new JButton(symbol);
				fields[i][j].addActionListener(this);
				fields[i][j].setFont(new Font("Arial", 1, (600/3)/4));
				if(blocks[i][j] == 1) fields[i][j].setForeground(Color.GREEN);
				else if(blocks[i][j] == 2) fields[i][j].setForeground(Color.RED);
				panel.add(fields[i][j]);
			}
		}
		add(panel);
		repaint();
		setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Point p = null;
		for (int i=0;i<3;i++) {
			for (int j=0;j<3;j++) {
				if(e.getSource() == fields[i][j]) {
					p = new Point(i, j);
					if (!board.updateBoard(p, USER))
						JOptionPane.showMessageDialog(this, "Invalid");
					//System.out.println(board);
					break;
				}
			}
			if (p!=null) break;
		}
		game();

	}

	public void game() {
		//System.out.println("Player Moves : ");
		//p = new Point(input.nextInt(3),input.nextInt(3));
		//if(board.isGameOver())
		if(!board.isGameOver()) {
			Point p = board.alphaBeta();
			if(p != null) board.updateBoard(p, COMPUTER);
			//System.out.println(board);
			p = null;
			updateFrame();
			if(board.isGameOver()) gameOver();
		}
		else gameOver();

	}

	public void gameOver() {

		if(board.XWon()) JOptionPane.showMessageDialog(this, "Computer Wins"); //System.out.println("Computer Wins");
		else if(board.OWon()) JOptionPane.showMessageDialog(this, "Player Wins"); //System.out.println("Player Wins");
		else JOptionPane.showMessageDialog(this, "Match Drawn"); //System.out.println("Match Drawn");
		dispose();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		int [][] blocks = new int[3][3];
		Board board = new Board(blocks);
		//System.out.println(board);
		Main m = new Main(board);

	}

}
