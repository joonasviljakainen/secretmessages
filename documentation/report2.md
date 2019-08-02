# Weekly report #2

[Hour reporting](/documentation/hours.md)

## What did I do?

- Implemented simple LSB encoding and decoding - ASCII data can now be hidden and decoded.
- Wrote tests for almost all classes - see the near-useless [testing document](/documentation/testing.md)
- Did some Javadoc.
- I restructured the document so as to make components reusable - I'll need a lot of the same stuff with Echo hiding as I did with LSB.

## Project status

Implementing LSB was faster than I thought. I'll start work on echo hiding as soon as possible as it seems really interesting.

I think it might be a good idea to extend LSB a little. For example give the user the option to choose how many channels are used. Right there's just the default setting, I use one channel only.

## What did I struggle with?

I didn't do much pen-and-paper work, which created problems. I'll try to do that more. All in all I think I need to do more research before I start coding - the total time spent will almost certainly be smaller if my sessions are planned better.

## What am I going to do now?

- Some kind of interface so that users can actually make use of the algorithm - probably a CLI at first.
- Echo hiding and decoding - I'll start by doing as much research as possible and start coding a bit later.
- Come up with ideas for extensions for this project. If it should happen that I manage to implement echo hiding and decoding quickly, I'll want to be able to make further refinements or comparisons between algorithms.
- Write more extensive tests for my current classes.