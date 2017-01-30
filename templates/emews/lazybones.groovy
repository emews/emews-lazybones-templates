

def params = [:]
params.project_dir = projectDir.getName()

processTemplates("README.md", params)
