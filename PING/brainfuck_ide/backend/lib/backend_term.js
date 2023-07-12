const express = require('express')
const expressWs = require('express-ws')
const os = require('os')
const pty = require('node-pty')

// Whether to use binary transport.
const USE_BINARY = os.platform() !== 'win32'

function init_term() {
  const app = express()
  expressWs(app)

  const terminals = {}, unsentOutput = {}, temporaryDisposable = {}

  app.post('/terminals', (req, res) => {
    const env = Object.assign({}, process.env)
    env['COLORTERM'] = 'truecolor'
    const confWin = {
      name : 'xterm-256color',
      cols : cols || 80,
      rows : rows || 24,
      cwd : process.platform === 'win32' ? undefined : env.PWD,
      env : env
    }

    const confLinux = {
      name : 'xterm-256color',
      cols : cols || 80,
      rows : rows || 24,
      cwd : process.platform === 'win32' ? undefined : env.PWD,
      env : env,
      encoding : null
    }
    const shell = process.platform === 'win32' ? 'powershell.exe'
      : process.env.SHELL || 'bash'
    var cols = parseInt(req.query.cols), rows = parseInt(req.query.rows),
      term = pty.spawn(shell, [],
        process.platform === 'win32' ? confWin : confLinux)

    terminals[term.pid] = term
    unsentOutput[term.pid] = ''
    temporaryDisposable[term.pid] =
        term.onData(function(data) { unsentOutput[term.pid] += data })
    res.send(term.pid.toString())
    res.end()
  })

  app.post('/terminals/:pid/size', (req, res) => {
    const pid = parseInt(req.params.pid), cols = parseInt(req.query.cols),
      rows = parseInt(req.query.rows), term = terminals[pid]

    term.resize(cols, rows)

    res.end()
  })

  app.ws('/terminals/:pid', function(ws, req) {
    const term = terminals[parseInt(req.params.pid)]

    temporaryDisposable[term.pid].dispose()
    delete temporaryDisposable[term.pid]
    ws.send(unsentOutput[term.pid])
    delete unsentOutput[term.pid]

    // unbuffered delivery after user input
    let userInput = false

    // string message buffering
    function buffer(socket, timeout, maxSize) {
      let s = ''
      let sender = null
      return (data) => {
        s += data
        if (s.length > maxSize || userInput) {
          userInput = false
          socket.send(s)
          s = ''
          if (sender) {
            clearTimeout(sender)
            sender = null
          }
        } else if (!sender) {
          sender = setTimeout(() => {
            socket.send(s)
            s = ''
            sender = null
          }, timeout)
        }
      }
    }
    // binary message buffering
    function bufferUtf8(socket, timeout, maxSize) {
      const chunks = []
      let length = 0
      let sender = null
      return (data) => {
        chunks.push(data)
        length += data.length
        if (length > maxSize || userInput) {
          userInput = false
          socket.send(Buffer.concat(chunks))
          chunks.length = 0
          length = 0
          if (sender) {
            clearTimeout(sender)
            sender = null
          }
        } else if (!sender) {
          sender = setTimeout(() => {
            socket.send(Buffer.concat(chunks))
            chunks.length = 0
            length = 0
            sender = null
          }, timeout)
        }
      }
    }
    const send = (USE_BINARY ? bufferUtf8 : buffer)(ws, 3, 262144)

    // WARNING: This is a naive implementation that will not throttle the flow
    // of data. This means it could flood the communication channel and make the
    // terminal unresponsive. Learn more about the problem and how to implement
    // flow control at https://xtermjs.org/docs/guides/flowcontrol/
    term.onData(function(data) {
      try {
        send(data)
      } catch (ex) {
        // The WebSocket is not open, ignore
      }
    })
    ws.on('message', function(msg) {
      term.write(msg)
      userInput = true
    })
    ws.on('close', function() {
      term.kill()
      // Clean things up
      delete terminals[term.pid]
    })
  })

  const port = process.env.PORT || 4000

  app.listen(port)
}

module.exports = { init_term }