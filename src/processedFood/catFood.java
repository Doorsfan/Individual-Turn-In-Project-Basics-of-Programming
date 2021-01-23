package processedFood;

import java.io.Serializable;

/**
 * A simple constructor for the catFood class.
 * Most of these values that are delegated in the Constructor are inherited from processedFood.
 * The only exception to this is mysteryMeat.
 */
public class catFood extends processedFood implements Serializable {
    public catFood(){
        this.value = 400;
    }
}
