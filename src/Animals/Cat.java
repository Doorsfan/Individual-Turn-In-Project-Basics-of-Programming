package Animals;

import Animals.Animal;
import Meats.cowMeat;
import processedFood.mysteryMeat;
import processedFood.catFood;

public class Cat extends Animal {
    public Cat(String name, String gender){
        this.name = name;
        this.gender = gender;
        this.value = 60;
        this.eats = whatItEats(new mysteryMeat(), new catFood(), new cowMeat());
    }
}
