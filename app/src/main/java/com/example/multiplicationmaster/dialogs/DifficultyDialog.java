package com.example.multiplicationmaster.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.multiplicationmaster.R;

public class DifficultyDialog extends DialogFragment {
    private DifficultyDialogListener difficultyListener; // Listener para comunicar la dificultad seleccionada al fragmento que lo invoca
    private int difficultySelected; // Dificultad seleccionada actualmente

    // Constructor que recibe la dificultad seleccionada actualmente (por defecto, FACIL)
    public DifficultyDialog(int difficultySelected) {
        this.difficultySelected = difficultySelected;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Configurar las opciones de dificultad
        final String[] difficultyOptions = {
                getString(R.string.easy_difficulty),
                getString(R.string.normal_difficulty),
                getString(R.string.hard_difficulty)
        };

        // Crear un AlertDialog con las opciones de dificultad
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_difficulty);

        // Configurar las opciones de dificultad como elementos seleccionables
        builder.setSingleChoiceItems(difficultyOptions, difficultySelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                // Cuando se selecciona una dificultad, se notifica al listener
                difficultyListener.onChangeDifficulty(index);
            }
        });

        // Configurar el botón positivo del diálogo (puede ser el botón "Aceptar" o similar)
        builder.setPositiveButton(R.string.exit_dialog, null);

        // Crear y devolver el diálogo configurado
        return builder.create();
    }

    public void setDifficultyListener(DifficultyDialogListener listener) {
        // Método para establecer el listener desde el fragmento que invoca el DifficultyDialog
        this.difficultyListener = listener;
    }
}

