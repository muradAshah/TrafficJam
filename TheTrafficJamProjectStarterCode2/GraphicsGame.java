import acm.program.*;
import acm.graphics.*;
import java.util.ArrayList;
import java.awt.event.MouseEvent;

public class GraphicsGame extends GraphicsProgram {
	public static final int PROGRAM_WIDTH = 500;
	public static final int PROGRAM_HEIGHT = 500;
	public static final String lABEL_FONT = "Arial-Bold-22";
	public static final String EXIT_SIGN = "EXIT";
	public static final String IMG_FILENAME_PATH = "images/";
	public static final String IMG_EXTENSION = ".png";
	public static final String VERTICAL_IMG_FILENAME = "_vert";
	private int row = 6;
	private int col = 6;
	private GLabel movesLabel;
	private Board board;
	private Level l;
	private Space winningTile;
	private double initialX, initialY; // For mouse drag
    private GObject selectedVehicle; // The vehicle currently selected
    private ArrayList<Vehicle> vehicles; // List of vehicles on the board
    private Space firstSpace;
    private Space lastSpace;
    private Vehicle vehicle;
    private double lastX;
    private double lastY;
    private boolean toDrag = false;
    
  
	public void init() {
		setSize(PROGRAM_WIDTH, PROGRAM_HEIGHT);
	}

	    public void run() {
	    	l =  new Level(6, 6);
	    	l.setUpLevel(6, 6);
	    	board = l.getBoard();
	    	addMouseListeners();
	        drawLevel(); // Draw the initial level
	    }

	    private void drawLevel() {
		    //removeAll();
		    drawGridLines();
		    drawCars();
		    drawNumMoves();
		    drawWinningTile();
	    }

	private void drawWinningTile() {
		winningTile = l.getGoalSpace();
		GLabel exit = new GLabel(EXIT_SIGN, spaceWidth() * winningTile.getCol(), spaceHeight() * winningTile.getRow());
		exit.setFont(lABEL_FONT);
		add(exit);
	}
	
	private void drawNumMoves() {
		movesLabel = new GLabel("Moves: 0", 10, 20); // Label to track moves
		movesLabel.setFont(lABEL_FONT);
		movesLabel.setLabel("" + l.getNumMoves());
		add(movesLabel);
	}
	
	private void drawGridLines() {
		for (int i = 0; i <= board.getNumCols(); i++) {
	        double x = i * spaceWidth();
	        GLine verticalLine = new GLine(x, 0, x, PROGRAM_HEIGHT);
	        add(verticalLine);
	    }

	    for (int i = 0; i <= board.getNumRows(); i++) {
	        double y = i * spaceHeight();
	        GLine horizontalLine = new GLine(0, y, PROGRAM_WIDTH, y);
	        add(horizontalLine);
	    }
	}

	/**
	 * Maybe given a list of all the cars, you can go through them and call
	 * drawCar on each?
	 */
	private void drawCars() {
	    vehicles = board.getVehiclesOnBoard();
	    for (Vehicle v : vehicles) {
	        drawCar(v);
	    }
	 }
	
	private void drawCar(Vehicle v) {
		   String imagePath = IMG_FILENAME_PATH + v.getVehicleType().toString();
		    if (v.isVertical()) {
		        imagePath += VERTICAL_IMG_FILENAME;
		    }
		    imagePath += IMG_EXTENSION;
		    
		    // Load the car image
		    GImage carImage = new GImage(imagePath);
		    
		    // Retrieve the starting position
		    Space startSpace = v.getStart();
		    int startRow = startSpace.getRow();
		    int startCol = startSpace.getCol();
		    
		    // Calculate the (x, y) position on the screen
		    double x = startCol * spaceWidth();
		    double y = startRow * spaceHeight();
		    
		    // Set the size based on the vehicle's length and orientation
		    if (v.isVertical()) {
		        carImage.setSize(spaceWidth(), spaceHeight() * v.getSize());
		    } else {
		        carImage.setSize(spaceWidth() * v.getSize(), spaceHeight());
		    }
		    
		    // Add the image to the display
		    add(carImage, x, y);
		}


