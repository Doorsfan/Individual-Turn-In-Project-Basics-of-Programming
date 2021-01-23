package processedFood;

import java.io.Serializable;

/**
 * A simple constructor for the fishFood  class.
 * Most of these values that are delegated in the Constructor are inherited from processedFood.
 * The only exception to this is mysteryMeat.
 */
public class fishFood extends processedFood implements Serializable {
    public fishFood(){
        this.value = 300;
    }
}
