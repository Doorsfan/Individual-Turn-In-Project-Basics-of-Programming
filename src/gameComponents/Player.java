package gameComponents;

import Animals.Animal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The class that is responsible for handling each Player and some of their interactions
 * Handles Death announcements, old death logs (used in Loading and Saving),
 */
public class Player extends utilityFunctions implements Serializable {
    private String name;
    private int amountOfMoney;
    private boolean playing = true;
    private ArrayList<Animal> ownedAnimals = new ArrayList<>(), shouldBeRemoved = new ArrayList<>();
    private ArrayList<Food> ownedFood = new ArrayList<>();
    private boolean turnIsOver = false, addedToHighScore = false;
    private ArrayList<Integer> diedAtRoundList = new ArrayList<>();
    private ArrayList<String> deathMessageList = new ArrayList<>(), savedDeathList = new ArrayList<>();
    private transient Scanner foodInput = new Scanner(System.in);

    /**
     * Instantiates a new Player.
     *
     * @param name          the name
     * @param amountOfMoney the amount of money
     */
    public Player(String name, int amountOfMoney){
        this.name = name;
        this.amountOfMoney = amountOfMoney;
    }

    /**
     * A method that builds a list of Healthy animals - Sick animals die at the end of the Turn,
     * and cannot be sold or bought.
     * @return An ArrayList of Animals of whom all are Healthy (if any)
     */
    public ArrayList<Animal> getHealthyAnimals(){
        ArrayList<Animal> healthyAnimals = new ArrayList<>();
        for(Animal toCheck : this.getOwnedAnimals()){
            if(!toCheck.isSick() && toCheck.getHealth() > 0){ //Can't sell dead animals or Sick Animals
                healthyAnimals.add(toCheck);
            }
        }
        return healthyAnimals;
    }

    /**
     * Get added to high score boolean.
     * @return the boolean
     */
    public boolean getAddedToHighScore(){
        return this.addedToHighScore;
    }

    /**
     * Set added to high score.
     * @param wasAdded the was added
     */
    public void setAddedToHighScore(boolean wasAdded){
        this.addedToHighScore = wasAdded;
    }

    /**
     * Getter that aids in interacting with what Animal should be removed from owned Animals in Game from Players
     * @return the array list
     */
    public ArrayList<Animal> getShouldBeRemoved(){
        return this.shouldBeRemoved;
    }

    /**
     * Adds a death announcement with time of death and the Message to present, for that Animal who died
     *
     * @param timeOfDeath An int, the round of death
     * @param message     A string, the message about the Animal who died
     */
    public void addDeathAnnouncement(int timeOfDeath, String message){
        diedAtRoundList.add(timeOfDeath);
        deathMessageList.add(message);
    }

    /**
     * Getter that helps with interacting with death logs (used in Saving and Loading)
     *
     * @return An arraylist of Strings, contains old death announcements that should be re-created upon loading
     * Assuming that any exist for the relevant turn
     */
    public ArrayList<String> getSavedDeathList(){
        return this.savedDeathList;
    }

    /**
     * Adds to the Players saved death list, which helps in keeping track of old dead animal logs that are
     * needed when loading an earlier turn - when we wish to present a old removed Death message
     *
     * @param toAdd A string, A old death announcement that should be kept for Loading/Saving purposes
     */
    public void addToPlayerDeathList(String toAdd){
        this.savedDeathList.add(toAdd);
    }

    /**
     * To not cause overflowing rehashing of old deaths that are not relevant, Animals who died
     * 2 turns ago are purged from the savedDeathlist (i.e, if a Animal dies on round 1, it's Death
     * message should only be re-created if a gameSave is loaded on Round 2, where it's death is
     * announced - Older than that should not be re-created)
     *
     * @param round An int, The round to identify by and remove in terms of death archives
     */
    public void purgeSavedDeathList(int round){
        for(int i = this.savedDeathList.size()-1; i > -1; i--){
            if(this.savedDeathList.get(i).contains("round of " + round)){
                this.savedDeathList.remove(i);
            }
        }
    }

