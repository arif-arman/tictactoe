import java.util.ArrayList;
import java.util.List;

public class Board {
	private int [][] blocks;
	
	public Board(int [][] blocks) {
		// TODO Auto-generated constructor stub
		this.blocks = blocks;
	}
	
	public int [][] getBlocks() {
		return blocks;
	}
	
	public boolean updateBoard(Point p, int player) {
		if(blocks[p.x][p.y] == 0) {
			blocks[p.x][p.y] = player;
			return true;
		}
		return false;
		
	}
	/*
	 *  evaluate the value of current board status
	 */
	public int evaluateBoard() {
		if(XWon()) return 1;
		else if(OWon()) return -1;
		return 0;/*
		int score = 0;
		// check rows
		for(int i=0;i<3;i++) {
			int blank = 0;
			int X = 0;
			int O = 0;
			for(int j=0;j<3;j++) {
				if(blocks[i][j] == 0) blank++;
				else if(blocks[i][j] == 1) X++;
				else O++;
			}
			score += updateScore(X,O);
		}
		// check columns
		for(int j=0;j<3;j++) {
			int blank = 0;
			int X = 0;
			int O = 0;
			for(int i=0;i<3;i++) {
				if(blocks[i][j] == 0) blank++;
				else if(blocks[i][j] == 1) X++;
				else O++;
			}
			score += updateScore(X,O);
			
		}
		// check first diagonal
		int blank = 0;
		int X = 0;
		int O = 0;
		for(int i=0,j=0;i<3;i++,j++) {
			if(blocks[i][j] == 0) blank++;
			else if(blocks[i][j] == 1) X++;
			else O++;
		}
		score += updateScore(X,O);
		
		// check second diagonal
		blank = 0;
		X = 0;
		O = 0;
		for(int i=0,j=2;i<3;i++,j--) {
			if(blocks[i][j] == 0) blank++;
			else if(blocks[i][j] == 1) X++;
			else O++;
		}
		score += updateScore(X, O);
		
		return score;
		*/
	}
	
	/*
	 *  checks number of X/O is a row,col or diagonal
	 *  and assigns a value for X as max, O as min
	 */
	
	public int updateScore(int X, int O) {
		int change;
		if(X==3) change = 100;
		else if(X==2 && O==0) change = 10;
		else if(X==1 && O==0) change = 1;
		else if(O==3) change = -100;
		else if(O==2 && X==0) change = -10;
		else if(O==1 && X==0) change = -1;
		else change = 0;
		return change;
	}
	
	/*
	 * check to see if the game is over
	 */	
	public boolean isGameOver() {
		return (XWon() || OWon() || boardFull());
	}
	public boolean XWon() {
		if((blocks[0][0] == blocks[1][1] && blocks[1][1] == blocks[2][2] && blocks[2][2] == 1) ||
				(blocks[0][2] == blocks[1][1] && blocks[1][1] == blocks[2][0] && blocks[2][0] == 1))
			return true;
		for(int i=0;i<3;i++) {
			if((blocks[i][0] == blocks[i][1] && blocks[i][1] == blocks[i][2] && blocks[i][2] == 1) ||
					(blocks[0][i] == blocks[1][i] && blocks[1][i] == blocks[2][i] && blocks[2][i] == 1))
				return true;
		}
		return false;
	}
	public boolean OWon() {
		if((blocks[0][0] == blocks[1][1] && blocks[1][1] == blocks[2][2] && blocks[2][2] == 2) ||
				(blocks[0][2] == blocks[1][1] && blocks[1][1] == blocks[2][0] && blocks[2][0] == 2))
			return true;
		for(int i=0;i<3;i++) {
			if((blocks[i][0] == blocks[i][1] && blocks[i][1] == blocks[i][2] && blocks[i][2] == 2) ||
					(blocks[0][i] == blocks[1][i] && blocks[1][i] == blocks[2][i] && blocks[2][i] == 2))
				return true;
		}
		return false;
	}
	public boolean boardFull() {
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				if(blocks[i][j] == 0) return false;
		return true;
	}
	
	public List<Point> getActions() {
		ArrayList<Point> actions = new ArrayList<>();
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				if(blocks[i][j] == 0) actions.add(new Point(i,j));
		return actions;
	}
	
	/*
	 * does alpha beta pruning and returns best move
	 */
	ArrayList<Point> children;
	public Point alphaBeta() {
		children = new ArrayList<>();
		int v = maxValue(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
		int mx = Integer.MIN_VALUE;
		Point p = null;
		for(int i=0;i<children.size();i++) {
			//System.out.println(children.get(i));
			if(children.get(i).getValue() == v) 
				return children.get(i);
		}
		return p;
	}
	public int maxValue(int alpha, int beta, int depth) {
		//System.out.println(this);
		//System.out.println(evaluateBoard());
		if(isGameOver()) return evaluateBoard();
		int v = Integer.MIN_VALUE;
		ArrayList<Point> a = (ArrayList<Point>) getActions();
		for(int i=0;i<a.size();i++) {
			Point p = a.get(i);
			blocks[p.x][p.y] = Main.COMPUTER;
			v = Math.max(v, minValue(alpha, beta, depth+1));
			p.setValue(v);
			if(depth==0) children.add(p);
			blocks[p.x][p.y] = 0;
			if(v>=beta) return v;
			alpha = Math.max(alpha,v);
		}
		return v;
	}
	public int minValue(int alpha, int beta, int depth) {
		//System.out.println(this);
		//System.out.println(evaluateBoard());
		if(isGameOver()) return evaluateBoard();
		int v = Integer.MAX_VALUE;
		ArrayList<Point> a = (ArrayList<Point>) getActions();
		for(int i=0;i<a.size();i++) {
			Point p = a.get(i);
			blocks[p.x][p.y] = Main.USER;
			v = Math.min(v, maxValue(alpha, beta, depth+1));
			blocks[p.x][p.y] = 0;
			if(v<=alpha) return v;
			beta = Math.min(beta,v);
		}
		return v;
	}

	
	public String toString() {
		String str = "";
        for (int i=0;i<3;i++) {
        	for (int j=0;j<3;j++) {
        		if(blocks[i][j] == Main.COMPUTER) str +=  "X\t";
        		else if(blocks[i][j] == Main.USER) str += "O\t";
        		else str += "-\t";
        		
        	}
        	str += "\n";
        }
        return str;
	}
	
}
