package gameComponents;

import Animals.Animal;
import java.util.ArrayList;

public class Player {
    private String name;
    private int amountOfMoney = 100;
    private boolean playing = true;
    ArrayList<Animal> ownedAnimals = new ArrayList<>();

    public Player(String name, int amountOfMoney){
        this.name = name;
        this.amountOfMoney = amountOfMoney;
    }

    public void eliminate(){
        this.playing = false;
    }

    public boolean isStillPlaying(){
        return this.playing;
    }
}
