# patrn

**patrn** aims to bring the functionality of SuperCollider's [Pattern][1] system to
Clojure whilst requiring the programmer to learn as little as possible library
specific vocabulary. 

Clojure provides a great many functions for the manipulation of sequences of
values and **patrn** encourages the user to leverage their power where
possible.

# Usage

## Getting started with patrn and Overtone

Create a new Clojure project at the command line with `lein`:
```
lein new plinky-plonky
```
Add the following to your `project.clj` `:dependancies`:
```clojure
[overtone "0.9.1"]
[patrn "0.0.1-SNAPSHOT]
```
And plop something this in your `src/plinky_plonkey/core.clj` file:
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

## Analogues

Clojure provides analogues to many of SuperCollider's pattern definition
functions in its core library.

#### List patterns

In SuperCollider list patterns are specified as follows:
```
Pseq([1, 2, 3])
```

**patrn** enables list patterns to be specified as vectors.
Clojure:
```clojure
[1 2 3]
```

`Pseq` allows a number of repetitions to be specified for a sequence of values.
This can be achieved with Clojure's built-in `repeat` function. 
i.e. in SuperCollider repeating the sequence `[1 2 3]` three times can be
achieved as follows:
```
Pseq([1, 2, 3], 3)
```
A similar result can be achieved in Clojure through the use of `repeat`.
```clojure
(repeat 3 [1 2 3]))
```

It's a slightly different procedure to create an infinite sequence, Clojure's
built-in `cycle` function can be used for this.
```clojure
(cycle [1 2 3])
```

SuperCollider's `Pser` which cycles through the items in the list until repeats
items have been output can be emulated by combining Clojure's `take` and
`cycle` functions.
```
Pser([1, 2, 3], 4)
```

```clojure
(take 4 (cycle [1 2 3]))
```


### Arithmetic and geometric series

#### Pseries -> range

SuperCollider:
```
Pseries(start, step, length)
```

Clojure:
```clojure
(range start end step)
```


## How to run the tests

The project uses [Midje](https://github.com/marick/Midje/) for testing.

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.
