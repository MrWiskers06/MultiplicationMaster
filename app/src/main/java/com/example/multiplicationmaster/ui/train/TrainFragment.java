package com.example.multiplicationmaster.ui.train;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.multiplicationmaster.MainActivity;
import com.example.multiplicationmaster.R;
import com.example.multiplicationmaster.databinding.FragmentTrainBinding;

import java.util.ArrayList;
import java.util.Collections;

public class TrainFragment extends Fragment {

    private FragmentTrainBinding binding;
    private Handler handlerShowResults = new Handler();
    private static final long DELAY_NEXT_MULTIPLICATION = 2000;
    private GridLayout buttonsGrid;
    private Button lastSelectedButton = null; // Almacena el último botón de tabla de multiplicar seleccionado
    private String tableSelected;
    private ArrayList<Integer> randomOrder;
    private TextView textViewUserResult;
    private TextView textViewCurrentMultiplication;
    private TextView textViewResultExpected;
    private int currentMultiplier;
    private int successCounter = 0;
    private int currentImageIndex = 0;
    private ProgressBar progressBar;
    private ArrayList<String> tablesSelected;
    private ArrayList<String []> mistakes;
    private String [] mistakesCurrentTable = new String[10];
    private int mistakesCounter = 0;
    private ArrayList<String> percentegesSuccess;
    private LottieAnimationView animationView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño y obtener la vista raíz
        binding = FragmentTrainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tableSelected = MainActivity.getTableSelected(); //Recupera la tabla de multplicar seleccionada
        tablesSelected = MainActivity.getTablesCompleted(); //Recupera la variable para las tablas de multiplicar seleccionadas para una fecha
        mistakes = MainActivity.getMistakes(); //Recupera la variable para los errores cometidos
        percentegesSuccess = MainActivity.getPercentegesSuccess(); //Recupera la variable para los porcentajes de aciertos

        // Configurar las vistas
        textViewCurrentMultiplication = binding.txvMultiplication;
        textViewUserResult = binding.txvResult;
        textViewResultExpected = binding.txvResultOK;
        progressBar = binding.pgbMultiplication;
        animationView = binding.animationStar;

        // Añade los botones de las tablas de multiplicar para los resultados
        addButtons();
        // Configura el orden aleatorio en caso de la dificultad dificil
        randomOrder = generateRandomOrder();
        // Al iniciar el fragmento añade la tabla de multiplicar seleccionada por el usuario en la configuracion
        addTableMultiplication();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Método para mostrar la siguiente imagen del avatar
    private void showNextAvatarImage() {
        ImageView imgAvatar = binding.imgAvatar;
        String avatarName = MainActivity.getAvatarSelected();
        int[] avatarProgressImages = MainActivity.getAvatarProgressImages(avatarName);

        if (avatarProgressImages != null && currentMultiplier >= 0) {
            // Verifica si currentImageIndex es un número par, para mostrar la siguiente imagen del avatar cada dos aciertos.
            if (successCounter % 2 == 0) {
                // Verifica si el índice calculado está dentro del rango del array avatarProgressImages.
                if (currentImageIndex >= 0 && currentImageIndex < avatarProgressImages.length) {
                    // Establece la imagen en imgAvatar usando el índice calculado de avatarProgressImages.
                    imgAvatar.setImageResource(avatarProgressImages[currentImageIndex]);
                    currentImageIndex++;
                }
            }
        }
    }

    private ArrayList<Integer> generateRandomOrder() {
        ArrayList<Integer> orderDifficultLevel = new ArrayList<>();

        for (int i = 0; i <= 10; i++) {
            orderDifficultLevel.add(i);
        }

        Collections.shuffle(orderDifficultLevel);
        return orderDifficultLevel;
    }

