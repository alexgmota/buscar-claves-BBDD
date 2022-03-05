import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BuscadorClaves {
    List<DependenciaFuncional> dependencias;
    List<String> atributos;
    List<String> atributosImprescindibles;
    List<String> atributosPosibles;

    public BuscadorClaves() {
        dependencias = new ArrayList<>();
        atributos = new ArrayList<>();
    }

    public List<String> buscarClaves(){
        List<String> claves = new ArrayList<>();
        cargarDatos();
        clasificarAtributos();
        return claves;
    }

    private void cargarDatos() {
        File file = new File("dependencias.txt");
        Scanner sc;
        try {
            sc = new Scanner(file);
            leerArchivo(sc);
            comprobarDF();
        } catch (FileNotFoundException e) {
            System.out.println("No se ha encontrado el archivo.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void leerArchivo(Scanner sc) throws Exception {
        String [] candidatos = sc.nextLine().split(" ");
        for (String aux : candidatos)
            if (!aux.isBlank())
                atributos.add(aux);
        while (sc.hasNext())
            dependencias.add(new DependenciaFuncional(sc.nextLine()));
    }

    private void comprobarDF() throws Exception {
        for (DependenciaFuncional df : dependencias) {
            for (String implicante : df.getImplicantes()) {
                if (!atributos.contains(implicante))
                    throw new Exception("Los atributos no coinciden con las dependencias");
            }
            for (String implicado : df.getImplicados()) {
                if (!atributos.contains(implicado))
                    throw new Exception("Los atributos no coinciden con las dependencias");
            }
        }
    }

    private void clasificarAtributos(){

    }

    private boolean isInDF(String atributo) {
        return false;
    }
}
