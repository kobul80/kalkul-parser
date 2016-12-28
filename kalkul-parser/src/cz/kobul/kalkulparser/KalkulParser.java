package cz.kobul.kalkulparser;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class KalkulParser {
		
	/** printer codes */
	public static final String START_ITALIC = "\u001b4";
	public static final String STOP_ITALIC = "\u001b5";
	public static final String START_BOLD = "\u001bE";
	public static final String STOP_BOLD = "\u001bF";
	public static final String START_DOUBLE_STRIKE = "\u001bG";
	public static final String STOP_DOUBLE_STRIKE = "\u001bH";
	public static final String WIDTH_PICA = "\u001bP";
	public static final String WIDTH_ELITE = "\u001bM";
	public static final String START_CONDENSED = "\u000f";
	public static final String STOP_CONDENSED = "\u0012";
	public static final String START_DOUBLE_HEIGHT = "\u001bw\u0001";
	public static final String STOP_DOUBLE_HEIGHT = "\u001bw\u0000";
	public static final String FONT_NLQ = "\u001bx\u0001";
	public static final String FONT_DRAFT = "\u001bx\u0000";
	public static final String PAGE_BREAK = "\u000c";
	public static final String PRINTER_RESET = "\u001b@";
	public static final String NLQ_FONT_0 = "\u001bk\u0000";
	public static final String NLQ_FONT_1 = "\u001bk\u0001";
	
	/** these codes will be interpreted as "nothing" */
	public static final String[] EMPTY_CODES = new String[] {
		START_ITALIC, STOP_ITALIC, START_BOLD, STOP_BOLD, START_DOUBLE_STRIKE,
		STOP_DOUBLE_STRIKE, WIDTH_PICA, WIDTH_ELITE, START_CONDENSED, STOP_CONDENSED,		
		START_DOUBLE_HEIGHT, STOP_DOUBLE_HEIGHT, FONT_NLQ, FONT_DRAFT, PAGE_BREAK,
		PRINTER_RESET, NLQ_FONT_0, NLQ_FONT_1
	};
	
	public static final int HEADER_LINES = 2;
	
	public static final int DEFAULT_RECORDS_PER_PAGE = 35;
	
	private final int RECORDS_PER_PAGE_REAL;	
	
	private final String inputFileName;
	
	private final String outputFileName;
	
	private String year = null;
	
	private final List<PDLine> lines = new ArrayList<PDLine>(200);

	
	public KalkulParser(final String inputFile, final String outputFile) {
		this(inputFile, outputFile, DEFAULT_RECORDS_PER_PAGE);
	}
	
	public KalkulParser(final String inputFile, final String outputFile, final int recordsPerPage) {
		if (inputFile == null) {
			throw new IllegalArgumentException("inputFile must not be null.");
		}
		if (outputFile == null) {
			throw new IllegalArgumentException("inputFile must not be null.");
		}
		this.inputFileName = inputFile;
		this.outputFileName = outputFile;
		this.RECORDS_PER_PAGE_REAL = recordsPerPage + HEADER_LINES;
	}
	
	/**
	 * Parse given inputFile.
	 * @throws Exception
	 */
	public void parse() throws Exception {
		// create printer stream parser with kamenicky recoding
		final PrinterStreamParser parser = new PrinterStreamParser(new FileInputStream(inputFileName), 160) {
			@Override
			public char intToChar(int character) {
				// convert page breaks to LF's
				if (character == 12) {
					return (char)10;
				}
				// convert Kamenicky code to Unicode
				return StringTools.kamenickyToUnicode(character);
			}        
		};
		try {
			String line = null;
			int linePos = 0;
			int part = -1;
			int page = 0;
			boolean start = false;		
			while ((line = parser.readLine()) != null) {
				if (year == null) {
					int yearIndex = line.indexOf("(rok): ");
					if (yearIndex != -1) {
						year = line.substring(yearIndex + 7, yearIndex + 11 );
					}
				}
				if (line.contains("/a")) {
					part = -1;
					System.out.print(" " + (page + 1));
					page++;
				}
				if (line.contains("\u2551èíslo")) {
					start = true;
					linePos = 0;					
					part++;
				}				
				if (line.contains("\u255a")) {
					start = false;
				}
				if (start & !line.contains("\u2500")) {					
					String[] cells = line.split("\u2551|\u2502");
					if (cells.length > 1) {
						if (linePos >= HEADER_LINES && linePos < RECORDS_PER_PAGE_REAL + HEADER_LINES) {
							int currentRecordNo = (page - 1) * RECORDS_PER_PAGE_REAL + linePos - HEADER_LINES;
//							System.out.println("page: " + page + "; part: " + part + "; line: " + linePos + "; currRec: " + currentRecordNo);
							if (part == 0) {
								PDLine pdLine = new PDLine(
										cells[1], 
										cells[2], 
										cells[3], 
										cells[5].trim().replaceAll("Komerèní.*", "KB"));
								for (int i = 6; i < cells.length; i++) {
									pdLine.addDataField(cells[i]);
								}
								lines.add(pdLine);
							}
							else {
								PDLine pdLine = lines.get(currentRecordNo);
								if (pdLine == null) {
									throw new IllegalStateException("Something's wrong! Can't retrieve pdLine " + currentRecordNo);
								}
								for (int i = 2; i < cells.length; i++) {
									pdLine.addDataField(cells[i]);
								}								
							}						
						}
						linePos++;
					}						
				}
			}
			
		}
		finally {
			parser.close();
		}
	}
	
	/**
	 * Write LaTeX output to outputFile.
	 * @throws Exception
	 */
	public void writeOutput() throws Exception {
		final Template template = getTemplate("pd.template");

		template.set("year", year);

		for (int i = 0; i < lines.size() / RECORDS_PER_PAGE_REAL; i ++) {
			final Template onePage = template.get("page").clear();
			onePage.set("year", year);
			onePage.set("pageno", Integer.valueOf(i + 1));
			for (int j = 0; j < RECORDS_PER_PAGE_REAL; j++) {
				final PDLine pdLine = lines.get(i * RECORDS_PER_PAGE_REAL + j);
				final Template linei = onePage.get("linei").clear();
				final Template lineii = onePage.get("linei").clear();
				linei.set("content", pdLine.getFirstPartLaTex());
				lineii.set("content", pdLine.getSecoondPartLatex());
				onePage.append("linei", linei);
				onePage.append("lineii", lineii);
			}
			template.append("page", onePage);				
		}

		dumpStringToFile(template.toString(), outputFileName, "Windows-1250");
	}
	
	
	// ----------------------
	// --- helper methods ---
	// ----------------------

	/**
	 * Returns template according to specified name.
	 */
	public static Template getTemplate(final String templateName) throws Exception {
		final String path = KalkulParser.class.getPackage().getName().replaceAll("\\.","/")+"/"+ templateName;
		final InputStream inputStream = KalkulParser.class.getClassLoader().getResourceAsStream(path);
        return new Template(new BufferedReader(new InputStreamReader(inputStream)));
	}

	/**
	 * The method does a dump of the string into file specified by the string name in given encoding.
	 * 
	 * @param 	buffer 		dump string
	 * @param	fileName	file name representing file to dump   
	 * @param	encoding	use encoding
	 * @throws				IOException
	 */    	
	public static void dumpStringToFile(final String buffer, final String fileName, final String encoding) throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(fileName), 65535), encoding);
		out.write(buffer);
		out.close();
	}
	
}
