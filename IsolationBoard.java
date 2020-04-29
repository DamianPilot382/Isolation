import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class IsolationBoard {

    private int[] locX;
    private int[] locO;
    private boolean[][] usedSpaces;
    private LinkedList<int[]> moves;

    public IsolationBoard(){
        this.locX = new int[]{0, 0};
        this.locO = new int[]{7, 7};
        this.usedSpaces = new boolean[8][8];
        this.usedSpaces[0][0] = true;
        this.usedSpaces[7][7] = true;
        this.moves = new LinkedList<>();
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
                if(usedSpaces[loc[0] - i][loc[1]])
                    up = false;
                else
                    moves.add(new Move(loc[0] - i, loc[1]));
            }

            if(down){
                if(usedSpaces[loc[0] + i][loc[1]])
                    down = false;
                else
                    moves.add(new Move(loc[0] + i, loc[1]));
            }

            if(left){
                if(usedSpaces[loc[0]][loc[1]-i])
                    down = false;
                else
                    moves.add(new Move(loc[0], loc[1]-i));
            }
            
            if(right){
                if(usedSpaces[loc[0]][loc[1]+i])
                    down = false;
                else
                    moves.add(new Move(loc[0], loc[1]+i));
            }

            if(upLeft){
                if(usedSpaces[loc[0]-i][loc[1]-i])
                    down = false;
                else
                    moves.add(new Move(loc[0]-i, loc[1]-i));
            }

            if(upRight){
                if(usedSpaces[loc[0]-i][loc[1]+i])
                    down = false;
                else
                    moves.add(new Move(loc[0]-i, loc[1]+i));
            }

            if(downLeft){
                if(usedSpaces[loc[0]+i][loc[1]-i])
                    down = false;
                else
                    moves.add(new Move(loc[0]+i, loc[1]-i));
            }

            if(downRight){
                if(usedSpaces[loc[0]+i][loc[1]+i])
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

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder("  1 2 3 4 5 6 7 8\tComputer vs. Opponent\n");
        for(int i = 0; i < usedSpaces.length; i++){
            builder.append((char)(65 + i) + " ");
            for(int j = 0; j < usedSpaces[0].length; j++){
                if(locX[0] == i && locX[1] == j)
                    builder.append("X ");
                else if(locO[0] == i && locO[1] == j)
                    builder.append("O ");
                else if(usedSpaces[i][j])
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

}