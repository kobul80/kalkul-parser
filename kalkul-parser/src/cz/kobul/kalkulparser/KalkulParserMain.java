package cz.kobul.kalkulparser;

public class KalkulParserMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Kalkul1 - konvertor penezniho deniku");
		System.out.println("====================================");
		if (args == null || args.length == 0) {
			System.out.println("Parametry: jmeno_vstupniho_souboru jmeno_vystupniho_souboru");
			return;
		}
		System.out.print("Konvertuji ... ");
		final KalkulParser kalkulParser = new KalkulParser(args[0], args[1]);
		try {
			kalkulParser.parse();			
		}
		catch (Exception e) {
			System.out.println(" chyba pri parsovani souboru: ");
			e.printStackTrace();
			return;
		}
		try {
			kalkulParser.writeOutput();			
			System.out.println(" ... hotovo.");
		}
		catch (Exception e) {
			System.out.println(" chyba pri zapisu vysledku: ");
			e.printStackTrace();
		}
	}
}
