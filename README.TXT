# Assignment 3

Core Species:
1. Shark (Predator)
2. Barracuda (Predator)
3. Tuna (Prey fish)
4. Sardine (Prey fish)
5. Jellyfish (Non-predator, filter feeder)

Interactions:
- Both Sharks and Barracudas compete for Tuna as their primary food source
- Sardines are eaten by Barracudas (but not Sharks, creating different predator niches)
- Jellyfish filter feed (eating microscopic organisms, which we can assume are always available like plants in the original)

Day/Night Behavior:
- Tuna and Sardines school together more tightly at night (reduced movement)
- Sharks become more active hunters at night
- Jellyfish rise to surface waters at night (vertical movement)
- Barracudas are primarily daytime hunters (less active at night)

Breeding Mechanics:
- Sharks and Tuna will require male/female matching for breeding
- They'll need to be in adjacent cells to breed
- Other species can keep the original breeding mechanism for simplicity
- We'll need to tune breeding probabilities - might start with:
 * Sharks: Lower breeding rate (as top predator)
 * Tuna: Medium breeding rate
 * Sardines: Higher breeding rate (as primary prey)

How to start:
    Create a Simulator object.
    Then call one of:
        + simulateOneStep - for a single step.
        + simulate - and supply a number (say 10) for that many steps.
        + runLongSimulation - for a simulation of 700 steps.
