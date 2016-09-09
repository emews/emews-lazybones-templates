#! /usr/bin/env bash

set -eu

if [ "\$#" -ne 1 ]; then
  script_name=\$(basename \$0)
  echo "Usage: \${script_name} EXPERIMENT_ID (e.g. \${script_name} experiment_1)"
  exit 1
fi


# uncomment to turn off swift/t logging
# export TURBINE_LOG=0 TURBINE_DEBUG=0 ADLB_DEBUG=0
export EMEWS_PROJECT_ROOT=\$( cd \$( dirname \$0 )/.. ; /bin/pwd )
export EXPID=\$1
export TURBINE_OUTPUT=\$EMEWS_PROJECT_ROOT/experiments/\$EXPID
export PROCS=${proc_count}  # number of processes

# QUEUE, WALLTIME, PPN, AND TURNBINE_JOBNAME will
# be ignored if the -m scheduler flag is not used
export QUEUE=batch
export WALLTIME=00:10:00
export PPN=16
export TURBINE_JOBNAME="\${EXPID}_job"

# if R cannot be found, then these will need to be
# uncommented and set correctly.
# export R_HOME=/soft/R/src/R-3.2.2/
# export LD_LIBRARY_PATH=\$LD_LIBRARY_PATH:/soft/R/src/R-3.2.2/lib/

# set machine to your schedule type (e.g. pbs, slurm, cobalt etc.),
# or empty for an immediate non-queued unscheduled run
MACHINE=""

if [ -n "\$MACHINE" ]; then
  MACHINE="-m \$MACHINE"
fi

# echo's anything following this standard out
set -x

swift-t -n \$PROCS \$MACHINE -p \$EMEWS_PROJECT_ROOT/swift/swift_run_sweep.swift -f="${input_file}"
