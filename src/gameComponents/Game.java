package gameComponents;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    int rounds = 0;
    int players = 0;
    ArrayList<Player> playersPlaying = new ArrayList<>();
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
                    playersPlaying.add(new Player(name, 100));
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

    public void runGame(){
        System.out.println("TO DO");
    }
}
