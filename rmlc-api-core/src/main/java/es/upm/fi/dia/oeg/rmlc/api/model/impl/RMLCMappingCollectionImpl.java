/*******************************************************************************
 * Copyright 2013, the Optique Consortium
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This first version of the R2RML API was developed jointly at the University of Oslo, 
 * the University of Bolzano, La Sapienza University of Rome, and fluid Operations AG, 
 * as part of the Optique project, www.optique-project.eu
 ******************************************************************************/
package es.upm.fi.dia.oeg.rmlc.api.model.impl;

import java.util.*;

import es.upm.fi.dia.oeg.rmlc.api.RMLCMappingManager;
import es.upm.fi.dia.oeg.rmlc.api.model.*;
import es.upm.fi.dia.oeg.rmlc.api.MappingFactory;
import org.apache.commons.rdf.api.BlankNodeOrIRI;
import org.apache.commons.rdf.api.Graph;
import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.api.Literal;
import org.apache.commons.rdf.api.RDF;
import org.apache.commons.rdf.api.RDFTerm;
import org.apache.commons.rdf.api.Triple;

import static java.util.stream.Collectors.toSet;

/**
 * The class representing a collection of TriplesMaps, which is generated from a
 * given graph.
 * 
 * @author Timea Bagosi
 * @author Martin G. Skjæveland
 * @author David Chaves-Fraga
 */
public class RMLCMappingCollectionImpl implements RMLCMappingCollection {

	// the set of generated mappings, using the subject node as key.
	private Map<BlankNodeOrIRI, TriplesMap> triplesMaps;

	// the value factory to create URIs
	private RDF rdf;
	private MappingFactory mfact;

	// the rdf graph to read the mappings from
	private Graph graph = null;

	/**
	 * @param mm
	 *            - The R2RMLMappingManager that is used to get a mapping
	 *            factory.
	 * @param rdf
	 *            - The LibConfiguration of the R2RMLMappingManager.
	 * @param graph
	 *            - The graph to generate TriplesMaps from.
	 * @throws InvalidRMLCMappingException
	 *             if found and invalid mapping
	 */
	public RMLCMappingCollectionImpl(RMLCMappingManager mm,
                                      RDF rdf, Graph graph)
			throws InvalidRMLCMappingException {
		this.rdf = rdf;
		mfact = mm.getMappingFactory();
		load(graph);
	}

    @Override
	public void addTriplesMap(TriplesMap mapping) {
		triplesMaps.put(mapping.getNode(), mapping);
	}

	@Override
	public void addTriplesMaps(Collection<TriplesMap> mappings) {
		for (TriplesMap map : mappings) {
			addTriplesMap(map);
		}
	}

	@Override
	public void load(Graph graph) throws InvalidRMLCMappingException {
		if (graph == null)
			throw new NullPointerException(
					"The RDF Graph supported for the mapping manager must not be null.");
		this.graph = graph;
		
		// Read all tripleMaps, ignoring predicate-object maps.
		triplesMaps = readTriplesMaps();
		
		// Add predicate-object maps to the tripleMaps.
		// This is done after all triplesmaps are created to ensure that RefObjectMap parent 
		// references do not refer to nonexistent (and possibly do-be-created) triplesmaps.
		for (BlankNodeOrIRI node : triplesMaps.keySet()) {
			triplesMaps.get(node).addPredicateObjectMaps(readPredicateObjectMaps(node));
		}
	}

	@Override
	public Collection<TriplesMap> getTriplesMaps() {
		return triplesMaps.values();
	}

    /**
     * This method processes the graph, creates the objects and populates the
     * set of TriplesMaps
     *
     * @return the set of TriplesMaps generated from the graph
     * @throws InvalidRMLCMappingException if found something invalid/missing
     */
    private Map<BlankNodeOrIRI, TriplesMap> readTriplesMaps()
            throws InvalidRMLCMappingException {
        Map<BlankNodeOrIRI, TriplesMap> triples = new HashMap<>();
        // find triplesmap nodes
        // it has to have a logical table declaration
        Collection<BlankNodeOrIRI> triplesMapNodes = graph
                .stream(null, getRDF().createIRI(R2RMLVocabulary.PROP_LOGICAL_TABLE), null)
                .map(Triple::getSubject)
                .collect(toSet());

        // for each triplesmap node populate the triplesmap, ignoring property-object maps,
        // and add it to the set of triplesmaps
        for (BlankNodeOrIRI node : triplesMapNodes) {
            triples.put(node, readTriplesMap(node));
        }
        return triples;
    }

