package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 14-Jun-2010
 */
public class SignatureDifferenceComplexityCalculator implements ComplexityCalculator {

    private static double weight = 10.0;

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        Set<OWLEntity> entailmentSignature = new HashSet<OWLEntity>(entailment.getSignature());
        Set<OWLEntity> axiomsSignature = new HashSet<OWLEntity>();
        for(OWLAxiom ax : axioms) {
            axiomsSignature.addAll(ax.getSignature());
        }
        entailmentSignature.removeAll(axiomsSignature);
        for(OWLEntity entity : new ArrayList<OWLEntity>(entailmentSignature)) {
            if(entity.isOWLClass()) {
                OWLClass cls = entity.asOWLClass();
                if(cls.isOWLThing()) {
                    entailmentSignature.remove(cls);
                }
                else if(cls.isOWLNothing()) {
                    entailmentSignature.remove(cls);
                }
            }
        }
        if(entailmentSignature.isEmpty()) {
            return 0;
        }
        else {
            return weight;
        }
    }

    public static double getWeight() {
        return weight;
    }

    public static void setWeight(double weight) {
        SignatureDifferenceComplexityCalculator.weight = weight;
    }
}
