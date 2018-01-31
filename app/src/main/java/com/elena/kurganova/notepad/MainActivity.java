package com.elena.kurganova.notepad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    //Defining views
    private ListView listView;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();
    }

    private void showNote() {
        JSONArray json;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            json = new JSONArray(JSON_STRING);
            for (int i = 0; i < json.length(); i++) {
                JSONObject e = json.getJSONObject(i);
                String title = e.getString("title");
                String id = e.getString("id");
                String description = e.getString("description");

                HashMap<String, String> note = new HashMap<>();
                note.put("title", title);
                note.put("description", description);
                note.put("id", id);
                list.add(note);
                Log.v("Notes: ", String.valueOf(note));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(MainActivity.this, list, R.layout.notes_layout,
                new String[]{ServerActivity.KEY_NAME, ServerActivity.KEY_DESCRIPTION, ServerActivity.KEY_ID},
                new int[]{R.id.value_note_title, R.id.value_note_description, R.id.value_note_id});
        listView.setAdapter(adapter);
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Fetching Data", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showNote();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                return rh.sendGetRequest(ServerActivity.URL_GET_ALL);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        Intent intent = new Intent(this, EditActivity.class);
        HashMap<String, String> map = (HashMap) adapter.getItemAtPosition(position);
        String NoteId = map.get(ServerActivity.KEY_ID);
        intent.putExtra(ServerActivity.ID, NoteId);
        startActivity(intent);
    }

    //Create note method
    public void createNote(View view) {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }
}
