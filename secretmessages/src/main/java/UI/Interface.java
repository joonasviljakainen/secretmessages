package main.java.UI;

import java.awt.Desktop;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import Steganography.LSBEncoder;
import Steganography.EHEncoding;
import Steganography.EHDecoding;
import domain.WavFile;
import IO.IOManager;
import main.java.domain.SecretMessages;

import java.util.List;


public class Interface extends Application {

    private Desktop desktop = Desktop.getDesktop();
    private String[] algorithms = {"Least-Significant Bit", "Echo Hiding"};


    private Stage mainStage;

    private SecretMessages stegWorker;

    private Scene main;
    private Scene ehScene;
    private Scene lsbScene;

    private Button toMain;
    private Button toMain2;
    private Button toEH;
    private Button toLSB;
    Button fileButton;

    FileChooser f;

    private String messageToEncode;
    private byte[] decodedMessage;


    public static void main(String[] args) {
        System.out.println("Starting...");
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        stegWorker = new SecretMessages();

        mainStage = stage;

        toMain = toPlaceButton(0, "To Main Menu");
        toMain2 = toPlaceButton(0, "To Main Menu");
        toEH = toPlaceButton(2, "To Echo Hiding");
        toLSB = toPlaceButton(1, "To Least-Significant Bit ");

        f = new FileChooser();
        f.setTitle("Select audio file to encode");
        fileButton = new Button("Select an audio file for encoding");
        fileButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = f.showOpenDialog(stage);
                        if (file != null) {
                            openFile(file);
                        }
                    }
                });

        main = mainScene(toEH, toLSB);
        ehScene = ehscene(toMain2);
        lsbScene = lsbscene(toMain);

        //Creating a scene object
        stage.setTitle("Steganography");
        stage.setScene(main);

        stage.show();

    }

    private void openFile(File file) {
            stegWorker.setWavfile((file.getPath()), file.getName());
            // TODO UPDATE VIEWS
    }

    private Pane echoHidingPane() {
        Pane p = new Pane();
        Text t = new Text();
        t.setText("Echo Hiding");
        t.setY(50);
        t.setX(50);
        p.getChildren().addAll(t, openFileButton(), encodeButton(), decodeButton());
        return p;
    }

    private Pane lsbPane() {
        Pane p = new Pane();
        Text t = new Text();
        t.setText("Least-Significant Bit");
        t.setY(50);
        t.setX(50);
        p.getChildren().addAll(t, openFileButton(), encodeButton(), decodeButton());
        return p;
    }

    private Scene ehscene(Button returnButton) {
        Pane p = new Pane();
        p.getChildren().addAll(echoHidingPane(), returnButton);
        Scene s = new Scene(p, 600, 300);
        return s;
    }

    private Scene lsbscene(Button returnButton) {
        Pane p = new Pane();
        p.getChildren().addAll(lsbPane(), returnButton);
        Scene s = new Scene(p, 600, 300);
        return s;
    }

    private Scene mainScene(Button b1, Button b2) {
        Pane p = new Pane();
        p.getChildren().addAll(b1, b2);
        Scene s = new Scene(p, 600, 300);
        return s;
    }

    private Button toPlaceButton(int i, String text) {
        Button b = new Button(text);
        b.setLayoutX(i * 200);
        b.setLayoutY(10);
        b.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        System.out.println(text);
                        switch (i){
                            case 0:
                                mainStage.setScene(main);
                                break;
                            case 1:
                                mainStage.setScene(lsbScene);
                                stegWorker.setAlg(0);
                                break;
                            default:
                                mainStage.setScene(ehScene);
                                stegWorker.setAlg(1);
                        }
                    }
                });
        return b;
    }

    private Button openFileButton() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select audio file to encode");
        Button ofButton = new Button("Select an audio file for encoding");
        ofButton.setLayoutY(200);
        ofButton.setLayoutX(10);
        ofButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fc.showOpenDialog(mainStage);
                        if (file != null) {
                            openFile(file);
                        }
                    }
                });
        return ofButton;
    }

    private Button encodeButton() {
        Button b = new Button("Encode");
        b.setLayoutY(240);
        b.setLayoutX(10);
        b.setOnAction(
                new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(final ActionEvent e) {
                        System.out.println(stegWorker.getAlg());
                        stegWorker.encode("testingtestingtesting");
                    }
        });

        return b;
    }

    private Button decodeButton() {
        Button b = new Button("Decode");
        b.setLayoutY(260);
        b.setLayoutX(10);
        b.setOnAction(
                new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(final ActionEvent e) {
                        decodedMessage = stegWorker.decode();
                        if (decodedMessage == null) {
                            System.out.println("Voi hemmetti");
                            return;
                        }
                        for (int i = 0; i < decodedMessage.length; i++) {
                            System.out.print((char)decodedMessage[i]);
                        }
                    }
                });

        return b;
    }
}
