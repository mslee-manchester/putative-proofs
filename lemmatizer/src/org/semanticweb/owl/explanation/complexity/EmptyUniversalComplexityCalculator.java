package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Jun-2010
 */
public class EmptyUniversalComplexityCalculator implements ComplexityCalculator {

    private static double weighting = 50.0;

    public EmptyUniversalComplexityCalculator() {
    }

    public static double getWeighting() {
        return weighting;
    }

    public static void setWeighting(double weighting) {
        EmptyUniversalComplexityCalculator.weighting = weighting;
    }

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        for(OWLAxiom ax : axioms) {
            for(OWLClassExpression ce : ax.getNestedClassExpressions()) {
                if(ce instanceof OWLObjectAllValuesFrom) {
                    OWLObjectAllValuesFrom avf = (OWLObjectAllValuesFrom) ce;
                    if(avf.getFiller().isOWLNothing()) {
                        return weighting;
                    }
                }
            }
        }
        return 0.0;
    }
}
