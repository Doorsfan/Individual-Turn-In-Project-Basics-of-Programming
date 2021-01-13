package gameComponents;

public abstract class Food {
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
}
