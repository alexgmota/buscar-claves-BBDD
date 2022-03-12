import java.io.File;
import java.io.FileNotFoundException;
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

    public List<List<String>> buscarClaves(String fichero){
        List<List<String>> claves = new ArrayList<>();
        cargarDatos(fichero);
        clasificarAtributos();
        probarClaves(claves);
        return claves;
    }

    private void probarClaves(List<List<String>> claves) {
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

    private void cargarDatos(String fichero) {
        File file = new File(fichero);
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
        String supIzq = Arrays.toString(atributosNoDF.toArray());
        String infIzq = Arrays.toString(atributosSoloImplicados.toArray());
        String supDcha = Arrays.toString(atributosSoloImplicantes.toArray());
        String infDcha = Arrays.toString(atributosImplicadosImplicantes.toArray());

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
                            i = -1;
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
        Queue<List<String>> posiblesCombinaciones = new LinkedList<>();
        List<String> atributosPosibles = new ArrayList<>(atributosImplicadosImplicantes);

        for (String atributo : atributosPosibles){
            List<String> aux = new ArrayList<>(atributosNecesarios);
            aux.add(atributo);
            posiblesCombinaciones.add(aux);
        }
        while (posiblesCombinaciones.size() > 0){
            List<String> posibleClave = posiblesCombinaciones.poll();
            if (esClave(posibleClave)) {
                claves.add(posibleClave);
                atributosPosibles.removeAll(posibleClave);
            }
        }
        buscarCombinacionesAtributosPosibles2(claves, atributosNecesarios, atributosPosibles);
    }

    private void buscarCombinacionesAtributosPosibles2(List<List<String>> claves, List<String> atributosNecesarios,
                                                       List<String> atributosPosibles) {
        Queue<List<String>> posiblesCombinaciones = new LinkedList<>();

        for (String atributo : atributosPosibles){
            for (String atributo2 : atributosPosibles) {
                if (!atributo2.equals(atributo)){
                    List<String> aux = new ArrayList<>(atributosNecesarios);
                    aux.add(atributo);
                    aux.add(atributo2);
                    if (!estaEnClaves(posiblesCombinaciones, aux))
                        posiblesCombinaciones.add(aux);
                }
            }
        }
        while (posiblesCombinaciones.size() > 0){
            List<String> posibleClave = posiblesCombinaciones.poll();
            if (esClave(posibleClave)) {
                claves.add(posibleClave);
                atributosPosibles.removeAll(posibleClave);
            }
        }
    }

    private boolean estaEnClaves(Queue<List<String>> claves, List<String> posibleClave) {
        for (List<String> clave : claves)
            if (clave.containsAll(posibleClave))
                return true;
        return false;
    }
}
