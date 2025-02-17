import java.util.List;
import java.util.Random;

/**
 * A simple model of a shark.
 * Sharks age, move, eat tuna, and die.
 * More active at night, requires gender matching for breeding.
 * 
 * @version 1.0
 */
public class Shark extends Animal {
    private static final int BREEDING_AGE = 20;
    private static final int MAX_AGE = 200;
    private static final double BREEDING_PROBABILITY = 0.05;
    private static final int MAX_LITTER_SIZE = 3;
    private static final int TUNA_FOOD_VALUE = 15;
    private static final Random rand = Randomizer.getRandom();

    private int age;
    private int foodLevel;
    private boolean isMale;

    public Shark(boolean randomAge, Location location) {
        super(location);
        age = randomAge ? rand.nextInt(MAX_AGE) : 0;
        foodLevel = rand.nextInt(TUNA_FOOD_VALUE);
        isMale = rand.nextBoolean();
    }

    public void act(Field currentField, Field nextFieldState, boolean isDay) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
            Location nextLocation = null;

            if (!isDay) {
                // Night behavior - more aggressive hunting and breeding
                nextLocation = findFood(currentField);
                if (!freeLocations.isEmpty()) {
                    giveBirth(currentField, nextFieldState, freeLocations);
                }
            } else {
                // Day behavior - less active hunting
                nextLocation = findFood(currentField);
                if (nextLocation == null && !freeLocations.isEmpty() && rand.nextDouble() < 0.5) {
                    nextLocation = freeLocations.remove(0);
                }
            }

            if (nextLocation != null) {
                setLocation(nextLocation);
                nextFieldState.placeAnimal(this, nextLocation);
            }
        }
    }

    private Location findFood(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Animal animal = field.getAnimalAt(loc);
            if (animal instanceof Tuna tuna && tuna.isAlive()) {
                tuna.setDead();
                foodLevel = TUNA_FOOD_VALUE;
                return loc;
            }
        }
        return null;
    }

    private void giveBirth(Field currentField, Field nextFieldState, List<Location> freeLocations) {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            List<Location> adjacent = currentField.getAdjacentLocations(getLocation());
            for (Location loc : adjacent) {
                Animal animal = currentField.getAnimalAt(loc);
                if (animal instanceof Shark shark && shark.isAlive() &&
                        shark.isMale() != this.isMale()) {
                    births = rand.nextInt(MAX_LITTER_SIZE) + 1;
                    break;
                }
            }
        }

        for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
            Location loc = freeLocations.remove(0);
            Shark young = new Shark(false, loc);
            nextFieldState.placeAnimal(young, loc);
        }
    }

    private void incrementAge() {
        age++;
        if (age > MAX_AGE)
            setDead();
    }

    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0)
            setDead();
    }

    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    public boolean isMale() {
        return isMale;
    }
}
