export EMEWS_PROJECT_ROOT=\$PWD/..
export TURBINE_OUTPUT=\$EMEWS_PROJECT_ROOT/experiments/${model_name}
# QUEUE, WALLTIME, PROCS, PPN, AND TURNBINE_JOBNAME will
# be ignored if the -m scheduler flag is not used
export QUEUE=batch
export WALLTIME=00:10:00
export PROCS=1   # number of processes
export PPN=16
export TURBINE_JOBNAME="${model_name}_job"
# if R cannot be found, then these will need to be
# uncommented and set correctly.
#export R_HOME=/soft/R/src/R-3.2.2/
#export LD_LIBRARY_PATH=\$LD_LIBRARY_PATH:/soft/R/src/R-3.2.2/lib/

# pbs scheduler run.
#swift-t -m pbs -p swift_run_sweep.swift -f="${input_file}"

# Run immediately without a scheduler.
# Change -n parameter to number of processes to use
swift-t -n ${proc_count} -p swift_run_sweep.swift -f="${input_file}"
