.. _instrumenting-software-c:

Instrumenting C and other Native Programming Languages 
======================================================

We provide experimental C language support for Kieker. The current library
supports all Kieker record types. In case you want to roll your own, you can
use the C generator of the Kieker instrumentation record language (IRL).

Instrumentation
---------------

Instrumentation with Compiler Hook
""""""""""""""""""""""""""""""""""

You can instrument C, C++ and Fortran application (or any other language
supported by GCC) with the kieker-lang-pack-c. To enable instrumentation you
have to specify -finstrument-functions as compiler option to instrument all
functions, add libkieker to the library path and add -lkieker to the linker
options.

To limit instrumentation you may use 

-finstrument-functions-exclude-file-list
-finstrument-functions-exclude-function-list

More details can be found here: 
https://gcc.gnu.org/onlinedocs/gcc-4.4.4/gcc/Code-Gen-Options.html

Depending on the configure and make setup used in your C, C++, Fortran project
your setup could look like this:

export LIBS=-lkieker
export LDFLAGS=-L/my/directory/lib/
export CFLAGS=-finstrument-functions

Kieker supports two environment variables:
- KIEKER_HOSTNAME
- KIEKER_PORT
which must be set to a port (default 5678) and a hostname (e.g. localhost)
specifying the computer receiving the monitoring data.

Use the Kieker collector to receive the data.
https://kieker-monitoring.readthedocs.io/en/latest/kieker-tools/Collector.html


AspectC++ AOP
"""""""""""""

There is initial code using aspectC++ for instrumentation. We have incorporated
that code in the source tree. You may find the aspect definitions under
`libkieker/include/aspectc++`. However, we have not yet tested them.

Installation
------------

The Kieker language pack can be found here:
`https://github.com/kieker-monitoring/kieker-lang-pack-c`

After checkout, you may have to perform the following four operations before you
can compile the library and the sample programs.

Prepare
"""""""

- `libtoolize`
- `aclocal`
- `automake --add-missing`
- `autoconf`

Compile
"""""""

In case you can compile and place the library in its default location type:

`./configure ; make ; make install`

In case you want to set a different installation location call configure
with the option `--prefix=/my/directory`

See `./configure --help` for details.

Examples
--------

Example instrumented programs can be found in `examples`.

- `basic-example` shows a manual instrumentation.

