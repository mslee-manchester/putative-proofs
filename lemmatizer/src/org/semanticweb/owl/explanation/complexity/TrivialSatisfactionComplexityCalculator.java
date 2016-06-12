package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.*;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 09-Dec-2010
 */
public class TrivialSatisfactionComplexityCalculator implements ComplexityCalculator {

    private static double weighting = 100.0;

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        double result = 0.0;
        for (OWLAxiom ax : axioms) {
            if (ax instanceof OWLEquivalentClassesAxiom) {
                OWLEquivalentClassesAxiom eca = (OWLEquivalentClassesAxiom) ax;
                for(OWLClassExpression ce : eca.getClassExpressions()) {
                    for (OWLClassExpression desc : ce.asDisjunctSet()) {
                        if (desc instanceof OWLObjectAllValuesFrom) {
                            if (!((OWLObjectAllValuesFrom) desc).getFiller().isOWLNothing()) {
                                result += weighting;
                            }
                        }
                    }
                }
            }
            else if (ax instanceof OWLSubClassOfAxiom) {
                OWLClassExpression desc = ((OWLSubClassOfAxiom) ax).getSubClass();
                for (OWLClassExpression ce : desc.asDisjunctSet()) {
                    if (ce instanceof OWLObjectAllValuesFrom) {
                        if (!((OWLObjectAllValuesFrom) ce).getFiller().isOWLNothing()) {
                            result += weighting;
                        }
                    }
                }
            }
        }
        return result;
    }
}
