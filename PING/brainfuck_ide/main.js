const { app, BrowserWindow, Menu, dialog } = require('electron')
const { init_term } = require('./backend/lib/backend_term')
const { start } = require('./backend/rest-api')


const createWindow = () => {
  const win = new BrowserWindow({
    width: 1440,
    height: 1024,
    minHeight: 640,
    minWidth: 640,
    frame: false,
    webPreferences: {
      nodeIntegration: false,
      contextIsolation: false
    }
  })

  win.loadFile('html/main.html')
}

app.whenReady().then(() => {
  init_term()
  createWindow()
  start()
})

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit()
})
