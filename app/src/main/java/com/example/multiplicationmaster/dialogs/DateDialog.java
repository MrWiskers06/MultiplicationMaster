package com.example.multiplicationmaster.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DateDialogListener dateListener; // Listener para comunicar la fecha seleccionada al fragmento que lo invoca

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear un nuevo DatePickerDialog con la fecha actual
        return new DatePickerDialog(requireActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // Cuando el usuario selecciona una fecha en el DatePickerDialog, se llama a este método
        // Se crea un objeto GregorianCalendar con la fecha seleccionada y se notifica al listener
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, day);
        dateListener.onSelectedDate(gregorianCalendar);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dateListener = (DateDialogListener) context;
        }catch (Exception ex){
            System.out.println("Error a implementar la interfaz");
        }
    }
}

