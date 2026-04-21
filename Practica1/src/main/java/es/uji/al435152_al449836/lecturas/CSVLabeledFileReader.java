package es.uji.al435152_al449836.lecturas;

import es.uji.al435152_al449836.datos.RowWithLabel;
import es.uji.al435152_al449836.datos.TableWithLabels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CSVLabeledFileReader extends FileReader <TableWithLabels> {
    public CSVLabeledFileReader(String source) {
        super(source);
    }

    @Override
    public void processHeaders(String header) {
        List<String> headers = new ArrayList<>();
        String[] partes = header.split(",");

        for (int i = 0; i < partes.length - 1; i++) {
            headers.add(partes[i]);
        }

        table.setHeaders(headers);
    }

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