	/**
	 * Read and return one TriplesMap given a Resource node in the graph.
	 * 
	 * @param node
	 *            - the Resource node of the TriplesMap in the graph
	 * @return the generated TriplesMap object
	 * @throws InvalidRMLCMappingException
	 *             if found something invalid/missing
	 */
	private TriplesMap readTriplesMap(BlankNodeOrIRI node)
			throws InvalidRMLCMappingException {
		// create a TriplesMap populating each argument
		LogicalTable logicalTable = readLogicalTable(node);
		SubjectMap subjectMap = readSubjectMap(node);
		TriplesMap triplesMap = mfact.createTriplesMap(logicalTable, subjectMap);
		triplesMap.setNode(node);
		return triplesMap;
	}

	/**
	 * This method reads and creates a LogicalTable given a TriplesMap Resource
	 * node.
	 * 
	 * @param node
	 *            the TriplesMap Resource node
	 * @return the created LogicalTable object
	 * @throws InvalidRMLCMappingException
	 *             if there's no logicalTable node
	 */
	private LogicalTable readLogicalTable(BlankNodeOrIRI node)
			throws InvalidRMLCMappingException {
		LogicalTable toReturn = null;
		// find logical table nodes
        Collection<RDFTerm> logicalTableNode = readObjectsInMappingGraph(node, getRDF().createIRI(R2RMLVocabulary.PROP_LOGICAL_TABLE));
		// must be exactly one logicaltable node
		if (logicalTableNode.size() != 1) {
			throw new InvalidRMLCMappingException(
					"Invalid mapping: TriplesMap " + node + " has no LogicalTable.");
		} else {
			BlankNodeOrIRI logicalTable = (BlankNodeOrIRI) logicalTableNode.toArray()[0];
			boolean isSQLTable = false;

			// look for tableName
            RDFTerm tableName = readObjectInMappingGraph(logicalTable, getRDF().createIRI(R2RMLVocabulary.PROP_TABLE_NAME));
			if (tableName != null) {
				isSQLTable = true;
				toReturn = mfact.createSQLBaseTableOrView(((Literal)tableName).getLexicalForm());
			}

			// look for sql query
            RDFTerm query = readObjectInMappingGraph(logicalTable, getRDF().createIRI(R2RMLVocabulary.PROP_SQL_QUERY));
			if (query != null) {
				if (isSQLTable) {
					throw new InvalidRMLCMappingException(
							"Invalid mapping: Logical table in TripleMap " + node + " has both a tablename and a SQL query.");
				}

				toReturn = mfact.createRMLCView(((Literal)query).getLexicalForm());

				// look for sql version -> what to do with it?
                RDFTerm version = readObjectInMappingGraph(logicalTable,
                        getRDF().createIRI(R2RMLVocabulary.PROP_SQL_VERSION));
				if (version != null) {
                    ((RMLCView) toReturn).addSQLVersion((IRI) version);
				}
			}

			if (toReturn == null) {
				throw new InvalidRMLCMappingException(
						"Invalid mapping: Logical table in TripleMap " + node + " has no tablename or SQL query.");
			}

			toReturn.setNode(logicalTable);
			return toReturn;
		}
	}

	/**
	 * This method processes the given Resource node as subject by finding the
	 * object node with the given resourceType URI as predicate. Finds and
	 * returns the first object.
	 * 
	 * @param node
	 *            The Resource node as subject
	 * @param resourceType
	 *            The URI as predicate
	 * @return the extracted first object as String
	 */
	private RDFTerm readObjectInMappingGraph(BlankNodeOrIRI node, IRI resourceType) {
		// look for resourceType declaration
        Collection<RDFTerm> obj = graph.stream(node, resourceType, null)
                .map(Triple::getObject)
                .collect(toSet());
		if (obj.size() == 1)
			return obj.iterator().next();
		else
		    return null;
	}

