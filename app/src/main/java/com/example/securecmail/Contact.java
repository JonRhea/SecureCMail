package com.example.securecmail;

/**
 * @author Jon Rhea
 * @since
 */
public class Contact {

    //initialize contact variables
    private String uniqueName;
    private String fullName;
    private String firstEmailID;
    private String secondEmailID;


    /**
     * Constructs a contact
     * @param uName The unique name of a contact
     * @param fName The full name of a contact
     * @param id1 The first email ID of a contact
     * @param id2 The second email ID of a contact
     */
    public Contact(String uName, String fName, String id1, String id2){
        uniqueName = uName;
        fullName = fName;
        firstEmailID = id1;
        secondEmailID = id2;
    }//end constructor

    /**
     * Gets the unique name of a contact
     * @return The unique name of a contact
     */
    public String getUniqueName() {
        return uniqueName;
    }//end getUniqueName

    /**
     * Sets the unique name of a contact
     * @param uniqueName The unique name to set
     */
    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }//end setUniqueName

    /**
     * Gets the full name of a contact
     * @return The full name of a contact
     */
    public String getFullName() {
        return fullName;
    }//end getFullName

    /**
     * Sets the full name of a contact
     * @param fullName The full name to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }//end setFullName

    /**
     * Gets the first email ID of a contact
     * @return The first email of of a contact
     */
    public String getFirstEmailID() {
        return firstEmailID;
    }//end getFirstEmailID

    /**
     * Sets the second email ID of a contact
     * @param firstEmailID
     */
    public void setFirstEmailID(String firstEmailID) {
        this.firstEmailID = firstEmailID;
    }//end setFirstEmailID

    /**
     * Gets the second email ID of a contact
     * @return The second email of of a contact
     */
    public String getSecondEmailID() {
        return secondEmailID;
    }//end getSecondEmailID

    /**
     * Sets the second email ID of a contact
     * @param secondEmailID The second email of a contact
     */
    public void setSecondEmailID(String secondEmailID) {
        this.secondEmailID = secondEmailID;
    }//end setSecondEmailID

}//end class
