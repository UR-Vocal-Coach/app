package com.example.urvocalcoach;


public class Tuning {
	public static final String TAG = "RealGuitarTuner";
	
	private static final MusicNote[] notesArray = new MusicNote[60];
	
	private static final int TOP = 60;
	
	public Tuning() {
		initNoteMap();
	}
	
	private void initNoteMap() {
		double[] freqArray = new double[60];
		StringBuilder sb = new StringBuilder(3);
		freqArray[0] = 65.41;freqArray[1] = 69.30;freqArray[2] = 73.42;freqArray[3] = 77.78;freqArray[4] = 82.41;
		freqArray[5] = 87.31;freqArray[6] = 92.50;freqArray[7] = 98.00;freqArray[8] = 103.83;freqArray[9] = 110.00;freqArray[10] = 116.54;
		freqArray[11] = 123.47;freqArray[12] = 130.81;freqArray[13] = 138.59;freqArray[14] = 146.83;freqArray[15] = 155.56;
		freqArray[16] = 164.81;freqArray[17] = 174.61;freqArray[18] = 185.00;freqArray[19] = 196.00;freqArray[20] = 207.65;freqArray[21] = 220.00;
		freqArray[22] = 233.08;freqArray[23] = 246.94;freqArray[24] = 261.63;freqArray[25] = 277.18;freqArray[26] = 293.66;freqArray[27] = 311.13;
		freqArray[28] = 329.63;freqArray[29] = 349.23;freqArray[30] = 369.99;freqArray[31] = 392.00;freqArray[32] = 415.30;freqArray[33] = 440.00;
		freqArray[34] = 466.16;freqArray[35] = 493.88;freqArray[36] = 523.25;freqArray[37] = 554.37;freqArray[38] = 587.33;
		freqArray[39] = 622.25;freqArray[40] = 659.25;freqArray[41] = 698.46;freqArray[42] = 739.99;freqArray[43] = 783.99;
		freqArray[44] = 830.61;freqArray[45] = 880.00;freqArray[46] = 932.33;freqArray[47] = 987.77;freqArray[48] = 1046.50;
		freqArray[49] = 1108.73;freqArray[50] = 1174.66;freqArray[51] = 1244.51;freqArray[52] = 1318.51;
		freqArray[53] = 1396.91;freqArray[54] = 1479.98;freqArray[55] = 1567.98;freqArray[56] = 1661.22;
		freqArray[57] = 1760.00;freqArray[58] = 1864.66;freqArray[59] = 1975.53;
		int counter = 0, octave = 2;
		while(counter < freqArray.length) {
			sb.append("C").append(octave);
			notesArray[counter] = new MusicNote(freqArray[counter], sb.toString(), counter++);
			sb.setLength(0);sb.append("C#").append(octave);
			notesArray[counter] = new MusicNote(freqArray[counter], sb.toString(), counter++);
			sb.setLength(0);sb.append("D").append(octave);
			notesArray[counter] = new MusicNote(freqArray[counter], sb.toString(), counter++);
			sb.setLength(0);sb.append("D#").append(octave);
			notesArray[counter] = new MusicNote(freqArray[counter], sb.toString(), counter++);
			sb.setLength(0);sb.append("E").append(octave);
			notesArray[counter] = new MusicNote(freqArray[counter], sb.toString(), counter++);
			sb.setLength(0);sb.append("F").append(octave);
			notesArray[counter] = new MusicNote(freqArray[counter], sb.toString(), counter++);
			sb.setLength(0);sb.append("F#").append(octave);
			notesArray[counter] = new MusicNote(freqArray[counter], sb.toString(), counter++);
			sb.setLength(0);sb.append("G").append(octave);
			notesArray[counter] = new MusicNote(freqArray[counter], sb.toString(), counter++);
			sb.setLength(0);sb.append("G#").append(octave);
			notesArray[counter] = new MusicNote(freqArray[counter], sb.toString(), counter++);
			sb.setLength(0);sb.append("A").append(octave);
			notesArray[counter] = new MusicNote(freqArray[counter], sb.toString(), counter++);
			sb.setLength(0);sb.append("A#").append(octave);
			notesArray[counter] = new MusicNote(freqArray[counter], sb.toString(), counter++);
			sb.setLength(0);sb.append("B").append(octave);
			notesArray[counter] = new MusicNote(freqArray[counter], sb.toString(), counter++);
			sb.setLength(0);
			octave++;
		}
	}
	
	
	public MusicNote getNote(double frequency) {
		int low = - 1, high = TOP, curr = 0;
		double highChk, lowChk, min;
		MusicNote retVal;
		while(high - low > 1) {
			curr = (high + low)/2;
			if(notesArray[curr].getFrequency() < frequency) {
				low = curr;
			} else {
				high = curr;
			}
		}
		if(curr < TOP - 1 && curr > 0) {
			highChk = notesArray[curr + 1].getFrequency() - frequency;
			lowChk = frequency - notesArray[curr - 1].getFrequency();
			if(frequency > notesArray[curr].getFrequency()) {
				min = frequency - notesArray[curr].getFrequency();				
			} else {
				min = notesArray[curr].getFrequency() - frequency;
			}
			if(highChk < min) {
				if(highChk < lowChk) {
					retVal = notesArray[curr + 1];
				} else {
					retVal = notesArray[curr - 1];
				}
			} else {
				if(min < lowChk) {
					retVal = notesArray[curr];
				} else {
					retVal = notesArray[curr - 1];
				}
			}
		} else {
			retVal = notesArray[curr];
		}
		return retVal;
	}
	
	public MusicNote getNote(int index) {
		return notesArray[index];
	}
	
	public static class MusicNote {
		
		private double frequency;
		
		private String note;
		
		private int index;

		public MusicNote(double frequency, String note, int index) {
			this.frequency = frequency;
			this.note = note;
			this.index = index;
		}

		public double getFrequency() {
			return frequency;
		}

		public String getNote() {
			return note;
		}

		public int getIndex() {
			return index;
		}
	}
}
