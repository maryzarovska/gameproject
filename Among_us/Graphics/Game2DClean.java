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

public class Game2DClean extends Application {
   // Window attributes
   private Stage stage;
   private Scene scene;
   private StackPane root;

   private static String[] args;

   private final static String CrewMate = "amongus.png"; // file with icon for a racer
   private final static String CrewMateRunners = "amongusRunners.png"; // file with icon for a racer
   private final static String BACGROUND_IMAGE = "background.jpg";
   CrewMateracer crewmatheracer = null;
   ArrayList<CrewMateracer> crewmateRunners = new ArrayList<>();
   AnimationTimer animTimer = null;
   private int renderCounter = 0;
   boolean goUP = false;
   boolean goDOWN = false;
   boolean goLEFT = false;
   boolean goRIGHT = false;

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
      crewmatheracer = new CrewMateracer(true);
      for (int i = 0; i < 5; i++) {
         CrewMateracer cR = new CrewMateracer(false);
         crewmateRunners.add(cR);
      }
      root.getChildren().addAll(crewmateRunners);
      for (int i = 0; i < crewmateRunners.size(); i++) {
        // root.getChildren().add(crewmateRunners.get(i));
      }

      // display the window
      

      scene = new Scene(root, 800, 500);
      // scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
      stage.setScene(scene);
      stage.show();

      // KEYBOARD
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
               case LEFT:
                  goLEFT = false;
                  break;
               case RIGHT:
                  goRIGHT = false;
                  break;
            }
         }
      });
      scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
         @Override
         public void handle(KeyEvent event) {
            switch (event.getCode()) {
               case UP:
                  goUP = true;
                  break;
               case DOWN:
                  goDOWN = true;
                  break;
               case LEFT:
                  goLEFT = true;
                  break;
               case RIGHT:
                  goRIGHT = true;
                  break;
            }
         }
      });

      animTimer = new AnimationTimer() {
         public void handle(long now) {
            // System.out.println(renderCounter);
            // renderCounter++;
            crewmatheracer.update();
         }
      };
      animTimer.start();
   }

   // Imposter class
   class CrewMateracer extends Pane {
      private int racePosX = 0;
      private int racePosY = 0;
      private ImageView aPicView = null;
      private boolean isPlayer = true;

      public CrewMateracer(boolean isPlayer) {
         this.isPlayer = isPlayer;
         if (isPlayer) {
            aPicView = new ImageView(CrewMate);
         } else
            aPicView = new ImageView(CrewMateRunners);
         this.getChildren().add(aPicView);
      }

      public void update() {
         double speed = 5;

         if (isPlayer) {
            if (goDOWN == true)
               racePosY += speed;
            if (goUP == true)
               racePosY -= speed;
            if (goLEFT == true)
               racePosX += speed;
            if (goRIGHT == true)
               racePosX -= speed;
         } else {
            racePosX += Math.random() * speed;
            racePosY += (Math.random() - 0.5) * speed;

         }

         aPicView.setTranslateX(racePosX);
         aPicView.setTranslateY(racePosY);
         if (racePosX > 800) {
            racePosX = 0;
         }
         if (racePosY > 800) {
            racePosY = 0;
         }
      }
   }
} // end class Races