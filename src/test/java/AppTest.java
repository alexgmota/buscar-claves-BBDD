import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class AppTest
{
    @Test
    public void testEj6 (){
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

    @Test
    public void testEj10() {
        BuscadorClaves bc = new BuscadorClaves();
        List<Clave> solucion = bc.buscarClaves("dependenciasEj10.txt");

        List<Clave> solEsperada =  new ArrayList<>();
        Clave clave1 = new Clave();
        clave1.addAtributo("G");
        clave1.addAtributo("A");
        clave1.addAtributo("B");

        Clave clave2 = new Clave();
        clave2.addAtributo("G");
        clave2.addAtributo("A");
        clave2.addAtributo("E");

        Clave clave3 = new Clave();
        clave3.addAtributo("G");
        clave3.addAtributo("B");
        clave3.addAtributo("C");

        Clave clave4 = new Clave();
        clave4.addAtributo("G");
        clave4.addAtributo("C");
        clave4.addAtributo("E");

        solEsperada.add(clave1);
        solEsperada.add(clave2);
        solEsperada.add(clave3);
        solEsperada.add(clave4);

        assertEquals(solEsperada.size(), solucion.size());
        for (int i = 0; i < solEsperada.size(); i++)
            assertEquals(solEsperada.get(i).getClave(), solucion.get(i).getClave());
    }
}
