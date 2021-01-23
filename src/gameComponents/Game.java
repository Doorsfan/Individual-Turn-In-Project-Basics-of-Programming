package gameComponents;

import Animals.Animal;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;

/**
 * The class Game is responsible for most of the Game logic that occurs, the Game loop
 * and other pieces of interactions - Such as announcing the winner, presenting the high-scores etc.
 */
public class Game extends utilityFunctions implements Serializable{
    //Initialize our variables that we are going to need
    private int rounds = 0, players = 0, currentRound = 1, currentPlayer = 0;
    private String wantedRoundsInput = "", wantedPlayersInput = "";
    private boolean showedMenu = false;
    private Random random = new Random();
    private ArrayList<Player> playersPlaying = new ArrayList<>();
    private Store ourStore;
    private ArrayList<String> winner = new ArrayList<>();
    //Every class that is to be Serialized, must implement the Serializable interface - however, Scanners don't -their
    //state cannot be Serialized - To circumvent this, i declare my Scanners as Transient - i.e, not included to be Serialized
    transient Scanner userInput = new Scanner(System.in); //user input
    transient Scanner nameScanner = new Scanner(System.in); //name input
    transient Scanner forceSavingPathScanner = new Scanner(System.in); //ForceSavingPathInput
    transient Scanner forceLoadingPathScanner = new Scanner(System.in);
    transient Scanner gameMenuScanner = new Scanner(System.in);

    /**
     * When a new Game is made, we kick things off by asking for amount of players and
     * names - and then Run the actual Game logic loop
     */
    public Game(){
        askForInput(); //Ask for User input
        runGame(); //Boot the game up
    }
    /**
     * The method responsible for handling amount of rounds and players to be played with - delegates repsonsibilities of
     * input to Sub-methods
     */
    public void askForInput(){
        System.out.println("How many rounds would you like to play?");
        while(!(safeIntInput(5, 30, wantedRoundsInput = userInput.next(), false) == 1)){
            //Breaks when the input is within a valid range and is a Number
        }
        rounds = Integer.parseInt(wantedRoundsInput); //Amount of Rounds
        System.out.println("How many players will be playing?");
        while(!(safeIntInput(1, 4, wantedPlayersInput = userInput.next(), false) == 1)){
            //Break when the input is within a valid range and is a Number
        }
        players = Integer.parseInt(wantedPlayersInput); //Amount of players
        for(int i = 0; i < players; i++){ //Create the Players

            System.out.print("Please write the name for player " + (i+1) + ": ");
            String name = nameScanner.nextLine();
            playersPlaying.add(new Player(name, 1000)); //Add them to the list of players playing
        }
    }

    /**
     * The method responsible for breeding of Animals - delegates responsibilities to Sub-methods
     * @param playerBreeding A player object, the player Breeding animals
     */
    public void breedAnimal(Player playerBreeding){
        //Owned animals/Females/males
        ArrayList<Animal> ownedAnimals = playerBreeding.getOwnedAnimals(), males = new ArrayList<>(), females = new ArrayList<>();
        String firstAnimalWanted,secondAnimalWanted; //wanted Animals indexes
        int returnCode = 0; //status code from handling input, used to check for exit

        if(ownedAnimals.size() <= 1){ System.out.println(playerBreeding.getName() + //Need at least 2 animals to breed
                " needs to have at least 2 Animals to breed. Returning to main menu.\n"); return;}
        for(Animal inspectedAnimal : ownedAnimals){ //inspect animals
            if( inspectedAnimal.getGender().equals("Male") ) { males.add(inspectedAnimal); } //Males get added to male List
            else{ females.add(inspectedAnimal); } //Females get added to female list
        }
        if(males.size() == 0 || females.size() == 0){ System.out.println("Found no males/females to breed with, Returning to main menu.\n"); return;}
        else {
            System.out.println("Which two animals do you wish to breed?");

            printMales(males); //Print the menu of relevant males
            while(!((returnCode = (safeIntInput(1, males.size()+1, firstAnimalWanted = userInput.next(),
                    true))) == 1)){ if(returnCode == 2) return; }
            //Force index boundary between 1 and males.size()+1, highest index is exit - returnCode is 2 if the input is the Exit index

            printFemales(females); //Print the menu of relevant females
            while(!((returnCode = (safeIntInput(1, females.size()+1, secondAnimalWanted = userInput.next(),
                    true))) == 1)){ if(returnCode == 2) return; }
            //Force index boundary between 1 and females.size()+1,
            // highest index is exit highest index is exit - returnCode is 2 if the input is the Exit index

            Animal theMale = males.get(Integer.valueOf(firstAnimalWanted) - 1); //The wanted Male
            Animal theFemale = females.get(Integer.valueOf(secondAnimalWanted) - 1); //The wanted female

            if(theMale.getClassName().equals(theFemale.getClassName())){ //If they're both of the same race
                int madeOffspring = random.ints(1,3).findFirst().getAsInt(); //NR of Offspring
                if(madeOffspring > 1)
                    { System.out.println(theFemale.getInfo() + " and " + theMale.getInfo() + " did not manage to make any babies.."); }
                else{
                    int amountOfBabies = random.ints(theFemale.getMinimumOffspring(),theFemale.getMaximumOffspring()).findFirst().getAsInt();
                    System.out.println(theMale.getInfo() + " " + theFemale.getInfo() + " made " + amountOfBabies + " babies.");
                    createBabies(amountOfBabies, females, playerBreeding, Integer.valueOf(secondAnimalWanted)); } } //Create the babies
            else{ System.out.println("Cannot breed two different Animals of different breeds."); } }
    }

