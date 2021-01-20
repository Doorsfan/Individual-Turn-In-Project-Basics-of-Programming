package gameComponents;

import java.io.Serializable;

public abstract class Food implements Serializable {
    protected int value;
    protected int grams = 1000;

    public int getValue(){
        return this.value;
    }
    public int getGrams(){
        return this.grams;
    }

    public void setGrams(int amount){
        this.grams = amount;
    }

    public String getName() { return this.getClass().getSimpleName(); };

    public void reduceFromStock(int amount){
        if(this.grams >= amount)
        {
            this.grams -= amount;
        }

    }
}
