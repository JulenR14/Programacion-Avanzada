package es.uji.al435152_al449836.lecturas;

import es.uji.al435152_al449836.datos.RowWithLabel;
import es.uji.al435152_al449836.datos.TableWithLabels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CSVLabeledFileReader extends FileReader <TableWithLabels> {
    public CSVLabeledFileReader(String source) {
        // Igual que en el lector sin etiquetas, la fuente se pasa a la clase madre.
        super(source);
    }

    @Override
    public void processHeaders(String header) {
        // Creamos una tabla preparada para filas que llevan etiqueta.
        table = new TableWithLabels();
        List<String> headers = new ArrayList<>();
        String[] partes = header.split(",");

        // En este CSV, la última columna no es un dato normal:
        // es la etiqueta, así que no la metemos como cabecera numérica.
        for (int i = 0; i < partes.length - 1; i++) {
            headers.add(partes[i]);
        }

        table.setHeaders(headers);
        // Guardamos aparte el nombre de la columna de etiquetas.
        table.setHeaderLabel(partes[partes.length - 1]);
    }

    @Override
    public void processData(String data) {
        // Separamos la línea en columnas.
        String[] partes = data.split(",");
        List<Double> valores = new ArrayList<>();

        // Todas las columnas menos la última son valores numéricos.
        for (int i = 0; i < partes.length - 1; i++) {
            valores.add(Double.parseDouble(partes[i]));
        }

        // La última posición de la línea es la etiqueta de la fila.
        String label = partes[partes.length - 1];

        // Construimos la fila etiquetada y la añadimos a la tabla.
        RowWithLabel fila = new RowWithLabel(valores, label);
        table.addRow(fila);
    }
}
