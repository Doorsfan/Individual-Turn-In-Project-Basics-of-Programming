package Animals;

import Meats.ratMeat;
import Plants.Plant;
import gameComponents.Food;
import gameComponents.Player;
import gameComponents.utilityFunctions;
import processedFood.mysteryMeat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public abstract class Animal extends utilityFunctions implements Serializable {
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
    protected int perishedAtRound = 0;
    protected int vetCost = 0;
    private String causeOfDeath = "";
    private boolean decayedThisRound = false;

    private boolean alive = true;
    private boolean sick = false;

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

    public boolean hasDecayedThisRound() { return this.decayedThisRound; }

    public void setDecayedThisRound(boolean decayedThisRound) { this.decayedThisRound = decayedThisRound; }

    public int getMinimumOffspring() { return this.minimumOffspring; }

    public int getMaximumOffspring() { return this.maximumOffspring; }

    public int getPortionSize(){
        return this.portionSize;
    }

    public void setCauseOfDeath(String in){
        this.causeOfDeath = in;
    }

    public String getCauseOfDeath(){
        return this.causeOfDeath;
    }

    public void age(){
        this.age += 1;
        if(this.age > this.maxAge){
            setAlive(false);
            setCauseOfDeath("Aging");
        }
    }

    protected ArrayList<Food> whatItEats(Food firstItem){
        ArrayList<Food> toReturn = new ArrayList<>();
        toReturn.add(firstItem);
        return toReturn;
    }

    public int getSellsFor(){
        double valueMultiplicator = (double) this.health / 100.0;
        double toReturn = this.value * valueMultiplicator;
        double ageFactor = 0;
        if(this.age < maxAge){
            ageFactor = 1.0 - ((double)this.age/maxAge);
        }
        else{ //In case age == maxAge, to avoid multiplication of 0
            ageFactor = 1.0 - ((double)(this.age - 1)/maxAge);
        }
        toReturn = toReturn * ageFactor;
        if(toReturn < 1.0){
            toReturn = 1;
        }
        return (int) toReturn;
    }

    public void setSick(boolean isSick){
        this.sick = isSick;
    }

    public boolean isSick(){
        return this.sick;
    }

    public int getVetCost(){
        return this.vetCost;
    }
    // Animals can become Sick - 20% per Animal on each Round -
    // Incurrs a Veterinary bill tto pay, price varying based on the Animal -
    // They have a 50% chanse of recovering - If this fails, they die. //Not started
    public int chanseForDisease(Player owner){
        String answer;
        Scanner diseaseScanner = new Scanner(System.in);

        Random random = new Random();
        int diseaseChanse = random.ints(1, 101).findFirst().getAsInt();


        if(diseaseChanse <= 20){ //20% chanse of being diseased, 20 out of 100
            this.setSick(true);
            System.out.println(this.getInfo() + " has been struck by a terrible disease!");

            if(owner.getAmountOfMoney() < this.getVetCost()){
                System.out.println(owner.getName() + " cannot afford to treat " + this.getInfo() + " (Costs: " + this.getVetCost() +
                        " coins, have: " + owner.getAmountOfMoney() + " coins left.)\n");
                setAlive(false);
                setCauseOfDeath("Disease");
                return -1; //Could not afford it
            }

            System.out.println("Do you wish to pay the Vet bill for " + this.getInfo() + "?" + " (Costs: " + this.getVetCost() + " coins.)");
            while(!(forceYOrN(answer = diseaseScanner.next()) == 1)){
                //Break when the input is Y,y,N or n
            }
            if(owner.getAmountOfMoney() >= this.getVetCost() && answer.toLowerCase().equals("y")){
                int getsCured = random.ints(1, 3).findFirst().getAsInt();
                owner.pay(this.getVetCost());
                System.out.println(owner.getName() + " paid the Vet bill for " + this.getInfo() + " for " + this.getVetCost() + " coins.\n" +
                        owner.getName() + " has " + owner.getAmountOfMoney() + " coins left after paying the Vet bill.");
                if(getsCured == 1){ //50% chanse of being cured, 1 or 2
                    System.out.println(this.getInfo() + " was cured!\n");
                    this.setSick(false);
                    return 1; //Paid for treatment and it worked
                }
                else{
                    System.out.println("The treatment for " + this.getInfo() + " did not work.\n");
                    setAlive(false);
                    setCauseOfDeath("Disease");
                    return -3; //Paid for the treatment, but did not work
                }
            }
            else{ //Can afford it, but answered no
                System.out.println(owner.getName() + " chose not to pay the Vet bill for " + this.getInfo() + " for " + this.getVetCost() + " coins.\n");
                setAlive(false);
                setCauseOfDeath("Disease");
                return -2; //Can afford it, but answered no
            }
        }
        return 0; //No disease occurred
    }

    public String getInfo(){
        return (this.getName() + " the " + this.getClassName() + " (" + this.getGender() +
                "), (Health: " + this.getHealth() + ", Age: " + this.getAge() + ") (SicK: " + isSick() + ")");
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

    public void decay(int round){
        wasAtHealth = health;
        Random random = new Random();
        int decayFactor = random.ints(10, 31).findFirst().getAsInt();
        this.health -= decayFactor;
        lostHealth = decayFactor;
        if(this.health <= 0){
            setAlive(false);
            setCauseOfDeath("Starvation");
        }
    }

    public boolean isAlive(){
        return this.alive;
    }

    public void setAlive(boolean status){
        this.alive = status;
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
