package es.uji.al435152_al449836.modelo.lecturas;

import es.uji.al435152_al449836.modelo.datos.RowWithLabel;
import es.uji.al435152_al449836.modelo.datos.TableWithLabels;

import java.util.ArrayList;
import java.util.List;

/**
 * Lector de CSV cuya ultima columna actua como etiqueta.
 *
 * <p>Este formato es el que necesita KNN, porque separa claramente atributos
 * numericos y clase conocida de cada fila.
 */
public class CSVLabeledFileReader extends FileReader<TableWithLabels> {
    /**
     * Construye el lector para el recurso indicado.
     */
    public CSVLabeledFileReader(String source) {
        super(source);
    }

    /**
     * Procesa las cabeceras numericas y guarda aparte el nombre de la etiqueta.
     */
    @Override
    public void processHeaders(String header) {
        table = new TableWithLabels();
        List<String> headers = new ArrayList<>();
        String[] partes = header.split(",");

        for (int i = 0; i < partes.length - 1; i++) {
            headers.add(partes[i]);
        }

        table.setHeaders(headers);
        table.setHeaderLabel(partes[partes.length - 1]);
    }

    /**
     * Convierte una linea CSV en una fila etiquetada y la anade a la tabla.
     */
    @Override
    public void processData(String data) {
        String[] partes = data.split(",");
        List<Double> valores = new ArrayList<>();

        for (int i = 0; i < partes.length - 1; i++) {
            valores.add(Double.parseDouble(partes[i]));
        }

        String label = partes[partes.length - 1];
        RowWithLabel fila = new RowWithLabel(valores, label);
        table.addRow(fila);
    }
}
