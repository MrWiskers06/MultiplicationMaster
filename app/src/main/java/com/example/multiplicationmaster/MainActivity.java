package com.example.multiplicationmaster;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

import com.example.multiplicationmaster.dialogs.DateDialogListener;
import com.example.multiplicationmaster.dialogs.DifficultyDialogListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.multiplicationmaster.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements DifficultyDialogListener, DateDialogListener {
    private ActivityMainBinding binding; // Enlace de datos
    private static final int[] AVATAR_ITACHI = {
            R.drawable.itachi_2, R.drawable.itachi_5,
            R.drawable.itachi_7, R.drawable.itachi_8, R.drawable.itachi_9
    };

    private static final int[] AVATAR_HINATA = {
            R.drawable.hinnata_2, R.drawable.hinnata_5,
            R.drawable.hinnata_7, R.drawable.hinnata_8, R.drawable.hinnata_9
    };

    private static final int[] AVATAR_NARUTO = {
            R.drawable.naruto_2, R.drawable.naruto_5,
            R.drawable.naruto_7, R.drawable.naruto_8, R.drawable.naruto_9
    };

    private static final int[] AVATAR_SASUKE = {
            R.drawable.sasuke_2, R.drawable.sasuke_5,
            R.drawable.sasuke_7, R.drawable.sasuke_8, R.drawable.sasuke_9
    };

    private static final int[] AVATAR_KAKASHI = {
            R.drawable.kakashi_2, R.drawable.kakashi_5,
            R.drawable.kakashi_7, R.drawable.kakashi_8, R.drawable.kakashi_9
    };
    private static String avatarSelected;
    private static int avatarImgSelected;
    private static ArrayList<String> avatarsCompleted = new ArrayList<>(); // Avatares conseguidos al completo
    private static String tableSelected; // Recupera la tabla de multiplicar seleccionada
    private static int difficultySelected = 0; // Nivel de dificultad seleccionado por defecto
    private EditText edtDateSelected; // EditText para mostrar la fecha seleccionada
    private static String dateSelected; // Fecha seleccionada
    private static ArrayList<String> tablesSelected = new ArrayList<>(); // Tablas de multiplicar seleccionadas
    private static ArrayList<String []> mistakes = new ArrayList<>(); // Errores cometidos
    private static ArrayList<String> percentegesSuccess = new ArrayList<>(); // Porcentajes de aciertos


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
    public static String getTableSelected() {
        return tableSelected;
    }
    public static void setTableSelected(String tableSelected) {
        MainActivity.tableSelected = tableSelected;
    }

    // Getter and setter for Difficulty
    public static int getDifficultySelected() {
        return difficultySelected;
    }
    public static void setDifficultySelected(int difficultySelected) {
        MainActivity.difficultySelected = difficultySelected;
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
    public static ArrayList<String> getAvatarsCompleted() {
        return avatarsCompleted;
    }
    public static void addAvatarCompleted(String avatarCompleted) {
        if (!avatarsCompleted.contains(avatarCompleted)) {
            avatarsCompleted.add(avatarCompleted);
        }
    }

    //Getter and setter para la fecha
    public static String getDateSelected() {
        return dateSelected;
    }
    public static void setDateSelected(String dateSelected) {
        MainActivity.dateSelected = dateSelected;
    }

    //Getter and setter para las tablas seleccionadas
    public static ArrayList<String> getTablesSelected() {
        return tablesSelected;
    }
    public static void setTablesSelected(ArrayList<String> tablesSelected) {
        MainActivity.tablesSelected = tablesSelected;
    }

    //Getter and setter para los errores
    public static ArrayList<String[]> getMistakes() {
        return mistakes;
    }
    public static void setMistakes(ArrayList<String[]> mistakes) {
        MainActivity.mistakes = mistakes;
    }

    //Getter and setter para los porcentajes de aciertos
    public static ArrayList<String> getPercentegesSuccess() {
        return percentegesSuccess;
    }
    public static void setPercentegesSuccess(ArrayList<String> percentegesSuccess) {
        MainActivity.percentegesSuccess = percentegesSuccess;
    }

    //Implementacion de la interfaz para la dificultad
    @Override
    public void onChangeDifficulty(int level) {
        difficultySelected = level;
    }

    //Implementacion de la interfaz para la fecha
    @SuppressLint("SetTextI18n")
    @Override
    public void onSelectedDate(GregorianCalendar date) {
        edtDateSelected = findViewById(R.id.edt_fecha);

        edtDateSelected.setText(date.get(Calendar.DAY_OF_MONTH) + "/"
                + (date.get(Calendar.MONTH) + 1) + "/" // Se suma 1 porque en Calendar, enero es 0
                + date.get(Calendar.YEAR));

        dateSelected = edtDateSelected.getText().toString().trim();
    }
}
