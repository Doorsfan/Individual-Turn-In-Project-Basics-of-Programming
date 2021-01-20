package Meats;

import gameComponents.Food;

/**
 * A simple constructor for the horseMeat class.
 * Most of these values that are delegated in the Constructor are inherited from Food.
 * The only exception to this is mysteryMeat.
 */
public class horseMeat extends Food {
    public horseMeat(){
        this.value = 700;
    }
}
