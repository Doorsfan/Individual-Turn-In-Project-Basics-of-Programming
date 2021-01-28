package gameComponents;

import Animals.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * A class that hosts useful methods in parsing Menu choices and Enforcing input elements
 */
public class utilityFunctions implements Serializable{
    private  transient Scanner nameScanner = new Scanner(System.in);

    /**
     * The method that is responsible for handling Menu printing in regards to an Player selling Animals to another Player
     * @param buyer A player object, the buyer
     * @param seller A player object, the seller
     * @param playersPlaying An arraylist of Players, the collective set of people who are still playing
     * @return An int, the index of the buyer, will be kept for processing later
     */
    public int sellingAnimalToOtherPlayerMenu(Player buyer, Player seller, ArrayList<Player> playersPlaying){
        int counter = 0, buyersIndex = 0;
        for(Player player: playersPlaying){
            if(player.equals(buyer)){
                buyersIndex = counter;
            }
            counter += 1;
        }
        System.out.println("What animal does " + seller.getName() + " wish to sell to: " + buyer.getName() + " (Funds: "
                + buyer.getAmountOfMoney() + " coins)" + "?");
        counter = 1;
        for(Animal sellersAnimals: seller.getHealthyAnimals()){
            System.out.println("[" + counter + "] " + sellersAnimals.getInfo() + " Sells for: " + sellersAnimals.getSellsFor() + " coins");
            counter += 1;
        }
        System.out.println("[" + counter + "]" + " Back to main menu");
        return buyersIndex;
    }

    /**
     * A method that handles printing when a player cannot afford an Animal
     * @param buyer A player object, the player
     * @param animalBeingSold An animal object, the Animal being sold
     */
    public void printCantAffordAnimal(Player buyer, Animal animalBeingSold){
        System.out.println(buyer.getName() + " cannot afford " + animalBeingSold.getInfo() + "! (Needed: " + animalBeingSold.getSellsFor() +
                " coins, has only " + buyer.getAmountOfMoney() + " coins)\n Returning back to main menu");
    }

