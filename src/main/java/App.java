import java.util.Arrays;
import java.util.List;

public class App
{
    public static void main( String[] args ) {
        BuscadorClaves bc = new BuscadorClaves();
        List<List<String>> claves = bc.buscarClaves("dependencias.txt");
        System.out.println("Las claves son: ");
        for (List<String> clave : claves) {
            System.out.println(" -" + Arrays.toString(clave.toArray())
                    .replace('[',' ')
                    .replace(']',' ')
                    .replaceAll(", ", "")
            );
        }
    }
}
