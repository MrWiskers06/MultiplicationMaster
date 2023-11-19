package com.example.multiplicationmaster.ui.train;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.multiplicationmaster.MainActivity;
import com.example.multiplicationmaster.R;
import com.example.multiplicationmaster.databinding.FragmentTrainBinding;
import com.example.multiplicationmaster.ui.settings.SettingsFragment;

import java.util.ArrayList;
import java.util.Collections;

public class TrainFragment extends Fragment {

    private FragmentTrainBinding binding;
    private Handler handlerShowResults = new Handler();
    private static final long DELAY_NEXT_MULTIPLICATION = 2500;
    private String tableSelected;
    private ArrayList<Integer> randomOrder;
    private int expectedMultiplier;
    private EditText editTextResult;
    private EditText editTextTable;
    private EditText editTextResultExpected;
    private int currentMultiplier;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño y obtener la vista raíz
        binding = FragmentTrainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tableSelected = MainActivity.getTableSelect();

        editTextTable = binding.edtMultiplication;
        editTextTable.setInputType(InputType.TYPE_NULL); // Evita que aparezca el teclado
        editTextTable.setFocusable(false); // Hace que el EditText no sea enfocable, útil para evitar doble clic ya que el teclado no se mostrará

        editTextResult = binding.edtResult;
        editTextResult.setInputType(InputType.TYPE_NULL); // Evita que aparezca el teclado
        editTextResult.setFocusable(false); // Hace que el EditText no sea enfocable, útil para evitar doble clic ya que el teclado no se mostrará

        editTextResultExpected = binding.edtResultOK;
        editTextResultExpected.setInputType(InputType.TYPE_NULL);
        editTextResultExpected.setFocusable(false);

        progressBar = binding.pgbMultiplication;