	/**
	 * This method processes the given Resource node as subject by finding the
	 * object node with the given resourceType URI as predicate. Finds and
	 * returns the first object.
	 * 
	 * @param node
	 *            The Resource node as subject
	 * @param resourceType
	 *            The URI as predicate
	 * @return the extracted first object as String
	 */
	private Collection<RDFTerm> readObjectsInMappingGraph(BlankNodeOrIRI node, IRI resourceType) {
		// look for resourceType declaration
        return graph.stream(node, resourceType, null)
                .map(Triple::getObject)
                .collect(toSet());
	}

    /**
	 * This method reads and returns the list of GraphMap-s of a given Resource
	 * node.
	 * 
	 * @param node
	 *            - the Resource node in which to look for graphMaps
	 * @return the created list of GraphMap objects
	 */
	private List<GraphMap> readGraphMap(BlankNodeOrIRI node) {
		List<GraphMap> graphMapList = new ArrayList<GraphMap>();
		// look for graph declaration
        Collection<RDFTerm> graphDecl = graph.stream(node, getRDF().createIRI(R2RMLVocabulary.PROP_GRAPH), null)
                .map(Triple::getObject)
                .collect(toSet());
		if (graphDecl.size() > 0) {
			for (RDFTerm val : graphDecl) {
				graphMapList.add(mfact.createGraphMap((IRI)val));
			}
		} else {
			// look for graphMap nodes
            Collection<RDFTerm> graphMaps = graph.stream(node, getRDF().createIRI(R2RMLVocabulary.PROP_GRAPH_MAP), null)
                    .map(Triple::getObject)
                    .collect(toSet());
			for (RDFTerm value : graphMaps) {
                BlankNodeOrIRI graphMapNode = (BlankNodeOrIRI) value;
				// create graphMap object
                GraphMap graphMap = (GraphMap) readTermMap(graphMapNode,
                        getRDF().createIRI(R2RMLVocabulary.PROP_GRAPH_MAP));
				graphMap.setNode(graphMapNode);
				// add it to the list
				graphMapList.add(graphMap);
			}
		}
		return graphMapList;
	}

	/**
	 * Reads and returns the SubjectMap generated from a given Resource node.
	 * 
	 * @param node
	 *            - the Resource node in which to look for SubjectMap
	 * @return the created SubjectMap object
	 * @throws InvalidRMLCMappingException
	 *             if there's no subjectMap node
	 */
	private SubjectMap readSubjectMap(BlankNodeOrIRI node)
			throws InvalidRMLCMappingException {
		// look for subject declaration -> constant
        RDFTerm subject = readObjectInMappingGraph(node,
                getRDF().createIRI(R2RMLVocabulary.PROP_SUBJECT));
		if (subject != null) {
			// return constant valued
			return mfact.createSubjectMap((IRI)subject);
		} else {
			// look for subjectMap declarations
            Collection<RDFTerm> subjectMapNode = graph.stream(node, getRDF().createIRI(R2RMLVocabulary.PROP_SUBJECT_MAP), null)
                    .map(Triple::getObject)
                    .collect(toSet());
			if (subjectMapNode.size() != 1) {
				throw new InvalidRMLCMappingException(
						"Invalid mapping: TriplesMap without subjectMap node");
			} else {
				BlankNodeOrIRI subjectNode = (BlankNodeOrIRI) subjectMapNode.toArray()[0];

				// create the subjectMap object
                SubjectMap subjectMap = (SubjectMap) readTermMap(subjectNode,
                        getRDF().createIRI(R2RMLVocabulary.PROP_SUBJECT_MAP));
				if (subjectMap == null)
					return null;

				// look for termtype -> iri or bnode
                IRI termtype = (IRI) readObjectInMappingGraph(subjectNode,
                        getRDF().createIRI(R2RMLVocabulary.PROP_TERM_TYPE));
				// set termtype if not null
				if (termtype != null)
					subjectMap.setTermType(termtype);

				// look for context graphMap
				List<GraphMap> graphMaps = readGraphMap(subjectNode);
				for (GraphMap graphMap : graphMaps)
					subjectMap.addGraphMap(graphMap);

				// look for inverseExpression
                RDFTerm invExpr = readObjectInMappingGraph(
						subjectNode,
                        getRDF().createIRI(R2RMLVocabulary.PROP_INVERSE_EXPRESSION));
				if (invExpr != null)
					subjectMap.setInverseExpression(mfact
							.createInverseExpression(((Literal)invExpr).getLexicalForm()));

				// look for class declarations
                Collection<RDFTerm> classes = readObjectsInMappingGraph(subjectNode,
                        getRDF().createIRI(R2RMLVocabulary.PROP_CLASS));

				// add all declared classes
				for (RDFTerm subjectClass : classes)
					subjectMap.addClass((IRI)subjectClass);

				subjectMap.setNode(subjectNode);
				return subjectMap;
			}
		}
	}

