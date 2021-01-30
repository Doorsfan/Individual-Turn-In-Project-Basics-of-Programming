package gameComponents;

import Animals.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * The class Game is responsible for most of the Game logic that occurs, the Game loop
 * and other pieces of interactions - Such as announcing the winner, presenting the high-scores etc.
 */
public class Game extends utilityFunctions implements Serializable{
    //Every class that is to be Serialized, must implement the Serializable interface - however, Scanners don't -their
    //state cannot be Serialized - To circumvent this, i declare my Scanners as Transient - i.e, not included to be Serialized
    transient Scanner nameScanner = new Scanner(System.in), gameMenuScanner = new Scanner(System.in); //user input
    transient Scanner loadingMenuScanner = new Scanner(System.in);
    //Initialize our variables that we are going to need
    private int roundsRemaining = 0, players = 0, currentRound = 1, currentPlayer = 0;
    private String wantedRoundsInput = "", wantedPlayersInput = "";
    private boolean showedMenu = false;
    private ArrayList<Player> playersPlaying = new ArrayList<>();
    private Store ourStore = null;
    private ArrayList<String> winner = new ArrayList<>();
    private transient Scanner userInputScanner = new Scanner(System.in), breedInput = new Scanner(System.in);
    private transient Scanner loadGameScanner = new Scanner(System.in), saveGameScanner = new Scanner(System.in);
    private transient DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private LocalDateTime timeOfSaving;

    // ============== CONSTRUCTOR =====================
    /**
     * When a new Game is made, we kick things off by asking for amount of players and
     * names - and then Run the actual Game logic loop
     */
    public Game(){
        int result = showMainMenu(); //Show the Main Menu for the Player, and account for what happens in the main menu
        if(result == 1){ runGame(); }//Boot the game up if we loaded a game successfully
    }

    // ============ GETTERS ====================
    /**
     * Getter for showedMenu - primarily used in not printing out redundant information/repeating information
     * @return A boolean, whether the Menu was shown already or not
     */
    public boolean getShowedMenu(){
        return this.showedMenu;
    }

    /**
     * Returns the amount of Players
     * @return An int, the amount of players
     */
    public int getPlayers(){
        return this.players;
    }

    /**
     * Get current player numeral
     * @return An int, that depicts who the current player is based on Index
     */
    public int getCurrentPlayer(){
        return this.currentPlayer;
    }

    /**
     * Getter for the Store object, utilized in Loading the game
     * @return A store object, our store
     */
    public Store getOurStore(){ return this.ourStore; }

    /**
     * Gets the Numeral of the current Round
     * @return An int, the current round of the Game
     */
    public int getCurrentRound(){
        return this.currentRound;
    }

    /**
     * Getter of roundsRemaining, roundsRemaining being the amount of Rounds remaining
     *
     * @return An int, returns the amount of Rounds remaining
     */
    public int getRoundsRemaining(){
        return this.roundsRemaining;
    }

    /**
     * Get players playing array list
     * @return An arraylist of Player objects, all of whom are the players still in the game
     */
    public ArrayList<Player> getPlayersPlaying(){
        return playersPlaying;
    }

    /**
     * Gets the time of when this game was Saved
     * @return A LocalDateTime object, used for getting the time of saving
     */
    public LocalDateTime getTimeOfSaving(){
        return this.timeOfSaving;
    }

    // ============= SETTERS =====================

    /**
     * Setter for Showed menu Boolean
     * @param showedMenu A boolean, whether the Menu has been shown or not yet
     */
    public void setShowedMenu(boolean showedMenu){
        this.showedMenu = showedMenu;
    }

    /**
     * Sets the amount of Players
     * @param players An int, the amount of players to be set to
     */
    public void setPlayers(int players){
        this.players = players;
    }

