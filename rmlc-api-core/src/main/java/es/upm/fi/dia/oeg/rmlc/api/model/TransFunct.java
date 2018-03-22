package es.upm.fi.dia.oeg.rmlc.api.model;


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
