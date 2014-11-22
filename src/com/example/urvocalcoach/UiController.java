package com.example.urvocalcoach;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.urvocalcoach.AudioAnalyzer.AnalyzedSound;
import com.example.urvocalcoach.Tuning.MusicNote;


public class UiController implements Observer {

	private MainActivity ui;
	private double frequency;
	private ExecutorService executor;
	private boolean tactileFeed;
	private boolean toneMatch;
	private boolean show;
	private Tuning tuning;
	private MusicNote targetNote;
	private MusicNote currentNote;
	
	private enum MessageClass {
		TUNING_IN_PROGRESS,
		WEIRD_FREQUENCY,
		TOO_QUIET,
		TOO_NOISY,
	}
	
	public UiController(MainActivity u) {
		ui = u;
		executor = Executors.newFixedThreadPool(4);
		frequency = Double.NaN;
		tuning = new Tuning();
		targetNote = tuning.getNote(262.5);
		currentNote = tuning.getNote(0);
	}
	
	@Override
	public void update(Observable who, Object obj) {
		if(who instanceof AudioAnalyzer) {
			if(obj instanceof AnalyzedSound) {
				AnalyzedSound result = (AnalyzedSound)obj;
				frequency = FrequencySmoothener.getSmoothFrequency(result);
				if(frequency > 0.0) {
					currentNote = tuning.getNote(frequency);
					ui.displayMessage(currentNote, toneMatch, targetNote.getIndex() - currentNote.getIndex());
//					ui.translateNotes(targetNote.getIndex() - currentNote.getIndex());
					toneMatch = false;
					if(!show) {
						show = true;
						ui.displayFeedBack(show);
					}
				} else {
					if(show) {
						show = false;
						ui.displayFeedBack(show);
					}
				}
			}
		}
	}
	
	public void startTactileFeedBack() {
		tactileFeed = true;
		Thread tactileFeedBack = new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis(), end ;
				while(tactileFeed) {
					if(currentNote.getFrequency() == targetNote.getFrequency()) {
						end = System.currentTimeMillis();
						if(end - start >= 2000) {
							toneMatch = true;
							//tactileFeed = false;
						}
					} else {
						start = System.currentTimeMillis();
					}
				}
			}
		});
		executor.execute(tactileFeedBack);
	}
	
	public void stopTactileFeedBack() {
		
	}
}