	/**
	 * Reads and returns the list of PredicateObjectMap-s of a given Resource
	 * node.
	 * 
	 * @param tripleMapNode
	 *            - the Resource node in which to look for PredicateObjectMaps
	 * @return the created list of PredicateObjectMap objects
	 * @throws InvalidRMLCMappingException
	 *             if there's no predicate or objectMap
	 */
	private List<PredicateObjectMap> readPredicateObjectMaps(BlankNodeOrIRI tripleMapNode)
			throws InvalidRMLCMappingException {
		List<PredicateObjectMap> predObjs = new ArrayList<PredicateObjectMap>();

		// find predicateobjectmap nodes
        Collection<RDFTerm> predicateObjectNodes = graph.stream(tripleMapNode, getRDF().createIRI(R2RMLVocabulary.PROP_PREDICATE_OBJECT_MAP), null)
                .map(Triple::getObject)
                .collect(toSet());
		// for each predicateobjectmap find predicatemap, objectmap
		// and add it to the list to be returned
		for (RDFTerm predobj : predicateObjectNodes) {
			BlankNodeOrIRI predobjNode = (BlankNodeOrIRI) predobj;

			List<PredicateMap> predicateMaps = readPredicateMaps(predobjNode);

			List<ObjectMap> objectMaps = readObjectMaps(predobjNode);

			List<RefObjectMap> refObjectMaps = readRefObjectMaps(predobjNode,
					tripleMapNode);

			if (predicateMaps.isEmpty()) {
				throw new InvalidRMLCMappingException(
						"Invalid mapping: PredicateObjectMap " + predobjNode + " in TripleMap " + tripleMapNode + " has no PredicateMap.");
			} else if (objectMaps.isEmpty() && refObjectMaps.isEmpty()) {
				throw new InvalidRMLCMappingException(
						"Invalid mapping: PredicateObjectMap " + predobjNode + " in TripleMap " + tripleMapNode + " has no ObjectMaps or RefObjectMaps.");
			}

			PredicateObjectMap predobjMap = mfact.createPredicateObjectMap(
					predicateMaps, objectMaps, refObjectMaps);

			// look for context graphMap
			List<GraphMap> graphMaps = readGraphMap(predobjNode);
			for (GraphMap graphMap : graphMaps)
				predobjMap.addGraphMap(graphMap);

			// add predobjmap to the list to be returned
			if (predobjMap != null) {
				predobjMap.setNode(predobjNode);
				predObjs.add(predobjMap);
			}

		}
		return predObjs;
	}

