package gameComponents;

import Animals.Animal;

import java.util.ArrayList;
import java.util.Scanner;

public class Game extends utilityFunctions{
    private int rounds = 0;
    private int players = 0;

    private String wantedRoundsInput = "";
    private String wantedPlayersInput = "";

    private int currentRound = 1;
    private int currentPlayer = 0;
    private boolean showedMenu = false;


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
            System.out.print("Please write the name for player " + (i+1) + ": ");
            String name = userInput.next();
            playersPlaying.add(new Player(name, 1000));
        }

    }

    public int feedAnimal(Player playerFeeding){
        //TO DO - Implement Selection/Finalize Error handling in processing
        int counter = 1;
        boolean finishedFeeding = false;
        Scanner userInput = new Scanner(System.in);
        String wantedAnimalToFeed;
        String wantedFood;

        if(playerFeeding.getOwnedAnimals().size() == 0){
            System.out.println(playerFeeding.getName() + " has no Animals to feed. Returning to main menu.");
            return -2;
        }


        while(!finishedFeeding){
            if(playerFeeding.getOwnedFood().size() == 0){
                System.out.println(playerFeeding.getName() + " has no food left to feed with. Returning to main menu.");
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
            System.out.println("With what do you wish to feed " + toBeFed.getName() + " the " + toBeFed.getClass().getSimpleName() +
                    "(" + toBeFed.getGender() + ", " + " Health: " + toBeFed.getHealth() + ")?");
            counter = 1;
            for(Food pieceOfFood : playerFeeding.getOwnedFood()){
                System.out.println("[" + counter + "] " + pieceOfFood.getClass().getSimpleName() + " ( " + pieceOfFood.getGrams() + " grams left in stock )");
                counter += 1;
            }

            System.out.println("[" + (counter) + "] Back to Main Menu");

            int chosenFoodIndex;
            while(!(safeIntInput(1, playerFeeding.getOwnedAnimals().size()+1, wantedFood = userInput.next()) == 1)){
                //Break when the input is within a valid range and is a Number
            }
            chosenFoodIndex = Integer.parseInt(wantedFood);

            if(chosenFoodIndex == (playerFeeding.getOwnedFood().size()+1)){
                System.out.println(playerFeeding.getName() + " returned back to the Main Menu.");
                return 1;
            }
            Food foodToFeedWith = playerFeeding.getOwnedFood().get(chosenFoodIndex-1);
            for(Food foodItEats : toBeFed.getWhatItEats()){
                if(foodItEats.getName().equals(foodToFeedWith.getName())){
                    if(toBeFed.getPortionSize() > foodToFeedWith.getGrams()){
                        System.out.println("There is not enough grams of " + foodToFeedWith.getName() + " left to feed " +
                                toBeFed.getName() + " the " + toBeFed.getClassName() + "(" + toBeFed.getGender() + ") Health: " +
                                toBeFed.getHealth() + " (" + foodToFeedWith.getGrams() + " left, needs " + toBeFed.getPortionSize() + " grams per meal.)");
                    }
                    else{
                        int resultCode = toBeFed.eat(toBeFed.getPortionSize(),foodToFeedWith);
                        if(resultCode == 1){ //The animal liked the food
                            System.out.println(toBeFed.getName() + " the " + toBeFed.getClass().getSimpleName() + "(" + toBeFed.getGender() +")" +
                                    " happily eats the " + foodToFeedWith.getName() + "!");
                        }
                        else if(resultCode == -3){ //Mystery meat that did not seem appealing..
                            System.out.println(toBeFed.getName() + " the " + toBeFed.getClass().getSimpleName() + "(" + toBeFed.getGender() + ")" +
                                    " seems to think there's something funny with the " + foodToFeedWith.getClass().getSimpleName() +"..");
                        }
                        else if(resultCode == -2){ //The Animal did not like the food
                            System.out.println(toBeFed.getName() + " the " + toBeFed.getClass().getSimpleName() + "(" + toBeFed.getGender() +")" +
                                    " doesn't seem to like the " + foodToFeedWith.getName() + "..");
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

    public void runGame(){
        Store ourStore = new Store();
        System.out.println("Welcome to the Raising your Animal Game.");
        Scanner gameMenyScanner = getMyScanner();
        while(rounds > 0){
            if(!showedMenu){
                System.out.println("Round " + currentRound + ", " + playersPlaying.get(currentPlayer).getName() + "'s turn.");
                System.out.print(playersPlaying.get(currentPlayer).getName() + "'s Owned Animals: \n");
                for(Animal ownedAnimal: playersPlaying.get(currentPlayer).getOwnedAnimals()){
                    System.out.print(ownedAnimal.getName() + " the " + ownedAnimal.getClass().getSimpleName() + "(" + ownedAnimal.getGender() + "), who is at: "
                            + ownedAnimal.getHealth() + " health (Lost " + ownedAnimal.getLostHealth() + " health last round, was at: " +
                            ownedAnimal.getWasAtHealth() + " health.)\n");
                }
                System.out.print(playersPlaying.get(currentPlayer).getName() + "'s Owned Food: \n");
                for(Food ownedFood: playersPlaying.get(currentPlayer).getOwnedFood()){
                    System.out.print(ownedFood.getGrams() + " Grams of " + ownedFood.getClass().getSimpleName()+"\n");
                }
            }

            System.out.println("\n" + playersPlaying.get(currentPlayer).getName() + "'s Funds: " + playersPlaying.get(currentPlayer).getAmountOfMoney()
             + " - Press: [1] Buy an Animal, [2] Sell an Animal, [3] Feed your animals, [4] Breed your animals, [5] Buy Food, [6] Save Game and Exit");
            String gameMenyInput = gameMenyScanner.next();
            switch(gameMenyInput){
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
                    //TO DO - BREED ANIMALS
                    break;
                case "5":
                    ourStore.buyFood(playersPlaying.get(currentPlayer));
                    playersPlaying.get(currentPlayer).setTurnIsOver(true); //Tried to enter shop with no funds
                    break;
                case "6":
                    //TO DO - SAVE GAME AND EXIT
                    break;
                default:
                    System.out.println("That input was not one of the valid options. Please try again.");
            }
            showedMenu = true;
            if(playersPlaying.get(currentPlayer).getTurnIsOver()){
                playersPlaying.get(currentPlayer).setTurnIsOver(false);
                showedMenu = false;
                for(Animal eachPlayerAnimal: playersPlaying.get(currentPlayer).getOwnedAnimals()){
                    eachPlayerAnimal.decay();
                }
                if(currentPlayer == playersPlaying.size()-1){
                    rounds -= 1;
                    currentRound += 1;
                }
                if(currentPlayer < playersPlaying.size() - 1){
                    currentPlayer += 1;
                }
                else{
                    currentPlayer = 0;
                }
            }
        }
        System.out.println("At the end of the game, here are the results: ");
        for(Player player : playersPlaying){
            System.out.println(player.getName() + " had " + player.getAmountOfMoney() + " coins.");
        }
    }
}
