package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 14-Jun-2010
 */
public class ClassExpressionTypeDifferenceComplexityCalculator implements ComplexityCalculator {

    private static double weight = 10;

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        Set<ClassExpressionType> originalTypes = getClassExpressionTypes(originalExplanation.getAxioms());
        Set<ClassExpressionType> lemmatisedTypes = getClassExpressionTypes(axioms);
        lemmatisedTypes.removeAll(originalTypes);
        if(lemmatisedTypes.isEmpty()) {
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
        ClassExpressionTypeDifferenceComplexityCalculator.weight = weight;
    }

    private Set<ClassExpressionType> getClassExpressionTypes(Set<OWLAxiom> axioms) {
        Set<ClassExpressionType> types = new HashSet<ClassExpressionType>();
        for(OWLAxiom ax : axioms) {
            for(OWLClassExpression ce : ax.getNestedClassExpressions()) {
                types.add(ce.getClassExpressionType());
            }
        }
        return types;
    }
}
