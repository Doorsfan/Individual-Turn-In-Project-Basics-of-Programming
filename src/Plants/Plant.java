package Plants;

import gameComponents.Food;

import java.io.Serializable;

/**
 * An abstract empty Class that only exists to enable a check in mysteryMeat, to as of wether an Animal
 * will like eating mysteryMeat or not - as mysteryMeat can be a radnom composition of different materials.
 * (Be that only meat, only plants or both.)
 */
public abstract class Plant extends Food implements Serializable {
}
