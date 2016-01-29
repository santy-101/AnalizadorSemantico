import java.io.PrintStream;
import java.util.Hashtable;
import java.lang.String;
import java.util.ArrayList;
import java.util.Enumeration;

class TokenAsignaciones {
	// Variable para validar asignaciones a caracteres(ichr)
	public static int segunda = 0;
	// Tabla que almacenara los tokens declarados
	private static Hashtable tabla = new Hashtable();

	// Listas que guardaran los tipos compatibles de las variables
	private static ArrayList<Integer> intComp = new ArrayList();
	private static ArrayList<Integer> decComp = new ArrayList();
	private static ArrayList<Integer> strComp = new ArrayList();
	private static ArrayList<Integer> chrComp = new ArrayList();
	private static ArrayList<Integer> boolComp = new ArrayList();
	private static ArrayList<Funcion> tablaFuncion = new ArrayList();

	public static void InsertarFuncion(Token identificador, int tipo, Token par) {
		Funcion e = new Funcion();
		e.setIdentificador(identificador.image);
		e.setTipoDato(tipo);
		e.setParametros(par);
		tablaFuncion.add(e);
	}
	
	public static void VerificarFuncion(Token identificador, int tipo, Token par) {
		Funcion e = new Funcion();
		boolean aux=false;
		e.setIdentificador(identificador.image);
		e.setTipoDato(tipo);
		e.setParametros(par);
		for(Funcion x:tablaFuncion)
		{
			if(x.getIdentificador().equals(e.getIdentificador()))
			{ aux=true;
			break;}
		}
		if(aux==false)
		{
			System.out.println("Error Semántico: Linea: " + identificador.beginLine + " La funcion \" "
					+ identificador.image + "\" no ha sido declarada");
		}
		
	}

	// variable //tipoDato
	public static void InsertarSimbolo(Token identificador, int tipo) {

		if (tabla.containsKey(identificador.image)) {
			System.out.println("Error Semántico: Linea: " + identificador.beginLine + " El identificador \" "
					+ identificador.image + "\" ya ha sido declarado");
		} else {
			tabla.put(identificador.image, tipo);
		}

		// En este metodo se agrega a la tabla de tokens el identificador que
		// esta siendo declarado junto con su tipo de dato

	}
	
	

	public static void SetTables() {
		/*
		 * En este metodo se inicializan las tablas, las cuales almacenaran los
		 * tipo de datos compatibles con: entero = intComp decimal = decComp
		 * cadena = strComp caracter = chrComp boolean = boolComp
		 */

		// Compatibles con int = int, float, bool
		intComp.add(40); // int
		intComp.add(41); // float
		intComp.add(44); // bool
		intComp.add(45); // int
		intComp.add(48); // float
		intComp.add(46); // bool

		// Compatibles con float = int, float, bool

		decComp.add(40); // int
		decComp.add(41); // float
		decComp.add(44); // bool
		decComp.add(45); // int
		decComp.add(48); // float
		decComp.add(46); // bool

		// Compatibles con char = char
		chrComp.add(42); // char
		chrComp.add(50); // char

		// Compatibles con string = string
		strComp.add(43); // string
		strComp.add(49); // string

		// Compatibles con bool = bool, int, float
		boolComp.add(44); // bool
		boolComp.add(40); // int
		boolComp.add(41); // float
		boolComp.add(46); // bool
		boolComp.add(45); // int
		boolComp.add(48); // float
	}

