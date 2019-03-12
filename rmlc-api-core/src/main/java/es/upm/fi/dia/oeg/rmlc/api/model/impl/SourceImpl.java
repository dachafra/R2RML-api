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

import java.util.HashSet;
import java.util.Set;

import es.upm.fi.dia.oeg.rmlc.api.model.R2RMLVocabulary;
import es.upm.fi.dia.oeg.rmlc.api.model.Source;
import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.api.RDF;
import org.apache.commons.rdf.api.Triple;

/**
 * An implementation of a SQLBaseTableOrView.
 * 
 * @author Marius Strandhaug
 */
public class SourceImpl extends LogicalSourceImpl implements Source {

	String source;

	private IRI referenceFormulation;

	public SourceImpl(RDF c, String sourceName, IRI referenceFormulation) {
		super(c);

		setSourceName(sourceName);

		setReferenceFormulation(referenceFormulation);

        setNode(getRDF().createBlankNode());
	}

	@Override
	public void setSourceName(String sourceName) {
		if (sourceName != null) {
			source = sourceName;
		} else {
			throw new NullPointerException("A source must have a source path.");
		}
	}

	@Override
	public String getSourceName() {
		return source;
	}

	@Override
	public IRI getReferenceFormulation() {
		return referenceFormulation;
	}

	@Override
	public void setReferenceFormulation(IRI referenceFormulation) {
		if (referenceFormulation != null) {
			this.referenceFormulation = referenceFormulation;
		} else {
			throw new NullPointerException("A Logical Source must have a Reference Formulation name.");
		}
	}

	@Override
	public String getSQLQuery() {
		return "SELECT * FROM " + source;
	}

	@Override
	public Set<Triple> serialize() {
		Set<Triple> stmtSet = new HashSet<Triple>();

        stmtSet.add(getRDF().createTriple(node, getRDF().createIRI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), getRDF().createIRI(R2RMLVocabulary.TYPE_BASE_TABLE_OR_VIEW)));

        stmtSet.add(getRDF().createTriple(node, getRDF().createIRI(R2RMLVocabulary.PROP_TABLE_NAME),
                getRDF().createLiteral(getSourceName())));

		return stmtSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof SourceImpl))
			return false;

		SourceImpl other = (SourceImpl) obj;
		if (node == null) {
			if (other.node != null) {
				return false;
			}
		} else if (!node.equals(other.node)) {
			return false;
		}

		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "SQLBaseTableOrViewImpl [table=" + source + ", node=" + node + "]";
	}

}
