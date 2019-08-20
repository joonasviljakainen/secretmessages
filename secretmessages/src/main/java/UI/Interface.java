package main.java.UI;

import java.awt.Desktop;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
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
import java.util.List;


public class Interface extends Application {

    private Desktop desktop = Desktop.getDesktop();
    private WavFile wavFile;


    public static void main(String[] args) {
        System.out.println("Starting...");
        launch(args);
    }

    @Override
    public void start(Stage stage) {
/*
        GridPane mainGridPane = new GridPane();
        mainGridPane.setMinSize(700, 900);
        mainGridPane.setPadding(new Insets(10, 10, 10, 10));

        mainGridPane.setVgap(5);
        mainGridPane.setHgap(5);
        mainGridPane.setAlignment(Pos.CENTER);

        FileChooser f = new FileChooser();
        f.setTitle("Select audio file to encode");

        Group root = new Group();
        ObservableList list = root.getChildren();
        list.add(mainGridPane);
        list.add(f);

        Scene s = new Scene(root, 600, 700);
        primaryStage.setTitle("Audio Steganography!");
        primaryStage.setScene(s);
        primaryStage.show();

*/

        //text
        Text text = new Text();
        text.setFont(new Font(45));
        text.setX(50);
        text.setY(150);
        text.setText("Welcome to Steganography!");

        Group root = new Group();

        ObservableList list = root.getChildren();
        list.add(text);

        // File chooser
        FileChooser f = new FileChooser();
        f.setTitle("Select audio file to encode");
        final Button encodeButton = new Button("Select an audio file for encoding");
        encodeButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = f.showOpenDialog(stage);
                        if (file != null) {
                            openFileForEncoding(file);
                        }
                    }
                });
        list.add(encodeButton);

        FileChooser f2 = new FileChooser();
        f.setTitle("Select audio file to encode");
        final Button decodeButton = new Button("Select an audio file for decoding");
        decodeButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = f.showOpenDialog(stage);
                        if (file != null) {
                            openFileForDecoding(file);
                        }
                    }
                });
        list.add(decodeButton);

        //Creating a scene object
        Scene scene = new Scene(root, 600, 300);
        stage.setTitle("Sample Application");
        stage.setScene(scene);

        stage.show();

    }

    private void openFileForEncoding(File file) {
        try {
            //desktop.open(file);
            wavFile = new WavFile(IOManager.readFileToBytes(file.getPath()));
            System.out.println("WAVFILE INITIALIZED FOR ENCODING");
        } catch (IOException ex) {
            System.out.println("VOIHAN RIPULI");
        }
    }

    private void openFileForDecoding(File file) {
        try {
            //desktop.open(file);
            wavFile = new WavFile(IOManager.readFileToBytes(file.getPath()));
            System.out.println("WAVFILE INITIALIZED FOR DECODING");
            byte[] result = EHDecoding.decode(wavFile.getChannelByNumber(1));
            System.out.println("DECODED");
            for ( int i = 0 ; i < result.length; i++) {
                System.out.print((char)(result[i]));
            }
        } catch (IOException ex) {
            System.out.println("VOIHAN RIPULI");
        }
    }
}
