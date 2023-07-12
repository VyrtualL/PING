function brailleEvent() {

  brailleButton.addEventListener('click', function() {
    const brailleButton = document.getElementById('brailleButton')
    const poemElement = document.getElementById('poem')
    const brailleText = document.querySelector('#brailleButton + label')
    const menuText = document.querySelector('#menuButton + label')
    const refreshButton = document.getElementById('refreshPoem')
    const debug = document.getElementById('memory')
    const ideTitle = document.getElementById('ide')
    const codeElement = document.getElementById('editor')
    const fileButton = document.getElementById('fileButton')
    const editButton = document.getElementById('editButton')
    const helpButton = document.getElementById('helpButton')
    const column = document.getElementsByClassName('column')
    const newFile = document.getElementById('new_file')
    const openFile = document.getElementById('open_file')
    const saveFile = document.getElementById('save_file')
    const cut = document.getElementById('cut')
    const copy = document.getElementById('copy')
    const paste = document.getElementById('paste')
    const changeLanguage = document.getElementById('change_language')
    const compile = document.getElementById('compile')

    const elems = [brailleText, poemElement, refreshButton, debug, ideTitle, codeElement, fileButton, editButton, helpButton, newFile, openFile, saveFile, cut, copy, paste, changeLanguage, compile]
    if (brailleButton.checked) {
      for (let i = 0; i < elems.length; i++)
        elems[i].style.fontFamily = 'FrBraille, sans-serif'
      term.options = { fontFamily: 'FrBraille, sans-serif' }
    } else {
      for (let i = 0; i < elems.length; i++)
        elems[i].style.fontFamily = 'inherit'
      term.options = { fontFamily: 'comic sans ms' }
    }
    apply_size()
    changePadding()
  })

  function changePadding() {
    const brailleButton = document.getElementById('brailleButton')
    const poemElement = document.getElementById('poem')
    const brailleText = document.querySelector('#brailleButton + label')
    const menuText = document.querySelector('#menuButton + label')
    const refreshButton = document.getElementById('refreshPoem')
    const debug = document.getElementById('memory')
    const ideTitle = document.getElementById('ide')
    const codeElement = document.getElementById('editor')
    const fileButton = document.getElementById('fileButton')
    const editButton = document.getElementById('editButton')
    const helpButton = document.getElementById('helpButton')
    const column = document.getElementsByClassName('column')
    const newFile = document.getElementById('new_file')
    const openFile = document.getElementById('open_file')
    const saveFile = document.getElementById('save_file')
    const cut = document.getElementById('cut')
    const copy = document.getElementById('copy')
    const paste = document.getElementById('paste')
    const changeLanguage = document.getElementById('change_language')
    const compile = document.getElementById('compile')

    const elems = [document.getElementById('poemTitle'), refreshButton, debug, fileButton, editButton, helpButton, newFile, openFile, saveFile, cut, copy, paste, changeLanguage]
    for (let i = 0; i < elems.length; i++) {
      if (brailleButton.checked)
        elems[i].style.paddingTop = '5px'
      else
        elems[i].style.paddingTop = '0'
    }
  }
}