    /**
     * The method responsible for buying Animals from other players - Delegates most responsibilities
     * to sub-methods
     * @param buyer A player object, the Buyer in question
     * @return A int, the status code to return
     */
    public int buyFromOtherPlayer(Player buyer){
        int counter = 1, returnCode = 0;
        ArrayList<Player> sellers = new ArrayList<Player>(); //List of sellers
        String targetPlayerIndex, animalToBuyIndex; //Indexes the player wishes to target (player and Animal)
        boolean doneBuying = false;
        while(!doneBuying) {
            if( playersPlaying.size() < 2 ){
                System.out.println("No other players in game. Returning to main menu.");
                return -1;
            } //Too few players
            sellers = buildSellersList(buyer, sellers, playersPlaying);
            if ( sellers.size() == 0 ) return -1; //No other player with Animals to buy from is available
            else{ printSalesMenu(buyer, sellers); } //Otherwise, just print the sellers

            while(!((returnCode = (safeIntInput(1, sellers.size()+1, targetPlayerIndex = userInput.next(),
                    true))) == 1)){ if(returnCode == 2) return 1; }
            //Which player the Player wants to buy from - forces a valid index, returnCode is 2 if it's the exit index
            Player seller = sellers.get(Integer.valueOf(targetPlayerIndex)-1); //Save the seller

            printBuyFromOtherPlayerMenu(buyer, seller); //print the Menu

            while(!((returnCode = (safeIntInput(1, seller.getOwnedAnimals().size()+1, animalToBuyIndex = userInput.next(),
                    true))) == 1)){ if(returnCode == 2) return 1; } //Which animal the player wants to Buy
            Animal animalBeingBought = seller.getOwnedAnimals().get(Integer.valueOf(animalToBuyIndex)-1); //Animal being Bought
            if(animalBeingBought.getSellsFor() > buyer.getAmountOfMoney()) {
                printFailedTransaction(buyer, animalBeingBought); //Print the failed Transaction section
                return -2;
            } //Not enough money
            System.out.println(buyer.getName() + " bought " + animalBeingBought.getInfo() + " for " + animalBeingBought.getSellsFor()
                    + " coins from " + seller.getName() + ".");

            buyer.pay(animalBeingBought.getSellsFor()); //buyer pays
            seller.getPaid(animalBeingBought.getSellsFor()); //Seller gets paid
            buyer.addToOwnedAnimals(animalBeingBought); //Buyer gets Animal
            seller.getOwnedAnimals().remove(animalBeingBought); //Seller removes ANimal
            sellers = new ArrayList<Player>(); //Sellers list is reset
        }
        return 3;
    }
    /**
     * Method that handles Selling to other players - delegates most responsibilities to sub-methods
     * @return An int, the status code of what happened
     */
    public int sellToOtherPlayer(Player seller){
        int counter = 1; //Shopcounter to keep track of indexes
        int buyersIndex = 0, sellersIndex = 0, returnCode = 0; //Indexes and the returnCode from selling
        ArrayList<Player> buyers = new ArrayList<Player>(); //List of Buyers
        String targetPlayerIndex, animalToSellIndex; //Inputs from Users to convert to Integers
        boolean doneSelling = false;
        while(!doneSelling){
            boolean result = playersPlaying.size() < 2 || seller.getOwnedAnimals().size() == 0; //1 player or no Animals
            if(result){ System.out.println("Too few players left or no animals to sell. Returning to main menu."); return -1; }


            sellersIndex = listOfBuyersInSellToOtherPlayer(seller, buyers, playersPlaying); //Index of the Seller
            //Index of the player to sell to, returnCode is 2 if the Index is the Exit index
            while(!((returnCode = (safeIntInput(1, buyers.size()+1, targetPlayerIndex = userInput.next(),
                    true))) == 1)){ if(returnCode == 2) return 1; }
            Player buyer = buyers.get(Integer.valueOf(targetPlayerIndex)-1); //Get the buyer

            buyersIndex = sellingAnimalToOtherPlayerMenu(buyer, seller, playersPlaying); //index of the Buyer
            //Index of the Animal to sell, returnCode is 2 if the index is the Exit index
            while(!((returnCode = (safeIntInput(1, seller.getOwnedAnimals().size()+1, animalToSellIndex = userInput.next(),
                    true))) == 1)){ if(returnCode == 2) return 1; }
            Animal animalBeingSold = seller.getOwnedAnimals().get(Integer.valueOf(animalToSellIndex)-1);

            if(animalBeingSold.getSellsFor() > buyer.getAmountOfMoney()){ printCantAffordAnimal(buyer, animalBeingSold); return -1; }

            System.out.println(buyer.getName() + " bought " + animalBeingSold.getInfo() + " for "
                    + animalBeingSold.getSellsFor() + " coins from " + seller.getName() + ".");

            buyer.pay(animalBeingSold.getSellsFor()); //buyer pays
            seller.getPaid(animalBeingSold.getSellsFor()); //Seller gets paid

            //An animal that is sold "downwards" in the turn order, should age and decay to account for aging/decaying during a round
            //as Decay and Aging occurs at the end of a Round, however - this would be skipped if sold downwards
            if(sellersIndex > buyersIndex){ //Age and Decay the Animal if it would have circumvented this
                animalBeingSold.age();
                if( animalBeingSold.isAlive() ) { animalBeingSold.decay(currentRound); }
            }
            buyer.addToOwnedAnimals(animalBeingSold); //Buyer gets the Animal
            seller.getOwnedAnimals().remove(animalBeingSold); //Seller removes his Animal

            //Reset the variables at the end of the While loop, so they're clean for the next run
            counter = 1;
            buyersIndex = 0;
            sellersIndex = 0;
            buyers = new ArrayList<Player>();
            targetPlayerIndex = "";
            animalToSellIndex = "";
        }
        return 1;
    }

