package com.example.codenames;

import static java.lang.Thread.sleep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.codenames.app.AppController;
import com.example.codenames.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LobbyActivity extends Activity implements View.OnClickListener
{
    private String TAG = LobbyActivity.class.getSimpleName();
//    private Button btnJsonObj, btnJsonArray;
//    private TextView msgResponse;
//    private ProgressDialog pDialog;
    private TextView player_count;
    private TextView lobby_name;
    private TextView user;
    private Button to_lobby;
    private String username;

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        //setting and saving username
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        user = (TextView) findViewById(R.id.lobby_username);
        user.setText(username);


        to_lobby = (Button) findViewById(R.id.reg_exit3);
        to_game = (Button) findViewById(R.id.button_ready);

        to_lobby.setOnClickListener(this);
        to_game.setOnClickListener(this);

        postJsonObj();
        try
        {
            sleep(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        showGameLobbyName();
        makeJsonObjReq();
    }

    private void showGameLobbyName()
    {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                Const.URL_JSON_GAMELOBBYNAME_GET, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            Log.d(TAG, response.getString("gameLobbyName"));
                            lobby_name = findViewById(R.id.text_header);
                            lobby_name.setText(response.getString("gameLobbyName")); //display string
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void postJsonObj()
    {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
                Const.URL_JSON_PLAYERNUM_POST, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            Log.d(TAG, response.getString("playerNum"));
                            player_count = findViewById(R.id.text_playercount);
                            player_count.setText(response.getString("playerNum")); //display string
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        })
        {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    /**
     * Making json object request
     * */
    private void makeJsonObjReq()
    {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                Const.URL_JSON_PLAYERNUM_GET, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            Log.d(TAG, response.getString("playerNum"));
                            player_count = findViewById(R.id.text_playercount);
                            player_count.setText(response.getString("playerNum")); //display string
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        })
        {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("name", "Androidhive");
//                params.put("email", "abc@androidhive.info");
//                params.put("pass", "password123");

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.reg_exit3) {
            startActivity(new Intent(LobbyActivity.this, HubActivity.class).putExtra("username", username));
        }

        if (view.getId() == R.id.button_ready)
        {
            startActivity(new Intent(LobbyActivity.this, SpymasterGameActivity.class));
        }
    }
}