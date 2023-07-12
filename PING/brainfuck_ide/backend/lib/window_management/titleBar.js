const browserWindow = require('electron').BrowserWindow


function minimizeApp() {
  browserWindow.getFocusedWindow().minimize()
}

function maximizeApp() {
  browserWindow.getFocusedWindow().maximize()
}

function quitApp() {
  browserWindow.getFocusedWindow().close()
}

function resizeApp() {
  // unmaximize the window
  browserWindow.getFocusedWindow().unmaximize()
}
module.exports = {
  minimizeApp,
  maximizeApp,
  quitApp,
  resizeApp
}