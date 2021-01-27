package gameComponents;

import Animals.Animal;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class that is responsible for handling each Player and some of their interactions
 * Handles Death announcements, old death logs (used in Loading and Saving),
 */
public class Player implements Serializable {
    private String name;
    private int amountOfMoney;
    private boolean playing = true;
    private ArrayList<Animal> ownedAnimals = new ArrayList<>(), shouldBeRemoved = new ArrayList<>();;
    private ArrayList<Food> ownedFood = new ArrayList<>();
    private boolean turnIsOver = false, addedToHighScore = false;
    private ArrayList<Integer> diedAtRoundList = new ArrayList<Integer>();
    private ArrayList<String> deathMessageList = new ArrayList<String>(), savedDeathList = new ArrayList<String>();

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
     * Get added to high score boolean.
     *
     * @return the boolean
     */
    public boolean getAddedToHighScore(){
        return this.addedToHighScore;
    }

    /**
     * Set added to high score.
     *
     * @param wasAdded the was added
     */
    public void setAddedToHighScore(boolean wasAdded){
        this.addedToHighScore = wasAdded;
    }

    /**
     * Getter that aids in interacting with what Animal should be removed from owned Animals in Game from Players
     *
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
     * @param round An int, a round notation in the death message that we use to identify which message to remove,
     *              save it in a list and then remove from the list when we have composed all of the relevant indexes
     * @return An int, a status code
     */
    public int purgeSavedDeathList(int round){
        ArrayList<String> toRemove = new ArrayList<String>();
        for(int i = 0; i < this.savedDeathList.size(); i++){ //Loop through to find which elements to remove
            if(savedDeathList.get(i).contains(String.valueOf("round of " + round))){
                toRemove.add(savedDeathList.get(i));
            }
        }
        for(int i = savedDeathList.size()-1; i > -1; i--){
            for(String shouldBeRemoved: toRemove){ //Then actually remove them
                if(savedDeathList.get(i).equals(shouldBeRemoved)){
                    savedDeathList.remove(shouldBeRemoved);
                }
            }
        }
        return -1;
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
        ArrayList<Integer> removeRound = new ArrayList<Integer>();
        ArrayList<String> removeMessage = new ArrayList<String>();
        if(diedAtRoundList.size() > 0){
            for(int i = diedAtRoundList.size()-1; i > -1; i--){
                if(diedAtRoundList.get(i) == currentRound-1){ //Check deaths for corresponding to last round
                    System.out.println(deathMessageList.get(i)); //Print it out
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
     * Get amount of money int.
     *
     * @return the int
     */
    public int getAmountOfMoney(){
        return this.amountOfMoney;
    }

    /**
     * Pay.
     *
     * @param amount the amount
     */
    public void pay(int amount){ this.amountOfMoney -= amount; }

    /**
     * Get paid.
     *
     * @param amount the amount
     */
    public void getPaid(int amount){ this.amountOfMoney += amount; }

    /**
     * Get owned animals array list.
     *
     * @return the array list
     */
    public ArrayList<Animal> getOwnedAnimals(){
        return this.ownedAnimals;
    }

    /**
     * Gets owned food.
     *
     * @return the owned food
     */
    public ArrayList<Food> getOwnedFood() { return this.ownedFood; }

    /**
     * Add to owned animals.
     *
     * @param toAdd the to add
     */
    public void addToOwnedAnimals(Animal toAdd){
        this.ownedAnimals.add(toAdd);
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
     * Get turn is over boolean.
     *
     * @return the boolean
     */
    public boolean getTurnIsOver(){
        return this.turnIsOver;
    }

    /**
     * Set turn is over.
     *
     * @param isOver the is over
     */
    public void setTurnIsOver(boolean isOver){
        this.turnIsOver = isOver;
    }

    /**
     * Add to owned food.
     *
     * @param toAdd the to add
     */
    public void addToOwnedFood(Food toAdd){
        this.ownedFood.add(toAdd);
    }

    @Override
    public String toString(){
        return "Name: " + this.getName() + " Funds: " + this.getAmountOfMoney();
    }
}