    /**
     * The method responsible for handling feeding of Animals - delegates some responsibilities through
     * sub-methods - such as Menu rendering, Input handling and Otherwise
     *
     * @param playerFeeding A player object, the player feeding the Animal
     * @return An int - Just the status code of what heppened when feeding the Animal
     */
    public int feedAnimal(Player playerFeeding){
        int counter = 1, returnCode = 0; //Shop index counter and the ReturnCode
        boolean finishedFeeding = false;
        String wantedAnimalToFeed, wantedFood;

        if(playerFeeding.getOwnedAnimals().size() == 0){ //Has no Animals to feed, thrown back to main menu
            System.out.println(playerFeeding.getName() + " has no Animals to feed. Returning to main menu.\n");
            return -2; }

        while(!finishedFeeding){
            boolean foundFood = false;
            if(playerFeeding.getOwnedFood().size() == 0){ //Ran out of Food to feed with
                System.out.println(playerFeeding.getName() + " has no food left to feed with. Returning to main menu.\n");
                return -2; }
            printAnimalsInFeedMenu(playerFeeding); //Print the Animals feeding menu
            //The index of the Animal to feed - returnCode is 2 if the Index is the Exit index
            // [1]
            while(!((returnCode = (safeIntInput(1, playerFeeding.getOwnedAnimals().size()+1,
                    wantedAnimalToFeed = userInput.next(), true))) == 1)){ if(returnCode == 2) return 1; }
            Animal toBeFed = playerFeeding.getOwnedAnimals().get((Integer.valueOf(wantedAnimalToFeed)-1)); //The animal being fed

            if(!checkIfItEatsFood(toBeFed, playerFeeding)){ //If the player does not own any food the Animal would want, returns to main menu
                System.out.println(playerFeeding.getName() + " does not have any food that a " + toBeFed.getClassName() + " would like.");
                return -1; }
            ArrayList<Food> filteredFood = printFoodOptions(toBeFed, playerFeeding); //Relevant food items for the Specified Animal
            //Handles a filtered list based on what the Animal wants - returnCode is 2 if the index is the Exit index
            while(!((returnCode = (safeIntInput(1, filteredFood.size()+1, wantedFood = userInput.next(),
                    true))) == 1)){ if(returnCode == 2) return 1; } //Which Animal to feed
            Food foodToFeedWith = filteredFood.get(Integer.valueOf(wantedFood)-1); //The chosen food from filtered food items
            checkingFoodFound(toBeFed, foodToFeedWith); //Checks amount of food left and attempts to feed the Animal - doesnt feed if not enough left
            clearOutFood(playerFeeding); //Clears out food if there is 0 grams left of it in the Inventory
            counter = 1;
        }
        return 1; //Successfully fed Animals
    }

