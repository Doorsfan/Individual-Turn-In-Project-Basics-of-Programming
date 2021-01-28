package processedFood;

import gameComponents.Food;

import java.io.Serializable;

/**
 * A general holding parent class for Food items that is Abstract, which acts as a intermediare
 * categorical class for the children classes who inherit from it
 */
public abstract class processedFood extends Food implements Serializable {
}
