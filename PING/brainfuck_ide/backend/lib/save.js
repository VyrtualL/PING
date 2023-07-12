const fs = require('fs')

function saveFile(path, newContent) {
  fs.writeFileSync(path, newContent, (err) => {
    if (err) return false
  })
  return true
}

module.exports = {
  saveFile
}