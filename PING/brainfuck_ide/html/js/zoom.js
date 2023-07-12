let focusElem = null
let terminalElem = null
let poemElem = null
let debugElem = null
let codeElem = null
let menuElem = null

function changeFocus(elem) {
  if (focusElem !== null && elem.id === focusElem.id)
    return
  if (focusElem !== null)
    console.log(`${focusElem.id} is now out of focus`)
  focusElem = elem
  console.log(`${focusElem.id} is now in focus`)
}

function setFocus() {
  terminalElem = document.getElementById('terminal')
  poemElem = document.getElementById('poem')
  debugElem = document.getElementById('memory')
  codeElem = document.getElementById('code')
  menuElem = document.getElementById('title')

  terminalElem.addEventListener('click', () => {
    changeFocus(terminalElem)
  })

  poemElem.addEventListener('click', () => {
    changeFocus(poemElem)
  })

  debugElem.addEventListener('click', () => {
    changeFocus(debugElem)
  })

  codeElem.addEventListener('click', () => {
    changeFocus(codeElem)
  })

  menuElem.addEventListener('click', () => {
    changeFocus(menuElem)
  })
}

let ctrlPress = false
let metaPress = false
const fontSizeStr = window.getComputedStyle(document.body).fontSize
const fontSize = Number(fontSizeStr.substring(0, fontSizeStr.length - 2))

function changeZoom(elem, zoomIn) {
  const elemFontSizeStr = elem.style.fontSize
  const elemFontSize = (elemFontSizeStr === '') ? fontSize : Number(elemFontSizeStr.substring(0, elemFontSizeStr.length - 2))
  if (zoomIn)
    elem.style.fontSize = `${elemFontSize + 1}px`
  else if (elemFontSize > 1)
    elem.style.fontSize = `${elemFontSize - 1}px`
  console.log(`${elem.id} has a font size of ${elem.style.fontSize}`)
}

function changeZoomTerminal(zoomIn) {
  const size = term._publicOptions.fontSize
  if (zoomIn)
    term._publicOptions.fontSize = size + 1
  else
    term._publicOptions.fontSize = size - 1
  apply_size()
}

function changeZoomEditor(zoomIn) {
  const elem = document.getElementById('editor')
  changeZoom(elem, zoomIn)
}

$(document).keydown(function(event) {
  if (event.originalEvent.key === 'Meta')
    metaPress = true
  else if (event.originalEvent.key === 'Control')
    ctrlPress = true

  if (ctrlPress || metaPress) {
    const zoomIn = event.which === 187 || event.which === 107 || event.which === 61
    const zoomOut = event.which === 189 || event.which === 109 || event.which === 173
    if (zoomIn || zoomOut) {
      event.preventDefault()
      if (focusElem === null)
        return
      switch (focusElem.id) {
      case 'poem':
        changeZoom(poemElem, zoomIn)
        break
      case 'memory':
        changeZoom(debugElem, zoomIn)
        break
      case 'code':
        changeZoomEditor(zoomIn)
        break
      case 'terminal':
        changeZoomTerminal(zoomIn)
        break
      default:
        break
      }
    }
  }
  console.log('event is ' + event.which)
})

document.addEventListener('keyup', function(event) {
  if (event.key === 'Meta')
    metaPress = false
  else if (event.key === 'Control')
    ctrlPress = false
})