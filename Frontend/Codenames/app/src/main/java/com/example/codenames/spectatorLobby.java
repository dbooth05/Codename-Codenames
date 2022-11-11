package com.example.codenames;

import static com.example.codenames.utils.Const.URL_JSON_GETPLAYERS_FIRST;
import static com.example.codenames.utils.Const.URL_JSON_GETPLAYERS_SECOND;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class spectatorLobby extends AppCompatActivity implements View.OnClickListener{

    private TextView lobby_name; // TextView to display the current lobby name
    private TextView error_text; // TextView to display error message if their is one
    private TextView player_count; // TextView to display the current amount of players in lobby
    private Button exit; // Button to exit back to the SpectatorHub
    private Button toGame; // Button to temporarily for testing purposes allow the spectator to go to game screen, will be removed
    private LinearLayout pList; // LinearLayout where all current players and their teams/roles will be displayed
    private String id; // String to hold the game id, used for anywhere where request will be made
    private String lobbyName; // String to hold the lobby name, used to display name and for temporary purposes.
    private JSONArray players; // JSONArray to hold the current players globally in spectatorLobby.java



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectator_lobby);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        lobby_name = (TextView) findViewById(R.id.specLobby_name);
        lobbyName = intent.getStringExtra("lobbyName");
        lobby_name.setText(lobbyName);
        player_count = (TextView) findViewById(R.id.specLobby_text_playercount);
        pList = (LinearLayout) findViewById(R.id.specLobby_view);

        error_text = (TextView) findViewById(R.id.specLobby_error);

        exit = (Button) findViewById(R.id.specLobby_exit);
        exit.setOnClickListener(this);

        toGame = (Button) findViewById(R.id.specLobby_toGame);
        toGame.setOnClickListener(this);

        getPlayers();
    }

    /*
    Method that when called will make a JSON GET request to get all players in the lobby. Calls addPlayer() with @params username, role, and team from the
    JSONObject in the JSONArray received by the response.
     */
    private void getPlayers() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = URL_JSON_GETPLAYERS_FIRST + id + URL_JSON_GETPLAYERS_SECOND;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            pList.removeAllViews();

                            JSONArray object = new JSONArray(response);
                            players = object;
                            player_count.setText(Integer.toString(players.length()));
                            System.out.println(response);
                            for (int i = 0; i < players.length(); i++) {
                                JSONObject o = (JSONObject) players.get(i);
                                String name = o.get("username").toString();
                                String role = o.get("role").toString();
                                String team = o.get("team").toString();
                                addPlayer(name, role, team);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                });

        queue.add(request);
    }

    /*
    Helper method that when called creates a new horizontal LinearLayout called "row", that will change the background color
    to that of the players team, creates a TextView to hold and display the username, and creates a TextView to hold and display the
    players role Spymaster or Operative. Then, the method will add the row to the vertical LinearLayout plist.
     */
    private void addPlayer(String pName, String role, String team) {

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150));

        LinearLayout.LayoutParams name = new LinearLayout.LayoutParams(500, 125);
        name.setMarginStart(100);
        name.setMargins(100, 20, 0, 20);
        LinearLayout.LayoutParams pRole = new LinearLayout.LayoutParams(500, 125);
        pRole.setMargins(0, 20, 150, 20);

        //Change background on row to player team
        if(team.toLowerCase(Locale.ROOT).equals("red")) {
            row.setBackgroundColor(Color.RED);
        } else if (team.toLowerCase(Locale.ROOT).equals("blue")) {
            row.setBackgroundColor(Color.BLUE);
        }

        //Create TextView to show player name
        TextView t = new TextView(this);
        t.setText(pName);
        t.setTextSize(20);
        t.setTextColor(Color.WHITE);
        t.setLayoutParams(name);


        row.addView(t);

        //Create TextView with role
        TextView r = new TextView(this);
        r.setText(role);
        r.setTextSize(20);
        r.setTextColor(Color.WHITE);
        r.setLayoutParams(pRole);

        row.addView(r);

        pList.addView(row);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.specLobby_exit) {
            startActivity(new Intent(spectatorLobby.this, spectatorHub.class));
        } else if (v.getId() == R.id.specLobby_toGame) {
            startActivity(new Intent(spectatorLobby.this, spectatorViewing.class).putExtra("lobbyName", lobbyName).putExtra("id", id));
        }
    }
}