    /**
     * Announce deaths that have occurred - Every time an animal dies, it's death is archived and presented
     * on the relevant turn - to then be removed. This acts as the main acting function for handling death
     * announcements in the Current game - which is separate from the Death archive logs used for Loading/Saving.
     *
     * @param currentRound An int, the current Round, checks against deaths of Animals time of death - Presents messages,
     *                     then clears both list of deathMessages and Time of death lists
     */
    public void announceDeaths(int currentRound){
        ArrayList<Integer> removeRound = new ArrayList<>();
        ArrayList<String> removeMessage = new ArrayList<>();
        if(diedAtRoundList.size() > 0){
            for(int i = diedAtRoundList.size()-1; i > -1; i--){
                if(diedAtRoundList.get(i) == currentRound-1){ //Check deaths for corresponding to last round
                    //HERE
                    System.out.println("\u001b[31m" + deathMessageList.get(i) + "\u001b[0m"); //Print it out
                    removeRound.add(diedAtRoundList.get(i));
                    removeMessage.add(deathMessageList.get(i));
                }
            }
            for(String deathMessage: removeMessage){
                deathMessageList.remove(deathMessage); //Clean out the death logs
            }
            for(Integer roundOfDeath: removeRound){
                diedAtRoundList.remove(roundOfDeath); //Clean out the round of death logs
            }
        }
    }

    /**
     * Method that is responsible for building the list of Hungry Animals (their health is less than 100)
     * These animals may not be sick either
     * @return An ArrayList of the type Animal, all of whom are Animals that are at less than 100 Health
     */
    public ArrayList<Animal> getHungryAnimals(){
        ArrayList<Animal> hungryAnimals = new ArrayList<>();
        for(Animal myAnimals : this.getOwnedAnimals()){
            if(myAnimals.getHealth() < 100 && myAnimals.getHealth() > 0 && !myAnimals.isSick()){
                hungryAnimals.add(myAnimals);
            }
        }
        return hungryAnimals;
    }

    /**
     * A method that handles printing up the Animals and their Info in the Feeding menu
     * Only prints Animals that are actually hungry - i.e, not at 100 Health
     * @param playerFeeding A player Object, the Player who is feeding
     */
    public void printAnimalsInFeedMenu(Player playerFeeding){
        int counter = 1;
        System.out.println("You have: ");
        for(Food ownedFood : playerFeeding.getOwnedFood()){
            System.out.println(ownedFood.getGrams() + " grams of " + ownedFood.getName());
        }
        System.out.println("\u001b[33mWhich animal do you wish to feed?\u001b[0m");
        for(Animal ownedAnimal: playerFeeding.getHungryAnimals()){
            System.out.println("[" + counter + "] " + ownedAnimal.getName() +
                    " the " + ownedAnimal.getClass().getSimpleName() + " (" + ownedAnimal.getGender()
                    + ") Health: " +
                    ownedAnimal.getHealth());
            System.out.print("\tIt eats: [ ");
            for(Food foodEaten: ownedAnimal.getWhatItEats()){
                System.out.print(foodEaten.getClass().getSimpleName() + " ");
            }
            System.out.print("]\n");
            System.out.print("\tPortion size: " + ownedAnimal.getPortionSize() + " grams\n");
            counter += 1;
        }
        System.out.println("[" + counter + "] Back to Main Menu");
    }

    /**
     * Checks if the Animal being fed eats anything that the player has
     * @param toBeFed An Animal object, the Animal being fed
     * @param playerFeeding A player object, the player feeding the Animal
     * @return A boolean - If the Animal being fed likes anything that the player has in Stock
     */
    public  boolean checkIfItEatsFood(Animal toBeFed, Player playerFeeding){
        boolean foundFood = false;
        for(Food whatTheAnimalEats: toBeFed.getWhatItEats()){
            for(Food whatThePlayerHas: playerFeeding.getOwnedFood()){
                if(whatThePlayerHas.getName().equals(whatTheAnimalEats.getName())){
                    foundFood = true;
                }
            }
        }
        return foundFood;
    }

