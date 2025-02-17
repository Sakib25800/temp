import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a barracuda.
 * Barracudas age, move, eat sardines, and die.
 * 
 * @version 1.0
 */
public class Barracuda extends Animal {
    // Characteristics shared by all barracudas (class variables).
    private static final int BREEDING_AGE = 15;
    private static final int MAX_AGE = 150;
    private static final double BREEDING_PROBABILITY = 0.1;
    private static final int MAX_LITTER_SIZE = 4;
    private static final int SARDINE_FOOD_VALUE = 10;
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).
    private int age;
    private int foodLevel;

    public Barracuda(boolean randomAge, Location location) {
        super(location);
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
        } else {
            age = 0;
        }
        foodLevel = rand.nextInt(SARDINE_FOOD_VALUE);
    }

    public void act(Field currentField, Field nextFieldState, boolean isDay) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
            if (!freeLocations.isEmpty()) {
                if (isDay) {
                    giveBirth(nextFieldState, freeLocations);
                }
            }
            Location nextLocation = findFood(currentField);
            if (nextLocation == null && !freeLocations.isEmpty()) {
                nextLocation = freeLocations.remove(0);
            }
            if (nextLocation != null) {
                setLocation(nextLocation);
                nextFieldState.placeAnimal(this, nextLocation);
            } else {
                setDead();
            }
        }
    }

    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            setDead();
        }
    }

    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    private Location findFood(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while (foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            Animal animal = field.getAnimalAt(loc);
            if (animal instanceof Sardine sardine) {
                if (sardine.isAlive()) {
                    sardine.setDead();
                    foodLevel = SARDINE_FOOD_VALUE;
                    foodLocation = loc;
                }
            }
        }
        return foodLocation;
    }

    private void giveBirth(Field nextFieldState, List<Location> freeLocations) {
        int births = breed();
        if (births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Barracuda young = new Barracuda(false, loc);
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
