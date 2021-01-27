package Animals;

import Meats.cowMeat;
import processedFood.mysteryMeat;
import processedFood.catFood;
import java.io.Serializable;

public class Cat extends Animal implements Serializable {
    /**
     * Most of these values that are delegated in the Constructor are inherited from Animal.
     * Only Name and Gender are specified in Creation later, rest remain constant
     *
     * @param name String, the name of the Animal
     * @param gender String, the gender of the Animal
     */
    public Cat(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.value = 60;
        this.eats = whatItEats(new mysteryMeat(), new catFood(), new cowMeat());
        this.portionSize = 50;
        this.minimumOffspring = 1;
        this.maximumOffspring = 14;
        this.maxAge = 9;
        this.vetCost = 13;
    }
}
