package com.example.classroomannouncement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.classroomannouncement.Database.Entities.Announcement;
import com.example.classroomannouncement.viewmodels.AnnouncementViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreateAnnouncementPage extends AppCompatActivity {

    private TextInputEditText titleInput;
    private TextInputEditText bodyInput;
    private TextInputEditText expiryDateInput;
    private TextInputLayout expiryDateLayout;
    private Button postButton;
    private AnnouncementViewModel announcementViewModel;
    private Date expiryDate;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement_page);

        initializeViewModel();
        initializeViews();
        setupDefaultExpiryDate();
        setupDatePicker();
        setupPostButton();
    }

    private void initializeViewModel() {
        announcementViewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);
    }

    private void initializeViews() {
        titleInput = findViewById(R.id.titleInput);
        bodyInput = findViewById(R.id.bodyInput);
        expiryDateInput = findViewById(R.id.expiryDateInput);
        expiryDateLayout = findViewById(R.id.expiryDateLayout);
        postButton = findViewById(R.id.postButton);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void setupDefaultExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        setExpiryDate(calendar.getTime());
    }

    private void setupDatePicker() {
        expiryDateLayout.setEndIconOnClickListener(v -> showDatePicker());
        expiryDateInput.setOnClickListener(v -> showDatePicker());
        expiryDateInput.setKeyListener(null); // Prevent keyboard from showing
    }

    private void setupPostButton() {
        postButton.setOnClickListener(this::validateAndPostAnnouncement);
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Expiry Date")
                .setSelection(expiryDate != null ? expiryDate.getTime() : MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);
            setExpiryDate(calendar.getTime());
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void setExpiryDate(Date date) {
        this.expiryDate = date;
        expiryDateInput.setText(dateFormat.format(date));
        expiryDateLayout.setError(null); // Clear any previous errors
    }

    private void validateAndPostAnnouncement(View view) {
        String title = Objects.requireNonNull(titleInput.getText()).toString().trim();
        String content = Objects.requireNonNull(bodyInput.getText()).toString().trim();

        if (!validateInputs(title, content)) {
            return;
        }

        postAnnouncement(title, content);
    }

    private boolean validateInputs(String title, String content) {
        boolean isValid = true;

        if (title.isEmpty()) {
            titleInput.setError("Title is required");
            titleInput.requestFocus();
            isValid = false;
        } else {
            titleInput.setError(null);
        }

        if (content.isEmpty()) {
            bodyInput.setError("Content is required");
            bodyInput.requestFocus();
            isValid = false;
        } else {
            bodyInput.setError(null);
        }

        if (expiryDate == null || expiryDate.before(new Date())) {
            expiryDateLayout.setError("Expiry date must be in the future");
            expiryDateInput.requestFocus();
            isValid = false;
        } else {
            expiryDateLayout.setError(null);
        }

        return isValid;
    }

    private void postAnnouncement(String title, String content) {
        try {
            Announcement announcement = new Announcement(title, content, expiryDate);
            announcementViewModel.insert(announcement);

            Toast.makeText(this, "Announcement posted successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error posting announcement: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}