


import java.io.Serializable;

/**
 * The Player class defines a player object with x and y coordinates and a name.
 */
public class Player implements Serializable{
    private int x = 0;
    private int y = 0;
    private String name = "";
    private long serialNum = 0;
    public Player(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }
    /**
     * The function returns the value of the variable "x".
     * 
     * @return The method `getX()` is returning the value of the variable `x`.
     */
    public int getX() {
        return x;
    }
    /**
     * This is a Java function that sets the value of a variable "x" to a given integer value.
     * 
     * @param x x is a parameter of type int that represents the value to be set for the instance
     * variable x in the class. The "this" keyword is used to refer to the current object instance.
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * The above function returns the value of the variable "y".
     * 
     * @return The method `getY()` is returning the value of the variable `y`.
     */
    public int getY() {
        return y;
    }
    /**
     * This function sets the value of the "y" variable in a Java class.
     * 
     * @param y The parameter "y" is an integer value that is used to set the value of the instance
     * variable "y" in the current object. The "setY" method takes an integer value as input and
     * assigns it to the "y" variable in the object.
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     * The function returns the name of an object.
     * 
     * @return The method `getName()` is returning a `String` value, which is the value of the variable
     * `name`.
     */
    public String getName() {
        return name;
    }
    /**
     * This function sets the name of an object.
     * 
     * @param name The parameter "name" is a String type variable that represents the name of an object
     * or entity. The method "setName" takes a String argument and sets the value of the instance
     * variable "name" to the value of the argument passed to the method.
     */
    public void setName(String name) {
        this.name = name;
    }
     

}
