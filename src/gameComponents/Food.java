package gameComponents;

import java.io.Serializable;

/**
 * An abstract class that acts as overarching Parent class for Food, implements Serializable for File serialization
 * (Saving and Loading of game state)
 */
public abstract class Food implements Serializable {
    protected int value, grams = 1000;

    /**
     * Get value int.
     *
     * @return the int
     */
    public int getValue(){
        return this.value;
    }

    /**
     * Get grams int.
     *
     * @return the int
     */
    public int getGrams(){
        return this.grams;
    }

    /**
     * Set grams.
     *
     * @param amount the amount
     */
    public void setGrams(int amount){
        this.grams = amount;
    }

    /**
     * Gets the name of the Class of the Food
     *
     * @return the name
     */
    public String getName() { return this.getClass().getSimpleName(); }

    /**
     * Reduce from stock by the amount specified
     *
     * @param amount the amount
     */
    public void reduceFromStock(int amount){
        if(this.grams >= amount)
        {
            this.grams -= amount;
        }

    }
}
