package Plants;

import java.io.Serializable;

/**
 * A simple constructor for the Apple class.
 * Most of these values that are delegated in the Constructor are inherited from Plant.
 * The only exception to this is mysteryMeat.
 */
 public class Apple extends Plant implements Serializable {
    public Apple(){
        this.value = 200;
    }
}
