# Weekly report 3

## Summary

I was super tired and the math is confusing so I worked less this week, and most of what I did do centered around understanding what I'm going to implement.

## Overall status

Last week I achieved an important milestone with the implementation of LSB, but this happened at the expense of my energy. I'm worried about the complexities involved with echo hiding. Encoding could be possible in a week or two, but autocepstrum analysis could take a while.

I haven't started working on an UI yet, this needs to be rectified ASAP.

## What did I do this week?

- Read up on echo hiding, cepstrum analysis, and Fourier transforms
- Found a library to use with Fourier transforms until I'm capable of implementing them myself
- Began work on transforming little-endian byte arrays to shorts and floats so I can feed them to the magical Fourier machine.

## What did I struggle with?

- The maths involved is fairly complicated and completely new to me. I'm flying blind here and could use some pointers.
- Balancing work and studies has been harder this week.
- I find it very hard to measure the difficulty of each piece of work. I.e. LSB turned out to be a relatively quick thing, and echo hiding (encoding) could surprise in that manner too - but optimism is dangerous, and so I find myself in doubt.

## Next steps

- finish and test converting byte data to float arrays
- A reasonable first attempt at encoding data with echo hiding.
- test fourier transforms.