    /**
     * Returns the amount of Players
     * @return An int, the amount of players
     */
    public int getPlayers(){
        return this.players;
    }

    /**
     * Sets the amount of Players
     * @param players An int, the amount of players to be set to
     */
    public void setPlayers(int players){
        this.players = players;
    }

    /**
     * Get current player numeral
     * @return An int, that depicts who the current player is based on Index
     */
    public int getCurrentPlayer(){
        return this.currentPlayer;
    }

    /**
     * Setter for the Store object, utilized in Loading the game
     * @param ourStore A store object, our store
     */
    public void setOurStore(Store ourStore){ this.ourStore = ourStore; }

    /**
     * Getter for the Store object, utilized in Loading the game
     * @return A store object, our store
     */
    public Store getOurStore(){ return this.ourStore; }

    /**
     * Setter for Setting the current Player index
     * @param currentPlayer An int, the index that one wishes to set the current Player to be
     */
    public void setCurrentPlayer(int currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    /**
     * Gets the Numeral of the current Round
     * @return An int, the current round of the Game
     */
    public int getCurrentRound(){
        return this.currentRound;
    }

    /**
     * Sets the current round
     * @param currentRound An int, sets the current round
     */
    public void setCurrentRound(int currentRound){
        this.currentRound = currentRound;
    }

    /**
     * Getter of rounds, rounds being the total amount of rounds to play
     *
     * @return An int, returns the amount of Rounds to be played in total
     */
    public int getRounds(){
        return this.rounds;
    }

    /**
     * Setter of rounds, rounds being the total amount of rounds to play
     * @param rounds An int, sets the total amount of rounds to this value
     */
    public void setRounds(int rounds){
        this.rounds = rounds;
    }

    /**
     * Get players playing array list
     * @return An arraylist of Player objects, all of whom are the players still in the game
     */
    public ArrayList<Player> getPlayersPlaying(){
        return this.playersPlaying;
    }

    /**
     * Setter for players playing.
     * @param playersPlaying An arrayList of players, which is the one which the user wishes to set the new current players
     *                       to be
     */
    public void setPlayersPlaying(ArrayList<Player> playersPlaying){
        this.playersPlaying = playersPlaying;
    }

    /**
     * Getter for showedMenu - primarily used in not printing out redundant information/repeating information
     * @return A boolean, whether the Menu was shown already or not
     */
    public boolean getShowedMenu(){
        return this.showedMenu;
    }

    /**
     * Setter for Showed menu Boolean
     * @param showedMenu A boolean, whether the Menu has been shown or not yet
     */
    public void setShowedMenu(boolean showedMenu){
        this.showedMenu = showedMenu;
    }

    /**
     * The method responsible for actually saving the Game - After the Save Filepath has been
     * vetted and confirmed - the actual saving process is executed
     * @param filePath A string input that is a vetted/sanitized form of Input of the User,
     *                 can for instance take the form of:
     *
     *                      D:\\myGames\\Savestates\\MyJavaGameSave
     *                      Will Create the Dirs myGames and Savestates if needed,
     *                      and then create the save game file named MyJavaGameSave
     *
     * @throws FileNotFoundException Since we are working with FileOutputStreams, we have
     *                  to account for the fact that it might not be able to find the filePath - so the
     *                  method demands that we declare that the Method can throw this Exception
     */
    public void saveGame(String filePath) throws FileNotFoundException{
        try{
            FileOutputStream fileOut = new FileOutputStream(filePath); //Opens the FileOutputStream to the File path destination
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut); //Opens the ObjectOutputStream to the fileOutputStream
            objectOut.writeObject(this); //Write the game Object to the Save
            objectOut.close(); //Close the stream
            System.out.println("Successfully saved game to: " + filePath);
        }
        catch(Exception e){
            System.out.println("Attempted to save with the filepath of: " + filePath + " but failed. Make sure the Filepath ends " +
                    "in a Filename.");
            System.out.println(e);
        }
    }

