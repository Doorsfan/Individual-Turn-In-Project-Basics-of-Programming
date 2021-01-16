package gameComponents;

import Animals.Animal;
import java.util.ArrayList;

public class Player {
    private String name;
    private int amountOfMoney;
    private boolean playing = true;
    private ArrayList<Animal> ownedAnimals = new ArrayList<>();
    private ArrayList<Food> ownedFood = new ArrayList<>();
    private boolean turnIsOver = false;
    private ArrayList<Integer> diedAtRoundList = new ArrayList<Integer>();
    private ArrayList<String> deathMessageList = new ArrayList<String>();

    public Player(String name, int amountOfMoney){
        this.name = name;
        this.amountOfMoney = amountOfMoney;
    }

    public void addDeathAnnouncement(int timeOfDeath, String message){
        diedAtRoundList.add(timeOfDeath);
        deathMessageList.add(message);
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
}
