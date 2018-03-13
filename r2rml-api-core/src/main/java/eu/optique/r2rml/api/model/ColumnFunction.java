package eu.optique.r2rml.api.model;

import org.apache.commons.rdf.api.IRI;

import java.util.ArrayList;


/**
 * RMLC ColumnFunction
 *
 * @author David Chaves-Fraga
 */

public interface ColumnFunction {


    void setFunctions(ArrayList<String> functions);

    void setColumnName (String columnName);

    void addFunction (String function);

    ArrayList<String> getFunction();

    String getColumnName();
}
