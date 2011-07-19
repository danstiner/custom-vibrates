package com.danielstiner.vibrates.utility;


public class MorseCodePattern {

	/** Length of the dit unit */
	public static final long DIT = 1;
	public static final long DAH = DIT * 3;

	public static final long CHAR_GAP = DIT;
	public static final long LETTER_GAP = DIT * 3;
	public static final long WORD_GAP = DIT * 7;

	private static final long g = CHAR_GAP;

	public static final long[] A = { DIT, g, DAH };
	public static final long[] B = { DAH, g, DIT, g, DIT, g, DIT };
	public static final long[] C = { DAH, g, DIT, g, DAH, g, DIT };
	public static final long[] D = { DAH, g, DIT, g, DIT };
	public static final long[] E = { DIT };
	public static final long[] F = { DIT, g, DIT, g, DAH, g, DIT };
	public static final long[] G = { DAH, g, DAH, g, DIT };
	public static final long[] H = { DIT, g, DIT, g, DIT, g, DIT };
	public static final long[] I = { DIT, g, DIT };
	public static final long[] J = { DIT, g, DAH, g, DAH, g, DAH };
	public static final long[] K = { DAH, g, DIT, g, DAH };
	public static final long[] L = { DIT, g, DAH, g, DIT, g, DIT };
	public static final long[] M = { DAH, g, DAH };
	public static final long[] N = { DAH, g, DIT };
	public static final long[] O = { DAH, g, DAH, g, DAH };
	public static final long[] P = { DIT, g, DAH, g, DAH, g, DIT };
	public static final long[] Q = { DAH, g, DAH, g, DIT, g, DAH };
	public static final long[] R = { DIT, g, DAH, g, DIT };
	public static final long[] S = { DIT, g, DIT, g, DIT };
	public static final long[] T = { DAH };
	public static final long[] U = { DIT, g, DIT, g, DAH };
	public static final long[] V = { DIT, g, DIT, g, DIT, g, DAH };
	public static final long[] W = { DIT, g, DAH, g, DAH };
	public static final long[] X = { DAH, g, DIT, g, DIT, g, DAH };
	public static final long[] Y = { DAH, g, DIT, g, DAH, g, DAH };
	public static final long[] Z = { DAH, g, DAH, g, DIT, g, DIT };
	
	public static final long[] ZERO = { DAH, g, DAH, g, DAH, g, DAH, g, DAH };
	public static final long[] ONE = { DIT, g, DAH, g, DAH, g, DAH, g, DAH };
	public static final long[] TWO = { DIT, g, DIT, g, DAH, g, DAH, g, DAH };
	public static final long[] THREE = { DIT, g, DIT, g, DIT, g, DAH, g, DAH };
	public static final long[] FOUR = { DIT, g, DIT, g, DIT, g, DIT, g, DAH };
	public static final long[] FIVE = { DIT, g, DIT, g, DIT, g, DIT, g, DIT };
	public static final long[] SIX = { DAH, g, DIT, g, DIT, g, DIT, g, DIT };
	public static final long[] SEVEN = { DAH, g, DAH, g, DIT, g, DIT, g, DIT };
	public static final long[] EIGHT = { DAH, g, DAH, g, DAH, g, DIT, g, DIT };
	public static final long[] NINE = { DAH, g, DAH, g, DAH, g, DAH, g, DIT };
	
	public static final long[] PERIOD = { DIT, g, DAH, g, DIT, g, DAH, g, DIT, g, DAH };
	public static final long[] COMMA = { DAH, g, DAH, g, DIT, g, DIT, g, DAH, g, DAH };
	public static final long[] QUESTION_MARK = { DIT, g, DIT, g, DAH, g, DAH, g, DIT, g, DIT };
	public static final long[] APOSTROPHE = { DIT, g, DAH, g, DAH, g, DAH, g, DIT };
	public static final long[] EXCLAMATION_MARK = { DAH, g, DIT, g, DAH, g, DIT, g, DAH, g, DAH };
	public static final long[] SLASH = { DAH, g, DIT, g, DIT, g, DAH, g, DIT };
	public static final long[] PAREN_OPEN = { DAH, g, DIT, g, DAH, g, DAH, g, DIT };
	public static final long[] PAREN_CLOSE = { DAH, g, DIT, g, DAH, g, DAH, g, DIT, g, DAH };
	public static final long[] AMPERSTAND = { DIT, g, DAH, g, DIT, g, DIT, g, DIT };
	public static final long[] COLON = { DAH, g, DAH, g, DAH, g, DIT, g, DIT, g, DIT };
	public static final long[] SEMICOLON = { DAH, g, DIT, g, DAH, g, DIT, g, DAH, g, DIT };
	public static final long[] EQUALS_SIGN = { DAH, g, DIT, g, DIT, g, DIT, g, DAH };
	public static final long[] PLUS = { DIT, g, DAH, g, DIT, g, DAH, g, DIT };
	public static final long[] MINUS = { DAH, g, DIT, g, DIT, g, DIT, g, DIT, g, DAH };
	public static final long[] UNDERSCORE = { DIT, g, DIT, g, DAH, g, DAH, g, DIT, g, DAH };
	public static final long[] QUOTE_MARK = { DIT, g, DAH, g, DIT, g, DIT, g, DAH, g, DIT };
	public static final long[] DOLLAR_SIGN = { DIT, g, DIT, g, DIT, g, DAH, g, DIT, g, DIT, g, DAH };
	public static final long[] AT_SIGN     = { DIT, g, DAH, g, DAH, g, DIT, g, DAH, g, DIT };

