package Animals;

import Plants.Seeds;

public class Bird extends Animal {
    public Bird(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.eats = whatItEats(new Seeds());
        this.value = 50;
        this.portionSize = 25;
        this.minimumOffspring = 2;
        this.maximumOffspring = 7;
        this.maxAge = 4;
        this.vetCost = 8;
    }
}
