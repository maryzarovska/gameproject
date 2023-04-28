/**
 * The Server class is a Java program that creates a chat server which allows multiple clients to
 * connect and communicate with each other.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Server extends Application implements EventHandler<ActionEvent> {
  private static final ArrayList<PlayerThread> list = new ArrayList<PlayerThread>();
  private static final int SERVER_PORT = 2030;
  private ServerSocket sSocket = null;
  private Stage stage;
  private Scene scene;
  private VBox root = new VBox();

  Button b1 = new Button("Start");
  TextField t1 = new TextField();
  Label l1 = new Label("The imposter wars");

  /**
   * This function sets up a JavaFX GUI with three FlowPanes, a TextArea, and a button that triggers an
   * action.
   * 
   * @param primaryStage The main window of the JavaFX application, which is passed as a parameter to
   * the start() method.
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    stage = primaryStage;

    FlowPane f1 = new FlowPane();
    FlowPane f2 = new FlowPane();
    FlowPane f3 = new FlowPane();
    f1.getChildren().add(l1);
    f1.setAlignment(Pos.TOP_CENTER);
    f2.getChildren().add(t1);
    f2.setAlignment(Pos.CENTER);
    f3.getChildren().add(b1);
    b1.setOnAction(this);
    f3.setAlignment(Pos.BOTTOM_CENTER);
    TextArea ta = new TextArea();
    root.getChildren().addAll(f1, f2, f3,ta);
    scene = new Scene(root, 400, 200);
    stage.setScene(scene);
    stage.show();
  }
/**
 * This is the main function that launches a Java program.
 */

  public static void main(String[] args) {
    launch(args);
  }
/**
 * The ServerThread class creates a server socket and accepts incoming client connections, creating a
 * new PlayerThread for each client.
 */

  class ServerThread extends Thread {
    public void run() {
      try {
        sSocket = new ServerSocket(SERVER_PORT);
        while (true) {

          Socket cSocket = sSocket.accept();
          PlayerThread pt = new PlayerThread(cSocket, t1.getText());
          pt.start();
          list.add(pt);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
/**
 * The class represents a thread for a chat application that allows users to change their name, send
 * messages, and quit the chat.
 */

  class PlayerThread extends Thread {
    Socket cSocket = null;
    String name = "";

    public PlayerThread(Socket cSocket, String name) {
      this.cSocket = cSocket;
      this.name = name;
    }

    public void run() {
      try {
        DataInputStream dis = new DataInputStream(cSocket.getInputStream());
        DataOutputStream dos = new DataOutputStream(cSocket.getOutputStream());
        dos.writeUTF("Welcome to the chat" + name + "!");
        dos.flush();
        for (PlayerThread pt : list) {
          if (pt != this) {
            pt.sendChatMessage(name + "has joined the chat");
          }
        }
        while (true) {
          dos.writeUTF(name);
          String message = dis.readUTF();
          
          if (message.startsWith("name")) {
            String newName = message.substring("name".length());
            for (PlayerThread pt : list) {
              if (pt != this && pt.name.equals(newName)) {
                dos.writeUTF("The name" + newName + "is alredy taken");
                dos.flush();
                continue;
              }
            }
            String oldName = name;
            name = newName;
            dos.writeUTF("Your name has been changed to" + name);
            for (PlayerThread pt : list) {
              if (pt != this) {
                pt.sendChatMessage(oldName + "has changed their name");
              }
            }
          } else if (message.startsWith("quit")) {
            dos.writeUTF("Goodbye!");
            dos.flush();
            for (PlayerThread pt : list) {
              if (pt != this) {
                pt.sendChatMessage(name + "has left the chat");

              }
            }
            break;
          } else{
            for (PlayerThread pt : list) {
              if (pt != this) {
                pt.sendChatMessage(name + ":" + message);

              }
            }

          }
        }

      } catch (Exception e) {
        e.printStackTrace();
      }

    }
/**
 * This function sends a chat message through a socket connection.
 * 
 * @param message The message that needs to be sent over the network through the cSocket.
 */

    private void sendChatMessage(String message) {
      try {
        DataOutputStream dos = new DataOutputStream(cSocket.getOutputStream());
        dos.writeUTF(message);
        dos.flush();
      } catch (Exception e) {

      }

    }

  }
/**
 * The function handles an action event triggered by a button click, and starts a server thread if the
 * button text is "Start".
 * 
 * @param event An ActionEvent object that represents the user's action, such as clicking a button.
 */

  @Override
  public void handle(ActionEvent event) {
    Button btn = (Button) event.getSource();
    switch (btn.getText()) {
      case "Start":
      
        ServerThread st = new ServerThread();
        st.start();
        break;

      default:
        break;
    }
  }
}