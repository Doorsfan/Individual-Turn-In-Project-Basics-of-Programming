package Animals;

import Animals.Animal;
import Plants.Peanut;

public class Elephant extends Animal {
    public Elephant(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.value = 200;
        this.eats = whatItEats(new Peanut());
    }
}
