# lazybones-templates
Lazybones templates for emews projects.

See https://github.com/pledbrook/lazybones for lazybones install info

The source for the templates are in the template folder. To install the templates so that they can be used via lazybones:
```bash
./gradlew installTemplateEmews
```

The repository contains an *emews* template and 3 subtemplates *sweep*, *eqpy*, and *eqr*. The command

```bash
lazybones create emews 0.0 X
```

will create an EMEWS project structure in director *X*, using version 0.0 of the template. For example

```bash
lazybones create emews 0.0 swift_project
```

will create the project structure in swift_project.

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

 * data - model input etc. data 
 * ext - swift-t extensions such as eqpy, eqr
 * python - python code (e.g. model exploration algorithms written in python)
 * python\test - tests of the python code
 * R - R code (e.g. model exploration algorithms written R)
 * R\test - tests of the R code
 * scripts - any necessary scripts (e.g. scripts to launch a model), excluding scripts used to run the workflow.
 * swift - swift code
 
The subtemplates will populate these directories with files appropriate for the type of workflow associated with that subtemplate. The subtemplates are executed with the command:

```
lazybones generate X
```

where X is the subtemplate name: 


by cd'ing into the top directory created by the *emews* template and 

### Sweep ###

cd $HOME/Documents/repos/emews-templates
./g
cd $root
rm -rf .lazybones data ext python R scripts swift Readme.md 
cd ..
l
cd $root
for var in "$@"
do
  lazybones generate $var



