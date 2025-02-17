/**
 * Common elements of foxes and rabbits.
 *
 * @author Sakibul Islam
 * @version 1.0
 */
public abstract class Animal {
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position.
    private Location location;

    /**
     * Constructor for objects of class Animal.
     * 
     * @param location The animal's location.
     */
    public Animal(Location location) {
        this.alive = true;
        this.location = location;
    }

    /**
     * Act.
     * 
     * @param currentField   The current state of the field.
     * @param nextFieldState The new state being built.
     * @param isDay          Whether it is day or night.
     */
    abstract public void act(Field currentField, Field nextFieldState, boolean isDay);

    /**
     * Check whether the animal is alive or not.
     * 
     * @return true if the animal is still alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     */
    protected void setDead() {
        alive = false;
        location = null;
    }

    /**
     * Return the animal's location.
     * 
     * @return The animal's location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set the animal's location.
     * 
     * @param location The new location.
     */
    protected void setLocation(Location location) {
        this.location = location;
    }
}
