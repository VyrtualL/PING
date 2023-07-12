const fs = require('fs')
const { dialog, BrowserWindow } = require('electron')

const createNewFile = async () => {
  const win = BrowserWindow.getFocusedWindow()
  // check if the app is running on windows or linux
  let arr = []
  if (process.platform === 'win32') {
    arr = ['openFile', 'openDirectory', 'promptToCreate']
  } else {
    arr = ['openFile', 'openDirectory', 'createDirectory']
  }
  const files = await dialog.showSaveDialog(win, {
    properties: arr,
    defaultPath: 'untitled.bf',
    filters: [
      { name: 'All Files', extensions: ['*'] }
    ]
  })
  if (files.canceled) {
    return ['', '']
  } else {
    return ['', files.filePath]
  }
}

module.exports = {
  createNewFile
}