    // Método para calcular el multiplicador esperado según la dificultad
    private int calculateExpectedMultiplier() {

        switch (MainActivity.getDifficultySelected()) {
            case 1: // Medio (descendente)
                return 11 - currentMultiplier;
            case 2: // Difícil (aleatorio)
                return randomOrder.get(currentMultiplier-1);
            default: // Fácil (ascendente)
                return currentMultiplier;
        }
    }

    @SuppressLint("SetTextI18n")
    public void addTableMultiplication() {
        currentMultiplier = 1;  // Inicializa el multiplicador actual en 1

        if (tableSelected != null && !tableSelected.isEmpty()) {
            int table = Integer.parseInt(tableSelected);
            // Limpiar campos de texto
            clearResultFields();
            showNextMultiplication(table);
        } else {
            // Si el usuario no ha seleccionado ninguna tabla
            Toast.makeText(getContext(), "Por favor, selecciona una tabla en la configuración", Toast.LENGTH_LONG).show();
            // Puedes redirigir al usuario a la pantalla de configuración o realizar alguna otra acción.
        }
    }

    // Método para mostrar la siguiente multiplicación en la interfaz de usuario
    private void showNextMultiplication(int table) {
        if (currentMultiplier <= 10) {
            // Calcular el multiplicador esperado en funcion de la difciultad seleccionada
            int multiplier = calculateExpectedMultiplier();

            String multiplicationText = table + " X " + multiplier + " = ";
            textViewCurrentMultiplication.setText(multiplicationText);
        }else{
            // Cuando se completa la tabla de multiplicar, oculta los campos de texto y la botonera
            textViewCurrentMultiplication.setVisibility(View.INVISIBLE);
            textViewUserResult.setVisibility(View.INVISIBLE);
            textViewResultExpected.setVisibility(View.INVISIBLE);
            buttonsGrid.setVisibility(View.INVISIBLE);

            // Muestra la animación de la estrella cuando se completa la tabla
            animationView.setVisibility(View.VISIBLE);
            animationView.playAnimation();

            // Guarda la tabla de multiplicar practicada
            tablesSelected.add("Tabla del " + tableSelected + " - " + MainActivity.getAvatarSelected());

            // Guarda los errores cometidos en la tabla actual para mostrarlos en las estadísticas
            mistakes.add(mistakesCurrentTable);

            // Guarda el porcentaje de aciertos
            String percentageSuccess = String.valueOf((successCounter * 100) / 10);
            percentegesSuccess.add(percentageSuccess);

            // Guarda el avatar en caso de que se haya completado la tabla sin errores
            if (mistakesCounter == 0){
                String avatarSelected = MainActivity.getAvatarSelected();
                MainActivity.addAvatarCompleted(avatarSelected);
            }

            // Toast.makeText(getContext(), "Felicidades, has finalizado la tabla del " + table, Toast.LENGTH_LONG).show();
            textViewCurrentMultiplication.setText("");
        }
    }

    // Método para verificar y manejar la respuesta del usuario
    @SuppressLint("SetTextI18n")
    public void checkResult(int table) {
        String userResultText = textViewUserResult.getText().toString().trim();

        if (userResultText.isEmpty()) {
            // Si el campo del resultado está vacío, le pide que introduzca un resultado
            // Toast.makeText(getContext(), "Introduce un resultado", Toast.LENGTH_LONG).show();
            textViewUserResult.setError("Introduce un resultado");
            return;
        }

        int userResult = Integer.parseInt(userResultText);
        int multiplier = calculateExpectedMultiplier();

        int expectedResult = table * multiplier;

        // Actualiza el progreso de la barra
        progressBar.setMax(10);
        progressBar.setProgress(currentMultiplier);

        // Obtiene el TextView que muestra el porcentaje de prograso
        TextView textViewPercentageProgress = binding.txvPercentageProgress;
        textViewPercentageProgress.setText((currentMultiplier*10) + " %");

        // Comparar el resultado del usuario con el resultado esperado y manejar la respuesta
        if (userResult == expectedResult) {
            handleCorrectResult(table);
        } else {
            handleIncorrectResult(table, expectedResult, multiplier);
        }
    }

