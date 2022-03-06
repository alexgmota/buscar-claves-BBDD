import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

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

    public List<List<String>> buscarClaves(){
        List<List<String>> claves = new ArrayList<>();
        cargarDatos();
        clasificarAtributos();
        buscarClaves(claves);
        return claves;
    }

    private void buscarClaves(List<List<String>> claves) {
        List<String> atributosNecesarios = new ArrayList<>();
        atributosNecesarios.addAll(atributosNoDF);
        atributosNecesarios.addAll(atributosSoloImplicantes);
        if (esClave(atributosNecesarios))
            claves.add(atributosNecesarios);
        else {
            buscarCombinacionesAtributosPosibles(claves, atributosNecesarios);
        }
        System.out.println();
    }

    private void cargarDatos() {
        File file = new File("dependencias.txt");
        Scanner sc;
        try {
            sc = new Scanner(file);
            leerArchivo(sc);
            sc.close();
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
        System.out.println();
    }

    private boolean esClave(List<String> candidatos) {
        List<String> cierre = new ArrayList<>();
        calcularCierre(candidatos, cierre);
        return cierre.size() == atributos.size();
    }

    private void calcularCierre(List<String> candidatos, List<String> cierre) {

        cierre.addAll(candidatos);
            for (int i = 0; i < dependencias.size(); i++) {
                DependenciaFuncional df = dependencias.get(i);
                if (df.contieneImplicante(cierre))
                    for (String aux : df.getImplicados()){
                        if (!cierre.contains(aux)) {
                            i = 0;
                            cierre.add(aux);
                        }
                    }

            }
        imprimirCierre(candidatos, cierre);
    }

    private void imprimirCierre(List<String> candidatos, List<String> cierre) {
        System.out.print((Arrays.toString(candidatos.toArray())
                        + "+ = " +
                        Arrays.toString(cierre.toArray()))
                            .replace('[','(')
                            .replace(']',')')
                            .replaceAll(", ", "")
        );
        if (atributos.size() == cierre.size()) System.out.println("  == T");
        else System.out.println("  != T");
    }

    private void buscarCombinacionesAtributosPosibles(List<List<String>> claves, List<String> atributosNecesarios) {
        Queue<List<String>> posiblesCombinaciones = new LinkedList<List<String>>();
        for (String atributo : atributosImplicadosImplicantes){
            List<String> aux = new ArrayList<String>(atributosNecesarios);
            aux.add(atributo);
            posiblesCombinaciones.add(aux);
        }
        while (posiblesCombinaciones.size() > 0){
            List<String> posibleClave = posiblesCombinaciones.poll();
            if (esClave(posibleClave)) claves.add(posibleClave);
        }
    }
}
