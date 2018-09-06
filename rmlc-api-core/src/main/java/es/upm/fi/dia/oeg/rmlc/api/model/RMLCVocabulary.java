package es.upm.fi.dia.oeg.rmlc.api.model;




/**
 * RMLC Vocabulary
 *
 * @author David Chaves-Fraga
 */


public class RMLCVocabulary {

    // the RMLC namespace
    public static final String NAMESPACE = "http://www.ex.org/ns/rmlc#";
    public static final String NAMESPACE_FUNCTIONS = "http://www.ex.org/ns/rmlcf##";

    // RMLC types
    public static final String TYPE_JOIN = NAMESPACE + "joinCondition";

    //RMLC properties

    public static final String PROP_PARENT = NAMESPACE + "parent";
    public static final String PROP_CHILD = NAMESPACE + "child";
    public static final String PROP_COLUMNS = NAMESPACE + "columns";
    public static final String PROP_FUNCTIONS = NAMESPACE + "functions";

    public static final String PROP_TEMPLATE = NAMESPACE + "template";
    public static final String PROP_CHILD_FUNCTION = NAMESPACE_FUNCTIONS + "childFunctions";
    public static final String PROP_PARENT_FUNCTION = NAMESPACE_FUNCTIONS + "parentFunctions";


}