        // Añade los botones de las tablas de multiplicar para los resultados
        addButtons();
        //Muestra el avatar seleccionado
        //getAvatarSelected();
        // Añade la tabla de multiplicar seleccionada por el usuario en la configuracion
        addTableMultiplication();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Preguntar sobre esto, tiene que haber mejro forma de ahcerlo
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        binding = null;
    }

    public void getAvatarSelected() {
        ImageView imgAvatar = binding.imgAvatar;
        imgAvatar.setImageResource(MainActivity.getAvatarImgSelected());

    }
    // Método para mostrar la siguiente imagen del avatar
    private void showNextAvatarImage() {
        ImageView imgAvatar = binding.imgAvatar;
        String avatarName = MainActivity.getAvatarSelected();
        int[] avatarProgressImages = MainActivity.getAvatarProgressImages(avatarName);

        if (avatarProgressImages != null && currentMultiplier >= 0 && currentMultiplier < avatarProgressImages.length) {
            imgAvatar.setImageResource(avatarProgressImages[currentMultiplier]);
        }
    }

    @SuppressLint("SetTextI18n")
    public void addTableMultiplication() {

        if (tableSelected != null && !tableSelected.isEmpty()) {
            int table = Integer.parseInt(tableSelected);

            currentMultiplier = 0;  // Multiplicador actual
            // Configura el orden aleatorio en caso de la dificultad dificil
            randomOrder = generateRandomOrder();
            showNextMultiplication(table);
        } else {
            // Si el usuario no ha seleccionado ninguna tabla, mostrar un mensaje o tomar alguna acción
            editTextResult.setEnabled(false);
            Toast.makeText(getContext(), "Por favor, selecciona una tabla en la configuración", Toast.LENGTH_LONG).show();
            // Puedes redirigir al usuario a la pantalla de configuración o realizar alguna otra acción.
        }
    }

    private ArrayList<Integer> generateRandomOrder() {
        ArrayList<Integer> order = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            order.add(i);
        }
        Collections.shuffle(order);
        return order;
    }

    private void showNextMultiplication(int table) {
        if (currentMultiplier < 11) {
            int multiplier;

            // Obtén la dificultad seleccionada
            int difficulty = MainActivity.getSelectedDifficulty();

            // Ajusta el orden de presentación según la dificultad
            switch (difficulty) {
                case 1: // Medio (descendente)
                    multiplier = 10 - currentMultiplier;
                    break;
                case 2: // Difícil (aleatorio)
                    multiplier = randomOrder.get(currentMultiplier);
                    break;
                default:
                    multiplier = currentMultiplier;
                    break;
            }

            String multiplicationText = table + " X " + multiplier + " = ";
            editTextTable.setText(multiplicationText);

            // Actualiza el progreso de la barra
            int progress = (currentMultiplier * 100) / 11;
            progressBar.setProgress(progress);
        }

        if (currentMultiplier == 11){
            Toast.makeText(getContext(), "Enhorabuena, has finalizado la tabla del " + table, Toast.LENGTH_LONG).show();

            handlerShowResults.postDelayed(() -> {

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

            }, DELAY_NEXT_MULTIPLICATION);

        }
    }

    public void checkResult(int table) {
        // Obtén el resultado ingresado por el usuario
        String userResultText = editTextResult.getText().toString().trim();

        // Compara el resultado ingresado con el resultado esperado
        if (!userResultText.isEmpty()) {
            int userResult = Integer.parseInt(userResultText);

            // Ajusta el cálculo del resultado esperado en función de la dificultad
            switch (MainActivity.getSelectedDifficulty()) {
                case 1: // Medio (descendente)
                    expectedMultiplier = 10 - currentMultiplier;
                    break;
                case 2: // Difícil (aleatorio)
                    expectedMultiplier = randomOrder.get(currentMultiplier);
                    break;
                default: // Fácil (ascendente)
                    expectedMultiplier = currentMultiplier;
                    break;
            }

            int expectedResult = table * expectedMultiplier;

            if (userResult == expectedResult) {
                // El resultado es correcto
                showCorrectMultiplication(table, expectedMultiplier);

                // Después de un breve retardo, limpia los colores y muestra la próxima multiplicación
                handlerShowResults.postDelayed(() -> {
                    clearResultFields();
                    // Muestra la siguiente multiplicación
                    currentMultiplier++;
                    showNextMultiplication(table);

                    // Muestra la siguiente imagen del avatar
                    showNextAvatarImage();

                }, DELAY_NEXT_MULTIPLICATION);
            } else {
                // El resultado no es correcto
                editTextTable.setTextColor(Color.RED);
                editTextResult.setTextColor(Color.RED);
                showCorrectResult(table, expectedResult);

                // Después de un breve retardo, limpia los colores y muestra la próxima multiplicación
                handlerShowResults.postDelayed(() -> {
                    clearResultFields();
                    // Muestra la siguiente multiplicación
                    currentMultiplier++;
                    showNextMultiplication(table);

                }, DELAY_NEXT_MULTIPLICATION);
            }
        } else {
            // Si el campo del resultado está vacío, le pide que introduzca un resultado
            Toast.makeText(getContext(), "Introduce un resultado", Toast.LENGTH_LONG).show();
        }
    }

    private void showCorrectMultiplication(int table, int multiplier) {
        // Muestra la multiplicación correcta en verde
        String correctMultiplicationText = table + " X " + multiplier + " = ";
        editTextTable.setTextColor(Color.GREEN);
        editTextTable.setText(correctMultiplicationText);

        editTextResult.setTextColor(Color.GREEN);
    }
    private void showCorrectResult(int table, int expectedResult) {
        // Muestra el resultado correcto en verde
        String textResultExpected = table + " X " + expectedMultiplier + " = " + expectedResult;
        editTextResultExpected.setTextColor(Color.GREEN);
        editTextResultExpected.setText(textResultExpected);
    }
    private void clearResultFields() {
        // Después de un retardo, limpia los colores y los campos de resultado
        editTextTable.setTextColor(Color.BLACK);
        editTextResult.setTextColor(Color.BLACK);
        editTextResult.setText("");
        editTextResultExpected.setText("");
    }

    // Botonera para introducir los resultados de las multiplicaciones
    @SuppressLint({"SetTextI18n", "ResourceType"})
    public void addButtons() {
        GridLayout buttonsGrid = binding.gridButtons;
        Button button;

        for (int i = 9; i >= -2; i--) {
            button = new Button(getContext());

            // Establecer los parámetros de diseño del botón
            int buttonSizeInDp = 60; // Tamaño deseado en dp
            int buttonSizeInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, buttonSizeInDp, getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(buttonSizeInPx, buttonSizeInPx);
            button.setLayoutParams(params);

            button.setId(View.generateViewId()); // Genera un ID único para el botón
            button.setTextSize(16); // Modifica el tamaño de los números

            if (i == -1) {
                button.setBackgroundResource(R.drawable.icon_backspace_24);
                button.setTag("backspace"); // Asigna una etiqueta única al botón de retroceso
            } else if (i == -2) {
                button.setBackgroundResource(R.drawable.icon_check_24);
                button.setTag("check"); // Asigna una etiqueta única al botón de check
            } else {
                button.setText("" + (i)); // Establece el texto del botón como el número actual (i + 1)
            }

            button.setTextColor(Color.BLACK);
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F7DC6F")));
            button.setOnClickListener(this::onClickTableNumber); // Asocia un OnClickListener para manejar los clics en el botón
            buttonsGrid.addView(button); // Agrega el botón al GridLayout
        }
    }

    @SuppressLint("ResourceType")
    public void onClickTableNumber(View view) {
        EditText editTextResult = binding.edtResult;
        if (view instanceof Button) {
            Button button = (Button) view;// Si es un botón, conviértelo a un objeto Button

            if (tableSelected != null && !tableSelected.isEmpty()) {
                if ("backspace".equals(button.getTag())) {
                    Editable editable = editTextResult.getText(); //Crea un objeto Editable que contiene el texto editable del EditText
                    if (editTextResult.length() > 0) {
                        editable.delete(editTextResult.length() - 1, editTextResult.length());
                    }
                } else if ("check".equals(button.getTag())) {
                    checkResult(Integer.parseInt(MainActivity.getTableSelect()));
                } else {
                    editTextResult.append(button.getText());
                }
            }
        }
    }
}