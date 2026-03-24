package es.uji.al435152_al449836.datos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableWithLabels extends Table{
    //Atributo que guarda para cada etiqueta su identificador
    private Map<String, Integer> labelsToIndex;
    private String headerLabel;

    //Constructor
    public TableWithLabels(){
        super();
        labelsToIndex = new HashMap<>();
    }

    //Getter que devuelve la fila (Parseada) que se pasa como parametro
    @Override
    public RowWithLabel getRowAt(int row) {
        return (RowWithLabel) super.getRowAt(row);
    }

    //Getter que devuelve el identificador de la etiqueta
    public Integer getLabelAsInteger(String label){
        return labelsToIndex.get(label);
    }

    //Función encargada de agregar fila con etiqueta a la tabla
    public void addRow(Row row) {
        super.addRow(row);
        //Parsea a una row con etiqueta
        RowWithLabel rowW = (RowWithLabel) row;
        //En el caso de que la etiqueta aun no exista, se agrega con un nuevo identificador.
        if (!labelsToIndex.containsKey(rowW.getLabel())){
            labelsToIndex.put(rowW.getLabel(), labelsToIndex.size());
        }
    }

    public void setHeaderLabel(String headerLabel){
        this.headerLabel = headerLabel;
    }
}
