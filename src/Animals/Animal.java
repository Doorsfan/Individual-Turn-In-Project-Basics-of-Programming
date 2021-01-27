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
/**
 * The main abstract class that all Animals inherit from - Implements a interface to allow for easy Serialization,
 * and inherits utilityFunctions to allow for enforcing of certain inputs/cleaning of certain inputs.
 */
public abstract class Animal extends utilityFunctions implements Serializable {
    //Initialize all of the variables that we will need
    //These attributes are all inherited and accessed in Child classes, so they are protected
    protected String name,gender;
    protected int health = 100, value, wasAtHealth = 100, lostHealth = 0, portionSize = 0, minimumOffspring, maximumOffspring;
    protected int age = 0, maxAge, perishedAtRound = 0, vetCost = 0;
    protected ArrayList<Food> eats;
    protected Random random = new Random();

    //These should not be inherited and are singularly unique to the Animal/Has interactions that should only apply to them
    private String causeOfDeath = "";
    private boolean decayedThisRound = false, alive = true, sick = false;
    private transient Scanner diseaseScanner = new Scanner(System.in);
    private Player owner;

    public void setOwner(Player owner){
        this.owner = owner;
    }

    public Player getOwner(){
        return this.owner;
    }

    /**
     * A method that aids in Construction of children of the Animal class - Accepts 3 Food Items
     *
     * @param firstItem  A food Object used in helping with constructing the Constructor for Animals
     * @param secondItem A food Object used in helping with constructing the Constructor for Animals
     * @param thirdItem  A food Object used in helping with constructing the Constructor for Animals
     * @return An ArrayList of Food object type, used for the Constructor of Animals
     */
    protected ArrayList<Food> whatItEats(Food firstItem, Food secondItem, Food thirdItem){
        //Just a simple form of Getter that will help us when Constructing Animal based Classes
        ArrayList<Food> toReturn = new ArrayList<>();
        toReturn.add(firstItem);
        toReturn.add(secondItem);
        toReturn.add(thirdItem);
        return toReturn;
    }

    /**
     * A method that aids in Construction of children of the Animal class - Accepts 2 Food Items
     *
     * @param firstItem  A food Object used in helping with constructing the Constructor for Animals
     * @param secondItem A food Object used in helping with constructing the Constructor for Animals
     * @return An ArrayList of Food object type, used for the Constructor of Animals
     */
    protected ArrayList<Food> whatItEats(Food firstItem, Food secondItem){
        //Just a simple form of Getter that will help us when Constructing Animal based Classes
        ArrayList<Food> toReturn = new ArrayList<>();
        toReturn.add(firstItem);
        toReturn.add(secondItem);
        return toReturn;
    }

    /**
     * A method that aids in Construction of children of the Animal class - Accepts 1 Food Item
     *
     * @param firstItem A food Object used in helping with constructing the Constructor for Animals
     * @return An ArrayList of Food object type, used for the Constructor of Animals
     */
    protected ArrayList<Food> whatItEats(Food firstItem){
        //Just a simple form of Getter that will help us when Constructing Animal based Classes
        ArrayList<Food> toReturn = new ArrayList<>();
        toReturn.add(firstItem);
        return toReturn;
    }

    /**
     * Getter for decayedThisRound
     *
     * @return A boolean saying wether this is true or not
     */
    public boolean hasDecayedThisRound() { return this.decayedThisRound; }

    /**
     * Setter for decayedThisRound
     *
     * @param decayedThisRound A boolean saying if the animal decayed this round
     */
    public void setDecayedThisRound(boolean decayedThisRound) { this.decayedThisRound = decayedThisRound; }

    /**
     * Gets minimum offspring.
     *
     * @return the minimum offspring
     */
    public int getMinimumOffspring() { return this.minimumOffspring; }

    /**
     * Gets maximum offspring.
     *
     * @return the maximum offspring
     */
    public int getMaximumOffspring() { return this.maximumOffspring; }

    /**
     * Get portion size int.
     *
     * @return the portionsize of the animal
     */
    public int getPortionSize(){
        return this.portionSize;
    }

    /**
     * Set cause of death.
     *
     * @param diedFrom the cause of death for the Animal
     */
    public void setCauseOfDeath(String diedFrom){
        this.causeOfDeath = diedFrom;
    }

    /**
     * Get cause of death string.
     *
     * @return the string
     */
    public String getCauseOfDeath(){
        return this.causeOfDeath;
    }

    /**
     * Ages the Animal, if it goes above it's max age, it dies and the cause of death is aging
     */
    public void age(){
        //Basic Aging method that is called every Round
        this.age += 1;
        if(this.age > this.maxAge){ //If the animal gets too old, it dies
            setAlive(false);
            setCauseOfDeath("Aging"); //Will be used for later purposes such as Death messages
        }
    }

