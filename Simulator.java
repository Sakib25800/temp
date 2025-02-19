import java.util.*;

/**
 * A simple predator-prey simulator, based on a rectangular field containing
 * sharks, barracudas, tunas, sardines, and jellyfish.
 * 
 * @version 1.0
 */
public class Simulator {
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a shark will be created in any given grid position.
    private static final double SHARK_CREATION_PROBABILITY = 0.02;
    // The probability that a barracuda will be created in any given grid position.
    private static final double BARRACUDA_CREATION_PROBABILITY = 0.02;
    // The probability that a tuna will be created in any given position.
    private static final double TUNA_CREATION_PROBABILITY = 0.08;
    // The probability that a sardine will be created in any given position.
    private static final double SARDINE_CREATION_PROBABILITY = 0.1;
    // The probability that a jellyfish will be created in any given position.
    private static final double JELLYFISH_CREATION_PROBABILITY = 0.05;

    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;
    // Whether it is currently day or night.
    private boolean isDay;
    // New weather variable and Random instance
    private String weather;
    private final Random rand = Randomizer.getRandom();

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * 
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        field = new Field(depth, width);
        view = new SimulatorView(depth, width);
        isDay = true;
        weather = "Sunny"; // default weather

        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long
     * period (700 steps).
     */
    public void runLongSimulation() {
        simulate(700);
    }

    /**
     * Run the simulation for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * 
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps) {
        reportStats();
        for (int n = 1; n <= numSteps && field.isViable(); n++) {
            simulateOneStep();
            delay(50); // adjust this to change execution speed
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each animal.
     */
    public void simulateOneStep() {
        step++;
        isDay = !isDay; // Toggle day/night
        updateWeather(); // update weather condition
        Field nextFieldState = new Field(field.getDepth(), field.getWidth());

        List<Animal> animals = field.getAnimals();
        for (Animal anAnimal : animals) {
            anAnimal.act(field, nextFieldState, isDay);
        }

        field = nextFieldState;
        // Use the improved logging output
        reportStats();
        view.showStatus(step, field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        populate();
        view.showStatus(step, field);
    }

    /**
     * Randomly populate the field with sharks, barracudas, tunas, sardines, and
     * jellyfish.
     */
    private void populate() {
        Random rand = Randomizer.getRandom();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                double randDouble = rand.nextDouble();
                Location location = new Location(row, col);
                if (randDouble <= SHARK_CREATION_PROBABILITY) {
                    Shark shark = new Shark(true, location);
                    field.placeAnimal(shark, location);
                } else if (randDouble <= SHARK_CREATION_PROBABILITY + BARRACUDA_CREATION_PROBABILITY) {
                    Barracuda barracuda = new Barracuda(true, location);
                    field.placeAnimal(barracuda, location);
                } else if (randDouble <= SHARK_CREATION_PROBABILITY + BARRACUDA_CREATION_PROBABILITY
                        + TUNA_CREATION_PROBABILITY) {
                    Tuna tuna = new Tuna(true, location);
                    field.placeAnimal(tuna, location);
                } else if (randDouble <= SHARK_CREATION_PROBABILITY + BARRACUDA_CREATION_PROBABILITY
                        + TUNA_CREATION_PROBABILITY + SARDINE_CREATION_PROBABILITY) {
                    Sardine sardine = new Sardine(true, location);
                    field.placeAnimal(sardine, location);
                } else if (randDouble <= SHARK_CREATION_PROBABILITY + BARRACUDA_CREATION_PROBABILITY
                        + TUNA_CREATION_PROBABILITY + SARDINE_CREATION_PROBABILITY + JELLYFISH_CREATION_PROBABILITY) {
                    Jellyfish jellyfish = new Jellyfish(true, location);
                    field.placeAnimal(jellyfish, location);
                }
                // else leave the location empty.
            }
        }
    }

    // Updated logging method that prints a consistent header with all info.
    public void reportStats() {
        // Compute population counts
        Map<String, Integer> counts = new HashMap<>();
        for (Animal a : field.getAnimals()) {
            if (a.isAlive()) {
                String key = a.getClass().getSimpleName();
                counts.put(key, counts.getOrDefault(key, 0) + 1);
            }
        }
        // Build a population details string
        StringBuilder popDetails = new StringBuilder();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            popDetails.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(" ");
        }
        System.out.println("--------------------------------------------------");
        System.out.println("Step: " + step + " | Weather: " + weather);
        System.out.println("Population: " + popDetails.toString().trim());
        System.out.println("--------------------------------------------------");
    }

    /**
     * Pause for a given time.
     * 
     * @param milliseconds The time to pause for, in milliseconds
     */
    private void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    // New method to update weather condition
    private void updateWeather() {
        String[] conditions = { "Sunny", "Rainy", "Cloudy", "Windy", "Stormy" };
        weather = conditions[rand.nextInt(conditions.length)];
    }
}