    /**
     * The method responsible for loading the relevant game save, based on the filePath
     * Uses an ObjectInputStream to read a serialized File and sets current values to that
     * of the save state.
     *
     * Do note: Usage of objectInputStreams must conclude with the Stream being closed after
     * usage is completed.
     *
     * @param filePath A string that is a User written Filepath input, which has been forcefully
     *                 checked to vet that it's possible to load a game save from the wished destination
     * @throws FileNotFoundException The method must be declared as being able ot throw a FileNotFoundException,
     *                 as there is no guarantee that it can open a FileInputStream if the Filepath is not valid.
     */
    public void loadGame(String filePath) throws FileNotFoundException{
        boolean holdLoop = true;
        while(holdLoop){
            FileInputStream fileIn = new FileInputStream(filePath); //Open the FileInputStream on the given Filepath
            try (ObjectInputStream objectIn = new ObjectInputStream(fileIn)) { //Open the ObjectInputStream on the filePath handle
                Object obj = objectIn.readObject(); //Read the object in from the Save file
                Game loadedGame = (Game) obj; //Convert it to a game object and save it
                this.setCurrentPlayer(loadedGame.getCurrentPlayer()); //Retrieve the attributes and set them to the current Game
                this.setCurrentRound(loadedGame.getCurrentRound());
                this.setPlayers(loadedGame.getPlayers());
                this.setRounds(loadedGame.getRounds());
                this.setPlayersPlaying(loadedGame.getPlayersPlaying());
                this.setShowedMenu(loadedGame.getShowedMenu());
                this.setOurStore(loadedGame.getOurStore());
                System.out.println("Successfully loaded save game from: " + filePath);
                if(!this.getShowedMenu()){ //When loading, re-create Menus printed as well - including Deaths from saved Turn
                    removeAnimals(playersPlaying, currentPlayer);
                    printAnimals(true, currentRound, currentPlayer, playersPlaying);
                }
                objectIn.close(); //Close the stream when Done
                break;
            } catch (Exception ex) {
                break; //Stream closed
            }
        }
    }

    /**
     * Method that handles user input in Attempts to load the game
     */
    public void forceLoadingPath(){
        String returnCode, filePathToSaveOrLoad = ""; //Returncode and Filepath
        System.out.println("Please write the System path you'd like to load a game from (Exit to abort - Case insensitive): ");
        try{
            //Will keep asking to validate a Filepath until it is resolved as Exit or Succeeded
            while (!((returnCode = (forceValidLoadingPath(filePathToSaveOrLoad = forceLoadingPathScanner.nextLine()))).equals("exit"))) {
                //Exit, succeeded or failed
                boolean invalidPath = returnCode.equals("failed"); //is Failed if the Loading failed
                if (!invalidPath) {
                    loadGame(filePathToSaveOrLoad); //Passed with a valid Path
                    break;
                }
                System.out.println("Attempted to load game from filePath of: " + filePathToSaveOrLoad + " but failed, please try again.");
            }
        }
        catch(Exception e){
            System.out.println("Had an exception, retrying.");
        }
    }

