# Weekly report 5

[Hour reporting](/documentation/hours.md)

## Overall status

Doing fine, lots of polish and pipe-laying to do but the core is solid.

## What did I do this week?

I implemented autocepstrum analysis. The results are interesting. I can extract hidden data from the files I encode with simple echo hiding. Short block lengths (i.e. the number of frames used for hiding a single bit) introduce bit errors at sizes equal to or less than 8 * 1024. At a length of 8 * 2048, however, the process is highly accurate.

In addition I worked on UI and tests. The UI is still terrible, but there's some time to fix it.

## What did I struggle with?

JavaFX is not my favorite thing, and apparently it's not included in Oracle's latest JDK? Maybe? I hacked it together but all in all it feels like a load of rocks held together by duct tape.

## Next steps

- Finalize UI: Give the user the power to play with block lengths, insert their own messages, decay rates, and so on.
- Lerp the mixer signal: round the transitions between ones and zeros so as to soften the shift between ones and zeroes. In addition, it would be _pretty swell_ to let the user choose whether the mixer signal should be lerped or not.
- Cleanup: remove comments and bloat, useless methods and variables.