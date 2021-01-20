package gameComponents;

import Animals.Animal;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;
import Animals.*;

public class Game extends utilityFunctions implements Serializable{
    private int rounds = 0;
    private int players = 0;
    private String wantedRoundsInput = "";
    private String wantedPlayersInput = "";
    private int currentRound = 1;
    private int currentPlayer = 0;
    private boolean showedMenu = false;
    private Random random = new Random();


    private ArrayList<Player> playersPlaying = new ArrayList<>();
    public Game(){
        askForInput();
        runGame();
    }


    public void askForInput(){
        Scanner userInput = new Scanner(System.in);

        System.out.println("How many rounds would you like to play?");
        while(!(safeIntInput(5, 30, wantedRoundsInput = userInput.next()) == 1)){
            //Breaks when the input is within a valid range and is a Number
        }
        rounds = Integer.parseInt(wantedRoundsInput);

        System.out.println("How many players will be playing?");
        while(!(safeIntInput(1, 4, wantedPlayersInput = userInput.next()) == 1)){
            //Break when the input is within a valid range and is a Number
        }
        players = Integer.parseInt(wantedPlayersInput);

        for(int i = 0; i < players; i++){
            Scanner nameScanner = new Scanner(System.in);
            System.out.print("Please write the name for player " + (i+1) + ": ");
            String name = nameScanner.nextLine();
            playersPlaying.add(new Player(name, 1000));
        }

    }