    /**
     * Gets the value that the animal sells for currently - depends on age and health value
     *
     * @return An int that is the relative value of the Animal
     */
    public int getSellsFor(){
        //First takes the % of Health an animal is at as a Double
        double valueMultiplicator = (double) this.health / 100.0;
        double toReturn = this.value * valueMultiplicator;
        double ageFactor = 0;
        //The closer the Animal is to death, the less it's value is
        if(this.age < maxAge){
            ageFactor = 1.0 - ((double)this.age/maxAge);
        }
        else{ //In case age == maxAge, to avoid multiplication of 0
            ageFactor = 1.0 - ((double)(this.age - 1)/maxAge);
        }
        toReturn = toReturn * ageFactor;
        if(toReturn < 1.0){ //In case the value of an Animal is so low, that it would be rounded down to 0, just set it to 1
            toReturn = 1;
        }
        return (int) toReturn;
    }

    /**
     * Sets wether the animal is sick
     *
     * @param isSick A boolean that  denotes wether this Animal is sick
     */
    public void setSick(boolean isSick){
        this.sick = isSick;
    }

    /**
     * Gets wether this animal is sick or not
     *
     * @return A boolean to denote if this animal is sick
     */
    public boolean isSick(){
        return this.sick;
    }

    /**
     * Gets cost of a veterniary
     *
     * @return An int, the cost of the Vet for this Animal
     */
    public int getVetCost(){
        return this.vetCost;
    }

    /**
     * The method to simulate getting sick - 20% chance per call that the respective Animal gets sick,
     * if this happens - a player is prompted to pay a Vet bill to possibly cure the Animal - if not done,
     * the Animal will remain sick until the end of the Round and die afterwards.
     *
     * If the player pays for the Vet bill, there is a 50% chance that the treatment will work, and the
     * animal is no longer sick. If this fails, however, the Animal perishes.
     *
     * @param owner the owner
     * @return An int that acts as Status code, wether said Animal got sick or other events. (Such as not paying Vet cost, etc.)
     */
    public void chanceForDisease(Player owner){
        String answer;
        if(this.diseaseScanner == null){ this.diseaseScanner = new Scanner(System.in); }

        Random random = new Random();
        int diseaseChanse = random.ints(1, 101).findFirst().getAsInt();

        if(diseaseChanse <= 20){ //20% chance of being diseased, 20 out of 100
            this.setSick(true);
            //Print some info about who has been struck by a Disease
            System.out.println(this.getInfo() + " has been struck by a terrible disease!");
            if(owner.getAmountOfMoney() < this.getVetCost()){
                System.out.println(owner.getName() + " cannot afford to treat " + this.getInfo() + " (Costs: " + this.getVetCost() +
                        " coins, have: " + owner.getAmountOfMoney() + " coins left.)\n");
                setAlive(false);
                setCauseOfDeath("Disease");
                return; //Could not afford it
            }
            System.out.println("Do you wish to pay the Vet bill for " + this.getInfo() + "?" + " (Costs: " + this.getVetCost() + " coins.)");
            while(!(forceYOrN(answer = diseaseScanner.next()) == 1)){
                //Break when the input is Y,y,N or n
            }
            if(owner.getAmountOfMoney() >= this.getVetCost() && answer.toLowerCase().equals("y")){
                //Random number in range of 1-2, 3 is exclusive - Returns a Stream of Ints, retrieves the first element and returns
                //it as a Int
                int getsCured = random.ints(1, 3).findFirst().getAsInt();
                owner.pay(this.getVetCost());
                System.out.println(owner.getName() + " paid the Vet bill for " + this.getInfo() + " for " + this.getVetCost() + " coins.\n" +
                        owner.getName() + " has " + owner.getAmountOfMoney() + " coins left after paying the Vet bill.");
                if(getsCured == 1){ //50% chance of being cured, 1 or 2
                    System.out.println(this.getInfo() + " was cured!\n");
                    this.setSick(false);
                    return; //Paid for treatment and it worked
                }
                else{
                    System.out.println("The treatment for " + this.getInfo() + " did not work.\n");
                    setAlive(false);
                    setCauseOfDeath("Disease");
                    return; //Paid for the treatment, but did not work
                }
            }
            else{
                System.out.println(owner.getName() + " chose not to pay the Vet bill for " + this.getInfo() + " for " + this.getVetCost() + " coins.\n");
                setAlive(false);
                setCauseOfDeath("Disease");
                return; //Can afford it, but answered no
            }
        }
    }

    /**
     * A more general getter to get some info about this Animal in general, used in Menus for Shorthand writing
     *
     * @return The string with information about the animal
     */
    public String getInfo(){
        return (this.getName() + " the " + this.getClassName() + " (" + this.getGender() +
                "), (Health: " + this.getHealth() + ", Age: " + this.getAge() + ") (SicK: " + isSick() + ")");
    }

