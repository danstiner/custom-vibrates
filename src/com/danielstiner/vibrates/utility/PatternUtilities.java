package com.danielstiner.vibrates.utility;

public class PatternUtilities {

	// milliseconds per dit to use
	private static final long MORSE_MULTIPLIER = 89;
	
	// number of letters to use when morsifying a name or such
	private static final int MAX_MORSE = 3;
    
    public static long[] generate(String text) {
		// TODO Auto-generated method stub
    	
    	// pare down the text to the first few words
    	text = text.substring(0, Math.min(MAX_MORSE, text.length()));
    	
    	long[] morse = MorseCodePattern.morsify(text);
    	long[] ret = new long[morse.length + 1];
    	
    	ret[0] = 0;
    	for(int i=0; i<morse.length; i++) {
    		ret[i+1] = morse[i] * MORSE_MULTIPLIER;
    	}
    	
		return ret;
	}
    
    public static boolean isValid(long[] pattern) {
		if(pattern == null) return false;
		// TODO Auto-generated method stub
		return true;
	}
}
