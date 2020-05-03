
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
public class Move {
    protected int row;
    protected int col;

    /**
     * Creates a move object.
     * This may or may not be a valid move for the current board.
     * Validation is done by other classes. This simply stores the data.
     * This move is intended to be 0-indexed.
     * @param row Row for the move.
     * @param col Column for the move.
     */
    public Move(int row, int col){
        this.row = row;
        this.col = col;
    }

    /**
     * It replaces the row with the alphabetic representation.
     * It also shifts from a 0-index to a 1-index.
     * @return A String representation of the Move.
     */
    @Override
    public String toString(){
        return ""+((char)(65+row)) + (col+1);
    }
    
}