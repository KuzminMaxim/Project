
function updateCoordinates(lat, lng) {
    document.getElementById('lat').value = lat;
    document.getElementById('lng').value = lng;
}

function initMap(listener) {
    var map;
    var contentString = '<div id="content">\n' +
        '        <div id="siteNotice">\n' +
        '        </div>\n' +
        '        <h1 id="firstHeading" class="firstHeading">yyy</h1>\n' +
        '        <div id="bodyContent">\n' +
        '         <p>Какое то описание.</p>\n' +
        '        </div>\n' +
        '        </div>;';
    var infowindow = new google.maps.InfoWindow({
        content: contentString
    });

    var latitude = 56.291828;
    var longitude = 43.981804;

    var myLatLng = {lat: latitude, lng: longitude};

    document.getElementById('lat').value = myLatLng.lat;
    document.getElementById('lng').value = myLatLng.lng;


    map = new google.maps.Map(document.getElementById('map'), {
        center: myLatLng,
        zoom: 14,
        disableDoubleClickZoom: true, // disable map zoom on double click
    });

    <!--
    var marker = new google.maps.Marker({
        position: myLatLng,
        map: map,
        title: myLatLng.lat + ', ' + myLatLng.lng,
        animation: google.maps.Animation.DROP,
    });
        -->
    var newMarker = google.maps.event.addListener(map, 'dblclick', function (event) {
        new google.maps.Marker({
            position: event.latLng,
            map: map,
            animation: google.maps.Animation.DROP,
            title: event.latLng.lat() + ', ' + event.latLng.lng()
        });
    });
    <!--
    google.maps.event.addListener(marker, 'click', function() {
        infowindow.open(map, marker);
    });
-->


    map.addListener('dblclick', function(e) {
        marker.setPosition(e.latLng);
        updateCoordinates(e.latLng.lat(), e.latLng.lng())
    });




}