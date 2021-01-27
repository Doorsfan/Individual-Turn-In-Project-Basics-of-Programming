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
    //Scanners declared as Transient to not include them in Serialization
    private transient Scanner nameScanner = new Scanner(System.in);
    private transient Scanner userInput = new Scanner(System.in);
    int shopCounter = 0; //Just a simple counter ot showcase Index of items in Store
    ArrayList<Food> foodToOffer = new ArrayList<>(); //Food to offer
    ArrayList<Integer> pricesOfFood = new ArrayList<>(), pricesOfAnimals = new ArrayList<>(); //Prices of Food and Animals
    ArrayList<Animal> animalsToOffer = new ArrayList<>(); //Animals to Offer
    public Store(){
        //Fill the shop with Food for when we start it up
        foodToOffer.add(new cowMeat());
        foodToOffer.add(new horseMeat());
        foodToOffer.add(new Apple());
        foodToOffer.add(new Peanut());
        foodToOffer.add(new Seeds());
        foodToOffer.add(new catFood());
        foodToOffer.add(new dogFood());
        foodToOffer.add(new fishFood());
        foodToOffer.add(new mysteryMeat());
        for(Food pieceOfFood : this.foodToOffer){
            pricesOfFood.add(pieceOfFood.getValue()); //Add the Foods prices to a price list
        }
        Collections.sort(pricesOfFood); //Sort it, ascending order

        //Fill the Shop with Animals when we build the Shop
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
        for(Animal animalInShop : this.animalsToOffer){
            pricesOfAnimals.add(animalInShop.getValue()); //Add the Animals prices to a price list
        }
        Collections.sort(pricesOfAnimals); //Sort it, ascending order
    }

    /**
     * Let's the player specify an Animal they wish to Sell to the Shop. The sell value is defined by
     * factors such as it's Health and Age.
     *
     *
     * @param seller A player object who is the Seller selling Animals to the Store
     */
    public void sellAnimal(Player seller){
        if(userInput == null){ userInput = new Scanner(System.in); }
        ArrayList<Animal> animalsToSell = seller.getHealthyAnimals(); //The owned Animals that are available to be sold
        String wantedAnimal; //index of the wanted animal to be sold
        int returnCode;
        boolean finishedSelling = false;
        while(!finishedSelling){
            if(animalsToSell.size() == 0){ System.out.println(seller.getName() + " has no healthy animals left to sell currently."); return; }
            printSellAnimalMenu(seller); //Print the Sales menu of animals based on the Seller
            //Keep asking for a input until it is a valid index - highest accepted Index is exit index, returns back to main menu
            while(!((returnCode = (safeIntInput(1, animalsToSell.size()+1, wantedAnimal = userInput.next(),
                    true))) == 1)){ if(returnCode == 2) return; }
            Animal animalBeingSold = animalsToSell.get(Integer.parseInt(wantedAnimal)-1);
            seller.getPaid(animalBeingSold.getSellsFor());
            System.out.println(seller.getName() + " sold " + animalBeingSold.getInfo()
                    + " for " + animalBeingSold.getSellsFor() + " coins. " + seller.getName() + " now has: "
                    + seller.getAmountOfMoney() + " coins.");
            seller.getOwnedAnimals().remove(animalBeingSold);
        }
    }

    /**
     * A method that prints menu options in the Shop when buying Food
     * @param buyer A player object, the Buyer in the Store
     */
    public void printShopMenu(Player buyer){
        System.out.println("Your current funds are: " + buyer.getAmountOfMoney());
        for(Food foodInStore: foodToOffer) {
            System.out.println("[" + (shopCounter + 1) + "]. " + foodInStore.getName() +
                    " - Costs: " + foodInStore.getValue() + " coins per kilo.");
            shopCounter += 1;
        }
        System.out.println("[" + (shopCounter+1) + "]. Exit shop");
    }

    /**
     * A method that handles the upper boundary that a player can afford in terms of a specific specified type of Food
     * @param wantedFood A string, the index of the wanted food in the Food menu
     * @param buyer A player object, the player acting as the Buyer
     * @return An int - The amount of grams that the player can max purchase (to a rounded down amount of Dividable by 100)
     */
    public int getMaxAmountOfGrams(String wantedFood, Player buyer){
        int pricePerKilo = foodToOffer.get(Integer.parseInt(wantedFood)-1).getValue(); //Price per kilo of selected food
        int moneyIHave = buyer.getAmountOfMoney(); //The amount of Money the player has
        double howManyKilosICanAfford = (double)moneyIHave/(double)pricePerKilo;
        double howManyGramsICanAfford = howManyKilosICanAfford * 1000;
        int gramsAsInt = (int) howManyGramsICanAfford; //Convert downwards to closest Gram
        gramsAsInt = gramsAsInt - (gramsAsInt % 100); //Force purchase to closest dividable by 100 in upper limit
        return gramsAsInt;
    }

    /**
     * Checks if the Player already has the Food item in their own Inventory - if so, adds to that amount, if not
     * adds as a new Item to the inventory of the Player
     *
     * @param index An int, the chosen index of the Food item in the Menu
     * @param buyer A player object, the Buyer of the Food
     * @param wantedAmount A string, the amount of food the player wants in Grams
     */
    public void addToInventory(int index, Player buyer, String wantedAmount){
        boolean foundFood = false;

        Food foodToAdd = foodToOffer.get(index); //The food to add to inventory
        String foodOfNameToAdd = foodToAdd.getName();
        switch(foodOfNameToAdd){
            case "cowMeat":
                foodToAdd = new cowMeat();
                break;
            case "horseMeat":
                foodToAdd = new horseMeat();
                break;
            case "Apple":
                foodToAdd = new Apple();
                break;
            case "Peanut":
                foodToAdd = new Peanut();
                break;
            case "Seeds":
                foodToAdd = new Seeds();
                break;
            case "catFood":
                foodToAdd = new catFood();
                break;
            case "dogFood":
                foodToAdd = new dogFood();
                break;
            case "fishFood":
                foodToAdd = new fishFood();
                break;
            case "mysteryMeat":
                foodToAdd = new mysteryMeat();
                break;
        }
        for(Food ownedFood : buyer.getOwnedFood()){ //Check buyers food stocks
            if(ownedFood.getName().contains(foodToAdd.getName())){ //if there is a match in owned food and the one being purchased
                foundFood = true; //Track that it was found
                ownedFood.setGrams((ownedFood.getGrams() + Integer.parseInt(wantedAmount))); //Increase the gram of the owned food stock
            }
        }
        if(!foundFood){ //Player does not own the purchased food in any amount yet
            foodToAdd.setGrams(Integer.parseInt(wantedAmount)); //Set the amount of grams of the food being bought
            buyer.addToOwnedFood(foodToAdd);  //Add it to the players stock
        }
    }

    /**
     * A method that is responsible for purchasing Food - delegates responsibilities to Sub-methods - Forces
     * purchase amount to be between 100 and a Max of Closest to dividable by 100 Grams (i.e, 2152 would become 2100)
     * @param buyer A player object, the player buying Food
     */
    public void buyFood(Player buyer){
        String wantedFood = "", wantedAmount = "";
        int returnCode;
        if(userInput == null){ userInput = new Scanner(System.in); }
        while(buyer.getAmountOfMoney() >= pricesOfFood.get(0)/10) {
            printShopMenu(buyer);
            shopCounter = 0;
            while(!((returnCode = (safeIntInput(1, foodToOffer.size()+1, wantedFood = userInput.next(),
                    true))) == 1)){ if(returnCode == 2) return; } //Index of Wanted food
            if(getMaxAmountOfGrams(wantedFood, buyer) < 100){
                System.out.println("Cannot afford at least 100 grams of " + foodToOffer.get(Integer.parseInt(wantedFood)-1).getName()
                + ", returning back to main menu.");
                return;
            }
            System.out.println("How many grams of " + foodToOffer.get(Integer.parseInt(wantedFood)-1).getName() + " do you want? " +
                    "(Min: 100, Max: " + getMaxAmountOfGrams(wantedFood, buyer) + ")");
            while(!(safeIntInput(100,getMaxAmountOfGrams(wantedFood,buyer),wantedAmount = userInput.next(),false) == 1));
            int index = Integer.parseInt(wantedFood) - 1; //Shorthand for index
            //Price of food per kilo * amount of kilos wanted
            double toPay = Double.valueOf(foodToOffer.get(index).getValue()) * Double.valueOf(wantedAmount)/Double.valueOf(1000);

            if (toPay <= buyer.getAmountOfMoney()) { //Can afford the food
                addToInventory(index, buyer, wantedAmount); //Add to players Inventory (checks if already has, increases that amount instead then)
                buyer.pay((int) toPay);
                System.out.println(buyer.getName() + " bought " + wantedAmount + " grams of " + foodToOffer.get(index).getName() + " for " +
                        (int) toPay + " coins. " + buyer.getName() + " now has " + buyer.getAmountOfMoney() + " coins left.");
            } else { //Can't afford the food
                System.out.println(buyer.getName() + " cannot afford the " + wantedAmount + " grams of " +
                        foodToOffer.get(index).getName() + ". It costs " + (int) toPay + " and " + buyer.getName() + " only has " +
                        buyer.getAmountOfMoney() + " coins left!"); }
        }
        System.out.println(buyer.getName() + " cannot afford any food in the store at the moment. The lowest price of 100 grams of " +
                "an food item in the shop is: " + pricesOfFood.get(0)/10 + " coins.\n"); //Ran out of money in the Store
    }

    /**
     * A method that handles printing of Menus in Buying the Animals shop
     * @param buyer A player object, that is the buyer
     */
    public void printAnimalsInBuyAnimals(Player buyer){
        System.out.println("Your current funds are: " + buyer.getAmountOfMoney());
        for(Animal animalInStore: animalsToOffer){ //prints information about each Animal
            System.out.println("[" + (shopCounter+1) + "]. " + animalInStore.getClassName() +
                    "(" + animalInStore.getGender() + ") - Costs: " + animalInStore.getValue() + " coins.");
            shopCounter += 1;
        }
        System.out.println("[" + (shopCounter+1) + "]. Exit shop.");
        System.out.println("Which animal would you like to buy?");
    }

    /**
     * A method that handles the option put in for having chosen an Animal to buy in the Store and prints
     * information
     * @param buyer A player object, the buyer
     * @param index An int, the index of the chosen animal
     */
    public void choseAnimal(Player buyer, int index){
        if(nameScanner == null){ nameScanner = new Scanner(System.in); }
        if (animalsToOffer.get(index).getValue() <= buyer.getAmountOfMoney()) {
            System.out.println("What would you like to name your new " + animalsToOffer.get(index).getClassName() + "?");
            String wantedName = nameScanner.nextLine(); //The wanted name
            String wantedGender = animalsToOffer.get(index).getGender(); //The wanted Gender is based on the index chosen
            switch(animalsToOffer.get(index).getClassName()){ //based on the index, we get a ClassName and add that respective Animal
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
            buyer.pay(animalsToOffer.get(index).getValue()); //Pay the full value of the Animal
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

    /**
     * Allows a player to choose an Animal to purchase from the Store
     * @param buyer A player object who is the Buyer wanting to buy a Animal from the Shop
     */
    public void buyAnimal(Player buyer){
        if(userInput == null){ userInput = new Scanner(System.in); }
        if(buyer.getAmountOfMoney() < pricesOfAnimals.get(0)){
            System.out.println(buyer.getName() + " cannot afford any animal in the store at the moment. " +
                    "The lowest price of an animal in the shop is: " + pricesOfAnimals.get(0) + " coins.");
            return;
        }
        System.out.println("Welcome " + buyer.getName() + " to The Emporium of Animals! We sell Animals/Food for animals!");
        while(buyer.getAmountOfMoney() >= pricesOfAnimals.get(0)) {
            printAnimalsInBuyAnimals(buyer); //Print info about the animals up for sale
            shopCounter = 0;
            String wantedAnimal = "";
            int returnCode = 0;
            //While the chosen index is not equal to that of the Exit option in the Shop menu
            while(!((returnCode = (safeIntInput(1, animalsToOffer.size()+1, wantedAnimal = userInput.next(),
                    true))) == 1)){ if(returnCode == 2) return; }
            //This variable represents the Menu choice of the player
            //it is named index because it's frequency of writing is so high, that i wanted to have a shorthand name
            //for when i had to repeat it constantly
            int index = Integer.parseInt(wantedAnimal) - 1;
            choseAnimal(buyer, index);
        }
        return;
    }
}
