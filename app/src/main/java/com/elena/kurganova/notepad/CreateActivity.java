package com.elena.kurganova.notepad;

/**
 * @author Elena Kurganova
 * @version 1.0
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    //Defining views
    private EditText editTextName;
    private EditText editTextDescription;
    private Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //Initializing views
        editTextName = findViewById(R.id.new_note_title);
        editTextDescription = findViewById(R.id.new_note_description);

        //Initializing a button
        buttonCreate = findViewById(R.id.create_button);
        //Setting listener to the button
        buttonCreate.setOnClickListener(this);
    }

     /**
     * Method to add a note
     */
    private void addNote() {
        final String title = editTextName.getText().toString().trim();
        final String description = editTextDescription.getText().toString().trim();

        //AsyncTask class enables proper use of the UI thread
        class AddTask extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CreateActivity.this, "Creating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(CreateActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(ServerActivity.KEY_NAME, title);
                params.put(ServerActivity.KEY_DESCRIPTION, description);

                RequestHandler rh = new RequestHandler();
                String res = null;
                try {
                    res = rh.sendPostRequest(ServerActivity.URL_CREATE, params);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return res;
            }
        }
        AddTask ae = new AddTask();
        ae.execute();
    }

    //

    /**
     * Method to run creation of a new note, adds note to the list
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == buttonCreate) {
            addNote();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}