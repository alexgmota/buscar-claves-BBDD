import java.util.Arrays;
import java.util.List;

public class Gui {
    public static void imprimirTabla(BuscadorClaves bc) {
        String supIzq = Arrays.toString(bc.getAtributosNoDF().toArray());
        String infIzq = Arrays.toString(bc.getAtributosSoloImplicados().toArray());
        String supDcha = Arrays.toString(bc.getAtributosSoloImplicantes().toArray());
        String infDcha = Arrays.toString(bc.getAtributosImplicadosImplicantes().toArray());

        int max = Math.max(supIzq.length(), infIzq.length());
        int tam = Math.max(supIzq.length()+ supDcha.length(),
                infIzq.length() + infDcha.length());

        System.out.print(supIzq);
        for (int i = supIzq.length(); i < max; i++) System.out.print(" ");
        System.out.print(" | ");
        System.out.println(supDcha);
        for (int i = 0; i <= tam; i++)
            if (i == max) System.out.print("-+-");
            else System.out.print("-");
        System.out.println();
        System.out.print(infIzq);
        for (int i = infIzq.length(); i < max; i++) System.out.print(" ");
        System.out.print(" | ");
        System.out.println(infDcha);
        System.out.println();
    }

    public static void imprimirCierre(List<String> clave, List<String> cierre, boolean esCierre) {
        System.out.print((Arrays.toString(clave.toArray())
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
