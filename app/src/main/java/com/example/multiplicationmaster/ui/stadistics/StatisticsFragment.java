package com.example.multiplicationmaster.ui.stadistics;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.multiplicationmaster.MainActivity;
import com.example.multiplicationmaster.R;
import com.example.multiplicationmaster.databinding.FragmentStatisticsBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StatisticsFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private FragmentStatisticsBinding binding;
    private ImageView imgAvatarHinnata, imgAvatarItachi, imgAvatarKakashi, imgAvatarNaruto, imgAvatarSasuke; // Imagen del avatar seleccionado y conseguido al completo
    private String dateSelected; // Fecha seleccionada
    private TextView textViewDateSelected;
    private ArrayList<String> tablesCompleted; // Tablas de multiplicar completadas
    private ArrayList<ArrayList<String>> mistakes; // Errores cometidos en las tablas completadas
    private ArrayList<String> percentegesSuccess; // Porcentajes de aciertos de las tablas completadas
    private ProgressBar progressBarPercentageSuccess;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño y obtener la vista raíz
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Recupera los ImageView de los avatares
        imgAvatarHinnata = binding.imgHinnata;
        imgAvatarItachi = binding.imgItachi;
        imgAvatarKakashi = binding.imgKakashi;
        imgAvatarNaruto = binding.imgNaruto;
        imgAvatarSasuke = binding.imgSasuke;

        // Configurar imagenes en blanco y negro

        // Recupera la fecha seleccionada
        dateSelected = MainActivity.getDateSelected();
        textViewDateSelected = binding.txvDateSelected;
        configureDate();

        tablesCompleted = MainActivity.getTablesCompleted(); // Obtiene las tablas de multiplicar completadas

        configureSpinner(); // Configura el Spinner para seleccionar la tabla practicada y ver los resultados
        configureImages(); // Configura el color de las imagenes en blanco y negro
        // Recupera los errores cometidos en las tablas practicadas y los porcentajes de aciertos
        mistakes = MainActivity.getMistakes();
        percentegesSuccess = MainActivity.getPercentegesSuccess();

        // Recupera el botón para enviar las estadísticas por mail y configura el onClickListener
        Button btnSendMail = binding.btnSendMail;
        btnSendMail.setOnClickListener(this::sendMail);

        // Añade los avatares conseguidos
        addAvatars();

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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tablesCompleted);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTables.setAdapter(adapter);
        spinnerTables.setOnItemSelectedListener(this);
    }

    // Configura la imagenes de los avatares en blanco y negro
    private void configureImages() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f); // Establece la matriz para la saturacion de colores (black&white)

        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix); // Establece el filtro para la imagen en base a la matriz de creada

        imgAvatarHinnata.setColorFilter(colorFilter);
        imgAvatarItachi.setColorFilter(colorFilter);
        imgAvatarKakashi.setColorFilter(colorFilter);
        imgAvatarSasuke.setColorFilter(colorFilter);
        imgAvatarNaruto.setColorFilter(colorFilter);
    }

    private void configureDate(){
        if (dateSelected != null){
            textViewDateSelected.setText(dateSelected); // Muestra la fecha seleccionada en la Configuración
        }else{
            // Si no se ha seleccionado ninguna fecha, obtener la fecha del sistema
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String currentDate = sdf.format(new Date());
            textViewDateSelected.setText(currentDate);
        }
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
        progressBarPercentageSuccess = binding.pgbPercentageSuccess;

        gridMistakes.removeAllViews(); // Elimina los errores de la tabla de multiplicar anteriormente seleccionada

        // Obtiene los errores de la tabla de multiplicar seleccionada
        ArrayList<String> mistakesTableSelected = mistakes.get(position);

        if (mistakesTableSelected.size() == 0) {
            //Personaliza el TextView que muestra el error en el Grid
            TextView textViewMistake = new TextView(getContext());
            textViewMistake.setPadding(10, 0, 50, 10);
            textViewMistake.setTextSize(16);
            textViewMistake.setTextColor(Color.BLACK);
            textViewMistake.setText(R.string.message_noMistakes);

            //Añade el TextView del error a la GridLayout
            gridMistakes.addView(textViewMistake);
        }else {
            // Añade los errores a la GridLayout en relacion a la tabla de multiplicar seleccionada en el spinner
            for (int i = 0; i < mistakesTableSelected.size(); i++) {
                String mistake = mistakesTableSelected.get(i);

                //Personaliza el TextView que muestra el error en el Grid
                TextView textViewMistake = new TextView(getContext());
                textViewMistake.setPadding(10, 0, 50, 10);
                textViewMistake.setTextSize(16);
                textViewMistake.setTextColor(Color.BLACK);
                textViewMistake.setText(mistake);

                //Añade el TextView del error a la GridLayout
                gridMistakes.addView(textViewMistake);
            }
        }

        // Añade los porcentajes de aciertos
        addPercentagesSuccess(position);
    }

    // Añade los porcentajes de aciertos
    @SuppressLint("SetTextI18n")
    private void addPercentagesSuccess(int position) {
        // Actualiza el progreso de la barra
        progressBarPercentageSuccess.setMax(100);
        progressBarPercentageSuccess.setProgress(Integer.parseInt(percentegesSuccess.get(position)));

        // Obtiene el TextView que muestra el porcentaje de aciertos
        TextView textViewPercentageSuccess = binding.txvPercentage;
        textViewPercentageSuccess.setText(percentegesSuccess.get(position) + " %");
    }

    // Añade los avatares conseguidos
    private void addAvatars(){
        // Obtiene la lista de avatares conseguidos
        // Avatares conseguidos al completo
        ArrayList<String> avatarsCompleted = MainActivity.getAvatarsCompleted();

        // Comprueba si hay avatares conseguidos y los muestra
        if (avatarsCompleted.size() > 0) {
            // Muestra el avatar completado en la ImageView correspondiente
            for (String avatarCompleted : avatarsCompleted) {
                switch (avatarCompleted) {
                    case "Hinata":
                        imgAvatarHinnata.clearColorFilter();
                        break;
                    case "Itachi":
                        imgAvatarItachi.clearColorFilter();
                        break;
                    case "Kakashi":
                        imgAvatarKakashi.clearColorFilter();
                        break;
                    case "Naruto":
                        imgAvatarNaruto.clearColorFilter();
                        break;
                    case "Sasuke":
                        imgAvatarSasuke.clearColorFilter();
                        break;
                }
            }
        }

    }

    // Envia las estadisticas por mail al usuario
    @SuppressLint("IntentReset")
    public void sendMail(View view) {
        Intent intent = new Intent();
        Intent chooser = null;
        EditText editTextEmail = binding.edtMail;

        if (view.getId() == R.id.btn_sendMail && !tablesCompleted.isEmpty() && editTextEmail.getText().toString().trim().length() > 0) {
            intent.setAction(Intent.ACTION_SEND);
            intent.setData(Uri.parse("mailto:"));
            String[] posting = {editTextEmail.getText().toString().trim()};
            intent.putExtra(Intent.EXTRA_EMAIL, posting);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Multiplication Master - Estadísticas");
            intent.putExtra(Intent.EXTRA_TEXT, emailBody());
            intent.setType("message/rfc822");
            chooser = Intent.createChooser(intent, "Enviar Email");
            startActivity(intent);
        }else{
            editTextEmail.setError("Introduce un email válido");
        }
    }

    private String emailBody(){
        // Formatea el texto para el cuerpo del correo
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Estas son tus estadísticas de Multiplication Master:\n\n");
        emailBody.append("Fecha: ").append(dateSelected).append("\n\n");
        emailBody.append("Tablas de multiplicar practicadas: \n");
        for (int i = 0; i < tablesCompleted.size(); i++){
            emailBody.append("  - ").append(tablesCompleted.get(i)).append("\n");
        }
        emailBody.append("\n");
        // Formatea los errores cometidos
        for (int i = 0; i < tablesCompleted.size(); i++) {
            emailBody.append("Errores cometidos en la tabla ").append(tablesCompleted.get(i)).append(": \n\n");
            ArrayList<String> mistakesCurrentTable = mistakes.get(i);
            // Verifica si la lista de errores no es nula y no está vacía
            if (mistakesCurrentTable != null && mistakesCurrentTable.size() > 0) {
                for (String mistake : mistakesCurrentTable) {
                    // Verifica si el campo mistake no es nulo
                    if (mistake != null) {
                        emailBody.append("  - ").append(mistake).append("\n");
                    }
                }
                emailBody.append("\n");
            } else {
                emailBody.append("  - ¡FELICIDADES! No se registraron errores en esta tabla.\n\n");
            }
        }

        // Formatea los porcentajes de aciertos
        emailBody.append("Porcentajes de aciertos: ").append("\n");
        for (int i = 0; i < tablesCompleted.size(); i++) {
            emailBody.append("  - ").append(tablesCompleted.get(i)).append(": ").append(percentegesSuccess.get(i)).append("%\n");
        }

        emailBody.append("\n¡¡¡Sigue practicando para conseguir más avatares!!!");

        return emailBody.toString();
    }
}