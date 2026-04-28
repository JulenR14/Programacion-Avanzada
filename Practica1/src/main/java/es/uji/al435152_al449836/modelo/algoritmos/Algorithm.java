package es.uji.al435152_al449836.modelo.algoritmos;

import es.uji.al435152_al449836.modelo.datos.Table;

public interface Algorithm<T extends Table,U,W>{
    void train(T table);
    W estimate(U lista);
}
