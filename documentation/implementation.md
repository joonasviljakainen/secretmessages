# Implementation document

__NOTE__: I have NOT implemented Fourier transforms myself.

## Least-Significant Bit encoding

Least-significant bit encoding has been implemented in such a way that at the moment data can be hidden in one channel (left by default).

## Echo Hiding

### Encoding

EH encoding was in the end fairly straightforward:

1. Extract the cover audio (the data to hide the message to).
2. Set delays for 1 and 0.
3. Create two delayed versions of the cover audio: essentially, pad the start of the audio with zeroes.
4. Create a mixer signal which will be used to select the signal currently being convolved with the cover audio.
5. Set a decay value for the delay (e.g. 0.7 of the amplitude of the original audio signal).
6. Convolve the three audio signals (cover audio, zero delayed, one delayed) using the mixer signal and decay.

### Decoding

In order to decode the signal, three values need to be known (or guessed) beforehand:

- The delay (as number of samples) for bit zero (d0)
- The delay for bit one (d1)
- The length (as number of samples) of a single segment i.e. block of samples containing a single bit.

With this info we can extract bits as follows:

1. Divide the steganographically modified audio to segments along the assumed block size (number of frames per hidden bit).
2. Calculate the autocepstrum for each segment: the inverse Fourier transform of the logarithm of the absolute value of the Fourier transform of the segment. The returned autocepstrum is an array of doubles.*
3. If the value at location d0 in the autocepstrum is larger than that at d1, then the bit for this block is 0. Essentially this says that the echo begins at d0 of the array.

Rinse and repeat.

\* Someone once invented that the autocepstrum measures delays and echoes in audio. Go figure.

## Class structure

The project is a Maven project, consisting of the following classes:

1. __SecretMessages.java__: An entry point class that brings together the features of all other classes, so as to provide them to the UI.
2. __WavFile.java__: A class representing a WAV file. Provides methods for accessing and manipulating the data of the WAV file, as well as for opening and reading WAV files using the IOManager class.
3. __IOManager.java__: A helper class for reading and writing files as byte arrays.
4. __Main.java__(Deprecated): the main class.
5. __Mathematics.java__: Mathematical functions.
6. __EHEncoding.java__: Methods for hiding messages using Echo Hiding.
7. __EHDecoding.java__: Methods for extracting messages from WAV files that have been encoded using Echo Hiding.
8. __LSBEncoder.java__: Methods for hiding and extracting messages from WAV files using Least-Significant Bit encoding.
9: __Interface.java__: The UI of the application.
10: __ArrayUtils.java__: Utilities for manipulating byte arrays.
11: __BitManipulation.java__: Utilities for bit operations and byte to short conversions (and reverse).

## IO

We need to load and save files. All of this functionality has been encapsulated in IOManager.java.

## UI

There's a UI, but it's still WIP.