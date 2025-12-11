package com.example.proyectofinal;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class mapa extends AppCompatActivity {

    private MapView mapView;
    private TextView tvHeader;
    private Button btnAbrirMaps, btnVolver;

    private String nombreTienda;
    private double latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_mapa);

        // Buscar vistas CON VERIFICACIÓN
        mapView = findViewById(R.id.mapView);
        if (mapView == null) {
            Toast.makeText(this, "ERROR: mapView no encontrado", Toast.LENGTH_LONG).show();
            return;
        }

        tvHeader = findViewById(R.id.tv_header_mapa);
        if (tvHeader == null) {
            Toast.makeText(this, "ERROR: tv_header_mapa no encontrado", Toast.LENGTH_LONG).show();
            return;
        }

        btnAbrirMaps = findViewById(R.id.btn_abrir_google_maps);
        if (btnAbrirMaps == null) {
            Toast.makeText(this, "ADVERTENCIA: btn_abrir_google_maps no encontrado", Toast.LENGTH_LONG).show();
        }

        btnVolver = findViewById(R.id.btn_volver);
        if (btnVolver == null) {
            Toast.makeText(this, "ERROR: btn_volver no encontrado", Toast.LENGTH_LONG).show();
            return;
        }

        // Recibir datos
        nombreTienda = getIntent().getStringExtra("tienda");
        if (nombreTienda == null) nombreTienda = "Tienda";

        tvHeader.setText("Ubicación de " + nombreTienda);
        obtenerCoordenadasTienda(nombreTienda);
        configurarMapa();

        // Listeners SOLO si existen
        if (btnAbrirMaps != null) {
            btnAbrirMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mapa.this, "Abrir en Maps", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void obtenerCoordenadasTienda(String tienda) {
        if (tienda.equals("D1")) {
            latitud = 4.7110;
            longitud = -74.0721;
        } else if (tienda.equals("Justo & Bueno")) {
            latitud = 4.7100;
            longitud = -74.0731;
        } else if (tienda.equals("Éxito")) {
            latitud = 4.7120;
            longitud = -74.0711;
        } else {
            latitud = 4.7110;
            longitud = -74.0721;
        }
    }

    private void configurarMapa() {
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

        mapView.getOverlays().add(marcador);
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
    }
}