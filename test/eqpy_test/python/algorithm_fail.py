
# ALGORITHM.PY

# Provides parameters to and accepts input from Swift tasks

import eqpy

def run():
    eqpy.OUT_put("Hello From Algorithm")
    params = eqpy.IN_get()
    print("Params: {}".format(params))
    eqpy.OUT_put("1;2;3")
    result = eqpy.IN_get()
    eqpy.OUT_put("33;34;35" + 1)
    result = eqpy.IN_get()
    eqpy.OUT_put("DONE")
