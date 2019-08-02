# Design document

## Overview

Secret Messages will be a java program that can be run from the command line. It may or may not feature a simple GUI.

## Algorithms

__Least-Significant Bit encoding__ will be used for hiding the messages. 

I will implement a second algorithm for comparison (e.g. __Echo Hiding__, or something simpler).


### Time & space complexities

At the moment the time complexity of all operations appears to be O(n). In addition, implementing echo hiding would require working on somewhat complicated mathematical functions.

Space requirements as I see them at the moment are also O(n) (possibly O(2n), to be more specific).

## Input/Output

The program will consume and modify Microsoft WAV files as they are a relatively easy starting point for processing audio. 

In addition, the program will require that the user supply a message to hide in the audio file.

As output, the program will create a WAV file that any user can play back, provided they possess a suitable player. The WAV file will include any message that the user has entered. 

The program will have the capacity to decode messages as well.

As for disk I/O, I will implement the required functions in their own package as simply as I can.

Essentially:
- WAV audio and input data (string) -> steganographically altered WAV file
- Stego WAV audio --> to reveal the hidden data.



## Sources

- [Intro to Audio Programming, Part 2: Demystifying the WAV Format](https://blogs.msdn.microsoft.com/dawate/2009/06/23/intro-to-audio-programming-part-2-demystifying-the-wav-format/)
- [WAVE PCM soundfile format](http://soundfile.sapp.org/doc/WaveFormat/)
- [Audio Steganography : The art of hiding secrets within earshot (Medium)](https://medium.com/@sumit.arora/audio-steganography-the-art-of-hiding-secrets-within-earshot-part-2-of-2-c76b1be719b3)
- [Least Significant Bit CodingAnalysis for Audio Steganography](http://www.fgcomputing.com/papers/04.%20Audio.pdf)
- [A Comparison of Echo Hiding Methods](https://pdfs.semanticscholar.org/6123/8f8522044acdef51ad3ed64f6593ba9b3308.pdf)