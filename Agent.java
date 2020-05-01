import java.util.List;

public class Agent {

    private int time;
    private long start;
    private Player computerPlayer;
    private Player opponentPlayer;

    public Agent(int time, Player computerPlayer){

        this.time = time;

        this.computerPlayer = computerPlayer;
        this.opponentPlayer = (computerPlayer == Player.X)? Player.O : Player.X;

        this.maxDepth = 5;

    }

    public Move search(){
        return iterativeDeepening();
    }

    public Move alphaBetaSearch(IsolationBoard board, int depth){

        maxValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);

    }

    public int maxValue(IsolationBoard board, int alpha, int beta, int depth){

        if(board.isTerminal(this.computerPlayer))
            return Integer.MIN_VALUE;

        int v = Integer.MIN_VALUE;

        List<Move> moves = board.getPossibleMoves(computerPlayer);

        for(Move move : moves){
            v = Math.max(v, minValue(board, alpha, beta, depth - 1));
            if(v >= beta)
                return v;
            alpha = Math.max(alpha, v);
        }

        return v;

    }

    public int minValue(IsolationBoard board, int alpha, int beta, int depth){
        
        if(board.isTerminal(this.opponentPlayer))
            return Integer.MAX_VALUE;



        int v = Integer.MAX_VALUE;

        List<Move> moves = board.getPossibleMoves(opponentPlayer);

        for(Move move : moves){
            v = Math.min(v, maxValue(board, alpha, beta, depth - 1));

            if(v <= alpha){
                return v;
            }
            
                beta = Math.min(beta, v);
        }

        return v;
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