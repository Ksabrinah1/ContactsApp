package com.example.mycontactsmidtermrodriguez;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class addContact extends AppCompatActivity {
    private ContactDataAccessObject contactDataAccessObject;

    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private Button buttonSave;
    private Button buttonBackToMain;

//    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        contactDataAccessObject = new ContactDataAccessObject(this);
        contactDataAccessObject.open();

        /*find buttons by id*/
        final EditText editTextName = findViewById(R.id.editTextName);
        final EditText editTextPhone = findViewById(R.id.editTextPhone);
        final EditText editTextEmail = findViewById(R.id.editTextEmail);
        Button buttonSave = findViewById(R.id.buttonUpdateContact);
        buttonBackToMain = findViewById(R.id.buttonBackToMain);

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                contactDataAccessObject.addContact(name, phone, email);
                Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close activity after saving
            }
        });
        //Back to main button
        buttonBackToMain.setOnClickListener(v -> finish());
    }

    //This method will close the activity and free memory
    @Override
    protected void onDestroy() {
        contactDataAccessObject.close();
        super.onDestroy();
    }
}
