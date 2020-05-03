/**
 * @author Damian Ugalde
 * @date 2020-05-03
 * @version 1.0
 *
 * Project 4 - Isolation
 * CS 4200 - Artificial Intelligence
 * California State Polytechnic University, Pomona
 * Computer Science Department
 *
 * Instructor: Dominick A. Atanasio
 *
 */
public enum Player{
    X, O;

    /**
     * Gets the opposite of the player.
     * @param player player to get the opposite of.
     * @return Opposite of the player.
     */
    public static Player opponent(Player player){
        return (player == X)? O : X;
    }
}