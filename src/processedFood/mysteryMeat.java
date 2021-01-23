package processedFood;

import Meats.cowMeat;
import Meats.horseMeat;
import Meats.ratMeat;
import Plants.Apple;
import Plants.Peanut;
import Plants.Seeds;
import gameComponents.Food;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * The mysteryMeat class is a form of processedFood that employs randomness in it's creation to add a bit of
 * fun and variety to the game - as well as justifying the concept of having both Plant and Meat as Abstract
 * categorical classes.
 *
 * There is a 100% chanse at Store creation, that the Mystery meat will contain either Meat, Plants or Both.
 * This meat can be horseMeat, cowMeat or ratMeat.
 * The plants range from Peanuts, Seeds to Apples.
 *
 * Each category has a 33% chance of being represented as - 33% only meat, 33% Plant and Meat, 33% only Plants
 * And for each categorical role - i.e, only meat, Plant and Meat or only plant -
 * there is a 33% chance of each respective categorical group there within to be represented.
 * I.e - 33% chance of ratMeat or horseMeat or cowMeat if only Meat
 *       33% chance of peanut, Seeds or Apple if only plants
 *       33% chance each for one of the respective meats and 33% chance each for one of the respective plants
 *
 */
public class mysteryMeat extends processedFood implements Serializable {
    private ArrayList<Food> contains = new ArrayList<>();
    Random random = new Random();
    private int contentInMeat = random.ints(1, 4).findFirst().getAsInt();
    private int meatRandomizer = random.ints(1, 4).findFirst().getAsInt();
    private int plantRandomizer = random.ints(1, 4).findFirst().getAsInt();
    public mysteryMeat(){
        this.value = 100;
        if(contentInMeat == 1){ //Only contains meat
            if(meatRandomizer == 1){
                contains.add(new ratMeat());
            }
            if(meatRandomizer == 2){
                contains.add(new horseMeat());
            }
            if(meatRandomizer == 3){
                contains.add(new cowMeat());
            }
        }
        if(contentInMeat == 2){ //Contains both Plant and Meat
            if(plantRandomizer == 1){
                contains.add(new Peanut());
            }
            if(plantRandomizer == 2){
                contains.add(new Seeds());
            }
            if(plantRandomizer == 3){
                contains.add(new Apple());
            }
            if(meatRandomizer == 1){
                contains.add(new ratMeat());
            }
            if(meatRandomizer == 2){
                contains.add(new horseMeat());
            }
            if(meatRandomizer == 3){
                contains.add(new cowMeat());
            }
        }
        if(contentInMeat == 3){ //Only contains plants
            if(plantRandomizer == 1){
                contains.add(new Peanut());
            }
            if(plantRandomizer == 2){
                contains.add(new Seeds());
            }
            if(plantRandomizer == 3){
                contains.add(new Apple());
            }
        }

    }

    /**
     * Simple getter to get what this mysteryMeat contains
     * Used in checking if a Dog will like this mysteryMeat or not.
     * @return An ArrayList of object type Food, yielding all what this meat contains.
     */
    public ArrayList<Food> getContentsOfMeat(){
        return this.contains;
    }
}
