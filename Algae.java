import java.util.List;
import java.util.Random;

/**
 * A model of a an algae - this is the plant challenge task but plants don't
 * make sense in sea-life.
 * Algae constantly grow, and reproduce, but never die.
 * 
 * @author Sakibul Islam
 * @version 1.0
 */
public class Algae extends Organism {
    private static final double REPRODUCTION_PROBABILITY = 0.1;

    public Algae(Location location) {
        super(location);
    }

    @Override
    public void act(Field currentField, Field nextField, boolean isDay) {
        if (!isAlive()) {
            return;
        }
        Random rand = Randomizer.getRandom();
        if (rand.nextDouble() < REPRODUCTION_PROBABILITY) {
            List<Location> free = currentField.getFreeAdjacentLocations(getLocation());
            if (!free.isEmpty()) {
                Location loc = free.get(0);
                Algae newAlgae = new Algae(loc);
                nextField.placeAnimal(newAlgae, loc);
            }
        }

        // Keep algae in the next field.
        nextField.placeAnimal(this, getLocation());
    }
}
