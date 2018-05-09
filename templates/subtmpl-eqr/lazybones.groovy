import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils


def isInt(val, int min) {
  if (val.isInteger()) {
    return val.toInteger() >= min
  }
  return false
}

def findFile(dirs, file) {
  for (d in dirs) {
    if (new File(d, file).exists()) return d
  }
  return ""
}

def findDirectory(dirs) {
  for (d in dirs) {
    if (new File(d).exists()) return d
  }
  return ""
}

def askFile(dirs, file, msg, ask_prop) {
  def file_default = findFile(dirs, file)
  def ask_default = file_default.size() > 0 ? "($file_default)" : ""
  return ask("$msg? $ask_default ", file_default, ask_prop)
}

def params = [:]
params.is_osx = System.getProperty("os.name").toLowerCase().contains("mac")
params.prefix = projectDir.canonicalPath + "/ext/EQ-R"
params.working_directory = params.prefix + "/eqr"

def files = ['BlockingQueue.h', 'bootstrap', "configure.ac", "EQR.cpp",
    "EQR.h", "EQR.i", "EQR.swift", "make-package.tcl", "Makefile.in",
    "settings.mk.in", "settings.template.sh", "COMPILING.txt"]
println "Copying EQ-R source to ${params.working_directory}"

for (f in files) {
   def dest = new File(params.working_directory, f)
   def source = new File(templateDir.path + "/eqr", f)
   FileUtils.copyFile(source, dest)
   if (f.equals("bootstrap")) {
     dest.setExecutable(true)
   }
 }

 def gignoreDest = new File(params.working_directory, ".gitignore")
 def gignoreSource = new File(templateDir.path + "/eqr", "gitignore.txt")
 FileUtils.copyFile(gignoreSource, gignoreDest)

def yn = ask("Build EQ/R ('n' to run autotools later)? (Y/n) ", "y", "build_eqr")
if (yn.equalsIgnoreCase('y')) {

  def tcl_files = ["/usr/include/tcl", "/usr/local/include/tcl"]
  params.tcl_include = askFile(tcl_files, "tcl.h", "Tcl headers ", "tcl_include")

  def r_files = ["/usr/share/R/include",
    "/Library/Frameworks/R.framework/Versions/3.4/Resources/include",
    "/Library/Frameworks/R.framework/Versions/3.3/Resources/include",
    "/Library/Frameworks/R.framework/Versions/3.2/Resources/include"]
  params.r_include = askFile(r_files, "R.h", "R headers ", "r_include")

  String[] command = ["Rscript", "-e", "cat(.libPaths(), sep=\",\")"].toArray()
  def process = new ProcessBuilder(command).redirectErrorStream(true).start()
  process.waitFor()
  def rdirs = process.text.split(",")
  params.rcpp_include = askFile(rdirs.collect{it + "/Rcpp/include"},
    "Rcpp.h", "Rcpp headers", "rcpp_include")
  params.rinside_include = askFile(rdirs.collect{it + "/RInside/include"},
    "RInside.h", "RInside headers ", "rinside_include")
  def rinside_lib = new File(new File(params.rinside_include).parent, "lib").path
  params.rinside_lib = ask("RInside libraries? (${rinside_lib}) ", rinside_lib,
    "rinside_lib")

  def rlib_dirs = ["/usr/lib/R/lib", "/usr/share/R/lib",
    "/Library/Frameworks/R.framework/Versions/3.4/Resources/lib",
    "/Library/Frameworks/R.framework/Versions/3.3/Resources/lib",
    "/Library/Frameworks/R.framework/Versions/3.2/Resources/lib"]
  def r_lib = findDirectory(rlib_dirs)
  params.r_lib = ask("R libraries? (${r_lib}) ", r_lib,
    "r_lib")

  processTemplates("build.sh", params)

  def dest =  new File(params.working_directory, "build.sh")
  def source = new File(templateDir, "build.sh")
  FileUtils.moveFile(source, dest)
  dest.setExecutable(true)

  process = new ProcessBuilder(dest.path).redirectErrorStream(true).start()
  process.inputStream.eachLine {println it}
  process.waitFor()

  if (!process.exitValue()) {
    println "\nSuccessfully compiled EQ/R library and swift script into\n" +
      "${params.prefix}"
  } else {
    println "\nError while compiling EQ/R library: see output for details and\n" +
    "COMPILING.txt for possible solutions."
  }
} else {
  println "See ext/EQ-R/eqr/COMPILING.txt for compilation instructions."
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
        string res = join(rs, ";");
        EQR_put(ME, res) => c = true;
'''

def run_2 = '''
      float result = obj(params, trials, "%i_%i" % (ME_rank,i));
      EQR_put(ME, fromfloat(result)) = > c = true;
'''

// This should be replaced with include functionalty when lazybones release updated
// with that
params.model_name = ask("Model Name? ", null, "model_name")
params.model_name = params.model_name.replace(' ', '_')

yn = ask("Create model script file? (Y/n) ", "Y", "create_model_sh")
while (!(yn.capitalize().equals("Y") || yn.capitalize().equals("N"))) {
  yn = ask("Create model script file? (Y/n) ", "Y", "create_model_sh")
}

params.model_sh = "eqr_" + params.model_name + ".sh"
if (yn.capitalize().equals("Y")) {
  model_sh_file = new File(FilenameUtils.concat(projectDir.path,
    "scripts"), params.model_sh)
    FileUtils.copyFile(new File(templateDir, "model.sh.gtpl"), model_sh_file)
    println "Created script file for running the model ./${FilenameUtils.normalize(model_sh_file.path)}"
} else {
  // This should be already in the scripts directory
  params.model_sh = ask("Model script file name? ", null, "eqr_model.sh")
}
// end chunk to replace

// model_sh in template is the  path
params.model_sh = "emews_root + \"/scripts/${params.model_sh}\""

params.r_algorithm = ask("R ME algorithm file? (my_algorithm.R) ",
  "my_algorithm.R", "r_algorithm")

yn = ask("Does the R algorithm produce multiple sets of parameters? (Y/n) ",
"Y", "parameter_sets")
while (!(yn.capitalize().equals("Y") || yn.capitalize().equals("N"))) {
    yn = ask("Does the R algorithm produce multiple sets of parameters? (Y/n) ",
    "Y", "parameter_sets")
}
if (yn.equals("Y")) {
  params.run_block = run_1
} else {
  params.run_block = run_2
}

params.swift_script_name = ask("Swift script name? (swift_run_eqr.swift) ",
  "swift_run_eqr.swift",  "swift_script_name")

params.swift_sh_name = ask("Swift launch script name? (swift_run_eqr.sh) ",
  "swift_run_eqr.sh", "swift_launch_script_name")

processTemplates("swift_run_eqr.swift", params)
processTemplates("swift_run_eqr.sh", params)

files = [['swift_run_eqr.swift', params.swift_script_name],
  ['swift_run_eqr.sh', params.swift_sh_name ]]

for (f in files) {
  def dest = new File(projectDir, FilenameUtils.concat("swift", f[1]))
  def source = new File(templateDir, f[0])
  FileUtils.moveFile(source, dest)
  if (f[0].endsWith(".sh")) {
    dest.setExecutable(true)
  }
  println "Created ./${FilenameUtils.normalize(dest.path)}"
}
