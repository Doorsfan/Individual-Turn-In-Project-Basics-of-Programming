package Meats;

import gameComponents.Food;

import java.io.Serializable;

/**
 * A simple constructor for the ratMeat class.
 * Most of these values that are delegated in the Constructor are inherited from Food.
 * The only exception to this is mysteryMeat.
 *
 * This class is the only  Food item that cannot be purchased directly in the Store - and instead can be a
 * component of mysteryMeat, the chance of it being contained within, being random.
 */

public class ratMeat extends Food implements Serializable {
    public ratMeat(){
        this.value = 100;
    }
}
