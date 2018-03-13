package eu.optique.r2rml.api.model;


import org.apache.commons.rdf.api.IRI;

import java.util.ArrayList;


/**
 * RMLC Function
 *
 * @author David Chaves-Fraga
 */

public interface TransFunct extends MappingComponent {


    void setFunctions(ArrayList<String> functions);

    void setColumnFunctions (ArrayList<ColumnFunction> columnFunctions);

    ArrayList<String> getFunction();

    ArrayList<ColumnFunction> getColumnFunctions();


}
