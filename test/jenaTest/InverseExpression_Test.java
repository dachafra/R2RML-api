/*******************************************************************************
 * Copyright 2013, the Optique Consortium
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
package jenaTest;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import eu.optique.api.mapping.InverseExpression;
import eu.optique.api.mapping.R2RMLMappingManager;
import eu.optique.api.mapping.R2RMLMappingManagerFactory;
import eu.optique.api.mapping.SubjectMap;
import eu.optique.api.mapping.TriplesMap;

/**
 * JUnit Test Cases
 * 
 * @author Riccardo Mancini
 */
public class InverseExpression_Test
{
	
	@Test
	public void test() throws Exception{
			FileInputStream fis = new FileInputStream(new File("test/mappingFiles/test14.ttl"));
			
			R2RMLMappingManager mm = R2RMLMappingManagerFactory.getJenaMappingManager();

			Model m = ModelFactory.createDefaultModel();
			m = m.read(fis,"testMapping", "TURTLE");
			Collection<TriplesMap> coll = mm.importMappings(m);
			
			Assert.assertTrue(coll.size()==1);
			
			Iterator<TriplesMap> it=coll.iterator();
			while(it.hasNext()){
				TriplesMap current=it.next();
				
				SubjectMap s=current.getSubjectMap();
				
				InverseExpression t=s.getInverseExpression();
				Assert.assertTrue(t.getInverseExpression().contains("deptno"));
				
	
			}
	}

	
}
