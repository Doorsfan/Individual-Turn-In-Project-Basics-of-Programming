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
    protected int portionSize = 0;
    protected Random random = new Random();
    protected int minimumOffspring;
    protected int maximumOffspring;
    protected int age = 0;
    protected int maxAge;
    private boolean alive = true;
    protected int perishedAtRound = 0;

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

    public int getMinimumOffspring() { return this.minimumOffspring; }

    public int getMaximumOffspring() { return this.maximumOffspring; }

    public int getPortionSize(){
        return this.portionSize;
    }

    public void age(){
        this.age += 1;
    }

    protected ArrayList<Food> whatItEats(Food firstItem){
        ArrayList<Food> toReturn = new ArrayList<>();
        toReturn.add(firstItem);
        return toReturn;
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

    public int getAge(){
        return this.age;
    }

    public int getMaxAge() { return this.maxAge; }

    public String getClassName(){ return this.getClass().getSimpleName(); }

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

    public void die(String cause){
        System.out.println("Die was called at: " + this.perishedAtRound + " from " + cause);
        this.alive = false;
    }

    public boolean isAlive(){
        return this.alive;
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

    public void setPerishedAtRound(int diedAtRound){
        this.perishedAtRound = diedAtRound;
    }

    public int eat(int gramsFedWith, Food fedWith){
        for(Food iEat : eats){
            if(iEat.getName().equals(fedWith.getName())){
                if(fedWith.getName().equals("mysteryMeat")){
                    mysteryMeat theMeat = (mysteryMeat) fedWith;
                    for(Food contents : theMeat.getContentsOfMeat()){
                        if(contents instanceof Plant || contents instanceof ratMeat){
                            return -3; //Eats Mystery meat, but it had plants or rat meat in it
                        }
                    }
                }
                fedWith.reduceFromStock(gramsFedWith);


                this.health += ((gramsFedWith/getPortionSize()) * 10);
                if(this.health <= 100){
                    System.out.println(this.getName() + "'s health increased by 10.");
                }
                else{
                    System.out.println(this.getName() + "'s health increased by " + (10 - (this.health - 100)) + ".");
                    this.health = 100;
                }
                return 1; //Went through fine, the Animal ate the food
            }
        }
        return -2; //The animal did not like the type of food being  served
    }

    public int getPerishedAtRound(){
        return this.perishedAtRound;
    }
    public int getValue(){
        return this.value;
    }

    public int getHealth(){
        return this.health;
    }
}
