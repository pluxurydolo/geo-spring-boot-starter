class RouteMap {
    map = null;
    routeLayer = null;
    markersLayer = null;
    currentId = '';
    currentProfile = '';
    currentRoute = null;
    currentUser = '';

    init(geoRouteData, id, user, profileName) {
        this.currentRoute = geoRouteData;
        this.currentId = id;
        this.currentUser = user;
        this.currentProfile = profileName;

        const centerLatitude = 55.7558;
        const centerLongitude = 37.6173;
        const zoom = 12;

        this.initMap(centerLatitude, centerLongitude, zoom);
        this.loadRouteFromGraphHopper(id, profileName);
    }

    initMap(centerLatitude, centerLongitude, zoom) {
        this.map = new ol.Map({
            target: 'map',
            layers: [
                new ol.layer.Tile({
                    source: new ol.source.OSM({
                        attributions: '© OpenStreetMap contributors'
                    })
                })
            ],
            view: new ol.View({
                center: ol.proj.fromLonLat([centerLongitude, centerLatitude]),
                zoom: zoom
            })
        });

        this.map.addControl(new ol.control.ZoomSlider());
        this.map.addControl(new ol.control.ScaleLine());
    }

    loadRouteFromGraphHopper(id, profileName) {
        const url = `/geo/route/waypoints?id=${id}&profileName=${profileName}`;

        fetch(url)
            .then(response => response.json())
            .then(data => this.prepareRoute(data));
    }

    prepareRoute(data) {
        this.drawRouteOnMap(data.points);
        this.addStopMarkers(this.currentRoute);
        this.showRouteInfo(data);
        document.querySelector('.badge').textContent = `${data.pointCount} точек`;
    }

    drawRouteOnMap(coordinates) {
        if (this.routeLayer) {
            this.map.removeLayer(this.routeLayer);
            this.routeLayer = null;
        }

        const routeCoords = coordinates.map(coord =>
            ol.proj.fromLonLat([coord[0], coord[1]])
        );

        const routeFeature = new ol.Feature({
            geometry: new ol.geom.LineString(routeCoords)
        });

        routeFeature.setStyle(new ol.style.Style({
            stroke: new ol.style.Stroke({
                color: '#0000ff',
                width: 5,
                lineDash: [12, 8]
            })
        }));

        const routeSource = new ol.source.Vector({
            features: [routeFeature]
        });

        this.routeLayer = new ol.layer.Vector({
            source: routeSource,
            zIndex: 10
        });

        this.map.addLayer(this.routeLayer);

        const extent = routeSource.getExtent();

        if (extent && !Number.isNaN(extent[0]) && !Number.isNaN(extent[1]) && !Number.isNaN(extent[2]) && !Number.isNaN(extent[3])) {
            this.map.getView().fit(extent, {
                padding: [50, 50, 50, 50],
                maxZoom: 16
            });
        }
    }

    addStopMarkers(route) {
        if (this.markersLayer) {
            this.map.removeLayer(this.markersLayer);
            this.markersLayer = null;
        }

        const features = [];

        route.points.forEach((point, index) => {
            const coord = ol.proj.fromLonLat([point.longitude, point.latitude]);

            let label = `📍 ${index}`;
            let bgColor = '#e74c3c';

            const markerStyle = new ol.style.Style({
                image: new ol.style.Circle({
                    radius: 16,
                    fill: new ol.style.Fill({
                        color: bgColor
                    }),
                    stroke: new ol.style.Stroke({
                        color: '#ffffff',
                        width: 3
                    })
                }),
                text: new ol.style.Text({
                    text: String(index + 1),
                    fill: new ol.style.Fill({
                        color: '#ffffff'
                    }),
                    font: 'bold 12px Arial',
                    offsetY: -2
                })
            });

            const feature = new ol.Feature({
                geometry: new ol.geom.Point(coord),
                label: label,
                index: index,
                title: point.label,
                latitude: point.latitude,
                longitude: point.longitude
            });

            feature.setStyle(markerStyle);
            features.push(feature);
        });

        const markerSource = new ol.source.Vector({
            features: features
        });

        this.markersLayer = new ol.layer.Vector({
            source: markerSource,
            zIndex: 20
        });

        this.map.addLayer(this.markersLayer);

        this.map.removeEventListener('click');
        this.map.on('click', event => this.handleMapClick(event));
    }

    handleMapClick(event) {
        const pixel = event.pixel;

        const feature = this.map.forEachFeatureAtPixel(pixel, feat => feat);

        if (feature?.get('label')) {
            const label = feature.get('label');
            const title = feature.get('title');
            const latitude = feature.get('latitude').toFixed(4);
            const longitude = feature.get('longitude').toFixed(4);

            const popupElement = document.createElement('div');
            popupElement.className = 'custom-popup';

            popupElement.style.cssText = `
                position: absolute;
                background: white;
                padding: 10px 14px;
                border-radius: 8px;
                box-shadow: 0 2px 12px rgba(0,0,0,0.2);
                border: 1px solid #ddd;
                min-width: 150px;
                pointer-events: none;
                z-index: 1000;
            `;

            popupElement.innerHTML = `
                <div style="font-weight:bold;font-size:14px;color:#2c3e50;">${label}</div>
                <div style="font-size:13px;color:#555;margin-top:2px;">${title}</div>
                <div style="font-size:11px;color:#999;margin-top:2px;">${latitude}, ${longitude}</div>
            `;

            const overlay = new ol.Overlay({
                element: popupElement,
                positioning: 'bottom-center',
                stopEvent: false,
                offset: [0, -10]
            });

            this.map.addOverlay(overlay);
            overlay.setPosition(feature.getGeometry().getCoordinates());

            setTimeout(() => {
                this.map.removeOverlay(overlay);
            }, 5000);
        }
    }

    showRouteInfo(data) {
        const oldInfo = document.querySelector('.route-info');

        if (oldInfo) {
            oldInfo.remove();
        }

        const distanceKm = data.distanceKm.toFixed(2);
        const timeMinutes = Math.round(data.timeMinutes);
        const pointCount = data.pointCount;

        const info = document.createElement('div');
        info.className = 'route-info';

        info.innerHTML = `
            <h3>🚶 Маршрут</h3>
            <div class="route-color-line" style="background:#ffff00;"></div>
            <div class="route-stats">
                <span><span class="icon">📏</span> ${distanceKm} км</span>
                <span><span class="icon">⏱️</span> ${timeMinutes} мин</span>
            </div>
            <div class="route-stats" style="font-size:12px;color:#888;">
                <span>📍 ${pointCount} точек</span>
            </div>
        `;

        document.body.appendChild(info);
    }
}

globalThis.RouteMap = RouteMap;
