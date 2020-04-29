import java.util.LinkedList;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

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

    public Move[] getPossibleMoves(Player player){

        List<Move> moves = new ArrayList<Move>(32);

        int[] loc = (player == Player.X)? locX : locO;

        int bestLow = 0;
        int col = 0;

        for(col = 0; col < 8; col++){

            if(usedSpaces[loc[0]][col]){
                if(col < loc[1])
                    bestLow = col;
                else if(col == loc[1])
                    continue;
                else{

                    break;

                    //CORRECT BESTLOW HERE


                }
            }
        }

        while(++bestLow <= col){
            moves.add(new Move(loc[0], bestLow++));
        }

















        for(int col = loc[1]-1; col >= 0; col--){
            if(usedSpaces[loc[0]][col])
                break;

            moves.add(new Move(loc[0], col));
        }

        for(int col = loc[1]+1; col < 8; col++){
            if(usedSpaces[loc[0]][col])
                break;
            
            moves.add(new Move(loc[0], col));
        }

        for(int row = loc[0]-1; row >= 0; row--){
            if(usedSpaces[row][loc[0]])
                break;
            
            moves.add(new Move(row, loc[1]));
        }

        for(int row = loc[0]+1; row < 8; row++){
            if(usedSpaces[row][loc[1]])
                break;
            
            moves.add(new Move(row, loc[1]));
        }
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

    public static void startGame(Scanner in){

        char next = ' ';

        while(true){

            System.out.print("Who goes first, C for computer, O for opponent: ");

            next = in.next().toUpperCase().charAt(0);
            in.nextLine();

            if(next == 'C' || next == 'O')
                break;

            System.out.println("Invalid input. Try again.");

        }

        if(next == 'C')
            System.out.println("The computer will start the game.");
        else
            System.out.println("The opponent will start the game.");
    }

    public static void main(String[] args){

        startGame(new Scanner(System.in));
        

        IsolationBoard isolation = new IsolationBoard();

        System.out.println(isolation);
    }

    

}