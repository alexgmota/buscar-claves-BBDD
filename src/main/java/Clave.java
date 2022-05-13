import java.util.*;

public class Clave {

    private final List<String> clave;

    public Clave() {
        clave = new ArrayList<>();
    }

    public Clave(List<String> atributos) {
        clave = new ArrayList<>(atributos);
    }
    public void addAtributo(String atributo) {
        clave.add(atributo);
    }


    public List<String> getClave() {
        return clave;
    }

    public boolean contains(String atributo) {
        return clave.contains(atributo);
    }

    public boolean containsAll(Clave clave) {
        return this.clave.containsAll(clave.getClave());
    }

    @Override
    public String toString() {
        return Arrays.toString(clave.toArray())
                .replace('[',' ')
                .replace(']',' ')
                .replaceAll(", ", "");
    }

    public boolean esClave(List<String> atributos, List<DependenciaFuncional> dependencias) {
        List<String> cierre = new ArrayList<>();
        calcularCierre(cierre, dependencias);
        boolean esCierre = cierre.size() == atributos.size();
        Gui.imprimirCierre(clave, cierre, esCierre);
        return esCierre;
    }

    private void calcularCierre(List<String> cierre, List<DependenciaFuncional> dependencias) {
        cierre.addAll(clave);
        for (int i = 0; i < dependencias.size(); i++) {
            DependenciaFuncional df = dependencias.get(i);
            if (df.contieneImplicante(cierre) && addImplicados(cierre, df)) {
                i = -1;
            }
        }
    }

    private boolean addImplicados(List<String> cierre, DependenciaFuncional df) {
        for (String aux : df.getImplicados()){
            if (!cierre.contains(aux)) {
                cierre.add(aux);
               return true;
            }
        }
        return false;
    }
}