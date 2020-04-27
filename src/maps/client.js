// document on ready -- this can probably be all updated to JS6...
document.addEventListener('DOMContentLoaded', function() {
  if (document.querySelectorAll('#map').length > 0)
  {
    // appends the google maps api to html header
    var js_file = document.createElement('script');
    js_file.type = 'text/javascript';
    js_file.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyD3Jb1jp2-D5Xi0kfEL5jEATK4c34nUDCY&callback=initMap';
    document.getElementsByTagName('head')[0].appendChild(js_file);
  }
});

// global on the dom so that we can access it later through java easily
var map; // holds out google map (on the div with id #map)
var markers = []; // global markers

// global so that the callback in the google maps api async call can access this function
function initMap() {
    // constructs a GoogleMap map on the map object at the div with id=map at MSOE's (lat,lng)
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 43.0469577, lng: -87.90786},
        zoom: 8
    });
}

// adds a marker to the map and global markers
function addMarker(lat, lng) {
    let pos = new google.maps.LatLng(lat, lng);
    let marker = new google.maps.Marker({
        position: pos,
        map: map,
        title: ""
    });
    markers.push(marker);
}