package es.upm.fi.dia.oeg.rmlc.api.model;




/**
 * RMLC Vocabulary
 *
 * @author David Chaves-Fraga
 */


public class RMLCVocabulary {

    // the RMLC namespace
    public static final String NAMESPACE = "http://www.oeg-upm.net/ns/rmlc#";
    public static final String RML = "ttp://semweb.mmlab.be/ns/rml#";

    // RMLC types
    public static final String TYPE_FUNCTION = NAMESPACE + "TransFunction";
    public static final String TYPE_COLUMN_FUNCTION = NAMESPACE + "ColumnFunction";


    //RMLC properties

    public static final String PROP_PARENT = NAMESPACE + "parent";
    public static final String PROP_CHILD = NAMESPACE + "child";
    public static final String PROP_GENERAL_TRANS_FUNCTION = NAMESPACE + "generalTransFunct";
    public static final String PROP_INDIVIDUAL_TRANS_FUNCTION = NAMESPACE + "individualTransFunct";
    public static final String PROP_COLUMN_FUNCTION = NAMESPACE + "columnFunction";

    //RML properties
    public static final String PROP_SOURCE = RML + "source";

}
