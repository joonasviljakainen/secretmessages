# Secretmessages

A project for TiraLabra.

## Purpose

The aim of this project is to hide data/messages in audio files. 


### Algorithms
 
 __Least-Significant Bit__ encoding will be used in one form or other.
  There is time, so I will try to implement an __Echo Hiding__ algorithm for watermarking audio files.

  The point, of course, is to also be able to recover the messages from the audio files.

### Target Data

This project will target Microsoft WAV files, as they are a simple starting point.

## Documentation

- [Design document](/documentation/design.md)
- [Implementation document](/documentation/implementation.md)
- [Testing document](/documentation/testing.md)
- [Hour reporting](/documentation/hours.md)
- [Sample on echo hiding](/secretmessages/alternatingechotest.wav)

### Using the project


I was unable to build the project properly (because why would I want to do that?), so you'll need to run it from source. 

To run:

1. clone the project
2. navigate to the root directory with POM.xml and punch in `mvn javafx:run`

If you have problems, try `mvn clean install && mvn javafx:run`.

To use LSB:

1. Click on "Least-Significant Bit"
2. Click on "Select an audio file for encoding". You can navigate to the root of the repo and pick one of the files in the directory /samples.
3. Write a message in the left-hand field
4. Select channel from the tiny menu with no labe.
5. Press "encode" to encode
6. Save the file (this always saves in the current directory)
7. Open the saved file
8. Select the same channel you used previously for encoding
9. Click "Decode".

You should see the hidden message.

To use Echo Hiding:

(I hid something in the file 0728-encoded.wav. Block size is the longest possible, channel is 1.)

1. Start app and go "To Echo Hiding
2. Select file
3. Select segment length in the unnamed field - e.g. 16384
4. Write your message in the left-hand text area - remember to follow the max limit!
5. Select channel, delay for bit 0 and delay for bit 1
6. Click "Encode"

Save the file if you want to and listen to it. Open it similarly to the instructions for LSB and punch in the same parameters you used for encoding.



If you select the file `echoHidingRealFile.wav`, you'll see my super secred encoded message.

## Weekly reports

- [Weekly report #1](/documentation/report1.md)
- [Weekly report #2](/documentation/report2.md)
- [Weekly report #3](/documentation/report3.md)
- [Weekly report #4](/documentation/report4.md)
- [Weekly report #5](/documentation/report5.md)
- [Weekly report #6](/documentation/report6.md)