    /**
     * Method that handles user input in attempts to save the game
     */
    public void forceSavingPath(){
        String returnCode, filePathToSaveOrLoad = ""; //The filePath and Returncode
        System.out.println("Please write the System path you'd like to save your game to (Exit to abort - Case insensitive): ");
        try{
            //Keeps asking for a Save path until it is confirmed as being Exit or a Valid save path
            while(!((returnCode = (forceValidSavingPath(filePathToSaveOrLoad = forceSavingPathScanner.nextLine()))).equals("exit"))){
                boolean invalidPath = returnCode.equals("failed making dir"); //returnCode is failed making dir if making Directories failed
                if(!invalidPath){ //If the path is valid
                    saveGame(filePathToSaveOrLoad); //Save the file to the Filepath
                    break;
                }
            }
        }
        catch(Exception e){
            System.out.println("Had an exception, retrying.");
        }
    }

    /**
     * Method that handles aging,decaying and disease at the end of the turn
     */
    public void ageDecayAndDiseaseAtEndofTurn(){
        for (Animal eachPlayerAnimal : playersPlaying.get(currentPlayer).getOwnedAnimals()) {
            if (eachPlayerAnimal.isAlive() && !eachPlayerAnimal.hasDecayedThisRound()) { //If the Animal is alive and hasn't decayed, decay
                eachPlayerAnimal.decay(currentRound);
            }
            if (eachPlayerAnimal.isAlive() && !eachPlayerAnimal.hasDecayedThisRound()) { //If the Animal is alive and hasn't decayed, age
                eachPlayerAnimal.age();
            }
            if (eachPlayerAnimal.isAlive() && !eachPlayerAnimal.hasDecayedThisRound()) { //If the Animal is alive and hasn't decayed, roll for Disease
                eachPlayerAnimal.chanceForDisease(playersPlaying.get(currentPlayer));
            }
            eachPlayerAnimal.setDecayedThisRound(true); //The animal has had it's decay/age/disease for the Turn
        }
    }

    /**
     * The method that handles game menu choices
     * @param gameMenuInput The player choice in menu options
     * @param ourStore The store that the player can buy from
     */
    public void makePlayerChoice(String gameMenuInput, Store ourStore){
        switch (gameMenuInput) {
            case "1":
                ourStore.buyAnimal(playersPlaying.get(currentPlayer)); //Buy an Animal from the Store
                playersPlaying.get(currentPlayer).setTurnIsOver(true); //Turn is over
                break;
            case "2":
                ourStore.sellAnimal(playersPlaying.get(currentPlayer)); //Sell an Animal to the Store
                playersPlaying.get(currentPlayer).setTurnIsOver(true); //Turn is over
                break;
            case "3":
                feedAnimal(playersPlaying.get(currentPlayer)); //Feed their animals
                playersPlaying.get(currentPlayer).setTurnIsOver(true); //Turn is over
                break;
            case "4":
                breedAnimal(playersPlaying.get(currentPlayer)); //Breed a set of Animals
                playersPlaying.get(currentPlayer).setTurnIsOver(true); //Turn is over
                break;
            case "5":
                ourStore.buyFood(playersPlaying.get(currentPlayer)); //Buy some food from the Store
                playersPlaying.get(currentPlayer).setTurnIsOver(true); //Turn is over
                break;
            case "6":
                sellToOtherPlayer(playersPlaying.get(currentPlayer)); //Sell Animals to other players
                playersPlaying.get(currentPlayer).setTurnIsOver(true); //Turn is over
                break;
            case "7":
                buyFromOtherPlayer(playersPlaying.get(currentPlayer)); //Buy Animals from other players
                playersPlaying.get(currentPlayer).setTurnIsOver(true); //Turn is over
                break;
            case "8":
                forceSavingPath(); //Start the process to validate a Filepath for Saving - to then Save the Game
                break;
            case "9":
                forceLoadingPath(); //Start the process to validate a Filepath for Loading - to then Load the Game
                break;
            default:
                System.out.println("That input was not one of the valid options. Please try again.");
        }
    }

