
public class Move {
    private int row;
    private int col;

    public Move(int row, int col){
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString(){
        return ""+((char)(65+row)) + (col+1);
    }

    public static void main(String[] args){
        Move move = new Move(7, 7);
        System.out.println(move);
    }
    
}