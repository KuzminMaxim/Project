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

    var input = document.getElementById('pac-input');
    var searchBox = new google.maps.places.SearchBox(input);
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

    map.addListener('bounds_changed', function() {
        searchBox.setBounds(map.getBounds());
    });

    var markersFromBox = [];

    searchBox.addListener('places_changed', function() {
        var places = searchBox.getPlaces();

        if (places.length === 0) {
            return;
        }

        markersFromBox.forEach(function(markerFromBox) {
            markerFromBox.setMap(null);
        });

        markersFromBox = [];

        marker.setMap(null);

        var bounds = new google.maps.LatLngBounds();
        places.forEach(function(place) {
            if (!place.geometry) {
                console.log("Returned place contains no geometry");
                return;
            }

            markersFromBox.push(new google.maps.Marker({
                map: map,
                title: place.name,
                position: place.geometry.location
            }));

            if (place.geometry.viewport) {
                bounds.union(place.geometry.viewport);
            } else {
                bounds.extend(place.geometry.location);
            }
        });
        map.fitBounds(bounds);

        for (let i = 0; i < markersFromBox.length; i++){
            markersFromBox[i].addListener('click', function () {
                var position = markersFromBox[i].position.toString();
                var lat = position.substr((position.indexOf('(') + 1), (position.indexOf(',') - 1));
                var lng = position.slice(position.indexOf(',') + 2, -1);
                updateCoordinates(lat, lng);
                openForm();
            });
        }

    });

    infoWindow = new google.maps.InfoWindow;

    var oldMarker, i;

    var markersForCluster = [];

    for (i = 0; i < locations.length; i++) {
         oldMarker = new google.maps.Marker({
            title : (locations[i][0]),
            position: new google.maps.LatLng(locations[i][1], locations[i][2]),
            map: map
        });

        google.maps.event.addListener(oldMarker, 'click', (function (oldMarker, i) {
            return function () {
                var geocoder = new google.maps.Geocoder;
                var latlng = {lat: parseFloat(locations[i][1]), lng: parseFloat(locations[i][2])};
                geocoder.geocode({'location': latlng}, function(results, status) {
                    if (status === 'OK') {
                        if (results[0]) {
                            var address = results[0].formatted_address;
                            infoWindow.setContent("Description: " + locations[i][3] + '<br>' + "Date: " + locations[i][4]
                                + '<br>' + "Date of creation: " + locations[i][6] + '<br>' + "Address: " + address);
                            infoWindow.open(map, oldMarker);
                        }
                    } else {
                        infoWindow.setContent("Description: " + locations[i][3] + '<br>' + "Date: " + locations[i][4]
                            + '<br>' + "Date of creation: " + locations[i][6]);
                        infoWindow.open(map, oldMarker);
                    }
                });
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
        markersForCluster.push(oldMarker);
    }

    var markerCluster = new MarkerClusterer(map, markersForCluster,
        {imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'});


    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var pos;
            if (locations.length === 1){
                pos = {
                    lat: parseFloat(locations[0][1]),
                    lng: parseFloat(locations[0][2])
                };
            } else {
                 pos = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude
                };
                infoWindow.setPosition(pos);
                infoWindow.setContent('Location found.');
                infoWindow.open(map);
            }
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
        updateCoordinates(position.lat(), position.lng());

        var geocoder = new google.maps.Geocoder;
        var latlng = {lat: parseFloat(position.lat()), lng: parseFloat(position.lng())};
        geocoder.geocode({'location': latlng}, function(results, status) {
            if (status === 'OK') {
                if (results[0]) {
                    var address = results[0].formatted_address;
                    marker.setTitle(address);
                }
            } else {
                marker.setTitle("Your event!");
            }
        });

    });

    map.addListener('dblclick', function(e) {
        marker.setMap(map);

        marker.setPosition(e.latLng);
        updateCoordinates(e.latLng.lat(), e.latLng.lng());

        var geocoder = new google.maps.Geocoder;
        var latlng = {lat: parseFloat(e.latLng.lat()), lng: parseFloat(e.latLng.lng())};
        geocoder.geocode({'location': latlng}, function(results, status) {
            if (status === 'OK') {
                if (results[0]) {
                    var address = results[0].formatted_address;
                    marker.setTitle(address);
                }
            } else {
                marker.setTitle("Your event!");
            }
        });

        if (markersFromBox.length !== 0){
            for (let i = 0; i < markersFromBox.length; i++){
                markersFromBox[i].setMap(null);
            }
        }

    });

    marker.addListener('click', function () {
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

