package com.example.securecmail;

import java.util.Random;

public class SecretHelper {

    /**
     * Splits a string into two sets (parts) of shares using Shamir's Secret Sharing Algorithm
     * @param message The string to split into shares
     * @return The string of shares, plus other information to help identify things like,
     * what sets of shares are in this email, is this an encrypted email at all, etc..
     */
    public static String[] encrypt(String message){

        Random rng = new Random();

        String nonce = generateNonce();
        //SecureCMail is used to identify if this email was split or not
        //1 or 2 denotes which set of shares a set has, which is important for reconstructing the secret
        //nonce added to each half. If we have multiple halves waiting to be reconstructed,
        //we can identify what the other half is if the nonce is the same in each half
        String firstSet = "SecureCMail: 1 " + nonce;
        String secondSet = "SecureCMail: 2 " + nonce;

        char[] dividedMessage = message.toCharArray();

        for(int i = 0; i < dividedMessage.length; i++){

            int randomNumber = rng.nextInt(127);
            char letter = dividedMessage[i];
            int asciiLetter = (int) letter;

            int randomInput = rng.nextInt(120);

            //create shares
            Share share1 = createShare(randomNumber, randomInput, asciiLetter);
            Share share2 = createShare(randomNumber, randomInput + 1, asciiLetter);

            //add shares to string
            firstSet += " " + share1.getInput() + "," + (share1.getOutput());
            secondSet += " " + share2.getInput()  + "," + (share2.getOutput());

        }//end for

        //used for testing
        System.out.println("first " + firstSet);
        System.out.println("second " + secondSet);

        String[] list = new String[2];
        list[0] = firstSet;
        list[1] = secondSet;

        return list;
    }//end encrypt

    /**
     * Creates a share using a first degree polynomial
     * @param random The random number used in the formula
     * @param input The input chosen for the formula
     * @param secret The secret to encode
     * @return The share created
     */
    public static Share createShare(int random, int input, int secret){

        int output = (secret + (random * input)) % 127;

        Share share = new Share(input,output);
        return share;

    }//end createShare

    /**
     * Reconstructs a string of secrets using two sets of shares
     * @param list The two sets of shares to be used in the reconstruction
     * @return The reconstructed string
     */
    public static String decode(String[] list){

        String message = "";
        String firstSet = list[0];
        String secondSet = list[1];

        String[] firstList = firstSet.split(" ");
        String [] secondList = secondSet.split(" ");

        int randomNumber = 0;
        boolean found = false;

        for(int i = 3; i < firstList.length; i++){

            String[] shareString = firstList[i].split(",");
            String[] shareString2 = secondList[i].split(",");
            int x1 = Integer.valueOf(shareString[0]);
            int y1 = Integer.valueOf(shareString[1]);

            int x2 = Integer.valueOf(shareString2[0]);
            int y2 = Integer.valueOf(shareString2[1]);

            int firstFraction = y1 * ((0 - x2)/(x1 - x2));
            int secondFraction = y2 * ((0 - x1)/(x2 - x1));

            int secretInt = ((firstFraction + secondFraction) % 127);

            //needed because modulo on negative numbers do not return it's positive equivalent
            if (secretInt < 0) {
                secretInt += 127;
            }//end if

            char letter = (char) secretInt;

            message += letter;

        }//end for
        System.out.println("decode " + message);
        return message;
    }//end decode

    /**
     * Generates a random nonce used for share identification
     * @return The random nonce
     */
    public static String generateNonce(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String nonce = "";
        Random random = new Random();

        //randomly choose letters to include in nonce
        for(int i = 0; i < 16; i++){
            int randomInt = random.nextInt(26);
            nonce += characters.charAt(randomInt);
        }//end for

        return nonce;
    }//end generateNonce
}//end class
