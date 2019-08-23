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

To run the (super crappy) UI:

1. clone the project
2. navigate to the root directory with POM.xml and punch in `mvn javafx:run`

If you select the file `echoHidingRealFile.wav`, you'll see my super secred encoded message.

## Weekly reports

- [Weekly report #1](/documentation/report1.md)
- [Weekly report #2](/documentation/report2.md)
- [Weekly report #3](/documentation/report3.md)
- [Weekly report #4](/documentation/report4.md)
- [Weekly report #5](/documentation/report5.md)
