package Animals;

import Meats.ratMeat;
import Plants.Plant;
import gameComponents.Food;
import processedFood.mysteryMeat;

import java.util.ArrayList;
import java.util.Random;

public abstract class Animal {
    protected String name, gender;
    protected int health = 100;
    protected ArrayList<Food> eats;
    protected int value;
    protected int wasAtHealth = 100;
    protected int lostHealth = 0;
    protected int sellsFor = 0;

    protected ArrayList<Food> whatItEats(Food firstItem, Food secondItem, Food thirdItem){
        ArrayList<Food> toReturn = new ArrayList<>();
        toReturn.add(firstItem);
        toReturn.add(secondItem);
        toReturn.add(thirdItem);
        return toReturn;
    }

    protected ArrayList<Food> whatItEats(Food firstItem, Food secondItem){
        ArrayList<Food> toReturn = new ArrayList<>();
        toReturn.add(firstItem);
        toReturn.add(secondItem);
        return toReturn;
    }

    protected ArrayList<Food> whatItEats(Food firstItem){
        ArrayList<Food> toReturn = new ArrayList<>();
        toReturn.add(firstItem);
        return toReturn;
    }
    public boolean checkIfAlive(){
        if(this.health <= 0) {
            return false;
        }
        return true;
    }

    public int getSellsFor(){
        double valueMultiplicator = (double) this.health / 100.0;
        double toReturn = this.value * valueMultiplicator;
        return (int) toReturn;
    }

    public ArrayList<Food> getWhatItEats(){
        return this.eats;
    }

    public String getName(){
        return this.name;
    }

    public String getGender(){
        return this.gender;
    }

    public void decay(){
        wasAtHealth = health;
        Random random = new Random();
        int decayFactor = random.ints(10, 31).findFirst().getAsInt();
        this.health -= decayFactor;
        lostHealth = decayFactor;
    }

    public int getWasAtHealth(){
        return this.wasAtHealth;
    }

    public void setName(String name){
        this.name= name;
    }
    public int getLostHealth(){
        return this.lostHealth;
    }

    public int eat(int kilosOfFood, Food fedWith){
        if(eats.contains(fedWith)){
            if(fedWith.getClass().getSimpleName().equals("mysteryMeat")){
                mysteryMeat theMeat = (mysteryMeat) fedWith;
                for(Food contents : theMeat.getContentsOfMeat()){
                    if(contents instanceof Plant || contents instanceof ratMeat){
                        return -2; //Did not like the mysteryMeat, because it smelled funny or is not a fan of plants
                    }
                }
            }
            this.health += (kilosOfFood * 10);
            if(this.health > 100){
                this.health = 100;
            }
            return -1; //Went through fine, the Animal ate the food
        }

        return -2; //Went through fine, the Animal ate the food
    }

    public int getValue(){
        return this.value;
    }

    public int getHealth(){
        return this.health;
    }
}
