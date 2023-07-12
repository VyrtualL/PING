function registerMenu() {

  const nf = document.getElementById('new_file')
  nf.addEventListener('click', function() {
    newFile(editor)
  })

  document.getElementById('open_file').addEventListener('click', function() {
    openFile(editor)
  })

  document.getElementById('save_file').addEventListener('click', function() {
    saveFile(editor.getValue())
  })

  document.getElementById('compile').addEventListener('click', function() {
    compile(editor.getValue())
  })

  document.getElementById('cut').addEventListener('click', function() {
    const selected = editor.getSelectedText().toString()
    console.log(selected)
    if (selected.length > 0) {
      fetch('http://localhost:3000/cut', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          text: selected
        })
      }).then(() => {
        const new_text = editor.getValue().replace(selected, '')
        editor.setValue(new_text)
      })
    }
  })

  document.getElementById('copy').addEventListener('click', function() {
    const selected = editor.getSelectedText().toString()
    if (selected.length > 0) {
      fetch('http://localhost:3000/copy', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          text: selected
        })
      })
    }
  })

  document.getElementById('paste').addEventListener('click', function() {
    fetch('http://localhost:3000/paste')
      .then(res => res.json())
      .then(data => {
        const to_replace = editor.getSelectedText().toString()
        const cursor_pos = editor.getCursorPosition()
        let new_text = editor.getValue()
        new_text = new_text.split('\n')
        new_text[cursor_pos.row] = new_text[cursor_pos.row].replace(to_replace, data.text)
        new_text = new_text.join('\n')
        editor.setValue(new_text)
      })
  })
  document.getElementById('change_language').addEventListener('click', () => {
    ouzbek_button = !ouzbek_button
    if (ouzbek_button) {
      translateToOuzbek()
    } else {
      translateToFrench()
    }
    refreshPoem(new Date()).then()
  })
  document.getElementById('refreshPoem').addEventListener('click', () => {
    refreshPoem(new Date()).then()
  })

}

registerMenu()

function translateToFrench() {
  document.getElementById('fileButton').innerHTML = `Fichier &ensp;
    <div class="sous">
        <div class="column" id="new_file">
            <div class="left">Nouveau fichier</div>
            <div class="right">ctrl/⌘ + n</div>
        </div>
        <div class="column" id="open_file">
            <div class="left">Ouvrir</div>
            <div class="right">ctrl/⌘ + o</div>
        </div>
        <div class="column" id="save_file">
            <div class="left">Enregistrer</div>
            <div class="right">ctrl/⌘ + s</div>
        </div>
        <div class="column" id="compile">
            <div class="left">Compiler</div>
            <div class="right">ctrl/⌘ + b</div>
        </div>
    </div>`
  document.getElementById('editButton').innerHTML = `Edition &ensp;
    <div class="sous">
        <div class="column" id="cut">
            <div class="left">Couper</div>
            <div class="right">ctrl/⌘ + x</div>
        </div>
        <div class="column" id="copy">
            <div class="left">Copier</div>
            <div class="right">ctrl/⌘ + c</div>
        </div>
        <div class="column" id="paste">
            <div class="left">Coller</div>
            <div class="right">ctrl/⌘ + v</div>
        </div>
    </div>`
  document.getElementById('helpButton').innerHTML = `Aide &ensp;
    <div class="sous">
        <div class="column" id="change_language">Changer la langue</div>
        <div class="column" id="refreshPoem">Changer le poème</div>
    </div>`
  document.getElementById('LabelbrailleButton').innerHTML = 'Braille'
  brailleEvent()
  // checks if the box is checked
  if (document.getElementById('brailleButton').checked) {
    // sends a click event to the braille button
    document.getElementById('brailleButton').click()
    document.getElementById('brailleButton').click()
  }
  registerMenu()
}

function translateToOuzbek() {
  document.getElementById('fileButton').innerHTML = `Fayl &ensp;
    <div class="sous">
        <div class="column" id="new_file">
            <div class="left">Yangi fayl</div>
            <div class="right">ctrl/⌘ + n</div>
        </div>
        <div class="column" id="open_file">
            <div class="left">Ochiq</div>
            <div class="right">ctrl/⌘ + o</div>
        </div>
        <div class="column" id="save_file">
            <div class="left">Roʻyxatdan oʻtish</div>
            <div class="right">ctrl/⌘ + s</div>
        </div>
        <div class="column" id="compile">
            <div class="left">Kompilyatsiya qilish</div>
            <div class="right">ctrl/⌘ + b</div>
        </div>
    </div>`
  document.getElementById('editButton').innerHTML = `Tahrirlash &ensp;
    <div class="sous">
        <div class="column" id="cut">
            <div class="left">Kesish</div>
            <div class="right">ctrl/⌘ + x</div>
        </div>
        <div class="column" id="copy">
            <div class="left">Nusxa ko'chirish</div>
            <div class="right">ctrl/⌘ + c</div>
        </div>
        <div class="column" id="paste">
            <div class="left">Yopishib qolmoq</div>
            <div class="right">ctrl/⌘ + v</div>
        </div>
    </div>`
  document.getElementById('helpButton').innerHTML = `Yordam &ensp;
    <div class="sous">
        <div class="column" id="change_language">Sherni o'zgartirish</div>
        <div class="column" id="refreshPoem">Nilni o'zgartiring</div>
    </div>`
  document.getElementById('LabelbrailleButton').innerHTML = 'Brayl'
  brailleEvent()
  // checks if the box is checked
  if (document.getElementById('brailleButton').checked) {
    // sends a click event to the braille button
    document.getElementById('brailleButton').click()
    document.getElementById('brailleButton').click()
  }
  registerMenu()
}

if (ouzbek_button) {
  translateToOuzbek()
}