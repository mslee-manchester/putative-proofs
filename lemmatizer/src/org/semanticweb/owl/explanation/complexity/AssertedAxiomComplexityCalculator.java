package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 26-May-2010
 */
public class AssertedAxiomComplexityCalculator implements ComplexityCalculator {

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        double complexity = 0.0;
        for(OWLAxiom ax : axioms) {
            if(!originalExplanation.contains(ax)) {
                complexity+= 0.5;
            }
        }
        return complexity;
    }
}
