package gameComponents;

import Animals.*;
import Plants.*;
import Meats.*;
import processedFood.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * The class that acts as a Store to Sell animals to, Buy animals from or Buy Food from
 */
public class Store extends utilityFunctions implements Serializable {

    Scanner userInput = getMyScanner(); //Utilize a separate scanner for each place to avoid bugs in parsing Tokens
    int shopCounter = 0; //Just a simple counter ot showcase Index of items in Store

    /**
     * Let's the player specify an Animal they wish to Sell to the Shop. The sell value is defined by
     * factors such as it's Health and Age.
     *
     *
     * @param seller A player object who is the Seller selling Animals to the Store
     */
    public void sellAnimal(Player seller){
        ArrayList<Animal> animalsToSell = seller.getOwnedAnimals();

        String wantedAnimal;

        if(animalsToSell.size() == 0){
            System.out.println(seller.getName() + " has no animals to sell currently.");
            return;
        }

        printSellAnimalMenu(seller);
        while(!(safeIntInput(1, animalsToSell.size()+1, wantedAnimal = userInput.next()) != 1)){
            if(Integer.parseInt(wantedAnimal) == animalsToSell.size()+1){
                System.out.println(seller.getName() + " exited the shop.");
                return;
            }
            seller.getPaid(seller.getOwnedAnimals().get(Integer.parseInt(wantedAnimal)-1).getSellsFor());
            System.out.println(seller.getName() + " sold " + seller.getOwnedAnimals().get(Integer.parseInt(wantedAnimal)-1).getName() + " the "
                    + seller.getOwnedAnimals().get(Integer.parseInt(wantedAnimal)-1).getClassName()
                    + " for " + animalsToSell.get(Integer.parseInt(wantedAnimal)-1).getSellsFor() + " coins. " + seller.getName() + " now has: "
            + seller.getAmountOfMoney() + " coins.");

            seller.getOwnedAnimals().remove((Integer.parseInt(wantedAnimal)-1));

            if(seller.getOwnedAnimals().size() == 0){
                System.out.println(seller.getName() + " has no more animals to sell. Exiting the shop.");
                return; //Finished selling
            }
            printSellAnimalMenu(seller);
        };
    }

    /**
     * A method that is responsible for handling the purchasing of Food from the Store
     *
     * Food is bought in a range of 100 grams to MAX AFFORDED AMOUNT in grams,  If the player runs
     * out of money while in the shop - or cannot afford anything to begin with from the Shop, he/she
     * is removed/not allowed into the shop.
     *
     * @param buyer A player object that is the Buyer of the food from the Store
     * @return An int, a status code message
     */
    public int buyFood(Player buyer){
        String wantedFood = "";
        String wantedAmount = "";

        ArrayList<Food> foodToOffer = new ArrayList<>();
        foodToOffer.add(new cowMeat());
        foodToOffer.add(new horseMeat());
        foodToOffer.add(new Apple());
        foodToOffer.add(new Peanut());
        foodToOffer.add(new Seeds());
        foodToOffer.add(new catFood());
        foodToOffer.add(new dogFood());
        foodToOffer.add(new fishFood());
        foodToOffer.add(new mysteryMeat());

        ArrayList<Integer> prices = new ArrayList<>();

        for(Food pieceOfFood : foodToOffer){
            prices.add(pieceOfFood.getValue());
        }
        Collections.sort(prices);
        if(buyer.getAmountOfMoney() < prices.get(0)/10){ //The lowest amount of food that you can buy is 100 grams, and prices are in Kilos
            System.out.println(buyer.getName() + " cannot afford any food in the store at the moment. The lowest price of 100 grams of " +
                    "an food item in the shop is: " +
                    prices.get(0)/10 + " coins.\n");
            return -2;
        }


        while(buyer.getAmountOfMoney() >= prices.get(0)/10) {
            System.out.println("Your current funds are: " + buyer.getAmountOfMoney());
            for(Food foodInStore: foodToOffer) {
                System.out.println("[" + (shopCounter + 1) + "]. " + foodInStore.getName() +
                        " - Costs: " + foodInStore.getValue() + " coins per kilo.");
                shopCounter += 1;
            }

            System.out.println("[" + (shopCounter+1) + "]. Exit shop");
            shopCounter = 0;

            while(!(safeIntInput(1, foodToOffer.size()+1, wantedFood = userInput.next()) == 1));
            if (Integer.parseInt(wantedFood) == foodToOffer.size()+1) {
                System.out.println(buyer.getName() + " decided to leave the shop.");
                return -1;
            }

            int pricePerKilo = foodToOffer.get(Integer.parseInt(wantedFood)-1).getValue(); //500
            int moneyIHave = buyer.getAmountOfMoney(); //1000
            double howManyKilosICanAfford = (double)moneyIHave/(double)pricePerKilo;
            double howManyGramsICanAfford = howManyKilosICanAfford * 1000;
            int gramsAsInt = (int) howManyGramsICanAfford;
            gramsAsInt = gramsAsInt - (gramsAsInt % 100); //Down to the closest 100 grams

            System.out.println("How many grams of " + foodToOffer.get(Integer.parseInt(wantedFood)-1).getName() + " do you want? " +
                    "(Min: 100, Max: " + gramsAsInt + ")");
            while(!(safeIntInput(100, gramsAsInt  ,wantedAmount = userInput.next()) == 1));
            int index = Integer.parseInt(wantedFood) - 1;
            double toPay = Double.valueOf(foodToOffer.get(index).getValue()) * Double.valueOf(wantedAmount)/Double.valueOf(1000);
            boolean foundFood = false;

            if (toPay <= buyer.getAmountOfMoney()) {
                Food foodToAdd = foodToOffer.get(index);

                for(Food ownedFood : buyer.getOwnedFood()){
                    if(ownedFood.getName().contains(foodToAdd.getName())){
                        foundFood = true;
                        ownedFood.setGrams((ownedFood.getGrams() + Integer.parseInt(wantedAmount)));
                    }
                }
                if(!foundFood){
                    foodToAdd.setGrams(Integer.parseInt(wantedAmount));
                    buyer.addToOwnedFood(foodToAdd);
                }

                buyer.pay((int) toPay);

                System.out.println(buyer.getName() + " bought " + wantedAmount +
                        " grams of " + foodToOffer.get(index).getName() + " for " +
                        (int) toPay + " coins. " + buyer.getName() + " now has " +
                        buyer.getAmountOfMoney() + " coins left.");

            } else {
                System.out.println(buyer.getName() + " cannot afford the " + wantedAmount + " grams of " +
                        foodToOffer.get(index).getName() + ". It costs " +
                        (int) toPay + " and " + buyer.getName() + " only has " +
                        buyer.getAmountOfMoney() + " coins left!");
            }
        }
        return -1;
    }