	/**
	 * Reads and returns the PredicateMaps generated from a given Resource node.
	 * 
	 * @param node
	 *            - the Resource node in which to look for PredicateMaps
	 * @return the created list of PredicateMap objects
	 */
	private List<PredicateMap> readPredicateMaps(BlankNodeOrIRI node) {
		List<PredicateMap> predicateMaps = new ArrayList<PredicateMap>();
		// look for predicate declaration -> constant
        Collection<RDFTerm> predicates = readObjectsInMappingGraph(node,
                getRDF().createIRI(R2RMLVocabulary.PROP_PREDICATE));
		if (predicates != null) {
			for (RDFTerm predicate : predicates) {
				// return constant valued
				predicateMaps.add(mfact.createPredicateMap((IRI)predicate));
			}
		}

		// there might still be predicateMap declarations

		// look for predicateMap declaration
        Collection<RDFTerm> predicateMapNodes = graph.stream(node, getRDF().createIRI(R2RMLVocabulary.PROP_PREDICATE_MAP), null)
                .map(Triple::getObject)
                .collect(toSet());
		for (RDFTerm predMapNode : predicateMapNodes) {

			BlankNodeOrIRI predicateNode = (BlankNodeOrIRI) predMapNode;

			// create the predicateMap object
            PredicateMap predicateMap = (PredicateMap) readTermMap(
					predicateNode,
                    getRDF().createIRI(R2RMLVocabulary.PROP_PREDICATE_MAP));

			// termtype can be only iri which is the default
			// so no reading of termtype needed
			predicateMap.setNode(predicateNode);

			predicateMaps.add(predicateMap);
		}

		return predicateMaps;
	}

	/**
	 * Reads and returns the ObjectMasp generated from a given Resource node.
	 * 
	 * @param node
	 *            - the Resource node in which to look for ObjectMap
	 * @return the created list of ObjectMap object or null if it's refObjectMap
	 */
	private List<ObjectMap> readObjectMaps(BlankNodeOrIRI node) {
		List<ObjectMap> objectMaps = new ArrayList<ObjectMap>();
		// look for predicate declaration -> constant
        Collection<RDFTerm> objects = readObjectsInMappingGraph(node,
                getRDF().createIRI(R2RMLVocabulary.PROP_OBJECT));

        for (RDFTerm object : objects) {
            // return constant valued
            objectMaps.add(mfact.createObjectMap(object));
        }


        // look for objectMap declaration
        Collection<RDFTerm> objectMapNodes = readObjectsInMappingGraph(node,
                getRDF().createIRI(R2RMLVocabulary.PROP_OBJECT_MAP));
		for (RDFTerm objectMapNode : objectMapNodes) {

            BlankNodeOrIRI objectNode = (BlankNodeOrIRI) objectMapNode;

			// create the objectMap object
            ObjectMap objectMap = (ObjectMap) readTermMap(objectNode,
                    getRDF().createIRI(R2RMLVocabulary.PROP_OBJECT_MAP));
			if (objectMap != null) {

				// look for termtype -> iri or bnode or literal
                IRI termtype = (IRI) readObjectInMappingGraph(objectNode,
                        getRDF().createIRI(R2RMLVocabulary.PROP_TERM_TYPE));
				// set termtype if not null
				if (termtype != null)
					objectMap.setTermType(termtype);

				// look for inverseExpression
                RDFTerm invExpr = readObjectInMappingGraph(
						objectNode,
                        getRDF().createIRI(R2RMLVocabulary.PROP_INVERSE_EXPRESSION));
				if (invExpr != null)
					objectMap.setInverseExpression(mfact
							.createInverseExpression(((Literal)invExpr).getLexicalForm()));

				// look for datatype
                IRI datatype = (IRI) readObjectInMappingGraph(objectNode,
                        getRDF().createIRI(R2RMLVocabulary.PROP_DATATYPE));
				if (datatype != null)
					objectMap.setDatatype(datatype);

				// look for language
                RDFTerm lang =  readObjectInMappingGraph(objectNode, getRDF().createIRI(R2RMLVocabulary.PROP_LANGUAGE));
				if (lang != null)
					objectMap.setLanguageTag(((Literal)lang).getLexicalForm());

				objectMap.setNode(objectNode);
				objectMaps.add(objectMap);
			}
		}

		return objectMaps;
	}

