import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class IsolationBoard {

    protected int[] locX;
    protected int[] locO;
    protected boolean[] usedSpaces;
    protected int spacesLeft;


    public IsolationBoard(){
        this.locX = new int[]{0, 0};
        this.locO = new int[]{7, 7};
        this.usedSpaces = new boolean[64];
        this.usedSpaces[0] = true;
        this.usedSpaces[64] = true;
        this.spacesLeft = 62;
    }

    public List<Move> getPossibleMoves(Player player){

        List<Move> moves = new ArrayList<Move>(32);

        int[] loc = (player == Player.X)? locX : locO;

        boolean left = true;
        boolean right = true;
        boolean up = true;
        boolean down = true;
        boolean upLeft = true;
        boolean upRight = true;
        boolean downLeft = true;
        boolean downRight = true;

        for(int i = 1; i < 8; i++){

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

            if(up){
                if(getBoard(loc[0]-i, loc[1]))
                    up = false;
                else
                    moves.add(new Move(loc[0] - i, loc[1]));
            }

            if(down){
                if(getBoard(loc[0]+i, loc[1]))
                    down = false;
                else
                    moves.add(new Move(loc[0] + i, loc[1]));
            }

            if(left){
                if(getBoard(loc[0], loc[1]-i))
                    down = false;
                else
                    moves.add(new Move(loc[0], loc[1]-i));
            }
            
            if(right){
                if(getBoard(loc[0], loc[1]+i))
                    down = false;
                else
                    moves.add(new Move(loc[0], loc[1]+i));
            }

            if(upLeft){
                if(getBoard(loc[0]-i, loc[1]-i))
                    down = false;
                else
                    moves.add(new Move(loc[0]-i, loc[1]-i));
            }

            if(upRight){
                if(getBoard(loc[0]-i, loc[1]+i))
                    down = false;
                else
                    moves.add(new Move(loc[0]-i, loc[1]+i));
            }

            if(downLeft){
                if(getBoard(loc[0]+i, loc[1]-i))
                    down = false;
                else
                    moves.add(new Move(loc[0]+i, loc[1]-i));
            }

            if(downRight){
                if(getBoard(loc[0]+i, loc[1]+i))
                    down = false;
                else
                    moves.add(new Move(loc[0]+i, loc[1]+i));
            }
        }

        for(Move move : moves){
            System.out.println(move);
        }

        return moves;
    }

    public void setBoard(boolean value, int row, int col){
        usedSpaces[(row << 3) + col] = value;
    }

    public boolean getBoard(int row, int col){
        return usedSpaces[(row << 3) + col];
    }

    public void move(Player player, Move move){
        int[] loc = player == Player.X? locX : locO;
        setBoard(true, loc[0], loc[1]);
        spacesLeft--;
    }

    public IsolationBoard copy(){
        IsolationBoard copy = new IsolationBoard();

        copy.locO = Arrays.copyOf(this.locO, locO.length);
        copy.locX = Arrays.copyOf(this.locX, locX.length);

        copy.usedSpaces = Arrays.copyOf(this.usedSpaces, usedSpaces.length);

        copy.spacesLeft = this.spacesLeft;

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

    public long getHashValue(){
        return Arrays.hashCode(usedSpaces);
    }

    public int getPositionsHash(){
        int result = locX[0];
        result = (result << 3) + locX[1];

        result = (result << 3) + locO[0];
        result = (result << 3) + locO[1];

        return result;

    }

    public int value(Heuristic heuristic){
        switch(heuristic){
            case MOVE_COUNT:
                return getMoveCountHeuristic();
            default:
                return 0;
        }

    }

    private int getMoveCountHeuristic(){
        return -1;
    }

}