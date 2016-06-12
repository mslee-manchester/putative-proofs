package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.Set;
import java.util.HashSet;/*
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
 * Author: Matthew Horridge<br> The University of Manchester<br> Information Management Group<br>
 * Date: 02-Jan-2009
 */
public class SpecialCaseComplexityCalculator implements ComplexityCalculator {


    private OWLReasonerFactory reasonerFactory;

    private static double nonPairedDomainWeighting = 20.0;

    private static double nonExplicitySynonymOfThingWeighting = 200.0;

    public SpecialCaseComplexityCalculator(OWLReasonerFactory reasonerFactory) {
        this.reasonerFactory = reasonerFactory;
    }

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        double result = 0.0;
        // Add in difficult cases

        ////////////////////////////////////////////////////////////////////////////////

        // TRIVIAL SATISFACTION

        for(OWLAxiom ax : axioms) {
            if(ax instanceof OWLEquivalentClassesAxiom) {
                OWLEquivalentClassesAxiom eca = (OWLEquivalentClassesAxiom) ax;
                for(OWLClassExpression desc : ((OWLEquivalentClassesAxiom) ax).getClassExpressions()) {
                    if(desc instanceof OWLObjectAllValuesFrom) {

                        if (!((OWLObjectAllValuesFrom) desc).getFiller().isOWLNothing()) {
                            result += 15;
                        }
                    }
                }
            }
            else if(ax instanceof OWLSubClassOfAxiom) {
                OWLClassExpression desc = ((OWLSubClassOfAxiom) ax).getSubClass();
                if(desc instanceof OWLObjectAllValuesFrom) {
                    if (!((OWLObjectAllValuesFrom) desc).getFiller().isOWLNothing()) {
                            result += 15;
                        }
                }
            }
        }

        ////////////////////////////////////////////////////////////////////////////////