    public int breedAnimal(Player playerBreeding){
        Scanner userInput = new Scanner(System.in);
        ArrayList<Animal> ownedAnimals = playerBreeding.getOwnedAnimals();
        String firstAnimalWanted;
        String secondAnimalWanted;

        if(playerBreeding.getOwnedAnimals().size() <= 1){
            System.out.println(playerBreeding.getName() + " needs to have at least 2 Animals to breed. Returning to main menu.\n");
            return -2;
        }

        ArrayList<Animal> males = new ArrayList<>();
        ArrayList<Animal> females = new ArrayList<>();

        for(Animal inspectedAnimal : ownedAnimals){
            if(inspectedAnimal.getGender().equals("Male")){
                males.add(inspectedAnimal);
            }
            else{
                females.add(inspectedAnimal);
            }
        }
        if(males.size() == 0){ System.out.println("Found no males to breed with, Returning to main menu.\n"); return -2;}
        else if(females.size() == 0){ System.out.println("Found no females to breed with, Returning to main menu.\n"); return -2; }
        else {
            System.out.println("Which two animals do you wish to breed?");

            System.out.println("Please choose a male: ");
            int firstAnimal;
            int counter = 1;
            for(Animal male : males){
                System.out.println("[" + counter + "] " + male.getName() + " the " + male.getClassName() + ", (Health: " + male.getHealth() + " " +
                        ", " + male.getGender() + ")");
                counter += 1;
            }
            System.out.println("[" + counter + "] Back to Main Menu");
            while(!(safeIntInput(1, males.size()+1, firstAnimalWanted = userInput.next()) == 1)){
                //Break when the input is within a valid range and is a Number
            }

            firstAnimal = Integer.parseInt(firstAnimalWanted);
            if(firstAnimal == males.size()+1){
                System.out.println(playerBreeding.getName() + " chose to exit. Returning to main menu.\n");
                return -2;
            }

            System.out.println("Please choose a female: ");
            int secondAnimal;
            counter = 1;
            for(Animal female : females){
                System.out.println("[" + counter + "] " + female.getName() + " the " + female.getClassName() + ", (Health: " +female.getHealth() + " " +
                        ", " + female.getGender() + ")");
                counter += 1;
            }
            System.out.println("[" + counter + "] Back to Main Menu");
            while(!(safeIntInput(1, females.size()+1, secondAnimalWanted = userInput.next()) == 1)){
                //Break when the input is within a valid range and is a Number
            }

            secondAnimal = Integer.parseInt(secondAnimalWanted);
            if(secondAnimal == females.size()+1){
                System.out.println(playerBreeding.getName() + " chose to exit. Returning to main menu.\n");
                return -2;
            }
            if(males.get(firstAnimal-1).getClassName().equals(females.get(secondAnimal-1).getClassName())){
                int madeOffspring = random.ints(1,3).findFirst().getAsInt();
                if(madeOffspring == 1){
                    int amountOfBabies = random.ints(females.get(secondAnimal-1).getMinimumOffspring(),
                            females.get(secondAnimal-1).getMaximumOffspring()).findFirst().getAsInt();
                    System.out.println(males.get(firstAnimal-1).getName() + " the " + males.get(firstAnimal-1).getClassName() +
                                    " (Male) and " + females.get(secondAnimal-1).getName() +
                                    " the " + females.get(secondAnimal-1).getClassName() +
                            " (Female) produced " + amountOfBabies + " baby " + males.get(firstAnimal-1).getClassName() +
                            (amountOfBabies > 1 ? "s." : "."));
                    for(int i = 0; i < amountOfBabies; i++){
                        int genderChance = random.ints(1,3).findFirst().getAsInt();
                        String gender;
                        if(genderChance == 1){
                            gender = "Male";
                            System.out.println("It's a boy!");
                        }
                        else{
                            gender = "Female";
                            System.out.println("It's a girl!");
                        }
                        System.out.println("What would you like to name your new baby " + females.get(secondAnimal-1).getClassName() + " " +
                                "(" + gender + ")?");
                        Scanner nameScanner = new Scanner(System.in);
                        String name = nameScanner.nextLine();

                        switch(females.get(secondAnimal-1).getClassName()){
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
                else{
                    System.out.println(females.get(secondAnimal-1).getName() + " the " + females.get(secondAnimal-1).getClassName()
                            + " and " + males.get(firstAnimal-1).getName() + " the " + males.get(firstAnimal-1).getClassName()
                            + " did not manage to make any babies..");
                }
            }
            else{
                System.out.println("Cannot breed two different Animals of different breeds.");
            }
        }
        return -1;
    }

    public int buyFromOtherPlayer(Player buyer){
        //TO DO
        int counter = 1;
        ArrayList<Player> sellers = new ArrayList<Player>();
        String targetPlayerIndex;
        String animalToBuyIndex;

        boolean doneBuying = false;

        while(!doneBuying) {

            if(playersPlaying.size() < 2){
                System.out.println("There are no other players left in the game to buy Animals from. Returning to main menu.");
                return -1; //Failed due to trying to buy when there are no other players left in the game
            }
            Scanner userInput = new Scanner(System.in);

            for(Player players : playersPlaying){
                if(!players.equals(buyer)){
                    counter += 1;
                    if(players.getOwnedAnimals().size() > 0){
                        sellers.add(players);
                    }
                }
            }

            if(sellers.size() == 0){
                System.out.println("There are no animals to purchase from other players right now. Returning to main menu.");
                return -1; //Failed finding any seller who owns an Animal
            }
            else{
                counter = 1;
                System.out.println("Which Player does " + buyer.getName() + " wish to buy Animals from?");
                for(Player players: sellers){
                    System.out.println("[" + counter + "] " + players.getName() + " - Owns " + players.getOwnedAnimals().size() + " animal(s): ");
                    for(Animal ownedAnimals : players.getOwnedAnimals()){
                        System.out.println("\t" + ownedAnimals.getInfo() + " Costs: " + ownedAnimals.getSellsFor() + " coins");
                    }
                    counter += 1;
                }
            }

            System.out.println("[" + counter + "] Back to main menu");
            while(!(safeIntInput(1, sellers.size()+1, targetPlayerIndex = userInput.next()) == 1)){
                //Break when the input is within a valid range and is a Number
            }

            if(Integer.valueOf(targetPlayerIndex) == sellers.size()+1){
                System.out.println(buyer.getName() + " returned back to the main menu.");
                return 1; //Exited selling menu by will
            }

            Player seller = sellers.get(Integer.valueOf(targetPlayerIndex)-1);

            counter = 0;
            for(Player player: playersPlaying){
                counter += 1;
            }

            System.out.println("Which animal does " + buyer.getName() + " wish to buy from:" + seller.getName() + " (" + buyer.getName() +"'s funds: "
                    + buyer.getAmountOfMoney() + " coins)" + "?");
            counter = 1;
            for(Animal sellersAnimals: seller.getOwnedAnimals()){
                System.out.println("[" + counter + "] " + sellersAnimals.getInfo() + " Costs: " + sellersAnimals.getSellsFor() + " coins");
                counter += 1;
            }
            System.out.println("[" + counter + "]" + " Back to main menu");

            while(!(safeIntInput(1, seller.getOwnedAnimals().size()+1, animalToBuyIndex = userInput.next()) == 1)){
                //Break when the input is within a valid range and is a Number
            }
            if(Integer.valueOf(animalToBuyIndex) == seller.getOwnedAnimals().size()+1){
                System.out.println(buyer.getName() + " returned back to the main menu.");
                return 1; //Exited selling menu by will
            }
            Animal animalBeingBought = seller.getOwnedAnimals().get(Integer.valueOf(animalToBuyIndex)-1);
            System.out.println("THE ANIMAL BEING BOUGHT IS : " + animalBeingBought.getInfo());

            if(animalBeingBought.getSellsFor() > buyer.getAmountOfMoney()){
                System.out.println(buyer.getName() + " cannot afford " + animalBeingBought.getInfo() + "! (Needed: " + animalBeingBought.getSellsFor() +
                        " coins, has only " + buyer.getAmountOfMoney() + " coins)");
                System.out.println("Returning back to main menu.");
                return -1;  //A transaction failed due to insufficient funds, aborting buying operations
            }

            System.out.println(buyer.getName() + " bought " + animalBeingBought.getInfo() + " for "
                    + animalBeingBought.getSellsFor() + " coins from " + seller.getName() + ".");


            buyer.pay(animalBeingBought.getSellsFor()); //buyer pays
            seller.getPaid(animalBeingBought.getSellsFor()); //Seller gets paid

            buyer.addToOwnedAnimals(animalBeingBought);
            seller.getOwnedAnimals().remove(animalBeingBought);

            counter = 1;
            sellers = new ArrayList<Player>();
            targetPlayerIndex = "";
            animalToBuyIndex = "";

        }
        return 1;
    }

    public int sellToOtherPlayer(Player seller){
        int counter = 1;
        int buyersIndex = 0;
        int sellersIndex = 0;
        ArrayList<Player> buyers = new ArrayList<Player>();
        String targetPlayerIndex;
        String animalToSellIndex;
        boolean doneSelling = false;

        while(!doneSelling){

            if(playersPlaying.size() < 2){
                System.out.println("There are no other players left in the game to sell Animals to. Returning to main menu.");
                return -1; //Failed due to trying to sell when there are no other players left in the game
            }
            if(seller.getOwnedAnimals().size() == 0){
                System.out.println(seller.getName() + " does not have any animals left to sell. Returning to main menu.");
                return -1; //Failed due to having no animals to sell
            }

            Scanner userInput = new Scanner(System.in);

            //Put in while loop to be able to keep selling
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
            //2 players
            // [1] Buyer
            // [2] Main menu
            while(!(safeIntInput(1, playersPlaying.size(), targetPlayerIndex = userInput.next()) == 1)){
                //Break when the input is within a valid range and is a Number
            }

            if(Integer.valueOf(targetPlayerIndex) == playersPlaying.size()){
                System.out.println(seller.getName() + " returned back to the main menu.");
                return 1; //Exited selling menu by will
            }

            Player buyer = buyers.get(Integer.valueOf(targetPlayerIndex)-1);

            counter = 0;
            for(Player player: playersPlaying){
                if(player.equals(buyer)){
                    buyersIndex = counter;
                }
                counter += 1;
            }

            System.out.println("What animal does " + seller.getName() + " wish to sell to: " + buyer.getName() + " (Funds: "
                    + buyer.getAmountOfMoney() + " coins)" + "?");
            counter = 1;
            for(Animal sellersAnimals: seller.getOwnedAnimals()){
                System.out.println("[" + counter + "] " + sellersAnimals.getInfo() + " Sells for: " + sellersAnimals.getSellsFor() + " coins");
                counter += 1;
            }
            System.out.println("[" + counter + "]" + " Back to main menu");

            while(!(safeIntInput(1, seller.getOwnedAnimals().size()+1, animalToSellIndex = userInput.next()) == 1)){
                //Break when the input is within a valid range and is a Number
            }
            if(Integer.valueOf(animalToSellIndex) == seller.getOwnedAnimals().size()+1){
                System.out.println(seller.getName() + " returned back to the main menu.");
                return 1; //Exited selling menu by will
            }
            Animal animalBeingSold = seller.getOwnedAnimals().get(Integer.valueOf(animalToSellIndex)-1);

            if(animalBeingSold.getSellsFor() > buyer.getAmountOfMoney()){
                System.out.println(buyer.getName() + " cannot afford " + animalBeingSold.getInfo() + "! (Needed: " + animalBeingSold.getSellsFor() +
                        " coins, has only " + buyer.getAmountOfMoney() + " coins)");
                System.out.println("Returning back to main menu.");
                return -1;  //A transaction failed due to insufficient funds, aborting selling operations
            }
            System.out.println(buyer.getName() + " bought " + animalBeingSold.getInfo() + " for "
                    + animalBeingSold.getSellsFor() + " coins from " + seller.getName() + ".");

            buyer.pay(animalBeingSold.getSellsFor()); //buyer pays
            seller.getPaid(animalBeingSold.getSellsFor()); //Seller gets paid

            if(sellersIndex > buyersIndex){
                //An animal that is sold "downwards" in the turn order, should age and decay to account for aging/decaying during a round
                //as Decay and Aging occurs at the end of a Round, however - this would be skipped if sold downwards
                animalBeingSold.age();
                if(animalBeingSold.isAlive()){
                    animalBeingSold.decay(currentRound);
                }
            }

            buyer.addToOwnedAnimals(animalBeingSold);
            seller.getOwnedAnimals().remove(animalBeingSold);

            counter = 1;
            buyersIndex = 0;
            sellersIndex = 0;
            buyers = new ArrayList<Player>();
            targetPlayerIndex = "";
            animalToSellIndex = "";
        }
        return 1;
    }

    public int feedAnimal(Player playerFeeding){
        int counter = 1;
        boolean finishedFeeding = false;
        Scanner userInput = new Scanner(System.in);
        String wantedAnimalToFeed;
        String wantedFood;

        if(playerFeeding.getOwnedAnimals().size() == 0){
            System.out.println(playerFeeding.getName() + " has no Animals to feed. Returning to main menu.\n");
            return -2;
        }

        while(!finishedFeeding){
            boolean foundFood = false;
            if(playerFeeding.getOwnedFood().size() == 0){
                System.out.println(playerFeeding.getName() + " has no food left to feed with. Returning to main menu.\n");
                return -2;
            }

            for(Animal ownedAnimal: playerFeeding.getOwnedAnimals()){
                System.out.println("[" + counter + "] " + ownedAnimal.getName() +
                        " the " + ownedAnimal.getClass().getSimpleName() + " (" + ownedAnimal.getGender()
                        + ") Health: " +
                        ownedAnimal.getHealth());
                System.out.print("\tIt eats: [ ");
                for(Food foodEaten: ownedAnimal.getWhatItEats()){
                    System.out.print(foodEaten.getClass().getSimpleName() + " ");
                }
                System.out.print("]\n");
                System.out.print("\tPortion size: " + ownedAnimal.getPortionSize() + " grams\n");
                counter += 1;
            }
            System.out.println("[" + counter + "] Back to Main Menu");

            System.out.println("Which animal do you wish to feed?");
            int chosenAnimal;

            while(!(safeIntInput(1, playerFeeding.getOwnedAnimals().size()+1, wantedAnimalToFeed = userInput.next()) == 1)){
                //Break when the input is within a valid range and is a Number
            }
            chosenAnimal = Integer.parseInt(wantedAnimalToFeed);

            if(chosenAnimal == (playerFeeding.getOwnedAnimals().size()+1)){
                System.out.println(playerFeeding.getName() + " returned back to the Main Menu.");
                return 1;
            }

            Animal toBeFed = playerFeeding.getOwnedAnimals().get((chosenAnimal-1));

            for(Food whatTheAnimalEats: toBeFed.getWhatItEats()){
                for(Food whatThePlayerHas: playerFeeding.getOwnedFood()){
                    if(whatThePlayerHas.getName().equals(whatTheAnimalEats.getName())){
                        foundFood = true;
                    }
                }
            }
            if(!foundFood){
                System.out.println(playerFeeding.getName() + " does not have any food that a " + toBeFed.getClassName() + " would like.");
                return -1;
            }

            System.out.println("With what do you wish to feed " + toBeFed.getName() + " the " + toBeFed.getClass().getSimpleName() +
                    "(" + toBeFed.getGender() + ", " + " Health: " + toBeFed.getHealth() + ")?");
            counter = 1;
            ArrayList<Food> acceptedFood = toBeFed.getWhatItEats();
            ArrayList<Food> filteredFood = new ArrayList<>();
            for(Food ownedItems: playerFeeding.getOwnedFood()){
                for(Food acceptedFoodItem: acceptedFood){
                    if(ownedItems.getName().equals(acceptedFoodItem.getName())){
                        filteredFood.add(ownedItems);
                    }
                }
            }
            for(Food pieceOfFood : filteredFood){
                System.out.println("[" + counter + "] " + pieceOfFood.getClass().getSimpleName() + " ( " + pieceOfFood.getGrams() + " grams left in stock )");
                counter += 1;
            }

            System.out.println("[" + (counter) + "] Back to Main");

            int chosenFoodIndex;
            while(!(safeIntInput(1, filteredFood.size()+1, wantedFood = userInput.next()) == 1)){
                //Break when the input is within a valid range and is a Number
            }
            chosenFoodIndex = Integer.parseInt(wantedFood);

            if(chosenFoodIndex == (filteredFood.size()+1)){
                System.out.println(playerFeeding.getName() + " returned back to the Main Menu.");
                return 1;
            }
            Food foodToFeedWith = filteredFood.get(chosenFoodIndex-1);
            
            for(Food foodItEats : toBeFed.getWhatItEats()){
                if(foodItEats.getName().equals(foodToFeedWith.getName())){
                    if(toBeFed.getPortionSize() > foodToFeedWith.getGrams()){
                        System.out.println("There is not enough grams of " + foodToFeedWith.getName() + " left to feed " +
                                toBeFed.getName() + " the " + toBeFed.getClassName() + "(" + toBeFed.getGender() + ") Health: " +
                                toBeFed.getHealth() + " - (" + foodToFeedWith.getGrams() +
                                " grams left, needs " + toBeFed.getPortionSize() + " grams per meal)");
                    }
                    else{
                        foundFood = true;
                        int resultCode = toBeFed.eat(toBeFed.getPortionSize(),foodToFeedWith);
                        if(resultCode == 1){ //The animal liked the food
                            System.out.println(toBeFed.getName() + " the " + toBeFed.getClass().getSimpleName() + "(" + toBeFed.getGender() +")" +
                                    " happily eats the " + foodToFeedWith.getName() + "!");
                        }
                        else if(resultCode == -3){ //Mystery meat that did not seem appealing..
                            System.out.println(toBeFed.getName() + " the " + toBeFed.getClass().getSimpleName() + "(" + toBeFed.getGender() + ")" +
                                    " seems to think there's something funny with the " + foodToFeedWith.getClass().getSimpleName() +"..");
                        }
                        break;
                    }
                }
            }
            int indexToRemove = 0;
            boolean shouldRemoveFood = false;
            for(Food playersFood : playerFeeding.getOwnedFood()){
                if(playersFood.getGrams() == 0){
                    shouldRemoveFood = true;
                    break;
                }
                indexToRemove += 1;
            }
            if(shouldRemoveFood){
                playerFeeding.getOwnedFood().remove(indexToRemove);
            }
            counter = 1;
        }
        return -1;
    }

    public int getPlayers(){
        return this.players;
    }

    public void setPlayers(int players){
        this.players = players;
    }
    public int getCurrentPlayer(){
        return this.currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    public int getCurrentRound(){
        return this.currentRound;
    }

    public void setCurrentRound(int currentRound){
        this.currentRound = currentRound;
    }

    public int getRounds(){
        return this.rounds;
    }

    public void setRounds(int rounds){
        this.rounds = rounds;
    }

    public ArrayList<Player> getPlayersPlaying(){
        return this.playersPlaying;
    }

    public void setPlayersPlaying(ArrayList<Player> playersPlaying){
        this.playersPlaying = playersPlaying;
    }

    public boolean getShowedMenu(){
        return this.showedMenu;
    }

    public void setShowedMenu(boolean showedMenu){
        this.showedMenu = showedMenu;
    }

    public void saveGame(String filePath) throws FileNotFoundException{
        Scanner userInput = new Scanner(System.in);
        try{
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            System.out.println("Successfully saved game to: " + filePath);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadGame(String filePath) throws FileNotFoundException{
        boolean holdLoop = true;
        while(holdLoop){
            FileInputStream fileIn = new FileInputStream(filePath);
            try (ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
                Object obj = objectIn.readObject();
                Game loadedGame = (Game) obj;
                this.setCurrentPlayer(loadedGame.getCurrentPlayer());
                this.setCurrentRound(loadedGame.getCurrentRound());
                this.setPlayers(loadedGame.getPlayers());
                this.setRounds(loadedGame.getRounds());
                this.setPlayersPlaying(loadedGame.getPlayersPlaying());
                this.setShowedMenu(loadedGame.getShowedMenu());
                System.out.println("Successfully loaded save game from: " + filePath);
                if(!this.getShowedMenu()){
                    printAnimals(true);
                }
                break;
            } catch (Exception ex) {
                break; //Stream closed
            }
        }
    }


    public void printAnimals(boolean loadedGame) {
        System.out.println("Round " + currentRound + ", " + playersPlaying.get(currentPlayer).getName() + "'s turn.");

        playersPlaying.get(currentPlayer).announceDeaths(currentRound);
        if (loadedGame) {
            for (String printedDeath : playersPlaying.get(currentPlayer).getSavedDeathList()) {
                System.out.println(printedDeath);
            }
        }

        System.out.print(playersPlaying.get(currentPlayer).getName() + "'s Owned Animals: \n");
        if (playersPlaying.get(currentPlayer).getShouldBeRemoved().size() > 0) {
            for (int i = playersPlaying.get(currentPlayer).getShouldBeRemoved().size() - 1; i > -1; i--) {
                Animal toRemove = playersPlaying.get(currentPlayer).getShouldBeRemoved().get(i);
                playersPlaying.get(currentPlayer).getOwnedAnimals().remove(toRemove);
                playersPlaying.get(currentPlayer).getShouldBeRemoved().remove(i);
            }
        }

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

    public void runGame() {
        Store ourStore = new Store();
        System.out.println("Welcome to the Raising your Animal Game.");
        Scanner gameMenuScanner = getMyScanner();
        boolean playerGotEliminated = false;
        boolean keepAskingForSaveOrLoad = true;
        String filePathtoSaveOrLoad = "";

        while (rounds > 0) {
            if (!showedMenu) {
                playerGotEliminated = false;
                printAnimals(false);
            }

            System.out.println("\n" + playersPlaying.get(currentPlayer).getName() + "'s Funds: " + playersPlaying.get(currentPlayer).getAmountOfMoney()
                    + "\nChoose: [1] Buy an Animal from Store, [2] Sell an Animal to Store, [3] Feed your animals, [4] Breed your animals, [5] Buy Food,\n " +
                    "[6] Sell Animal to Other Player" +
                    ", [7] Buy Animal from Other Player [8] Save game and Exit [9] Load game");
            String gameMenuInput = gameMenuScanner.next();
            switch (gameMenuInput) {
                case "1":
                    ourStore.buyAnimal(playersPlaying.get(currentPlayer));
                    playersPlaying.get(currentPlayer).setTurnIsOver(true);
                    break;
                case "2":
                    ourStore.sellAnimal(playersPlaying.get(currentPlayer));
                    playersPlaying.get(currentPlayer).setTurnIsOver(true);
                    break;
                case "3":
                    feedAnimal(playersPlaying.get(currentPlayer));
                    playersPlaying.get(currentPlayer).setTurnIsOver(true);
                    break;
                case "4":
                    breedAnimal(playersPlaying.get(currentPlayer));
                    playersPlaying.get(currentPlayer).setTurnIsOver(true);
                    break;
                case "5":
                    ourStore.buyFood(playersPlaying.get(currentPlayer));
                    playersPlaying.get(currentPlayer).setTurnIsOver(true);
                    break;
                case "6":
                    sellToOtherPlayer(playersPlaying.get(currentPlayer));
                    playersPlaying.get(currentPlayer).setTurnIsOver(true);
                    break;
                case "7":
                    buyFromOtherPlayer(playersPlaying.get(currentPlayer));
                    playersPlaying.get(currentPlayer).setTurnIsOver(true);
                    break;
                case "8":
                    Scanner saveInput = new Scanner(System.in);
                    filePathtoSaveOrLoad = "";
                    keepAskingForSaveOrLoad = true;
                    boolean check = false;
                    int returnCode = 0;
                    while(keepAskingForSaveOrLoad){
                        System.out.println("Please write the System path you'd like to save your game to (Exit to abort - Case insensitive): ");
                        try{
                            //While the returnCode from forceValidSavingPath is not 1
                            while(!((returnCode = (forceValidSavingPath(filePathtoSaveOrLoad = saveInput.nextLine(),
                                    this))) == 1)
                            ){

                                //Breaks when the input is within a valid range and is a Number
                                if(!filePathtoSaveOrLoad.toLowerCase().equals("exit")){
                                    if(returnCode == 2){
                                        System.out.println("User chose to not overwrite file at: " + filePathtoSaveOrLoad);
                                    }
                                    System.out.println("Please write a different System path " +
                                            "you'd like to save your game to " +
                                            "(Exit to abort - Case insensitive): "); //In case of not wanting to overwrite the filepath
                                }
                                //the user is still asked for another input
                                if(returnCode == -5){
                                    System.out.println("You are not allowed to save a game without a name, " +
                                            "the filepath cannot end in '\\\\', please try again.");
                                }
                                if(returnCode == -6){
                                    System.out.println("Could not create a directory with the file path of: " + filePathtoSaveOrLoad);
                                }
                            }
                            if(!filePathtoSaveOrLoad.toLowerCase().equals("exit")){
                                saveGame(filePathtoSaveOrLoad);
                            }

                            keepAskingForSaveOrLoad = false; //passed with a valid saving path
                        }
                        catch(Exception e){
                            System.out.println("Error in saving file: " + e.getMessage());
                        }
                    }
                    break;
                case "9":
                    Scanner loadInput = new Scanner(System.in);
                    filePathtoSaveOrLoad = "";
                    keepAskingForSaveOrLoad = true;
                    returnCode = 0;
                    while(keepAskingForSaveOrLoad){
                        System.out.println("Please write the System path you'd like to load a game from (Exit to abort - Case insensitive): ");
                        try{
                            while(!((returnCode = forceValidLoadingPath(filePathtoSaveOrLoad = loadInput.next())) == 1)){
                                //Breaks when the input is within a valid range and is a Number
                                if(returnCode == -1){
                                    System.out.println("Could not find find a saveFile to load at the path of: " + filePathtoSaveOrLoad + ", please " +
                                            "specify another one (Exit to abort - Case insensitive): ");
                                }
                            }
                            if(filePathtoSaveOrLoad.toLowerCase().equals("exit")){
                                System.out.println("User chose to abort loading a save game.");
                                break;
                            }

                            loadGame(filePathtoSaveOrLoad);
                            keepAskingForSaveOrLoad = false; //passed with a valid loading name
                        }
                        catch(Exception e){
                            System.out.println("Error in loading file: " + e.getMessage());
                        }
                    }
                    break;
                default:
                    System.out.println("That input was not one of the valid options. Please try again.");
            }
            showedMenu = true;
            if (playersPlaying.get(currentPlayer).getTurnIsOver()) {
                playersPlaying.get(currentPlayer).setTurnIsOver(false);
                showedMenu = false;
                for (Animal eachPlayerAnimal : playersPlaying.get(currentPlayer).getOwnedAnimals()) {
                    if (eachPlayerAnimal.isAlive() && !eachPlayerAnimal.hasDecayedThisRound()) {
                        eachPlayerAnimal.decay(currentRound);
                    }
                    if (eachPlayerAnimal.isAlive() && !eachPlayerAnimal.hasDecayedThisRound()) {
                        eachPlayerAnimal.age();
                    }
                    if (eachPlayerAnimal.isAlive() && !eachPlayerAnimal.hasDecayedThisRound()) {
                        eachPlayerAnimal.chanseForDisease(playersPlaying.get(currentPlayer));
                    }
                    eachPlayerAnimal.setDecayedThisRound(true);
                }
                for (int i = 0; i < playersPlaying.get(currentPlayer).getOwnedAnimals().size(); i++) {
                    if (!playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).isAlive()) {
                        //Died of starvation or Old Age or Disease
                        playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).setPerishedAtRound(currentRound);
                        playersPlaying.get(currentPlayer).addDeathAnnouncement(currentRound,
                                playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getName()
                                        + " the " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getClassName()
                                        + " (" + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getGender() + ")"
                                        + " perished at the game round of "
                                        + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getPerishedAtRound()
                                        + ", died of " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getCauseOfDeath()
                                        + ", became : " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getAge()
                                        + " years old. Rest in peace, " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getName());
                        playersPlaying.get(currentPlayer).addToPlayerDeathList(playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getName()
                                + " the " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getClassName()
                                + " (" + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getGender() + ")"
                                + " perished at the game round of "
                                + currentRound
                                + ", died of " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getCauseOfDeath()
                                + ", became : " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getAge()
                                + " years old. Rest in peace, " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getName());
                        playersPlaying.get(currentPlayer).getShouldBeRemoved().add(playersPlaying.get(currentPlayer).getOwnedAnimals().get(i));

                    }
                }

                //Think of way to get lowest price of both animal and Food that is Smooth
                if (playersPlaying.get(currentPlayer).getAmountOfMoney() < 10 &&
                        playersPlaying.get(currentPlayer).getOwnedAnimals().size() == 0) {
                    System.out.println(playersPlaying.get(currentPlayer).getName() + " was eliminated at Round: " + currentRound + " due to not having" +
                            " enough money to buy anything and no animals left.");
                    playersPlaying.remove(currentPlayer);

                    if(playersPlaying.size() > 1){
                        currentPlayer -= 1;
                    }
                    playerGotEliminated = true;
                }
                if (playersPlaying.size() == 0) {
                    System.out.println("Game is over. No players remaining.");
                    break;
                }

                if (currentPlayer == playersPlaying.size() - 1 && playerGotEliminated){ //Compensate to balance out eliminated player
                    rounds += 1;
                    currentRound -= 1;
                }
                if (currentPlayer == playersPlaying.size() - 1) { //Round is advanced to the next one
                    rounds -= 1;
                    currentRound += 1;
                    for(Player players: playersPlaying){
                        for(Animal eachPlayerAnimal : players.getOwnedAnimals()){
                            eachPlayerAnimal.setDecayedThisRound(false);
                        }
                        players.purgeSavedDeathList(currentRound-2);
                    }
                }
                if (currentPlayer < playersPlaying.size() - 1) {
                    currentPlayer += 1;
                } else {
                    currentPlayer = 0;
                }
            }
        }

        if (playersPlaying.size() > 0){
            if (playersPlaying.get(currentPlayer).getShouldBeRemoved().size() > 0) {
                for (int i = playersPlaying.get(currentPlayer).getShouldBeRemoved().size() - 1; i > -1; i--) {
                    Animal toRemove = playersPlaying.get(currentPlayer).getShouldBeRemoved().get(i);
                    playersPlaying.get(currentPlayer).getOwnedAnimals().remove(toRemove);
                    playersPlaying.get(currentPlayer).getShouldBeRemoved().remove(i);
                }
            }
        }
        ArrayList<Integer> moneyOfPlayers = new ArrayList<>();
        ArrayList<String> namesOfPlayers = new ArrayList<>();
        ArrayList<String> highScore = new ArrayList<>();
        ArrayList<String> winner = new ArrayList<>();

        for(Player player : playersPlaying){
            for(int i = player.getOwnedAnimals().size()-1; i > -1; i--){
                System.out.println(player.getName() + " sold off " + player.getOwnedAnimals().get(i).getInfo()
                        + " for " + player.getOwnedAnimals().get(i).getSellsFor() + " coins.");
                player.getPaid(player.getOwnedAnimals().get(i).getSellsFor());
                player.getOwnedAnimals().remove(i);
            }
            moneyOfPlayers.add(player.getAmountOfMoney());
            namesOfPlayers.add(player.getName());
        }
        Collections.sort(moneyOfPlayers);
        int spot = 1;
        for(int i = moneyOfPlayers.size()-1; i > -1; i--){
            for(Player player: playersPlaying){
                if(player.getAmountOfMoney() == moneyOfPlayers.get(i) && !player.getAddedToHighScore()){
                    highScore.add("[" + spot + "] - " + player.getName() + " : " + player.getAmountOfMoney() + " coins.");
                    if(spot == 1){
                        winner.add("The winner is: " + player.getName() + " with a whopping amount of " + player.getAmountOfMoney() + " coins!");
                    }
                    player.setAddedToHighScore(true);
                }
            }
            spot += 1;
        }

        System.out.println("At the end of the game, here are the results: ");
        for(String highScoreSpots : highScore){
            System.out.println(highScoreSpots);
        }
        if(winner.size() > 0){
            System.out.println(winner.get(0));
        }
        else{
            System.out.println("There were no players who made it to the end of the game.");
        }

    }
}


/*
        Add Changelog //Not started - will do after Refactoring and all commits have been done - Will implement on next project from Start

        Refactor
*/