package com.example.multiplicationmaster;

import android.os.Bundle;
import android.widget.Toast;

import com.example.multiplicationmaster.dialogs.DifficultyDialogListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.multiplicationmaster.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; // Enlace de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Infla el diseño de la actividad utilizando el enlace de datos generado por View Binding.
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtiene una referencia al elemento BottomNavigationView con el ID nav_view del diseño de la actividad.
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Paso de cada ID de menú como un conjunto de IDs porque cada menú debe considerarse como destinos de nivel superior.
        // Crea una configuración de la barra de aplicaciones que especifica qué destinos deben considerarse como destinos de nivel superior en tu aplicación.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_settings, R.id.navigation_train, R.id.navigation_statistics).build();

        // Obtiene el NavController asociado con el nav_host_fragment_activity_main de tu diseño.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Configura la barra de aplicaciones (ActionBar) para mostrar la navegación entre destinos.
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Configura el BottomNavigationView para reflejar la navegación entre destinos.
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}
