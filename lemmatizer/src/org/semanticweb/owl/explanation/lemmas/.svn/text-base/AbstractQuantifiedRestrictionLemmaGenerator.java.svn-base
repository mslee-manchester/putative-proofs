package org.semanticweb.owl.explanation.lemmas;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorFactory;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.model.*;

import java.util.Set;
import java.util.HashSet;
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
public abstract class AbstractQuantifiedRestrictionLemmaGenerator<E> extends LemmaGenerator<E> {

    private OWLClass currentSubClass;

    public AbstractQuantifiedRestrictionLemmaGenerator(Explanation<E> explanation, ExplanationGeneratorFactory<E> fac,  OWLReasonerFactory reasonerFactory,
                                                       OWLDataFactory dataFactory) {
        super(explanation, fac, reasonerFactory, dataFactory);
    }


    public Set<Lemma> getCandidateLemmas() {
        Set<Lemma> lemmas = new HashSet<Lemma>();
        for(OWLClass cls : getClassesInSignature()) {
            for(OWLObjectProperty prop : getObjectPropertiesInSignature()) {
                for(OWLClass filler : getClassesInSignature()) {
                    currentSubClass = cls;
                    OWLClassExpression restriction = getRestriction(prop, filler);
                    if(restriction != null) {
                        OWLAxiom ax = getDataFactory().getOWLSubClassOfAxiom(cls, restriction);
                        addExplanationsIfLemma(ax, lemmas);
                    }
                    OWLClassExpression negRestriction = getRestriction(prop, getDataFactory().getOWLObjectComplementOf(filler));
                    if(negRestriction != null) {
                        OWLAxiom ax = getDataFactory().getOWLSubClassOfAxiom(cls, negRestriction);
                        addExplanationsIfLemma(ax, lemmas);
                    }
                }
            }
        }
        return lemmas;
    }

    public OWLClass getCurrentSubClass() {
        return currentSubClass;
    }

    protected abstract OWLRestriction getRestriction(OWLObjectProperty prop, OWLClassExpression filler);
}
