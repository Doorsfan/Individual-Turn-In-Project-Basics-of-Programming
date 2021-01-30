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

    // ================= PRINT METHODS =============================
    /**
     * A method that handles printing when a player cannot afford an Animal
     * @param buyer A player object, the player
     * @param animalBeingSold An animal object, the Animal being sold
     */
    public void printCantAffordAnimal(Player buyer, Animal animalBeingSold){
        //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
        System.out.println("\u001b[31m" + buyer.getName() + " cannot afford " + animalBeingSold.getVanillaInfo() + "! (Needed: " + animalBeingSold.getSellsFor() +
                " coins, has only " + buyer.getAmountOfMoney() + " coins)\n Returning back to main menu\u001b[0m");
    }

    /**
     * A utility method that helps in printing Animals for when the player is in the Store to sell Animals
     *
     * @param seller A player object, the Player selling Animals to the Store
     */
    public void printSellAnimalMenu(Player seller){
        ArrayList<Animal> animalsToSell = seller.getHealthyAnimals();
        int shopCounter = 1;
        //Code for Yellow in Consoles - \u001b[33m - Reset code for Colors in Console \u001b[0m
        System.out.println("\u001b[33mWhich animal would " + seller.getName() + " like to sell?\u001b[0m");
        for(Animal animal : animalsToSell){
            System.out.println("[" + shopCounter + "] " + animal.getColoredInfo() + " (Sells for: " + animal.getSellsFor() + " coins)");
            shopCounter += 1;
        }
        System.out.println("[" + (animalsToSell.size() + 1) + "] Exit shop");
    }

    /**
     * The method that handles the printing of Menus in regards to buying Animals from Players, when selecting players
     * to buy from
     * @param buyer A player object, the buyer
     * @param sellers An ArrayList of Player objects, which is the potential sellers to buy Animals from
     */
    public void printSalesMenu(Player buyer, ArrayList<Player> sellers){
        int counter = 1;
        //Code for Yellow in Consoles - \u001b[33m - Reset code for Colors in Console \u001b[0m
        System.out.println("\u001b[33mWhich Player does " + buyer.getName() + " wish to buy Animals from? ("
                + buyer.getName() + "'s funds: " + buyer.getAmountOfMoney() + " coins)\u001b[0m");
        for(Player players: sellers){ //Go through all the Sellers and print Info about them
            System.out.println("[" + counter + "] " + players.getName() + " - Owns "
                    + players.getHealthyAnimals().size() + " healthy animal(s): ");
            for(Animal ownedAnimals : players.getHealthyAnimals()){ //Go through their Animals and Print info about them
                System.out.println("\t" + ownedAnimals.getColoredInfo() + " Costs: " + ownedAnimals.getSellsFor() + " coins");
            }
            counter += 1;
        }
        System.out.println("[" + counter + "] Back to main menu");
    }

    /**
     * A method that handles printing menus for when a Player is buying Animals from another Player, this part being
     * the one where you are selecting an Animal to purchase from another player
     * @param buyer A player object, the buyer
     * @param seller A player object, the Seller
     */
    public void printBuyFromOtherPlayerMenu(Player buyer, Player seller){
        int counter = 1;
        //Code for Yellow in Consoles - \u001b[33m - Reset code for Colors in Console \u001b[0m
        System.out.println("\u001b[33mWhich animal does " + buyer.getName() + " wish to buy from: " + seller.getName()
                + " (" + buyer.getName() +"'s funds: "
                + buyer.getAmountOfMoney() + " coins)" + "?\u001b[0m");
        for(Animal sellersAnimals: seller.getHealthyAnimals()){
            System.out.println("[" + counter + "] " + sellersAnimals.getColoredInfo() + " Costs: " + sellersAnimals.getSellsFor() + " coins");
            counter += 1;
        }
        System.out.println("[" + counter + "]" + " Back to main menu");
    }

    /**
     * A method responsible for printing out information about having insufficient funds to buy an Animal from another Player
     * @param buyer A player object, the buyer
     * @param animalBeingBought An Animal object, the Animal trying to be bought
     */
    public void printFailedTransaction(Player buyer, Animal animalBeingBought){
        //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
        System.out.println("\u001b[31m" + buyer.getName() + " cannot afford " + animalBeingBought.getVanillaInfo() + "! " +
                "(Needed: " + animalBeingBought.getSellsFor() + " coins, has only " + buyer.getAmountOfMoney() + " coins)\u001b[0m");
        //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
        System.out.println("\u001b[31mReturning to Game menu.\u001b[0m");
    }

    /**
     *
     * @param loadedGame A boolean, if the game was loaded or not
     * @param currentRound An int, the current Round
     * @param currentPlayer An int, the index of the current Player
     * @param playersPlaying An ArrayList of Players, all of the players still Playing
     * @param maxRounds An int, the max amount of rounds
     */
    public void printAnimals(boolean loadedGame, int currentRound, int currentPlayer, ArrayList<Player> playersPlaying, int maxRounds) {
        int counter = 0;
        //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
        System.out.println("\nRound " + currentRound + "/" + maxRounds + ", \u001B[1m\u001b[31m"
                + playersPlaying.get(currentPlayer).getName() + "\u001b[0m's turn.");
        System.out.println("\t~~===    STILL PLAYING    ===~~\t");
        for(Player playerStillPlaying: playersPlaying){
            if(counter == currentPlayer){ //Mark the current players name as red to highlight it in the turn order
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                System.out.print("== \u001b[31m" + playerStillPlaying.getName() + "\u001b[0m - " + " Funds: " +
                        playerStillPlaying.getAmountOfMoney() + " coins ==\n");
            }
            else{ //otherwise, it's just standard text with no coloring
                System.out.print("== " + playerStillPlaying.getName() + " - " + " Funds: " +
                        playerStillPlaying.getAmountOfMoney() + " coins ==\n");
            }
            counter += 1;
        }
        counter = 0;
        playersPlaying.get(currentPlayer).announceDeaths(currentRound); //Announce the relevant deaths for the current player
        if (loadedGame) { //If a game was loaded
            for (String printedDeath : playersPlaying.get(currentPlayer).getSavedDeathList()) { //print the death lists from the death Archive
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                System.out.println("\u001b[31m" + printedDeath + "\u001b[0m");
            }
        }
        //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
        System.out.print("\n\u001b[31m" + playersPlaying.get(currentPlayer).getName() + "\u001b[0m's Owned Animals: \n");

        //Print info about the current living Animals
        for (Animal ownedAnimal : playersPlaying.get(currentPlayer).getOwnedAnimals()) {
            System.out.print(ownedAnimal.getName() + " the " + ownedAnimal.getClass().getSimpleName() +
                    "(" + ownedAnimal.getGender() + "), who is at: ");
            if(ownedAnimal.getHealth() >= 50){ //Is healthy
                //Code for Green in Consoles - \u001b[32m - Reset code for Colors in Console \u001b[0m
                System.out.print("\u001b[32m"); //Green health
            }
            else if(ownedAnimal.getHealth() >= 30 && ownedAnimal.getHealth() <= 49){ //A bit worse off
                //Code for Yellow in Consoles - \u001b[33m - Reset code for Colors in Console \u001b[0m
                System.out.print("\u001b[33m"); //Yellow health
            }
            else{ //is in the danger zone of dying
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                System.out.print("\u001b[31m"); //Red health
            }
            System.out.print(ownedAnimal.getHealth() + "\u001b[0m health (Lost " + ownedAnimal.getLostHealth()
                    + " health last round, was at: " + ownedAnimal.getWasAtHealth() + " health.) (Age: ");
            double percentOfYearsSpent = (double)ownedAnimal.getAge()/(double)ownedAnimal.getMaxAge(); //How much of their time they have left
            if(percentOfYearsSpent >= 0.75){ //Lived for at least 75% of their life span
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                System.out.print("\u001b[31m"); //Red age
            }
            else if(percentOfYearsSpent >= 0.33 && percentOfYearsSpent <= 74){ //lived 33-74% of its lifespan
                //Code for Yellow in Consoles - \u001b[33m - Reset code for Colors in Console \u001b[0m
                System.out.print("\u001b[33m"); //Yellow age
            }
            else{ //Hasnt lived for 33% of its lifespan yet
                //Code for Green in Consoles - \u001b[32m - Reset code for Colors in Console \u001b[0m
                System.out.print("\u001b[32m"); //Green age
            }
            System.out.print(ownedAnimal.getAge() + "\u001b[0m of " + ownedAnimal.getMaxAge() + ")\n");
        }
        //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
        System.out.print("\u001b[31m" + playersPlaying.get(currentPlayer).getName() + "\u001b[0m's Owned Food: \n");
        for (Food ownedFood : playersPlaying.get(currentPlayer).getOwnedFood()) {
            System.out.print(ownedFood.getGrams() + " Grams of " + ownedFood.getClass().getSimpleName() + "\n");
        }
    }

    /**
     * The method responsible for printing out a list of Males for use when Breeding Animals
     * @param males An ArrayList of Animals, all of whom are Males
     */
    public  void printMales(ArrayList<Animal> males){
        //Code for Yellow in Consoles - \u001b[33m - Reset code for Colors in Console \u001b[0m
        System.out.println("\u001b[33mPlease choose a male:\u001b[0m");
        int counter = 1;
        for(Animal male : males){
            System.out.println("[" + counter + "] " + male.getColoredInfo());
            counter += 1;
        }
        System.out.println("[" + counter + "] Back to Main Menu");
    }

    /**
     * The method responsible for printing out a list of Females for use when Breeding Animals
     * @param females An ArrayList of Animals, all of whom are females
     */
    public  void printFemales(ArrayList<Animal> females){
        //Code for Yellow in Consoles - \u001b[33m - Reset code for Colors in Console \u001b[0m
        System.out.println("\u001b[33mPlease choose a female:\u001b[0m");
        int counter = 1;
        for(Animal female : females){
            System.out.println("[" + counter + "] " + female.getColoredInfo());
            counter += 1;
        } System.out.println("[" + counter + "] Back to Main Menu");
    }

    /**
     * The method that is responsible for handling Menu printing in regards to an Player selling Animals to another Player
     * @param buyer A player object, the buyer
     * @param seller A player object, the seller
     * @param playersPlaying An arraylist of Players, the collective set of people who are still playing
     * @return An int, the index of the buyer, will be kept for preventing a bug in selling to other players
     */
    public int sellingAnimalToOtherPlayerMenu(Player buyer, Player seller, ArrayList<Player> playersPlaying){
        int counter = 0, buyersIndex = 0;
        for(Player player: playersPlaying){
            if(player.equals(buyer)){
                buyersIndex = counter; //Keep record of who the buyers index, needed for tracking turn order in selling
            }
            counter += 1;
        }
        //Code for Yellow in Consoles - \u001b[33m - Reset code for Colors in Console \u001b[0m
        System.out.println("\u001b[33mWhat animal does " + seller.getName() + " wish to sell to: " + buyer.getName() + " (Funds: "
                + buyer.getAmountOfMoney() + " coins)" + "?\u001b[0m");
        counter = 1;
        for(Animal sellersAnimals: seller.getHealthyAnimals()){
            System.out.println("[" + counter + "] " + sellersAnimals.getColoredInfo() + " Sells for: " + sellersAnimals.getSellsFor() + " coins");
            counter += 1;
        }
        System.out.println("[" + counter + "]" + " Back to main menu");
        return buyersIndex; //return the buyers index
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
        //Code for Yellow in Consoles - \u001b[33m - Reset code for Colors in Console \u001b[0m
        System.out.println("\u001b[33mWhich Player does " + seller.getName() + " wish to sell Animals to? ("
                + seller.getName() +"'s funds: " + seller.getAmountOfMoney() + " coins)\u001b[0m");
        for(Player players : playersPlaying){
            if(!players.equals(seller)){ //If the players gone through are not the seller themselves
                System.out.println("[" + counter + "] " + players.getName() + " - Funds: " + players.getAmountOfMoney() + " coins.");
                counter += 1;
                buyers.add(players); //Add to the buyers list
            }
            else{
                sellersIndex = counter-1; //Keep track of when the sellers turn is
            }
        }
        System.out.println("[" + counter + "] Back to main menu");
        return sellersIndex; //Keep track of the Sellers index, to prevent a bug
    }

    // ======================== UTILITY METHODS ========================
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
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                throw new RuntimeException("\u001b[31mInput must be 1 character long (y or n) (Case insensitive)\u001b[0m");
            }
            else if(input.toLowerCase().equals("y") || input.toLowerCase().equals("n")){
                return 1;
            }
            else{
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                throw new RuntimeException("\u001b[31mInput was : " + input + ", only allowed to be y or n\u001b[0m");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * The method that is responsible for forcing a input that is a int between a certain range.
     *
     * @param lowerBoundary A int, the lower accepted boundary, inclusive
     * @param upperBoundary A int, the upper accepted boundary, inclusive
     * @param input A string, the input that the user wrote, which is to be vetted
     * @param maxIsExitCode A boolean, if the upper boundary index is a exit index or not (it's not always the case)
     * @return An int, the status code, used to figure out if while loop encasing this in calls should be broken or not (sometimes)
     * @throws RuntimeException The exception to throw, in case a input is out of boundary
     */
    public  int safeIntInput(int lowerBoundary, int upperBoundary, String input, boolean maxIsExitCode) throws RuntimeException{
        int check;
        try{
            check = Integer.parseInt(input); //Try to parse the input
            if(check < lowerBoundary || check > upperBoundary){ //If the input is out of boundary in terms of value
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                throw new RuntimeException("\u001b[31mOut of accepted boundary - [LOWER: " + lowerBoundary + " UPPER: " + upperBoundary +
                        " GIVEN: " + check + "] - " + "Please write a number between: " + lowerBoundary +
                        " and " + upperBoundary + ".\u001b[0m"); //Case it was out of bounds
            }
            if(check == upperBoundary && maxIsExitCode){ //If the max is a exit code index, return 2 to signal exiting
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                System.out.println("\u001b[31mUser chose to exit. Returning back to main menu.\u001b[0m");
                return 2;
            }
        }
        catch(Exception e){ //In case input was not in boundary or not a parsable integer
            if(e.getMessage().contains("For input")){  //If the input was text
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                System.out.println("\u001b[31mPlease do not put in letters instead of Numbers.\u001b[0m");
            }
            else{ //otherwise, it was just ouf of boundary
                System.out.println(e.getMessage());
            }
            return -1; //Did not make it through with no issues
        }
        return 1; //Made it through with no issues
    }

    /**
     * A helper method in building a list of Sellers, who are not the buyer themselves
     * Will only get players who have living and not sick Animals
     * @param buyer A player object, the buyer itself
     * @param sellers Array List of Player objects, used to add to and then return when built
     * @param playersPlaying Array List of Player objects that all are plays still playing in the game
     * @return An Array List of Player Objects, that are of all the Possible sellers
     */
    public ArrayList<Player> buildSellersList(Player buyer, ArrayList<Player> sellers, ArrayList<Player> playersPlaying){
        for(Player players : playersPlaying){ //Go through the playersPlaying
            if( !players.equals(buyer) ) {  //If they're not the buyer themselves
                if(players.getHealthyAnimals().size() > 0) { sellers.add(players); } //Add them to the list if they have Healthy animals
            }
        }
        return sellers;
    }
}
