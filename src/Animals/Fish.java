package Animals;

import processedFood.fishFood;

public class Fish extends Animal {
    public Fish(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.value = 10;
        this.eats = whatItEats(new fishFood());
        this.portionSize = 20;
        this.minimumOffspring = 1;
        this.maximumOffspring = 12;
        this.maxAge = 4;
    }
}
