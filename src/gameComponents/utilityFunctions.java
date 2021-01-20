package gameComponents;

import Animals.Animal;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class that hosts useful methods in parsing Menu choices and Enforcing input elements
 */
public class utilityFunctions {
    private Scanner myScanner = new Scanner(System.in);

    public Scanner getMyScanner(){
        return this.myScanner;
    }

    /**
     * A method that is used in forcing a valid Filepath as a written input to where the
     * player wishes to Save their files.
     *
     * If the Method finds a File with the name already in place at the specified Filepath,
     * it will give a prompt for confirmation on wether the User wishes to overwrite the previous
     * saveFile or not.
     *
     * @param input A string, the input to sanitize and handle in regards to building a valid Filepath
     * @param savedGame The game being saved
     * @return An important status code message that updates on what kind of event occurred in the Saving process
     *      1: The method managed to verify the filePath for saving or the Input was peacefully exited with an exit input
     *      2: A saveFile with the same name of the file was already present in the specified Filepath - but the user Chose
     *          to not overwrite the file
     *      -1: A general Exception occurred in the validation process
     *      -5: The user put in a filepath that ends in \\, which should not be considered valid
     *      -6: A folder was not created along the Filepath specification
     *
     * @throws FileNotFoundException To verify that a file exists, it will be attempted to be opened in a FileOutputStream -
     * Thus, it requires that the Exception is declared as being able to be thrown by this method.
     */
    public int forceValidSavingPath(String input, Game savedGame) throws FileNotFoundException {
        if(input.toLowerCase().equals("exit")){
            System.out.println("User chose to abort saving the game.");
            return 1; //Used did not wish to save the file, should break loop at 1
        }
        try{
            try{
                String[] splitInput = input.split("\\\\"); //Splits on \\ in File dir string, every split on \\ causes an element

                int subFolders = -1;
                String pathSoFar = "";
                for(String split: splitInput){
                    if(split.equals("")){
                        subFolders += 1;
                        pathSoFar += "\\\\";
                        if(subFolders > 0){
                            File f = new File(pathSoFar);
                            if(f.mkdir()){
                                System.out.println("Created a directory with the path of: " + pathSoFar);
                            }
                            else{
                                //Could not create a directory at that path, so returns -6 to symbolize error code for this
                                return -6;
                            }
                        }

                    }
                    pathSoFar += split;
                }
                if(input.substring(input.length()-2, input.length()).equals("\\\\")){
                    return -5; //User tried to feed in Folder structure that ends with \\
                }

                if(forceValidLoadingPath(input) == 1){ //Exited or could load the file specified
                    String answer;
                    System.out.println("There seems to already exist a save game file at the path of: " + input);
                    System.out.println("Do you wish to overwrite the file at: " + input + "? (Y/N case-insensitive)");
                    while(!(forceYOrN(answer = myScanner.next()) == 1)){
                        //Break when the input is Y,y,N or n
                    }
                    if(answer.toLowerCase().equals("n")){
                        return 2; //USer chose to not overwrite the file at save path
                    }
                    else{
                        System.out.println("Overwriting save-file at path of: " + input);
                    }
                }
                else if(splitInput.length == 1 && splitInput[0].contains(":")){
                    System.out.println("You cannot specify a partition of a drive alone without a name of the Savefile. Please try again.");
                }
                else{ //Did not Exit and Failed to load a system path
                    System.out.println("Did not find a saveFile present at " + input);
                    System.out.println("If trying to save a file and it failed, please make sure the input ends " +
                            "in just the file name (such as: 'D:\\\\myExampleSave')");
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
            FileOutputStream fileOut = new FileOutputStream(input); //Does not get past when trying to open invalid drive or invalid Path
            fileOut.close();
            return 1; //Only goes through if actually successfully can open fileoutputstream to the specified Path
        }
        catch(Exception e){
            return -1;
        }
    }

    /**
     * A method that is used in forcing a valid Filepath as a written input to where the
     * player wishes to Load their files from.
     *
     * @param input A string, the input to sanitize and handle in regards to building a valid Filepath
     * @return An important status code message that updates on what kind of event occurred in the Saving process
     *      1: The method managed to verify the filePath for Loading or it Peacefully exited
     *      -1: Could not open a FileInputStream on the designated Filepath that it was given as Input
     *
     * @throws FileNotFoundException To verify that a file exists, it will be attempted to be opened in a FileOutputStream -
     * Thus, it requires that the Exception is declared as being able to be thrown by this method.
     */
    public int forceValidLoadingPath(String input) throws FileNotFoundException {
        //D:\
        if(input.toLowerCase().equals("exit")){
            return 1; //User wanted to abort loading a game
        }
        try{
            FileInputStream fileIn = new FileInputStream(input);
            return 1; //Succeeded in loading a file
        }
        catch(Exception e)
        {
            return -1; //Could not load the file
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
    public int forceYOrN(String input) throws RuntimeException{
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
     *          1: Successfully parsed and accepted as being a number within the range
     *          -1: Not accepted as being a number within the range or a number
     * @throws RuntimeException Throws a RuntimeException if the input is not within the accepted Range of Numbers
     */
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

    /**
     * A utility method that helps in printing Animals for when the player is in the Store to sell Animals
     *
     * @param seller A player object, the Player selling Animals to the Store
     */
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
