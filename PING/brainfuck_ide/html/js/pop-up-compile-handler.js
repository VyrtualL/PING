let newWin
function openPopupSucess() {
  newWin = window.open('./pop-up-success.html', 'About us', 'width=800, height=500')
  document.onmousedown = focusPopup
  document.onkeyup = focusPopup
  document.onmousemove = focusPopup
}

function focusPopup() {
  if (!newWin.closed) {
    newWin.focus()
  }
}

function openPopupFail() {
  newWin = window.open('./pop-up-fail.html', 'About us', 'width=800, height=500')
  document.onmousedown = focusPopup
  document.onkeyup = focusPopup
  document.onmousemove = focusPopup
}