	public static String checkAsing(Token TokenIzq, Token TokenAsig) {
		// variables en las cuales se almacenara el tipo de dato del
		// identificador y de las asignaciones (ejemplo: n1(tipoIdent1) =
		// 2(tipoIdent2) + 3(tipoIdent2))
		int tipoIdent1=0;
		int tipoIdent2=0;
		/*
		 * De la tabla obtenemos el tipo de dato del identificador asi como, si
		 * el token enviado es diferente a algun tipo que no se declara como los
		 * numeros(48), los decimales(50), caracteres(52) y cadenas(51) entonces
		 * tipoIdent1 = tipo_de_dato, ya que TokenAsig es un dato
		 */
		if (TokenIzq.kind != 45 && TokenIzq.kind != 48) {
			
			try {
				// Si el TokenIzq.image existe dentro de la tabla de tokens,
				// entonces tipoIdent1 toma el tipo de dato con el que
				// TokenIzq.image fue declarado
				tipoIdent1 = (Integer) tabla.get(TokenIzq.image);
			} catch (Exception e) {
				// Si TokenIzq.image no se encuentra en la tabla en la cual se
				// agregan los tokens, el token no ha sido declarado, y se manda
				// un error
				return "Error Semántico : El identificador " + TokenIzq.image + " No ha sido declarado \r\nLinea: "
						+ TokenIzq.beginLine;
			}
		} else
			tipoIdent1 = 0;

		// TokenAsig.kind != 48 && TokenAsig.kind != 50 && TokenAsig.kind != 51
		// && TokenAsig.kind != 52
		if (TokenAsig.kind == 47) {
			/*
			 * Si el tipo de dato que se esta asignando, es algun
			 * identificador(kind == 46) se obtiene su tipo de la tabla de
			 * tokens para poder hacer las comparaciones
			 */
			try {
				tipoIdent2 = (Integer) tabla.get(TokenAsig.image);
			} catch (Exception e) {
				// si el identificador no existe manda el error
				return "Error Semántico : El identificador " + TokenAsig.image + " No ha sido declarado \r\nLinea: "
						+ TokenIzq.beginLine;
			}
		}
		// Si el dato es entero(45) o decimal(47) o caracter(51) o cadena(52) o
		// bool (51)
		// tipoIdent2 = tipo_del_dato
		else if (TokenAsig.kind == 45 || TokenAsig.kind == 48 || TokenAsig.kind == 49 || TokenAsig.kind == 50
				|| TokenAsig.kind == 46)
			tipoIdent2 = TokenAsig.kind;
		else // Si no, se inicializa en algun valor "sin significado(con
				// respecto a los tokens)", para que la variable este
				// inicializada y no marque error al comparar
			tipoIdent2 = 0;

		if (tipoIdent1 == 40) // Int
		{
			// Si la lista de enteros(intComp) contiene el valor de tipoIdent2,
			// entonces es compatible y se puede hacer la asignacion
			if (intComp.contains(tipoIdent2))
				return " ";
			else // Si el tipo de dato no es compatible manda el error
				return "Error Semántico : No se puede convertir " + TokenAsig.image + " a Entero \r\nLinea: "
						+ TokenIzq.beginLine;
		} else if (tipoIdent1 == 41) // double
		{
			if (decComp.contains(tipoIdent2))
				return " ";
			else
				return "Error Semántico : No se puede convertir " + TokenAsig.image + " a Decimal \r\nLinea: "
						+ TokenIzq.beginLine;
		} else if (tipoIdent1 == 42) // char
		{
			/*
			 * variable segunda: cuenta cuantos datos se van a asignar al
			 * caracter: si a el caracter se le asigna mas de un dato (ej: 'a' +
			 * 'b') marca error NOTA: no se utiliza un booleano ya que entraria
			 * en asignaciones pares o impares
			 */
			segunda++;
			if (segunda < 2) {
				if (chrComp.contains(tipoIdent2))
					return " ";
				else
					return "Error Semántico : No se puede convertir " + TokenAsig.image + " a Caracter \r\nLinea: "
							+ TokenIzq.beginLine;
			} else // Si se esta asignando mas de un caracter manda el error
				return "Error: No se puede asignar mas de un valor a un caracter \r\nLinea: " + TokenIzq.beginLine;

		} else if (tipoIdent1 == 43) // string
		{
			if (strComp.contains(tipoIdent2))
				return " ";
			else
				return "Error Semántico : No se puede convertir " + TokenAsig.image + " a Cadena \r\nLinea: "
						+ TokenIzq.beginLine;
		} else if (tipoIdent1 == 44) // bool
		{
			if (boolComp.contains(tipoIdent2))
				return " ";
			else
				return "Error Semántico : No se puede convertir " + TokenAsig.image + " a Booleano \r\nLinea: "
						+ TokenIzq.beginLine;
		} else {
			return "El Identificador " + TokenIzq.image + " no ha sido declarado" + " Linea: " + TokenIzq.beginLine;
		}
	}

	/*
	 * Metodo que verifica si un identificador ha sido declarado, ej cuando se
	 * declaran las asignaciones: i++, i--)
	 */
	public static String checkVariable(Token checkTok) {
		try {
			// Intenta obtener el token a verificar(checkTok) de la tabla de los
			// tokens
			int tipoIdent1 = (Integer) tabla.get(checkTok.image);
			return " ";
		} catch (Exception e) {
			// Si no lo puede obtener, manda el error
			return "Error: El identificador " + checkTok.image + " No ha sido declarado \r\nLinea: "
					+ checkTok.beginLine;
		}
	}

	public static void visualizarTablas() {
		Enumeration e = tabla.keys();
		Object obj;
		System.out.printf("\n%10s%6s%5s%6s", "TABLA DE SIMBOLOS\n", "NOMBRE", " |", "TIPO\n");

		while (e.hasMoreElements()) {
			obj = e.nextElement();
			System.out.printf("%6s%5s%6s", obj, " :", tipo(obj) + "\n");
		}
	}

	public static void visualizarTablasFunciones() {
		System.out.printf("\n%10s%6s%5s%6s", "TABLA DE FUNCIONES\n", "NOMBRE", " |", "TIPO\n");
		for (Funcion x : tablaFuncion) {
			System.out.printf("%6s%5s%6s", x.getIdentificador(), " : ", tipos(x.getTipoDato()));
		}
	}

	static String tipo(Object o) {
		String nombre = "";
		if (tabla.get(o).equals(40)) {
			nombre = "Int";
		}
		if (tabla.get(o).equals(41)) {
			nombre = "Float";
		}
		if (tabla.get(o).equals(42)) {
			nombre = "Char";
		}
		if (tabla.get(o).equals(43)) {
			nombre = "String";
		}
		if (tabla.get(o).equals(44)) {
			nombre = "Bool";
		}
		return nombre;
	}

	static String tipos(int o) {
		String nombre = "";
		if (o == 40) {
			nombre = "Int";
		}
		if (o == 41) {
			nombre = "Float";
		}
		if (o == 42) {
			nombre = "Char";
		}
		if (o == 43) {
			nombre = "String";
		}
		if (o == 44) {
			nombre = "Bool";
		}
		return nombre;

	}

}