    public void wait(int seconds){
        try{
            Thread.sleep((seconds * 1000)); //wait one second to allow user to react to events
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * A utility method that forces a user to write y or n (case-insensitive)
     * @param input A string, the input that the user gave
     * @return A status code message:
     *          1: Successfully verified the input as y or n
     *          -1: Did not verify the input as y or n
     * @throws RuntimeException - An exception is thrown if the input is not correct length or is anything but y or n
     *
     */
    public  int forceYOrN(String input) throws RuntimeException{
        String[] splitInput = input.split("");
        try{
            if(splitInput.length > 1){
                throw new RuntimeException("Input must be 1 character long (y or n) (Case insensitive)");
            }
            else if(input.toLowerCase().equals("y") || input.toLowerCase().equals("n")){
                return 1;
            }
            else{
                throw new RuntimeException("Input was : " + input + ", only allowed to be y or n");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * A utility method that forces an input to be a number between lowerBoundary (inclusive) and upperBoundary (inclusive)
     * If the input is not within this range, a accepted range is displayed and the given Input is also displayed
     *
     * @param lowerBoundary An int, the lower limit of what the number is allowed to be
     * @param upperBoundary An int, the upper limit of what the number is allowed to be
     * @param input A string, the users input which is to be verified to be a number and within the given range
     * @return Status code message:
     *                -1: Did not make it through
     *                 1: Made it through with no Issues
     *                 2: Used chose to exit based on feeding in an index that is the exit index
     *
     * @throws RuntimeException Throws a RuntimeException if the input is not within the accepted Range of Numbers
     */
    public  int safeIntInput(int lowerBoundary, int upperBoundary, String input, boolean maxIsExitCode) throws RuntimeException{
        int check;
        try{
            check = Integer.valueOf(input);
            if(check < lowerBoundary || check > upperBoundary){
                throw new RuntimeException("Out of accepted boundary - [LOWER: " + lowerBoundary + " UPPER: " + upperBoundary +
                        " GIVEN: " + check + "] - " + "Please write a number between: " + lowerBoundary + " and " + upperBoundary + ".");
            }
            if(check == upperBoundary && maxIsExitCode){
                System.out.println("User chose to exit. Returning back to main menu.");
                return 2;
            }
        }
        catch(Exception e){
            if(e.getMessage().contains("For input")){
                System.out.println("Please do not put in letters instead of Numbers.");
            }
            else{
                System.out.println(e.getMessage());
            }
            return -1; //Did not make it through with no issues
        }
        return 1; //Made it through with no issues
    }

    /**
     * A utility method that helps in printing Animals for when the player is in the Store to sell Animals
     *
     * @param seller A player object, the Player selling Animals to the Store
     */
    public void printSellAnimalMenu(Player seller){
        ArrayList<Animal> animalsToSell = seller.getHealthyAnimals();
        int shopCounter = 1;
        System.out.println("Which animal would " + seller.getName() + " like to sell?");
        for(Animal animal : animalsToSell){
            System.out.println("[" + shopCounter + "] " + animal.getInfo() + " (Sells for: " + animal.getSellsFor() + " coins)");
            shopCounter += 1;
        }
        System.out.println("[" + (animalsToSell.size() + 1) + "] Exit shop");
    }

    /**
     * A method that is responsible for creating babies - 50% chanse of a male or female per baby, and each
     * type of animal has a different amount of Babies they produce
     *
     * @param amountOfBabies An int, the amount of children to create
     * @param females An Arraylist of Animals, all of them being Female
     * @param playerBreeding A player Object, the player breeding the animals
     * @param secondAnimalIndex An int, the index for the chosen Female
     */
    public  void createBabies(int amountOfBabies, ArrayList<Animal> females, Player playerBreeding, int secondAnimalIndex){
        if(nameScanner == null){ nameScanner = new Scanner(System.in); }
        Random random = new Random();
        for(int i = 0; i < amountOfBabies; i++){ //Create amountOfBabies babies
            int genderChance = random.ints(1,3).findFirst().getAsInt(); //50% chanse of being female or male
            String gender; //The gender
            gender = (genderChance == 1 ? "Male" : "Female"); //If it's 1, it's a Male, otherwise, it's a Female
            System.out.println("It's a " + gender); //Announce what it is
            System.out.println("What would you like to name your new baby " + females.get(secondAnimalIndex-1).getClassName() + " " +
                    "(" + gender + ")?"); //Print the Class name (same as its parents) and the gender

            String name = nameScanner.nextLine(); //ask for a name
            switch(females.get(secondAnimalIndex-1).getClassName()){ //Based on what the parent is, the baby is the same in terms of Species
                case "Bird":
                    playerBreeding.addToOwnedAnimals(new Bird(name,gender));
                    break;
                case "Cat":
                    playerBreeding.addToOwnedAnimals(new Cat(name,gender));
                    break;
                case "Dog":
                    playerBreeding.addToOwnedAnimals(new Dog(name,gender));
                    break;
                case "Elephant":
                    playerBreeding.addToOwnedAnimals(new Elephant(name,gender));
                    break;
                case "Fish":
                    playerBreeding.addToOwnedAnimals(new Fish(name,gender));
                    break;
            }
        }
    }

    /**
     * The method that handles the printing of Menus in regards to buying Animals from Players, when selecting players
     * to buy from
      * @param buyer A player object, the buyer
     * @param sellers An ArrayList of Player objects, which is the potential sellers to buy Animals from
     */
    public void printSalesMenu(Player buyer, ArrayList<Player> sellers){
        int counter = 1;
        System.out.println("Which Player does " + buyer.getName() + " wish to buy Animals from?");
        for(Player players: sellers){ //Go through all the Sellers and print Info about them
            System.out.println("[" + counter + "] " + players.getName() + " - Owns " + players.getHealthyAnimals().size() + " healthy animal(s): ");
            for(Animal ownedAnimals : players.getHealthyAnimals()){ //Go through their Animals and Print info about them
                System.out.println("\t" + ownedAnimals.getInfo() + " Costs: " + ownedAnimals.getSellsFor() + " coins");
            }
            counter += 1;
        }
        System.out.println("[" + counter + "] Back to main menu");
    }

    /**
     * A helper method in building a list of Sellers, who are not the buyer themselves
     * @param buyer A player object, the buyer itself
     * @param sellers Array List of Player objects, used to add to and then return when built
     * @param playersPlaying Array List of Player objects that all are plays still playing in the game
     * @return An Array List of Player Objects, that are of all the Possible sellers
     */
    public ArrayList<Player> buildSellersList(Player buyer, ArrayList<Player> sellers, ArrayList<Player> playersPlaying){

        for(Player players : playersPlaying){ //Go through the playersPlaying
            if( !players.equals(buyer) ) {  //If they're not the buyer themselves
                if(players.getHealthyAnimals().size() > 0) { sellers.add(players); } //Add them to the list
            }
        }
        return sellers;
    }

    /**
     * A method that handles printing menus for when a Player is buying Animals from another Player, this part being
     * the one where you are selecting an Animal to purchase from another player
     * @param buyer A player object, the buyer
     * @param seller A player object, the Seller
     */
    public void printBuyFromOtherPlayerMenu(Player buyer, Player seller){
        int counter = 1;

        System.out.println("Which animal does " + buyer.getName() + " wish to buy from: " + seller.getName() + " (" + buyer.getName() +"'s funds: "
                + buyer.getAmountOfMoney() + " coins)" + "?");

        counter = 1;
        for(Animal sellersAnimals: seller.getHealthyAnimals()){
            System.out.println("[" + counter + "] " + sellersAnimals.getInfo() + " Costs: " + sellersAnimals.getSellsFor() + " coins");
            counter += 1;
        }
        System.out.println("[" + counter + "]" + " Back to main menu");
    }

    /**
     * A method responsible for printing out information about having insufficient funds to buy an ANimal from another Player
     * @param buyer A player object, the buyer
     * @param animalBeingBought An Animal object, the Animal trying to be bought
     */
    public void printFailedTransaction(Player buyer, Animal animalBeingBought){
        System.out.println(buyer.getName() + " cannot afford " + animalBeingBought.getInfo() + "! (Needed: " + animalBeingBought.getSellsFor() +
                " coins, has only " + buyer.getAmountOfMoney() + " coins)");
        System.out.println("Returning back to main menu.");
    }



    /**
     * The method that is responsible for clearing out animals based on that they are dead
     * @param playersPlaying An Arraylist of Player, all of which are players still in the game
     * @param currentPlayer An int, the current player
     */
    public void removeAnimals(ArrayList<Player> playersPlaying, int currentPlayer){
        if (playersPlaying.get(currentPlayer).getShouldBeRemoved().size() > 0) {
            for (int i = playersPlaying.get(currentPlayer).getShouldBeRemoved().size() - 1; i > -1; i--) {
                Animal toRemove = playersPlaying.get(currentPlayer).getShouldBeRemoved().get(i);
                playersPlaying.get(currentPlayer).getOwnedAnimals().remove(toRemove);
                playersPlaying.get(currentPlayer).getShouldBeRemoved().remove(i);
            }
        }
    }

    /**
     * This is the method that is responsible for printing out the information in the Main menu, in terms of
     * Rounds, Owned Animals, Decay
     *
     * @param loadedGame A boolean, if the Game was being loaded at this point or not
     * @param currentRound An int, the current Round
     * @param currentPlayer An int, the currentPlayer
     * @param playersPlaying An ArrayList of Player objects, all of whom are Players still playing
     */
    public void printAnimals(boolean loadedGame, int currentRound, int currentPlayer, ArrayList<Player> playersPlaying, int maxRounds) {
        System.out.println("\nRound " + currentRound + "/" + maxRounds + ", " + playersPlaying.get(currentPlayer).getName() + "'s turn.\n[Remaining players:" +
                " " + playersPlaying + "]");
        playersPlaying.get(currentPlayer).announceDeaths(currentRound);
        if (loadedGame) {
            for (String printedDeath : playersPlaying.get(currentPlayer).getSavedDeathList()) {
                System.out.println(printedDeath);
            }
        }
        System.out.print(playersPlaying.get(currentPlayer).getName() + "'s Owned Animals: \n");

        for (Animal ownedAnimal : playersPlaying.get(currentPlayer).getOwnedAnimals()) {
            System.out.print(ownedAnimal.getName() + " the " + ownedAnimal.getClass().getSimpleName() +
                    "(" + ownedAnimal.getGender() + "), who is at: "
                    + ownedAnimal.getHealth() + " health (Lost " + ownedAnimal.getLostHealth() + " health last round, was at: " +
                    ownedAnimal.getWasAtHealth() + " health.) (Age: " + ownedAnimal.getAge() + " of " + ownedAnimal.getMaxAge() + ")\n");
        }
        System.out.print(playersPlaying.get(currentPlayer).getName() + "'s Owned Food: \n");
        for (Food ownedFood : playersPlaying.get(currentPlayer).getOwnedFood()) {
            System.out.print(ownedFood.getGrams() + " Grams of " + ownedFood.getClass().getSimpleName() + "\n");
        }
    }

    /**
     * The method that is responsible for Printing out the Menu of Players that you can sell to in Sell Animals to other Players
     * Returns the index of the seller - needed to be kept track of, to prevent a bug that makes Animals avoid decaying when sold
     * to an earlier players turn
     *
     * @param seller A player object, the Seller
     * @param buyers An ArrayList of Player Objects, the buyers
     * @param playersPlaying An ArrayList of Player objects, all of whom are still players playing
     * @return An int, the index of the Seller
     */
    public int listOfBuyersInSellToOtherPlayer(Player seller, ArrayList<Player> buyers, ArrayList<Player> playersPlaying){
        int counter = 1, sellersIndex = 1;
        System.out.println("Which Player does " + seller.getName() + " wish to sell Animals to?");
        for(Player players : playersPlaying){
            if(!players.equals(seller)){
                System.out.println("[" + counter + "] " + players.getName() + " - Funds: " + players.getAmountOfMoney() + " coins.");
                counter += 1;
                buyers.add(players);
            }
            else{
                sellersIndex = counter-1; //Keep track of when the sellers turn is
            }
        }
        System.out.println("[" + counter + "] Back to main menu");
        return sellersIndex;
    }

    /**
     * The method responsible for printing out a list of Males for use when Breeding Animals
     * @param males An ArrayList of Animals, all of whom are Males
     */
    public  void printMales(ArrayList<Animal> males){
        System.out.println("Please choose a male: ");
        int counter = 1;
        for(Animal male : males){
            System.out.println("[" + counter + "] " + male.getInfo());
            counter += 1;
        }
        System.out.println("[" + counter + "] Back to Main Menu");
    }

    /**
      * The method responsible for printing out a list of Females for use when Breeding Animals
      * @param females An ArrayList of Animals, all of whom are females
     */
    public  void printFemales(ArrayList<Animal> females){
        System.out.println("Please choose a female: ");
        int counter = 1;
        for(Animal female : females){
            System.out.println("[" + counter + "] " + female.getInfo());
            counter += 1;
        } System.out.println("[" + counter + "] Back to Main Menu");
    }
}
