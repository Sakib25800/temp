import java.util.List;
import java.util.Random;

/**
 * A simple model of a tuna.
 * Tuna age, move, breed, and die.
 * 
 * @version 1.0
 */
public class Tuna extends Organism {
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 100;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 5;
    private static final Random rand = Randomizer.getRandom();

    private int age;
    private boolean isMale;

    public Tuna(boolean randomAge, Location location) {
        super(location);
        age = randomAge ? rand.nextInt(MAX_AGE) : 0;
        isMale = rand.nextBoolean();
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
            giveBirth(currentField, nextFieldState, freeLocations);
            if (!freeLocations.isEmpty()) {
                nextLocation = freeLocations.remove(0);
            }
        } else if (!isDay) {
            // Night behavior - reduced movement
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

    private void giveBirth(Field currentField, Field nextFieldState, List<Location> freeLocations) {
        int births = 0;

        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            List<Location> adjacent = currentField.getAdjacentLocations(getLocation());
            for (Location loc : adjacent) {
                Organism animal = currentField.getAnimalAt(loc);
                if (animal instanceof Tuna tuna && tuna.isAlive() &&
                        tuna.isMale() != this.isMale()) {
                    births = rand.nextInt(MAX_LITTER_SIZE) + 1;
                    break;
                }
            }
        }

        for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
            Location loc = freeLocations.remove(0);
            Tuna young = new Tuna(false, loc);
            nextFieldState.placeAnimal(young, loc);
        }
    }

    private void incrementAge() {
        age++;
        if (age > MAX_AGE)
            setDead();
    }

    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    public boolean isMale() {
        return isMale;
    }
}
