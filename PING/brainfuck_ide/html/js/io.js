function compile(code) {
  fetch('http://localhost:3000/compile').then(function(response) {
    const socket = new WebSocket('ws://localhost:6061')
    socket.onopen = function(e) {
      socket.send(code)
    }
    socket.onmessage = function(event) {
      let out = event.data
      if (out.includes('ERROR')) {
        openPopupFail()
        out = out.replace(/\n/g, '<br>')
        document.getElementById('debug').innerHTML = out
      } else {
        // replace \n by <br>
        out = out.replace(/\n/g, '<br>')
        document.getElementById('debug').innerHTML = out
        openPopupSucess()
      }
    }
  })

}


function openFile(editor) {
  fetch('http://localhost:3000/open').then(function(response) {
    response.json().then(function(data) {
      editor.setValue(data.content)
      document.getElementById('currentFile').innerHTML = data.path
    })
  })
}

function saveFile(content) {
  fetch('http://localhost:3000/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ content: content, path: document.getElementById('currentFile').innerHTML })
  })
}

function newFile(editor) {
  fetch('http://localhost:3000/create').then(function(response) {
    response.json().then(function(data) {
      editor.setValue(data.content)
      document.getElementById('currentFile').innerHTML = data.path
    })
  })
}