package com.example.urvocalcoach;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urvocalcoach.Tuning.MusicNote;


public class MainActivity extends Activity {
	
	private UiController uiController;
	
	private AudioAnalyzer analyzer;
	
	private TextView userFreq;
	
	private ImageView userNoteImg;
	
	private TextView targetFreq;
	
	private TextView userNote;
	
	private Vibrator vibrator;
	
	private Spinner noteSelector;
	
	private SeekBar volumeBar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
        	analyzer = new  AudioAnalyzer();
        	uiController = new UiController(this);
			userFreq = (TextView)findViewById(R.id.user_note_freq);
			userNoteImg = (ImageView)findViewById(R.id.user_note);
			userNote = (TextView)findViewById(R.id.user_note_letter);
			noteSelector = (Spinner)findViewById(R.id.spinner_targetNote);
			volumeBar = (SeekBar)findViewById(R.id.user_volume_level);
			analyzer.addObserver(uiController);
			String defaultNote = this.getResources().getString(R.string.default_note);
			Button button = (Button)findViewById(R.id.target_note_sing);
			button.setOnClickListener(uiController);
			button.setOnTouchListener(uiController);
			this.displayFeedBack(false);
			vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
			ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_targetNotes, 
					R.layout.spinner_layout);
			noteSelector.setAdapter(arrayAdapter);
			noteSelector.setOnItemSelectedListener(uiController);
			noteSelector.setSelection(Tuning.getNoteByName(defaultNote).getIndex());
			targetFreq = (TextView)findViewById(R.id.target_note_freq);
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
    
    public void updateUserNote(MusicNote note, boolean tactileFeed, int position, int volume) {
    	userFreq.setText(Double.toString(note.getFrequency()));
    	userNote.setText(note.getNote());
    	position = position * 25;
    	userFreq.setTranslationY(position);
    	userNote.setTranslationY(position);
    	userNoteImg.setTranslationY(position);
    	if(volume < volumeBar.getMax()) {
    		volumeBar.setProgress(volumeBar.getMax()- (volumeBar.getMax() - volume));    		
    	} else {
    		volumeBar.setProgress(volumeBar.getMax());
    	}
    	if(tactileFeed) {
//    		analyzer.stop();
//    		uiController.stopFeedBack();
    		vibrator.vibrate(200);
    	}
    }
    
    public void updateTargetNote(MusicNote note) {
    	targetFreq.setText(Double.toString(note.getFrequency()));
    	noteSelector.setSelection(note.getIndex());
//    	targetNote.setText(note.getNote());
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
    		volumeBar.setProgress(0);
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
