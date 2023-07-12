const express = require('express')
const WebSocket = require('ws')
const { interpret } = require('./lib/compile')
const { deleteFile } = require('./lib/delete')
const { saveFile } = require('./lib/save')
const { init_term } = require('./lib/backend_term')
const { maximizeApp, minimizeApp, quitApp, resizeApp } = require('./lib/window_management/titleBar')
const { hexdump } = require('./lib/hexdump')
const { OpenFile } = require('./lib/open')
const { createNewFile } = require('./lib/new-file')
const { copy, paste } = require('./lib/copy')


function start() {
  const app = express()
  app.use(express.json())
  const port = 3000
  const host = process.platform === 'win32' ? '127.0.0.1' : '0.0.0.0'
  const wss = new WebSocket.Server({ port: 6061 })
  wss.on('connection', ws => {
    ws.on('message', command => {
      // console.log(command.toString());
      const out = interpret(command.toString(), '')
      if (out.includes('ERROR')) {
        ws.send(out)
      } else {

        const str = hexdump(out, 15)
        ws.send(str)
      }
    })
  })
  app.get('/compile', (req, res) => {
    res.send('OK')
  })

  app.get('/create', (req, res) => {
    createNewFile().then(function(data) {
      res.send({ content: data[0], path: data[1] })
    })
  })
  // Delete file
  app.get('/delete', (req, res) => {
    try {
      const path = req.body.path
      deleteFile(path)
      res.send('File delented')
    } catch (err) {
      res.send('File or Directory not Found :', err.message)
    }
  })

  app.post('/save', (req, res) => {

    if (req.body == undefined || req.body.path == '' || req.body.content == undefined) {
      res.statusCode = 400
      res.send('No path or content')
    } else{
      saveFile(req.body.path, req.body.content)
      res.send('File saved')
    }
  })

  app.get('/open_term', (req, res) => {
    init_term()
    res.send('OK')
  })

  app.post('/add', (req, res) => {
    if (req.body.files != undefined) {
      Add(req.body.files)
      res.send('File(s) added')
    } else {
      res.send('No files to add')
    }
  })

  app.get('/minimize', (req, res) => {
    minimizeApp()
    res.send('OK')
  })

  app.get('/maximize', (req, res) => {
    maximizeApp()
    res.send('OK')
  })

  app.get('/quit', (req, res) => {
    quitApp()
    res.send('OK')
  })

  app.get('/resize', (req, res) => {
    resizeApp()
    res.send('OK')
  })

  app.get('/open', (req, res) => {
    OpenFile().then(function(data) {
      res.send({ content: data[0], path: data[1] })
    })
  })

  app.post('/cut', (req, res) => {
    if (req.body.text != undefined) {
      copy(req.body.text)
      res.send('OK')
    } else {
      res.send('No text to cut')
    }
  })

  app.post('/copy', (req, res) => {
    if (req.body.text != undefined) {
      copy(req.body.text)
      res.send('OK')
    } else {
      res.send('No text to copy')
    }
  })

  app.get('/paste', (req, res) => {
    const out = paste()
    res.send({ text: out })
  })


  app.listen(port, host)
  return app
}


module.exports = {
  start
}