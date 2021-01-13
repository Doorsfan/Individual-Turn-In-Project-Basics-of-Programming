package Animals;

import Animals.Animal;
import Meats.cowMeat;
import processedFood.dogFood;
import processedFood.mysteryMeat;

public class Dog extends Animal {
    public Dog(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.value = 100;
        this.eats = whatItEats(new dogFood(), new mysteryMeat(), new cowMeat());
    }
}
