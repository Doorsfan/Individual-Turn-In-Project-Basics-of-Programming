package Animals;

import Plants.Peanut;
import java.io.Serializable;

public class Elephant extends Animal implements Serializable {
    /**
     * Most of these values that are delegated in the Constructor are inherited from Animal.
     * Only Name and Gender are specified in Creation later, rest remain constant
     *
     * @param name String, the name of the Animal
     * @param gender String, the gender of the Animal
     */
    public Elephant(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.value = 200;
        this.eats = whatItEats(new Peanut());
        this.portionSize = 100;
        this.minimumOffspring = 1;
        this.maximumOffspring = 3;
        this.maxAge = 65;
        this.vetCost = 26;
    }
}
