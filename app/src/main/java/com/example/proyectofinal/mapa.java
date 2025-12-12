package com.example.proyectofinal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

public class mapa extends AppCompatActivity {

    private MapView mapView;
    private TextView tvHeader;
    private Button btnAbrirMaps, btnVolver;

    private String nombreTienda;
    private double latitud, longitud;

    private static final int REQUEST_PERMISSIONS = 1001;

    // Cliente de ubicación de Google Play Services
    private FusedLocationProviderClient fusedLocationClient;
    private Location ubicacionUsuario;

    // Para solicitar actualizaciones si getLastLocation() devuelve null
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuración de OSMDroid (IMPORTANTE: antes de setContentView)
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_mapa);

        // Inicializar vistas
        mapView = findViewById(R.id.mapView);
        tvHeader = findViewById(R.id.tv_header_mapa);
        btnAbrirMaps = findViewById(R.id.btn_abrir_google_maps);
        btnVolver = findViewById(R.id.btn_volver);

        // Recibir el nombre de la tienda desde el Intent
        nombreTienda = getIntent().getStringExtra("tienda");
        if (nombreTienda == null) nombreTienda = "Tienda";

        tvHeader.setText("Ubicación de " + nombreTienda);

        // Inicializar cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Preparar callback por si necesitamos actualizaciones
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) return;
                // Tomamos la última ubicación recibida
                ubicacionUsuario = locationResult.getLastLocation();
                // Detenemos actualizaciones para ahorrar batería
                fusedLocationClient.removeLocationUpdates(locationCallback);
                // Continuar flujo: calcular sucursal más cercana (si aplica) y actualizar mapa
                continuarFlujoDespuesUbicacion();
            }
        };

        // Botones
        btnAbrirMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirEnMapasExterna();
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Si la tienda es Justo & Bueno, cerrada → mensaje y cierre
        if (nombreTienda.equalsIgnoreCase("Justo & Bueno")) {
            Toast.makeText(this, "Esta tienda cerró hace años. Ubicación no disponible.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Pedir permisos (si ya están, continuará el flujo)
        solicitarPermisos();
    }

    /**
     * Solicita permisos de ubicación si no están concedidos.
     * Si ya están concedidos, inicia la obtención de ubicación.
     */
    private void solicitarPermisos() {
        String[] permisos = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permisos, REQUEST_PERMISSIONS);
        } else {
            // Permisos ya concedidos: iniciar flujo
            iniciarFlujoUbicacionYMapa();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido, obteniendo ubicación...", Toast.LENGTH_SHORT).show();
                iniciarFlujoUbicacionYMapa();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado. Mostrando mapa sin tu ubicación.", Toast.LENGTH_LONG).show();
                // Si no hay permiso, mostramos la tienda por defecto (si aplica)
                obtenerCoordenadasTiendaSinUsuario();
                configurarMapa();
            }
        }
    }

    /**
     * Inicia la obtención de la ubicación del usuario y luego configura el mapa.
     * Flujo:
     *  - obtenerCoordenadasTienda() para inicializar lat/long en caso de tiendas distintas a D1
     *  - intentar getLastLocation(); si es null pedir updates
     */
    private void iniciarFlujoUbicacionYMapa() {
        // Si no es D1, fijamos coordenadas de la tienda ahora (D1 requiere usuario para elegir sucursal)
        if (!nombreTienda.equalsIgnoreCase("D1")) {
            obtenerCoordenadasTienda(nombreTienda);
        }

        // Intentar obtener la última ubicación rápidamente
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        ubicacionUsuario = location;
                        continuarFlujoDespuesUbicacion();
                    } else {
                        // Si lastLocation es null, solicitar actualizaciones (GPS)
                        solicitarActualizacionesUbicacion();
                    }
                }
            });
        } catch (SecurityException e) {
            Toast.makeText(this, "Error de permisos al obtener ubicación: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // Mostrar mapa sin usuario
            obtenerCoordenadasTiendaSinUsuario();
            configurarMapa();
        }
    }

    /**
     * Si no se pudo obtener lastLocation, solicitamos actualizaciones temporales.
     */
    private void solicitarActualizacionesUbicacion() {
        try {
            LocationRequest request = LocationRequest.create();
            request.setInterval(2000);
            request.setFastestInterval(1000);
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationClient.requestLocationUpdates(request, locationCallback, null);
            Toast.makeText(this, "Esperando señal GPS...", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "Error al solicitar actualizaciones: " + e.getMessage(), Toast.LENGTH_LONG).show();
            obtenerCoordenadasTiendaSinUsuario();
            configurarMapa();
        }
    }

    /**
     * Continúa el flujo una vez que tenemos la ubicación del usuario (ubicacionUsuario != null).
     * Si la tienda es D1, calcula la sucursal más cercana. Luego configura y actualiza el mapa.
     */
    private void continuarFlujoDespuesUbicacion() {
        if (nombreTienda.equalsIgnoreCase("D1")) {
            calcularD1MasCercano();
        } else {
            // Para otras tiendas, si no se llamó anteriormente a obtenerCoordenadasTienda, lo hacemos ahora
            // (por si entramos por el flujo de permiso tardío)
            obtenerCoordenadasTienda(nombreTienda);
        }

        configurarMapa();
        agregarMarcadorUsuario();
        dibujarRuta();
    }

    /**
     * Obtiene las coordenadas de la tienda si no dependemos de la ubicación del usuario.
     * (Se utiliza en casos donde no se concedieron permisos o para inicializar tiendas que no son D1).
     */
    private void obtenerCoordenadasTiendaSinUsuario() {
        // Coordenadas reales en Dorada, Caldas
        if (nombreTienda.equalsIgnoreCase("Éxito")) {
            latitud = 5.457753763731659;
            longitud = -74.66318108294723;
        } else {
            // Por defecto D1 - Ubicación 1
            latitud = 5.4772622519861915;
            longitud = -74.67364780582078;
        }
    }

    /**
     * Obtiene las coordenadas según la tienda seleccionada.
     * Si es D1, por ahora dejamos la lógica en calcularD1MasCercano() que depende de ubicacionUsuario.
     */
    private void obtenerCoordenadasTienda(String tienda) {
        if (tienda.equalsIgnoreCase("D1")) {
            // Deja que calcularD1MasCercano() decida basándose en ubicacionUsuario
            // Si ubicacionUsuario es null al invocar ese método, se usará la ubicación 1 por defecto
            calcularD1MasCercano();
        } else if (tienda.equalsIgnoreCase("Éxito")) {
            latitud = 5.457753763731659;
            longitud = -74.66318108294723;
        } else {
            // Default
            latitud = 5.4772622519861915;
            longitud = -74.67364780582078;
        }
    }

    /**
     * Calcula cuál D1 está más cerca del usuario y establece esas coordenadas.
     * Si no hay ubicación de usuario, usa la ubicación 1 por defecto.
     */
    private void calcularD1MasCercano() {
        double lat1 = 5.4772622519861915, lon1 = -74.67364780582078;  // D1 - 1
        double lat2 = 5.461711832137912, lon2 = -74.66592339968278;   // D1 - 2
        double lat3 = 5.481916043961331, lon3 = -74.67315659459213;   // D1 - 3

        if (ubicacionUsuario == null) {
            // No tenemos ubicación del usuario todavía -> usar D1-1 por defecto
            latitud = lat1;
            longitud = lon1;
            return;
        }

        float d1 = calcularDistancia(ubicacionUsuario.getLatitude(), ubicacionUsuario.getLongitude(), lat1, lon1);
        float d2 = calcularDistancia(ubicacionUsuario.getLatitude(), ubicacionUsuario.getLongitude(), lat2, lon2);
        float d3 = calcularDistancia(ubicacionUsuario.getLatitude(), ubicacionUsuario.getLongitude(), lat3, lon3);

        if (d2 < d1 && d2 < d3) {
            latitud = lat2;
            longitud = lon2;
        } else if (d3 < d1 && d3 < d2) {
            latitud = lat3;
            longitud = lon3;
        } else {
            latitud = lat1;
            longitud = lon1;
        }
    }

    /**
     * Calcula la distancia entre dos puntos geográficos en metros
     */
    private float calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        android.location.Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }

    /**
     * Configura el mapa de OpenStreetMap y agrega el marcador de la tienda.
     * Limpia overlays previos para evitar duplicados.
     */
    private void configurarMapa() {
        if (mapView == null) return;

        mapView.getOverlays().clear();

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(false);

        GeoPoint puntoTienda = new GeoPoint(latitud, longitud);
        mapView.getController().setZoom(17.0);
        mapView.getController().setCenter(puntoTienda);

        Marker marcador = new Marker(mapView);
        marcador.setPosition(puntoTienda);
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marcador.setTitle(nombreTienda);
        marcador.setSnippet("Toca para más información");
        mapView.getOverlays().add(marcador);

        mapView.invalidate();
    }

    /**
     * Agrega un marcador azul en la ubicación del usuario
     */
    private void agregarMarcadorUsuario() {
        if (ubicacionUsuario == null || mapView == null) return;

        GeoPoint puntoUsuario = new GeoPoint(ubicacionUsuario.getLatitude(), ubicacionUsuario.getLongitude());

        Marker marcadorUsuario = new Marker(mapView);
        marcadorUsuario.setPosition(puntoUsuario);
        marcadorUsuario.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marcadorUsuario.setTitle("Tu ubicación");
        marcadorUsuario.setSnippet("Estás aquí");
        marcadorUsuario.setIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_mylocation));

        mapView.getOverlays().add(marcadorUsuario);
        mapView.invalidate();
    }

    /**
     * Dibuja una línea entre la ubicación del usuario y la tienda y muestra la distancia.
     */
    private void dibujarRuta() {
        if (ubicacionUsuario == null || mapView == null) return;

        GeoPoint puntoUsuario = new GeoPoint(ubicacionUsuario.getLatitude(), ubicacionUsuario.getLongitude());
        GeoPoint puntoTienda = new GeoPoint(latitud, longitud);

        Polyline linea = new Polyline();
        linea.addPoint(puntoUsuario);
        linea.addPoint(puntoTienda);
        linea.setColor(Color.BLUE);
        linea.setWidth(5f);

        mapView.getOverlays().add(linea);
        mapView.invalidate();

        float distancia = calcularDistancia(
                ubicacionUsuario.getLatitude(),
                ubicacionUsuario.getLongitude(),
                latitud,
                longitud
        );

        Toast.makeText(this, String.format("Distancia: %.2f km", distancia / 1000f), Toast.LENGTH_LONG).show();
    }

    /**
     * Abre la ubicación en una aplicación de mapas externa (Google Maps, Waze, etc.)
     */
    private void abrirEnMapasExterna() {
        String uri = "geo:" + latitud + "," + longitud + "?q=" + latitud + "," + longitud + "(" + nombreTienda + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No hay ninguna aplicación de mapas instalada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDetach();
        // Quitar actualizaciones si quedaron
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        } catch (Exception ignored) {}
    }
}
