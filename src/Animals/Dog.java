package Animals;

import Meats.cowMeat;
import processedFood.dogFood;
import processedFood.mysteryMeat;

public class Dog extends Animal {
    /**
     * Most of these values that are delegated in the Constructor are inherited from Animal.
     * Only Name and Gender are specified in Creation later, rest remain constant
     *
     * @param name String, the name of the Animal
     * @param gender String, the gender of the Animal
     */
    public Dog(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.value = 100;
        this.eats = whatItEats(new dogFood(), new mysteryMeat(), new cowMeat());
        this.portionSize = 75;
        this.minimumOffspring = 3;
        this.maximumOffspring = 10;
        this.maxAge = 11;
        this.vetCost = 20;
    }
}
