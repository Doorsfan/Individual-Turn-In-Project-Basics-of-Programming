package gameComponents;

import Animals.Animal;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private int rounds = 0;
    private int players = 0;
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

        boolean menuSetup = true;
        boolean validAmountOfRounds = false;
        while(menuSetup) {
            try {
                if(!validAmountOfRounds){
                    System.out.print("Rounds: ");
                    rounds = Integer.valueOf(userInput.next());
                }
                if (rounds > 30 || rounds < 5 && !validAmountOfRounds) {
                    throw new RuntimeException("You are only allowed to choose between 5-30 rounds.");
                }
                validAmountOfRounds = true;
                System.out.print("Players: ");
                players = Integer.valueOf(userInput.next());
                if (players > 4 || players < 1) {
                    throw new IllegalArgumentException("You are only allowed to choose between 1-4 players.");
                }
                for(int i = 0; i < players; i++){
                    System.out.print("Please write the name for player " + (i+1) + ": ");
                    String name = userInput.next();
                    playersPlaying.add(new Player(name, 1000));
                }
                menuSetup = false;
            } catch (Exception e) {
                if(e instanceof RuntimeException){
                    if(e.getMessage().contains("For input string")){
                        System.out.println("You are not allowed to put in letters instead of a Number. Please try again.");
                    }
                    else{
                        System.out.println(e.getMessage());
                    }
                }
                rounds = 0;
                players = 0;
            }
        }
    }

    public int feedAnimal(Player playerFeeding){
        //TO DO - Implement Selection/Finalize Error handling in processing
        int counter = 1;
        boolean finishedFeeding = false;
        Scanner input = new Scanner(System.in);
        if(playerFeeding.getOwnedFood().size() == 0){
            System.out.println(playerFeeding.getName() + " has no food to feed with. Returning to main menu.");
            return -2;
        }
        System.out.println("Which one of your animals would you like to feed?");

        while(!finishedFeeding){
            for(Animal ownedAnimal: playerFeeding.getOwnedAnimals()){
                System.out.println("[" + counter + "] " + ownedAnimal.getName() + " the " + ownedAnimal.getClass().getSimpleName() + " (" + ownedAnimal.getGender()
                        + ") Health: " +
                        ownedAnimal.getHealth());
                System.out.print("\tIt eats: [ ");
                for(Food foodEaten: ownedAnimal.getWhatItEats()){
                    System.out.print(foodEaten.getClass().getSimpleName() + " ");
                }
                System.out.print("]\n");
                counter += 1;
            }

            System.out.println("Which animal do you wish to feed?");
            int chosenAnimal = Integer.valueOf(input.next());
            Animal toBeFed = playerFeeding.getOwnedAnimals().get((chosenAnimal-1));
            System.out.println("With what do you wish to feed " + toBeFed.getName() + " the " + toBeFed.getClass().getSimpleName() +
                    "(" + toBeFed.getGender() + ", " + " Health: " + toBeFed.getHealth() + ")?");
            //TO DO

            counter = 1;
        }

        return -1;
    }

    public void runGame(){
        Store ourStore = new Store();
        System.out.println("Welcome to the Raising your Animal Game.");
        Scanner gameMenyScanner = new Scanner(System.in);
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
                    System.out.print(ownedFood.getGrams() + " Grams of " + ownedFood.getClass().getSimpleName());
                }
            }

            System.out.println("\n" + playersPlaying.get(currentPlayer).getName() + "'s Funds: " + playersPlaying.get(currentPlayer).getAmountOfMoney()
             + " - Press: [1] Buy an Animal, [2] Sell an Animal, [3] Feed your animals, [4] Breed your animals, [5] Buy Food, [6] Save Game and Exit");
            String gameMenyInput = gameMenyScanner.next();
            switch(gameMenyInput){
                case "1":
                    ourStore.buyAnimal(playersPlaying.get(currentPlayer));
                    playersPlaying.get(currentPlayer).setTurnIsOver(true); //Tried to enter shop with no funds
                    break;
                case "2":
                    ourStore.sellAnimal(playersPlaying.get(currentPlayer));
                    playersPlaying.get(currentPlayer).setTurnIsOver(true); //Tried to Sell animals to the shop with no animals
                    break;
                case "3":
                    //TO DO - FEED ANIMALS - ABOUT 50% DONE
                    feedAnimal(playersPlaying.get(currentPlayer));
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
