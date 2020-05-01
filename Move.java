
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

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }
    
}