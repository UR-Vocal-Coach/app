package com.example.urvocalcoach;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urvocalcoach.Tuning.MusicNote;


public class MainActivity extends Activity {
	
	private UiController uiController;
	
	private AudioAnalyzer analyzer;
	
	private TextView userFreq;
	
	private ImageView userNoteImg;
	
	private TextView userNote;
	
	private Vibrator vibrator;
	
	private long bottom;
	
	private long top;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
			analyzer = new  AudioAnalyzer();
			uiController = new UiController(this);
			analyzer.addObserver(uiController);
			userFreq = (TextView)findViewById(R.id.user_note_freq);
			userNoteImg = (ImageView)findViewById(R.id.user_note);
			userNote = (TextView)findViewById(R.id.user_note_letter);
			this.displayFeedBack(false);
			vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
			top = findViewById(R.id.scale_marker_1).getTop();
			bottom = findViewById(R.id.scale_marker_21).getTop();
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
    
    public void displayMessage(MusicNote note, boolean tactileFeed, int position) {
    	userFreq.setText(Double.toString(note.getFrequency()));
    	userNote.setText(note.getNote());
    	position = position * 25;
    	userFreq.setTranslationY(position);
    	userNote.setTranslationY(position);
    	userNoteImg.setTranslationY(position);
    	if(tactileFeed) {
//    		analyzer.stop();
//    		uiController.stopFeedBack();
    		vibrator.vibrate(200);
    	}
    }
    
    public void displayFeedBack(boolean show) {
    	if(show) {
    		userFreq.setVisibility(View.VISIBLE);
    		userNote.setVisibility(View.VISIBLE);
    		userNoteImg.setVisibility(View.VISIBLE);    		
    	} else {
    		userFreq.setVisibility(View.INVISIBLE);
    		userNote.setVisibility(View.INVISIBLE);
    		userNoteImg.setVisibility(View.INVISIBLE);
    	}
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
        if(analyzer!=null) {
        	analyzer.ensureStarted();
        }
	}

	@Override
	protected void onStart() {
		super.onStart();
        if(analyzer!=null) {
        	analyzer.start();
        }
        if(uiController != null) {
        	uiController.startTactileFeedBack();
        }
	}

	@Override
	protected void onStop() {
		super.onStop();
        if(analyzer!=null) {
        	analyzer.stop();        	
        }
        if(uiController != null) {
        	uiController.stopTactileFeedBack();
        }
	}
}
