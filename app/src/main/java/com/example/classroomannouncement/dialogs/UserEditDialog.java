package com.example.classroomannouncement.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.classroomannouncement.R;
import com.example.classroomannouncement.Database.Entities.User;

public class UserEditDialog extends DialogFragment {
    public interface UserEditListener {
        void onUserEdited(User user);
    }

    private UserEditListener listener;
    private User user;
    private boolean isNewUser;

    public UserEditDialog(User user, UserEditListener listener) {
        this.user = user;
        this.listener = listener;
        this.isNewUser = (user == null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_user_edit, null);

        EditText etName = view.findViewById(R.id.et_name);
        EditText etEmail = view.findViewById(R.id.et_email);
        EditText etPassword = view.findViewById(R.id.et_password);

        if (!isNewUser) {
            etName.setText(user.getName());
            etEmail.setText(user.getEmail());
            etPassword.setText(user.getPassword());
        }

        builder.setView(view)
                .setTitle(isNewUser ? "Add New User" : "Edit User")
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();

                    User updatedUser;
                    if (isNewUser) {
                        updatedUser = new User(name, email, password, false); // Uses @Ignore constructor
                    } else {
                        updatedUser = new User(
                                user.getId(), // Preserve ID
                                name,
                                email,
                                password,
                                user.isAdmin() // Preserve admin status
                        );
                    }

                    if (listener != null) {
                        listener.onUserEdited(updatedUser);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dismiss());

        return builder.create();
    }
}