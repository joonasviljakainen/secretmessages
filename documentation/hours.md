# Hour reporting


## Week 1

[Weekly report #1](/documentation/report1.md)

| Date | Time spent | Description |
|-|-|-|
| 23.7.2019 | 2h | Wondering about topics, basic research on WAV files, established a repository & hello world |
| 25.7.2019 | 2h | Documentation, repo setup, searching for sources. Dummy program structure. |
| 26.7.2019| 1h | Additional thinking & preparing I/O functions for processing WAV. |
| __Total__ | 5h | Good start, but there's a whole lot ahead. |

## Week 2

[Weekly report #2](/documentation/report2.md)

| Date | Time spent | Description |
|-|-|-|
| 27.7.2019 | 2h | Gloom & Doom, Reading up on WAV, writing back to file, and some WAVE header parsing |
| 28.7.2019 | 4,5h | More reading up on WAV, parsing WAVE headers, added sample files with different sampling rates. Now I can see what the file is like. Implemented writing WavFile objects to disk from the pieces they were originally broken into. |
| 29.7.2019 | 3h | Added javadoc to wavfile, tests for wavfile, setting up tests and general shenanigans |
| 30.7.2019 | 1h | Implemented basic bit interleaving and extraction for single bits. |
| 31.7.2019 | 4h | Pain and suffering, I struggled a lot bit LSB encoding and decoding in its simple form is now available. |
| 2.8.2019 | 3h | Fixed LSB, wrote tests, refactored project for maintainability. Wrote report and documentation |
| __Total__ | 17,5h | That's alot of hours. I did ok mostly. |

Subtotal for the first 2 weeks: __21,5h__

## Week 3

[Weekly report #3](/documentation/report3.md)

| Date | Time spent | Description |
|-|-|-|
| 4.8.2019 | 0,5h | Research |
| 5.8.2019 | 1,5h | Research |
| 6.8.2019 | 1h| Research. Added JTransform package for dealing with Fourier Transforms. |
| 9.8.2019 | 2,5h | More research. I worked on transforming audio bytes to doubles for fourier transformations. |
| __Total__ | 5,5h | This week I was super tired, so I focused on trying to understand what I was doing. |

Subtotal for 3 weeks: 27h


## Week 4

[Weekly report #4](/documentation/report4.md)

| Date | Time spent | Description |
|-|-|-|
| 10.8.2019 | 2h | Converting shorts to little-endian bytes. Work on creating my first echoes. |
| 11.8.2019 | 3h | Implemented a simple echo function. The values still need to be normalized i.e. I need to create overflow checks. |
| 12.8.2019 | 3,5h | Implemented checks for overflows - now adding the echo should not cause distortion to the audio signal. Implemented a vaying echo! All we need is a nice smooth mixer signal to help. |
| 13.8.2019 | 2,5 | Work on tests. IDE apocalypse forced me to work on another IDE - which is painful. |
| 14.8.2019 | 0.5h | Thinking about tests |
| 16.8.2019 | 1,5h | Tests, documentation. |
| __Total__ | 15h | |

Subtotal for 4 weeks: 42h


## Week 5

[Weekly report #5](/documentation/report5.md)

| Date | Time spent | Description |
|-|-|-|
| 17.8.2019 | 2h | Work on cepstrum analysis. |
| 18.8.2019 | 3h | Further work on cepstrum analysis and echo hiding data extraction. For the most part, I have been successful. |
| 20.8.2019 | 3h | Almost had a meltdown over JavaFX BS. I had to switch to Oracle's JDK. There's a super simple UI that can only be used for decoding at the moment. |
| 22.8.2019 | 4h | Threw away the UI and started over. Created a business logic class that should work to simplify the interactions between the UI and the algorithmic work. |
| 23.8.2019 | 5h | Tests, minor refactoring. Peer review. POM updates so as to create a more accurate test report. |
| __Total__ |  17h |  |

Subtotal for 5 weeks: 59h

## Week 6

[Weekly report #6](/documentation/report6.md)

| Date | Time spent | Description |
|-|-|-|
| 25.8.2019 | 1,5h | UI work |
| 27.8.2019 | 3h | UI: plugging things in |
| 28.8.2019 | 1h | UI work |
| 29.8.2019 | 1,5h | Work on logarithms |
| __Total__ | 7h ||

Subtotal for 5 weeks: 66h

## Week 7

Totally ran out of time, but it'll 

| Date | Time spent | Description |
|-|-|-|
| 2.9.2019 | 2h | Discarded logarithm work. Preparing for the demo |
| 6.9.2019 | 4h | UI work, documentation, crunch time! |
| __Total__ | 6h ||

Total for the project: 72h