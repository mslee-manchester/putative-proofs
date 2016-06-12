package org.semanticweb.owl.explanation.proofs;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.MaximumModalDepthFinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 23-Jun-2010
 */
public class SubConceptGenerator {

    private OWLDataFactory dataFactory;

    public SubConceptGenerator(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    public Set<OWLClassExpression> getExpandedSubConcepts(Explanation<OWLAxiom> explanation) {
        Set<OWLClassExpression> basicSubConcepts = new HashSet<OWLClassExpression>();
        basicSubConcepts.addAll(explanation.getEntailment().getNestedClassExpressions());
        for(OWLAxiom axiom : explanation.getAxioms()) {
            basicSubConcepts.addAll(axiom.getNestedClassExpressions());
        }
        // Now expand
        Set<OWLClassExpression> negatedClosed = new HashSet<OWLClassExpression>();
        for(OWLClassExpression ce : basicSubConcepts) {
            negatedClosed.add(ce);
            if(!ce.isOWLThing() && !ce.isOWLNothing() && !(ce instanceof OWLObjectComplementOf)) {
                negatedClosed.add(ce.getObjectComplementOf());
            }
        }
        System.out.println("The basic set closed under negation is " + negatedClosed.size() + " concepts");
        // Powerset
        Set<Set<OWLClassExpression>> powerset = computeReplacements(negatedClosed);

        Set<OWLClassExpression> stage1ClassExpressions = new HashSet<OWLClassExpression>();
//        for(Set<OWLClassExpression> ces : setree) {
//            if(ces.size() > 1) {
//                OWLObjectIntersectionOf conjunction = dataFactory.getOWLObjectIntersectionOf(ces);
//                stage1ClassExpressions.add(conjunction);
//                stage1ClassExpressions.add(conjunction.getObjectComplementOf());
//                OWLObjectUnionOf disjunction = dataFactory.getOWLObjectUnionOf(ces);
//                stage1ClassExpressions.add(disjunction);
//                stage1ClassExpressions.add(disjunction.getObjectComplementOf());
//            }
//        }
        stage1ClassExpressions.addAll(negatedClosed);

        Set<OWLClassExpression> stage2ClassExpressions = new HashSet<OWLClassExpression>(stage1ClassExpressions);
        Set<OWLObjectProperty> propertySignature = new HashSet<OWLObjectProperty>();
        int maxDepth = 0;
        for(OWLAxiom ax : explanation.getAxioms()) {
            propertySignature.addAll(ax.getObjectPropertiesInSignature());
            MaximumModalDepthFinder finder = new MaximumModalDepthFinder();
            int depth = ax.accept(finder);
            if(depth > maxDepth) {
                maxDepth = depth;
            }
        }
        propertySignature.addAll(explanation.getEntailment().getObjectPropertiesInSignature());
        int d = maxDepth;// * explanation.getAxioms().size();
        for(OWLObjectProperty property : propertySignature) {
            for(int i = 0; i < d; i++) {
                Set<OWLClassExpression> fillers = new HashSet<OWLClassExpression>(stage2ClassExpressions);
                for(OWLClassExpression filler : fillers) {
                    stage2ClassExpressions.add(dataFactory.getOWLObjectSomeValuesFrom(property, filler));
                    stage2ClassExpressions.add(dataFactory.getOWLObjectSomeValuesFrom(property, filler).getObjectComplementOf());
                    stage2ClassExpressions.add(dataFactory.getOWLObjectAllValuesFrom(property, filler));
                    stage2ClassExpressions.add(dataFactory.getOWLObjectAllValuesFrom(property, filler).getObjectComplementOf());
                    // TODO: CARDI
                }
            }
        }
        return stage2ClassExpressions;
    }


    private Set<Set<OWLClassExpression>> computeReplacements(Set<OWLClassExpression> operands) {
        Set<List<OWLClassExpression>> ps = new HashSet<List<OWLClassExpression>>();
        ps.add(new ArrayList<OWLClassExpression>());
        for (OWLClassExpression op : operands) {
            Set<List<OWLClassExpression>> pscopy = new HashSet<List<OWLClassExpression>>(ps);

//            for (OWLClassExpression opEx : operands) { //
                for (List<OWLClassExpression> pselement : pscopy) {
                    ArrayList<OWLClassExpression> union = new ArrayList<OWLClassExpression>();

                    union.addAll(pselement);
                    union.add(op);
                    ps.remove(pselement);
                    ps.add(union);
                }
//            }
        }
        Set<Set<OWLClassExpression>> result = new HashSet<Set<OWLClassExpression>>();
        for(List<OWLClassExpression> desc : ps) {
            result.add(new HashSet<OWLClassExpression>(desc));
        }
        return result;
    }

}
