R_HOME=$HOME/sfw/R-3.0.1

CPPFLAGS=""
CPPFLAGS+="-I$HOME/sfw/tcl-8.6.0/include "
CPPFLAGS+="-I$R_HOME/lib/R/include "
CPPFLAGS+="-I$R_HOME/lib/R/library/RInside/include "
CPPFLAGS+="-I$R_HOME/lib/R/library/Rcpp/include"
CXXFLAGS=$CPPFLAGS

LDFLAGS=""
LDFLAGS+="-L$R_HOME/lib/R/library/RInside/lib -lRInside "
LDFLAGS+="-L$R_HOME/lib/R/lib -lR -lRblas  "
LDFLAGS+="-Wl,-rpath -Wl,$R_HOME/lib/R/lib "
LDFLAGS+="-Wl,-rpath -Wl,$R_HOME/lib/R/library/RInside/lib"

export CPPFLAGS CXXFLAGS LDFLAGS
