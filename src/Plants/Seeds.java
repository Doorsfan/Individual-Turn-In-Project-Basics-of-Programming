package Plants;

import java.io.Serializable;

/**
 * A simple constructor for the Seeds class.
 * Most of these values that are delegated in the Constructor are inherited from Plant.
 * The only exception to this is mysteryMeat.
 */
public class Seeds extends Plant implements Serializable {
    public Seeds(){
        this.value = 100;
    }
}
