import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class IsolationBoard {

    protected int[] locX;
    protected int[] locO;
    protected boolean[] usedSpaces;
    protected List<Move> movesX;
    protected List<Move> movesO;


    public IsolationBoard(){
        this.locX = new int[]{0, 0};
        this.locO = new int[]{7, 7};
        this.usedSpaces = new boolean[64];
        this.usedSpaces[0] = true;
        this.usedSpaces[64] = true;
    }

    public List<Move> getPossibleMoves(Player player){

        List<Move> moves = new ArrayList<Move>(32);

        int[] loc;
        if(player == Player.X){
            if(movesX != null)
                return movesX;

            loc = locX;
            movesX = moves;
        }else{
            if(movesO != null)
                return movesO;

            loc = locO;
            movesO = moves;
        }

        boolean left = true;
        boolean right = true;
        boolean up = true;
        boolean down = true;
        boolean upLeft = true;
        boolean upRight = true;
        boolean downLeft = true;
        boolean downRight = true;

        for(int i = 1; i < 7; i++){
            
            if(!(left || right || up || down || upLeft || upRight || downLeft || downRight))
                break;

            if(loc[0] - i < 0){
                up = false;
                upLeft = false;
                upRight = false;
            }

            if(loc[0] + i > 7){
                down = false;
                downLeft = false;
                downRight = false;
            }

            if(loc[1] - i < 0){
                left = false;
                upLeft = false;
                downLeft = false;
            }

            if(loc[1] + i > 7){
                right = false;
                upRight = false;
                downRight = false;
            }

            up = checkMoveAuxiliary(up, loc, moves, -i, 0);
            down = checkMoveAuxiliary(down, loc, moves, i, 0);
            left = checkMoveAuxiliary(left, loc, moves, 0, -i);
            right = checkMoveAuxiliary(right, loc, moves, 0, i);

            upLeft = checkMoveAuxiliary(upLeft, loc, moves, -i, -i);
            upRight = checkMoveAuxiliary(upRight, loc, moves, -i, i);
            downLeft = checkMoveAuxiliary(downLeft, loc, moves, i, -i);
            downRight = checkMoveAuxiliary(downRight, loc, moves, i, i);

        }

        return moves;
    }

    private boolean checkMoveAuxiliary(boolean value, int[] loc, List<Move> moves, int rowOffset, int colOffset){
        
        if(value){
            if(getBoard(loc[0]+rowOffset, loc[1]+colOffset))
                return false;
            
            moves.add(new Move(loc[0]+rowOffset, loc[1]+colOffset));
            return true;
        }
        return false;
    }

    private void setBoard(boolean value, int row, int col){
        usedSpaces[(row << 3) + col] = value;
    }

    private boolean getBoard(int row, int col){
        return usedSpaces[(row << 3) + col];
    }

    public void move(Player player, Move move){
        int[] loc = player == Player.X? locX : locO;

        loc[0] = move.row;
        loc[1] = move.col;

        setBoard(true, loc[0], loc[1]);

        movesX = null;
        movesO = null;
    }

    public void remove(Player player, Move move){
        int[] loc = player == Player.X? locX : locO;

        setBoard(false, loc[0], loc[1]);

        loc[0] = move.row;
        loc[1] = move.col;

        movesX = null;
        movesO = null;

    }

    public IsolationBoard copy(){
        IsolationBoard copy = new IsolationBoard();

        copy.locO = Arrays.copyOf(this.locO, locO.length);
        copy.locX = Arrays.copyOf(this.locX, locX.length);

        copy.usedSpaces = Arrays.copyOf(this.usedSpaces, usedSpaces.length);

        return copy;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder("  1 2 3 4 5 6 7 8\tComputer vs. Opponent\n");
        for(int i = 0; i < 8; i++){
            builder.append((char)(65 + i) + " ");
            for(int j = 0; j < 8; j++){
                if(locX[0] == i && locX[1] == j)
                    builder.append("X ");
                else if(locO[0] == i && locO[1] == j)
                    builder.append("O ");
                else if(getBoard(i, j))
                    builder.append("# ");
                else
                    builder.append("- ");
            }
            builder.append("\t   " + (i+1) + ". " + 123 + "\n");
        }

        return builder.toString();
    }

    public boolean isTerminal(Player player){
        return this.getPossibleMoves(player).size() == 0;
    }

    public boolean isMoveValid(Move move, Player player){

        if(getBoard(move.row, move.col))
            return false;
    
        int[] loc = (player == Player.X)? locX : locO;
    
        int rowMult = (loc[0] < move.row)? 1: -1;
        int colMult = (loc[1] < move.col)? 1: -1;
    
        if(loc[0] == move.row)
            rowMult = 0;
        if(loc[1] == move.col)
            rowMult = 0;
    
        int row = loc[0] + rowMult;
        int col = loc[1] + colMult;
    
        while(row != move.row && col != move.col){
    
            try{
                if(getBoard(row, col))
                    return false;
            }catch(ArrayIndexOutOfBoundsException e){
                return false;
            }
    
            row += rowMult;
            col += colMult;
        }
    
        return (row == move.row && col == move.col);
    
    }

    public int evaluate(Player player){
        int[] loc = player == Player.X? locX: locO;

        int computerMovesAvailable = getPossibleMoves(player).size();
        int opponentMovesAvailable = getPossibleMoves(Player.opponent(player)).size();

        int[] closenessToCenterMatrix = new int[]{
            0, 1, 2, 3, 3, 2, 1, 0,
            1, 2, 3, 4, 4, 3, 2, 1,
            2, 3, 4, 5, 5, 4, 3, 2,
            3, 4, 5, 6, 6, 5, 4, 3,
            3, 4, 5, 6, 6, 5, 4, 3,
            2, 3, 4, 5, 5, 4, 3, 2,
            1, 2, 3, 4, 4, 3, 2, 1,
            0, 1, 2, 3, 3, 2, 1, 0,
        };

        int closenessToCenterValue = closenessToCenterMatrix[(loc[0] << 3) + loc[1]];

        return ((computerMovesAvailable - opponentMovesAvailable) << 1) + closenessToCenterValue;
    }

}