import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

    public void add(List<String> atributos) {
        clave.addAll(atributos);
    }

    public List<String> getClave() {
        return clave;
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
        imprimirCierre(cierre, atributos);
        return cierre.size() == atributos.size();
    }

    private void calcularCierre(List<String> cierre, List<DependenciaFuncional> dependencias) {

        cierre.addAll(clave);
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
    }

    private void imprimirCierre(List<String> cierre, List<String> atributos) {
        System.out.print((Arrays.toString(clave.toArray())
                + "+ = " +
                Arrays.toString(cierre.toArray()))
                .replace('[', '(')
                .replace(']', ')')
                .replaceAll(", ", "")
        );
        if (atributos.size() == cierre.size()) System.out.println("  == T");
        else System.out.println("  != T");
    }

}