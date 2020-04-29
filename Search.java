
public class Search {


    public Move IterativeDeepeningSearch(IsolationBoard board){
        Move bestMove = null;
        for(int i = 0; i < 5; i++){
            bestMove
        }

        return bestMove;
    }


    public Move depthLimitedSearch(Board board, int limit){

    }


    public Move alphaBetaSearch(IsolationBoard board){
        return this.abValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public int abValue(boolean isMax, IsolationBoard board, int alpha, int beta){
        if(board.gameOver())
            return board.value();

        int v = isMax? Integer.MIN_VALUE : Integer.MAX_VALUE;
        
        Move[] successors = board.getSuccessors();

        for(Move move : successors){
            v = isMax? Math.max(v, abValue(false, board, alpha, beta)):
               Math.min(v, abValue(true, board, alpha, beta));
            if(v >= beta)
                return v;
            alpha = Math.max(alpha, v);
        }

        return v;

    }


}