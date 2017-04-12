lazybones create emews 1.1 eqpy_test

 replace obj with:

 (float agg_result) obj(string params, int trials, string iter_indiv_id) {
   printf("swift received: %s", params);
   agg_result = string2float(params) + 1.5;
 }

 Output should look like, although not necessarily in that order

Params: 0
swift received: 1
swift received: 2
swift received: 3
swift received: 33
swift received: 34
swift received: 35

To failure handling:

update the swift script to use algorithm_fail as the package:

Output should look like 

swift received: 1
swift received: 2
swift received: 3
EQR Aborted
Traceback (most recent call last):
  File "/Users/nick/Documents/repos/emews-lazybones-templates/test/eqpy_test/ext/EQ-Py/eqpy.py", line 39, in run
    self.runnable.run()
  File "/Users/nick/Documents/repos/emews-lazybones-templates/test/eqpy_test/python/algorithm_fail.py", line 14, in run
    eqpy.OUT_put("33;34;35" + 1)
TypeError: Can't convert 'int' object to str implicitly
