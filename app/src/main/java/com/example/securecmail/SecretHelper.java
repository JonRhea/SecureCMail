package com.example.securecmail;

import java.util.Random;

public class SecretHelper {



    public static String[] encrypt(String message){

        Random rng = new Random();

        int randomNumber = rng.nextInt(99);
        String firstSet = randomNumber + ":";
        String secondSet = randomNumber + ":";

        char[] dividedMessage = message.toCharArray();

        for(int i = 0; i < dividedMessage.length; i++){

            char letter = dividedMessage[i];
            int asciiLetter = (int) letter;

            Share share1 = createShare(randomNumber, 0,asciiLetter);
            Share share2 = createShare(randomNumber, 1, asciiLetter);


            firstSet += " " + share1.getInput() + "," + (share1.getOutput() + randomNumber);
            secondSet += " " + share2.getInput()  + "," + (share2.getOutput() + randomNumber);

        }//end for

        //used for testing
        System.out.println("first " + firstSet);
        System.out.println("second " + secondSet);

        String[] list = new String[2];
        list[0] = firstSet;
        list[1] = secondSet;

        return list;
    }//end encrypt


    public static Share createShare(int random, int input, int secret){

        int output = (secret + (random * input)) % 127;

        Share share = new Share(input,output);
        return share;

    }//end createShare

    public static String decode(String[] list){

        String message = "";
        String firstSet = list[0];
        String secondSet = list[1];

        String[] firstList = firstSet.split(" ");
        String [] secondList = secondSet.split(" ");

        int randomNumber = 0;
        boolean found = false;

        for(int i = 0; i < firstList.length; i++){
            if(i == 0){
                //only need the number from first set
                String[] rngString = firstList[0].split(":");
                randomNumber = Integer.valueOf(rngString[0]);
            }//end if
            else{
                String[] shareString = firstList[i].split(",");
                String[] shareString2 = secondList[i].split(",");
                int x1 = Integer.valueOf(shareString[0]);
                int y1 = Integer.valueOf(shareString[1]) - randomNumber;

                int x2 = Integer.valueOf(shareString2[0]);
                int y2 = Integer.valueOf(shareString2[1]) - randomNumber;

                int firstFraction = y1 * ((0 - x2)/(x1 - x2));
                int secondFraction = y2 * ((0 - x1)/(x2 - x1));

                int secretInt = ((firstFraction + secondFraction) % 127);
                char letter = (char) secretInt;

                message += letter;
            }//end else

        }
        System.out.println("decode " + message);
        return message;
    }//end decode
}//end class
