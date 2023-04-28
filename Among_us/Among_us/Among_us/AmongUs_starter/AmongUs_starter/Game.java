/**
 * The Game class is a JavaFX application that creates a game with movable backgrounds, crewmate
 * racers, and collision detection.
 */

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
import java.net.Socket;
import java.util.*;

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
/*

This class implements the game mechanics, including moving backgrounds, control of actors, collision

detection, and animation.
*/ 
public class Game extends Application implements EventHandler<ActionEvent> {
   // Window attributes
   private Stage stage;
   private Scene scene;
   private StackPane root;

   private static String[] args;

   private static String CREWMATE_IMAGE = "Actual_Stormtrooper.png"; // file with icon for a racer
   private final static String CREWMATE_RUNNERS = "Darth_vader_Finished.png"; // file with icon for a racer
   private final static String BACKGROUND_IMAGE = "map.png";
   private final static String MASK_IMAGE = "RedGreen.png";
   Button task = new Button("Task");
   Button chat = new Button("Chat");
   FlowPane fp = new FlowPane();
   TextArea ta = new TextArea();
   // crewmate and runners
   CrewmateRacer crewmateRacer = null;
   ArrayList<CrewmateRacer> crewmateRunners = new ArrayList<>();

   // backgroundd
   MovableBackground movableBackground = null;

   // Animation timer
   AnimationTimer animTimer = null;
   private long renderCounter = 0;
   boolean goUP = false, goDOWN = false, goLEFT = false, goRIGHT = false;

   // collision
   Image backgroundCollision = null;

   // main program
   /**

The entry point of the application.
@param _args the command line arguments
*/
   public static void main(String[] _args) {
      args = _args;
      launch(args);
   }

/**

Sets up the stage and the root pane.

@param _stage the main window
*/
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
/**

Initializes the game scene.
*/
   public void initializeScene() {

      // initialize crewmate
      crewmateRacer = new CrewmateRacer(true);

      // add background
      movableBackground = new MovableBackground();

      // add to the root
      root.getChildren().add(movableBackground);
      root.getChildren().add(crewmateRacer);
      for (int i = 0; i < crewmateRunners.size(); i++) {
         root.getChildren().add(crewmateRunners.get(i));
      }
      ta.setPrefHeight(100);
      ta.setStyle("-fx-control-inner-background: #000000; -fx-text-fill: #FFFFFF; -fx-opacity: 0.5;");
      ta.setVisible(false);
      ta.setEditable(false);
      fp.setAlignment(Pos.BASELINE_LEFT);
      fp.getChildren().add(ta);

      HBox h = new HBox();
      h.setAlignment(Pos.BOTTOM_LEFT);
      h.getChildren().addAll(chat, task);
      task.setAlignment(Pos.BOTTOM_RIGHT);
      task.setOnAction(this);
      chat.setOnAction(this);
      task.setVisible(false);
      root.getChildren().addAll(fp, h);
      // display the window
      scene = new Scene(root, 800, 500);
      // scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
      stage.setScene(scene);
      stage.show();

      // KEYBOARD
      scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
         @Override
         public void handle(KeyEvent event) {
            // TODO Auto-generated method stub
            // System.out.println("PRESSED");
            switch (event.getCode()) {
               case UP:
                  goUP = true;
                  break;
               case DOWN:
                  goDOWN = true;
                  break;
               case RIGHT:
                  goRIGHT = true;
                 
                  break;
               case LEFT:
                  goLEFT = true;
                 
                  break;
             
            }

         }
      });
      scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
         @Override
         public void handle(KeyEvent event) {
            switch (event.getCode()) {
               case UP:
                  goUP = false;
                  break;
               case DOWN:
                  goDOWN = false;
                  break;
               case RIGHT:
                  goRIGHT = false;
                  break;
               case LEFT:
                  goLEFT = false;
                  break;
            }

         }
      });

      // here you load background MASK
      backgroundCollision = new Image(MASK_IMAGE);

      // animation timer
      animTimer = new AnimationTimer() {
         @Override
         public void handle(long now) {
            // System.out.println(renderCounter);
            // renderCounter++;
            crewmateRacer.update();

            for (int i = 0; i < crewmateRunners.size(); i++) {
               crewmateRunners.get(i).update();
            }

            movableBackground.update();

         }
      };
      animTimer.start();

   }
   //Handle method for chat button and task button
   @Override
   public void handle(ActionEvent event) {
      Button btn = (Button) event.getSource();
      switch (btn.getText()) {
         case "Task":
           task.setVisible(false);
            break;
         case "Chat":
            if (ta.isVisible() == false) {
               ta.setVisible(true);
            } else if (ta.isVisible() == true) {
               ta.setVisible(false);
            }

            break;

         default:
            break;
      }
   }
