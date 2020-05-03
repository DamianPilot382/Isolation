import java.util.List;

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
public class Agent {

    //Time allocated for the compuer per move in milliseconds
    private int time;

    //Time when the computer began searching in milliseconds
    private long start;

    //Keeping track of which player is which piece.
    private Player computerPlayer;
    private Player opponentPlayer;

    //Initial depth to start searching for iterative deepening.
    public static final int INITIAL_DEPTH = 6;

    /**
     * Creates a new Agent object
     * @param time time in seconds that the computer is alloted per move.
     * @param computerPlayer piece to represent the computer.
     */
    public Agent(int time, Player computerPlayer){

        //Convert the time from seconds to milliseconds
        this.time = time * 1000;

        //Set the players' pieces
        this.computerPlayer = computerPlayer;
        this.opponentPlayer = Player.opponent(computerPlayer);

    }

    /**
     * Searches the board for the most optimal solution that it can find.
     * @param board Board to be searched.
     * @return Most optimal move that it could find or estimate.
     */
    public Move search(IsolationBoard board){

        //Start the timer
        start = System.currentTimeMillis();

        //Start searching
        return iterativeDeepening(board);
    }

    /**
     * Searches the board using iterative deepening.
     * @param board Board to be searched
     * @return Most optimal move that it could find or estimate.
     */
    private Move iterativeDeepening(IsolationBoard board) {

        //Initialize the best move found so far and an initial depth to search.
        Move bestMove = null;
        int depth = INITIAL_DEPTH;

        //Try-Catch to detect a NoTimeException.
        //It occurs when the computer runs out of alloted time.
        try{

            //Keep searching until the timer expires.
            while(true){

                //search using minmax alpha-beta search
                bestMove = alphaBetaSearch(board, depth);
    
                //Increase the depth to search on each iteration.
                depth++;
            }
        }catch(NoTimeException e){
            //At this point, comptuer has run out of time.

            //If no solution has been found, prompt so and return a random possible move.
            if(bestMove == null){
                System.out.println("Computer ran out of time. Random move generated.");
                return board.getPossibleMoves(this.computerPlayer).get(0);
            }
        }
        
        //Return the best move found
        return bestMove;

    }

    /**
     * Search the board using an alpha-beta search with iterative deepening.
     * @param board Board to be searched
     * @param depth maximum depth to search
     * @return Most optimal move that it could find or estimate.
     * @throws NoTimeException When the alloted time has expired.
     */
    private Move alphaBetaSearch(IsolationBoard board, int depth) throws NoTimeException {

        //Create a copy of the board. Not really needed but good practice.
        IsolationBoard boardCopy = board.copy();

        //Get the move that produced the value that maximizes the options for the computer player.
        return maxValue(boardCopy, Integer.MIN_VALUE, Integer.MAX_VALUE, depth).move;

    }

    /**
     * Function to maximize the value of the minmax function.
     * This acts as the best move that the agent can make based on the best move that the opponent can make.
     * It also uses alpha-beta pruning to avoid searching branches that don't lead to a better outcome.
     * @param board Board to search
     * @param alpha Alpha value that the max function can guarantee
     * @param beta Beta value that the min function can guarantee
     * @param depth depth maximum of the iterative deepening
     * @return A tuple containing the best move and the heurisitc or terminal value.
     * @throws NoTimeException Thrown when the alloted time for the move has expired.
     */
    private MinMaxTuple maxValue(IsolationBoard board, int alpha, int beta, int depth) throws NoTimeException {

        //Check if the timer has expired
        checkTime();

        //Check if this board configuration lead to the computer losing.
        if(board.isTerminal(this.computerPlayer))
            //If the computer loses here, return the lowest possible number so that this path isn't taken.
            return new MinMaxTuple(null, Integer.MIN_VALUE);

        //If the maximum depth for iterative deepening is reached, return the value of the evaluate function.
        if(depth <= 0)
            return new MinMaxTuple(null, board.evaluate(this.computerPlayer));

        //Set v to the lowest possible number
        int v = Integer.MIN_VALUE;

        //Get the moves available with this board configuration
        List<Move> moves = board.getPossibleMoves(this.computerPlayer);

        Move bestMove = null;

        //Loop through all the moves available
        for(Move move : moves){

            //Make the move on the board.
            board.move(this.computerPlayer, move);

            //Get the minimum value of the recursive call to minValue() with a lower depth.
            MinMaxTuple minTuple = minValue(board, alpha, beta, depth - 1);

            //If this move is better than the current best move, update the best move.
            if(v < minTuple.value){
                v = minTuple.value;
                bestMove = move;
            }

            //Remove the move from the board. This is to use one board and save memory and time from making copies.
            board.remove(this.computerPlayer, move);

            //If this value is higher than that produced by the minimize algorithm, just return it.
            //There is no point to keep searching, as a different node's value is returned.
            if(v >= beta)
                return new MinMaxTuple(move, v);

            //Update alpha with the best value found so far.
            alpha = Math.max(alpha, v);
        }

        //Return the best move found.
        return new MinMaxTuple(bestMove, v);

    }