	/**
	 * Reads and returns a specific type of TermMap from a given Resource node.
	 * 
	 * @param node
	 *            - the Resource node in which to look
	 * @param type
	 *            - the URI that can be one of
	 *            SubjectMap/PredicateMap/ObjectMap/GraphMap
	 * @return the created TermMap object
	 */
	private TermMap readTermMap(BlankNodeOrIRI node, IRI type) {

		// look for template
        RDFTerm resource = readObjectInMappingGraph(node, getRDF().createIRI(R2RMLVocabulary.PROP_TEMPLATE));
		TermMap.TermMapType termMapType = TermMap.TermMapType.TEMPLATE_VALUED;

		// look for column
		if (resource == null) {
            resource = readObjectInMappingGraph(node, getRDF().createIRI(R2RMLVocabulary.PROP_COLUMN));
			termMapType = TermMap.TermMapType.COLUMN_VALUED;
		}

		// look for constant
		if (resource == null) {
            resource = readObjectInMappingGraph(node, getRDF().createIRI(R2RMLVocabulary.PROP_CONSTANT));
			termMapType = TermMap.TermMapType.CONSTANT_VALUED;
		}

		if (resource != null) {
            if (type.equals(getRDF().createIRI(R2RMLVocabulary.PROP_SUBJECT_MAP))) {
				// return the corresponding subjectMap
                switch (termMapType){
                    case COLUMN_VALUED:
                        return mfact.createSubjectMap(((Literal) resource).getLexicalForm());
                    case CONSTANT_VALUED:
                        return mfact.createSubjectMap((IRI)resource);
                    case TEMPLATE_VALUED:
                        return mfact.createSubjectMap(mfact.createTemplate(((Literal) resource).getLexicalForm()));
                }

			} else if (type.equals(getRDF().createIRI(R2RMLVocabulary.PROP_PREDICATE_MAP))) {
				// return the corresponding predicateMap
                switch (termMapType){
                    case COLUMN_VALUED:
                        return mfact.createPredicateMap(((Literal) resource).getLexicalForm());
                    case CONSTANT_VALUED:
                        return mfact.createPredicateMap((IRI)resource);
                    case TEMPLATE_VALUED:
                        return mfact.createPredicateMap(mfact.createTemplate(((Literal) resource).getLexicalForm()));
                }

			} else if (type.equals(getRDF().createIRI(R2RMLVocabulary.PROP_OBJECT_MAP))) {
				// return the corresponding objectMap
                switch (termMapType){
                    case COLUMN_VALUED:
                        return mfact.createObjectMap(((Literal) resource).getLexicalForm());
                    case CONSTANT_VALUED:
                        return mfact.createObjectMap(resource);
                    case TEMPLATE_VALUED:
                        return mfact.createObjectMap(mfact.createTemplate(((Literal) resource).getLexicalForm()));
                }

			} else if (type.equals(getRDF().createIRI(R2RMLVocabulary.PROP_GRAPH_MAP))) {
				// return the corresponding graphMap
				switch (termMapType) {
					case COLUMN_VALUED:
						return mfact.createGraphMap(((Literal) resource).getLexicalForm());
					case CONSTANT_VALUED:
						return mfact.createGraphMap((IRI) resource);
					case TEMPLATE_VALUED:
						return mfact.createGraphMap(mfact.createTemplate(((Literal) resource).getLexicalForm()));
				}
			}
		}
		return null;

	}


