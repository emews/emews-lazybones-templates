import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

def files = ['eqpy.py', 'EQPy.swift']
for (f in files) {
  def dest = new File(projectDir.path + "/ext/EQ-Py", f)
  def source = new File(templateDir, f)
  FileUtils.copyFile(source, dest)
  println "Created ./${FilenameUtils.normalize(dest.path)}"
}
