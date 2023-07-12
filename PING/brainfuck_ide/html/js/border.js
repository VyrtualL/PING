function setHorizontalBorderSize(elem) {
  const totalHeight = window.innerHeight
  const borderVal = elem.getBoundingClientRect()

  const bottomPercent = borderVal.bottom / totalHeight * 100
  const below = elem.getAttribute('data-below').split(', ')
  for (let i = 0; i < below.length; i++) {
    if (below[i] !== '')
      document.getElementById(below[i]).style.top = `${bottomPercent}%`
  }

  const topPercent = 100 - (borderVal.top / totalHeight * 100)
  const above = elem.getAttribute('data-above').split(', ')
  for (let i = 0; i < above.length; i++) {
    if (above[i] !== '')
      document.getElementById(above[i]).style.bottom = `${topPercent}%`
  }
}

function setVerticalBorderSize(elem) {
  const totalWidth = window.innerWidth
  const borderVal = elem.getBoundingClientRect()

  const rightPercent = borderVal.right / totalWidth * 100
  const right = elem.getAttribute('data-right').split(', ')
  for (let i = 0; i < right.length; i++) {
    if (right[i] !== '')
      document.getElementById(right[i]).style.left = `${rightPercent}%`
  }

  const topPercent = 100 - (borderVal.left / totalWidth * 100)
  const left = elem.getAttribute('data-left').split(', ')
  for (let i = 0; i < left.length; i++) {
    if (left[i] !== '')
      document.getElementById(left[i]).style.right = `${topPercent}%`
  }
}

function setSize() {
  let list = document.getElementsByClassName('hBorder')
  for (let i = 0; i < list.length; i++) {
    setHorizontalBorderSize(list[i])
  }

  list = document.getElementsByClassName('vBorder')
  for (let i = 0; i < list.length; i++) {
    setVerticalBorderSize(list[i])
  }

  const elem = document.getElementById('menuBorder')
  const elemBottom = document.getElementById(elem.getAttribute('data-below'))
  const elemHeight = `${elemBottom.getBoundingClientRect().height}px`
  elem.style.bottom = elemHeight
  list = elem.getAttribute('data-above').split(', ')
  elemBottom.style.height = elemHeight
  const bottom = window.innerHeight - elem.getBoundingClientRect().top
  // console.log(bottom);
  for (let i = 0; i < list.length; i++) {
    if (list[i] !== '') {
      document.getElementById(list[i]).style.bottom = `${bottom}px`
    }
  }
}
setSize()

function changePosHorizontal(elem, x) {
  let percentLeft = x / window.innerWidth * 100
  const size = elem.getBoundingClientRect().width

  const blockRight = elem.getAttribute('data-blockRight').split(', ')
  for (let i = 0; i < blockRight.length; i++) {
    if (blockRight[i] !== '') {
      const elem = document.getElementById(blockRight[i])
      const left = elem.getBoundingClientRect().left
      if (left - size < x) {
        // console.log("abort mission!");
        const percent = (left - size) / window.innerHeight * 100
        percentLeft = (percent < percentLeft) ? percent : percentLeft
      }
    }
  }

  const blockLeft = elem.getAttribute('data-blockLeft').split(', ')
  for (let i = 0; i < blockLeft.length; i++) {
    if (blockLeft[i] !== '') {
      const elem = document.getElementById(blockLeft[i])
      const right = elem.getBoundingClientRect().right
      if (right > x) {
        // console.log("abort mission!");
        const percent = right / window.innerHeight * 100
        percentLeft = (percent > percentLeft) ? percent : percentLeft
      }
    }
  }

  elem.style.left = `${percentLeft}%`
  setVerticalBorderSize(elem)
}

function changePosVertical(elem, y) {
  let percentTop = y / window.innerHeight * 100
  const size = elem.getBoundingClientRect().height

  const blockBelow = elem.getAttribute('data-blockDown').split(', ')
  for (let i = 0; i < blockBelow.length; i++) {
    if (blockBelow[i] !== '') {
      const elem = document.getElementById(blockBelow[i])
      const below = elem.getBoundingClientRect().top
      if (below - size < y) {
        // console.log("abort mission!");
        const percent = (below - size) / window.innerHeight * 100
        percentTop = (percent < percentTop) ? percent : percentTop
      }
    }
  }

  const blockAbove = elem.getAttribute('data-blockUp').split(', ')
  for (let i = 0; i < blockAbove.length; i++) {
    if (blockAbove[i] !== '') {
      const elem = document.getElementById(blockAbove[i])
      const above = elem.getBoundingClientRect().bottom
      if (above > y) {
        // console.log("abort mission!");
        const percent = above / window.innerHeight * 100
        percentTop = (percent > percentTop) ? percent : percentTop
      }
    }
  }

  elem.style.top = `${percentTop}%`
  setHorizontalBorderSize(elem)
  apply_size()
}

function setUserSelect() {
  const codingScreen = document.getElementsByClassName('codingScreen')[0]
  codingScreen.style.userSelect = 'auto'
  const titleBar = document.getElementById('title')
  titleBar.style.userSelect = 'auto'
}

function removeUserSelect() {
  const codingScreen = document.getElementsByClassName('codingScreen')[0]
  codingScreen.style.userSelect = 'none'
  const titleBar = document.getElementById('title')
  titleBar.style.userSelect = 'none'
}

const ver = document.getElementsByClassName('vBorder')
const hor = document.getElementsByClassName('hBorder')

for (let i = 0; i < ver.length; i++) {
  const elem = ver[i]
  function changeHor(event) {
    const localX = event.clientX
    changePosHorizontal(elem, localX)
  }
  elem.addEventListener('mousedown', () => {
    window.addEventListener('mousemove', changeHor)
    removeUserSelect()
  })
  window.addEventListener('mouseup', () => {
    window.removeEventListener('mousemove', changeHor)
    setUserSelect()
  })
}

for (let i = 0; i < hor.length; i++) {
  const elem = hor[i]
  function changeVer(event) {
    const localY = event.clientY
    changePosVertical(elem, localY)
  }

  elem.addEventListener('mousedown', () => {
    window.addEventListener('mousemove', changeVer)
    removeUserSelect()
  })
  window.addEventListener('mouseup', () => {
    window.removeEventListener('mousemove', changeVer)
    setUserSelect()
  })
}