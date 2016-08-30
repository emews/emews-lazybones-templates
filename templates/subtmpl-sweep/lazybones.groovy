import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

def isInt(val, int min) {
  if (val.isInteger()) {
    return val.toInteger() >= min
  }
  return false
}

def params = [:]
params.model_name = ask("Model Name? ", null, "model_name")
params.model_name = params.model_name.replace(' ', '_')
//params.model_sh = include(templateDir.path + "/ask_model_sh.groovy",
//  params)

def yn = ask("Create model script file? (Y/n) ", "Y", "create_model_sh")
while (!(yn.capitalize().equals("Y") || yn.capitalize().equals("N"))) {
  yn = ask("Create model script file? (Y/n) ", "Y", "create_model_sh")
}

params.model_sh = params.model_name + ".sh"
if (yn.capitalize().equals("Y")) {
  model_sh_file = new File(FilenameUtils.concat(projectDir.path,
    "scripts"), params.model_name + ".sh")
    FileUtils.copyFile(new File(templateDir, "model.sh.gtpl"), model_sh_file)
    println "Created script file for running the model ./${FilenameUtils.normalize(model_sh_file.path)}"
} else {
  // This should be already in the scripts directory
  params.model_sh = ask("Model script file name? ", null, "model_sh")
}

params.input_file = ask("Input file path? (\$EMEWS_PROJECT_ROOT/data/input.txt) ",
  "\$EMEWS_PROJECT_ROOT/data/input.txt", "input_file")

params.proc_count = ask("Process count for non-cluster run? (2) ", '2', "process_count")
while (!isInt(params.proc_count, 2)) {
  params.proc_count = ask("Process count for non-cluster run? (2) ", '2', "process_count")
}

processTemplates("swift_run_sweep.sh", params)
processTemplates("swift_run_sweep.swift", params)

def files = ['swift_run_sweep.sh', 'swift_run_sweep.swift']
for (f in files) {
  def dest = new File(projectDir, FilenameUtils.concat("swift", f))
  def source = new File(templateDir, f)
  FileUtils.moveFile(source, dest)
  if (f.endsWith(".sh")) {
    dest.setExecutable(true)
  }
  println "Created ./${FilenameUtils.normalize(dest.path)}"
}
