# patrn

**patrn**s describe calculations without stating every step.
They are a higher-level representation of a computational task.
While not ideally suited to every calculation they free the user from worrying
about every detail of the process.
Using **patrns** one writes *what* is supposed to happen, rather than *how* to
accomplish it.

# Usage

Clojure provides analogs to many of SuperCollider's pattern definition
functions in its core library.

## Analogs

### Pseries -> range

SuperCollider:
```
Pseries(start, step, length)
```

Clojure:
```clojure
(range start end step)
```

### Pseq([1, 2, 3]) -> []

SuperCollider:
```
Pseq([1, 2, 3])
```

Clojure:
```clojure
[1 2 3]
```

## How to run the tests

The project uses [Midje](https://github.com/marick/Midje/) for testing.

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.
