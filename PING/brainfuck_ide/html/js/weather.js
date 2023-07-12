function setTheme(weather) {
  const themeLink = document.getElementById('themeLink')
  if (weather != 'Sunny' && weather != 'Rainy') {
    themeLink.href = 'css/themeDark.css'
    document.getElementsByClassName('xterm-viewport')[0].style.backgroundColor =
        '#370617'
  } else {
    themeLink.href = `css/theme${weather}.css`
    if (weather == 'Sunny')
      document.getElementsByClassName('xterm-viewport')[0].style.backgroundColor =
          'chocolate'
    else
      document.getElementsByClassName('xterm-viewport')[0].style.backgroundColor =
          'lightblue'

  }
}

function meteo() {
  // get the city location

  const url = 'http://api.weatherapi.com/v1/current.json?key=470a3a3f0eff4b5c95a153342230706&q=Rennes&aqi=no'
  fetch(url).then(function(response) {
    response.json().then(function(data) {
      const condition = data.current.condition.text
      const conditionElement = document.getElementById('weather')
      conditionElement.textContent = condition
    })
  })
}

meteo()


window.addEventListener('DOMContentLoaded', function() {
  const weather = document.getElementById('weather')
  if (weather.textContent.includes('rain'))
    weather.textContent = 'Rainy'
  setTheme(weather.textContent)
  console.log(weather.textContent)
})