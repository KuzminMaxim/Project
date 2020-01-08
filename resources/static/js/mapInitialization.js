function updateCoordinates(lat, lng) {
    document.getElementById('lat').value = lat;
    document.getElementById('lng').value = lng;
}

function initMap() {
    var map, marker, infoWindow;
    var myLatlng = {
        lat: 55.753864,
        lng: 37.620739
    };
    document.getElementById('lat').value = myLatlng.lat;
    document.getElementById('lng').value = myLatlng.lng;

    var locations = [];
    for (var x = 0; x < document.getElementsByClassName("names").length; x++){
        var names = document.getElementsByClassName("names")[x].innerHTML;
        var lat = document.getElementsByClassName("latitudes")[x].innerHTML;
        var lng = document.getElementsByClassName("longitudes")[x].innerHTML;
        var descript = document.getElementsByClassName("descript")[x].innerHTML;
        var dates = document.getElementsByClassName("eventDates")[x].innerHTML;
        var eventNamesOfCreator = document.getElementsByClassName("eventNamesOfCreator")[x].innerHTML;
        var eventDatesOfCreation = document.getElementsByClassName("eventDatesOfCreation")[x].innerHTML;
        locations.push([names, lat, lng, descript, dates, eventNamesOfCreator, eventDatesOfCreation]);
    }


    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 12,
        disableDoubleClickZoom: true,
        center: myLatlng
    });



    infoWindow = new google.maps.InfoWindow;

    var i;

    for (i = 0; i < locations.length; i++) {
        var oldMarker = locations.map(function(location, i){
            return new google.maps.Marker({
                title : (locations[i][0]),
                position: new google.maps.LatLng(locations[i][1], locations[i][2]),
                map: map
            });
        });
        /*oldMarker = new google.maps.Marker({
            title : (locations[i][0]),
            position: new google.maps.LatLng(locations[i][1], locations[i][2]),
            map: map
        });*/

        google.maps.event.addListener(oldMarker, 'click', (function (oldMarker, i) {
            return function () {
                infoWindow.setContent("Description: " + locations[i][3] + '<br>' + "Date: " + locations[i][4]
                    + '<br>' + "Date of creation: " + locations[i][6]);
                infoWindow.open(map, oldMarker);
            }
        })(oldMarker, i));
        google.maps.event.addListener(oldMarker, 'dblclick', (function (oldMarker, i) {
            return function () {
                document.getElementById('nameOfOldEvent').value = locations[i][0];
                document.getElementById('nameOfOldChat').value = locations[i][0];
                document.getElementById('eventNamesOfCreator').value = locations[i][5];
                document.getElementById('descriptionOfThisEvent').value = locations[i][3];
                document.getElementById('dateOfCreation').value = locations[i][6];
                openOldForm();
            }
        })(oldMarker, i));
    }

    var markerCluster = new MarkerClusterer(map, oldMarker,
        {imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'});


    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var pos = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };

            infoWindow.setPosition(pos);
            infoWindow.setContent('Location found.');
            infoWindow.open(map);
            map.setCenter(pos);
        }, function() {
            handleLocationError(true, infoWindow, map.getCenter());
        });
    } else {
        handleLocationError(false, infoWindow, map.getCenter());
    }

    marker = new google.maps.Marker({
        title : "Your event!",
        position: null,
        map: map,
        draggable: true
    });

    marker.addListener('dragend', function(e) {
        var position = marker.getPosition();
        updateCoordinates(position.lat(), position.lng())
    });

    map.addListener('dblclick', function(e) {
        marker.setPosition(e.latLng);
        updateCoordinates(e.latLng.lat(), e.latLng.lng())
    });

    marker.addListener('click', function (e) {
        openForm();
    });

}
function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(browserHasGeolocation ?
        'Error: The Geolocation service failed.' :
        'Error: Your browser doesn\'t support geolocation.');
    infoWindow.open(map);
}