        try {



            // NON EXPLICIT SYNONYM OF THING


            boolean entailmentIsSynOfThing = false;
            if(entailment instanceof OWLSubClassOfAxiom) {
                entailmentIsSynOfThing = ((OWLSubClassOfAxiom) entailment).getSubClass().isOWLThing();
            }
            else if(entailment instanceof OWLEquivalentClassesAxiom) {
                entailmentIsSynOfThing = ((OWLEquivalentClassesAxiom) entailment).containsOWLThing();
            }
            if (!entailmentIsSynOfThing) {
                OWLOntologyManager man = OWLManager.createOWLOntologyManager();
//            OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();
                OWLReasoner reasoner = reasonerFactory.createReasoner(man.createOntology(axioms));
                OWLClass thing = man.getOWLDataFactory().getOWLThing();
                Set<OWLClass> clses = new HashSet<OWLClass>(reasoner.getEquivalentClasses(thing).getEntities());
                reasoner.dispose();
                for(OWLClass sup : clses) {
                    if(!sup.isOWLThing()) {
                        OWLDataFactory df = man.getOWLDataFactory();
                        if(!axioms.contains(df.getOWLEquivalentClassesAxiom(sup, df.getOWLThing()))) {
                            if(!axioms.contains(df.getOWLSubClassOfAxiom(df.getOWLThing(), sup))) {
                                result += nonExplicitySynonymOfThingWeighting;
                            }
                        }
                    }
                }
            }

            // Domain(R, C), but no A -> min 1 R for all A in sig
            // TODO: In LACONIC EXPL!
            for(OWLAxiom ax : axioms) {
                if(ax instanceof OWLObjectPropertyDomainAxiom) {
                    OWLObjectPropertyDomainAxiom domAx = (OWLObjectPropertyDomainAxiom) ax;
                    boolean ent = false;
                    for(OWLClass cls : getClassesInSig(axioms)) {
                        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
                        OWLDataFactory df = man.getOWLDataFactory();
                        OWLClass thing = df.getOWLThing();
                        OWLObjectSomeValuesFrom restriction = man.getOWLDataFactory().getOWLObjectSomeValuesFrom(domAx.getProperty(), thing);
                        OWLReasoner reasoner = reasonerFactory.createReasoner(man.createOntology(axioms));
                        OWLClassExpression and = df.getOWLObjectIntersectionOf(cls, df.getOWLObjectComplementOf(restriction));
                        if(reasoner.isSatisfiable(cls) && !reasoner.isSatisfiable(and)) {
                            ent = true;
                            reasoner.dispose();
                            break;
                        }
                        else {
                            reasoner.dispose();
                        }
                    }
//                    if(!ent) {
//                        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
//                        OWLDataFactory df = man.getOWLDataFactory();
//                        for(OWLNamedIndividual ind : getIndividualsInSig(axioms)) {
//                            OWLClass thing = df.getOWLThing();
//                            OWLObjectSomeValuesFrom restriction = man.getOWLDataFactory().getOWLObjectSomeValuesFrom(domAx.getProperty(), thing);
//                            OWLClassAssertionAxiom assertion = df.getOWLClassAssertionAxiom(restriction, ind);
//                            OWLReasoner reasoner = reasonerFactory.createReasoner(man.createOntology(axioms));
//                            if(reasoner.isEntailed(assertion)) {
//                                ent = true;
//                                reasoner.dispose();
//                                break;
//                            }
//                            else {
//                                reasoner.dispose();
//                            }
//                        }
//                    }
                    if(!ent) {
                        result += nonPairedDomainWeighting;
                    }
                }
//                else if (ax instanceof OWLSubClassOfAxiom) {
//                    OWLSubClassOfAxiom sca = (OWLSubClassOfAxiom) ax;
//                    if(sca.getSubClass() instanceof OWLObjectSomeValuesFrom) {
//                        OWLObjectSomeValuesFrom svf = ((OWLObjectSomeValuesFrom) sca.getSubClass());
//                        if(!svf.getFiller().isAnonymous()) {
//                            // Encoding of domain checker!
//                            boolean ent = false;
//                            for(OWLClass cls : getClassesInSig(axioms)) {
//                                OWLOntologyManager man = OWLManager.createOWLOntologyManager();
//                                OWLDataFactory df = man.getOWLDataFactory();
//                                OWLClass thing = df.getOWLThing();
//                                OWLObjectSomeValuesFrom restriction = man.getOWLDataFactory().getOWLObjectSomeValuesFrom(svf.getProperty(), thing);
//                                OWLReasoner reasoner = reasonerFactory.createReasoner(man, Collections.singleton( man.createOntology(axioms)));
//                                OWLClassExpression and = df.getOWLObjectIntersectionOf(cls, df.getOWLObjectComplementOf(restriction));
//                                if(reasoner.isSatisfiable(cls) && !reasoner.isSatisfiable(and)) {
//                                    ent = true;
//                                    reasoner.dispose();
//                                    break;
//                                }
//                                else {
//                                    reasoner.dispose();
//                                }
//                            }
//                            if(ent) {
//                                result += 40;
//                            }
//                        }
//                    }
//                }
            }

            // Number of GCIs

//            ExplanationGeneratorFactory<OWLAxiom> genFac = ExplanationManager.createLaconicExplanationGeneratorFactory(reasonerFactory);
//            ExplanationGenerator<OWLAxiom> gen = genFac.createExplanationGenerator(axioms);
//            Set<Explanation<OWLAxiom>> explanations = gen.getExplanations(entailment);
//            for(Explanation<OWLAxiom> explanation : explanations) {
//                for(OWLAxiom ax : explanation.getAxioms()) {
//                    if(ax instanceof OWLSubClassOfAxiom) {
//                        OWLSubClassOfAxiom sca = (OWLSubClassOfAxiom) ax;
//                        if(sca.getSubClass().isAnonymous()) {
////                            result += 2;
//                        }
//                    }
//                }
//            }
//

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static double getNonPairedDomainWeighting() {
        return nonPairedDomainWeighting;
    }

    public static void setNonPairedDomainWeighting(double nonPairedDomainWeighting) {
        SpecialCaseComplexityCalculator.nonPairedDomainWeighting = nonPairedDomainWeighting;
    }

    private static Set<OWLClass> getClassesInSig(Set<OWLAxiom> axioms) {
        Set<OWLClass> clses = new HashSet<OWLClass>();
        for(OWLAxiom ax : axioms) {
            clses.addAll(ax.getClassesInSignature());
        }
        return clses;
    }
    
    private static Set<OWLNamedIndividual> getIndividualsInSig(Set<OWLAxiom> axioms) {
        Set<OWLNamedIndividual> clses = new HashSet<OWLNamedIndividual>();
        for(OWLAxiom ax : axioms) {
            clses.addAll(ax.getIndividualsInSignature());
        }
        return clses;
    }
}
