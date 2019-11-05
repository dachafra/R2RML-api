package es.upm.fi.dia.oeg.rmlc.api.model;




/**
 * RMLC Vocabulary
 *
 * @author David Chaves-Fraga
 */


public class RMLCVocabulary {

    // the RML[C] namespace
    public static final String NAMESPACE = "http://www.ex.org/ns/rmlc#";
    public static final String RML_NAMESPACE="http://semweb.mmlab.be/ns/rml#";

    // RML[C] types
    public static final String TYPE_JOIN = NAMESPACE + "joinCondition";
    public static final String TYPE_LOGICALSOURCE = RML_NAMESPACE+"logicalSource";

    //RML[C] properties
    public static final String PROP_PARENT = NAMESPACE + "parent";
    public static final String PROP_CHILD = NAMESPACE + "child";
    public static final String PROP_FUNCTIONS = NAMESPACE + "function";
    public static final String PROP_SOURCE = RML_NAMESPACE + "source";
    public static final String PROP_REFERENCE_FORM = RML_NAMESPACE + "referenceFormulation";
    public static final String PROP_REFERENCE = RML_NAMESPACE + "reference";




}
