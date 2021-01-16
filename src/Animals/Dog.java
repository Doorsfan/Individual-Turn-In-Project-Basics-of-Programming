package Animals;

import Meats.cowMeat;
import processedFood.dogFood;
import processedFood.mysteryMeat;

public class Dog extends Animal {
    public Dog(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.value = 100;
        this.eats = whatItEats(new dogFood(), new mysteryMeat(), new cowMeat());
        this.portionSize = 75;
        this.minimumOffspring = 3;
        this.maximumOffspring = 10;
        this.maxAge = 11;
    }
}
