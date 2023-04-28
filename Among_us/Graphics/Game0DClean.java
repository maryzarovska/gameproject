import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.animation.*;
import java.io.*;
import java.util.*;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * AmongUSStarter with JavaFX and Threads
 * Loading imposters
 * Loading background
 * Control actors and backgrounds
 * Create many number of imposters - random controlled
 * RGB based collision
 * Collsion between two imposters
 */

public class Game0DClean extends Application {
   // Window attributes
   private Stage stage;
   private Scene scene;
   private StackPane root;

   private static String[] args;

   private final static String CREWMATE_IMAGE = "Actual_Stormtrooper.png"; // file with icon for a racer
   private final static String CREWMATE_RUNNERS = "Darth_vader_Finished.png"; // file with icon for a racer
   private final static String BACKGROUND_IMAGE = "RedGreen.png";


   //crewmate and runners
   CrewmateRacer crewmateRacer=null;
   ArrayList<CrewmateRacer> crewmateRunners=new ArrayList<>();

   //backgroundd
   MovableBackground movableBackground=null;


   //Animation timer
   AnimationTimer animTimer=null;
   private long renderCounter=0;
   boolean goUP=false,goDOWN=false,goLEFT=false,goRIGHT=false;
    Image backgroundcollision = null;
   

   // main program
   public static void main(String[] _args) {
      args = _args;
      launch(args);
   }

   // start() method, called via launch
   public void start(Stage _stage) {
      // stage seteup
      stage = _stage;
      stage.setTitle("Game2D Starter");
      stage.setOnCloseRequest(
            new EventHandler<WindowEvent>() {
               public void handle(WindowEvent evt) {
                  System.exit(0);
               }
            });

      // root pane
      root = new StackPane();

      initializeScene();

   }

   // start the game scene
   public void initializeScene() {

      //initialize crewmate
      crewmateRacer = new CrewmateRacer(true);
      for(int i=0;i<5;i++){
         CrewmateRacer cR = new CrewmateRacer(false);
         crewmateRunners.add(cR);
      }
      //add background
      movableBackground = new MovableBackground();


      //add to the root
      root.getChildren().add(movableBackground);
      root.getChildren().add(crewmateRacer);
      for(int i=0;i<crewmateRunners.size();i++){
         root.getChildren().add(crewmateRunners.get(i));
      }
      

      // display the window
      scene = new Scene(root, 800, 500);
      // scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
      stage.setScene(scene);
      stage.show();

      //KEYBOARD
      scene.setOnKeyPressed(new EventHandler<KeyEvent>()
      {
         @Override
         public void handle(KeyEvent event) {
            // TODO Auto-generated method stub
            //System.out.println("PRESSED");
            switch(event.getCode()){
                  case UP:
                  goUP=true;
                  break;
               case DOWN:
                  goDOWN=true;
                  break;
               case RIGHT:
                  goRIGHT=true;
                  break;
               case LEFT:
                  goLEFT=true;
                  break;
            }
            
         }
      });
      scene.setOnKeyReleased(new EventHandler<KeyEvent>()
      {
         @Override
         public void handle(KeyEvent event) {
            switch(event.getCode()){
               case UP:
                  goUP=false;
                  break;
               case DOWN:
                  goDOWN=false;
                  break;
               case RIGHT:
                  goRIGHT=false;
                  break;
               case LEFT:
                  goLEFT=false;
                  break;
            }
            
         }
      });
      
      
           backgroundcollision = new Image(BACKGROUND_IMAGE);
      //animation timer
      animTimer = new AnimationTimer() {
         @Override
         public void handle(long now) {
           // System.out.println(renderCounter);
           // renderCounter++;
           crewmateRacer.update();

           for(int i=0;i<crewmateRunners.size();i++){
            crewmateRunners.get(i).update();
           }
            
         }
      };
      animTimer.start();

   }

   //Imposter class 
   class CrewmateRacer extends Pane{
      private int racePosX=0;
      private int  racePosY = 0;
      private ImageView aPicView=null;
      private boolean isMaster=true;

      public CrewmateRacer(boolean isMaster){
         this.isMaster = isMaster;
         if(isMaster){
            aPicView = new ImageView(CREWMATE_IMAGE);
         }
         else aPicView = new ImageView(CREWMATE_RUNNERS);
         this.getChildren().add(aPicView);
      }
      public void update(){
        //get pixle

         double speed=5;

         if(isMaster){
            Color color = backgroundcollision.getPixelReader().getColor(racePosX, racePosY);
       //     System.out.println(color.getBlue() + " " + color.getGreen() + " " +color.getRed());
            //keyboard
            if(color.getRed()>0.6){   speed = 10;  }
           int targetX = 0;
           int targetY = 0;
           double dist = Math.sqrt(Math.pow(racePosX -targetX,2)+Math.pow(racePosY -targetY,2));
           System.out.println(dist);
            if(goDOWN==true) racePosY+=speed;
            if(goUP==true) racePosY-=speed;
            if(goLEFT==true) racePosX-=speed;
            if(goRIGHT==true) racePosX+=speed;
   
         }
         else{
            racePosX += Math.random()*speed; 
            racePosY += (Math.random()-0.2)*speed; 
         }

         //set the picture position
         aPicView.setTranslateX(racePosX);
         aPicView.setTranslateY(racePosY);

         if(racePosX>root.getWidth()) racePosX=0;
         if(racePosY>root.getHeight()) racePosY=0;
         if(racePosX<0) racePosX=0;
         if(racePosY<0) racePosY=0;


      }
   }//end of CrewmateRacer

   class MovableBackground extends Pane{
      private int racePosX=0;
      private int  racePosY = 0;
      private ImageView aPicView=null;
    
      public MovableBackground(){
             aPicView = new ImageView(BACKGROUND_IMAGE);
             this.getChildren().add(aPicView);
      }
      public void update(){

         double speed=5;
         if(goDOWN==true) racePosY+=speed;
         if(goUP==true) racePosY-=speed;
         if(goLEFT==true) racePosX-=speed;
         if(goRIGHT==true) racePosX+=speed;

         aPicView.setTranslateX(racePosX);
         aPicView.setTranslateY(racePosY);
      }
   }
} // end class Races