    /**
     * Function to minimize the value of the minmax function.
     * This acts as the best move that the opponent can make based on the best move that the agent can make.
     * It also uses alpha-beta pruning to avoid searching branches that don't lead to a better outcome.
     * @param board Board to search
     * @param alpha Alpha value that the max function can guarantee
     * @param beta Beta value that the min function can guarantee
     * @param depth depth maximum of the iterative deepening
     * @return A tuple containing the best move and the heurisitc or terminal value.
     * @throws NoTimeException Thrown when the alloted time for the move has expired.
     */
    private MinMaxTuple minValue(IsolationBoard board, int alpha, int beta, int depth) throws NoTimeException {

        //Check if the timer has expired
        checkTime();

        //Check if this board configuration lead to the opponent losing.
        if(board.isTerminal(this.opponentPlayer))
            //If the opponent loses here, return the highest possible number so that this path is taken.
            return new MinMaxTuple(null, Integer.MAX_VALUE);

        //If the maximum depth for iterative deepening is reached, return the value of the evaluate function.
        if(depth <= 0)
            return new MinMaxTuple(null, board.evaluate(this.opponentPlayer));

        //Set v to the highest possible number
        int v = Integer.MAX_VALUE;

        //Get the moves available with this board configuration
        List<Move> moves = board.getPossibleMoves(this.opponentPlayer);

        Move bestMove = null;

        //Loop through all the moves available
        for(Move move : moves){

            //Make the move on the board.
            board.move(this.opponentPlayer, move);

            //Get the maximum value of the recursive call to maxValue() with a lower depth.
            MinMaxTuple maxTuple = maxValue(board, alpha, beta, depth - 1);

            //If this move is better than the current best move, update the best move.
            if(v > maxTuple.value){
                v = maxTuple.value;
                bestMove = move;
            }

            //Remove the move from the board. This is to use one board and save memory and time from making copies.
            board.remove(this.opponentPlayer, move);

            //If this value is less than that produced by the maximize algorithm, just return it.
            //There is no point to keep searching, as a different node's value is returned.
            if(v <= alpha)
                return new MinMaxTuple(move, v);

            //Update alpha with the best value found so far.
            beta = Math.min(beta, v);
        }

        //Return the best move found.
        return new MinMaxTuple(bestMove, v);

    }

    /**
     * Auxiliary method to check if the time alloted for the computer to make
     * a move has expired.
     * @throws NoTimeException When the timer runs out.
     */
    private void checkTime() throws NoTimeException {

        //Compare the current time to the start time.
        //If the time ellapsed is greater than the time alloted,
        //throw the exception.
        if((System.currentTimeMillis() - start) > time)
            throw new NoTimeException();
    }

    /**
     * Auxiliary class to store the move performed and the value of that move
     * at each step of the max and min value functions.
     */
    private class MinMaxTuple {

        //Allocate space for the move and the value of the evaluate function.
        public Move move;
        public int value;
    
        /**
         * Constructor for the tuple.
         * @param move Move to store
         * @param value value of the evaluate function
         */
        public MinMaxTuple(Move move, int value){
            this.move = move;
            this.value = value;
        }
    }

}