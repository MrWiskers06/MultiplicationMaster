package com.example.multiplicationmaster.ui.settings;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.multiplicationmaster.R;
import com.example.multiplicationmaster.databinding.FragmentSettingsBinding;
import com.example.multiplicationmaster.dialogs.DateDialog;
import com.example.multiplicationmaster.dialogs.DateDialogListener;
import com.example.multiplicationmaster.dialogs.DifficultyDialog;
import com.example.multiplicationmaster.dialogs.DifficultyDialogListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener, DifficultyDialogListener, DateDialogListener {

    private FragmentSettingsBinding binding; // Clase autogenerada que representa el binding con el layout del fragmento
    private static String[] avatars = {"Itachi", "Hinata", "Sasuke", "Kakashi", "Naruto"};
    private static int avatarImages[] = {R.drawable.itachi_9, R.drawable.hinnata_9, R.drawable.sasuke_9, R.drawable.kakashi_9, R.drawable.naruto_9};
    private Button lastSelectedButton = null; // Almacena el último botón de tabla de multiplicar seleccionado
    private Button btnSelectDifficulty = null; // Botón para seleccionar dificultad
    private DifficultyDialog difficultyDialog; // Diálogo para la selección de dificultad
    private int selectedDifficulty = 0; // Nivel de dificultad seleccionado
    private DateDialog dateDialog; // Diálogo para la selección de fecha
    private EditText selectDate; // EditText para mostrar la fecha seleccionada

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflar el diseño y obtener la vista raíz
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar el Spinner
        Spinner selectAvatar = binding.spinnerAvatar;
        CustomSpinnerAdapter personalSpinner = new CustomSpinnerAdapter(SettingsFragment.super.getContext(), R.layout.lines_spinner, avatars, avatarImages);
        selectAvatar.setAdapter(personalSpinner);
        selectAvatar.setOnItemSelectedListener(this);

        // Añade los botones de las tablas de multiplicar
        addButtons();

        // Configurar el OnClickListener del botón dificultad
        btnSelectDifficulty = binding.btnSelectDifficulty;
        btnSelectDifficulty.setOnClickListener(this::onClickDifficulty);

        // Configurar el OnClickListener del EditText para la fecha
        selectDate = binding.edtFecha;
        selectDate.setText(R.string.select_date); // Establece un texto visual inicial en el EditText
        selectDate.setInputType(InputType.TYPE_NULL); // Evita que aparezca el teclado
        selectDate.setOnClickListener(this::onClickDate); // Agrega el onClick con una expresión lambda
        selectDate.setFocusable(false); // Hace que el EditText no sea enfocable, útil para evitar doble clic ya que el teclado no se mostrará

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedAvatar = avatars[position]; // Obtiene el nombre del avatar seleccionado
        int selectedAvatarImage = avatarImages[position]; // Obtiene el recurso de la imagen del avatar seleccionado

        // Hacer lo necesario con el avatar
        // Puedes realizar acciones adicionales al seleccionar un avatar
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Lógica cuando no se selecciona ningún elemento en el Spinner
        // Puedes implementar lógica adicional para manejar este caso
    }

    @SuppressLint("SetTextI18n")
    public void addButtons() {
        GridLayout buttonsGrid = binding.gridButtons;
        Button btn;

        for (int i = 0; i < 10; i++) {
            btn = new Button(getContext());

            // Establecer los parámetros de diseño del botón
            int buttonSizeInDp = 50; // Tamaño deseado en dp
            int buttonSizeInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, buttonSizeInDp, getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(buttonSizeInPx, buttonSizeInPx);
            btn.setLayoutParams(params);

            btn.setId(View.generateViewId()); // Genera un ID único para el botón
            btn.setTextSize(16); // Modifica el tamaño de los números

            if (i == 9) {
                btn.setText("?");
            } else {
                btn.setText("" + (i + 1)); // Establece el texto del botón como el número actual (i + 1)
            }

            btn.setTextColor(Color.BLACK);
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F7DC6F")));
            btn.setOnClickListener(this::onClickTableNumber); // Asocia un OnClickListener para manejar los clics en el botón
            buttonsGrid.addView(btn, i); // Agrega el botón al GridLayout
        }
    }

    private void select_unselectButton(Button button) {
        // Desmarca el último botón seleccionado y restaura su color original
        if (lastSelectedButton != null) {
            lastSelectedButton.setSelected(false); // Desmarca el último botón seleccionado
            lastSelectedButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F7DC6F"))); // Restaura el color original del último botón
        }

        // Selecciona el nuevo botón y establece su color
        button.setSelected(true); // Selecciona el nuevo botón
        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#73C6B6"))); // Establece el nuevo color del botón
        lastSelectedButton = button; // Actualiza la referencia al último botón seleccionado

        Toast.makeText(getContext(), "Has seleccionado la tabla del " + button.getText(), Toast.LENGTH_SHORT).show();
    }

    public void onClickTableNumber(View view) {
        if (view instanceof Button) {
            Button button = (Button) view;// Si es un botón, conviértelo a un objeto Button

            // Desmarca el último botón seleccionado y restaura su color original
            select_unselectButton(button);

            button.getText(); //Lo utilizaremos para recuperar la tabla seleccionada, falta aplicar método que recupera el número seleccionado para el Fragment !A entrenar¡
        }
    }

    public void onClickDifficulty(View view) {
        difficultyDialog = new DifficultyDialog(selectedDifficulty);
        difficultyDialog.setDifficultyListener(this); // Establece el contexto de este fragmento para el listener del diálogo
        difficultyDialog.show(getActivity().getSupportFragmentManager(), "DifficultyDialog");
    }

    @Override
    public void onChangeDifficulty(int level) {
        selectedDifficulty = level;
        Toast.makeText(getContext(), "Se ha seleccionado el nivel " + level, Toast.LENGTH_SHORT).show();

        // Puedes realizar acciones adicionales al cambiar la dificultad, falta aplicar método que recupera la dificultad seleccionada para el Fragment !A entrenar¡
    }

    public void onClickDate(View view) {
        dateDialog = new DateDialog();
        dateDialog.setDateListener(this);
        dateDialog.show(getActivity().getSupportFragmentManager(), "DateDialog");
    }

    @Override
    public void onSelectedDate(GregorianCalendar date) {
        selectDate.setText(date.get(Calendar.DAY_OF_MONTH) + "/"
                + (date.get(Calendar.MONTH) + 1) + "/" // Se suma 1 porque en Calendar, enero es 0
                + date.get(Calendar.YEAR));

        selectDate.getText(); // Puedes realizar acciones adicionales al seleccionar la fecha, falta aplicar método que recupera la fecha seleccionada para el Fragment Estadisticas
    }
}
