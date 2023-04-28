package game;



import java.io.Serializable;

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
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
     

}
