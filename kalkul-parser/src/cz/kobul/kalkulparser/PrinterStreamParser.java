package cz.kobul.kalkulparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;

/**
 * This class is used to interpret character stream as it is printed on printer.
 * The main purpose is to correctly interpret #13 (Carriage Return, <CR>) character. 
 * The meaning of #10 is changed a bit, becouse on real printer, when you
 * call #10, no <CR> is called and printer starts printing from the same column it ended 
 * one line before. This behaviour is not emulated and #10 leads to Carriage Return too.
 * @author kobul
 */
public class PrinterStreamParser {

    /** default line width */
    public static final int DEFAULT_LINE_WIDTH = 80;
    
    /** actual line width */
    private final int lineWidth;
    
    /** reader */
    private final Reader reader;

    /** input stream */
    private final InputStream inputStream;
    
    /** one line buffer */
    private final char[] lineBuffer; 
    
    /**
     * Conctructs PrinterStreamParser with default line width.
     * @param inputStream
     */
    public PrinterStreamParser(final Reader reader) {     
        this(reader, null, DEFAULT_LINE_WIDTH);
    }
    
    public PrinterStreamParser(final Reader reader, final int lineWidth) {
    	this(reader, null, lineWidth);
    }
    	    
    public PrinterStreamParser(final InputStream inputStream) {     
        this(null, inputStream, DEFAULT_LINE_WIDTH);
    }
    
    public PrinterStreamParser(final InputStream inputStream, final int lineWidth) {
    	this(null, inputStream, lineWidth);
    }
    	    
    
    /**
     * Conctructs PrinterStreamParser with given line width.
     * @param inputStream
     */
    public PrinterStreamParser(final Reader reader, final InputStream inputStream, final int lineWidth) {
        //TODO: check values
        this.reader = reader;
        this.inputStream = inputStream;
        this.lineWidth = lineWidth;
        this.lineBuffer = new char[lineWidth];
    }
    
    /**
     * Converts given int representation of character to char.
     * This method is for recoding purposes. 
     * @param character ordinal value
     * @return
     */
    public char intToChar(int character) {
        return (char)character;
    }

    public int readInt() throws IOException {
    	return (reader != null ? reader.read() : inputStream.read());
    }
    
    /**
     * Reads one line from given stream. Correctly parses #10 and #13 chars.
     * @return
     * @throws IOException
     */
    public String readLine() throws IOException {
        // init variables
        int onechar = 0;
        int column = 0;
        int maxColumn = 0;
        Arrays.fill(lineBuffer, ' ');
        // read one line
        while ((onechar = readInt()) != -1) {
            if (onechar == 10 || column == lineWidth) {
                return new String(lineBuffer, 0, maxColumn);
            }
            if (onechar == 13) {
                column = 0;
            }
            if (onechar == 32) {
                column++;
            }
            if (onechar != 10 && onechar != 13 && onechar != 32) {
                lineBuffer[column] = intToChar(onechar);
                column++;
                if (column > maxColumn) {
                    maxColumn = column;
                }
            }
        }
        return (column > 0) ? new String(lineBuffer, 0, maxColumn) : null;
    }
    
    /**
     * Close associated inputStream.
     */
    public void close() {
        try {
            reader.close();
        }
        catch (Exception e) {
            // --- nothing ---
        }
    }
    
}

