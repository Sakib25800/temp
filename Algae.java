import java.util.List;
import java.util.Random;

public class Algae extends Animal {
    // Chance for algae to reproduce.
    private static final double REPRODUCTION_PROBABILITY = 0.05;

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
