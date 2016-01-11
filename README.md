# patrn 
**patrn** brings the declarative functionality of [SuperCollider's
Pattern][sc-pattern] system to Clojure whilst requiring the programmer to
learn as little as possible library specific vocabulary. 

## Philosphy

**Simple** - pattern/event definition and playback decomplected.

**Easy** - get started quickly when using Overtone, Pink, MIDI, or OSC messages to render output.

**Declarative** - say *what* will occur not *how* it will happen.

Clojure provides a great many functions for the manipulation of sequences and
**patrn** encourages the user to leverage their power where possible.

# Usage

## Getting started 

### Playing back a patrn with Overtone
Create a new Clojure project at the command line with `lein`:
```
lein new plinky-plonky
```
Add the following to `:dependancies` vector in `plinky-plonky/project.clj`:
```clojure
[overtone "0.9.1"]
[patrn "0.0.1-SNAPSHOT]
```
And plop something this in your `src/plinky_plonky/core.clj` file:
```clojure
(ns plinky-plonky.core
  (:require [patrn.core :as p]
            [patrn.overtone :refer [play])
  (:use [overtone.live]))

(def a-flock-of-sea-gulls 
  (p/bind :degree   (take 8 (partition-all 3 1 (range -6 12 2)))
          :duration (repeat [0.2 0.2 0.1])
          :length   (repeat 0.45)))

(play a-flock-of-sea-gulls)
```
And run the expressions.

## SuperCollider -> patrn Analogues

**patrn**'s `patrn->seq` function and Clojure's core library do away with the need
for a large vocabulary of sequence generating functions as provided by
SuperCollider's Pattern classes.
Clojure provides analogues to many of SuperCollider's pattern definition
functions in its core library.

SuperCollider                    | Clojure & patrn                                 | `patrn->seq`
------------------               | -----------------------                         | --------------------------
`Pseq([1, 2, 3])`                | `[1 2 3]`                                       | `'(1 2 3)`  
`Pseq([1, 2, 3], 4)`             | `(repeat 4 [1 2 3])`                            | `'(1 2 3 1 2 3 1 2 3 1 2 3)`
`Pseq([1, 2, 3], inf)`           | `(cycle [1 2 3])`                               | `'(1 2 3 1 2 3 1 2 3 ...`
`Pser([1, 2, 3], 4)`             | `(take 4 (cycle [1 2 3]))`                      | `'(1 2 3 1)`
`Pseries(1, 3, 9)`               | `(take 9 (range 9 100 3))`                      | `'(1 4 7 10 13 16 19 22 25 28)``
`(-6,2..12)` 			 | `(range -6 12 2)`			 	   | `'(-6 -4 -2 0 2 4 6 8 10 12)`
`Pslide((-6,2..12), 8, 3, 1, 0)` | `(take 8 (partition-all 3 1 (range -16 12 2)))` | `'(-6 -4 -2 -4 -2 0 -2 0 2...)`


## How to run the tests

The project uses [Midje][midje] for testing. `lein midje` will run all tests.

[sc-pattern]: http://doc.sccode.org/Tutorials/A-Practical-Guide/PG_01_Introduction.html 
[midje]: https://github.com/marick/Midje/
[flatten]: https://mwfogleman.github.io/posts/20-12-2014-flatcat.html "A few implementations of flatten"

