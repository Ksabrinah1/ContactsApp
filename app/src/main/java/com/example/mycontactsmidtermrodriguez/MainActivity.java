package com.example.mycontactsmidtermrodriguez;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
/*This class is the central hub for the application and the primary user interface where users can
 * navigate to others parts of the app*/
/*It contains initialization and set up inflators*/

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button viewContactsButton = findViewById(R.id.viewContactsButton);
        Button buttonAddContact = findViewById(R.id.buttonAddContact);

        /*The following methods are event listeners for navigation*/
        /*The viewContacts triggers an intent to start contactListActivity, which displays a list of all contacts allowing the user
         * to select, view or edit an existing contact*/
        viewContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContactListActivity.class);
                startActivity(intent);
            }
        });
        /*This method has a click listener that launches an intent to start the activity add contact, that will add the contact to the database*/
        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addContact.class);
                startActivity(intent);
            }
        });
    }
}