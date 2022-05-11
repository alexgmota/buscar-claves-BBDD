import java.util.Arrays;
import java.util.List;

public class Gui {
    public static void imprimirTabla(BuscadorClaves bc) {
        String supIzq = Arrays.toString(bc.getAtributosNoDF().toArray());
        String infIzq = Arrays.toString(bc.getAtributosSoloImplicados().toArray());
        String supDcha = Arrays.toString(bc.getAtributosSoloImplicantes().toArray());
        String infDcha = Arrays.toString(bc.getAtributosImplicadosImplicantes().toArray());

        int mitad = Math.max(supIzq.length(), infIzq.length());
        int tam = Math.max(supIzq.length() + supDcha.length(),
                infIzq.length() + infDcha.length());
        System.out.println();
        imprimirFila(supIzq, supDcha, mitad);
        imprimirLineaHorizontal(mitad, tam);
        imprimirFila(infIzq, infDcha, mitad);
        System.out.println();
    }

    private static void imprimirFila(String izq, String dcha, int mitad) {
        System.out.print(izq);
        for (int i = izq.length(); i < mitad; i++) System.out.print(" ");
        System.out.print(" | ");
        System.out.println(dcha);
    }

    private static void imprimirLineaHorizontal(int mitad, int tam) {
        for (int i = 0; i <= tam; i++)
            if (i == mitad) System.out.print("-+-");
            else System.out.print("-");
        System.out.println();
    }

    public static void imprimirCierre(List<String> clave, List<String> cierre, boolean esCierre) {
        System.out.print(
                (Arrays.toString(clave.toArray())
                + "+ = " +
                Arrays.toString(cierre.toArray()))
                .replace('[', '(')
                .replace(']', ')')
                .replaceAll(", ", "")
        );
        if (esCierre) System.out.println("  == T");
        else System.out.println("  != T");
    }
}
