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

    public Move search(IsolationBoard board){
        start = System.currentTimeMillis();
        return iterativeDeepening(board);
    }

    private Move iterativeDeepening(IsolationBoard board) {
        Move bestMove = null;
        int depth = 3;

        try{
            while(true){
                bestMove = alphaBetaSearch(board, depth);
    
                depth++;
            }
        }catch(NoTimeException e){
            if(bestMove == null){
                System.out.println("Computer ran out of time. Random move generated.");
                return board.getPossibleMoves(this.computerPlayer).get(0);
            }
            return bestMove;
        }

    }

    private Move alphaBetaSearch(IsolationBoard board, int depth) throws NoTimeException {

        IsolationBoard boardCopy = board.copy();

        return maxValue(boardCopy, Integer.MIN_VALUE, Integer.MAX_VALUE, depth).move;

    }

    private MinMaxTuple maxValue(IsolationBoard board, int alpha, int beta, int depth) throws NoTimeException {

        checkTime();

        if(board.isTerminal(this.computerPlayer))
            return new MinMaxTuple(null, Integer.MIN_VALUE);

        if(depth == 0)
            return new MinMaxTuple(null, board.evaluate(this.computerPlayer));

        int v = Integer.MIN_VALUE;

        List<Move> moves = board.getPossibleMoves(computerPlayer);

        Move bestMove = null;

        for(Move move : moves){
            board.move(this.computerPlayer, move);

            MinMaxTuple minTuple = minValue(board, alpha, beta, depth - 1);

            if(v < minTuple.value){
                v = minTuple.value;
                bestMove = minTuple.move;
            }

            board.remove(this.computerPlayer, move);

            if(v >= beta)
                return new MinMaxTuple(move, v);

            alpha = Math.max(alpha, v);
        }

        return new MinMaxTuple(bestMove, v);

    }

    private MinMaxTuple minValue(IsolationBoard board, int alpha, int beta, int depth) throws NoTimeException {

        checkTime();

        if(board.isTerminal(this.opponentPlayer))
            return new MinMaxTuple(null, Integer.MAX_VALUE);

        if(depth == 0)
            return new MinMaxTuple(null, board.evaluate(this.opponentPlayer));

        int v = Integer.MAX_VALUE;

        List<Move> moves = board.getPossibleMoves(opponentPlayer);

        Move bestMove = null;

        for(Move move : moves){
            board.move(this.opponentPlayer, move);

            MinMaxTuple maxTuple = maxValue(board, alpha, beta, depth - 1);

            if(v > maxTuple.value){
                v = maxTuple.value;
                bestMove = maxTuple.move;
            }

            board.remove(this.opponentPlayer, move);

            if(v <= alpha)
                return new MinMaxTuple(move, v);

            beta = Math.min(beta, v);
        }

        return new MinMaxTuple(bestMove, v);

    }

    private void checkTime() throws NoTimeException {
        if((System.currentTimeMillis() - start) > time)
            throw new NoTimeException();
    }

    private class MinMaxTuple {

        public Move move;
        public int value;
    
        public MinMaxTuple(Move move, int value){
            this.move = move;
            this.value = value;
        }
    }

}