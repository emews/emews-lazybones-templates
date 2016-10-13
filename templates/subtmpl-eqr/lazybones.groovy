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
    "Readme.rst", "settings.mk.in", "settings.template.sh"]
println "Copying EQ-R source to ${params.working_directory}"

for (f in files) {
   def dest = new File(params.working_directory, f)
   def source = new File(templateDir.path + "/eqr", f)
   FileUtils.copyFile(source, dest)
   if (f.equals("bootstrap")) {
     dest.setExecutable(true)
   }
 }

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
    println "\nSuccessfully compiled EQ-R library and swift script into\n" +
      "${params.prefix}"
  } else {
    println "\nError while compiling EQ-R library: see output for details and\n" +
    "Readme.md for possible solutions."
  }
}