    /**
     * A method that handles printing menus in terms of Feeding an Animal with Food - after having selected the Animal,
     * and returns a filtered list based on what the Animal likes and what the player has in terms of Food
     *
     * @param toBeFed An animal object, the Animal to be fed
     * @param playerFeeding A player object, the Player who is feeding an Animal
     * @return An Arraylist of Food objects, which is the filtered List of food (what the Animal likes to eat)
     */
    public  ArrayList<Food> printFoodOptions(Animal toBeFed, Player playerFeeding){
        int counter = 1;
        System.out.println("\u001b[33mWith what do you wish to feed " + toBeFed.getName() + " the " + toBeFed.getClass().getSimpleName() +
                "(" + toBeFed.getGender() + ", " + " Health: " + toBeFed.getHealth() + ")?\u001b[0m");
        ArrayList<Food> acceptedFood = toBeFed.getWhatItEats();
        ArrayList<Food> filteredFood = new ArrayList<>();
        for(Food ownedItems: playerFeeding.getOwnedFood()){
            for(Food acceptedFoodItem: acceptedFood){
                if(ownedItems.getName().equals(acceptedFoodItem.getName())){
                    filteredFood.add(ownedItems);
                }
            }
        }
        for(Food pieceOfFood : filteredFood){
            System.out.println("[" + counter + "] " + pieceOfFood.getClass().getSimpleName() +
                    " ( " + pieceOfFood.getGrams() + " grams left in stock )");
            counter += 1;
        }
        System.out.println("[" + (counter) + "] Back to Main");
        return filteredFood;
    }

    /**
     * Presents all of the Hungry Animals of the player - if none are found or the player does not have any food,
     * they are thrown back to the game menu and their turn is consumed.
     *
     * If a player attempts to feed an Animal that does not like any of the food in stock, the player is thrown back
     * to the Game Menu
     * @return An int, a status code:
     *      -2: Has no Animals to Feed
     *          Has no hungry animals to feed
     *          Has no food left to feed with
     *      -1: Player doesn't own any food that the specified Animal would like
     */
    public int feedAnimal(){
        if(foodInput == null){ this.foodInput = new Scanner(System.in); }
        int returnCode; //Shop index counter and the ReturnCode
        boolean finishedFeeding = false;

        String wantedAnimalToFeed, wantedFood;

        if(this.getOwnedAnimals().size() == 0){ //Has no Animals to feed, thrown back to main menu
            System.out.println("\u001b[31m" + this.getName() + " has no Animals to feed. Returning to Game menu.\u001b[0m");
            return -2; }

        while(!finishedFeeding){
            boolean hasFood = true;
            if(this.getHungryAnimals().size() == 0){
                System.out.println("\u001b[31m" + this.getName() + " has no more hungry animals to feed. " +
                        "Returning to Game menu.\u001b[0m");
                return -2; }
            if(this.getOwnedFood().size() == 0){ //Ran out of Food to feed with
                System.out.println("\u001b[31m" + this.getName() + " has no food left to feed with. Returning to Game menu.\n\u001b[0m");
                return -2; }
            printAnimalsInFeedMenu(this); //Print the Animals feeding menu
            while(!((returnCode = (safeIntInput(1, this.getHungryAnimals().size()+1,
                    wantedAnimalToFeed = foodInput.next(), true))) == 1)){ if(returnCode == 2) return 1; }
            Animal toBeFed = this.getHungryAnimals().get((Integer.parseInt(wantedAnimalToFeed)-1)); //The animal being fed

            if(!checkIfItEatsFood(toBeFed, this)){ //If the player does not own any food the Animal would want, the animal wont eat it
                System.out.println("\u001b[31m" + this.getName() + " does not have any food that a "
                        + toBeFed.getClassName() + " would like.\u001b[0m");
                hasFood = false;
            }
            if(hasFood){
                ArrayList<Food> filteredFood = printFoodOptions(toBeFed, this); //Relevant food items for the Specified Animal
                //Handles a filtered list based on what the Animal wants - returnCode is 2 if the index is the Exit index
                while(!((returnCode = (safeIntInput(1, filteredFood.size()+1, wantedFood = foodInput.next(),
                        true))) == 1)){ if(returnCode == 2) return 1; } //Which Animal to feed
                Food foodToFeedWith = filteredFood.get(Integer.parseInt(wantedFood)-1); //The chosen food from filtered food items
                checkingFoodFound(toBeFed, foodToFeedWith); //Checks amount of food left and attempts to feed the Animal - doesn't feed if not enough left
                clearOutFood(this); //Clears out food if there is 0 grams left of it in the Inventory
            }
        }
        return 1; //Successfully fed Animals
    }

