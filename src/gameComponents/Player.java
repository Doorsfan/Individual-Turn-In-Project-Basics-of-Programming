package gameComponents;

import Animals.Animal;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private String name;
    private int amountOfMoney;
    private boolean playing = true;
    private ArrayList<Animal> ownedAnimals = new ArrayList<>();
    private ArrayList<Food> ownedFood = new ArrayList<>();
    private boolean turnIsOver = false;
    private ArrayList<Integer> diedAtRoundList = new ArrayList<Integer>();
    private ArrayList<String> deathMessageList = new ArrayList<String>();
    private ArrayList<Animal> shouldBeRemoved = new ArrayList<>();
    private boolean addedToHighScore = false;
    private ArrayList<String> savedDeathList = new ArrayList<String>();

    public Player(String name, int amountOfMoney){
        this.name = name;
        this.amountOfMoney = amountOfMoney;
    }

    public boolean getAddedToHighScore(){
        return this.addedToHighScore;
    }
    public void setAddedToHighScore(boolean wasAdded){
        this.addedToHighScore = wasAdded;
    }
    public ArrayList<Animal> getShouldBeRemoved(){
        return this.shouldBeRemoved;
    }

    public void addToShouldBeRemoved(Animal toAdd){
        this.shouldBeRemoved.add(toAdd);
    }

    public void addDeathAnnouncement(int timeOfDeath, String message){
        diedAtRoundList.add(timeOfDeath);
        deathMessageList.add(message);
    }


    public ArrayList<String> getSavedDeathList(){
        return this.savedDeathList;
    }
    public void addToPlayerDeathList(String toAdd){
        this.savedDeathList.add(toAdd);
    }

    public int purgeSavedDeathList(int round){
        ArrayList<String> toRemove = new ArrayList<String>();
        for(int i = 0; i < this.savedDeathList.size(); i++){
            if(savedDeathList.get(i).contains(String.valueOf("round of " + round))){
                toRemove.add(savedDeathList.get(i));
            }
        }
        for(int i = savedDeathList.size()-1; i > -1; i--){
            for(String shouldBeRemoved: toRemove){
                if(savedDeathList.get(i).equals(shouldBeRemoved)){
                    savedDeathList.remove(shouldBeRemoved);
                }
            }
        }
        return -1;
    }
    public void announceDeaths(int currentRound){
        ArrayList<Integer> removeRound = new ArrayList<Integer>();
        ArrayList<String> removeMessage = new ArrayList<String>();
        if(diedAtRoundList.size() > 0){
            for(int i = diedAtRoundList.size()-1; i > -1; i--){
                if(diedAtRoundList.get(i) == currentRound-1){
                    System.out.println(deathMessageList.get(i));
                    removeRound.add(diedAtRoundList.get(i));
                    removeMessage.add(deathMessageList.get(i));
                }
            }
            for(String deathMessage: removeMessage){
                deathMessageList.remove(deathMessage);
            }
            for(Integer roundOfDeath: removeRound){
                diedAtRoundList.remove(roundOfDeath);
            }
        }
    }

    public int getAmountOfMoney(){
        return this.amountOfMoney;
    }

    public void pay(int amount){ this.amountOfMoney -= amount; }

    public void getPaid(int amount){ this.amountOfMoney += amount; }

    public ArrayList<Animal> getOwnedAnimals(){
        return this.ownedAnimals;
    }

    public ArrayList<Food> getOwnedFood() { return this.ownedFood; }

    public void addToOwnedAnimals(Animal toAdd){
        this.ownedAnimals.add(toAdd);
    }

    public void eliminate(){
        this.playing = false;
    }

    public boolean isStillPlaying(){
        return this.playing;
    }

    public String getName(){
        return this.name;
    }

    public boolean getTurnIsOver(){
        return this.turnIsOver;
    }

    public void setTurnIsOver(boolean isOver){
        this.turnIsOver = isOver;
    }

    public void addToOwnedFood(Food toAdd){
        this.ownedFood.add(toAdd);
    }

    @Override
    public String toString(){
        return "Name: " + this.getName();
    }
}
