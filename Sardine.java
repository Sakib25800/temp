import java.util.List;
import java.util.Random;

/**
 * A model of a sardine.
 * 
 * @version 1.0
 */
public class Sardine extends Organism {
    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 50;
    private static final double BREEDING_PROBABILITY = 0.2;
    private static final int MAX_LITTER_SIZE = 8;
    private static final Random rand = Randomizer.getRandom();

    private int age;

    public Sardine(boolean randomAge, Location location) {
        super(location);

        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
        } else {
            age = 0;
        }
    }

    public void act(Field currentField, Field nextFieldState, boolean isDay) {
        incrementAge();

        if (!isAlive()) {
            return;
        }

        List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
        Location nextLocation = null;

        if (isDay && !freeLocations.isEmpty()) {
            // Day behavior - normal movement and breeding
            giveBirth(nextFieldState, freeLocations);
            if (!freeLocations.isEmpty()) {
                nextLocation = freeLocations.remove(0);
            }
        } else if (!isDay) {
            // Night behavior - reduced movement (schooling)
            if (!freeLocations.isEmpty() && rand.nextDouble() < 0.3) {
                nextLocation = freeLocations.remove(0);
            }
            nextLocation = nextLocation == null ? getLocation() : nextLocation;
        }

        if (nextLocation != null) {
            setLocation(nextLocation);
            nextFieldState.placeAnimal(this, nextLocation);
        }
    }

    private void incrementAge() {
        age++;

        if (age > MAX_AGE) {
            setDead();
        }
    }

    private void giveBirth(Field nextFieldState, List<Location> freeLocations) {
        int births = breed();

        if (births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Sardine young = new Sardine(false, loc);
                nextFieldState.placeAnimal(young, loc);
            }
        }
    }

    private int breed() {
        int births;

        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        } else {
            births = 0;
        }

        return births;
    }

    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }
}
