package org.semanticweb.owl.explanation.lemmas;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owl.explanation.api.*;
import org.semanticweb.owl.explanation.impl.blackbox.checker.SatisfiabilityEntailmentChecker;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
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
 * 05-Nov-2008<br><br>
 */
public abstract class LemmaGenerator<E> {

    private Explanation<E> explanation;

    private OWLOntologyManager man;

    private OWLReasonerFactory reasonerFactory;

    private OWLDataFactory dataFactory;

//    private ExplanationGeneratorFactory<E> explanationGeneratorFactory;

    private ExplanationGeneratorFactory<E> laconicExplanationGeneratorFactory;

    public LemmaGenerator(Explanation<E> explanation,
                          ExplanationGeneratorFactory<E> laconicExplanationGeneratorFactory,
                          OWLReasonerFactory reasonerFactory,
                          OWLDataFactory dataFactory) {
        this.explanation = explanation;
        this.man = OWLManager.createOWLOntologyManager(dataFactory);
        this.reasonerFactory = reasonerFactory;
        this.dataFactory = dataFactory;
//        this.explanationGeneratorFactory = explanationGeneratorFactory;
        this.laconicExplanationGeneratorFactory = laconicExplanationGeneratorFactory;
    }



    public OWLOntology getOntology() {
        try {
            return man.createOntology(explanation.getAxioms());
        }
        catch (OWLOntologyCreationException e) {
            throw new OWLRuntimeException(e);
        }
        catch (OWLOntologyChangeException e) {
            throw new OWLRuntimeException(e);
        }
    }


    public Set<OWLAxiom> getAxioms() {
        return Collections.unmodifiableSet(explanation.getAxioms());
    }



    public OWLDataFactory getDataFactory() {
        return dataFactory;
    }


    public Set<OWLObjectProperty> getObjectPropertiesInSignature() {
        Set<OWLObjectProperty> props = new HashSet<OWLObjectProperty>();
        for (OWLAxiom ax : getAxioms()) {
            props.addAll(ax.getObjectPropertiesInSignature());
        }
        return props;
    }


    public Set<OWLClass> getClassesInSignature() {
        Set<OWLClass> clses = new HashSet<OWLClass>();
        clses.add(getDataFactory().getOWLThing());
        for (OWLAxiom ax : getAxioms()) {
            clses.addAll(ax.getClassesInSignature());
        }
        return clses;
    }

    public Set<OWLIndividual> getIndividualsInSignature() {
            Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();
            for (OWLAxiom ax : getAxioms()) {
                individuals.addAll(ax.getIndividualsInSignature());
            }
            return individuals;
        }




    protected boolean isEntailed(OWLAxiom ax) {
            SatisfiabilityEntailmentChecker checker = new SatisfiabilityEntailmentChecker(reasonerFactory, ax);
            Set<OWLAxiom> axioms = getAxioms();
            return checker.isEntailed(axioms);
    }


    /**
     * Gets the laconic explanations
     * @param lemma
     * @return
     */
    protected Set<Explanation<E>> getLaconicExplanationsIncludingLemma(OWLAxiom lemma) {
        try {
            Set<OWLAxiom> axiomsPlusLemma = new HashSet<OWLAxiom>(getAxioms());
            axiomsPlusLemma.add(lemma);
            ExplanationGenerator<E> gen = laconicExplanationGeneratorFactory.createExplanationGenerator(axiomsPlusLemma);
            Set<Explanation<E>> expls = gen.getExplanations(explanation.getEntailment());
            Set<Explanation<E>> explsContainingLemma = new HashSet<Explanation<E>>();

            for(Explanation<E> exp : expls) {
                Set<OWLAxiom> splitAxioms = new HashSet<OWLAxiom>();
                for (OWLAxiom ax : exp.getAxioms()) {
                    if(ax instanceof OWLSubClassOfAxiom) {
                        OWLSubClassOfAxiom sca = (OWLSubClassOfAxiom) ax;
                        for(OWLClassExpression conj : sca.getSuperClass().asConjunctSet()) {
                            OWLAxiom split = getDataFactory().getOWLSubClassOfAxiom(sca.getSubClass(), conj);
                            splitAxioms.add(split);
                        }
                    }
                    else {
                        splitAxioms.add(ax);
                    }
                }
                Explanation<E> splitExpl = new Explanation<E>(exp.getEntailment(), splitAxioms);
                if(splitAxioms.contains(lemma)) {
                    explsContainingLemma.add(splitExpl);
                }
            }
            
            return explsContainingLemma;
        }
        catch (ExplanationException e) {
            throw new OWLRuntimeException(e);
        }
    }

    protected boolean isCandidateLemma(OWLAxiom lemma) {
            if(explanation.getAxioms().contains(lemma)) {
                return false;
            }
            if(!isEntailed(lemma)) {
                return false;
            }
            Set<Explanation<E>> expls = getLaconicExplanationsIncludingLemma(lemma);
            for(Explanation<E> exp : expls) {
                if(exp.getAxioms().contains(lemma)) {
                    return true;
                }
            }
            return false;
    }

    protected Set<Explanation<OWLAxiom>> getExplanationsForLemma(OWLAxiom lemma) {
        try {
            Set<OWLAxiom> augmentedAxioms = new HashSet<OWLAxiom>(getAxioms());
            ExplanationGeneratorFactory<OWLAxiom> genFac = ExplanationManager.createExplanationGeneratorFactory(reasonerFactory);
            ExplanationGenerator<OWLAxiom> gen = genFac.createExplanationGenerator(augmentedAxioms);
            return gen.getExplanations(lemma);
        }
        catch (ExplanationException e) {
            throw new OWLRuntimeException(e);
        }
    }

    public abstract Set<Lemma> getCandidateLemmas();


    protected void addExplanationsIfLemma(OWLAxiom potentialLemma, Set<Lemma> lemmas) {
        if(isCandidateLemma(potentialLemma)) {
            for(Explanation<OWLAxiom> exp : getExplanationsForLemma(potentialLemma)) {
                lemmas.add(new Lemma(potentialLemma, exp.getAxioms()));
            }
        }
    }
}
