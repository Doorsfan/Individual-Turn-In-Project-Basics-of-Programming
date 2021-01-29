package Meats;

import gameComponents.Food;

import java.io.Serializable;

/**
 * An empty Abstract class, to which only role is to act as a Check for mysteryMeat in terms of being
 * a dual-typed kind of Food - i.e, only exists to check if something is a instanceof Meat
 */
public abstract class Meat extends Food implements Serializable {
}
