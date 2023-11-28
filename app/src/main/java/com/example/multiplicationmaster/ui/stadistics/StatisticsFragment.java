package com.example.multiplicationmaster.ui.stadistics;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.multiplicationmaster.MainActivity;
import com.example.multiplicationmaster.R;
import com.example.multiplicationmaster.databinding.FragmentStatisticsBinding;
import com.example.multiplicationmaster.ui.settings.CustomSpinnerAdapter;
import com.example.multiplicationmaster.ui.settings.SettingsFragment;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private FragmentStatisticsBinding binding;
    private String dateSelected; // Fecha seleccionada
    private TextView textViewDateSelected;
    private ArrayList<String> tablesSelected; // Tablas de multiplicar seleccionadas
    private ArrayList<String []> mistakes; // Errores cometidos
    private ArrayList<String> percentegesSuccess; // Porcentajes de aciertos
    private ProgressBar progressBarPercentageSuccess;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño y obtener la vista raíz
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtiene la fecha seleccionada
        dateSelected = MainActivity.getDateSelected();
        textViewDateSelected = binding.txvDateSelected;
        textViewDateSelected.setText(dateSelected); // Muestra la fecha seleccionada en la Configuración

        // Obtiene las tablas de multiplicar seleccionadas
        tablesSelected = MainActivity.getTablesSelected();
        configureSpinner(); // Configura el Spinner para seleccionar la tabla practicada y ver los resultados

        // Obtiene los errores cometidos y los porcentajes de aciertos
        mistakes = MainActivity.getMistakes();
        progressBarPercentageSuccess = binding.pgbPercentageSuccess;
        percentegesSuccess = MainActivity.getPercentegesSuccess();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Configura el Spinner
    private void configureSpinner() {
        Spinner spinnerTables = binding.spinnerTables;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tablesSelected);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTables.setAdapter(adapter);
        spinnerTables.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        addMistakes(position);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Añade los errores cometidos
    private void addMistakes(int position) {
        GridLayout gridMistakes = binding.gridMistakes;
        gridMistakes.removeAllViews(); // Elimina los errores de la tabla de multiplicar anteriormente seleccionada

        // Obtiene los errores de la tabla de multiplicar seleccionada
        String [] mistakesTable = mistakes.get(position);

        // Añade los errores a la GridLayout en relacion a la tabla de multiplicar seleccionada
        for (int i = 0; i < mistakesTable.length; i++) {
            String mistake = mistakesTable[i];

            //Personaliza el TextView que muestra el error en la GridL
            TextView textViewMistake = new TextView(getContext());
            textViewMistake.setPadding(10, 0, 50, 10);
            textViewMistake.setTextSize(20);
            textViewMistake.setTextColor(Color.BLACK);
            textViewMistake.setText(mistake);

            //Añade el TextView del error a la GridLayout
            gridMistakes.addView(textViewMistake);

            // Añade los porcentajes de aciertos
            addPercentagesSuccess(position);
        }
    }

    // Añade los porcentajes de aciertos
    @SuppressLint("SetTextI18n")
    private void addPercentagesSuccess(int position) {
        // Actualiza el progreso de la barra
        progressBarPercentageSuccess.setMax(10);
        progressBarPercentageSuccess.setProgress(Integer.parseInt(percentegesSuccess.get(position)));

        // Obtiene el TextView que muestra el porcentaje de aciertos
        TextView textViewPercentageSuccess = binding.txvPercentage;
        textViewPercentageSuccess.setText(percentegesSuccess.get(position) + " %");
    }
}