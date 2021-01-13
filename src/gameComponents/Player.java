package gameComponents;

import Animals.Animal;
import java.util.ArrayList;

public class Player {
    private String name;
    private int amountOfMoney = 1000;
    private boolean playing = true;
    private ArrayList<Animal> ownedAnimals = new ArrayList<>();
    private ArrayList<Food> ownedFood = new ArrayList<>();
    private boolean turnIsOver = false;

    public Player(String name, int amountOfMoney){
        this.name = name;
        this.amountOfMoney = amountOfMoney;
    }


    public int getAmountOfMoney(){
        return this.amountOfMoney;
    }

    public void setAmountOfMoney(int amount){
        this.amountOfMoney = amount;
    }

    public ArrayList<Animal> getOwnedAnimals(){
        return this.ownedAnimals;
    }

    public ArrayList<Food> getOwnedFood() { return this.ownedFood; }

    public void addToOwnedAnimals(Animal toAdd, String name){
        toAdd.setName(name);
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
