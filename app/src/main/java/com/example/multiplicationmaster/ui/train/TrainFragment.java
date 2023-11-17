package com.example.multiplicationmaster.ui.train;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.multiplicationmaster.MainActivity;
import com.example.multiplicationmaster.R;
import com.example.multiplicationmaster.databinding.FragmentTrainBinding;

public class TrainFragment extends Fragment {

    private FragmentTrainBinding binding;
    private EditText editTextResult;
    private EditText editTextTable;
    private int currentMultiplier;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño y obtener la vista raíz
        binding = FragmentTrainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editTextTable= binding.edtMultiplication;
        editTextTable.setInputType(InputType.TYPE_NULL); // Evita que aparezca el teclado
        editTextTable.setFocusable(false); // Hace que el EditText no sea enfocable, útil para evitar doble clic ya que el teclado no se mostrará

        editTextResult= binding.edtResult;
        editTextResult.setInputType(InputType.TYPE_NULL); // Evita que aparezca el teclado
        editTextResult.setFocusable(false); // Hace que el EditText no sea enfocable, útil para evitar doble clic ya que el teclado no se mostrará

        // Añade los botones de las tablas de multiplicar para los resultados
        addButtons();
        // Mosifica la imagen del avatar en funcion del avatar seleccionado en la configuracion
        setAvatarSelected();
        // Añade la tabla de multiplicar seleccionada por el usuario en la configuracion
        addTableMultiplication();

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Modifica el avatar seleccionado
    public void setAvatarSelected(){
        ImageView imgAvatar = binding.imgAvatar;
        imgAvatar.setImageResource(MainActivity.getAvatarImgSelected());
    }

    @SuppressLint("SetTextI18n")
    public void addTableMultiplication() {
        int table = Integer.parseInt(MainActivity.getTableSelect());
        currentMultiplier = 0;  // Multiplicador actual
        showNextMultiplication(table);
    }
    private void showNextMultiplication(int table) {
        if (currentMultiplier <= 10) {
            String multiplicationText = table + " X " + currentMultiplier + " = ";

            editTextTable.setText(multiplicationText);
        }
    }
    public void checkResult(int table){
        // Obtén el resultado ingresado por el usuario
        String userResult = editTextResult.getText().toString().trim();

        // Compara el resultado ingresado con el resultado esperado
        if (!userResult.isEmpty()) {
            int result = Integer.parseInt(userResult);
            int expectedResult = table * currentMultiplier;

            if (result == expectedResult) {
                // El resultado es correcto, puedes manejar esta situación según tus necesidades
                Toast.makeText(getContext(), "RESULT OK", Toast.LENGTH_SHORT).show();
                editTextResult.setText("");
                // Muestra la siguiente multiplicación
                currentMultiplier++;
                showNextMultiplication(Integer.parseInt(MainActivity.getTableSelect()));
            } else {
                // El resultado no es correcto, puedes manejar esta situación según tus necesidades
                Toast.makeText(getContext(), "RESULT FAIL", Toast.LENGTH_SHORT).show();
            }
        }else{
            // Si el campo del resultado esta vacio le pide que introduzca un resultado
            Toast.makeText(getContext(), "Introduce un rasultado", Toast.LENGTH_LONG).show();
        }
    }

    // Botonera para introducir los resultados de las multiplicaciones
    @SuppressLint({"SetTextI18n", "ResourceType"})
    public void addButtons() {
        GridLayout buttonsGrid = binding.gridButtons;
        Button button;

        for (int i = 0; i <= 11; i++) {
            button = new Button(getContext());

            // Establecer los parámetros de diseño del botón
            int buttonSizeInDp = 60; // Tamaño deseado en dp
            int buttonSizeInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, buttonSizeInDp, getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(buttonSizeInPx, buttonSizeInPx);
            button.setLayoutParams(params);

            button.setId(View.generateViewId()); // Genera un ID único para el botón
            button.setTextSize(16); // Modifica el tamaño de los números

            if (i == 10) {
                button.setBackgroundResource(R.drawable.icon_backspace_24);
                button.setTag("backspace"); // Asigna una etiqueta única al botón de retroceso
            }else if (i == 11){
                button.setBackgroundResource(R.drawable.icon_check_24);
                button.setTag("check"); // Asigna una etiqueta única al botón de check
            } else {
                button.setText("" + (i)); // Establece el texto del botón como el número actual (i + 1)
            }

            button.setTextColor(Color.BLACK);
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F7DC6F")));
            button.setOnClickListener(this::onClickTableNumber); // Asocia un OnClickListener para manejar los clics en el botón
            buttonsGrid.addView(button, i); // Agrega el botón al GridLayout
        }
    }
    @SuppressLint("ResourceType")
    public void onClickTableNumber(View view) {
        EditText editTextResult = binding.edtResult;
        if (view instanceof Button) {
            Button button = (Button) view;// Si es un botón, conviértelo a un objeto Button

            if("backspace".equals(button.getTag())){
                Editable editable = editTextResult.getText(); //Crea un objeto Editable que contiene el texto editable del EditText
                if (editTextResult.length() > 0) {
                    editable.delete(editTextResult.length() - 1, editTextResult.length() );
                }
            }else if("check".equals(button.getTag())){
                checkResult(Integer.parseInt(MainActivity.getTableSelect()));
            }else{
                editTextResult.append(button.getText());
            }
        }
    }
}