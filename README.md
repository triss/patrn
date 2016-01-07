# patrn

**patrn** aims to bring the functionality of SuperCollider's Pattern system to
Clojure whilst requiring the programmer to learn as little as possible library
specific vocabulary. 

Clojure provides a great many functions for the manipulation of sequences of
values and **patrn** encourages the user to leverage their power where
possible.

# Usage

Clojure provides analogues to many of SuperCollider's pattern definition
functions in its core library.

## Analogues

#### List patterns

**patrn** enables list patterns to be specified as vectors.

SuperCollider:
```
Pseq([1, 2, 3])
```

Clojure:
```clojure
[1 2 3]
```

`Pseq` allows a number of repetitions to be specified for the sequence of
values. This can be achieved with composition of `concat` and `repeat`. 
i.e. in SuperCollider, repeating the sequence `[1 2 3]` three times can be
achieved as follows:
```
Pseq([1, 2, 3], 3)
```
A similar result can be achieved in Clojure through the use of `apply`, `concat`
and `repeat`.
```clojure
(apply concat (repeat 3 [1 2 3]))
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
