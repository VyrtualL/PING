let term
let protocol
let socketURL
let socket
let pid

const addons = new Map()
// addons.set("attach", new AttachAddon.AttachAddon());
addons.set('search', new SearchAddon.SearchAddon())
addons.set('serialize', new SerializeAddon.SerializeAddon())
addons.set('fit', new FitAddon.FitAddon())
addons.set('unicode', new Unicode11Addon.Unicode11Addon())
addons.set('webgl', new WebglAddon.WebglAddon())
addons.set('web-links', new WebLinksAddon.WebLinksAddon())

const terminalContainer = document.getElementById('terminal-container')

const xtermjsTheme = {
  foreground : '#F8F8F8',
  background : '#7B3F00',
  selectionBackground : '#5DA5D533',
  black : '#1E1E1D',
  brightBlack : '#262625',
  red : '#CE5C5C',
  brightRed : '#FF7272',
  green : '#5BCC5B',
  brightGreen : '#72FF72',
  yellow : '#CCCC5B',
  brightYellow : '#FFFF72',
  blue : '#5D5DD3',
  brightBlue : '#7279FF',
  magenta : '#BC5ED1',
  brightMagenta : '#E572FF',
  cyan : '#5DA5D5',
  brightCyan : '#72F0FF',
  white : '#F8F8F8',
  brightWhite : '#FFFFFF'
}

function createTerminal() {
  // Clean terminal
  while (terminalContainer.children.length) {
    terminalContainer.removeChild(terminalContainer.children[0])
  }

  const isWindows =
      [ 'Windows', 'Win16', 'Win32', 'WinCE' ].indexOf(navigator.platform) >= 0
  term = new Terminal({
    allowProposedApi : true,
    windowsPty : isWindows ? {
      // In a real scenario, these values should be verified on the backend
      backend : 'conpty',
      buildNumber : 22621
    }
      : undefined,
    fontFamily : '"comic sans ms"',
    theme : xtermjsTheme
  })

  term.loadAddon(addons.get('fit'))
  term.loadAddon(addons.get('search'))
  term.loadAddon(addons.get('serialize'))
  term.loadAddon(addons.get('unicode'))
  term.loadAddon(addons.get('web-links'))

  window.term = term // Expose `term` to window for debugging purposes
  term.onResize((size) => {
    if (!pid) {
      return
    }
    const cols = size.cols
    const rows = size.rows
    const url = 'http://localhost:4000/terminals/' + pid +
                '/size?cols=' + cols + '&rows=' + rows

    fetch(url, { method : 'POST' })
  })
  protocol = (location.protocol === 'https:') ? 'wss://' : 'ws://'
  socketURL = protocol + location.hostname +
              ((location.port) ? (':' + 4000) : 'localhost:4000') +
              '/terminals/'

  // addons.get('fit').fit();

  // term.loadAddon(addons.get('webgl'));
  term.open(terminalContainer)
  if (!term.element) {
    // webgl loading failed for some reason, attach with DOM renderer
    term.open(terminalContainer)
  }

  term.focus()

  // fit is called within a setTimeout, cols and rows need this.
  setTimeout(async () => {
    // TODO: Clean this up, opt-cols/rows doesn't exist anymore
    // (document.getElementById(`opt-cols`)).value = term.cols;
    // (document.getElementById(`opt-rows`)).value = term.rows;
    // Set terminal size again to set the specific dimensions on the demo
    updateTerminalSize()
    const res =
        await fetch('http://localhost:4000/terminals?cols=' + term.cols +
                        '&rows=' + term.rows,
        { method : 'POST' })
    const processId = await res.text()
    pid = processId
    socketURL += processId
    socket = new WebSocket(socketURL)
    socket.onopen = runRealTerminal
    socket.onclose = runFakeTerminal
    socket.onerror = runFakeTerminal
  }, 0)
}

function updateTerminalSize() {
  const width = (term._core._renderService.dimensions.css.canvas.width +
                 term._core.viewport.scrollBarWidth)
    .toString() +
                'px'
  const height =
      (term._core._renderService.dimensions.css.canvas.height).toString() +
      'px'
  terminalContainer.style.width = width
  terminalContainer.style.height = height
  addons.get('fit').fit()
}

function runRealTerminal() {
  addons.set('attach', new AttachAddon.AttachAddon(socket))
  term.loadAddon(addons.get('attach'))
  term._initialized = true
}

function runFakeTerminal() {
  if (term._initialized) {
    return
  }

  term._initialized = true

  term.prompt = () => { term.write('\r\n$ ') }

  term.writeln('Welcome to xterm.js')
  term.writeln(
    'This is a local terminal emulation, without a real terminal in the back-end.')
  term.writeln('Type some keys and commands to play around.')
  term.writeln('')
  term.prompt()

  term.onKey((e) => {
    const ev = e.domEvent
    const printable = !ev.altKey && !ev.ctrlKey && !ev.metaKey

    if (ev.keyCode === 13) {
      term.prompt()
    } else if (ev.keyCode === 8) {
      // Do not delete the prompt
      if (term._core.buffer.x > 2) {
        term.write('\b \b')
      }
    } else if (printable) {
      term.write(e.key)
    }
  })
}

// createTerminal();

function clearInput(command) {
  const inputLengh = command.length
  for (let i = 0; i < inputLengh; i++) {
    term.write('\b \b')
  }
}
function prompt_(term) {
  command = ''
  term.write('\r\n$ ')
}
// socket.onmessage = (event) => {
//     term.write(event.data);
// }

function runCommand(term, command) {
  if (command.length > 0) {
    clearInput(command)
    socket.send(command + '\n')
    return
  }
}

function setStyle() {
  const weather = document.getElementById('weather')
  let theme = ''
  if (weather.textContent == 'Rainy') {
    theme = 'lightblue'
  } else if (weather.textContent == 'Sunny') {
    theme = 'chocolate'
  } else {
    theme = '#370617'
  }
  document.getElementsByClassName('xterm-viewport')[0].style.backgroundColor =
      theme
}
createTerminal()
setStyle()
//
//
//   init();