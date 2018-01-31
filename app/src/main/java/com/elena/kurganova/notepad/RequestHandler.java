package com.elena.kurganova.notepad;

/**
 * @author Elena Kurganova
 * @version 1.0
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

    /**
     * Method to send http GET Request
     *
     * @param requestURL
     * @return
     */
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

    /**
     * Method to send http GET Request detailed
     *
     * @param requestURL
     * @param id
     * @return
     */
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

    /**
     * Method to send http PUT Request
     *
     * @param requestURL
     * @param putDataParams
     * @param id
     * @return
     * @throws IOException
     * @throws JSONException
     */
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

    /**
     * @param params
     * @return
     * @throws JSONException
     */
    private String getPutDataString(HashMap<String, String> params) throws JSONException {
        String json;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", params.get("title"));
        jsonObject.put("description", params.get("description"));
        json = jsonObject.toString();
        return json;
    }

    /**
     * Method to send http DELETE Request
     *
     * @param requestURL
     * @param id
     * @return
     * @throws Exception
     */
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

    /**
     * Method to send http POST Request
     *
     * @param requestURL
     * @param postDataParams
     * @return
     * @throws IOException
     * @throws JSONException
     */
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

    /**
     * @param params
     * @return
     * @throws JSONException
     */
    private String getPostDataString(HashMap<String, String> params) throws JSONException {
        String json;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", params.get("title"));
        jsonObject.put("description", params.get("description"));
        json = jsonObject.toString();
        return json;
    }
}

















