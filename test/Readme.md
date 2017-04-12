# Tests of EQR and EQP #

These are intended to test the eqpy and eqr templates with simple
python and R code that pass values between swift and python / R via
the queues.

## EQ/Py ##

To run the eqpy test, do the following in the test directory. Make sure
you've installed the latest templates into lazybones.

```bash
cp -R eqpy eqpy_test
lazybones create emews 1.1 eqpy_test
cd eqpy_test
lazybones generate eqpy (specify algorithm as the python package)
```

In the swift script that is created as part of the eqpy template, replace
the obj function with:

```java
 (float agg_result) obj(string params, int trials, string iter_indiv_id) {
   printf("swift received: %s", params);
   agg_result = string2float(params) + 1.5;
 }
 ```

 Run the wokflow using the generated launch script. You may need to set
 PYTHONPATH in the launch script if the workflow fails. The output should look
like the following, although not necessarily in this order.

```
Params: 0
swift received: 1
swift received: 2
swift received: 3
swift received: 33
swift received: 34
swift received: 35
```

To test failure handling:

Update the swift script to use algorithm_fail as the package, replacing

```
EQPy_init_package(ME,"algorithm")
```

with

```
EQPy_init_package(ME,"algorithm_fail")
```

Run the launch script and the output should look like:

```
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
```

## EQ/R ##

To run the eqr test, do the following in the test directory. Make sure
you've installed the latest templates into lazybones.

```bash
cp -R eqr eqr_test
lazybones create emews 1.1 eqr_test
cd eqr_test
lazybones generate eqr (specify algorithm.R as the R ME algorithm)
```

In the swift script that is created as part of the eqr template, replace
the obj function with:

```java
 (float agg_result) obj(string params, int trials, string iter_indiv_id) {
   printf("swift received: %s", params);
   agg_result = string2float(params) + 1.5;
 }
 ```

 Run the wokflow using the generated launch script. The output should look
like the following, although not necessarily in this order.

```
swift received: 1
swift received: 2
swift received: 3
swift received: 33
swift received: 34
swift received: 35
...
```

To test failure handling:

Update the swift script to use algorithm_fail as the package, replacing

```
string algorithm = strcat(emews_root,"/R/algorithm.R");
```

with

```
string algorithm = strcat(emews_root,"/R/fail.R");
```

Run the launch script and the output should look like:

```
swift received: 1
swift received: 2
swift received: 3
Error in "33" + 3 : non-numeric argument to binary operator
EQR aborted: see output for R error
Error evaluating:
# Provides parameters to and accepts input from Swift tasks
OUT_put("Hello From Algorithm")
params = IN_get()
OUT_put("1;2;3")
result = IN_get()
OUT_put("33" + 3)
result = IN_get()
OUT_put("DONE")
OUT_put("final result")
```
