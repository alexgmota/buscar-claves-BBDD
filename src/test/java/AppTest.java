import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class AppTest
{
    @Test
    public void testEj6 () {
        BuscadorClaves bc = new BuscadorClaves();
        List<List<String>> solucion = bc.buscarClaves("dependenciasEj6.txt");

        List<List<String >> solEsperada =  new ArrayList<>();
        List<String> clave1 = new ArrayList<>();
        clave1.add("A");
        clave1.add("B");

        List<String> clave2 = new ArrayList<>();
        clave2.add("A");
        clave2.add("C");
        clave2.add("D");

        solEsperada.add(clave1);
        solEsperada.add(clave2);

        assertEquals(solEsperada, solucion);
    }
}
