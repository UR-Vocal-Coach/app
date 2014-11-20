package com.example.urvocalcoach;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	private UiController uiController;
	
	private AudioAnalyzer analyzer;
	
	private TextView userNote;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
			analyzer = new  AudioAnalyzer();
			uiController = new UiController(this);
			analyzer.addObserver(uiController);
			userNote = (TextView)findViewById(R.id.user_note_freq);
		} catch (Exception e) {
			Toast.makeText(this, "The are problems with your microphone :(", Toast.LENGTH_LONG ).show();
		}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }   
    
    public void displayMessage(String message) {
    	userNote.setText(message);
    }
    
    
    @Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
        if(analyzer!=null)
        	analyzer.ensureStarted();
	}

	@Override
	protected void onStart() {
		super.onStart();
        if(analyzer!=null)
        	analyzer.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
        if(analyzer!=null)
        	analyzer.stop();
	}
}