    /**
     * The method that handles death announcements and Death list of Players
     */
    public void handleDeathsOfAnimals(){
        for (int i = 0; i < playersPlaying.get(currentPlayer).getOwnedAnimals().size(); i++) {
            if (!playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).isAlive()) {
                //Died of starvation or Old Age or Disease
                playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).setPerishedAtRound(currentRound);
                //Add a death Announcement
                playersPlaying.get(currentPlayer).addDeathAnnouncement(currentRound,
                        playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getName()
                                + " the " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getClassName()
                                + " (" + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getGender() + ")"
                                + " perished at the game round of "
                                + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getPerishedAtRound()
                                + ", died of " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getCauseOfDeath()
                                + ", became : " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getAge()
                                + " years old. Rest in peace, " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getName());
                //Add to the players death List - used in Loading games to archive old deaths and re-construct death messages
                playersPlaying.get(currentPlayer).addToPlayerDeathList(playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getName()
                        + " the " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getClassName()
                        + " (" + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getGender() + ")"
                        + " perished at the game round of "
                        + currentRound
                        + ", died of " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getCauseOfDeath()
                        + ", became : " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getAge()
                        + " years old. Rest in peace, " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getName());
                //Add Animal to should be removed list - utilized when Animal dies, it is then purged
                playersPlaying.get(currentPlayer).getShouldBeRemoved().add(playersPlaying.get(currentPlayer).getOwnedAnimals().get(i));
            }
        }
    }

    /**
     * Checks if a Player fulfills the criteria of being eliminated
     * @return A boolean, true if they were eliminated - False if not
     */
    public boolean checkIfPlayerIsEliminated(){
        if (playersPlaying.get(currentPlayer).getAmountOfMoney() < 10 &&
                playersPlaying.get(currentPlayer).getOwnedAnimals().size() == 0) { //If the player cannot afford anything and has no Animals
            System.out.println(playersPlaying.get(currentPlayer).getName() + " was eliminated at Round: " + currentRound + " due to not having" +
                    " enough money to buy anything and no animals left.");
            playersPlaying.remove(currentPlayer); //Remove the player
            if(playersPlaying.size() > 1){ currentPlayer -= 1; } //Compensate who the current player is, upon removal of a Player
            return true; //Player was Eliminated
        }
        return false; //player was not eliminated
    }

    /**
     * The method responsible for purging Animals that should be removed
     */
    public void purgeDeadAnimals(){
        if (playersPlaying.size() > 0){ //To avoid a out of bounds bug when there are no players left
            if (playersPlaying.get(currentPlayer).getShouldBeRemoved().size() > 0) { //If there are any Animals to purge
                for (int i = playersPlaying.get(currentPlayer).getShouldBeRemoved().size() - 1; i > -1; i--) {
                    Animal toRemove = playersPlaying.get(currentPlayer).getShouldBeRemoved().get(i); //get the Animal to remove
                    playersPlaying.get(currentPlayer).getOwnedAnimals().remove(toRemove); //Remove it from ownership
                    playersPlaying.get(currentPlayer).getShouldBeRemoved().remove(i); //Remove it from the Purging list
                }
            }
        }
    }

    /**
     * Handles advancements in rounds as the game progresses
     * @param playerGotEliminated A boolean, if a player Got eliminated this turn or not
     */
    public void advanceRound(boolean playerGotEliminated){
        if (currentPlayer == playersPlaying.size() - 1 && playerGotEliminated){ //Compensate to balance out eliminated player
            rounds += 1;
            currentRound -= 1;
        }
        if (currentPlayer == playersPlaying.size() - 1) { //Round is advanced to the next one
            rounds -= 1;
            currentRound += 1;
            for(Player players: playersPlaying){
                for(Animal eachPlayerAnimal : players.getOwnedAnimals()){
                    eachPlayerAnimal.setDecayedThisRound(false); //Reset the state of Animals having decayed, as its reset between Rounds
                }
                players.purgeSavedDeathList(currentRound-2); //Purge old death archives, to keep them up to date - relevant for Loading
            }
        }
        if (currentPlayer < playersPlaying.size() - 1) { currentPlayer += 1; }
        else { currentPlayer = 0; } //If it's the last player in the turn order, reset it to the first player again
    }

    public ArrayList<Integer> sellOffAnimalsAtEndOfGame(){
        ArrayList<Integer> moneyOfPlayers = new ArrayList<>(); //The money of the Players

        for(Player player : playersPlaying){ //Sell off the Animal of each player at the end of the game
            for(int i = player.getOwnedAnimals().size()-1; i > -1; i--){
                System.out.println(player.getName() + " sold off " + player.getOwnedAnimals().get(i).getInfo()
                        + " for " + player.getOwnedAnimals().get(i).getSellsFor() + " coins.");
                player.getPaid(player.getOwnedAnimals().get(i).getSellsFor()); //Sells the Animal
                player.getOwnedAnimals().remove(i); //removes it
            }
            moneyOfPlayers.add(player.getAmountOfMoney()); //Add to a list of Money, how much money each player had
        }
        Collections.sort(moneyOfPlayers);
        return moneyOfPlayers;
    }
    /**
     * A method that sells off all the animals at the end of the Game and builds the High-score
     * @return An Arraylist of Strings, which is the players highscore
     */
    public ArrayList<String> buildHighScore(){
        ArrayList<Integer> moneyOfPlayers; //The money of the Players
        ArrayList<String> namesOfPlayers = new ArrayList<>(), highScore = new ArrayList<>(); //names and the Highscore

        for(Player player : playersPlaying){ //
            namesOfPlayers.add(player.getName()); //Add to a list, the names of the remaining players
        }
        moneyOfPlayers = sellOffAnimalsAtEndOfGame();
        Collections.sort(moneyOfPlayers); //Sort the money list, lowest to highest
        int spot = 1;
        for(int i = moneyOfPlayers.size()-1; i > -1; i--){ //For each amount of Money to be looked at
            for(Player player: playersPlaying){ //For each player, look if they correspond to the amount of money
                if(player.getAmountOfMoney() == moneyOfPlayers.get(i) && !player.getAddedToHighScore()){ //If it's a match, add them to the highscore
                    highScore.add("[" + spot + "] - " + player.getName() + " : " + player.getAmountOfMoney() + " coins.");
                    if(spot == 1){ //The last element in the sorted list is the winner, so on the first looping through, thats the winner
                        //Add the winner to the winners list
                        winner.add("The winner is: " + player.getName() + " with a whopping amount of " + player.getAmountOfMoney() + " coins!");
                    }
                    player.setAddedToHighScore(true); //player was added to the highscore
                }
            }
            spot += 1; //keep running tally of how many elements we've gone through
        }
        return highScore; //return the high score
    }
    /**
     * A method that hosts and interacts with the main set of Game mechanics in play,
     * such as Deaths, Menus, player choices etc, through Sub-methods
     */
    public void runGame() {
        ourStore = new Store(); //Create our store
        System.out.println("Welcome to the Raising your Animal Game.");
        boolean playerGotEliminated = false;

        while (rounds > 0) {
            if (!showedMenu) { //If the menu has not been shown
                playerGotEliminated = false;
                removeAnimals(playersPlaying, currentPlayer);
                printAnimals(false, currentRound, currentPlayer, playersPlaying); //Showcase the info menu
            }

            System.out.println("\n" + playersPlaying.get(currentPlayer).getName()
                    + "'s Funds: " + playersPlaying.get(currentPlayer).getAmountOfMoney() + " coins "
                    + "\nChoose: [1] Buy an Animal from Store, [2] Sell an Animal to Store, [3] Feed your animals, " +
                    "[4] Breed your animals, [5] Buy Food,\n " + "[6] Sell Animal to Other Player" +
                    ", [7] Buy Animal from Other Player [8] Save game and Exit [9] Load game");
            String gameMenuInput = gameMenuScanner.next(); //User choise in the menu
            makePlayerChoice(gameMenuInput, ourStore); //handle the player choise input
            showedMenu = true; //menu was shown and input was processed
            if (playersPlaying.get(currentPlayer).getTurnIsOver()) { //If the player turn is over
                playersPlaying.get(currentPlayer).setTurnIsOver(false); //Reset variables for next round
                showedMenu = false; //Reset variables
                ageDecayAndDiseaseAtEndofTurn(); //Age, Decay and roll for Disease on Animals
                handleDeathsOfAnimals(); //Handle death announcements of Animals
                playerGotEliminated = checkIfPlayerIsEliminated(); //See if a player was eliminated
                if (playersPlaying.size() == 0) { //Check if there are any players left
                    System.out.println("Game is over. No players remaining.");
                    break;
                }
                advanceRound(playerGotEliminated); //Advance a round, accounting for if a player got eliminated or not
                purgeDeadAnimals(); //Purge the dead animals
            }
        }
        ArrayList<String> highScore = buildHighScore(); //Build the highscore list

        System.out.println("At the end of the game, here are the results: ");
        for(String highScoreSpots : highScore){ System.out.println(highScoreSpots); }
        if(winner.size() > 0) { System.out.println(winner.get(0)); } //There was a winner
        else{ System.out.println("There were no players who made it to the end of the game."); } //No player made it to the end
    }
}