package processedFood;

import java.io.Serializable;

/**
 * A simple constructor for the dogFood class.
 * Most of these values that are delegated in the Constructor are inherited from processedFood.
 * The only exception to this is mysteryMeat.
 */
public class dogFood extends processedFood implements Serializable {
    public dogFood(){
        this.value = 375;
    }
}
