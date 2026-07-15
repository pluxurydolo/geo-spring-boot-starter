class GeoMap {
    map = null;
    vectorSource = null;
    vectorLayer = null;
    popupOverlay = null;
    popupElement = null;
    currentUser = '';
    currentPoints = [];

    init(points, user) {
        this.currentUser = user;
        this.currentPoints = points;

        const centerLatitude = 55.7558;
        const centerLongitude = 37.6173;
        const zoom = 12;

        this.initMap(centerLatitude, centerLongitude, zoom);
        this.addPoints(this.currentPoints);
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

        this.vectorSource = new ol.source.Vector();

        this.vectorLayer = new ol.layer.Vector({
            source: this.vectorSource
        });

        this.map.addLayer(this.vectorLayer);

        this.initPopup();

        this.map.removeEventListener('click');
        this.map.on('click', event => this.handleMapClick(event));
    }

    initPopup() {
        this.popupElement = document.createElement('div');
        this.popupElement.className = 'ol-popup';

        this.popupElement.style.cssText = `
            position: absolute;
            background: white;
            padding: 12px 16px;
            border-radius: 8px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.2);
            border: 1px solid #ddd;
            min-width: 200px;
            pointer-events: none;
            display: none;
            z-index: 1000;
        `;

        document.body.appendChild(this.popupElement);

        this.popupOverlay = new ol.Overlay({
            element: this.popupElement,
            positioning: 'bottom-center',
            stopEvent: false,
            offset: [0, -10]
        });

        this.map.addOverlay(this.popupOverlay);
    }

    handleMapClick(event) {
        const pixel = event.pixel;

        const feature = this.map.forEachFeatureAtPixel(pixel, feat => feat);

        if (feature) {
            const coord = feature.getGeometry().getCoordinates();
            const title = feature.get('title');
            const description = feature.get('description');
            const latitude = feature.get('latitude').toFixed(4);
            const longitude = feature.get('longitude').toFixed(4);

            this.popupElement.innerHTML = `
                <div class="custom-popup">
                    <div class="popup-title">${title}</div>
                    <div class="popup-desc">${description}</div>
                    <div class="popup-coords">${latitude}, ${longitude}</div>
                </div>
            `;

            this.popupOverlay.setPosition(coord);
            this.popupElement.style.display = 'block';
        } else {
            this.popupElement.style.display = 'none';
            this.popupOverlay.setPosition(undefined);
        }
    }

    createMarkerStyle() {
        return new ol.style.Style({
            image: new ol.style.Circle({
                radius: 12,
                fill: new ol.style.Fill({
                    color: '#000000'
                }),
                stroke: new ol.style.Stroke({
                    color: '#ffffff',
                    width: 2
                })
            })
        });
    }

    addPoints(points) {
        this.vectorSource.clear();

        points.forEach(point => {
            const coord = ol.proj.fromLonLat([point.longitude, point.latitude]);

            const feature = new ol.Feature({
                geometry: new ol.geom.Point(coord),
                title: point.title,
                description: point.description,
                latitude: point.latitude,
                longitude: point.longitude
            });

            feature.setStyle(this.createMarkerStyle());
            this.vectorSource.addFeature(feature);
        });

        if (this.vectorSource.getFeatures().length > 0) {
            const extent = this.vectorSource.getExtent();

            this.map.getView().fit(extent, {
                padding: [50, 50, 50, 50],
                maxZoom: 15
            });
        }

        const badge = document.querySelector('.badge');

        if (badge) {
            badge.textContent = `${points.length} точек`;
        }
    }
}

globalThis.GeoMap = GeoMap;