    // Método para manejar la respuesta correcta
    private void handleCorrectResult(int table) {
        textViewCurrentMultiplication.setTextColor(Color.GREEN);
        textViewUserResult.setTextColor(Color.GREEN);

        handlerShowResults.postDelayed(() -> {
            // Después de un breve retardo, limpiar los campos y mostrar la siguiente multiplicación
            clearResultFields();
            lastSelectedButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B0956B"))); // Restaura el color original del último botón
            currentMultiplier++;
            successCounter++;
            showNextAvatarImage();
            showNextMultiplication(table);
        }, DELAY_NEXT_MULTIPLICATION);
    }

    // Método para manejar la respuesta incorrecta
    private void handleIncorrectResult(int table, int expectedResult, int multiplier) {
        // Cambiar el color del texto para indicar una respuesta incorrecta
        textViewCurrentMultiplication.setTextColor(Color.RED);
        textViewUserResult.setTextColor(Color.RED);

        // Guarda el error cometido
        String mistake = textViewCurrentMultiplication.getText() + textViewUserResult.getText().toString().trim();
        mistakesCurrentTable[mistakesCounter] = mistake;
        mistakesCounter++;

        String textResultExpected = table + " X " + multiplier + " = " + expectedResult;
        textViewResultExpected.setTextColor(Color.GREEN);
        textViewResultExpected.setText(textResultExpected);

        handlerShowResults.postDelayed(() -> {
            clearResultFields();
            lastSelectedButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B0956B"))); // Restaura el color original del último botón
            currentMultiplier++;
            showNextMultiplication(table);
        }, DELAY_NEXT_MULTIPLICATION);
    }

    // Limpia los colores y los campos de resultado
    private void clearResultFields() {
        textViewCurrentMultiplication.setTextColor(Color.BLACK);
        textViewUserResult.setTextColor(Color.BLACK);
        textViewUserResult.setText("");
        textViewResultExpected.setText("");
    }

    // Botonera para introducir los resultados de las multiplicaciones
    @SuppressLint({"SetTextI18n", "ResourceType"})
    public void addButtons() {
        buttonsGrid = binding.gridButtons;
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
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B0956B")));
            button.setOnClickListener(this::onClickTableNumber); // Asocia un OnClickListener para manejar los clics en el botón
            buttonsGrid.addView(button); // Agrega el botón al GridLayout
        }
    }
    private void selectUnselectButton(Button button) {
        // Desmarca el último botón seleccionado y restaura su color original
        if (lastSelectedButton != null) {
            lastSelectedButton.setSelected(false); // Desmarca el último botón seleccionado
            lastSelectedButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B0956B"))); // Restaura el color original del último botón
        }

        // Selecciona el nuevo botón y establece su color
        button.setSelected(true); // Selecciona el nuevo botón
        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#73C6B6"))); // Establece el nuevo color del botón
        lastSelectedButton = button; // Actualiza la referencia al último botón seleccionado
    }
    @SuppressLint("ResourceType")
    public void onClickTableNumber(View view) {
        if (view instanceof Button) {
            Button button = (Button) view;// Si es un botón, conviértelo a un objeto Button

            if (tableSelected != null && !tableSelected.isEmpty() && currentMultiplier <=10) {
                if ("backspace".equals(button.getTag())) {
                    Editable editable = textViewUserResult.getEditableText(); //Crea un objeto Editable que contiene el texto editable del textViewUserResult
                    if (textViewUserResult.length() > 0) {
                        editable.delete(textViewUserResult.length() - 1, textViewUserResult.length());
                    }
                } else if ("check".equals(button.getTag())) {
                    checkResult(Integer.parseInt(tableSelected));
                } else {
                    selectUnselectButton(button);
                    textViewUserResult.append(button.getText());
                }
            }
        }
    }
}