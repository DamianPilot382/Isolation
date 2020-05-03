import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Damian Ugalde
 * @date 2020-05-03
 * @version 1.0
 *
 * Project 4 - Isolation
 * CS 4200 - Artificial Intelligence
 * California State Polytechnic University, Pomona
 * Computer Science Department
 *
 * Instructor: Dominick A. Atanasio
 *
 */
public class IsolationBoard {

    //Store the position of the players
    protected int[] locX;
    protected int[] locO;

    //Store the used spaces (set to true)
    protected boolean[] usedSpaces;

    /**
     * Creates a new board.
     * Initializes the starting positions for the 2 players.
     */
    public IsolationBoard(){

        //Start positions for the players.
        this.locX = new int[]{0, 0};
        this.locO = new int[]{7, 7};

        //Create the board with all spaces as unused.
        this.usedSpaces = new boolean[64];
        
        //Then assign the starting positions for the two players as used.
        this.usedSpaces[0] = true;
        this.usedSpaces[63] = true;

    }

    /**
     * Calculates the moves available for the player specified.
     * @param player Player to calculate the moves for.
     * @return List object containing all of the moves available for that player.
     */
    public List<Move> getPossibleMoves(Player player){

        //Initialize the List with a size of 28,
        //since that's the biggest amount of moves that can be made.
        List<Move> moves = new ArrayList<Move>(28);

        //Get the location of the appropriate player.
        int[] loc = (player == Player.X)? locX: locO;

        //Use a flag for each direction to see if we still have to check that path.
        boolean left = true;
        boolean right = true;
        boolean up = true;
        boolean down = true;
        boolean upLeft = true;
        boolean upRight = true;
        boolean downLeft = true;
        boolean downRight = true;

        //Loop the size of the board
        for(int i = 1; i < 7; i++){
            
            //Check if there are still any paths to check
            if(!(left || right || up || down || upLeft || upRight || downLeft || downRight))
                break;

            //Check the top boundary of the board.
            if(loc[0] - i < 0){
                up = false;
                upLeft = false;
                upRight = false;
            }

            //Check the bottom boundary of the board.
            if(loc[0] + i > 7){
                down = false;
                downLeft = false;
                downRight = false;
            }

            //Check the left boundary of the board.
            if(loc[1] - i < 0){
                left = false;
                upLeft = false;
                downLeft = false;
            }

            //Check the right boundary of the board.
            if(loc[1] + i > 7){
                right = false;
                upRight = false;
                downRight = false;
            }

            //Check if the square is occupied for each direction.
            up = checkMoveAuxiliary(up, loc, moves, -i, 0);
            down = checkMoveAuxiliary(down, loc, moves, i, 0);
            left = checkMoveAuxiliary(left, loc, moves, 0, -i);
            right = checkMoveAuxiliary(right, loc, moves, 0, i);

            upLeft = checkMoveAuxiliary(upLeft, loc, moves, -i, -i);
            upRight = checkMoveAuxiliary(upRight, loc, moves, -i, i);
            downLeft = checkMoveAuxiliary(downLeft, loc, moves, i, -i);
            downRight = checkMoveAuxiliary(downRight, loc, moves, i, i);

        }

        //Shuffle the results so that the algorithm doesn't always check the
        //1-space moves first.
        Collections.shuffle(moves);

        return moves;
    }

    /**
     * Checks if the next space in this path is free.
     * Also adds the Move to the moves List.
     * @param value If this path should be checked.
     * @param loc location of the player.
     * @param moves Moves List that stores the moves available.
     * @param rowOffset row offset for the player location.
     * @param colOffset column offset for the player location.
     * @return true if this path should be checked again, false otherwise.
     */
    private boolean checkMoveAuxiliary(boolean value, int[] loc, List<Move> moves, int rowOffset, int colOffset){
        
        //If this path should be checked
        if(value){
            //If the square is used, return false so that it's not checked again.
            if(getBoard(loc[0]+rowOffset, loc[1]+colOffset))
                return false;
            
            //Otherwise, add the move and return true so that it is checked again.
            moves.add(new Move(loc[0]+rowOffset, loc[1]+colOffset));
            return true;
        }

        //The path shouldn't be checked.
        return false;
    }

    /**
     * Will set the board's row and column to used or unused.
     * This is an auxiliary method to make it easy to convert from 2D to 1D indexing.
     * 
     * @param value Value to set the board to.
     * @param row Row of the board
     * @param col Column of the board
     */
    private void setBoard(boolean value, int row, int col){
        //Equivalent to multiplying the row by 8 and adding the column.
        usedSpaces[(row << 3) + col] = value;
    }

    /**
     * Gets if the specified square is used or unused.
     * This is an Auxiliary method to transform a row-column value into a single int index.
     * @param row Row of the board
     * @param col Column of the board
     * @return True if the specified square is used, false otherwise.
     */
    private boolean getBoard(int row, int col){
        //Equivalent to multiplying the row by 8 and adding the column.
        return usedSpaces[(row << 3) + col];
    }

    /**
     * Moves the player from their current position to the new position.
     * @param player Player to move
     * @param move New position to move the player to
     */
    public void move(Player player, Move move){

        //Get the corresponding player position.
        int[] loc = (player == Player.X)? locX : locO;

        //Set the position to the new one
        loc[0] = move.row;
        loc[1] = move.col;

        //Set the board for the new location as used.
        setBoard(true, loc[0], loc[1]);

    }

    /**
     * Remove the player from the current position and return it to the position specified.
     * @param player Player to move
     * @param move New position to move the player to
     */
    public void remove(Player player, Move move){

        //Get the corresponding player position.
        int[] loc = player == Player.X? locX : locO;

        //Set the board for the new location as unused.
        setBoard(false, loc[0], loc[1]);

        //Set the player's position to the new position.
        loc[0] = move.row;
        loc[1] = move.col;
        
    }

