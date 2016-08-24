#!/bin/bash
set -eux

CPPFLAGS=""
CPPFLAGS+="-I${tcl_include} "
CPPFLAGS+="-I${r_include} "
CPPFLAGS+="-I${rinside_include} "
CPPFLAGS+="-I${rcpp_include}"
CXXFLAGS=\$CPPFLAGS

LDFLAGS=""
LDFLAGS+="-L${rinside_lib} -lRInside "
LDFLAGS+="-L${r_lib} -lR "
LDFLAGS+="-Wl,-rpath -Wl,${r_lib} "
LDFLAGS+="-Wl,-rpath -Wl,${rinside_lib}"

cd "${working_directory}"

./bootstrap
CXXFLAGS=\$CPPFLAGS CPPFLAGS=\$CPPFLAGS LDFLAGS=\$LDFLAGS ./configure --prefix="${prefix}"
make clean
make install