    /**
     * The method that is responsible for handling that there is enough food left to feed an Animal and
     * calls the Animals eat method - if there actually is enough of it left in Stock to feed the Animal
     *
     * Returns true if the Animal was fed with the food - False otherwise
     *
     * @param toBeFed An Animal object, the Animal being fed
     * @param foodToFeedWith A Food object, the Food the Animal is being fed with
     * @return A boolean, if the Animal was fed in terms of the Food was found in sufficient amount or not
     */
    public  boolean checkingFoodFound(Animal toBeFed, Food foodToFeedWith){
        boolean foundFood = false;
        for(Food foodItEats : toBeFed.getWhatItEats()){
            if(foodItEats.getName().equals(foodToFeedWith.getName())){
                if(toBeFed.getPortionSize() > foodToFeedWith.getGrams()){
                    System.out.println("\u001b[31mThere is not enough grams of " + foodToFeedWith.getName() + " left to feed " +
                            toBeFed.getName() + " the " + toBeFed.getClassName() + "(" + toBeFed.getGender() + ") Health: " +
                            toBeFed.getHealth() + " - (" + foodToFeedWith.getGrams() +
                            " grams left, needs " + toBeFed.getPortionSize() + " grams per meal)\u001b[0m");
                }
                else{
                    foundFood = true;
                    int resultCode = toBeFed.eat(toBeFed.getPortionSize(),foodToFeedWith);
                    if(resultCode == 1){ //The animal liked the food
                        System.out.println("\u001b[32m" +
                                toBeFed.getName() + " the " + toBeFed.getClass().getSimpleName() + "(" + toBeFed.getGender() +")" +
                                " happily eats the " + foodToFeedWith.getName() + "!\u001b[0m");
                    }
                    else if(resultCode == -3){ //Mystery meat that did not seem appealing..
                        //HERE
                        System.out.println("\u001b[31m" +
                                toBeFed.getName() + " the " + toBeFed.getClass().getSimpleName() + "(" + toBeFed.getGender() + ")" +
                                " seems to think there's something funny with the " + foodToFeedWith.getClass().getSimpleName() +".." +
                                "\u001b[0m");
                    }
                    return foundFood;
                }
            }
        }
        return foundFood;
    }

    /**
     * The method responsible clearing our food - if the Food has run out of Stock, i.e it's 0 Grams left,
     * this method cleans that food out from the Stock
     * @param playerFeeding A player object, the respective Players inventory to clean out
     */
    public  void clearOutFood(Player playerFeeding){ //On each turn, we check if a food has run out of Stock, thus needing to be removed
        int indexToRemove = 0;
        boolean shouldRemoveFood = false;
        for(Food playersFood : playerFeeding.getOwnedFood()){
            if(playersFood.getGrams() == 0){
                shouldRemoveFood = true; //With a running index, we keep track of if a Food item should be removed
                break;
            }
            indexToRemove += 1;
        }
        if(shouldRemoveFood){
            System.out.println("\u001b[31m" + playerFeeding.getName() + " has run out of " +
                    playerFeeding.getOwnedFood().get(indexToRemove).getName() + "!\u001b[0m");
            playerFeeding.getOwnedFood().remove(indexToRemove); //Remove the food that had run out of Stock
        }
    }
    
    /**
     * Get amount of money int.
     * @return the int
     */
    public int getAmountOfMoney(){
        return this.amountOfMoney;
    }

    /**
     * Pay.
     * @param amount the amount
     */
    public void pay(int amount){ this.amountOfMoney -= amount; }

    /**
     * Get paid.
     * @param amount the amount
     */
    public void getPaid(int amount){ this.amountOfMoney += amount; }

    /**
     * Get owned animals array list.
     * @return the array list
     */
    public ArrayList<Animal> getOwnedAnimals(){
        return this.ownedAnimals;
    }

    /**
     * Gets owned food.
     * @return the owned food
     */
    public ArrayList<Food> getOwnedFood() { return this.ownedFood; }

    /**
     * Add to owned animals.
     * @param toAdd the to add
     */
    public void addToOwnedAnimals(Animal toAdd){
        this.ownedAnimals.add(toAdd);
    }

    /**
     * Get name string.
     * @return the string
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get turn is over boolean.
     * @return the boolean
     */
    public boolean getTurnIsOver(){
        return this.turnIsOver;
    }

    /**
     * Set turn is over.
     * @param isOver the is over
     */
    public void setTurnIsOver(boolean isOver){
        this.turnIsOver = isOver;
    }

    /**
     * Add to owned food.
     * @param toAdd the to add
     */
    public void addToOwnedFood(Food toAdd){
        this.ownedFood.add(toAdd);
    }

    /**
     * The toString of the Player class which has been replaced to allow for some more general info about the player
     * @return A string, the information about the player
     */
    @Override
    public String toString(){
        return "Name: " + this.getName() + " - Funds: " + this.getAmountOfMoney();
    }
}
