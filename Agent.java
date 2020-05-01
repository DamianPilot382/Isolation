import java.util.List;

public class Agent {

    private int time;
    private long start;
    private Player computerPlayer;
    private Player opponentPlayer;

    public Agent(int time, Player computerPlayer){

        this.time = time * 1000;

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
            return bestMove;
        }

    }

    private Move alphaBetaSearch(IsolationBoard board, int depth) throws NoTimeException {

        return maxValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE, depth).move;

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