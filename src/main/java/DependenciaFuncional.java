import java.util.ArrayList;
import java.util.List;

public class DependenciaFuncional {
    private final List<String> implicados;
    private final List<String> implicantes;

    public DependenciaFuncional(String df) throws Exception{
        implicados = new ArrayList<>();
        implicantes = new ArrayList<>();

        String[] vdf = df.split("->");
        if (vdf.length != 2) throw new Exception("Dependencia funcional incorrecta");
        aniadir(implicados, vdf[0].split(" "));
        aniadir(implicantes, vdf[1].split(" "));
    }

    private void aniadir (List<String> lista, String[] candidatos){
        for (String aux : candidatos)
            if (!aux.isBlank()) lista.add(aux);
    }

    public List<String> getImplicados() {
        return implicados;
    }

    public List<String> getImplicantes() {
        return implicantes;
    }
}
