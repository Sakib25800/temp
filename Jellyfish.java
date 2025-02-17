import java.util.List;
import java.util.Random;

/**
 * A simple model of a jellyfish.
 * Jellyfish age, move, breed, and die.
 *
 * @version 1.0
 */
public class Jellyfish extends Animal {

    private static final int BREEDING_AGE = 1;
    private static final int MAX_AGE = 30;
    private static final double BREEDING_PROBABILITY = 0.3;
    private static final int MAX_LITTER_SIZE = 12;
    private static final Random rand = Randomizer.getRandom();

    private int age;

    public Jellyfish(boolean randomAge, Location location) {
        super(location);
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
        } else {
            age = 0;
        }
    }

    public void act(Field currentField, Field nextFieldState, boolean isDay) {
        incrementAge();
        if (isAlive()) {
            List<Location> freeLocations =
                nextFieldState.getFreeAdjacentLocations(getLocation());
            Location nextLocation = null;

            if (!isDay && !freeLocations.isEmpty()) {
                // Night behavior - rise to surface (upward movement)
                List<Location> upwardLocations = freeLocations
                    .stream()
                    .filter(loc -> loc.row() < getLocation().row())
                    .toList();
                if (!upwardLocations.isEmpty()) {
                    nextLocation = upwardLocations.get(0);
                    giveBirth(nextFieldState, freeLocations);
                }
            } else if (isDay && !freeLocations.isEmpty()) {
                // Day behavior - normal movement
                nextLocation = freeLocations.remove(0);
            }

            if (nextLocation != null) {
                setLocation(nextLocation);
                nextFieldState.placeAnimal(this, nextLocation);
            }
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
                Jellyfish young = new Jellyfish(false, loc);
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
