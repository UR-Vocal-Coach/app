package com.example.urvocalcoach;

import com.example.urvocalcoach.Tuning.MusicNote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urvocalcoach.Tuning.MusicNote;

public class DynamicsActivity extends Activity {

		/*
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_dynamics);
		}
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			startActivity(new Intent(getApplicationContext(), MenuActivity.class));
			finish();
		}
		*/
		
		private UiControllerDynamics uiController;
		private AudioAnalyzer analyzer;
		// private TextView userFreq;
		// private ImageView userNoteImg
		private ImageView userVolumeImg;
		private TextView volumeText;
		private TextView targetFreq;
		// private TextView userNote;
		private Vibrator vibrator;
		private Spinner noteSelector;
		// private SeekBar volumeBar;
		private TextView time;
		
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_dynamics);
	        try {
	        	analyzer = new  AudioAnalyzer();
	        	uiController = new UiControllerDynamics(this);
				// userFreq = (TextView)findViewById(R.id.user_note_freq);
				// userNoteImg = (ImageView)findViewById(R.id.user_note);
				// userNote = (TextView)findViewById(R.id.user_note_letter);
	        	userVolumeImg = (ImageView)findViewById(R.id.user_volume);
	        	volumeText = (TextView)findViewById(R.id.volume_text);
	        	time = (TextView)findViewById(R.id.dynamics_time);
				noteSelector = (Spinner)findViewById(R.id.spinner_targetNote);
				// volumeBar = (SeekBar)findViewById(R.id.user_volume_level);
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
				Toast.makeText(this, "The are problems with your microphone :(" + e, Toast.LENGTH_LONG ).show();
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
	    
	    public void getTime(double t) {
	    	int t1 = (int)(t/100);
	    	time.setText(String.valueOf(t1));
	    }
	    
	    public void updateUserNote(MusicNote note, boolean tactileFeed, int position, int volume, double t) {
	    	// old code from pitch training... can delete
	    	// userFreq.setText(Double.toString(note.getFrequency()));
	    	// userNote.setText(note.getNote());
	    	// position = position * 25;
	    	// userFreq.setTranslationY(position);
	    	// userNote.setTranslationY(position);
	    	// userNoteImg.setTranslationY(position);
	    	
	    	/*
	    	<ImageView
	        android:id="@+id/user_volume"
	        android:contentDescription="@string/user_volume_desc"
	        android:layout_width="wrap_content"
	        android:layout_height="@dimen/volume_height"
	        android:scaleType="fitXY"
	        android:layout_below="@id/scale_marker_9"
	        android:layout_marginLeft="@dimen/user_note_x"
	        android:layout_marginTop="@dimen/volume_y"
	        android:src="@drawable/dynamics_volume_line" /> 
	    	*/
	    	
	    	
	    	
	    	int t1 = (int)(t/100);
	    	int targetVolume = -Math.abs((int)(2.3*t1/100)-9)+7;
	    	// int targetVolume = 7-(7-(int)(t1*2/100));
	    	System.out.println("target volume = " + targetVolume + "\ntime = " + t1);
	    	
	    	// update the volume and convert it into a decimal that works with the layout
	    	double dVolume = (double)volume;
	    	float fVolume = (float)(dVolume/1000.0);
	    	volume = (int)(volume/100);
	    	userVolumeImg.setTranslationX(t1);
	    	userVolumeImg.setTranslationY(position*25);
	    	userVolumeImg.setScaleY(fVolume);
	    	volumeText.setText(String.valueOf(volume));
	    	
	    	// Create the current volume image
	    	ImageView currentVolume = new ImageView(this);
	    	if (t1 < 100)
	    		currentVolume.setImageResource(R.drawable.dynamics_start);
	    	else if ((position < 4 && position > -4) && volume==targetVolume)
	    		currentVolume.setImageResource(R.drawable.dynamics_success);
	    	else
	    		currentVolume.setImageResource(R.drawable.dynamics_wrong);
	    	RelativeLayout rl = (RelativeLayout) findViewById(R.id.target_scale);
	    	RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
	    	    RelativeLayout.LayoutParams.WRAP_CONTENT,
	    	    RelativeLayout.LayoutParams.WRAP_CONTENT);
	    	lp.addRule(RelativeLayout.BELOW, R.id.scale_marker_5);
	    	lp.topMargin = -16;
	    	lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	    	currentVolume.setTranslationX(t1);
	    	currentVolume.setTranslationY(position*25);
	    	currentVolume.setScaleY(fVolume);
	    	rl.removeView(currentVolume);
	    	rl.addView(currentVolume, lp);
	    	
	    	/*
	    	if(volume < volumeBar.getMax()) {
	    		volumeBar.setProgress(volumeBar.getMax()- (volumeBar.getMax() - volume));    		
	    	} else {
	    		volumeBar.setProgress(volumeBar.getMax());
	    	}
	    	*/
	    	if(tactileFeed) {
//	    		analyzer.stop();
//	    		uiController.stopFeedBack();
	    		vibrator.vibrate(200);
	    	}
	    }
	    
	    public void updateTargetNote(MusicNote note) {
	    	targetFreq.setText(Double.toString(note.getFrequency()));
	    	noteSelector.setSelection(note.getIndex());
//	    	targetNote.setText(note.getNote());
	    }
	    
	    public void displayFeedBack(boolean show) {
	    	if(show) {
	    		// userFreq.setVisibility(View.VISIBLE);
	    		// userNote.setVisibility(View.VISIBLE);
	    		// userNoteImg.setVisibility(View.VISIBLE);
	    		userVolumeImg.setVisibility(View.VISIBLE);
	    	} else {
	    		// userFreq.setVisibility(View.INVISIBLE);
	    		// userNote.setVisibility(View.INVISIBLE);
	    		// userNoteImg.setVisibility(View.INVISIBLE);
	    		userVolumeImg.setVisibility(View.INVISIBLE);
	    		// volumeBar.setProgress(0);
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
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			startActivity(new Intent(getApplicationContext(), MenuActivity.class));
			finish();
		}
	
}
