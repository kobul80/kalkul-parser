package cz.kobul.kalkulparser;


/**
 * Class containing tools for working with strings.
 */
public final class StringTools {
	
	public static final char[] LATIN2_2_UNICODE = new char[] {        
		// znaky 128 a vyse
		'\u00C7', '\u00FC', '\u00E9', '\u00E2', '\u00E4', '\u016F', '\u0107', '\u00E7', //80 
		'\u0142', '\u00EB', '\u0150', '\u0151', '\u00EE', '\u0179', '\u00C4', '\u0106', //88
		'\u00C9', '\u0139', '\u013A', '\u00F4', '\u00F6', '\u013D', '\u013E', '\u015A', //90
		'\u015B', '\u00D6', '\u00DC', '\u0164', '\u0165', '\u0141', '\u00D7', '\u010D', //98
		'\u00E1', '\u00ED', '\u00F3', '\u00FA', '\u0104', '\u0105', '\u017D', '\u017E', //A0
		'\u0118', '\u0119', '\u00AC', '\u017A', '\u010C', '\u015F', '\u00AB', '\u00BB', //A8
		'\u2591', '\u2592', '\u2593', '\u2502', '\u2524', '\u00C1', '\u00C2', '\u011A', //B0 
		'\u015E', '\u2563', '\u2551', '\u2557', '\u255D', '\u017B', '\u017C', '\u2510', //B8
		'\u2514', '\u2534', '\u252C', '\u251C', '\u2500', '\u253C', '\u0102', '\u0103', //C0 
		'\u255A', '\u2554', '\u2569', '\u2566', '\u2560', '\u2550', '\u256C', '\u00A4', //C8
		'\u0111', '\u0110', '\u010E', '\u00CB', '\u010F', '\u0147', '\u00CD', '\u00CE', //D0 
		'\u011B', '\u2518', '\u250C', '\u2588', '\u2584', '\u0162', '\u016E', '\u2580', //D8
		'\u00D3', '\u00DF', '\u00D4', '\u0143', '\u0144', '\u0148', '\u0160', '\u0161', //E0
		'\u0154', '\u00DA', '\u0155', '\u0170', '\u00FD', '\u00DD', '\u0163', '\u00B4', //E8
		'\u00AD', '\u02DD', '\u02DB', '\u02C7', '\u02D8', '\u00A7', '\u00F7', '\u00B8', //F0 
		'\u00B0', '\u00A8', '\u02D9', '\u0171', '\u0158', '\u0159', '\u25A0', '\u00A0'  //F8
	};

	public static final char latin2ToUnicode(final int value) {
		return charsetToUnicode(value, LATIN2_2_UNICODE);
	}

    public static final char[] KAMENICKY_2_UNICODE = new char[] {        
        // znaky 128 a vyse
        '\u010C', '\u00FC', '\u00E9', '\u010F', '\u00E4', '\u010E', '\u0164', '\u010D', //80 
        '\u011B', '\u011A', '\u0139', '\u00CD', '\u013E', '\u013A', '\u00C4', '\u00C1', //88
        '\u00C9', '\u017E', '\u017D', '\u00F4', '\u00F6', '\u00D3', '\u016F', '\u00DA', //90
        '\u00FD', '\u00D6', '\u00DC', '\u0160', '\u013D', '\u00DD', '\u0158', '\u0165', //98
        '\u00E1', '\u00ED', '\u00F3', '\u00FA', '\u0148', '\u0147', '\u016E', '\u00D4', //A0
        '\u0161', '\u0159', '\u0155', '\u0154', '\u00BC', '\u00A1', '\u00AB', '\u00BB', //A8
        '\u2591', '\u2592', '\u2593', '\u2502', '\u2524', '\u2561', '\u2562', '\u2556', //B0 
        '\u2555', '\u2563', '\u2551', '\u2557', '\u255D', '\u255C', '\u255B', '\u2510', //B8
        '\u2514', '\u2534', '\u252C', '\u251C', '\u2500', '\u253C', '\u255E', '\u255F', //C0 
        '\u255A', '\u2554', '\u2569', '\u2566', '\u2560', '\u2550', '\u256C', '\u2567', //C8
        '\u2568', '\u2564', '\u2565', '\u2559', '\u2558', '\u2552', '\u2553', '\u256B', //D0 
        '\u256A', '\u2518', '\u250C', '\u2588', '\u2584', '\u258C', '\u2590', '\u2580', //D8
        '\u03B1', '\u00DF', '\u0393', '\u03C0', '\u03A3', '\u03C3', '\u00B5', '\u03C4', //E0 
        '\u03A6', '\u0398', '\u03A9', '\u03B4', '\u221E', '\u03C6', '\u03B5', '\u2229', //E8
        '\u2261', '\u00B1', '\u2265', '\u2264', '\u2320', '\u2321', '\u00F7', '\u2248', //F0 
        '\u00B0', '\u2219', '\u00B7', '\u221A', '\u207F', '\u00B2', '\u25A0', '\u00A0'  //F8
    };
    
    public static final char kamenickyToUnicode(final int value) {
    	return charsetToUnicode(value, KAMENICKY_2_UNICODE);
    }
    
    private static final char charsetToUnicode(final int character, final char[] charsetTable) {
    	return character < 128 ? (char)character : charsetTable[character - 128];
    }

}