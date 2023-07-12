const url = 'http://api.weatherapi.com/v1/current.json?key=470a3a3f0eff4b5c95a153342230706&q=Rennes&aqi=no'
const XMLHttpRequest = require('xhr2')
// Create a new XMLHttpRequest object
const request = new XMLHttpRequest()

// Set up a GET request to the API endpoint
request.open('GET', url, true)

// Define a callback function to handle the response
request.onload = function() {
  if (request.status >= 200 && request.status < 400) {
    // Success! Parse the response
    const response = JSON.parse(request.responseText)

    // Handle the data as needed
    console.log('location: ' + response.location.name)
    console.log('Température: ' + response.current.temp_c + ' °C')
    console.log('Currently, it\'s ' + response.current.condition.text)
  } else {
    // Error occurred
    console.error('An error occurred while making the request:', request.status, request.statusText)
  }
}

// Send the request
request.send()