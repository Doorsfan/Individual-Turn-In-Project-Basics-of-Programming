package Animals;

import Animals.Animal;
import Plants.Apple;
import Plants.Seed;

public class Bird extends Animal {
    public Bird(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.eats = whatItEats(new Seed());
        this.value = 50;
    }
}
