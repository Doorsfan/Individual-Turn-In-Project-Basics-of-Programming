package Animals;

import processedFood.fishFood;

public class Fish extends Animal {
    public Fish(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.value = 10;
        this.eats = whatItEats(new fishFood());
        this.portionSize = 20;
    }
}
