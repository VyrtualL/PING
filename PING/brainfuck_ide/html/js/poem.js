function refreshPoem(date) {
  meteo()
  console.log(ouzbek_button)
  if (ouzbek_button === true) {
    localStorage.setItem('language', 'true')
    return new Promise(() => {
      const ouz = random_ouzbek()
      document.getElementById('poemTitle').innerHTML = ''
      document.getElementById('poemContent').innerHTML = ouz.at(0)
      document.getElementById('poemAuthor').innerHTML = ouz.at(1)
      localStorage.setItem('refreshDate', date.getTime())
      localStorage.setItem('poemTitle', '')
      localStorage.setItem('poemContent', ouz.at(0))
      localStorage.setItem('poemAuthor', ouz.at(1))
    })
  } else {
    localStorage.setItem('language', 'false')
    return fetch('https://www.poeticamundi.com/?redirect_to=random&category_name=poemes')
      .then((response) => response.text())
      .then((response) => {
        let title = ''
        title = response.split('entry-title">')[1]
        title = title.split('</h1>')[0]
        title = title.split(' - ')[0]
        let author = ''
        author = response.split('entry-title">')[1]
        author = author.split('</h1>')[0]
        author = author.split(' - ')[1]
        let content = ''
        content = response.split('entry-content">')[1]
        content = content.split('<div')[0]

        let index = 0
        while (index !== content.length && content.includes(author, index + 1)) {
          index = content.indexOf(author, index)
        }
        content = content.substring(0, index)

        // display the poem
        document.getElementById('poemTitle').innerHTML = title
        document.getElementById('poemContent').innerHTML = content
        document.getElementById('poemAuthor').innerHTML = author
        localStorage.setItem('refreshDate', date.getTime())
        localStorage.setItem('poemTitle', title)
        localStorage.setItem('poemContent', content)
        localStorage.setItem('poemAuthor', author)

        // save the poem
        localStorage.setItem('refreshDate', date.getTime())
        localStorage.setItem('poemTitle', title)
        localStorage.setItem('poemContent', content)
        localStorage.setItem('poemAuthor', author)
      }).catch(() => {
        document.getElementById('poemTitle').innerHTML = 'Désolé, pas de poème pour l\'instant!'
        document.getElementById('poemContent').innerHTML = ''
        document.getElementById('poemAuthor').innerHTML = ''

        localStorage.removeItem('refreshDate')
        localStorage.setItem('poemTitle', 'Désolé, pas de poème pour l\'instant!')
        localStorage.setItem('poemContent', '')
        localStorage.setItem('poemAuthor', '')
      })
  }
}

// Checks the date of the last loaded poem
const dateStr = localStorage.getItem('refreshDate')
const pastDate = (dateStr == null) ? null : new Date(parseInt(dateStr))
const date = new Date()
const timeDiff = 5 * 60 * 1000
if (pastDate === null || date.getTime() >= pastDate.getTime() + timeDiff) {
  // searches a random poem of 14 lines
  refreshPoem(date).then()
} else {
  // load and display the poem
  document.getElementById('poemTitle').innerHTML = localStorage.getItem('poemTitle')
  document.getElementById('poemContent').innerHTML = localStorage.getItem('poemContent')
  document.getElementById('poemAuthor').innerHTML = localStorage.getItem('poemAuthor')
}

async function sleep(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms))
}

function timedReset() {
  const refreshDate = localStorage.getItem('refreshDate')
  let waitTime = (refreshDate == null) ? timeDiff : timeDiff - (Date.now() - parseInt(refreshDate))
  waitTime = (waitTime < 0) ? 0 : waitTime
  sleep(waitTime).then(() => {
    const refreshDate = localStorage.getItem('refreshDate')
    // console.log(`${Date.now()}, ${parseInt(refreshDate) +timeDiff}`)
    if (Date.now() >= parseInt(refreshDate) + timeDiff) {
      refreshPoem(new Date()).then(timedReset)
    } else
      timedReset()
  })
}

timedReset()