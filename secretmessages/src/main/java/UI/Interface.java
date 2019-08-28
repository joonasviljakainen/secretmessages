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
import javafx.collections.FXCollections;
import javafx.application.Application;
import javafx.collections.ObservableList;;
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
import java.util.List;
import java.util.Observable;
import java.util.ArrayList;
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
    private String[] algorithms = { "Least-Significant Bit", "Echo Hiding" };

    private Stage mainStage;

    private final int[] segmentLengths = { 8 * 512, 8 * 1024, 8 * 2 * 1024, 8 * 3 * 1024 };

    private SecretMessages stegWorker;

    private Scene main;
    private Scene ehScene;
    private Scene lsbScene;

    private Button toMain;
    private Button toMain2;
    private Button toEH;
    private Button toLSB;
    Button fileButton;

    private Button saveButton1;
    private Button saveButton2;

    private TextArea messageEntryField;
    private TextArea messageEntryField2;
    private TextArea decodedMessageDisplay;
    private TextArea decodedMessageDisplay2;

    private Text delayDescriptor;
    private TextField d0Display;
    private TextField d1Display;

    FileChooser f;

    private String messageToEncode;
    private byte[] decodedMessage;

    private Text fileName;
    private Text fileName2;
    private Text messageMaxSize;
    private Text messageMaxSize2;
    private Text numberOfChannels;
    private Text numberOfChannel2;

    private Text lsbMaxLengthDisplay;
    private Text ehMaxLengthDisplay;

    private ChoiceBox cb1;
    private ChoiceBox cb2;

    private ChoiceBox ehSegmentLengthChoiceBox;

    public static void main(String[] args) {
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

        messageEntryField2 = new TextArea();
        messageEntryField2.setPrefHeight(height);
        messageEntryField2.setPrefWidth(width);
        messageEntryField2.setText("Message to hide");
        messageEntryField2.setLayoutX(20);
        messageEntryField2.setLayoutY(60);

        decodedMessageDisplay = new TextArea();
        decodedMessageDisplay.setPrefHeight(height);
        decodedMessageDisplay.setPrefWidth(width);
        decodedMessageDisplay.setEditable(false);
        decodedMessageDisplay.setText("The decoded message");
        decodedMessageDisplay.setLayoutX(300);
        decodedMessageDisplay.setLayoutY(60);

        decodedMessageDisplay2 = new TextArea();
        decodedMessageDisplay2.setPrefHeight(height);
        decodedMessageDisplay2.setPrefWidth(width);
        decodedMessageDisplay2.setEditable(false);
        decodedMessageDisplay2.setText("The decoded message");
        decodedMessageDisplay2.setLayoutX(300);
        decodedMessageDisplay2.setLayoutY(60);

        cb1 = createChoiceBox();
        cb2 = createChoiceBox();

        ehSegmentLengthChoiceBox = new ChoiceBox<>();
        setupSegmentLengthChoiceBox();
        setupSaveButtons();
        setupDelayControls();

        initFileInfo();

        f = new FileChooser();
        f.setTitle("Select audio file to encode");
        fileButton = new Button("Select an audio file for encoding");
        fileButton.setOnAction(new EventHandler<ActionEvent>() {
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

        // Creating a scene object
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
        p.getChildren().addAll(t, openFileButton(), encodeButton(), decodeButton(), messageEntryField,
                decodedMessageDisplay, ehMaxLengthDisplay, ehSegmentLengthChoiceBox, saveButton1, delayDescriptor, d0Display, d1Display);

        if (this.stegWorker.fileLoaded()) {
            List<Integer> choices = new ArrayList<>();
            for (int i = 0; i < stegWorker.getNumberOfChannels(); i++) {
                choices.add(i);
            }
            // cb1 = createChoiceBox("Select channel");
            ObservableList o = FXCollections.observableArrayList(choices);
            cb1.setItems(o);
            cb2.setItems(o);
        }
        p.getChildren().add(cb1);
        return p;
    }

    private Pane lsbPane() {
        Pane p = new Pane();
        Text t = new Text();
        t.setText("Least-Significant Bit");
        t.setY(50);
        t.setX(50);

        // Message entry field

        p.getChildren().addAll(t, openFileButton(), encodeButton(), decodeButton(), messageEntryField2,
                decodedMessageDisplay2, lsbMaxLengthDisplay, saveButton2);

        if (this.stegWorker.fileLoaded()) {
            List<Integer> choices = new ArrayList<>();
            for (int i = 0; i < stegWorker.getNumberOfChannels(); i++) {
                choices.add(i);
            }
            ObservableList o = FXCollections.observableArrayList(choices);
            cb1.setItems(o);
            cb2.setItems(o);
        }
        p.getChildren().add(cb2);
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
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                switch (i) {
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
        ofButton.setOnAction(new EventHandler<ActionEvent>() {
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
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                if (stegWorker.getAlg() == "LSB") {
                    // TODO check message length
                    stegWorker.encode(messageEntryField2.getText());
                } else {
                    // TODO check message length
                    stegWorker.encode(messageEntryField.getText());
                }
                System.out.println("DONE!");
            }
        });
        return b;
    }

    private Button decodeButton() {
        Button b = new Button("Decode");
        b.setLayoutY(350);
        b.setLayoutX(100);
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                decodedMessage = stegWorker.decode();
                if (decodedMessage == null) {
                    System.out.println("Voi hemmetti");
                    return;
                }
                char[] chars = new char[decodedMessage.length];
                for (int i = 0; i < decodedMessage.length; i++) {
                    chars[i] = (char) decodedMessage[i];
                }
                String mes = new String(chars);
                decodedMessageDisplay.setText(mes);
                decodedMessageDisplay2.setText(mes);
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

            // length displays
            this.lsbMaxLengthDisplay = new Text("");
            lsbMaxLengthDisplay.setLayoutY(255);
            lsbMaxLengthDisplay.setLayoutX(20);
            this.ehMaxLengthDisplay = new Text("");
            ehMaxLengthDisplay.setLayoutY(255);
            ehMaxLengthDisplay.setLayoutX(20);

            updateChoiceBox();
        }
        if (!stegWorker.fileLoaded()) {
            this.fileName.setText("Filename: No File Selected");
            this.fileName2.setText("Filename: No File Selected");
            ehMaxLengthDisplay.setText("Max Length: Info unavailable");
            lsbMaxLengthDisplay.setText("Max Length: Info unavailable");
        } else {

        }

    }

    private void updateFileInfo() {
        this.fileName.setText("File name: " + stegWorker.getFileName());
        this.fileName2.setText("File name: " + stegWorker.getFileName());
        updateChoiceBox();

        ehMaxLengthDisplay.setText("Max Length: " + stegWorker.getMaxLengthForEH() + " bytes");
        lsbMaxLengthDisplay.setText("Max Length: " + stegWorker.getMaxLengthForLSB() + " bytes");
    }

    private ChoiceBox createChoiceBox() {
        ChoiceBox c = new ChoiceBox<>();
        c.setTooltip((new Tooltip("Select channel to decode. Typically, smaller is left, larger is right.")));
        c.setMinWidth(50);
        c.setLayoutX(300);
        c.setLayoutY(300);
        c.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue o, Number value, Number newValue) {
                stegWorker.setChannel(newValue.intValue());
            }
        });
        return c;
    }

    private void setupSegmentLengthChoiceBox() {
        ehSegmentLengthChoiceBox.setTooltip(new Tooltip("Select segment length for EH encoding and decoding"));
        ObservableList o = FXCollections.observableArrayList(8 * 512, 8 * 1024, 8 * 2 * 1024, 8 * 3 * 1024);
        ehSegmentLengthChoiceBox.setItems(o);
        ehSegmentLengthChoiceBox.setLayoutX(250);
        ehSegmentLengthChoiceBox.setLayoutY(250);
        ehSegmentLengthChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue o, Number value, Number newValue) {
                stegWorker.setSegmentLength(segmentLengths[newValue.intValue()]);
                updateFileInfo();
            }
        });
    }

    private void updateChoiceBox() {
        if (this.stegWorker.fileLoaded()) {
            List<Integer> choices = new ArrayList<>();
            for (int i = 0; i < stegWorker.getNumberOfChannels(); i++) {
                choices.add(i + 1);
            }
            ObservableList o = FXCollections.observableArrayList(choices);
            cb1.setItems(o);
            cb2.setItems(o);

        }
    }

    public void setupSaveButtons() {
        saveButton1 = new Button("Save as...");
        saveButton2 = new Button("Save as...");

        saveButton1.setLayoutX(20);
        saveButton1.setLayoutY(400);
        saveButton2.setLayoutX(20);
        saveButton2.setLayoutY(400);

        saveButton1.setOnAction(new EventHandler<ActionEvent>() {
  
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
    
                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
                fileChooser.getExtensionFilters().add(extFilter);
                
                //Show save file dialog
                File file = fileChooser.showSaveDialog(mainStage);
                byte[] bytes = stegWorker.getSaveableByteArray();
                if(file != null && bytes != null){
                    try {
                    IOManager.writeBytesToFile(bytes, file.getName());
                    } catch (Exception e) {
                        System.out.println("ERRIR");
                        System.out.println(e);
                    }
                    //SaveFile(Santa_Claus_Is_Coming_To_Town, file);
                }
            }
        });
    }

    public void setupDelayControls() {
        delayDescriptor = new Text("Delay lengths for echo hiding");
        delayDescriptor.setLayoutX(230);
        delayDescriptor.setLayoutY(350);

        d0Display = new TextField();
        d0Display.setMaxWidth(60);
        d0Display.setLayoutX(230);
        d0Display.setLayoutY(360);

        d0Display.textProperty().addListener((observable, oldValue, newValue) -> {
            Integer i = Integer.parseInt(newValue);
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });

        d1Display = new TextField();
        d1Display.setMaxWidth(60);
        d1Display.setLayoutX(300);
        d1Display.setLayoutY(360);

        d1Display.textProperty().addListener((observable, oldValue, newValue) -> {
            Integer i = Integer.parseInt(newValue);
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });
    }
}
