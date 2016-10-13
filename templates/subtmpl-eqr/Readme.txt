Re. running autotools from with the post-install lazybones scripts,

* build.sh runs bootstrap, configure, etc.
* If configure halts with

checking for g++... g++
checking whether the C++ compiler works... no
configure: error: in `/home/nick/Documents/emews-test/ext/EQ-R/eqr':
configure: error: C++ compiler cannot create executables
See `config.log' for more details

That usually means that LDFLAGS in build.sh points to a non-existent library
or directory. 
