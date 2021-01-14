package Animals;

import Plants.Seed;

public class Bird extends Animal {
    public Bird(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.eats = whatItEats(new Seed());
        this.value = 50;
        this.portionSize = 25;
    }
}
