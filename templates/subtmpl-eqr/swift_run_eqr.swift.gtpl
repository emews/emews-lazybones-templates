import io;
import sys;
import files;
import location;
import string;
import EQR;
import R;
import assert;

string emews_root = getenv("EMEWS_PROJECT_ROOT");
string turbine_output = getenv("TURBINE_OUTPUT");
string resident_work_ranks = getenv("RESIDENT_WORK_RANKS");
string r_ranks[] = split(resident_work_ranks,",");

string read_last_row = ----
  # TODO
  # change model_output to your output file
  last.row <- tail(read.csv("%s/model_output.csv"), 1)
  # TODO change B to your column header
  # B is the column header
  res <- last.row['B']
----;

app (file out, file err) run_model (string model_sh, string param_line, string instance)
{
    "bash" model_sh param_line emews_root instance @stdout=out @stderr=err;
}

(float result) get_result(string instance_dir) {
  // Use a few lines of R code to read the output file
  // See the read_last_row variable above
  string r_code = read_last_row % instance_dir;
  result = tofloat(R(r_code, "toString(res)"));
}

(float result) run_obj(string param_line, string id_suffix)
{
    // make instance dir
    string instance_dir = "%s/instance_%s/" % (turbine_output, id_suffix);
    make_dir(instance_dir) => {
      file out <instance_dir + "out.txt">;
      file err <instance_dir + "err.txt">;
      string model_sh = ${model_sh};
      (out,err) = run_model(model_sh, param_line,instance_dir) =>
      result = get_result(instance_dir) =>
      // delete the instance directory as it is no longer needed
      // if it is needed then delete this line
      rm_dir(instance_dir);
    }
}

(string parameter_combos[]) create_parameter_combinations(string params, int trials) {
  // TODO
  // Given the parameter string and the number of trials for that
  // those parameters, create an array of parameter combinations
  // Typically, this involves at least appending a different random
  // seed to the parameter string for each trial
}

(float agg_result) obj(string params, int trials, string iter_indiv_id) {
    float fresults[];
    string parameter_combos[] = create_parameter_combinations(params, trials);
    foreach f,i in parameter_combos {
        string id_suffix = "%s_%i" % (iter_indiv_id,i);
        fresults[i] = run_obj(f, id_suffix);
    }
    // TODO: Return a result
    // calculate some aggregate result from the combined trial results
    // in fresults, e.g.  avg(fresults);
    // ?? should this be a single float
    agg_result = 0;
}

(void v) loop(location ME, int ME_rank, int trials) {

    for (boolean b = true, int i = 1;
       b;
       b=c, i = i + 1)
  {
    string params =  EQR_get(ME);
    boolean c;

    // TODO
    // Edit the finished flag, if necessary.
    // when the python algorithm is finished it should
    // pass "DONE" into the queue, and then the
    // final set of parameters. If your python algorithm
    // passes something else then change "DONE" to that
    if (params == "DONE")
    {
      string finals =  EQR_get(ME);
      // TODO if appropriate
      // split finals string and join with "\\\\n"
      // e.g. finals is a ";" separated string and we want each
      // element on its own line:
      // multi_line_finals = join(split(finals, ";"), "\\\\n");
      string fname = "%s/final_result_%i" % (turbine_output, ME_rank);
      file results_file <fname> = write(finals) =>
      printf("Writing final result to %s", fname) =>
      // printf("Results: %s", finals) =>
      v = make_void() =>
      c = false;
    }
    else
    {
      ${run_block}
    }
  }
}

(void o) start(int ME_rank, int num_variations, int random_seed) {
    location ME = locationFromRank(ME_rank);
    // TODO: Edit algo_params to include those required by the R
    // algorithm.
    // algo_params are the parameters used to initialize the
    // R algorithm. We pass these as a comma separated string.
    // By default we are passing a random seed. String parameters
    // should be passed with a \"%s\" format string.
    // e.g. algo_params = "%d,%\"%s\"" % (random_seed, "ABC");
    string algo_params = "%d" % random_seed;
    string algorithm = strcat(emews_root,"/R/${r_algorithm}");
    EQR_init_script(ME, algorithm) =>
    EQR_get(ME) =>
    EQR_put(ME, algo_params) =>
    loop(ME, ME_rank, num_variations) => {
        EQR_stop(ME) =>
        EQR_delete_R(ME);
        o = propagate();
    }
}

// deletes the specified directory
app (void o) rm_dir(string dirname) {
  "rm" "-rf" dirname;
}

// call this to create any required directories
app (void o) make_dir(string dirname) {
  "mkdir" "-p" dirname;
}

// anything that need to be done prior to a model runs
// (e.g. file creation) can be done here
//app (void o) run_prerequisites() {
//
//}

main() {

  // TODO
  // Retrieve arguments to this script here
  // these are typically used for initializing the R algorithm
  // Here, as an example, we retrieve the number of variations
  // (i.e. trials) for each model run, and the random seed for the
  // R algorithm.
  int num_variations = toint(argv("nv", "1"));
  int random_seed = toint(argv("seed", "0"));

  assert(strlen(emews_root) > 0, "Set EMEWS_PROJECT_ROOT!");

  int ME_ranks[];
  foreach r_rank, i in r_ranks{
    ME_ranks[i] = toint(r_rank);
  }

  //run_prerequisites() => {
    foreach ME_rank, i in ME_ranks {
      start(ME_rank, num_variations, random_seed) =>
      printf("End rank: %d", ME_rank);
    }
//}
}
