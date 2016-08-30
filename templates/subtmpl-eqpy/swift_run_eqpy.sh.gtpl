export EMEWS_PROJECT_ROOT=$PWD/..
export TURBINE_OUTPUT=$EMEWS_PROJECT_ROOT/experiments/model
# QUEUE, WALLTIME, PROCS, PPN, AND TURNBINE_JOBNAME will
# be ignored if the -m scheduler flag is not used
export QUEUE=batch
export WALLTIME=00:10:00
export PROCS=4   # number of processes
export PPN=16
export TURBINE_JOBNAME="model_job"
# if R cannot be found, then these will need to be
# uncommented and set correctly.
#export R_HOME=/soft/R/src/R-3.2.2/
#export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/soft/R/src/R-3.2.2/lib/
export PYTHONPATH=$EMEWS_PROJECT_ROOT/python:$EMEWS_PROJECT_ROOT/ext/EQ-Py

# Resident task workers and ranks
export TURBINE_RESIDENT_WORK_WORKERS=1
export RESIDENT_WORK_RANKS=$(( PROCS - 2 ))

# EQ/Py location
EQPY=$EMEWS_PROJECT_ROOT/ext/EQ-Py

# TODO edit command line arguments, e.g. -nv etc., as appropriate
# for your EQ/Py based run.
CMD_LINE_ARGS=" -nv=5 -seed=0"

# pbs scheduler run.
#swift-t -m pbs -p -I $EQPY -r $EQPY eqpy_run.swift$CMD_LINE_ARGS

# Run immediately without a scheduler.
swift-t -n $PROCS -p -I $EQPY -r $EQPY eqpy_run.swift $CMD_LINE_ARGS
