package gameComponents;

import Animals.Animal;

import java.util.ArrayList;
import java.util.Scanner;

public class utilityFunctions {
    private Scanner myScanner = new Scanner(System.in);

    public Scanner getMyScanner(){
        return this.myScanner;
    }
    public int safeIntInput(int lowerBoundary, int upperBoundary, String input) throws RuntimeException{
        int check;
        try{
            check = Integer.valueOf(input);
            if(check < lowerBoundary || check > upperBoundary){
                throw new RuntimeException("Out of accepted boundary - [LOWER: " + lowerBoundary + " UPPER: " + upperBoundary +
                        " GIVEN: " + check + "]");
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

    public void printSellAnimalMenu(Player seller){
        ArrayList<Animal> animalsToSell = seller.getOwnedAnimals();
        int shopCounter = 1;
        System.out.println("Which animal would " + seller.getName() + " like to sell?");
        for(Animal animal : animalsToSell){
            System.out.println("[" + shopCounter + "] " + animal.getName() + " the " + animal.getGender() + " " + animal.getClass().getSimpleName() +
                    " - Health: " + animal.getHealth() + " - Sells for: " + animal.getSellsFor());
            shopCounter += 1;
        }
        System.out.println("[" + (animalsToSell.size() + 1) + "] Exit shop");
    }
}