    /**
     * A method that is responsible for handling the Purchase of Animals from the Shop
     *
     * While the Buyer still has funds left to purchase the cheapest animal in the Shop,
     * they are allowed to stay - Upon purchase of each Animal, they are prompted to give
     * a name to the Animal.
     *
     * @param buyer A player object, the buyer of Animals from the Shop
     * @return An int, a status code message
     */
    public int buyAnimal(Player buyer){
        ArrayList<Animal> animalsToOffer = new ArrayList<>();
        animalsToOffer.add(new Bird("", "Male"));
        animalsToOffer.add(new Bird("", "Female"));
        animalsToOffer.add(new Cat("", "Male"));
        animalsToOffer.add(new Cat("", "Female"));
        animalsToOffer.add(new Dog("", "Male"));
        animalsToOffer.add(new Dog("", "Female"));
        animalsToOffer.add(new Elephant("", "Male"));
        animalsToOffer.add(new Elephant("", "Female"));
        animalsToOffer.add(new Fish("", "Male"));
        animalsToOffer.add(new Fish("", "Female"));

        ArrayList<Integer> prices = new ArrayList<>();

        for(Animal animal : animalsToOffer){
            prices.add(animal.getValue());
        }
        Collections.sort(prices);
        if(buyer.getAmountOfMoney() < prices.get(0)){
            System.out.println(buyer.getName() + " cannot afford any animal in the store at the moment. The lowest price of an animal in the shop is: " +
                    prices.get(0) + " coins.");
            return -2;
        }

        System.out.println("Welcome " + buyer.getName() + " to The Emporium of Animals! We sell Animals/Food for animals!");

        while(buyer.getAmountOfMoney() >= prices.get(0)) {
            System.out.println("Your current funds are: " + buyer.getAmountOfMoney());
            for(Animal animalInStore: animalsToOffer){
                System.out.println("[" + (shopCounter+1) + "]. " + animalInStore.getClassName() +
                        "(" + animalInStore.getGender() + ") - Costs: " + animalInStore.getValue() + " coins.");
                shopCounter += 1;
            }
            System.out.println("[" + (shopCounter+1) + "]. Exit shop.");
            System.out.println("Which animal would you like to buy?");
            shopCounter = 0;

            String wantedAnimal = "";
            while(!(safeIntInput(1, animalsToOffer.size()+1,wantedAnimal = userInput.next()) == 1));
            if (Integer.parseInt(wantedAnimal) == animalsToOffer.size()+1) {
                System.out.println(buyer.getName() + " decided to leave the shop.");
                return -1;
            }
            //This variable represents the Menu choise of the player
            //it is named index because it's frequency of writing is so high, that i wanted to have a shorthand name
            //for when i had to repeat it constantly
            int index = Integer.parseInt(wantedAnimal) - 1;
            if (animalsToOffer.get(index).getValue() <= buyer.getAmountOfMoney()) {
                Scanner nameScanner = new Scanner(System.in);
                System.out.println("What would you like to name your new " + animalsToOffer.get(index).getClassName() + "?");
                String wantedName = nameScanner.nextLine();

                String wantedGender = animalsToOffer.get(index).getGender();
                switch(animalsToOffer.get(index).getClassName()){
                    case "Bird":
                        buyer.addToOwnedAnimals(new Bird(wantedName, wantedGender));
                        break;
                    case "Cat":
                        buyer.addToOwnedAnimals(new Cat(wantedName, wantedGender));
                        break;
                    case "Dog":
                        buyer.addToOwnedAnimals(new Dog(wantedName, wantedGender));
                        break;
                    case "Elephant":
                        buyer.addToOwnedAnimals(new Elephant(wantedName, wantedGender));
                        break;
                    case "Fish":
                        buyer.addToOwnedAnimals(new Fish(wantedName, wantedGender));
                        break;
                }
                buyer.pay(animalsToOffer.get(index).getValue());

                System.out.println(buyer.getName() + " bought a " + animalsToOffer.get(index).getGender() +
                        " " + animalsToOffer.get(index).getClassName() + " for " +
                        animalsToOffer.get(index).getValue() + " coins. " + buyer.getName() + " now has " +
                        buyer.getAmountOfMoney() + " coins left.");

            } else {
                System.out.println(buyer.getName() + " cannot afford the " + animalsToOffer.get(index).getClassName() + ". It costs " +
                        animalsToOffer.get(index).getValue() + " and " + buyer.getName() + " only has " +
                        buyer.getAmountOfMoney() + " coins left!");
            }
        }
        return -1;
    }
}
