# Troubleshooting EMEWS Templates #

## EQ/Py ##

### Error: wrong # args ###

Your workflow fails with:

```
CAUGHT ERROR:
wrong # args: should be "turbine::python persist exceptions_are_errors code expression"
    while executing
"turbine::python 1 ${v:code:1:1} "\"\"""
    (procedure "_void_py-argwait" line 3)
    invoked from within
...
```

This can occur when running older version EQ/Py with a newer swift. 
In `ext/EQ-Py/EQPy.swift`, change:

```
[ "turbine::python 1 <<code>> <<expr>> "];
```

to

```
[ "turbine::python 1 1 <<code>> <<expr>> "];
```

by adding an additional 1 in the argument list.

Change

```
[ "set <<output>> [ turbine::python 1 <<code>> <<expr>> ]" ];
```

to

```
[ "set <<output>> [ turbine::python 1 1 <<code>> <<expr>> ]" ];
```

again by adding an additional 1 to the argument list.
   
