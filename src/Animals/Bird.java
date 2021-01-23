package Animals;

import Plants.Seeds;

import java.io.Serializable;

public class Bird extends Animal implements Serializable {
    /**
     * Most of these values that are delegated in the Constructor are inherited from Animal.
     * Only Name and Gender are specified in Creation later, rest remain constant
     *
     * @param name String, the name of the Animal
     * @param gender String, the gender of the Animal
     */
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
