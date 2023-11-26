package com.example.multiplicationmaster.ui.stadistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.multiplicationmaster.MainActivity;
import com.example.multiplicationmaster.databinding.FragmentStatisticsBinding;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {
    private FragmentStatisticsBinding binding;
    private String dateSelected; // Fecha seleccionada
    private ArrayList<String> tablesSelected; // Tablas de multiplicar seleccionadas
    private ArrayList<String []> mistakes; // Errores cometidos
    private ArrayList<String> percentegesSuccess; // Porcentajes de aciertos


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño y obtener la vista raíz
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dateSelected = MainActivity.getDateSelected();
        tablesSelected = MainActivity.getTablesSelected();
        mistakes = MainActivity.getMistakes();
        percentegesSuccess = MainActivity.getPercentegesSuccess();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}