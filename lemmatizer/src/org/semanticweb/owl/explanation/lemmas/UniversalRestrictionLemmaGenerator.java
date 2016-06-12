package org.semanticweb.owl.explanation.lemmas;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorFactory;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLRestriction;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLClassExpression;

import java.util.Set;
/*
 * Copyright (C) 2008, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */


/**
 * Author: Matthew Horridge<br> The University Of Manchester<br> Information Management Group<br> Date:
 * 06-Nov-2008<br><br>
 */
public class UniversalRestrictionLemmaGenerator<E> extends AbstractQuantifiedRestrictionLemmaGenerator<E> {


    public UniversalRestrictionLemmaGenerator(Explanation<E> explanation, ExplanationGeneratorFactory<E> explanationGeneratorFactory, OWLReasonerFactory reasonerFactory,
                                              OWLDataFactory dataFactory) {
        super(explanation, explanationGeneratorFactory, reasonerFactory, dataFactory);
    }

    

    protected OWLRestriction getRestriction(OWLObjectProperty prop, OWLClassExpression filler) {
        if (!filler.isOWLThing() && !getCurrentSubClass().isOWLThing()) {
            return getDataFactory().getOWLObjectAllValuesFrom(prop, filler);
        }
        else {
            return null;
        }
    }
}

