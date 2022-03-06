import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BuscadorClaves {
    List<DependenciaFuncional> dependencias;
    List<String> atributos;
    List<String> atributosNoDF;
    List<String> atributosSoloImplicantes;
    List<String> atributosImplicadosImplicantes;
    List<String> atributosSoloImplicados;

    public BuscadorClaves() {
        dependencias = new ArrayList<>();
        atributos = new ArrayList<>();
        atributosNoDF = new ArrayList<>();
        atributosSoloImplicantes = new ArrayList<>();
        atributosImplicadosImplicantes = new ArrayList<>();
        atributosSoloImplicados = new ArrayList<>();
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
        if (!sc.hasNext()) throw new Exception("Archivo dependencias.txt vacio");
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
        boolean esImplicante, esImplicado;
        for (String atributo : atributos) {
            esImplicante = false;
            esImplicado = false;
            for (DependenciaFuncional df : dependencias) {
                if (df.isImplicado(atributo)) esImplicado = true;
                if (df.isImplicante(atributo)) esImplicante = true;
                if (esImplicado && esImplicante) break;
            }
            if (esImplicado && esImplicante) atributosImplicadosImplicantes.add(atributo);
            else if (esImplicante) atributosSoloImplicantes.add(atributo);
            else if (esImplicado) atributosSoloImplicados.add(atributo);
            else atributosNoDF.add(atributo);
        }
        imprimirTabla();
    }

    private void imprimirTabla() {
        int max = Math.max(atributosNoDF.size(), atributosSoloImplicados.size());
        int tam = Math.max(atributosNoDF.size() + atributosSoloImplicantes.size(),
                atributosSoloImplicados.size() + atributosImplicadosImplicantes.size());

        System.out.print(Arrays.toString(atributosNoDF.toArray()));
        for (int i = atributosNoDF.size(); i < max-1; i++) System.out.print("   ");
        if (atributosNoDF.size() == 0) System.out.print(" ");
        System.out.print(" | ");
        System.out.println(Arrays.toString(atributosSoloImplicantes.toArray()));
        for (int i = 0; i <= tam; i++)
            if (i == max) System.out.print("-+-");
            else System.out.print("---");
        System.out.println();
        System.out.print(Arrays.toString(atributosSoloImplicados.toArray()));
        for (int i = atributosSoloImplicados.size(); i < max-1; i++) System.out.print("   ");
        if (atributosSoloImplicados.size() == 0) System.out.print(" ");
        System.out.print(" | ");
        System.out.println(Arrays.toString(atributosImplicadosImplicantes.toArray()));
    }
}
