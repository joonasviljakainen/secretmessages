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
import javafx.scene.control.TextField;
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

    private TextArea messageEntryField;
    private TextArea decodedMessageDisplay;

    FileChooser f;

    private String messageToEncode;
    private byte[] decodedMessage;

    private Text fileName;
    private Text fileName2;
    private Text messageMaxSize;
    private Text messageMaxSize2;
    private Text numberOfChannels;
    private Text numberOfChannel2;


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

        int height = 100;
        int width = 250;
        messageEntryField = new TextArea();
        messageEntryField.setPrefHeight(height);
        messageEntryField.setPrefWidth(width);
        messageEntryField.setText("Message to hide");
        messageEntryField.setLayoutX(20);
        messageEntryField.setLayoutY(60);

        decodedMessageDisplay = new TextArea();
        decodedMessageDisplay.setPrefHeight(height);
        decodedMessageDisplay.setPrefWidth(width);
        decodedMessageDisplay.setEditable(false);
        decodedMessageDisplay.setText("The decoded message");
        decodedMessageDisplay.setLayoutX(300);
        decodedMessageDisplay.setLayoutY(60);

        initFileInfo();

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
        updateFileInfo();
        // TODO UPDATE VIEWS
    }

    private Pane echoHidingPane() {
        Pane p = new Pane();
        Text t = new Text();
        t.setText("Echo Hiding");
        t.setY(50);
        t.setX(50);
        p.getChildren().addAll(
                t,
                openFileButton(),
                encodeButton(),
                decodeButton(),
                messageEntryField,
                decodedMessageDisplay);
        return p;
    }

    private Pane lsbPane() {
        Pane p = new Pane();
        Text t = new Text();
        t.setText("Least-Significant Bit");
        t.setY(50);
        t.setX(50);

        // Message entry field



        p.getChildren().addAll(
                t,
                openFileButton(),
                encodeButton(),
                decodeButton(),
                messageEntryField,
                decodedMessageDisplay);
        return p;
    }

    private Scene ehscene(Button returnButton) {
        Pane p = new Pane();
        p.getChildren().addAll(echoHidingPane(), returnButton, this.fileName2);
        Scene s = new Scene(p, 600, 500);
        return s;
    }

    private Scene lsbscene(Button returnButton) {
        Pane p = new Pane();
        p.getChildren().addAll(lsbPane(), returnButton, this.fileName);
        Scene s = new Scene(p, 600, 500);
        return s;
    }

    private Scene mainScene(Button b1, Button b2) {
        Pane p = new Pane();
        p.getChildren().addAll(b1, b2, this.fileName);
        Scene s = new Scene(p, 600, 500);
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
        b.setLayoutY(350);
        b.setLayoutX(10);
        b.setOnAction(
                new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(final ActionEvent e) {
                        // TODO test for length of message vs calculated max length
                        //stegWorker.encode("testingtestingtesting");
                        stegWorker.encode(messageEntryField.getText());
                        System.out.println("DONE!");
                    }
        });

        return b;
    }

    private Button decodeButton() {
        Button b = new Button("Decode");
        b.setLayoutY(350);
        b.setLayoutX(100);
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

    private void initFileInfo() {
        if (this.fileName == null) {
            this.fileName = new Text();
            this.fileName.setLayoutY(240);
            this.fileName.setLayoutX(20);

            this.fileName2 = new Text();
            this.fileName2.setLayoutY(240);
            this.fileName2.setLayoutX(20);
        }
        if (!stegWorker.fileLoaded()) {
            this.fileName.setText("Filename: No File Selected");
            this.fileName2.setText("Filename: No File Selected");
        } else {

        }

    }

    private void updateFileInfo() {
        this.fileName.setText("File name: " + stegWorker.getFileName());
        this.fileName2.setText("File name: " + stegWorker.getFileName());
    }
}
