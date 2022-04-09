import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class AppTest
{
    @Test
    public void testEj6 () {
        BuscadorClaves bc = new BuscadorClaves();
        List<Clave> solucion = bc.buscarClaves("dependenciasEj6.txt");

        List<Clave> solEsperada =  new ArrayList<>();
        Clave clave1 = new Clave();
        clave1.addAtributo("A");
        clave1.addAtributo("B");

        Clave clave2 = new Clave();
        clave2.addAtributo("A");
        clave2.addAtributo("C");
        clave2.addAtributo("D");

        solEsperada.add(clave1);
        solEsperada.add(clave2);

        assertEquals(solEsperada.size(), solucion.size());
        for (int i = 0; i < solEsperada.size(); i++)
            assertEquals(solEsperada.get(i).getClave(), solucion.get(i).getClave());
    }
}
