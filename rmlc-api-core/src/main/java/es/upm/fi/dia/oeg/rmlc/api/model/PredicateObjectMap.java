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
package es.upm.fi.dia.oeg.rmlc.api.model;

import java.util.List;

/**
 * R2RML Predicate Object Map
 * 
 * @author Marius Strandhaug
 */
@W3C_R2RML_Recommendation(R2RMLVocabulary.TYPE_PREDICATE_OBJECT_MAP)
public interface PredicateObjectMap extends MappingComponent {

	/**
	 * Adds a PredicateMap to this PredicateObjectMap. The PredicateMap will be
	 * added to the end of the PredicateMap list. A PredicateObjectMap must have
	 * one or more PredicateMaps.
	 * 
	 * @param pm
	 *            The PredicateMap that will be added.
	 */
	@W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_PREDICATE_MAP)
	public void addPredicateMap(PredicateMap pm);

	/**
	 * Adds an ObjectMap to this PredicateObjectMap. The ObjectMap will be added
	 * to the end of the ObjectMap list. A PredicateObjectMap must have at least
	 * one ObjectMap or RefObjectMap.
	 * 
	 * @param om
	 *            The ObjectMap that will be added.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_OBJECT_MAP)
	public void addObjectMap(ObjectMap om);

	/**
	 * Adds an RefObjectMap to this PredicateObjectMap. The RefObjectMap will be
	 * added to the end of the RefObjectMap list. A PredicateObjectMap must have
	 * at least one ObjectMap or RefObjectMap.
	 * 
	 * @param rom
	 *            The RefObjectMap that will be added.
	 */
	@W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_OBJECT_MAP)
	public void addRefObjectMap(RefObjectMap rom);

	/**
	 * Adds a GraphMap to this PredicateObjectMap. A PredicateObjectMap can have
	 * zero or more GraphMaps. The GraphMap will be added to the end of the
	 * GraphMap list.
	 * 
	 * @param gm
	 *            The GraphMap that will be added.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_GRAPH_MAP)
	public void addGraphMap(GraphMap gm);

	/**
	 * Adds a list of GraphMaps to this PredicateObjectMap. A PredicateObjectMap
	 * can have zero or more GraphMaps. The GraphMaps will be added to the end
	 * of the GraphMap list.
	 * 
	 * @param gms
	 *            The list of GraphMaps that will be added.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_GRAPH_MAP)
    public void addGraphMaps(List<GraphMap> gms);

	/**
	 * Get the GraphMap located at the given index.
	 * 
	 * @param index
	 *            The index of the GraphMap.
	 * @return The GraphMap located at the given index.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_GRAPH_MAP)
	public GraphMap getGraphMap(int index);

	/**
	 * Get the RefObjectMap located at the given index.
	 * 
	 * @param index
	 *            The index of the RefObjectMap.
	 * @return The RefObjectMap located at the given index.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_OBJECT_MAP)
    public RefObjectMap getRefObjectMap(int index);

	/**
	 * Get the ObjectMap located at the given index.
	 * 
	 * @param index
	 *            The index of the ObjectMap.
	 * @return The ObjectMap located at the given index.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_OBJECT_MAP)
    public ObjectMap getObjectMap(int index);

	/**
	 * Get the PredicateMap located at the given index.
	 * 
	 * @param index
	 *            The index of the PredicateMap.
	 * @return The PredicateMap located at the given index.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_PREDICATE_MAP)
    public PredicateMap getPredicateMap(int index);

	/**
	 * Returns an unmodifiable view of the list of GraphMaps that have been
	 * added to this PredicateObjectMap.
	 * 
	 * @return An unmodifiable list of GraphMaps.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_GRAPH_MAP)
	public List<GraphMap> getGraphMaps();

	/**
	 * Returns an unmodifiable view of the list of RefObjectMaps that have been
	 * added to this PredicateObjectMap.
	 * 
	 * @return An unmodifiable list of RefObjectMaps.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_OBJECT_MAP)
	public List<RefObjectMap> getRefObjectMaps();

	/**
	 * Returns an unmodifiable view of the list of ObjectMaps that have been
	 * added to this PredicateObjectMap.
	 * 
	 * @return An unmodifiable list of ObjectMaps.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_OBJECT_MAP)
    public List<ObjectMap> getObjectMaps();

	/**
	 * Returns an unmodifiable view of the list of PredicateMaps that have been
	 * added to this PredicateObjectMap.
	 * 
	 * @return An unmodifiable list of PredicateMaps.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_PREDICATE_MAP)
    public List<PredicateMap> getPredicateMaps();

	/**
	 * Remove the GraphMap given by the parameter, from the PredicateObjectMap.
	 * The subsequent GraphMaps in the list will be shifted left.
	 * 
	 * @param gm
	 *            The GraphMap to be removed.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_GRAPH_MAP)
    public void removeGraphMap(GraphMap gm);

	/**
	 * Remove the RefObjectMap, given by the parameter, from the
	 * PredicateObjectMap. The subsequent RefObjectMaps in the list will be
	 * shifted left.
	 * 
	 * @param rom
	 *            The RefObjectMap to be removed.
	 * @throws IllegalStateException
	 *             If removing the RefObjectMap would cause the
	 *             PredicateObjectMap to contain no RefObjectMaps or ObjectMaps.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_OBJECT_MAP)
	public void removeRefObjectMap(RefObjectMap rom);

	/**
	 * Remove the ObjectMap, given by the parameter, from the
	 * PredicateObjectMap. The subsequent ObjectMaps in the list will be shifted
	 * left.
	 * 
	 * @param om
	 *            The ObjectMap to be removed.
	 * @throws IllegalStateException
	 *             If removing the RefObjectMap would cause the
	 *             PredicateObjectMap to contain no RefObjectMaps or ObjectMaps.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_OBJECT_MAP)
	public void removeObjectMap(ObjectMap om);

	/**
	 * Remove the PredicateMap, given by the parameter, from the
	 * PredicateObjectMap. The subsequent PredicateMaps in the list will be
	 * shifted left.
	 * 
	 * @param pm
	 *            The PredicateMap to be removed.
	 * @throws IllegalStateException
	 *             If removing the RefObjectMap would cause the
	 *             PredicateObjectMap to contain no PredicateMaps.
	 */
    @W3C_R2RML_Recommendation(R2RMLVocabulary.PROP_OBJECT_MAP)
	public void removePredicateMap(PredicateMap pm);

}
