# Implementation document

I implemented two steganographic algorithms: Least-Significant Bit and Echo hiding. For both of these, data can be hidden and extracted.

## Input data

The project accepts WAV files with 16bit PCM audio. The source audio may have more than two channels, although I haven't tested it on surround sound data. 

Most of my tests have been done on 44100Hz audio, but LSB doesn't care about sampling rates and I don't see any reason 8000Hz and 16000Hz would work with echo hiding - echoes are not handled in the time dimension, but rather with integer values representing the number of frames used as the delay.

I have included three sample audio files in the root of the project for testing.

## Least-Significant Bit encoding

Least-significant bit encoding has been implemented in such a way that at the moment data can be hidden in one channel (left by default). Peachy stuff.

## Echo Hiding

Echo hiding requires stuff like creating delays, convolving signals. Analysis involves fourier transforms and natural logarithms. 
- I have __not__ implemented fourier transforms. I use [JTransforms](https://sites.google.com/site/piotrwendykier/software/jtransforms) instead.
- I made attempts at a natural logarithm function, but decoding times rose up to 30 seconds or more (form 400-1000 milliseconds), so I abandoned that idea.

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
4. __Main.java__(Deprecated): the main class. Only used for testing.
5. __Mathematics.java__: Mathematical functions (absolute values, power of 2, exponential functions)
6. __EHEncoding.java__: Methods for hiding messages using Echo Hiding.
7. __EHDecoding.java__: Methods for extracting messages from WAV files that have been encoded using Echo Hiding.
8. __LSBEncoder.java__: Methods for hiding and extracting messages from WAV files using Least-Significant Bit encoding.
9: __Interface.java__: The UI of the application.
10: __ArrayUtils.java__: Utilities for manipulating byte arrays: (slicing, combining)
11: __BitManipulation.java__: Utilities for bit operations and byte to short conversions (and reverse).

## IO

We need to load and save files. All of this functionality has been encapsulated in IOManager.java.

Both loading and saving has been implemented.

## UI

There's a UI, but it's super crappy. Nonetheless, it works.

# Features left out & suggestions for improvement

- I implemented a decay rate variable for the echoes in echo hiding, but never plugged it in to the UI. That could prove some interesting results.
- Mixer signal lerping: the echo hidden audio has clicks in it due to the changing echo. This could be alleviated by smoothening the mixer signal close to the borders of changing bits. 
- Natural logarithms: could be replaced with own implementation 
- Fourier transforms: Currently using an external library.