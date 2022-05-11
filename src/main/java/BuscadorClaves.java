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
    List<Clave> claves;

    public BuscadorClaves() {
        dependencias = new ArrayList<>();
        atributos = new ArrayList<>();
        atributosNoDF = new ArrayList<>();
        atributosSoloImplicantes = new ArrayList<>();
        atributosImplicadosImplicantes = new ArrayList<>();
        atributosSoloImplicados = new ArrayList<>();
        claves = new ArrayList<>();
    }

    public List<String> getAtributosNoDF() {
        return atributosNoDF;
    }

    public List<String> getAtributosSoloImplicantes() {
        return atributosSoloImplicantes;
    }

    public List<String> getAtributosImplicadosImplicantes() {
        return atributosImplicadosImplicantes;
    }

    public List<String> getAtributosSoloImplicados() {
        return atributosSoloImplicados;
    }

    public List<Clave> buscarClaves(String fichero) {
        cargarDatos(fichero);
        clasificarAtributos();
        probarClaves();
        return this.claves;
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
            comprobarImplicantes(df.getImplicantes());
            comprobarImplicados(df.getImplicados());
        }
    }

    private void comprobarImplicados(List<String> implicados) throws Exception {
        for (String implicado : implicados) {
            if (!atributos.contains(implicado))
                throw new Exception("Los atributos no coinciden con las dependencias");
        }
    }

    private void comprobarImplicantes(List<String> implicantes) throws Exception {
        for (String implicante : implicantes) {
            if (!atributos.contains(implicante))
                throw new Exception("Los atributos no coinciden con las dependencias");
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
        Gui.imprimirTabla(this);
    }

    private void probarClaves() {
        List<String> atributosNecesarios = new ArrayList<>();
        atributosNecesarios.addAll(atributosNoDF);
        atributosNecesarios.addAll(atributosSoloImplicantes);
        Clave posibleClave = new Clave(atributosNecesarios);
        if (posibleClave.esClave(atributos, dependencias))
            claves.add(new Clave(atributosNecesarios));
        else {
            List<Clave> candidatos = new ArrayList<>();
            candidatos.add(new Clave(atributosNecesarios));
            buscarCombinaciones(candidatos, new ArrayList<>(atributosImplicadosImplicantes), 1);
        }
    }

    private void buscarCombinaciones(List<Clave> candidatos, List<String> atributosPosibles, int nivel) {
        List<Clave> sigCandidatos = new ArrayList<>();
        List<String> sigAtributosPosibles = new ArrayList<>(atributosPosibles);
        for(Clave candidata : candidatos) {
            for (String atributo : atributosPosibles){
                Clave aux = new Clave(candidata.getClave());
                if (!aux.contains(atributo)){
                    aux.addAtributo(atributo);
                    if (noEstaEnClaves(claves, aux) && noEstaEnClaves(sigCandidatos, aux)
                            && aux.esClave(atributos, dependencias)) {
                        claves.add(aux);
                        sigAtributosPosibles.removeAll(aux.getClave());
                    } else if (noEstaEnClaves(sigCandidatos, aux)){
                        sigCandidatos.add(aux);
                    }
                }
            }
        }
        if (nivel < 2)
            buscarCombinaciones(sigCandidatos, sigAtributosPosibles, nivel + 1);
    }

    private boolean noEstaEnClaves(List<Clave> claves, Clave posibleClave) {
        for (Clave clave : claves)
            if (clave.containsAll(posibleClave))
                return false;
        return true;
    }
}
