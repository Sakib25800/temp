import java.util.Random;

/**
 * Provide control over the randomization of the simulation.
 * 
 * @author Sakibul Islam
 * @version 1.0
 */
public class Randomizer {
    private static final Random rand = new Random();

    public static Random getRandom() {
        return rand;
    }
}