/**
 * The CrewmateRacer class is a Java class that represents a player in a racing game and handles their
 * movement and animation.
 */

   // Player class
   class CrewmateRacer extends Pane {
      private int racePosX = 400;
      private int racePosY = 250;
      private ImageView aPicView = null;
      private boolean isMaster = true;

      Image leftStep = new Image("Stormtrooper_Leftstep_one.png");
      Image leftStep1 = new Image("Stormtrooper_Leftstep_two.png");
      Image rightSept = new Image("Stormtrooper_Rightstep_one.png");
      Image rightSept1 = new Image("Stormtrooper_Rightstep_two.png");
      int step = 1;

      public CrewmateRacer(boolean isMaster) {
         this.isMaster = isMaster;
         if (isMaster) {
            aPicView = new ImageView(CREWMATE_IMAGE);
            racePosX = 400;
            racePosY = 250;
         } else
            aPicView = new ImageView(CREWMATE_RUNNERS);
         this.getChildren().add(aPicView);
      }

      public void update() {

         // get pixel

         double speed = 5;

         if (isMaster) {

            if (goDOWN == true)
               racePosY += speed;
            if (goUP == true)
               racePosY -= speed;

            new AnimationTimer() {
               private long lastUpdate = 0;
               private final long longDelay = 4000_000_000L;

               @Override
               public void handle(long now) {
                  if (now - lastUpdate >= longDelay) {
                     if (goLEFT == true) {
                        racePosX -= speed;
                        if (step % 2 == 0) {
                           aPicView.setImage(leftStep);
                        } else {
                           aPicView.setImage(leftStep1);
                        }
                        step++;
                     } else if (goRIGHT == true) {
                        racePosX += speed;
                        if (step % 2 == 0) {
                           aPicView.setImage(rightSept);
                        } else {
                           aPicView.setImage(rightSept1);
                        }
                        step++;
                     }
                     lastUpdate = now;
                  }
               }

            }.start();

            // System.out.println(color.getRed() + " " + color.getGreen() + " " +
            // color.getBlue());

            // calculate distance

            // keyboard

         }

         // set the picture position
         aPicView.setTranslateX(400);
         aPicView.setTranslateY(250);

      }
   }// end of CrewmateRacer
/**
 * The class MovableBackground is responsible for updating the background image and detecting
 * collisions in a game, while the Client method establishes a connection to a server.
 */

   class MovableBackground extends Pane {
      private int racePosX = -470;
      private int racePosY = -300;
      private ImageView aPicView = null;

      public MovableBackground() {
         aPicView = new ImageView(BACKGROUND_IMAGE);
         this.getChildren().add(aPicView);
      }
      //Collision command for crewmate racer
      public void update() {

         double speed = 5;
         Color color1 = backgroundCollision.getPixelReader().getColor(Math.abs(racePosX) + 400,
               Math.abs(racePosY) + 250);
         System.out.println(
               backgroundCollision.getPixelReader().getColor(Math.abs(racePosX) + 400, Math.abs(racePosY) + 250));

         Color color2 = backgroundCollision.getPixelReader().getColor(Math.abs(racePosX) + 428,
               Math.abs(racePosY) + 250);

         Color color3 = backgroundCollision.getPixelReader().getColor(Math.abs(racePosX) + 400,
               Math.abs(racePosY) + 294);

         Color color4 = backgroundCollision.getPixelReader().getColor(Math.abs(racePosX) + 428,
               Math.abs(racePosY) + 294);
         System.out.println(
               backgroundCollision.getPixelReader().getColor(Math.abs(racePosX) + 428, Math.abs(racePosY) + 294));

         if (color1.getRed() > 0.6) {
            speed = 0;
            racePosY -= 2;
            racePosX -= 2;
         }

         if (color1.getBlue() > 0.6) {
            task.setVisible(true);

         }
         if (color2.getRed() > 0.6) {
            speed = 0;
            racePosY -= 2;
            racePosX += 2;
         }

         if (color2.getBlue() > 0.6) {
            task.setVisible(true);
         }
         if (color3.getRed() > 0.6) {
            speed = 0;
            racePosY += 2;
            racePosX -= 2;
         }

         if (color3.getBlue() > 0.6) {
            task.setVisible(true);
         }
         if (color4.getRed() > 0.6) {
            speed = 0;
            racePosY += 2;
            racePosX += 2;
         }

         if (color4.getBlue() > 0.6) {
            task.setVisible(true);
         }
         if (goDOWN == true)
            racePosY -= speed;
         if (goUP == true)
            racePosY += speed;
         if (goLEFT == true)
            racePosX += speed;
         if (goRIGHT == true)
            racePosX -= speed;

         aPicView.setTranslateX(racePosX);
         aPicView.setTranslateY(racePosY);
      }
   }
/**
 * The function creates a client socket and initializes input and output streams to communicate with a
 * server.
 */

   public void Client() {
      try {
         Socket socket = new Socket("127.0.0.1", 2030);
         ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
         ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
         String introduction = ois.readUTF();
         
         Player p1 = new Player(0, 0, STYLESHEET_CASPIAN);
      } catch (Exception e) {
         // TODO: handle exception
      }

   }

} // end class Races