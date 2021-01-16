package processedFood;

import Meats.cowMeat;
import Meats.horseMeat;
import Meats.ratMeat;
import Plants.Apple;
import Plants.Peanut;
import Plants.Seeds;
import gameComponents.Food;

import java.util.ArrayList;
import java.util.Random;

public class mysteryMeat extends processedFood{
    private ArrayList<Food> contains = new ArrayList<>();
    Random random = new Random();
    private int contentInMeat = random.ints(1, 101).findFirst().getAsInt();
    private int meatRandomizer = random.ints(1, 101).findFirst().getAsInt();
    private int plantRandomizer = random.ints(1, 101).findFirst().getAsInt();
    public mysteryMeat(){
        this.value = 100;
        if(contentInMeat <= 35){ //Only contains meat
            if(meatRandomizer <= 33){
                contains.add(new ratMeat());
            }
            if(meatRandomizer > 33 && meatRandomizer <= 66){
                contains.add(new horseMeat());
            }
            if(meatRandomizer > 66){
                contains.add(new cowMeat());
            }
        }
        if(contentInMeat > 35 && contentInMeat <= 67){ //Contains both Plant and Meat
            if(plantRandomizer <= 33){
                contains.add(new Peanut());
            }
            if(plantRandomizer > 33 && plantRandomizer <= 67){
                contains.add(new Seeds());
            }
            if(plantRandomizer > 67){
                contains.add(new Apple());
            }
            if(meatRandomizer <= 33){
                contains.add(new ratMeat());
            }
            if(meatRandomizer > 33 && meatRandomizer <= 66){
                contains.add(new horseMeat());
            }
            if(meatRandomizer > 66){
                contains.add(new cowMeat());
            }
        }
        if(contentInMeat > 67){ //Only contains plants
            if(plantRandomizer <= 33){
                contains.add(new Peanut());
            }
            if(plantRandomizer > 33 && plantRandomizer <= 67){
                contains.add(new Seeds());
            }
            if(plantRandomizer > 67){
                contains.add(new Apple());
            }
        }

    }

    public ArrayList<Food> getContentsOfMeat(){
        return this.contains;
    }
}
