const { dialog, BrowserWindow } = require('electron')
const fs = require('fs')

const OpenFile = async () => {
  const win = BrowserWindow.getFocusedWindow()
  const files = await dialog.showOpenDialog(win, {
    properties: ['openFile'],
    filters: [
      { name: 'All Files', extensions: ['*'] }
    ]
  })
  try {
    const fc = fs.readFileSync(files['filePaths'][0]).toString()
    const fp = files['filePaths'][0]
    return [fc, fp]
  } catch (err) {
    return ['', '']
  }
}

module.exports = {
  OpenFile
}