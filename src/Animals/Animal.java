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
    private  transient Scanner nameScanner = new Scanner(System.in);


    // ============= GETTERS =====================
    /**
     * Gets minimum offspring.
     * @return the minimum offspring
     */
    public int getMinimumOffspring() { return this.minimumOffspring; }

    /**
     * Gets maximum offspring.
     * @return the maximum offspring
     */
    public int getMaximumOffspring() { return this.maximumOffspring; }

    /**
     * Get portion size int.
     * @return the portion size of the animal
     */
    public int getPortionSize(){
        return this.portionSize;
    }

    /**
     * Get cause of death string.
     * @return the string
     */
    public String getCauseOfDeath(){
        return this.causeOfDeath;
    }

    /**
     * Gets the value that the animal sells for currently - depends on age and health value
     * @return An int that is the relative value of the Animal
     */
    public int getSellsFor(){
        //First takes the % of Health an animal is at as a Double
        double valueMultiplicator = (double) this.health / 100.0;
        double toReturn = this.value * valueMultiplicator, ageFactor;
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
     * Gets if this animal is sick or not
     * @return A boolean to denote if this animal is sick
     */
    public boolean isSick(){
        return this.sick;
    }

    /**
     * Gets cost of a Vet
     * @return An int, the cost of the Vet for this Animal
     */
    public int getVetCost(){
        return this.vetCost;
    }

    /**
     * A getter that implements colours into the Terminal printing - to be used when colours codes are NOT implemented
     * directly in the System.out.print by itself
     * @return A string that includes Colours in it
     */
    public String getColoredInfo(){
        String colorOfHealth, colorOfAge;
        if(this.getHealth() >= 50){
            //Code for Green in Consoles - \u001b[32m - Reset code for Colors in Console \u001b[0m
            colorOfHealth = "\u001b[32m";
        }
        else if(this.getHealth() >= 30 && this.getHealth() <= 49){
            //Code for Yellow in Consoles - \u001b[33m - Reset code for Colors in Console \u001b[0m
            colorOfHealth = "\u001b[33m";
        }
        else{
            //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
            colorOfHealth = "\u001b[31m";
        }
        double percentOfYearsSpent = (double)this.getAge()/(double)this.getMaxAge();
        if(percentOfYearsSpent >= 0.75){
            //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
            colorOfAge = "\u001b[31m";
        }
        else if(percentOfYearsSpent >= 0.33 && percentOfYearsSpent <= 74){
            //Code for Yellow in Consoles - \u001b[33m - Reset code for Colors in Console \u001b[0m
            colorOfAge = "\u001b[33m";
        }
        else{
            //Code for Green in Consoles - \u001b[32m - Reset code for Colors in Console \u001b[0m
            colorOfAge = "\u001b[32m"; //Green age
        }
        return (this.getName() + " the " + this.getClassName() + " (" + this.getGender() +
                "), (Health: " + colorOfHealth + this.getHealth() + "\u001b[0m, " +
                "Age: " + colorOfAge + this.getAge() + "\u001b[0m) (SicK: " + isSick() + ")");
    }

    /**
     * The normal setup of general Information getting - to be used in conjunction with setting colours manually
     * in the actual System Out
     * @return A string with general info that is not colour modified by itself
     */
    public String getVanillaInfo(){
        return (this.getName() + " the " + this.getClassName() + " (" + this.getGender() +
                "), (Health: " + this.getHealth() + ", " + "Age: " + this.getAge() + ") (SicK: " + isSick() + ")");
    }

    /**
     * Get what it eats array list.
     * @return the array list
     */
    public ArrayList<Food> getWhatItEats(){
        return this.eats;
    }

    /**
     * Get name string.
     * @return the string
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get age int.
     * @return the int
     */
    public int getAge(){
        return this.age;
    }

    /**
     * Gets max age.
     * @return the max age
     */
    public int getMaxAge() { return this.maxAge; }

    /**
     * Shorthand getter for getting the SimpleName of the Class
     * @return A string with the simpleName of the Class
     */
    public String getClassName(){ return this.getClass().getSimpleName(); }

    /**
     * Get gender string.
     * @return the string
     */
    public String getGender(){
        return this.gender;
    }

    /**
     * Is alive boolean.
     * @return the boolean
     */
    public boolean isAlive(){
        return this.alive;
    }

    /**
     * Get was at health int.
     * @return the int
     */
    public int getWasAtHealth(){
        return this.wasAtHealth;
    }

    /**
     * Get lost health int.
     * @return the int
     */
    public int getLostHealth(){
        return this.lostHealth;
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

    /**
     * Getter for decayedThisRound
     * @return A boolean saying if this is true or not
     */
    public boolean getDecayedThisRound() { return this.decayedThisRound; }

    /**
     * A method that builds a death message for a Animal
     * @return A string, it's death message
     */
    public String getDeathMessage(){
        return (this.getName() + " the " + this.getClassName() + " (" + this.getGender() + ")"
                + " perished at the game round of " + this.getPerishedAtRound() + ", died of " + this.getCauseOfDeath()
                + ", became : " + this.getAge() + " years old. Rest in peace, " + this.getName());
    }

    // ============= SETTERS =====================

    /**
     * Setter for decayedThisRound
     * @param decayedThisRound A boolean saying if the animal decayed this round
     */
    public void setDecayedThisRound(boolean decayedThisRound) { this.decayedThisRound = decayedThisRound; }

    /**
     * Set cause of death.
     * @param diedFrom the cause of death for the Animal
     */
    public void setCauseOfDeath(String diedFrom){
        this.causeOfDeath = diedFrom;
    }

    /**
     * Sets if the animal is sick
     * @param isSick A boolean that denotes if this Animal is sick
     */
    public void setSick(boolean isSick){
        this.sick = isSick;
    }

    /**
     * Set alive.
     * @param status the status
     */
    public void setAlive(boolean status){
        this.alive = status;
    }

    /**
     * Set name.
     * @param name the name
     */
    public void setName(String name){
        this.name= name;
    }

    /**
     * Set perished at round.
     *
     * @param diedAtRound the died at round
     */
    public void setPerishedAtRound(int diedAtRound){
        this.perishedAtRound = diedAtRound;
    }

    // =============== UTILITY METHODS ===================

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

    // ============ GAME LOGIC METHODS ==================

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
     * The method to simulate getting sick - 20% chance per call that the respective Animal gets sick,
     * if this happens - a player is prompted to pay a Vet bill to possibly cure the Animal (50% chance of working)
     * - if not done or the treatment fails, the Animal dies.
     * @param owner the owner
     */
    public void chanceForDisease(Player owner){
        String answer;
        if(this.diseaseScanner == null){ this.diseaseScanner = new Scanner(System.in); }
        Random random = new Random();
        if(random.ints(1, 6).findFirst().getAsInt() == 1){ //20% chance of being diseased, 1 out of 5
            this.setSick(true);
            //Print some info about who has been struck by a Disease
            System.out.println("\u001b[31m" + this.getVanillaInfo() + " has been struck by a terrible disease!\u001b[0m");
            if(owner.getAmountOfMoney() < this.getVetCost()){
                System.out.println("\u001b[31m" + owner.getName() +
                        " cannot afford to treat " + this.getVanillaInfo() + " (Costs: " + this.getVetCost() +
                        " coins, have: " + owner.getAmountOfMoney() + " coins left.)\n\u001b[0m");
                setAlive(false);
                setCauseOfDeath("Disease");
                return; //Could not afford it
            }
            System.out.println("\u001b[33mDoes " + owner.getName() + " wish to pay the Vet bill for " + this.getVanillaInfo() + "?"
                    + " (Costs: " + this.getVetCost() + " coins.)\u001b[0m");
            //Break when the input is Y,y,N or n
            while(!(forceYOrN(answer = diseaseScanner.next()) == 1)){ }
            if(owner.getAmountOfMoney() >= this.getVetCost() && answer.toLowerCase().equals("y")){
                owner.pay(this.getVetCost());
                System.out.println("\u001b[32m" + owner.getName() + " paid the Vet bill for " +
                        this.getVanillaInfo() + " for " + this.getVetCost() + " coins.\u001b[0m");
                System.out.println(owner.getName() + " has " + owner.getAmountOfMoney() + " coins left after paying the Vet bill.");
                if(random.ints(1, 3).findFirst().getAsInt() == 1){ //50% chance of being cured, 1 or 2
                    System.out.println("\u001b[32m" + this.getVanillaInfo() + " was cured!\n\u001b[0m");
                    this.setSick(false); //Paid for treatment and it worked
                }
                else{
                    System.out.println("\u001b[31mThe treatment for " + this.getVanillaInfo() + " did not work.\n\u001b[0m");
                    setAlive(false); //Paid for the treatment and it did not work
                    setCauseOfDeath("Disease");
                }
                return;
            }
            else{
                System.out.println("\u001b[31m" + owner.getName() + " chose not to pay the Vet bill for " + this.getVanillaInfo()
                        + " for " + this.getVetCost() + " coins.\n\u001b[0m");
                setAlive(false);
                setCauseOfDeath("Disease");
                return; //Can afford it, but answered no
            }
        }
    }

    /**
     * Simulates the health loss of animals for each turn - Yields a random number between 10 and 30 (inclusive),
     * saves the amount of health this Animal lost - deducts from the current Health - and if the Health is below 0,
     * this animal dies - cause of death being Starvation.
     */
    public void decay(){
        wasAtHealth = health;
        Random random = new Random();
        //Returns a Int stream, gets the first occurrence, returns it as a int - between 10 and 30 (inclusive)
        int decayFactor = random.ints(10, 31).findFirst().getAsInt();
        this.health -= decayFactor; //Decays
        lostHealth = decayFactor; //The amount of Health the animal lost
        if(this.health <= 0){ //It died because of Starvation
            setAlive(false); //It's dead
            setCauseOfDeath("Starvation"); //Died of starvation
        }
    }

    /**
     * The method responsible for handling the Animal attempting/eating the Food. Heals the Animal for 10% of the
     * Current health if it likes the food - Displays gained Health. (Can't go over 100 Health)
     * @param gramsFedWith An int, the amount of Grams the Animal is being fed with - is reduced from Player stock
     * @param fedWith A food object, the food that the Animal is being fed with
     * @return A int, wether it likes the food or not - used to differentiate between if it ate the food or attempted
     *          to eat mysteryMeat (which can contain Plants or Rat, in which case the Animal will not eat it.)
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
                System.out.println(this.getColoredInfo() + " starts eating the food..");
                int beforeHealing = this.health; //The Health before it healed
                this.health *= 1.10; //Increase health by 10% of the Current health
                int afterHealing = this.health; //The health after healing
                int gained = afterHealing - beforeHealing; //Example: started at 99, went up to 108, 108 - 99 = gained is 9
                if(this.health > 100){ //Health is capped at 100, so if it's above 100, set it to 100
                    gained -= (this.health - 100); //Example: 108 - 100 = 8, gained is 9, 9 - 8 = 1, actual gain was 1
                    this.health = 100;
                }
                //Code for Green in Consoles - \u001b[32m - Reset code for Colors in Console \u001b[0m
                System.out.println("\u001b[32m" + this.getVanillaInfo() + " gained " + gained + " health!\u001b[0m");
                return 1; //Went through fine, the Animal ate the food
            }
        }
        return -2; //The animal did not like the type of food being  served
    }

    /**
     * A method that is responsible for creating babies - 50% chance of a male or female per baby, and each
     * type of animal has a different amount of Babies they produce
     *
     * @param amountOfBabies An int, the amount of children to create
     * @param females An Arraylist of Animals, all of them being Female
     * @param playerBreeding A player Object, the player breeding the animals
     * @param secondAnimalIndex An int, the index for the chosen Female
     */
    public void createBabies(int amountOfBabies, ArrayList<Animal> females, Player playerBreeding, int secondAnimalIndex){
        if(nameScanner == null){ nameScanner = new Scanner(System.in); }
        Random random = new Random();
        for(int i = 0; i < amountOfBabies; i++){ //Create amountOfBabies babies
            int genderChance = random.ints(1,3).findFirst().getAsInt(); //50% chance of being female or male
            String gender; //The gender
            gender = (genderChance == 1 ? "Male" : "Female"); //If it's 1, it's a Male, otherwise, it's a Female
            System.out.println("It's a " + gender); //Announce what it is
            //Code for Yellow in Consoles - \u001b[33m - Reset code for Colors in Console \u001b[0m
            System.out.println("\u001b[33mWhat would you like to name your new baby "
                    + females.get(secondAnimalIndex-1).getClassName() + " " +
                    "(" + gender + ")?\u001b[0m"); //Print the Class name (same as its parents) and the gender

            String name = nameScanner.nextLine(); //ask for a name
            //Based on what the parent is, the baby is the same in terms of Species
            switch (females.get(secondAnimalIndex - 1).getClassName()) {
                case "Bird" -> playerBreeding.addToOwnedAnimals(new Bird(name, gender));
                case "Cat" -> playerBreeding.addToOwnedAnimals(new Cat(name, gender));
                case "Dog" -> playerBreeding.addToOwnedAnimals(new Dog(name, gender));
                case "Elephant" -> playerBreeding.addToOwnedAnimals(new Elephant(name, gender));
                case "Fish" -> playerBreeding.addToOwnedAnimals(new Fish(name, gender));
            }
        }
    }
}