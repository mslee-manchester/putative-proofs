package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 09-Dec-2010
 */
public class SynonymOfThingComplexityCalculator implements ComplexityCalculator {


    private OWLReasonerFactory rf;

    private static double weighting = 200.0;

    public SynonymOfThingComplexityCalculator(OWLReasonerFactory rf) {
        this.rf = rf;
    }

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        double result = 0.0;
        try {
            boolean entailmentIsSynOfThing = false;
            if (entailment instanceof OWLSubClassOfAxiom) {
                entailmentIsSynOfThing = ((OWLSubClassOfAxiom) entailment).getSubClass().isOWLThing();
            }
            else if (entailment instanceof OWLEquivalentClassesAxiom) {
                entailmentIsSynOfThing = ((OWLEquivalentClassesAxiom) entailment).containsOWLThing();
            }
            if (!entailmentIsSynOfThing) {
                OWLOntologyManager man = OWLManager.createOWLOntologyManager();
                OWLOntology ontology = man.createOntology(axioms);
                OWLReasoner reasoner = rf.createReasoner(ontology);
                OWLClass thing = man.getOWLDataFactory().getOWLThing();
                Set<OWLClass> clses = new HashSet<OWLClass>(reasoner.getEquivalentClasses(thing).getEntities());
                reasoner.dispose();
                for (OWLClass sup : clses) {
                    if (!sup.isOWLThing()) {
                        OWLDataFactory df = man.getOWLDataFactory();
                        if (!axioms.contains(df.getOWLEquivalentClassesAxiom(sup, df.getOWLThing()))) {
                            if (!axioms.contains(df.getOWLSubClassOfAxiom(df.getOWLThing(), sup))) {
                                result += weighting;
                            }
                        }
                    }
                }
            }
        }
        catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static double getWeighting() {
        return weighting;
    }

    public static void setWeighting(double weighting) {
        SynonymOfThingComplexityCalculator.weighting = weighting;
    }
}
