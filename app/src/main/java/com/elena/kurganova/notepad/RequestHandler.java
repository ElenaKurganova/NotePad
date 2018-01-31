package com.elena.kurganova.notepad;

/**
 * Created by Elena on 28-Jan-18.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


class RequestHandler {

    //Method to send http GET Request
    public String sendGetRequest(String requestURL) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s).append("\n");
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }

    //Method to send http GET Request detailed
    public String sendGetRequestParam(String requestURL, String id) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s).append("\n");
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }

    //Method to send http PUT Request
    //Method is taking three arguments: the URL to send the request
    //a HashMap with name value pairs containing the data to be send with the request and note id
    public String sendPutRequest(String requestURL, HashMap<String, String> putDataParams, String id) throws IOException, JSONException {
        URL url = new URL(requestURL + id);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("Content-Type", "application/json");
        OutputStreamWriter out = new OutputStreamWriter(
                httpCon.getOutputStream());
        out.write(getPutDataString(putDataParams));
        out.close();
        int code = httpCon.getResponseCode();
        return (String.valueOf(getPutDataString(putDataParams)));
    }

    private String getPutDataString(HashMap<String, String> params) throws JSONException {
        String json;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", params.get("title"));
        jsonObject.put("description", params.get("description"));
        json = jsonObject.toString();
        return json;
    }

    //Method to send http DELETE Request
    public String sendDeleteRequest(String requestURL, String id) throws Exception {
        URL url = new URL(requestURL + id);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestProperty("Content-Type", "application/json");
        httpCon.setRequestMethod("DELETE");
        httpCon.connect();
        int code = httpCon.getResponseCode();
        return ("Response code: " + String.valueOf(code));
    }

    //Method to send http POST Request
    //Method is taking two arguments: the URL of the script to send the request
    //and HashMap with name value pairs containing the data to be send with the request
    public String sendPostRequest(String requestURL,
                                  HashMap<String, String> postDataParams) throws IOException, JSONException {
        URL url = new URL(requestURL);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("POST");
        httpCon.setRequestProperty("Content-Type", "application/json");
        OutputStreamWriter out = new OutputStreamWriter(
                httpCon.getOutputStream());
        out.write(getPostDataString(postDataParams));
        out.close();
        int code = httpCon.getResponseCode();
        return (String.valueOf(getPutDataString(postDataParams)));
    }

    private String getPostDataString(HashMap<String, String> params) throws JSONException {
        String json;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", params.get("title"));
        jsonObject.put("description", params.get("description"));
        json = jsonObject.toString();
        return json;
    }
}

















