import java.io.*;
import java.util.Scanner;

public class ConsoleGame {
	public static final int NUM_ROWS = 6;
	public static final int NUM_COLS = 6;
	
	private Level level;

	public static void main(String[] args) {
		ConsoleGame cg = new ConsoleGame();
		cg.playGame();
	}
	
	public ConsoleGame() {
		level = new Level(NUM_ROWS, NUM_COLS);
	}
	

    public void playGame() {
    	System.out.println(level); // Print initial board state
        while (!level.isSolved()) {
        	System.out.println("What's your next move? (Moves so far: " + level.getNumMoves() + ")");
        	// Get the location from the user
        	Space start = getLocationFromUser(level.getRows(), level.getColumns());
        	// Get the numSpaces to move from the user
        	int spaces = getInteger1("How many spaces would you like this vehicle to move?");
        	// Move vehicle
        	if (start != null) {
        		level.moveVehicle(start, spaces);
        		System.out.println(level);
        	if (level.isSolved()) {
        		System.out.println("Congratulations! You've solved the level.");
        	} else {
        		System.out.println("Keep trying!");
        	}
        	} else {
        		System.out.println("Invalid input. Please try again.");
        	}
        }
      }
    
    private final static BufferedReader CONSOLE_READER = new BufferedReader(new
    		InputStreamReader(System.in));
    		public static Space getLocationFromUser(int maxRows, int maxCols) {
    		try {
    		while (true) {
    		System.out.print("Please enter a location using the letter for row and number for column: ");

    		String response = CONSOLE_READER.readLine();
    		Space loc = convertStringToIntPair(response);

    		if (isValidLocation(loc, maxRows, maxCols)) return loc;
    		System.out.println("Sorry, but \"" + response + "\" isn't a valid response. Please try again.");

    		System.out.println();
    		}
    		} catch (IOException ioe) {
    		System.err.println("Problem encountered while reading text from standard input. Bailing.");

    		System.exit(1);
    		}
    		return null;
    		}
    		public static int getInteger1(String message) {

    		int num;
    		while(true) {
    		System.out.println(message);
    		try {
    		num = Integer.parseInt(CONSOLE_READER.readLine());
    		return num;
    		}catch(NumberFormatException nfe) {
    		System.err.println("You didn't enter a valid number, please try again.");
    		}catch(IOException ioe) {
    		System.err.println("Problem encountered while reading text from standard input. Bailing.");

    		System.exit(1);
    		}
    		}
    		}

	public static int getInteger(String message) {
		int num;
		while(true) {
			System.out.println(message);
			try {
				num = Integer.parseInt(CONSOLE_READER.readLine());
				return num;
			}catch(NumberFormatException nfe) {
				System.err.println("You didn't enter a valid number, please try again.");
			}catch(IOException ioe) {
				System.err.println("Problem encountered while reading text from standard input.  Bailing.");
				System.exit(1);
			}
		}
	}

	public static boolean isValidLocation(Space loc, int maxRows, int maxCols) {
		if(loc == null) return false;
		return loc.getRow() >= 0 && loc.getRow() < maxRows && loc.getCol() >= 0 && loc.getCol() < maxCols;
	}

	public static Space convertStringToIntPair(String location) {
		if(location == null || location.equals("") || location.length() != 2) return null;
		location = location.toUpperCase();
		char row = location.charAt(0);
		char col = location.charAt(1);
		return new Space(row-'A', col-'1');
	}
}
