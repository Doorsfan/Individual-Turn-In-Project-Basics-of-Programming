package Animals;

import processedFood.fishFood;

import java.io.Serializable;

public class Fish extends Animal implements Serializable {
    /**
     * Most of these values that are delegated in the Constructor are inherited from Animal.
     * Only Name and Gender are specified in Creation later, rest remain constant
     *
     * @param name String, the name of the Animal
     * @param gender String, the gender of the Animal
     */
    public Fish(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.value = 10;
        this.eats = whatItEats(new fishFood());
        this.portionSize = 20;
        this.minimumOffspring = 1;
        this.maximumOffspring = 12;
        this.maxAge = 4;
        this.vetCost = 2;
    }
}