    /**
     * Get what it eats array list.
     *
     * @return the array list
     */
    public ArrayList<Food> getWhatItEats(){
        return this.eats;
    }

    /**
     * Get name string.
     *
     * @return the string
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get age int.
     *
     * @return the int
     */
    public int getAge(){
        return this.age;
    }

    /**
     * Gets max age.
     *
     * @return the max age
     */
    public int getMaxAge() { return this.maxAge; }

    /**
     * Shorthand getter for getting the SimpleName of the Class
     *
     * @return A string with the simpleName of the Class
     */
    public String getClassName(){ return this.getClass().getSimpleName(); }

    /**
     * Get gender string.
     *
     * @return the string
     */
    public String getGender(){
        return this.gender;
    }

    /**
     * Simulates the health loss of animals for each turn - Yields a random number between 10 and 30 (inclusive),
     * saves the amount of health this Animal lost - deducts from the current Health - and if the Health is below 0,
     * this animal dies - cause of death being Starvation.
     *
     * @param round the round
     */
    public void decay(int round){
        wasAtHealth = health;
        Random random = new Random();
        // Random number between 10 and 30 (inclusive)
        int decayFactor = random.ints(10, 31).findFirst().getAsInt();
        this.health -= decayFactor;
        lostHealth = decayFactor;
        if(this.health <= 0){
            setAlive(false);
            setCauseOfDeath("Starvation"); //Died of starvation
        }
    }

    /**
     * Is alive boolean.
     *
     * @return the boolean
     */
    public boolean isAlive(){
        return this.alive;
    }

    /**
     * Set alive.
     *
     * @param status the status
     */
    public void setAlive(boolean status){
        this.alive = status;
    }

    /**
     * Get was at health int.
     *
     * @return the int
     */
    public int getWasAtHealth(){
        return this.wasAtHealth;
    }

    /**
     * Set name.
     *
     * @param name the name
     */
    public void setName(String name){
        this.name= name;
    }

    /**
     * Get lost health int.
     *
     * @return the int
     */
    public int getLostHealth(){
        return this.lostHealth;
    }

    /**
     * Set perished at round.
     *
     * @param diedAtRound the died at round
     */
    public void setPerishedAtRound(int diedAtRound){
        this.perishedAtRound = diedAtRound;
    }

    /**
     * The method for an Animal to eat Food - Checks if this Animal eats what it is being fed,
     * if it is something it likes and wants - it eats it and the amount of Grams it takes is
     * reduced from the owning players Stock of food.
     *
     * Heals the animal for 10 health if at or under 90 Health, otherwise sets health to 100
     *
     * @param gramsFedWith An int, the amount of grams fed with
     * @param fedWith      A food object, the Food the Animal was fed with
     * @return An status code     An int, that acts as a Status code for events transpiring
     */
    public int eat(int gramsFedWith, Food fedWith){
        //Go through all of the food that the Animal eats
        for(Food iEat : eats){
            if(iEat.getName().equals(fedWith.getName())){ //If there is a match with what is being fed to it
                if(fedWith.getName().equals("mysteryMeat")){
                    mysteryMeat theMeat = (mysteryMeat) fedWith; //Special case for mysteryMeat to downcast
                    for(Food contents : theMeat.getContentsOfMeat()){ //Check what's inside the mysteryMeat
                        if(contents instanceof Plant || contents instanceof ratMeat){ //If there's plant or ratMeat in it
                            return -3; //Eats Mystery meat, but it had plants or rat meat in it - Won't eat it
                        }
                    }
                }
                fedWith.reduceFromStock(gramsFedWith); //Reduce the amount of fed food from owners Stock of Food
                this.health += ((gramsFedWith/getPortionSize()) * 10); //Is always 1 * 10, but could allow for different amounts of grams
                //fed with - Not supported, at the moment, though
                if(this.health <= 100){
                    System.out.println(this.getName() + "'s health increased by 10.");
                }
                else{
                    System.out.println(this.getName() + "'s health increased by " + (10 - (this.health - 100)) + ".");
                    this.health = 100;
                }
                return 1; //Went through fine, the Animal ate the food
            }
        } return -2; //The animal did not like the type of food being  served
    }

    /**
     * Get perished at round int.
     *
     * @return An int, the round the animal died at
     */
    public int getPerishedAtRound(){
        return this.perishedAtRound;
    }

    /**
     * Getter for the base value of the Animal
     *
     * @return An int, the base value of the Animal
     */
    public int getValue(){
        return this.value;
    }

    /**
     * Getter for the Health of the Animal
     *
     * @return An int, the current health of the Animal
     */
    public int getHealth(){
        return this.health;
    }
}