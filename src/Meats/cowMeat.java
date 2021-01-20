package Meats;

import gameComponents.Food;

/**
 * A simple constructor for the cowMeat class.
 * Most of these values that are delegated in the Constructor are inherited from Food.
 * The only exception to this is mysteryMeat.
 */
public class cowMeat extends Food {
    public cowMeat(){
        this.value = 500;
    }
}
