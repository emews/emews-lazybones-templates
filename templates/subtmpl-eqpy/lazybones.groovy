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
params.model_sh = "emews_root + \"/scripts/model.sh\""

params.python_package = ask("Python package? (python_package_placeholder) ",
  "python_package_placeholder", "python_package")

def yn = ask("Does the python algorithm produce multiple sets of parameters? (Y/n) ",
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

def swift_script_name = ask("Swift script name? (eqpy_run.swift)", "eqpy_run.swift",
  "swift_script_name")

processTemplates("eqpy_run.swift", params)

files = [['eqpy_run.swift', swift_script_name]]
for (f in files) {
  def dest = new File(projectDir, FilenameUtils.concat("swift", f[1]))
  def source = new File(templateDir, f[0])
  FileUtils.moveFile(source, dest)
  if (f[0].endsWith(".sh")) {
    dest.setExecutable(true)
  }
  println "Created ./${FilenameUtils.normalize(dest.path)}"
}
