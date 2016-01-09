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

SuperCollider        | Clojure & patrn         | Result
------------------   | ----------------------- | ------------------------------
Pseq([1, 2, 3])      | [1 2 3]                 | [1 2 3]  
Pseq([1, 2, 3], 4)   | (repeat 4 [1 2 3])      | [1 2 3 1 2 3 1 2 3 1 2 3]
Pseq([1, 2, 3], inf) | (cycle [1 2 3])         | [1 2 3 1 2 3 1 2 3 ...
Pser([1, 2, 3], 4)   | (take 4 (cycle [1 2 3]) | [1 2 3 1]

#### List patterns

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
