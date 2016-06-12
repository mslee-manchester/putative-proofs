package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.util.MaximumModalDepthFinder;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 16-Jun-2010
 */
public class ModalDepthComplexityCalculator implements ComplexityCalculator {


    private static double weight = 10.0;

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        MaximumModalDepthFinder modalDepthFinder = new MaximumModalDepthFinder();
        int originalMaxDepth = 0;
        for(OWLAxiom ax : originalExplanation.getAxioms()) {
            int depth = ax.accept(modalDepthFinder);
            if(depth > originalMaxDepth) {
                originalMaxDepth = depth;
            }
        }
        int lemmatisedMaxDepth = 0;
        for(OWLAxiom ax : axioms) {
            int depth = ax.accept(modalDepthFinder);
            if(depth > lemmatisedMaxDepth) {
                lemmatisedMaxDepth = depth;
            }
        }
        int entailmentDepth = entailment.accept(modalDepthFinder);
        int diff = lemmatisedMaxDepth - originalMaxDepth;
        if(diff < 0) {
            return 0;
        }
        else {
            return diff * weight;
        }
    }

    public static double getWeight() {
        return weight;
    }

    public static void setWeight(double weight) {
        ModalDepthComplexityCalculator.weight = weight;
    }
}
