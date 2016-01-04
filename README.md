# patrn

**patrns** describe calculations without stating every step.
They are a higher-level representation of a computational task.
While not ideally suited to every calculation they free the user from worrying
about every detail of the process.
Using **patrns** one writes *what* is supposed to happen, rather than *how* to
accomplish it.

The project uses [Midje](https://github.com/marick/Midje/).

## How to run the tests

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.
