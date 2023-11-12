package com.example.multiplicationmaster.ui.settings;

import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {
    private int selectedDifficulty = 0;

    public int getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public void setSelectedDifficulty(int difficulty) {
        selectedDifficulty = difficulty;
    }
}