    /**
     * Gets a deep copy of the current board.
     * @return Copy of the board
     */
    public IsolationBoard copy(){

        //Create a new board
        IsolationBoard copy = new IsolationBoard();

        //Copy the player positions
        copy.locO = Arrays.copyOf(this.locO, locO.length);
        copy.locX = Arrays.copyOf(this.locX, locX.length);

        //Copy the current state of the board
        copy.usedSpaces = Arrays.copyOf(this.usedSpaces, usedSpaces.length);

        return copy;
    }

    /**
     * Gets a printable copy of the current board.
     * It will place each line of the board in a different array spot.
     * It will also contain the row and column markings on the sides.
     * @return Array containing the board.
     */
    public String[] getPrintableBoard(){

        //Create the array to be returned.
        String[] result = new String[9];

        //Set the column markings
        result[0] = "  1 2 3 4 5 6 7 8";

        //Use a StringBuilder object for more efficiency.
        StringBuilder builder = new StringBuilder();

        //Loop through the board's rows
        for(int row = 0; row < 8; row++){
            
            //Add the row marking
            builder.append((char)(65 + row) + " ");

            //Loop through the board's columns
            for(int col = 0; col < 8; col++){

                //If the space is occupied by the X player
                if(locX[0] == row && locX[1] == col)
                    builder.append("X ");
                //If the space is occupied by the O player
                else if(locO[0] == row && locO[1] == col)
                    builder.append("O ");
                //If the space is used
                else if(getBoard(row, col))
                    builder.append("# ");
                //If the space is empty
                else
                    builder.append("- ");
            }
            //Add the row to the array to return
            result[row+1] = builder.toString();
            //Empty the StringBuilder for the next row
            builder.delete(0, builder.length());
        }

        return result;
    }

    /**
     * Checks if the player specified has lost the game.
     * @param player Player to check
     * @return True if the player has lost the game, false otherwise.
     */
    public boolean isTerminal(Player player){
        //If the player has no more moves, it means the player lost the game.
        return this.getPossibleMoves(player).size() == 0;
    }

    /**
     * Checks to see if the move is valid for this player.
     * @param move Move to check the validity for.
     * @param player player to perform the move.
     * @return True if the move is valid, false otherwise.
     */
    public boolean isMoveValid(Move move, Player player){

        //If the space where the player wants to move is already used, it's invalid.
        if(getBoard(move.row, move.col))
            return false;
    
        //Get the location for the appropriate player.
        int[] loc = (player == Player.X)? locX : locO;
    
        //Check to see which directions the rows and columns differ by.
        //If the new position is down or to the left of the current position,
        //seet to negative. Set them to true if they are up or right.
        int rowMult = (loc[0] < move.row)? 1: -1;
        int colMult = (loc[1] < move.col)? 1: -1;
    
        //If they are in the same row or column, set them to 0.
        if(loc[0] == move.row)
            rowMult = 0;
        if(loc[1] == move.col)
            colMult = 0;
    
        //add the multipliers to the player position,
        //so that the current position isn't checked.
        int row = loc[0] + rowMult;
        int col = loc[1] + colMult;
    
        //Keep looping until both the rows and columns are equal.
        while(row != move.row || col != move.col){
    
            try{
                //If the space on the way to the specified location is occupied,
                //The move is invalid, so return false.
                if(getBoard(row, col))
                    return false;

            //This exception is thrown if an invalid location in the board is accessed.
            //This happens if a move is invalid. If the exception is thrown, catch it and return false.
            }catch(ArrayIndexOutOfBoundsException e){
                return false;
            }
    
            //Move the next move to check either up, down, left, or right,
            //depending on the multipliers calculated earlier.
            row += rowMult;
            col += colMult;
        }
    
        //If the row and column values are equal,
        //That means the move is a valid diagonal move.
        //If they are not equal, it isn't.
        return (row == move.row && col == move.col);
    
    }

    /**
     * Used to calculate a heuristic on how good a board configuration
     * is for a player compared to their opponent and available moves.
     * @param player Player to evaluate
     * @return a number to 'grade' the current configuration. A higher number is a greater score.
     */
    public int evaluate(Player player){

        //Get the location for this player
        int[] loc = player == Player.X? locX: locO;

        //Get the number of moves that each player has available to do.
        int computerMovesAvailable = getPossibleMoves(player).size();
        int opponentMovesAvailable = getPossibleMoves(Player.opponent(player)).size();

        //Matrix used to give higher weight to certain board positions.
        //In this case, higher weight is given to move in the center of the board.
        int[] boardPositionWeight = new int[]{
            0, 1, 2, 3, 3, 2, 1, 0,
            1, 2, 3, 4, 4, 3, 2, 1,
            2, 3, 4, 5, 5, 4, 3, 2,
            3, 4, 5, 6, 6, 5, 4, 3,
            3, 4, 5, 6, 6, 5, 4, 3,
            2, 3, 4, 5, 5, 4, 3, 2,
            1, 2, 3, 4, 4, 3, 2, 1,
            0, 1, 2, 3, 3, 2, 1, 0,
        };

        //Get the value of the weight by comparing it to the position of the player.
        int boardPositionWeightValue = boardPositionWeight[(loc[0] << 3) + loc[1]];

        //Formula:
        //2*(CPU_moves - Opp_moves) - boardPosWeight
        return ((computerMovesAvailable - opponentMovesAvailable) << 1) + boardPositionWeightValue;
    }

}