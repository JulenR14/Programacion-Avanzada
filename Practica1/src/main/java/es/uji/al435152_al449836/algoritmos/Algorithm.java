package es.uji.al435152_al449836.algoritmos;

import es.uji.al435152_al449836.datos.Table;
import es.uji.al435152_al449836.datos.TableWithLabels;

import java.util.Collection;
import java.util.List;

public interface Algorithm<T extends Table,U,W>{
    void train(T table);
    W estimate(U lista);
}
