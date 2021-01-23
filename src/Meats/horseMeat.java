package Meats;

import gameComponents.Food;

import java.io.Serializable;

/**
 * A simple constructor for the horseMeat class.
 * Most of these values that are delegated in the Constructor are inherited from Food.
 * The only exception to this is mysteryMeat.
 */
public class horseMeat extends Food implements Serializable {
    public horseMeat(){
        this.value = 700;
    }
}
