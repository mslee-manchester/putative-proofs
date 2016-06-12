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
public class UnpairedDomainAxiomComplexityCalculator implements ComplexityCalculator {

    private static double weighting = 100.0;

    private OWLReasonerFactory rf;

    public UnpairedDomainAxiomComplexityCalculator(OWLReasonerFactory rf) {
        this.rf = rf;
    }

    private Set<OWLClass> getClassesInSignature(Set<OWLAxiom> axioms) {
        Set<OWLClass> sig = new HashSet<OWLClass>();
        for (OWLAxiom ax : axioms) {
            sig.addAll(ax.getClassesInSignature());
        }
        return sig;
    }

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {

        double result = 0.0;
        try {
            for (OWLAxiom ax : axioms) {
                if (ax instanceof OWLObjectPropertyDomainAxiom) {
                    OWLObjectPropertyDomainAxiom domAx = (OWLObjectPropertyDomainAxiom) ax;
                    OWLOntologyManager man = OWLManager.createOWLOntologyManager();
                    OWLDataFactory df = man.getOWLDataFactory();
                    OWLReasoner reasoner = rf.createReasoner(man.createOntology(axioms));
                    for (OWLClass cls : getClassesInSignature(axioms)) {
                        OWLClass thing = df.getOWLThing();
                        if (reasoner.isSatisfiable(cls)) {
                            OWLObjectSomeValuesFrom restriction = man.getOWLDataFactory().getOWLObjectSomeValuesFrom(domAx.getProperty(), thing);
                            OWLSubClassOfAxiom scaRestriction = df.getOWLSubClassOfAxiom(cls, restriction);
                            OWLObjectSomeValuesFrom restrictioInv = man.getOWLDataFactory().getOWLObjectSomeValuesFrom(domAx.getProperty().getInverseProperty(), thing);
                            OWLSubClassOfAxiom scaRestrictionInv = df.getOWLSubClassOfAxiom(cls, restrictioInv);

                            if (reasoner.isEntailed(scaRestriction) || reasoner.isEntailed(scaRestrictionInv)) {
                                result += weighting;
                                break;
                            }
                        }
                    }
                    reasoner.dispose();
                }
            }
        }
        catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        return result;
    }
}
