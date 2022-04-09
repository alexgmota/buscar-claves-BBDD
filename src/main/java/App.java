import java.util.Arrays;
import java.util.List;

public class App
{
    public static void main( String[] args ) {
        BuscadorClaves bc = new BuscadorClaves();
        List<Clave> claves = bc.buscarClaves("dependencias.txt");
        System.out.println("Las claves son: ");
        for (Clave clave : claves) {
            System.out.println(" - " + clave.toString());
        }
    }
}
