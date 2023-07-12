// load the terminal window
let ouzbek_button = localStorage.getItem('language') === 'true'


document.getElementById('quit').addEventListener('click', function() {
  fetch('http://localhost:3000/quit')
})

let isFullscreen = false
document.getElementById('maximize').addEventListener('click', function() {
  if (isFullscreen)
    fetch('http://localhost:3000/resize')
  else
    fetch('http://localhost:3000/maximize')
  isFullscreen = !isFullscreen
})

document.getElementById('minimize').addEventListener('click', function() {
  fetch('http://localhost:3000/minimize')
})

function apply_size() {
  // let row = document.getElementsByClassName("xterm-rows")[0];
  // let rowHeight = row.offsetHeight / term.rows;
  // // get the height of the terminal
  // let t = document.getElementsByClassName("xterm-viewport")[0];
  // let termHeight = t.offsetHeight;
  // // get the maximum number of rows
  // //console.log(rowHeight, termHeight, termHeight / rowHeight);
  // let maxRows = Math.floor(termHeight / rowHeight);
  // let columWidth = row.getBoundingClientRect().width / term.cols;
  // let maxCols = Math.floor(t.getBoundingClientRect().width / columWidth);
  // // set the number of rows
  // term.resize(maxCols, maxRows);
  const tspace = document.getElementsByClassName('xterm-viewport')[0]

  terminalContainer.style.width = tspace.offsetWidth - term._core.viewport.scrollBarWidth + 'px'
  terminalContainer.style.height = tspace.offsetHeight - 3 + 'px'
  addons.get('fit').fit()
  // updateTerminalSize()
}
$('#menu-container').load('menu.html', function() {
  $('#code').load('code.html', function() {
    $('#poem').load('poem.html', function() {
      $('#terminal').load('terminal.html', function() {
        $('#memory').load('memory.html', function() {
          $('#borders').load('borders.html', apply_size)
          brailleEvent()
          setFocus()
        })
      })
    })
  })
})
window.addEventListener('resize', apply_size)