	public static final long[] A_UMLAUT = { DIT, g, DAH, g, DIT, g, DAH };
	public static final long[] A_GRAVE = { DIT, g, DAH, g, DAH, g, DIT, g, DAH };
	public static final long[] C_CEDILLA = { DAH, g, DIT, g, DAH, g, DIT, g, DIT };
	public static final long[] CH = { DAH, g, DAH, g, DAH, g, DAH };
	public static final long[] ETH = { DIT, g, DIT, g, DAH, g, DAH, g, DIT };
	public static final long[] S_MARK = { DIT, g, DIT, g, DIT, g, DAH, g, DIT, g, DIT, g, DIT };

	public static final long[] UMLAUT_A = { DIT, g, DAH, g, DIT, g, DIT, g, DAH };
	public static final long[] UMLAUT_B = { DIT, g, DIT, g, DAH, g, DIT, g, DIT };
	public static final long[] UMLAUT_C = { DAH, g, DAH, g, DIT, g, DAH, g, DIT };
	public static final long[] UMLAUT_D = { DAH, g, DAH, g, DAH, g, DAH };
	public static final long[] UMLAUT_E = { DIT, g, DAH, g, DAH, g, DAH, g, DIT };
	public static final long[] UMLAUT_F = { DAH, g, DAH, g, DIT, g, DIT, g, DAH, g, DIT };

	public static final long[] N_TILDE = { DAH, g, DAH, g, DIT, g, DAH, g, DAH };
	public static final long[] O_UMLAUT = { DAH, g, DAH, g, DAH, g, DIT };
	public static final long[] S_CIRCUMFLEX = { DIT, g, DIT, g, DIT, g, DAH, g, DIT };
	public static final long[] THORN = { DIT, g, DAH, g, DAH, g, DIT, g, DIT };
	public static final long[] U_UMLAUT = { DIT, g, DIT, g, DAH, g, DAH };
	public static final long[] Z_GRAPHEME = { DAH, g, DAH, g, DIT, g, DIT, g, DAH };


	public static long[] morsify(String string) {
		java.util.ArrayList<Long> pattern = new java.util.ArrayList<Long>();
		// Loop each letter
		for(int char_index=0; char_index<string.length(); char_index++) {
			long[] char_pattern = morsify(string.charAt(char_index));
			
			// Check for invalid character
			if(char_pattern == null) return null;
			
			// Append the character's morse pattern
			for(int index=0; index<char_pattern.length; index++) {
				pattern.add(char_pattern[index]);
			}
			// Space between letters
			pattern.add(LETTER_GAP);
			
		}
		// Build a pattern to return
		long[] return_pattern = new long[pattern.size()];
		for(int index=0; index<pattern.size(); index++) {
			return_pattern[index] = pattern.get(index);
		}		
		return return_pattern;
	}

	/**
	 * 
	 * @param character 
	 * @return
	 */
	public static long[] morsify(char character) {
		switch (Character.toLowerCase(character)) {
		case ' ': return new long[]{0, WORD_GAP, 0};
		
		case 'a': return A;
		case 'b': return B;
		case 'c': return C;
		case 'd': return D;
		case 'e': return E;
		case 'f': return F;
		case 'g': return G;
		case 'h': return H;
		case 'i': return I;
		case 'j': return J;
		case 'k': return K;
		case 'l': return L;
		case 'm': return M;
		case 'n': return N;
		case 'o': return O;
		case 'p': return P;
		case 'q': return Q;
		case 'r': return R;
		case 's': return S;
		case 't': return T;
		case 'u': return U;
		case 'v': return V;
		case 'w': return W;
		case 'x': return X;
		case 'y': return Y;
		case 'z': return Z;
		
		case '0': return ZERO;
		case '1': return ONE;
		case '2': return TWO;
		case '3': return THREE;
		case '4': return FOUR;
		case '5': return FIVE;
		case '6': return SIX;
		case '7': return SEVEN;
		case '8': return EIGHT;
		case '9': return NINE;
		
		case '.': return PERIOD;
		case ',': return COMMA;
		case '?': return QUESTION_MARK;
		case '\'': return APOSTROPHE;
		case '!': return EXCLAMATION_MARK;
		case '/': return SLASH;
		case '(': return PAREN_OPEN;
		case ')': return PAREN_CLOSE;
		case '&': return AMPERSTAND;
		case ':': return COLON;
		case ';': return SEMICOLON;
		case '=': return EQUALS_SIGN;
		case '+': return PLUS;
		case '-': return MINUS;
		case '_': return UNDERSCORE;
		case '"': return QUOTE_MARK;
		case '$': return DOLLAR_SIGN;
		case '@': return AT_SIGN;
		
		
		// TODO
		
		case 0x0144: return N_TILDE;
		case 0x00F1: return N_TILDE;
		case 0x00F3: return O_UMLAUT;
		case 0x00F8: return O_UMLAUT;
		case 0x00F6: return O_UMLAUT;
		case 0x015D: return S_CIRCUMFLEX;
		case 0x00FE: return THORN;
		case 0x00FC: return U_UMLAUT;
		case 0x017E: return Z_GRAPHEME;
		
		default: return null;
		}
	}
}