	/**
	 * Reads and returns the RefObjectMaps of a given Resource node.
	 * 
	 * @param pomNode
	 *            - the Resource node (predicateObjectMap) in which to look for
	 *            RefObjectMap
	 * @param tmNode
	 *            - the Resource node (of the TriplesMap) needed for child
	 *            logical table
	 * @return the created list of RefObjectMap objects
	 * @throws InvalidRMLCMappingException
	 *             if refObjectMap has no parentTriplesMap
	 */
	private List<RefObjectMap> readRefObjectMaps(BlankNodeOrIRI pomNode, BlankNodeOrIRI tmNode)
			throws InvalidRMLCMappingException {
		List<RefObjectMap> refObjectMaps = new ArrayList<RefObjectMap>();
		JoinObject childObject, parentObject;
		// look for objectMap declaration
        Collection<RDFTerm> objectMapNodes = graph.stream(pomNode, getRDF().createIRI(R2RMLVocabulary.PROP_OBJECT_MAP), null)
                .map(Triple::getObject)
                .collect(toSet());
		for (RDFTerm objectMapNode : objectMapNodes) {

			BlankNodeOrIRI objectNode = (BlankNodeOrIRI) objectMapNode;

			// look for parentTriplesMap keyword
            Iterator<RDFTerm> parentTriplesMap = graph.stream(objectNode, getRDF().createIRI(R2RMLVocabulary.PROP_PARENT_TRIPLES_MAP), null)
                    .map(Triple::getObject)
                    .collect(toSet())
					.iterator();
			if (!parentTriplesMap.hasNext()) {
				continue;
			}

			// create refObjMap object
			BlankNodeOrIRI parentNode = (BlankNodeOrIRI) parentTriplesMap.next();
			if (! triplesMaps.containsKey(parentNode)) {
				throw new InvalidRMLCMappingException(
						"Invalid mapping: RefObjectMap in TripleMap " + tmNode + " refers to non-existent TripleMap: " + parentNode);
			}
			RefObjectMap refObjectMap = mfact.createRefObjectMap(triplesMaps.get(parentNode));
			refObjectMap.setNode(objectNode);
			refObjectMap.setChildLogicalTable(readLogicalTable(tmNode));
			refObjectMap.setParentLogicalTable(readLogicalTable(parentNode));

			// look for join condition
            Collection<RDFTerm> joinConditions = graph.stream(objectNode, getRDF().createIRI(R2RMLVocabulary.PROP_JOIN_CONDITION), null)
                    .map(Triple::getObject)
                    .collect(toSet());
			for (RDFTerm value : joinConditions) {
				BlankNodeOrIRI joinCondition = (BlankNodeOrIRI) value;

				// look for child declaration
				 Collection<RDFTerm> children = graph.stream(joinCondition, getRDF().createIRI(RMLCVocabulary.PROP_CHILD), null)
						 .map(Triple::getObject)
						 .collect(toSet());

				 if(children.size()!=1){
					 throw new InvalidRMLCMappingException(
							 "Invalid mapping: Join Condition " + joinCondition + " has 0 or multiple children.");
				 } else {
					 BlankNodeOrIRI child = (BlankNodeOrIRI) children.toArray()[0];
					 childObject=readJoinObject(child, RMLCVocabulary.PROP_CHILD);
				 }


				Collection<RDFTerm> parents = graph.stream(joinCondition, getRDF().createIRI(RMLCVocabulary.PROP_PARENT), null)
						.map(Triple::getObject)
						.collect(toSet());

				if(parents.size()!=1){
					throw new InvalidRMLCMappingException(
							"Invalid mapping: Join Condition " + joinCondition + " has 0 or multiple parents.");
				} else {
					BlankNodeOrIRI parent = (BlankNodeOrIRI) parents.toArray()[0];
					parentObject=readJoinObject(parent, RMLCVocabulary.PROP_PARENT);
				}

				// add join condition to refobjMap instance
				if (childObject != null && parentObject != null)
					refObjectMap.addJoinCondition(mfact.createJoinCondition(childObject, parentObject));
			}
			refObjectMaps.add(refObjectMap);
		}

		return refObjectMaps;
	}


	private JoinObject readJoinObject(BlankNodeOrIRI function, String type) throws InvalidRMLCMappingException{

		Collection<RDFTerm> aux = graph.stream(function, getRDF().createIRI(RMLCVocabulary.PROP_COLUMNS), null)
				.map(Triple::getObject)
				.collect(toSet());

		if(aux.size()!=1){
			throw new InvalidRMLCMappingException("Invalid mapping:"+function+" has 0 or multiple "+type+" objects");
		}

		String c = aux.toArray()[0].toString();
		ArrayList<String> columns = new ArrayList<>(Arrays.asList(c.replace("[","").replace("]","").replace("\"","").split(",")));


		aux = graph.stream(function, getRDF().createIRI(RMLCVocabulary.PROP_FUNCTIONS), null)
				.map(Triple::getObject)
				.collect(toSet());
		String functions="";
		if(aux.size()>1){
			throw new InvalidRMLCMappingException("Invalid mapping: Child has 0 or multiple rmlc:child properties");
		}
		if(aux.size()==1) {
			functions = aux.toArray()[0].toString().replace("\"", "");
		}
		return mfact.createJoinObject(columns, functions);

	}



    private RDF getRDF() {
        return rdf;
    }
}
