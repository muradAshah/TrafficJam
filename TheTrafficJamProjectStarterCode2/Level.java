import java.util.*;

public class Level {	
	private Board board;
	private int numMoves;
	private boolean solved;
	private Space goal;
	
	public Level(int nRows, int nCols) {
		 board = new Board(nRows, nCols);
		 //numMoves = 0;
		 //solved = false;
		 //setUpLevel();	 
	}
	
	public void setUpLevel(int maxRows, int maxCols) {
		board = new Board(maxRows, maxCols);
		goal = getGoalSpace();
		
		board.addVehicle(VehicleType.MYCAR, 1, 0, 2, false); // MYCAR = Player
		board.addVehicle(VehicleType.TRUCK, 0, 2, 3, true);
		board.addVehicle(VehicleType.AUTO, 4, 0, 2, true);
		board.addVehicle(VehicleType.AUTO, 3, 3, 2, false);
		board.addVehicle(VehicleType.TRUCK, 1, 4, 2, true);
		
	}
	
	public int getColumns() {
		return board.getNumCols();
		
	}
	public int getRows() {
		return board.getNumRows();
	 
	}
	public boolean isSolved() {
		return solved;
	}
	
	public boolean passedLevel() {
		if (board.isVehicleOnSpace(goal )) {
			if (board.getVehicle(goal).getVehicleType() == VehicleType.MYCAR) {
				return true;
			}
		}
		return false;
	}
	
	public int getNumMoves() {
		return numMoves;
	}
	
	public Space getGoalSpace() {
		goal = new Space(1, 5);
		return goal;
	}
	
	public Board getBoard() {
        return board;
    }
	
	
	public Vehicle getVehicle(Space s) {
		return board.getVehicle(s);
	}
	
	public ArrayList<Vehicle> getVehiclesOnBoard() {
		return board.getVehiclesOnBoard();
	}
	
	public boolean moveVehicle(Space start, int spaces) {
		/*
		if (board.canMoveNumSpaces(start, spaces)) {
			if (board.moveNumSpaces(start, spaces)) {
			numMoves++;
			if (isGameSolved()) {
			solved = true;
			}
		} else {
		System.out.println("Move failed. Try again.");

		}
		} else {
		System.out.println("Invalid move. Please try again.");
		}
		*/
		if (board.isVehicleOnSpace(goal)) {
			if(board.getVehicle(goal).getVehicleType() == VehicleType.MYCAR) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isGameSolved() {
		for (int row = 0; row < board.getNumRows(); row++) {
			Vehicle vehicle = board.getVehicle(new Space(row, board.getNumCols() - 1));
			if (vehicle != null && vehicle.getVehicleType() == VehicleType.MYCAR) {
			return true;
			}
		}
			return false;
	}

	
	public String toString() {
		String result = generateColHeader(getColumns());
		result+=addRowHeader(board.toString());
		return result;
	}
	
	private String addRowHeader(String origBoard) {
		String result = "";
		String[] elems = origBoard.split("\n");
		for(int i = 0; i < elems.length; i++) {
			result += (char)('A' + i) + "|" + elems[i] + "\n"; 
		}
		return result;
	}
	
	private String generateColHeader(int cols) {
		String result = "  ";
		for(int i = 1; i <= cols; i++) {
			result+=i;
		}
		result+="\n  ";
		for(int i = 0; i < cols; i++) {
			result+="-";
		}
		result+="\n";
		return result;
	}

}
