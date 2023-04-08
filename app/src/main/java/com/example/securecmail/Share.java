package com.example.securecmail;

public class Share {

    int input;
    int output;

    /**
     * Constructs a share
     * @param input The value inputted for the equation
     * @param output The output of the equation
     */
    public Share(int input, int output){

        this.input = input;
        this.output = output;
    }//end constructor

    //standard setters and getters below

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }

}//end class
