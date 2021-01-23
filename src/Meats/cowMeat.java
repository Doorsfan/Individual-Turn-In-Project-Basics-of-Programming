package Meats;

import gameComponents.Food;

import java.io.Serializable;

/**
 * A simple constructor for the cowMeat class.
 * Most of these values that are delegated in the Constructor are inherited from Food.
 * The only exception to this is mysteryMeat.
 */
public class cowMeat extends Food implements Serializable {
    public cowMeat(){
        this.value = 500;
    }
}
