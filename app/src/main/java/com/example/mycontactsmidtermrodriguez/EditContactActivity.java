package com.example.mycontactsmidtermrodriguez;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


/*This class is a component that handles both creating new contacts and updates or deletes existing ones.
It interacts heavily with the ContactDataAccessObject for all database operations ensuring data persistence is handled efficiently.
This activity also manages user interactions and provides feedback, making it an integral part of the application's user experience related to contact management.*/
public class EditContactActivity extends AppCompatActivity {
    private ContactDataAccessObject contactDataAccessObject;
    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextEmail;
    //private Button buttonSave;
    private Button buttonDelete;
    private Button buttonSaveOrUpdateContact;
    private Button buttonBackToMain;
    private TextView textViewTitle;
    private int contactId = -1; // This is a default value for new contact

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        //open DataAccessObject
        contactDataAccessObject = new ContactDataAccessObject(this);
        contactDataAccessObject.open();

        //Initialize the views
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        //buttonSave = findViewById(R.id.buttonSave);
        buttonSaveOrUpdateContact = findViewById(R.id.buttonSaveOrUpdateContact);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonBackToMain = findViewById(R.id.buttonBackToMain);
        textViewTitle = findViewById(R.id.textViewTitle);

        // Get the contact ID from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CONTACT_ID")) {
            contactId = intent.getIntExtra("CONTACT_ID", -1);
            // Load contact details if editing an existing contact
            loadContactDetails(contactId);
            buttonSaveOrUpdateContact.setText("Update");
            textViewTitle.setText("Update Contact");
        } else {
            buttonSaveOrUpdateContact.setText("Save");
            textViewTitle.setText("Add Contact");
        }

        // Set the click listener for the save/update button
        buttonSaveOrUpdateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });

        //set onClickListener for delete button
        // buttonSave.setOnClickListener(v -> onSaveClick());
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClick();
            }
        });

        //set onClickListener for back to main button
        buttonBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToMain();
            }
        });
    }

    private void loadContactDetails(int id) {
        Contact contact = contactDataAccessObject.getContactById(id);
        if (contact != null) {
            editTextName.setText(contact.getName());
            editTextPhone.setText(contact.getPhone());
            editTextEmail.setText(contact.getEmail());
        }
    }

    //    this method will save contact in the db
    private void onSaveClick() {
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();
//  check if id doesn't exist then add new contact
        if (contactId == -1) {
            // Add a new contact
            contactDataAccessObject.addContact(name, phone, email);
        } else {
            // Update existing contact
            Contact contact = new Contact(contactId, name, phone, email);
            contact.setId(contactId); // Ensure the ID is set for updating
            contactDataAccessObject.updateContact();
        }

        finish(); // Go back to the previous activity
    }

    // Method to save or update contact
    private void updateContact() {
        // Get the updated contact information
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();

        // Validate the input
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please enter name, phone number and email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contactId == -1) {
            // Add a new contact
            contactDataAccessObject.addContact(name, phone, email);
            Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
        } else {
            // Update existing contact
//            Contact contact = new Contact(contactId, name, phone, email);
            boolean success = contactDataAccessObject.updateContact(contactId, name, phone, email);

            if (success) {
                Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update contact", Toast.LENGTH_SHORT).show();
            }
        }

        // Finish the activity to go back
        finish();
    }

    //ondelete event
    private void onDeleteClick() {
        if (contactId != -1) {
            contactDataAccessObject.deleteContact(contactId);
            // Show a confirmation message
            Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show();
            //go back to the previous activity
            finish();
        }
    }

    private void navigateBackToMain() {
        Intent intent = new Intent(EditContactActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        contactDataAccessObject.close();
        super.onDestroy();
    }
}