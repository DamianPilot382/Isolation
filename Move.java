
public class Move {
    protected int row;
    protected int col;

    public Move(int row, int col){
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString(){
        return ""+((char)(65+row)) + (col+1);
    }
    
}