    /**
     * Setter for Setting the current Player index
     * @param currentPlayer An int, the index that one wishes to set the current Player to be
     */
    public void setCurrentPlayer(int currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    /**
     * Setter for the Store object, utilized in Loading the game
     * @param ourStore A store object, our store
     */
    public void setOurStore(Store ourStore){ this.ourStore = ourStore; }

    /**
     * Sets the current round
     * @param currentRound An int, sets the current round
     */
    public void setCurrentRound(int currentRound){
        this.currentRound = currentRound;
    }

    /**
     * Setter of rounds, rounds being the total amount of rounds to play
     * @param roundsRemaining An int, sets the total amount of rounds to this value
     */
    public void setRoundsRemaining(int roundsRemaining){
        this.roundsRemaining = roundsRemaining;
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
     * Sets the time of Saving
     * @param timeOfSaving A LocalDateTime object, used for setting the time of saving
     */
    public void setTimeOfSaving(LocalDateTime timeOfSaving){
        this.timeOfSaving = timeOfSaving;
    }

    // ============= SHOW THE MAIN MENU ==================
    /**
     * The method that is responsible for handling input choices in the main menu - can start new game, load game
     * , show the Rule set or exit the game.
     * @return An int based on success:
     *          -1: Failed - Should realistically never return this
     *           1: Successfully loaded a game
     */
    public int showMainMenu(){
        if(loadingMenuScanner == null){ this.loadingMenuScanner = new Scanner(System.in); }
        String userInput = "";
        System.out.println("==== Welcome to the Pet Game! ====\n[1] New game\n[2] Load game\n[3] Game Rules\n[4] Exit");
        while(!((safeIntInput(1, 4, userInput = loadingMenuScanner.next(),false)) == 1)){}
        if(userInput.equals("1")){ askForInput(); runGame(); } //If the user chose to create a new game, ask for input and run the  game
        if(userInput.equals("2")){ loadGame(this); return 1; } //Goes to Load game Menu
        if(userInput.equals("3")){ printRules(); showMainMenu(); } //Print the Rule set of the game
        if (userInput.equals("4")){
            //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
            System.out.print("\u001b[31mUser chose to exit game. Shutting down.\u001b[0m");
            System.exit(1);
        }
        return -1;
    }

    // ============ IN CASE THE PLAYER CHOSE TO PRINT THE RULE SET ======================
    /**
     * A method that is responsible for printing the Rules of the Game
     */
    public void printRules(){
        System.out.println("""
                \t\t\t\t====Welcome to the Pet Game!====
                \t\t\t----Here are the rules for the Pet Game----
                \t
                \t1: The game is played between 5-30 Rounds, with 1-4 Players. (Chosen at the start)
                \t2: Each player starts with 1000 coins and no Animals.
                \t3: Every round a Player gets to do ONE of the following things:

                \t\t-> Buy an Animal from the Store (if they have enough money to do so)
                \t\t-> Sell an Animal to the Store (If they have any)
                \t\t-> Feed their Animals (if they have any and they have food that the Animal likes)
                \t\t-> Try to breed one male and one female of their animals (if compatible)
                \t\t-> Buy some food from the Store (if they can afford it - minimum 100g of a food item)
                \t\t-> Sell one of their Animals to another player who is still in the game (if they can afford it)(Not sick ones)
                \t\t-> Buy an Animal from another player (Not sick ones)(If they can afford it and there are others to buy from)
                \t\t-> Skip their turn
                \t\t-> Save the Game and Exit to Main Menu (Asks Y/N in case of Overwriting)

                \tEvery round, all animals decay (losing 10-30% of max Health every round), age (if they get too old, they die)
                \tand have a 20% chance of getting sick. (20% per Animal per Round) - The sell value of an Animal declines with age
                \tand reduced Health and a Sick animal cannot be sold, fed or spared from death - other than paying the Vet bill
                \t,which gives them a 50% chance of being cured.

                \tWhen a player no longer has enough money to buy anything from the store and no Animals - they are eliminated.

                \tAt the end of the game - all remaining players sell off their living and healthy animals for their sell value,
                \tand gain said money - Afterwards, rankings are presented - There are no shared spots for equal amount of Coins,
                \tit simply takes the first player it finds as the winner, in that case. If no players make it to the end, there is
                \t no winner.

                \tWhen an animal dies - it's death is announced to the player at the next Round, along with cause of death  - At the
                \t start of every round a player is given information about remaining players, their own Animals, Decay since last turn\s
                \tand their funds.

                \tIn the Main Menu, the player can choose to start a new game, load a previous game (if any save is available) or
                \tto see the Game Rules printed out.

                """);
    }

    // =========== IN CASE OF A NEW GAME BEING STARTED ======================
    /**
     * The method responsible for handling amount of rounds and players to be played with - delegates tasks of
     * input to Sub-methods resets values of CurrentRound, CurrentPlayer and showedMenu - Needed for when
     * creating a new game, if player already loaded a game before starting a new one.
     */
    public void askForInput(){
        if(nameScanner == null){ nameScanner = new Scanner(System.in); } //To avoid a NullPointer exception
        if(userInputScanner == null){ userInputScanner = new Scanner(System.in); } //To avoid a NullPointer exception
        playersPlaying.clear(); //Clear out the playersPlaying list
        setCurrentRound(1); //Reset the current Round to be 1
        setCurrentPlayer(0); //Set the currentPlayer to be the first one
        this.setShowedMenu(false); //Reset showed the menu
        System.out.println("\n===================================\n == STARTING A FRESH NEW GAME ==");
        System.out.println("How many rounds would you like to play? (5-30)");
        while(!(safeIntInput(5, 30, wantedRoundsInput = userInputScanner.next(), false) == 1)){
            //Breaks when the input is within a valid range and is a Number
        }
        roundsRemaining = Integer.parseInt(wantedRoundsInput); //Amount of Rounds
        System.out.println("How many players will be playing? (1-4)");
        while(!(safeIntInput(1, 4, wantedPlayersInput = userInputScanner.next(), false) == 1)){
            //Break when the input is within a valid range and is a Number
        }
        players = Integer.parseInt(wantedPlayersInput); //Amount of players
        for(int i = 0; i < players; i++){ //Create the Players
            System.out.print("Please write the name for player " + (i+1) + ": ");
            String name = nameScanner.nextLine(); //Ask user to put in the Name of the respective Players
            playersPlaying.add(new Player(name, 1000)); //Add them to the list of players playing
        }
        System.out.println("\n===================================\n");
    }

    // =========== IF PLAYER CHOSE TO LOAD GAME FROM MAIN MENU ==============

    /**
     * The method responsible for handling loading of Games - creates a savedGames folder if
     * one does not exist - creates a list of all files in the savedGames folder.
     *
     * If there are no files in the savedGames folder, returns to the Main Menu again.
     *
     * Loads every Game File in the savedGames folder (but does not START them) - to showcase
     * information about each respective game (current round in that game, current player, players left,
     * how many were eliminated and each players funds.)
     *
     * @param myGame A game object, the Game the player is currently in
     */
    public void loadGame(Game myGame){
        //Initialize the variables we will be needing
        int counter = 1;
        String userInput = "", filePath;
        File savedGamesFolder = new File("savedGames"); //This is only to make sure that there is a savedGames folder if none exist
        savedGamesFolder.mkdir(); //Returns true if it made a dir, false if not - does not create a new dir if one exists
        File[] gameFiles = new File("savedGames").listFiles(); //Create a list of all the files present in savedGames
        if(loadGameScanner == null){ loadGameScanner = new Scanner(System.in); } //To avoid NullPointer Excpetion
        if(gameFiles.length == 0){ //In case there are no save games in the savedGamesFolder
            //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
            System.out.println("\u001b[31mThere are no save games currently. Returning to main menu.\u001b[0m");
            myGame.showMainMenu(); //Return to the main menu
            return;
        }
        else{
            System.out.println("Which game would you like to load?");
            //Loop through the save files and present info about each save game file
            for(File saves : gameFiles){
                try{
                    FileInputStream fileIn = new FileInputStream("savedGames\\" + saves.getName());
                    ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                    Object obj = objectIn.readObject(); //Read the object in from the Save file
                    Game loadedGame = (Game) obj; //Convert it to a game object and save it
                    objectIn.close(); //Close the stream when Done
                    //Info about the save Game
                    System.out.println("[" + counter + "] " + saves.getName().substring(0, saves.getName().length()-4) + " [ = Round: "
                            + loadedGame.getCurrentRound() + "/" + (loadedGame.getRoundsRemaining() + (loadedGame.getCurrentRound()-1))
                            + " - Players remaining (" + loadedGame.getPlayersPlaying().size() + "/" + loadedGame.getPlayers() + ") = ]");
                    //Print which players are still in the respective available saved Game
                    for(Player playerInGame : loadedGame.getPlayersPlaying()){
                        System.out.println("\t\t\t" + playerInGame);
                    }
                    //Current players name and funds without the Name: part
                    System.out.println("\tCurrent player: " +
                            loadedGame.getPlayersPlaying().get(loadedGame.getCurrentPlayer()).toString().substring(6));
                    //The point of time when the respective saved Game was Saved
                    System.out.println("\tLast played: " + dtf.format(loadedGame.getTimeOfSaving()));
                }
                catch(Exception e){ } //Should just ignore errors in terms of invalid Files loaded
                counter += 1;
            }
            System.out.println("[" + counter + "] Exit");
            //Ask the player for a int input that is valid (between 1 and gameFiles.length+1, inclusive)
            while(!((safeIntInput(1, gameFiles.length+1, userInput = loadGameScanner.next(),false)) == 1)){}
            if(Integer.parseInt(userInput) == gameFiles.length+1){ //If the player wanted to exit back to the Main Menu
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                System.out.println("\u001b[31mExiting from loading game menu.\u001b[0m");
                myGame.showMainMenu(); //Show the Main Menu again
                return;
            }
            filePath = "savedGames\\" + gameFiles[Integer.parseInt(userInput)-1].getName(); //Build the filePath for the selected save Game
            System.out.println("filePath is:" + filePath);
        }
        loadFile(filePath, myGame); //Attempt to load the specified save file, based on the chosen save File
    }

    /**
     * The method that is responsible for loading the save File - opens a FileInputStream on the passed down filePath,
     * opens a ObjectInputStream on the fileInputStream, reads the Object from the ObjectInputStream, converts it to
     * Game - sets the attributes on the passed down Game object with attributes taken from the Loaded game - and runs
     * the passed down game.
     * @param filePath A string, the filePath to open the FileInputStream on
     * @param myGame A game object, the current Game to set attributes for and Run, if loading is successful
     */
    public void loadFile(String filePath, Game myGame){
        //Open the FileInputStream on the given Filepath
        try { //Open the ObjectInputStream on the filePath handle
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = objectIn.readObject(); //Read the object in from the Save file
            Game loadedGame = (Game) obj; //Convert it to a game object and save it
            myGame.setCurrentPlayer(loadedGame.getCurrentPlayer()); //Retrieve the attributes and set them to the current Game
            myGame.setCurrentRound(loadedGame.getCurrentRound());
            myGame.setPlayers(loadedGame.getPlayers());
            myGame.setRoundsRemaining(loadedGame.getRoundsRemaining());
            myGame.setPlayersPlaying(loadedGame.getPlayersPlaying());
            myGame.setShowedMenu(loadedGame.getShowedMenu());
            myGame.setOurStore(loadedGame.getOurStore());
            System.out.println("Successfully loaded save game from: " + filePath);
            if(!myGame.getShowedMenu()){ //When loading, re-create Menus printed as well - including Deaths from saved Turn
                myGame.printAnimals(true, loadedGame.getCurrentRound(), loadedGame.getCurrentPlayer()
                        , loadedGame.getPlayersPlaying(), (roundsRemaining+currentRound-1));
                myGame.setShowedMenu(true); //
            }
            objectIn.close(); //Close the stream when Done
            myGame.runGame(); //Run the loaded game
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // =============== RUNS THE GAME UNTIL REMAINING ROUNDS HITS 0 OR NO MORE PLAYERS IN THE GAME ============

    /**
     * The method that is responsible for running a Game session - handles the flow of the game, such as turns,
     * deaths of Animals, making player choices, calling decay, making death announcements, removing dead animals,
     * checking if a player is eliminated (at the end of their own turn), if the game is over, advancing the round,
     * etc. - When the roundsRemaining hit 0 - or there are no players left - the game ends and concludes with
     * showing the high scores for that session. (If any, if no players are left - it just says that no players made
     * it to the end of the game to actually be crowned a winner).
     */
    public void runGame() {
        if(ourStore == null){ ourStore = new Store(); } //To avoid Null pointer exception
        if(gameMenuScanner == null){ this.gameMenuScanner = new Scanner(System.in); } //To avoid Null pointer exception
        while (roundsRemaining > 0) { //While there still are rounds remaining
            String gameMenuInput = ""; //Input of the player in terms of action choice
            if (playersPlaying.get(currentPlayer).getAmountOfMoney() < 10 &&
                    playersPlaying.get(currentPlayer).getOwnedAnimals().size() == 0){ //The player should be eliminated
                playersPlaying.get(currentPlayer).setTurnIsOver(true); //Their turn is forfeited due to no resources
            }
            if(!playersPlaying.get(currentPlayer).getTurnIsOver()){ //If the players turn is not over
                System.out.println("\n===================================\n");
                if (!showedMenu) { //If the menu has not been shown
                    printAnimals(false, currentRound, currentPlayer, playersPlaying, (roundsRemaining+currentRound-1));
                }
                System.out.println("\nChoose an action:\n" +
                        "[1] Buy an Animal from Store\n" +
                        "[2] Sell an Animal to Store\n" +
                        "[3] Feed your animals\n" +
                        "[4] Breed your animals\n" +
                        "[5] Buy Food\n" +
                        "[6] Sell Animal to Other Player\n" +
                        "[7] Buy Animal from Other Player\n" +
                        "[8] Save game and Exit\n" +
                        "[9] Skip turn");
                //Breaks when input is 1 long and is a number between 1-9
                while(gameMenuInput.replaceAll("[^1-9]", "XX").length() != 1){
                    gameMenuInput = gameMenuScanner.next(); //Keep asking for input
                    makePlayerChoice(gameMenuInput, ourStore); //handle the player choice input
                }
                if(!gameMenuInput.equals("8")){ playersPlaying.get(currentPlayer).setTurnIsOver(true); } //Saving does not consume a turn
                showedMenu = true; //menu was shown and input was processed
            }
            if (playersPlaying.get(currentPlayer).getTurnIsOver()) { //If the player turn is over
                showedMenu = false; //Reset variables
                decayAgeAndDiseaseAtEndofTurn(); //Age, Decay and roll for Disease on Animals
                makeDeathAnnouncementsForDeadAnimals(playersPlaying.get(currentPlayer)); //Handle death announcements of Animals for current Player
                removeDeadAnimals(); //Remove dead animals for current Player
                checkIfPlayerIsEliminated(playersPlaying.get(currentPlayer)); //See if the current player was eliminated
                if(ifGameIsOver()){ break; } //Check if the Game is over, i.e any players remaining
                nextPlayer(); //Increment the currentPlayer index
                if(seeIfNewRound()){ //See if there is a new round abound
                    //If there is, then..
                    advanceToNewRound(); //Resets currentPlayer to 0, increases current round and reduces roundsRemaining
                    resetAnimalDecay(); //Reset the Decay for ALL Animals of ALL players
                    resetTurnIsOver(); //Reset the Turn is over for ALL remaining Players
                    purgeSavedDeathLists(); //Purge SavedDeathLists from 2 rounds ago
                }

            }
        }
        presentHighScore(); //Proceed to handling the high scores
        System.out.println("Thanks for playing The Pet Game! See you next time!");
        System.exit(1); //Exit the game, as the session is concluded
    }

    // ==================== PLAYER MAKES A CHOICE ============
    /**
     * The method that handles game menu choices
     * @param gameMenuInput The player choice in menu options
     * @param ourStore The store that the player can buy from
     */
    public void makePlayerChoice(String gameMenuInput, Store ourStore){
        switch (gameMenuInput) {
            case "1" -> ourStore.buyAnimal(playersPlaying.get(currentPlayer)); //Buy an Animal from the Store
            case "2" -> ourStore.sellAnimal(playersPlaying.get(currentPlayer)); //Sell an Animal to the Store
            case "3" -> playersPlaying.get(currentPlayer).feedAnimal(); //Feed their animals
            case "4" -> breedAnimal(playersPlaying.get(currentPlayer)); //Breed a set of Animals
            case "5" -> ourStore.buyFood(playersPlaying.get(currentPlayer)); //Buy some food from the Store
            case "6" -> sellToOtherPlayer(playersPlaying.get(currentPlayer)); //Sell Animals to other players
            case "7" -> buyFromOtherPlayer(playersPlaying.get(currentPlayer)); //Buy Animals from other players
            case "8" -> saveGame(this); //Goes to saving game menu
            case "9" -> System.out.println(playersPlaying.get(currentPlayer).getName() + " chose to skip their turn."); //User skipped their turn
            default -> System.out.println("Not a valid option, please try again."); //In case input is not 1-9
        }
    }

    // ==================== IF THE PLAYER WANTS TO BREED ANIMALS ============
    /**
     * The method responsible for breeding of Animals - delegates responsibilities to Sub-methods
     * @param playerBreeding A player object, the player Breeding animals
     */
    public void breedAnimal(Player playerBreeding){
        if(breedInput == null){ breedInput = new Scanner(System.in); }
        Random random = new Random();
        //Owned animals/Females/males
        ArrayList<Animal> ownedAnimals = playerBreeding.getOwnedAnimals(), males = new ArrayList<>(), females = new ArrayList<>();
        String firstAnimalWanted,secondAnimalWanted; //wanted Animals indexes
        int returnCode = 0; //status code from handling input, used to check for exit
        if(ownedAnimals.size() <= 1){ //If the player has not enough Animals to breed
            //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
            System.out.println("\u001b[31m" + playerBreeding.getName() +
                    " needs to have at least 2 Animals to breed. Returning to Game menu.\u001b[0m");
            return;
        }
        for(Animal inspectedAnimal : ownedAnimals){ //inspect animals
            if( inspectedAnimal.getGender().equals("Male") ) { males.add(inspectedAnimal); } //Males get added to male List
            else{ females.add(inspectedAnimal); } //Females get added to female list
        }
        if(males.size() == 0 || females.size() == 0){ //If the player owns no Males or Females
            //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
            System.out.println("\u001b[31mDid not find enough males or females to breed with, Returning to Game menu.\u001b[0m");
            return;
        }
        else {
            System.out.println("Which two animals do you wish to breed?");
            printMales(males); //Print the menu of relevant males
            while(!((returnCode = (safeIntInput(1, males.size()+1, firstAnimalWanted = breedInput.next(),
                    true))) == 1)){ if(returnCode == 2) return; }
            //Force index boundary between 1 and males.size()+1, highest index is exit - returnCode is 2 if the input is the Exit index
            printFemales(females); //Print the menu of relevant females
            while(!((returnCode = (safeIntInput(1, females.size()+1, secondAnimalWanted = breedInput.next(),
                    true))) == 1)){ if(returnCode == 2) return; }
            //Force index boundary between 1 and females.size()+1,
            // highest index is exit highest index is exit - returnCode is 2 if the input is the Exit index
            Animal theMale = males.get(Integer.parseInt(firstAnimalWanted) - 1); //The wanted Male
            Animal theFemale = females.get(Integer.parseInt(secondAnimalWanted) - 1); //The wanted female
            if(theMale.getClassName().equals(theFemale.getClassName())){ //If they're both of the same race
                int madeOffspring = random.ints(1,3).findFirst().getAsInt(); //Chanse of making offspring
                if(madeOffspring > 1) //Failed
                {
                    //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                    System.out.println("\u001b[31m" + theFemale.getVanillaInfo() + " and " + theMale.getVanillaInfo() +
                            " did not manage to make any babies.\n\u001b[0m");
                }
                else{ //Made children
                    //Code for Green in Consoles - \u001b[32m - Reset code for Colors in Console \u001b[0m
                    int amountOfBabies = random.ints(theFemale.getMinimumOffspring(),theFemale.getMaximumOffspring()).findFirst().getAsInt();
                    System.out.println("\u001b[32m" + theMale.getVanillaInfo() + " and " + theFemale.getVanillaInfo() + " made "
                            + amountOfBabies + " babies!\u001b[0m");
                    //Create the babies
                    theFemale.createBabies(amountOfBabies, females, playerBreeding, Integer.parseInt(secondAnimalWanted)); } }
            else{
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                System.out.println("\u001b[31mCannot breed two different Animals of different breeds. Returning to Game Menu\u001b[0m");
            }
        }
    }

    // ============ IF THE PLAYER WANTS TO SELL TO OTHER PLAYERS ================
    /**
     * Method that handles Selling to other players - delegates most responsibilities to sub-methods
     * @return An int, the status code of what happened
     */
    public int sellToOtherPlayer(Player seller){
        int buyersIndex, sellersIndex, returnCode; //Indexes and the returnCode from selling
        ArrayList<Player> buyers = new ArrayList<>(); //List of Buyers
        String targetPlayerIndex, animalToSellIndex; //Inputs from Users to convert to Integers
        boolean doneSelling = false;
        while(!doneSelling){
            boolean result = playersPlaying.size() < 2 || seller.getHealthyAnimals().size() == 0; //1 player or no Animals
            if(result){
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                System.out.println("\u001b[31mToo few players left or no healthy animals left to sell. Returning to Game menu.\u001b[0m");
                return -1;
            }

            sellersIndex = listOfBuyersInSellToOtherPlayer(seller, buyers, playersPlaying); //Index of the Seller
            //Index of the player to sell to, returnCode is 2 if the Index is the Exit index
            while(!((returnCode = (safeIntInput(1, buyers.size()+1, targetPlayerIndex = userInputScanner.next(),
                    true))) == 1)){ if(returnCode == 2) return 1; }
            Player buyer = buyers.get(Integer.parseInt(targetPlayerIndex)-1); //Get the buyer

            buyersIndex = sellingAnimalToOtherPlayerMenu(buyer, seller, playersPlaying); //index of the Buyer
            //Index of the Animal to sell, returnCode is 2 if the index is the Exit index
            while(!((returnCode = (safeIntInput(1, seller.getHealthyAnimals().size()+1, animalToSellIndex = userInputScanner.next(),
                    true))) == 1)){ if(returnCode == 2) return 1; }
            Animal animalBeingSold = seller.getHealthyAnimals().get(Integer.parseInt(animalToSellIndex)-1);

            if(animalBeingSold.getSellsFor() > buyer.getAmountOfMoney()){ printCantAffordAnimal(buyer, animalBeingSold); return -1; }
            //Code for Green in Consoles - \u001b[32m - Reset code for Colors in Console \u001b[0m
            System.out.println("\u001b[32m" + buyer.getName() + " bought " + animalBeingSold.getVanillaInfo() + " for "
                    + animalBeingSold.getSellsFor() + " coins from " + seller.getName() + ".\u001b[0m");

            buyer.pay(animalBeingSold.getSellsFor()); //buyer pays
            seller.getPaid(animalBeingSold.getSellsFor()); //Seller gets paid

            buyer.addToOwnedAnimals(animalBeingSold); //Buyer gets the Animal
            seller.getOwnedAnimals().remove(animalBeingSold); //Seller removes his Animal

            //An animal that is sold "downwards" in the turn order, should age, decay and disease
            // to account for aging/decaying/disease during a round
            //as Decay,Aging and Disease occurs at the end of a Turn (once per Round), however - this would be skipped if sold downwards
            if(sellersIndex > buyersIndex){ //Age, Decay and Disease the Animal if it would have circumvented this
                if(animalBeingSold.isAlive()){
                    animalBeingSold.decay(); //Decay
                }
                if(animalBeingSold.isAlive()){
                    animalBeingSold.age(); //Age
                }
                if(animalBeingSold.isAlive()){
                    animalBeingSold.chanceForDisease(buyer); //Buyer is responsible for vet bill as the new owner
                }
                animalBeingSold.setDecayedThisRound(true);
                //To place the deathAnnouncement on the correct player (the buyer), we need to have a modifier of
                //the SELLER_INDEX - BUYER_INDEX (This would be the same as saying CURRENT_PLAYER - BUYER_INDEX) -
                //to place the death announcement for the correct player who is (CURRENT_PLAYER - BUYER_INDEX) turns
                //behind in the turn order
                makeDeathAnnouncementsForDeadAnimals(buyer); //If the Animal died in the process of selling to a player who is
                //earlier in the turn order - make a death announcement for that respective player - based on this turn
                for(int i = buyer.getOwnedAnimals().size()-1; i > -1; i--){
                    if(!buyer.getOwnedAnimals().get(i).isAlive()){
                        buyer.getOwnedAnimals().remove(i); //if the Animal died during the process, remove it
                    }
                }
                checkIfPlayerIsEliminated(buyer); //If the player Buying has run out of funds, the animal they were
                //buying would be their only Animal and it Died in the selling process due to Aging/decay or Disease
                //when it had to Decay/Age/Check for Disease to not circumvent this mechanic in the trading process of a player
                //later in the turn order selling to an earlier one - they should be eliminated at this point of time instead of
                //their own turn, to avoid being eliminated on their own turn
            }
            //Reset the variables at the end of the While loop, so they're clean for the next run
            buyersIndex = 0;
            sellersIndex = 0;
            buyers = new ArrayList<Player>();
            targetPlayerIndex = "";
            animalToSellIndex = "";
        }
        return 1;
    }

    // ============= IF THE PLAYER WANTS TO BUY FROM OTHER PLAYERS ==================

    /**
     * The method responsible for buying Animals from other players - Delegates most responsibilities
     * to sub-methods
     * @param buyer A player object, the Buyer in question
     * @return A int, the status code to return
     */
    public int buyFromOtherPlayer(Player buyer){
        int counter = 1, returnCode = 0;
        ArrayList<Player> sellers = new ArrayList<>(); //List of sellers
        String targetPlayerIndex, animalToBuyIndex; //Indexes the player wishes to target (player and Animal)
        boolean doneBuying = false;
        while(!doneBuying) {
            if( playersPlaying.size() < 2 ){
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                System.out.println("\u001b[31mNo other players in game. Returning to Game menu.\u001b[0m");
                return -1;
            } //Too few players
            sellers = buildSellersList(buyer, sellers, playersPlaying);
            if ( sellers.size() == 0 ){
                //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
                System.out.println("\u001b[31mNo other players had healthy animals to sell. Returning to Game menu.\u001b[0m");
                return -1;
            } //No other player with Animals to buy from is available
            else{ printSalesMenu(buyer, sellers); } //Otherwise, just print the sellers

            while(!((returnCode = (safeIntInput(1, sellers.size()+1, targetPlayerIndex = userInputScanner.next(),
                    true))) == 1)){ if(returnCode == 2) return 1; }
            //Which player the Player wants to buy from - forces a valid index, returnCode is 2 if it's the exit index
            Player seller = sellers.get(Integer.parseInt(targetPlayerIndex)-1); //Save the seller

            printBuyFromOtherPlayerMenu(buyer, seller); //print the Menu

            while(!((returnCode = (safeIntInput(1, seller.getHealthyAnimals().size()+1, animalToBuyIndex = userInputScanner.next(),
                    true))) == 1)){ if(returnCode == 2) return 1; } //Which animal the player wants to Buy
            Animal animalBeingBought = seller.getHealthyAnimals().get(Integer.parseInt(animalToBuyIndex)-1); //Animal being Bought
            if(animalBeingBought.getSellsFor() > buyer.getAmountOfMoney()) {
                printFailedTransaction(buyer, animalBeingBought); //Print the failed Transaction section
                return -2;
            } //Not enough money
            //Code for Green in Consoles - \u001b[32m - Reset code for Colors in Console \u001b[0m
            System.out.println("\u001b[32m" + buyer.getName() + " bought " + animalBeingBought.getVanillaInfo() +
                    " for " + animalBeingBought.getSellsFor() + " coins from " + seller.getName() + ".\u001b[0m");
            buyer.pay(animalBeingBought.getSellsFor()); //buyer pays
            seller.getPaid(animalBeingBought.getSellsFor()); //Seller gets paid
            buyer.addToOwnedAnimals(animalBeingBought); //Buyer gets Animal
            seller.getOwnedAnimals().remove(animalBeingBought); //Seller removes Animal
            sellers = new ArrayList<>(); //Sellers list is reset
            checkIfPlayerIsEliminated(seller); //Check to see if the Seller is to be eliminated based on that they have
            //no more animals and insufficient coins to do ANYTHING on their turn - this is to ensure that a player is eliminated
            //at the point of time where they ran out of resources - not just during their own turn
        }
        return 3;
    }

    // ============== IF THE PLAYER CHOSE TO SAVE THE GAME =======================

    /**
     * Handles menu printing and User input for Saving games
     * @param myGame A game object, the game being saved to be passed down to handling saving the game in terms of file writing
     * @throws FileNotFoundException An exception that has to be declared in case of IO errors
     */
    public void saveGame(Game myGame){
        String overwrite = "", newName = "", fullSavePath = "savedGames\\", saveGameName;
        File savedGamesFolder = new File("savedGames");
        savedGamesFolder.mkdir(); //If the savedGamesFolder does not exist, create it - to avoid a Null pointerException
        File[] gameFiles = new File("savedGames").listFiles();
        if(saveGameScanner == null){ saveGameScanner = new Scanner(System.in); }
        if(gameFiles.length > 0){
            System.out.println("Saved games in savedGames folder: ");
            for(File gameFile: gameFiles){
                System.out.println(gameFile.getName().substring(0,gameFile.getName().length()-4));
            }
        }
        System.out.println("What would you like to name your save file?");
        while(((saveGameName = saveGameScanner.nextLine()).replaceAll("[^A-Za-z0-9]", "").length()) == 0){
            System.out.println("The name of the save game can only consist of letters,Numbers and must be more than 0 Symbols long.");
            System.out.println("Attempted input was: " + saveGameName);
        }
        fullSavePath += saveGameName + ".ser";
        for(File gameFile : gameFiles){
            if(("savedGames\\" + gameFile.getName()).equals(fullSavePath)){
                newName = fullSavePath;
                System.out.println("There already is a saved game at the path of: " + fullSavePath + ", do you wish to overwrite it? (Y/N)");
                while(!(forceYOrN(overwrite = saveGameScanner.nextLine()) == 1)){ } //Breaks when Y,y,N or n
                if(overwrite.toLowerCase().equals("n")){
                    while(newName.equals(fullSavePath)){
                        System.out.println("Please provide another name for the file to save: ");
                        newName = "savedGames\\" + saveGameScanner.nextLine() + ".ser";
                    }
                }
            }
        }
        if(overwrite.toLowerCase().equals("n")){ fullSavePath = newName; }
        saveFile(fullSavePath, saveGameName, myGame);
    }

    /**
     * Handles saving of a File
     * @param fullSavePath A string, the File path
     * @param saveGameName A string, the name of the saved game
     * @param myGame A game object, the game being saved
     * @return An int, a status code
     *     -1: Failed in saving the game
     *      1: Succeeded in Saving the game
     */
    public int saveFile(String fullSavePath, String saveGameName, Game myGame){
        try{
            timeOfSaving = LocalDateTime.now();
            FileOutputStream fileOut = new FileOutputStream(fullSavePath); //Opens the FileOutputStream to the File path destination
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut); //Opens the ObjectOutputStream to the fileOutputStream
            myGame.setShowedMenu(false);
            objectOut.writeObject(myGame); //Write the game Object to the Save
            objectOut.close(); //Close the stream
            System.out.println("Successfully saved game to: savedGames\\" + saveGameName + ".ser - Returning to Main Menu.");
            myGame.showMainMenu(); //Throw the user back to the Main Menu
            return 1;
        }
        catch(Exception e){
            System.out.println(e + "\nReturning back to Game Menu.");
            printAnimals(false, currentRound, currentPlayer, playersPlaying, (roundsRemaining+currentRound-1));
            return -1;
        }
    }

    // ==================== HANDLE DECAYING, AGING AND DISEASE AT END OF PLAYER TURN ============
    /**
     * Method that handles aging,decaying and disease at the end of the turn FOR CURRENT PLAYER
     */
    public void decayAgeAndDiseaseAtEndofTurn(){
        for (Animal eachPlayerAnimal : playersPlaying.get(currentPlayer).getOwnedAnimals()) {
            if (eachPlayerAnimal.isAlive() && !eachPlayerAnimal.getDecayedThisRound()) { //If the Animal is alive and hasn't decayed, decay
                eachPlayerAnimal.decay();
            }
            if (eachPlayerAnimal.isAlive() && !eachPlayerAnimal.getDecayedThisRound()) { //If the Animal is alive and hasn't decayed, age
                eachPlayerAnimal.age();
            }
            if (eachPlayerAnimal.isAlive() && !eachPlayerAnimal.getDecayedThisRound()) { //If the Animal is alive and hasn't decayed, roll for Disease
                eachPlayerAnimal.chanceForDisease(playersPlaying.get(currentPlayer));
            }
            if(!eachPlayerAnimal.getDecayedThisRound()){
                eachPlayerAnimal.setDecayedThisRound(true); //The animal has had it's decay/age/disease for the Turn
            }
        }
    }

    // ==================== HANDLE DEATHS OF ANIMALS ============
    /**
     * The method that handles death announcements and Death lists - adds Death Announcements to
     * a player based on a modifier - If the modifier is 0 - It's the current Player - The only time
     * there is a modifier otherwise - is when sold to an Earlier player - as ownership of the Pet is
     * changed - and the death announcement should be added for the respective new owner.
     */
    public void makeDeathAnnouncementsForDeadAnimals(Player theOwnerOfTheAnimal){
        for (int i = 0; i < theOwnerOfTheAnimal.getOwnedAnimals().size(); i++) {
            if (!theOwnerOfTheAnimal.getOwnedAnimals().get(i).isAlive()) {
                //Died of starvation or Old Age or Disease
                theOwnerOfTheAnimal.getOwnedAnimals().get(i).setPerishedAtRound(currentRound);
                //Add a death Announcement
                theOwnerOfTheAnimal.addDeathAnnouncement(currentRound,theOwnerOfTheAnimal.getOwnedAnimals().get(i).getDeathMessage());
                //Add to the players death List - used in Loading games to archive old deaths and re-construct death messages
                theOwnerOfTheAnimal.addToPlayerDeathList(theOwnerOfTheAnimal.getOwnedAnimals().get(i).getDeathMessage());
            }
        }
    }

    // ==================== REMOVE DEAD ANIMALS ============
    /**
     * Removes Dead animals from CURRENT PLAYER
     */
    public void removeDeadAnimals(){
        for(int i = playersPlaying.get(currentPlayer).getOwnedAnimals().size()-1; i > -1; i--){
            if(!playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).isAlive()){
                playersPlaying.get(currentPlayer).getOwnedAnimals().remove(i);
            }
        }
    }

    // ==================== CHECK IF PLAYER IS ELIMINATED ============
    /**
     * Checks if a Player fulfills the criteria of being eliminated and eliminates them if true
     */
    public void checkIfPlayerIsEliminated(Player playerToCheck){
        if (playerToCheck.getAmountOfMoney() < 10 &&
                playerToCheck.getOwnedAnimals().size() == 0) { //If the player cannot afford anything and has no Animals
            //Code for Red in Consoles - \u001b[31m - Reset code for Colors in Console \u001b[0m
            System.out.println("\u001b[31m" + playerToCheck.getName()
                    + " was eliminated at Round: " + currentRound + " due to not having" +
                    " enough money to buy anything and no animals left." + "\u001b[0m");
            playersPlaying.remove(playerToCheck); //Remove the player
            int newIndex = 0;
            for(Player playerRemaining: playersPlaying){ //When a player is eliminated, find the index of the next players turn
                if(!playerRemaining.getTurnIsOver()){
                    currentPlayer = newIndex;
                    break;
                }
                newIndex += 1;
            }
            if(currentPlayer == playersPlaying.size()){ //To prevent out of bounds error
                currentPlayer -= 1;
            }
        }
    }

    // ==================== CHECK IF THE GAME IS OVER ============
    /**
     * Checks if the Game is Over
     * @return True if it's over, False if not
     */
    public boolean ifGameIsOver(){
        if (playersPlaying.size() == 0) { //Check if there are any players left
            System.out.println("Game is over. No players remaining.");
            return true;
        }
        return false;
    }

    // ==================== ADVANCE TO THE NEXT PLAYER ============
    /**
     * Advances the currentPlayer counter
     */
    public void nextPlayer(){
        if (playersPlaying.get(currentPlayer).getTurnIsOver()) { //Switch over to the next player
            currentPlayer += 1;
        }
    }

    // ==================== SEE IF THERE IS A NEW ROUND ABOUND ============

    /**
     * Returns true if there should be a new round, false if not
     * @return A boolean, true if should advance to next round, false if not
     */
    public boolean seeIfNewRound(){
        if(currentPlayer > playersPlaying.size()-1){ //If it's a new round
            return true;
        }
        return false;
    }

    // =============== IF THERE IS A NEW ROUND FROM SEEIFNEWROUND, ADVANCE TO THE NEW ROUND ============
    /**
     * Resets player to first player in Order, advances CurrentRound by 1, decreases roundsRemaining by 1
     */
    public void advanceToNewRound(){
        currentPlayer = 0; //Reset it to the first player
        currentRound += 1; //Increase the current Round
        roundsRemaining -= 1; //Remove one round from Rounds remaining
    }

    // ==================== RESET ANIMAL DECAY FOR THE NEW ROUND ============
    /**
     * Resets the Animal Decay for ALL players still playing
     */
    public void resetAnimalDecay(){
        for(Player players : playersPlaying) {
            for (Animal animals : players.getOwnedAnimals()) {
                animals.setDecayedThisRound(false);
            }
        }
    }

    // ==================== RESET TURN IS OVER FOR THE NEW ROUND ============
    /**
     * Resets turn is over for ALL players
     */
    public void resetTurnIsOver(){
        for(Player players : playersPlaying){
            players.setTurnIsOver(false);
        }
    }

    // ==================== PURGE SAVED DEATH LISTS FOR THE NEW ROUND  ============
    /**
     * Purges the Saved Death Lists of ALL players - to prevent rehashing of Old Death messages when Loading
     */
    public void purgeSavedDeathLists(){
        for(Player players : playersPlaying){
            players.purgeSavedDeathList(currentRound-2);
        }
    }

    // ============== SELL OFF THE ANIMALS AT THE END OF THE GAME ==================
    /**
     * Handles selling off healthy Animals at the end of the Game and removing Animals that are dead/sick, so they are not
     * accounted for in the Selling process
     * @return An ArrayList of Integers, the money of the players after selling off
     */
    public ArrayList<Integer> sellOffAnimalsAtEndOfGame(){
        ArrayList<Integer> moneyOfPlayers = new ArrayList<>(); //The money of the Players
        System.out.println();
        for(Player player : playersPlaying){ //Sell off the Animal of each player at the end of the game
            for(int i = player.getOwnedAnimals().size()-1; i > -1; i--){
                //Dead Animals and Sick Animals don't count at the end of the game when selling, as both are functionally dead
                if(player.getOwnedAnimals().get(i).getHealth() > 0 && !player.getOwnedAnimals().get(i).isSick()){
                    System.out.println(player.getName() + " sold off " + player.getOwnedAnimals().get(i).getColoredInfo()
                            + " for " + player.getOwnedAnimals().get(i).getSellsFor() + " coins.");
                    player.getPaid(player.getOwnedAnimals().get(i).getSellsFor()); //Sells the Animal
                }
                player.getOwnedAnimals().remove(i); //removes it
            }
            moneyOfPlayers.add(player.getAmountOfMoney()); //Add to a list of Money, how much money each player had
        }
        Collections.sort(moneyOfPlayers);
        return moneyOfPlayers;
    }

    // =============== BUILD THE HIGH SCORE ======================
    /**
     * A method that sells off all the animals at the end of the Game and builds the High-score
     * @return An Arraylist of Strings, which is the players high score
     */
    public ArrayList<String> buildHighScore(){
        ArrayList<Integer> moneyOfPlayers; //The money of the Players
        ArrayList<String> namesOfPlayers = new ArrayList<>(), highScore = new ArrayList<>(); //names and the High score

        for(Player player : playersPlaying){ //
            namesOfPlayers.add(player.getName()); //Add to a list, the names of the remaining players
        }
        moneyOfPlayers = sellOffAnimalsAtEndOfGame();
        Collections.sort(moneyOfPlayers); //Sort the money list, lowest to highest
        int spot = 1;
        for(int i = moneyOfPlayers.size()-1; i > -1; i--){ //For each amount of Money to be looked at
            for(Player player: playersPlaying){ //For each player, look if they correspond to the amount of money
                if(player.getAmountOfMoney() == moneyOfPlayers.get(i) && !player.getAddedToHighScore()){ //If it's a match, add them to the high score
                    highScore.add(player.getName() + " : " + player.getAmountOfMoney() + " coins.");
                    if(spot == 1){ //The last element in the sorted list is the winner, so on the first looping through, that's the winner
                        //Add the winner to the winners list
                        winner.add("The winner is: " + player.getName() + " with a whopping amount of " + player.getAmountOfMoney() + " coins!");
                    }
                    player.setAddedToHighScore(true); //player was added to the high score
                }
            }
            spot += 1; //keep running tally of how many elements we've gone through
        }
        return highScore; //return the high score
    }

    // ======================= PRESENT THE HIGHS CORE THAT WAS BUILT ======================
    /**
     * Presents the High score and Announces the Winner (if any is present) at the End of the Game
     */
    public void presentHighScore(){
        ArrayList<String> highScore = buildHighScore(); //Build the high score list

        System.out.println("""
                \n\t\t=======================
                At the end of the game, here are the results:
                \t\t=======================""");
        int spots = 1;
        for(String highScoreSpots : highScore){
            System.out.println("\t\t[" + spots + "] " + highScoreSpots);
            spots += 1;
        }
        System.out.println("\t\t=======================");
        if(winner.size() > 0) { System.out.println(winner.get(0)); } //There was a winner
        else{ System.out.println("There were no players who made it to the end of the game."); } //No player made it to the end
        System.out.println("\t\t=======================");
    }
}