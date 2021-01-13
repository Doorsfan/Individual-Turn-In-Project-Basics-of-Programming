package gameComponents;

import Animals.*;
import Plants.*;
import Meats.*;
import processedFood.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Store {

    public int sellAnimal(Player seller){
        int shopCounter = 1;
        boolean finishedSelling = false;
        ArrayList<Animal> animalsToSell = seller.getOwnedAnimals();
        if(animalsToSell.size() == 0){
            System.out.println(seller.getName() + " has no animals to sell currently.");
            return -2; //Exit code for unsuccessful entry attempt
        }

        Scanner sellAnimalInput = new Scanner(System.in);

        double animalHealth = 0.00;
        while(!finishedSelling){
            if(seller.getOwnedAnimals().size() == 0){
                System.out.println(seller.getName() + " has no more animals to sell. Exiting the shop.");
                return -1; //Finished selling
            }
            System.out.println("Which animal would " + seller.getName() + " like to sell?");
            for(Animal animal : animalsToSell){
                System.out.println("[" + shopCounter + "] " + animal.getName() + " the " + animal.getGender() + " " + animal.getClass().getSimpleName() +
                        " - Health: " + animal.getHealth() + " - Sells for: " + animal.getSellsFor());
                shopCounter += 1;
            }
            System.out.println("[" + (animalsToSell.size() + 1) + "] Exit shop");
            try{
                int input = Integer.valueOf(sellAnimalInput.next());
                if(input > (animalsToSell.size()+1) || input < 1){ // [Animal 1] and Exit, 2 elements, size: 1 + exit
                    throw new RuntimeException("That is not a valid option, please try again.");
                }
                if(input != animalsToSell.size()+1){ //If it's not the exit option
                    System.out.println(seller.getName() + " sold " + seller.getOwnedAnimals().get(input-1).getName() + " the "
                            + seller.getOwnedAnimals().get(input-1).getClass().getSimpleName()
                            + " for " + animalsToSell.get(input-1).getSellsFor() + " coins.");
                    seller.getPaid(seller.getOwnedAnimals().get(input-1).getSellsFor());
                    seller.getOwnedAnimals().remove((input-1));
                }
                else{
                    return -1; //Exit code for successful exit
                }
            }
            catch(Exception e){
                // TO DO
                e.getMessage();
            }
        }
        return -1;
    }

    public int buyAnimalorFood(Player buyer, String purpose){
        int shopCounter = 0;
        boolean boughtFood = false;
        boolean wantedFoodIsCorrect = false;
        String wantedFood = "";
        String wantedAmount = "";

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

        ArrayList<Food> foodToOffer = new ArrayList<>();
        foodToOffer.add(new cowMeat());
        foodToOffer.add(new horseMeat());
        foodToOffer.add(new Apple());
        foodToOffer.add(new Peanut());
        foodToOffer.add(new Seed());
        foodToOffer.add(new catFood());
        foodToOffer.add(new dogFood());
        foodToOffer.add(new fishFood());
        foodToOffer.add(new mysteryMeat());

        ArrayList<Integer> prices = new ArrayList<>();
        if(purpose.equals("Buy Food")){
            for(Food pieceOfFood : foodToOffer){
                prices.add(pieceOfFood.getValue());
            }
            Collections.sort(prices);
            if(buyer.getAmountOfMoney() < prices.get(0)){
                System.out.println(buyer.getName() + " cannot afford any food in the store at the moment. The lowest price of an food item in the shop is: " +
                        prices.get(0) + " coins.");
                return -2;
            }

        }
        if(purpose.equals("Buy Animal")){

            for(Animal animal : animalsToOffer){
                prices.add(animal.getValue());
            }
            Collections.sort(prices);
            if(buyer.getAmountOfMoney() < prices.get(0)){
                System.out.println(buyer.getName() + " cannot afford any animal in the store at the moment. The lowest price of an animal in the shop is: " +
                        prices.get(0) + " coins.");
                return -2;
            }
        }

        System.out.println("Welcome " + buyer.getName() + " to The Emporium of Animals! We sell Animals/Food for animals!");
        Scanner input = new Scanner(System.in);
        while(buyer.getAmountOfMoney() >= prices.get(0)) {
            boughtFood = false;
            wantedFoodIsCorrect = false;

            try {
                System.out.println("Your current funds are: " + buyer.getAmountOfMoney());

                if(purpose.equals("Buy Food")){
                    for(Food foodInStore: foodToOffer){
                        System.out.println("[" + (shopCounter+1) + "]. " + foodInStore.getClass().getSimpleName() +
                                " - Costs: " + foodInStore.getValue() + " coins per kilo.");
                        shopCounter += 1;
                    }
                    System.out.println("[" + (shopCounter+1) + "]. Exit shop.");
                    shopCounter = 0;

                    while(!wantedFoodIsCorrect){
                        try{
                            System.out.println("Which food item would you like to buy?");
                            wantedFood = input.next();
                            if(Integer.valueOf(wantedFood) < 1 || Integer.valueOf(wantedFood) > 10){
                                throw new RuntimeException("That is not a valid option in this menu, please try again.");
                            }
                            wantedFoodIsCorrect = true;
                        }
                        catch(Exception e){
                            if(e.getMessage().contains("For input string")){
                                System.out.println("Please input a number, not letters.");
                            }
                            else{
                                System.out.println(e.getMessage());
                            }
                        }
                    }

                    if (Integer.valueOf(wantedFood) == 10) {
                        System.out.println(buyer.getName() + " decided to leave the shop.");
                        return -1;
                    }

                    while(!boughtFood){
                        try{
                            System.out.println("How many grams? (Specify in steps of 100 grams, so 100,200 or 300 etc.)");
                            wantedAmount = input.next();
                            if(Integer.valueOf(wantedAmount) % 100 == 0){
                                boughtFood = true;
                            }
                            else{
                                throw new RuntimeException("You can't request an amount that is not dividable by 100!");
                            }
                        }
                        catch(Exception e){
                            if(e.getMessage().contains("For input string")){
                                System.out.println("Please only write numbers as options.");
                            }
                            else{
                                System.out.println(e.getMessage());
                            }
                        }
                    }


                    int index = Integer.valueOf(wantedFood) - 1;
                    double toPay = Double.valueOf(foodToOffer.get(index).getValue()) * Double.valueOf(wantedAmount)/Double.valueOf(1000);

                    if (toPay <= buyer.getAmountOfMoney()) {
                        Food foodToAdd = foodToOffer.get(index);
                        foodToAdd.setGrams(Integer.valueOf(wantedAmount));
                        buyer.addToOwnedFood(foodToAdd);
                        buyer.pay((int) toPay);

                        System.out.println(buyer.getName() + " bought " + wantedAmount +
                                " grams of " + foodToOffer.get(index).getClass().getSimpleName() + " for " +
                                (int) toPay + " coins. " + buyer.getName() + " now has " +
                                buyer.getAmountOfMoney() + " coins left.");

                    } else {
                        System.out.println(buyer.getName() + " cannot afford the " + wantedAmount + " grams of " + foodToOffer.get(index).getClass().getSimpleName() + ". It costs " +
                                (int) toPay + " and " + buyer.getName() + " only has " +
                                buyer.getAmountOfMoney() + " coins left!");
                    }
                }
                if(purpose.equals("Buy Animal")){
                    for(Animal animalInStore: animalsToOffer){
                        System.out.println("[" + (shopCounter+1) + "]. " + animalInStore.getClass().getSimpleName() +
                                "(" + animalInStore.getGender() + ") - Costs: " + animalInStore.getValue() + " coins.");
                        shopCounter += 1;
                    }
                    System.out.println("[" + (shopCounter+1) + "]. Exit shop.");
                    System.out.println("Which animal would you like to buy?");
                    shopCounter = 0;
                    String wantedAnimal = input.next();

                    if (Integer.valueOf(wantedAnimal) == 11) {
                        System.out.println(buyer.getName() + " decided to leave the shop.");
                        return -1;
                    }
                    //This variable represents the Menu choise of the player
                    //it is named index because it's frequency of writing is so high, that i wanted to have a shorthand name
                    //for when i had to repeat it constantly
                    int index = Integer.valueOf(wantedAnimal) - 1;
                    if (animalsToOffer.get(index).getValue() <= buyer.getAmountOfMoney()) {
                        System.out.println("What would you like to name your new " + animalsToOffer.get(index).getClass().getSimpleName() + "?");
                        String wantedName = input.next();
                        String wantedGender = animalsToOffer.get(index).getGender();
                        switch(animalsToOffer.get(index).getClass().getSimpleName()){
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
                                " " + animalsToOffer.get(index).getClass().getSimpleName() + " for " +
                                animalsToOffer.get(index).getValue() + " coins. " + buyer.getName() + " now has " +
                                buyer.getAmountOfMoney() + " coins left.");

                    } else {
                        System.out.println(buyer.getName() + " cannot afford the " + animalsToOffer.get(index).getClass().getSimpleName() + ". It costs " +
                                animalsToOffer.get(index).getValue() + " and " + buyer.getName() + " only has " +
                                buyer.getAmountOfMoney() + " coins left!");
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not afford that item. Please try again.");
            }

        }
        return -1;
    }
}
