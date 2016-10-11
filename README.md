# lazybones-templates
Lazybones templates for emews projects.

See https://github.com/pledbrook/lazybones for lazybones install info

## Installing the Templates ##

The source for the templates is in the template folder. To install the templates so that they can be used via lazybones:
```bash
./gradlew installTemplateEmews
```

## Running the Templates ##

The repository contains an *emews* template and 3 subtemplates *sweep*, *eqpy*, and *eqr*. The command

```bash
lazybones create emews 0.0 X
```

will create an EMEWS project structure in director *X*, using version 0.0 of the template. For example

```bash
lazybones create emews 0.0 swift_project
```

will create the default EMEWS project structure in a swift_project directory.

```
swift_project\
  data\
  ext\
  python\
    test\
  R\
    test\
  scripts\
  swift\
  README.md
```
The directories are intended to contain the following:

 * `data` - model input etc. data
 * `ext` - swift-t extensions such as eqpy, eqr
 * `python` - python code (e.g. model exploration algorithms written in python)
 * `python\test` - tests of the python code
 * `R` - R code (e.g. model exploration algorithms written R)
 * `R\test` - tests of the R code
 * `scripts` - any necessary scripts (e.g. scripts to launch a model), excluding scripts used to run the workflow.
 * `swift` - swift code

The subtemplates will populate these directories with files appropriate for the
type of workflow associated with that subtemplate. The subtemplates are executed
with the command:

```
lazybones generate X
```

where X is the subtemplate name: *sweep*, *eqpy*, or *eqr*. Once the
subtemplate has been generated the files can be edited to suit the
current purpose. The files themselves contain comments to guide the
customization. The subtemplates must be generated from within the directory
created by the top-level *emews* template. Subtemplate generation will query
the user for additional information (e.g. the model name) required to
generate the template.

### Sweep ###

The *sweep* subtemplate generates files appropriate for a sweep through
a collection of parameters, running the model for each set of parameters. By
default, the parameters are defined in a input file where each line is a
set of parameters to run.

  * `swift/swift_run_sweep.sh` - bash script used to launch the workflow
  * `swift/swift_run_sweep.swift` - swift script that will iterate through an
  input file, passing each line of that input to a model. The model is called
  from an app function.
  * (optional) `scripts/X.sh` - the actual name of this file is derived from user
  input during the subtemplate generation. The file is a bash script that
  should be edited to call the model. The app function in
  swift_run_sweep.swift calls this file.

### EQ/Py ###

The *eqpy* subtemplate generates files and code for model exploration using
the EQ/Py EMEWS extension. This extension allows model runs and exploration
to be controlled from a python code algorithm.

The template installs the EQ/Py extension in ext/EQ-Py. The extension consists
of two files.

  * `ext/EQ-Py/eqpy.py`
  * `ext/EQ-Py/EQPy.swift`

These should not need to be edited by the user.

The remaining files are generated in the `swift/` directory. Note that the file
names are determined by user input and the names below are defaults.

  * `swift/swift_run_eqpy.sh` - bash script used to launch the EQ/Py-based
  workflow.
  * `swift/swift_run_eqpy.swift` - a skeleton swift script for model exploration
  runs whose parameters are generated from a python algorithm. See the
  TODOs in the script for additional instructions.

### EQ/R ###

The *eqr* subtemplate ...
