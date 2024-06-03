package com.example.mycontactsmidtermrodriguez;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

//The function of this class is to mananage the UI and functionality for displaying a list of contacts
//It also connects to a database and retrieve contacts

public class ContactListActivity extends AppCompatActivity {

    private ContactDataAccessObject contactDataAccessObject;
    private ListView listViewContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        contactDataAccessObject = new ContactDataAccessObject(this);
        contactDataAccessObject.open();

        listViewContacts = findViewById(R.id.listViewContacts);
        loadContacts();
    }

    //This method allows users to edit contacts by clicking on them
    private void loadContacts() {
        List<Contact> contacts = contactDataAccessObject.getAllContacts();
        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, contacts);
        listViewContacts.setAdapter(adapter);

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = (Contact) listViewContacts.getItemAtPosition(position);
                Intent intent = new Intent(ContactListActivity.this, EditContactActivity.class);
                intent.putExtra("CONTACT_ID", contact.getId());
                startActivity(intent);
            }
        });
    }

    //This method refreshes the contact list upon resuming the activity to reflect any changes made.
    @Override
    protected void onResume() {
        super.onResume();
        loadContacts(); // Refresh list when returning from edit
    }

    //This method ensures proper resource mananagement by closing the database connection when the activity is destroyed.
    @Override
    protected void onDestroy() {
        contactDataAccessObject.close();
        super.onDestroy();
    }
}