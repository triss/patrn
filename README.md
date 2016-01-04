# patrn

**patrn**s describe calculations without stating every step.
They are a higher-level representation of a computational task.
While not ideally suited to every calculation they free the user from worrying
about every detail of the process.
Using **patrn**s one writes *what* is supposed to happen, rather than *how* to
accomplish it.

# Usage

Clojure provides analogs to many of SuperCollider's pattern definition
functions in its core library.

## Analogs

#### List patterns

SuperCollider:
```
Pseq([1, 2, 3])
```

Clojure:
```clojure
[1 2 3]
```

`Pseq` allows a number of repetitions to be specified for the sequence of
values. This can be achieved with `p/rep-cat` (a simple composition of `concat`
and `repeat`). 
i.e. in SuperCollider, repeating the sequence `[1 2 3]` three times can be
achieved as follows:
```
Pseq([1, 2, 3], 3)
```
A similar result can be achieved with patrn's `rep-cat`.
```clojure
(p/rep-cat 3 [1 2 3])
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
