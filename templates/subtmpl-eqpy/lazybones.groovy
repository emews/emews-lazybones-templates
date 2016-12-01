import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

def files = ['eqpy.py', 'EQPy.swift']
for (f in files) {
  def dest = new File(projectDir.path + "/ext/EQ-Py", f)
  def source = new File(templateDir, f)
  FileUtils.copyFile(source, dest)
  println "Created ./${FilenameUtils.normalize(dest.path)}"
}

def run_1 = '''
        string param_array[] = split(params, ";");
        float results[];
        foreach p, j in param_array
        {
            results[j] = obj(p, trials, "%i_%i_%i" % (ME_rank,i,j));
        }

        string rs[];
        foreach result, k in results
        {
            rs[k] = fromfloat(result);
        }
        string res = join(rs, ",");
        EQPy_put(ME, res) => c = true;
'''

def run_2 = '''
      float result = obj(params, trials, "%i_%i" % (ME_rank,i));
      EQPy_put(ME, fromfloat(result)) = > c = true;
'''


def params = [:]

// This should be replaced with include functionalty when lazybones release updated
// with that
params.model_name = ask("Model Name? ", null, "model_name")
params.model_name = params.model_name.replace(' ', '_')

def yn = ask("Create model script file? (Y/n) ", "Y", "create_model_sh")
while (!(yn.capitalize().equals("Y") || yn.capitalize().equals("N"))) {
  yn = ask("Create model script file? (Y/n) ", "Y", "create_model_sh")
}

params.model_sh = "eqpy_" + params.model_name + ".sh"
if (yn.capitalize().equals("Y")) {
  model_sh_file = new File(FilenameUtils.concat(projectDir.path,
    "scripts"), params.model_sh)
    FileUtils.copyFile(new File(templateDir, "model.sh.gtpl"), model_sh_file)
    println "Created script file for running the model ./${FilenameUtils.normalize(model_sh_file.path)}"
} else {
  // This should be already in the scripts directory
  params.model_sh = ask("Model script file name? ", null, "eqpy_model.sh")
}
// end chunk to replace

// model_sh in template is the  path
params.model_sh = "emews_root + \"/scripts/${params.model_sh}\""

params.python_package = ask("Python package? (python_package_placeholder) ",
  "python_package_placeholder", "python_package")

yn = ask("Does the python algorithm produce multiple sets of parameters? (Y/n) ",
"Y", "parameter_sets")
while (!(yn.capitalize().equals("Y") || yn.capitalize().equals("N"))) {
    yn = ask("Does the python algorithm produce multiple sets of parameters? (Y/n) ",
    "Y", "parameter_sets")
}
if (yn.equals("Y")) {
  params.run_block = run_1
} else {
  params.run_block = run_2
}

params.swift_script_name = ask("Swift script name? (swift_run_eqpy.swift) ",
  "swift_run_eqpy.swift",  "swift_script_name")

params.swift_sh_name = ask("Swift launch script name? (swift_run_eqpy.sh) ",
  "swift_run_eqpy.sh", "swift_launch_script_name")

processTemplates("swift_run_eqpy.swift", params)
processTemplates("swift_run_eqpy.sh", params)

files = [['swift_run_eqpy.swift', params.swift_script_name],
  ['swift_run_eqpy.sh', params.swift_sh_name ]]

for (f in files) {
  def dest = new File(projectDir, FilenameUtils.concat("swift", f[1]))
  def source = new File(templateDir, f[0])
  FileUtils.moveFile(source, dest)
  if (f[0].endsWith(".sh")) {
    dest.setExecutable(true)
  }
  println "Created ./${FilenameUtils.normalize(dest.path)}"
}
