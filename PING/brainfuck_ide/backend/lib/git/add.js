const fs = require('fs')
const GIT_DIRECTORY = '.git'
const INDEX_FILE = `${GIT_DIRECTORY}/index`

function Add(files) {
  if (!fs.existsSync(GIT_DIRECTORY)) {
    console.log('Not a git repository')
    return false
  }

  if (files.length == 0) {
    console.log('No files to add')
    return false
  }

  for (let i = 0; i < files.length; i++) {
    if (!fs.existsSync(files[i])) {
      console.log('File not found')
      return false
    } else {
      const content = fs.readFileSync(files[i], 'utf-8')
      fs.appendFileSync(INDEX_FILE, content)
    }
  }
  return true

}

module.exports = {
  Add
}