package com.example.multiplicationmaster;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.multiplicationmaster.databinding.FragmentSettingsBinding;
import com.example.multiplicationmaster.dialogs.DateDialogListener;
import com.example.multiplicationmaster.dialogs.DifficultyDialogListener;
import com.example.multiplicationmaster.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.multiplicationmaster.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements DifficultyDialogListener, DateDialogListener {
    private ActivityMainBinding binding; // Enlace de datos
    private static final int[] AVATAR_ITACHI = {
            R.drawable.itachi_1, R.drawable.itachi_2,
            R.drawable.itachi_3, R.drawable.itachi_4, R.drawable.itachi_5,
            R.drawable.itachi_6, R.drawable.itachi_7, R.drawable.itachi_8,
            R.drawable.itachi_9, R.drawable.itachi_10
    };

    private static final int[] AVATAR_HINATA = {
            R.drawable.hinnata_1, R.drawable.hinnata_2,
            R.drawable.hinnata_3, R.drawable.hinnata_4, R.drawable.hinnata_5,
            R.drawable.hinnata_6, R.drawable.hinnata_7, R.drawable.hinnata_8,
            R.drawable.hinnata_9, R.drawable.hinnata_10
    };

    private static final int[] AVATAR_NARUTO = {
            R.drawable.naruto_1, R.drawable.naruto_2,
            R.drawable.naruto_3, R.drawable.naruto_4, R.drawable.naruto_5,
            R.drawable.naruto_6, R.drawable.naruto_7, R.drawable.naruto_8,
            R.drawable.naruto_9, R.drawable.naruto_10
    };

    private static final int[] AVATAR_SASUKE = {
            R.drawable.sasuke_1, R.drawable.sasuke_2,
            R.drawable.sasuke_3, R.drawable.sasuke_4, R.drawable.sasuke_5,
            R.drawable.sasuke_6, R.drawable.sasuke_7, R.drawable.sasuke_8,
            R.drawable.sasuke_9, R.drawable.sasuke_10
    };

    private static final int[] AVATAR_KAKASHI = {
            R.drawable.kakashi_1, R.drawable.kakashi_2,
            R.drawable.kakashi_3, R.drawable.kakashi_4, R.drawable.kakashi_5,
            R.drawable.kakashi_6, R.drawable.kakashi_7, R.drawable.kakashi_8,
            R.drawable.kakashi_9, R.drawable.kakashi_10
    };
    private static String avatarSelected;
    private static int avatarImgSelected;
    private static String tableSelect; // Recupera la tabla de multiplicar seleccionada
    private static int selectedDifficulty = 0; // Nivel de dificultad seleccionado por defecto
    private EditText selectDate; // EditText para mostrar la fecha seleccionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Infla el diseño de la actividad utilizando el enlace de datos generado por View Binding.
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureActionBarAndBottomNav();
    }

    // Configuración de la barra de aplicaciones y BottomNavigationView
    private void configureActionBarAndBottomNav() {
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

    //Getter and setter for table selection
    public static String getTableSelect() {
        return tableSelect;
    }
    public static void setTableSelect(String tableSelect) {
        MainActivity.tableSelect = tableSelect;
    }

    // Getter and setter for Difficulty
    public static int getSelectedDifficulty() {
        return selectedDifficulty;
    }
    public static void setSelectedDifficulty(int selectedDifficulty) {
        MainActivity.selectedDifficulty = selectedDifficulty;
    }

    //Getter and setter para el avatar y avatarImage
    public static String getAvatarSelected() {
        return avatarSelected;
    }
    public static void setAvatarSelected(String avatarSelected) {
        MainActivity.avatarSelected = avatarSelected;
    }
    public static int getAvatarImgSelected() {
        return avatarImgSelected;
    }
    public static void setAvatarImgSelected(int avatarImgSelected) {
        MainActivity.avatarImgSelected = avatarImgSelected;
    }
    public static int[] getAvatarProgressImages(String avatarName) {
        switch (avatarName) {
            case "Itachi":
                return AVATAR_ITACHI;
            case "Hinata":
                return AVATAR_HINATA;
            case "Naruto":
                return AVATAR_NARUTO;
            case "Sasuke":
                return AVATAR_SASUKE;
            case "Kakashi":
                return AVATAR_KAKASHI;
            default:
                return null;
        }
    }

    //Implementacion de la interfaz para la dificultad
    @Override
    public void onChangeDifficulty(int level) {
        selectedDifficulty = level;
    }

    //Implementacion de la interfaz para la fecha
    @SuppressLint("SetTextI18n")
    @Override
    public void onSelectedDate(GregorianCalendar date) {
        selectDate = findViewById(R.id.edt_fecha);

        selectDate.setText(date.get(Calendar.DAY_OF_MONTH) + "/"
                + (date.get(Calendar.MONTH) + 1) + "/" // Se suma 1 porque en Calendar, enero es 0
                + date.get(Calendar.YEAR));

        selectDate.getText(); // Puedes realizar acciones adicionales al seleccionar la fecha, falta aplicar método que recupera la fecha seleccionada para el Fragment Estadisticas
    }
}
