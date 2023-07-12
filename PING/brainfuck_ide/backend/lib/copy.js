const { clipboard } = require('electron')

function copy(text) {
  clipboard.writeText(text, 'clipboard')
}

function paste() {
  return clipboard.readText('clipboard')
}

module.exports = {
  copy,
  paste
}