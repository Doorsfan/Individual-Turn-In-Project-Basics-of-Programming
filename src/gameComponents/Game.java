package gameComponents;

import Animals.Animal;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import Animals.*;

public class Game extends utilityFunctions{
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
                        int genderChanse = random.ints(1,3).findFirst().getAsInt();
                        String gender;
                        if(genderChanse == 1){
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
            ArrayList<Food> filteredFood = new ArrayList<Food>();
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

    public void runGame(){
        Store ourStore = new Store();
        System.out.println("Welcome to the Raising your Animal Game.");
        Scanner gameMenyScanner = getMyScanner();
        while(rounds > 0){
            if(!showedMenu){
                System.out.println("Round " + currentRound + ", " + playersPlaying.get(currentPlayer).getName() + "'s turn.");
                playersPlaying.get(currentPlayer).announceDeaths(currentRound);
                System.out.print(playersPlaying.get(currentPlayer).getName() + "'s Owned Animals: \n");

                ArrayList<Animal> shouldBeRemoved = new ArrayList<>();
                for (int i = 0; i < playersPlaying.get(currentPlayer).getOwnedAnimals().size(); i++) {
                    if(playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getHealth() < 0
                        || playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getAge() >
                            playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getMaxAge()){
                        //Died of starvation or Old Age
                        playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).setPerishedAtRound(currentRound);
                        playersPlaying.get(currentPlayer).addDeathAnnouncement(currentRound,
                                playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getName()
                                        + " the " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getClassName()
                                        + " perished at the game round of "
                                        + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getPerishedAtRound()
                                        + ", died of " +
                                        (playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getHealth() > 0 ? "Old Age" : "Starvation")
                                        + ", became : " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getAge()
                                        + " years old. Rest in peace, " + playersPlaying.get(currentPlayer).getOwnedAnimals().get(i).getName());
                        shouldBeRemoved.add(playersPlaying.get(currentPlayer).getOwnedAnimals().get(i));
                    }
                }
                if (shouldBeRemoved.size() > 0) {
                    for (int i = shouldBeRemoved.size()-1; i > -1; i--) {
                        Animal toRemove = shouldBeRemoved.get(i);
                        playersPlaying.get(currentPlayer).getOwnedAnimals().remove(toRemove);
                        shouldBeRemoved.remove(i);
                    }
                }

                for(Animal ownedAnimal: playersPlaying.get(currentPlayer).getOwnedAnimals()){
                    System.out.print(ownedAnimal.getName() + " the " + ownedAnimal.getClass().getSimpleName() +
                            "(" + ownedAnimal.getGender() + "), who is at: "
                            + ownedAnimal.getHealth() + " health (Lost " + ownedAnimal.getLostHealth() + " health last round, was at: " +
                            ownedAnimal.getWasAtHealth() + " health.) (Age: " + ownedAnimal.getAge() + " of " + ownedAnimal.getMaxAge() + ")\n");
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
                    breedAnimal(playersPlaying.get(currentPlayer));
                    playersPlaying.get(currentPlayer).setTurnIsOver(true);
                    break;
                case "5":
                    ourStore.buyFood(playersPlaying.get(currentPlayer));
                    playersPlaying.get(currentPlayer).setTurnIsOver(true);
                    break;
                case "6":
                    //TO DO - SAVE GAME AND EXIT
                    break;
                default:
                    System.out.println("That input was not one of the valid options. Please try again.");
            }
            showedMenu = true;
            if(playersPlaying.get(currentPlayer).getTurnIsOver()) {
                playersPlaying.get(currentPlayer).setTurnIsOver(false);
                showedMenu = false;
                for (Animal eachPlayerAnimal : playersPlaying.get(currentPlayer).getOwnedAnimals()) {
                    eachPlayerAnimal.decay();
                    eachPlayerAnimal.age();
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
