package Plants;

import java.io.Serializable;

/**
 * A simple constructor for the Peanut class.
 * Most of these values that are delegated in the Constructor are inherited from Plant.
 * The only exception to this is mysteryMeat.
 */
public class Peanut extends Plant implements Serializable {
    public Peanut(){
        this.value = 200;
    }
}