	public void mousePressed(MouseEvent e) {
	    int x = e.getX();
	    int y = e.getY();
	    selectedVehicle = getElementAt(x, y); // Get the vehicle at the clicked position

	    if (selectedVehicle != null) {
	    	firstSpace = convertXYToSpace(x, y);
	    	vehicle = l.getVehicle(firstSpace);    	
	        lastX = x;
	        lastY = y;
	    }
	}

    
    public void mouseDragged(MouseEvent e) {
      if (selectedVehicle != null) {
            // Convert the mouse coordinates to a space
            Space newSpace = convertXYToSpace(e.getX(), e.getY());

            // Determine the direction and calculate spaces moved
            boolean isVertical = vehicle.isVertical();
            int spacesMoved = isVertical
                ? newSpace.getRow() - firstSpace.getRow()
                : newSpace.getCol() - firstSpace.getCol();

            // Check if the move is valid
            if (board.canMoveNumSpaces(firstSpace, spacesMoved)) {
                // Remove the old vehicle image
                remove(selectedVehicle);

                // Move the vehicle logically
                board.moveNumSpaces(firstSpace, spacesMoved);

                // Update the vehicle's graphical representation
                removeAll(); // Clear the board
                drawLevel(); // Redraw everything, including the updated vehicle
            }
        }
    } 
       

	public void mouseReleased(MouseEvent e) {
		 if ((selectedVehicle != null) && (!toDrag)) {
		        lastSpace = convertXYToSpace(e.getX(), e.getY()); // Calculate the last position
		        int spacesMoved = calculateSpacesMoved();

		        if (l.moveVehicle(firstSpace, spacesMoved)) { // Attempt to move the vehicle logically
		            // Check if the MYCAR is past the exit
		            Vehicle vehicle = l.getVehicle(lastSpace);
		            if (vehicle != null && vehicle.getVehicleType() == VehicleType.MYCAR && l.passedLevel()) {
		                // MYCAR has successfully exited
		                removeAll();
		                GLabel winLabel = new GLabel("Congratulations! You've solved the game!", 50, 250);
		                winLabel.setFont(lABEL_FONT);
		                winLabel.setLocation(PROGRAM_WIDTH / 4, PROGRAM_HEIGHT / 2);
		                add(winLabel);
		            } else {
		                // Redraw the level
		                removeAll();
		                drawLevel();
		            }
		            System.out.println("Vehicle moved successfully.");
		        } else {
		            // Move failed, reset the vehicle to the original position
		            resetVehiclePosition();
		            System.out.println("Vehicle move failed.");
		        }
		    }
		}


	private void resetVehiclePosition() {
	    double originalX = firstSpace.getCol() * spaceWidth();
	    double originalY = firstSpace.getRow() * spaceHeight();
	    selectedVehicle.setLocation(originalX, originalY);
	}
		

	private Vehicle getVehicleFromXY(double x, double y) {
		Space space = convertXYToSpace(x, y);
		if (space != null) {
			return board.getVehicle(space);
			}
			return null;
		}

	/**
	 * This is a useful helper function to help you calculate the number of
	 * spaces that a vehicle moved while dragging so that you can then send that
	 * information over as numSpacesMoved to that particular Vehicle object.
	 * 
	 * @return the number of spaces that were moved
	 */
	private int calculateSpacesMoved() {
		// if car is vertical
		if((l.getVehicle(firstSpace)).isVertical()) {
			return lastSpace.getRow() - firstSpace.getRow();
			
		}
		// if car is horizontal
		else {
			return lastSpace.getCol() - firstSpace.getCol();
		}
		
	}

	
	private Space convertXYToSpace(double x, double y) {
	    int col = (int) (x / spaceWidth());  // Calculate the column based on x-coordinate
	    int row = (int) (y / spaceHeight()); // Calculate the row based on y-coordinate

	    // Dynamically create and return a new Space object representing the clicked space
	    return new Space(row, col);
	}

	/**
	 * 
	 * @return the width (in pixels) of a single space in the grid
	 */
	private double spaceWidth() {
		return PROGRAM_WIDTH / board.getNumCols(); // returns size of a single column
	}

	/**
	 * 
	 * @return the height in pixels of a single space in the grid
	 */
	private double spaceHeight() {
		return PROGRAM_HEIGHT / board.getNumRows(); // returns size of a single row
	}
	
	private ArrayList<Vehicle> getVehiclesOnBoard() {
		return l.getVehiclesOnBoard();
	}
	
	public static void main(String[] args) {
		new GraphicsGame().start();
	}
}
