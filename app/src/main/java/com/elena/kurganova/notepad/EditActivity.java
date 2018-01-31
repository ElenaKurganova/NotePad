package com.elena.kurganova.notepad;

/**
 * @author Elena Kurganova
 * @version 1.0
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;


public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextName;
    private EditText editTextDescription;
    private Button buttonUpdate;
    private Button buttonDelete;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        id = getIntent().getExtras().getString(ServerActivity.ID);

        editTextName = findViewById(R.id.edited_title);
        editTextDescription = findViewById(R.id.edited_description);

        buttonUpdate = findViewById(R.id.button_save);
        buttonDelete = findViewById(R.id.button_delete);
        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        getNote();
    }

    /**
     * Method to get a note
     */
    private void getNote() {
        //AsyncTask class enables proper use of the UI thread
        class GetNote extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditActivity.this, "Fetching...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showNote(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                return rh.sendGetRequestParam(ServerActivity.URL_GET_DETAIL, id);
            }
        }
        GetNote gn = new GetNote();
        gn.execute();
    }

    /**
     * Method to display a note
     * @param json
     */
    private void showNote(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String title = jsonObject.getString("title");
            String description = jsonObject.getString("description");

            editTextName.setText(title);
            editTextDescription.setText(description);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to edit note
     */

    private void updateNote() {
        final String title = editTextName.getText().toString().trim();
        final String description = editTextDescription.getText().toString().trim();

        class UpdateNote extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditActivity.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(EditActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(ServerActivity.KEY_ID, id);
                hashMap.put(ServerActivity.KEY_NAME, title);
                hashMap.put(ServerActivity.KEY_DESCRIPTION, description);

                RequestHandler rh;
                rh = new RequestHandler();
                String s = null;
                try {
                    s = rh.sendPutRequest(ServerActivity.URL_UPDATE, hashMap, id);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                return s;
            }
        }
        UpdateNote un = new UpdateNote();
        un.execute();
    }

    /**
     * Method to delete note
     */
    private void deleteNote() {
        class DeleteNote extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditActivity.this, "Deleting...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(EditActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = null;
                try {
                    s = rh.sendDeleteRequest(ServerActivity.URL_DELETE, id);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return s;
            }
        }

        DeleteNote dn = new DeleteNote();
        dn.execute();
    }

    //AlertDialog to confirm note deletion with the user
    private void confirmDeleteNote() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this note?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteNote();
                        startActivity(new Intent(EditActivity.this, MainActivity.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonUpdate) {
            updateNote();
            startActivity(new Intent(this, MainActivity.class));
        }
        if (v == buttonDelete) {
            confirmDeleteNote();
        }
    }
}
