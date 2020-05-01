public enum Player{
    X, O;

    public static Player opponent(Player player){
        if(player == X)
            return O;
        else
            return X;
    }
}