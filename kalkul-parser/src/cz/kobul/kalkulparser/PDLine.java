package cz.kobul.kalkulparser;

import java.math.BigDecimal;

public class PDLine {

	public static final char COL_SEPARATOR = '&';
	
	//  3 - cislo bank. uctu
	// 14 - vydaje na udrzeni prijmu - mzdy
	// 17 - datum
	// 18 - cislo dokladu
	// 19 - rozliseni
	// 20 - text
	// 22 - nedanene prijmy - dph	
	// 23 - nedanene prijmy - sd
	// 31 - vydaje nezahrnovane do zakladu dane - dph
	private final int[] skipCols = { 3, 14, 17, 18, 19, 20, 22, 23, 31 };
	
	private String cisloRadku;
	private String datum;
	private String cisloDokladu;
	private String text;
	
	//	1. pokladna - prijmy
	//	2. pokladna - vydaje
	//	3. pokladna - stav
	//	4. ucet - prijmy
	//	5. ucet - vydaje
	//	6. ucet - stav
	//	7. danene prijmy - celkem
	//	8. danene prijmy - zbozi
	//	9. danene prijmy - vyrobky
	//	10. danene prijmy - ostatni
	//	11. vydaje nutne k zach. prijmu - celkem
	//	12. vydaje nutne k zach. prijmu - material
	//	13. vydaje nutne k zach. prijmu - zbozi
	//	14. vydaje nutne k zach. prijmu - pojisteni
	//	15. vydaje nutne k zach. prijmu - rezie
	//	16. nedanene prijmy - zdanene
	//	17. nedanene prijmy - uvery
	//	18. nedanene prijmy - osobni
	//	19. nedanene prijmy - ostatni
	//	20. vydaje nezahr. do zakl. dane - celkem
	//	21. vydaje nezahr. do zakl. dane - HM a NHM
	//	22. vydaje nezahr. do zakl. dane - dan z prijmu
	//	23. vydaje nezahr. do zakl. dane - rezerva
	//	24. vydaje nezahr. do zakl. dane - os. spotreba
	//	25. vydaje nezahr. do zakl. dane - dary
	//	26. vydaje nezahr. do zakl. dane - ostatni
	//	27. prubezne polozky - prijmy
	//	28. prubezne polozky - vydaje 
	private BigDecimal[] data = new BigDecimal[28];

	
	private int realCol = 0;
	
	private int parsedCol = 0;
	
	public PDLine(final String cisloRadku, final String datum, final String cisloDokladu, final String text) {
		this.cisloRadku = cisloRadku.trim();
		this.datum = datum.trim();
		this.cisloDokladu = cisloDokladu.trim().replace(" ", "");
		this.text = text.trim();
	}

	private boolean skipThisCol() {
		for (int i = 0; i < skipCols.length; i++) {
			if (parsedCol == skipCols[i]) {
				return true;
			}
		}
		return false;
	}
	
	public void addDataField(String field) {
		if (!skipThisCol()) {
			final String value = field.trim();
//			System.out.println("Adding \"" + value + "\"");
			data[realCol] =  (value.length() == 0) ? BigDecimal.ZERO : new BigDecimal(value);
			realCol++;			
		}
		parsedCol++;
	}
	
	public String getLine() {
		final StringBuffer result = new StringBuffer(200);
		result.append(cisloRadku).append(".\t").append(datum).append("\t").append(cisloDokladu).append("\t").append(text);		
		for (int i = 1; i < data.length; i++) {
			result.append("\t").append(data[i]);
		}
		return result.toString();
	}
		
	protected String bigDecimalToString(final BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) == 0) {
			return "";
		}
		if (value.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
			return value.longValue() + ",---";			
		}
		return value.toString().replace('.', ',');
	}
	
	public String getFirstPartLaTex() {
		final StringBuffer result = new StringBuffer(100);
		result.append(cisloRadku).append(COL_SEPARATOR).
			append(datum).append(COL_SEPARATOR).
			append(cisloDokladu).append(COL_SEPARATOR).
			append(text);		
		for (int i = 0; i < 10; i++) {
			result.append(COL_SEPARATOR).append(bigDecimalToString(data[i]));
		}
		return result.toString();		
	}
	
	public String getSecoondPartLatex() {
		final StringBuffer result = new StringBuffer(100);
		result.append(bigDecimalToString(data[10]));
		for (int i = 11; i < data.length - 2; i++) {
			result.append(COL_SEPARATOR).append(bigDecimalToString(data[i]));
		}
		return result.toString();		
		
	}
	
}
