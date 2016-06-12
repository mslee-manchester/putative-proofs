package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.*;

import java.util.Set;/*
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
public class StructuralComplexityCalculator implements ComplexityCalculator {


    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        double complexity = 0.0;
        for (OWLAxiom ax : axioms) {
            AxiomVisitor axiomVisitor = new AxiomVisitor();
            ax.accept(axiomVisitor);
            complexity += axiomVisitor.getComplexity();
        }
        return complexity;
    }


    private class AxiomVisitor implements OWLAxiomVisitor {

        private double complexity;

        public double getComplexity() {
            return complexity;
        }

        public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
            complexity += 1.0;
        }

        public void visit(OWLClassAssertionAxiom axiom) {
            DescriptionVisitor descriptionVisitor = new DescriptionVisitor();
            axiom.getClassExpression().accept(descriptionVisitor);

        }

        public void visit(OWLDataPropertyAssertionAxiom axiom) {
            complexity += 1.0;
        }

        public void visit(OWLDataPropertyDomainAxiom axiom) {
            DescriptionVisitor descriptionVisitor = new DescriptionVisitor();
            axiom.getDomain().accept(descriptionVisitor);
        }

        public void visit(OWLDataPropertyRangeAxiom axiom) {
            complexity += 1.0;
        }

        public void visit(OWLSubDataPropertyOfAxiom axiom) {
            complexity += 1.0;
        }


        public void visit(OWLHasKeyAxiom owlHasKeyAxiom) {
        }


        public void visit(OWLDeclarationAxiom owlDeclarationAxiom) {
        }


        public void visit(OWLSubAnnotationPropertyOfAxiom owlSubAnnotationPropertyOfAxiom) {
        }


        public void visit(OWLAnnotationPropertyDomainAxiom owlAnnotationPropertyDomainAxiom) {
        }


        public void visit(OWLAnnotationPropertyRangeAxiom owlAnnotationPropertyRangeAxiom) {
        }


        public void visit(OWLDifferentIndividualsAxiom axiom) {
            complexity += 1.0;
        }

        public void visit(OWLDisjointClassesAxiom axiom) {
            for (OWLClassExpression desc : axiom.getClassExpressions()) {
                DescriptionVisitor descriptionVisitor = new DescriptionVisitor();
                desc.accept(descriptionVisitor);

            }
        }

        public void visit(OWLDisjointDataPropertiesAxiom axiom) {
            complexity += 1.0;
        }

        public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
            complexity += 1.0;
        }

        public void visit(OWLDisjointUnionAxiom axiom) {
            for (OWLClassExpression desc : axiom.getClassExpressions()) {
                DescriptionVisitor descriptionVisitor = new DescriptionVisitor();
                desc.accept(descriptionVisitor);
                complexity += descriptionVisitor.getComplexity();
            }
        }

        public void visit(OWLAnnotationAssertionAxiom axiom) {

        }

        public void visit(OWLEquivalentClassesAxiom axiom) {
            // Generally bad! People don't know which way implication goes
            // In all of the examples I have seen, rarely are both directions required
            for (OWLSubClassOfAxiom ax : axiom.asOWLSubClassOfAxioms()) {
                AxiomVisitor visitor = new AxiomVisitor();
                ax.accept(visitor);
                complexity += visitor.getComplexity();
            }

        }

        public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
            complexity += 2.0;
        }

        public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
            complexity += 2.0;
        }

        public void visit(OWLFunctionalDataPropertyAxiom axiom) {
            complexity += 15.0;
        }

        public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
            complexity += 10.0;
        }

        public void visit(OWLImportsDeclaration axiom) {
        }

        public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
            complexity += 15.0;
        }

        public void visit(OWLInverseObjectPropertiesAxiom axiom) {
            complexity += 20.0;
        }

        public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
            complexity += 1.0;
        }

        public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
            complexity += 3.0;
        }

        public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
            complexity += 3.0;
        }

        public void visit(OWLObjectPropertyAssertionAxiom axiom) {
            complexity += 2.0;
        }

        public void visit(OWLSubPropertyChainOfAxiom axiom) {
            complexity += 20.0;
        }

        public void visit(OWLObjectPropertyDomainAxiom axiom) {
            DescriptionVisitor descriptionVisitor = new DescriptionVisitor();
            axiom.getDomain().accept(descriptionVisitor);
            // Bias against domain... we would just prefer a direct subclass checker
            complexity += descriptionVisitor.getComplexity() * 2.0;
        }

        public void visit(OWLObjectPropertyRangeAxiom axiom) {
            DescriptionVisitor descriptionVisitor = new DescriptionVisitor();
            axiom.getRange().accept(descriptionVisitor);
        }

        public void visit(OWLSubObjectPropertyOfAxiom axiom) {
            // Pretty bad.  Many people didn't find these so easy to deal with
            complexity += 10.0;
        }


        public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
            complexity += 1.0;
        }

        public void visit(OWLSameIndividualAxiom axiom) {
            complexity += 1.0;
        }

        public void visit(OWLSubClassOfAxiom axiom) {

            double subClassWeighting = 1.0;
            double superClassWeighting = 1.0;

            if (axiom.getSubClass().isAnonymous()) {
                if (axiom.getSuperClass().isAnonymous()) {
                    // LHS and RHS both anon
                    subClassWeighting = 5.0;
                    superClassWeighting = 5.0;
                } else {
                    // Just LHS is anon
                    subClassWeighting = 5.0;
                    superClassWeighting = 0.5;
                }
            } else {
                if (axiom.getSuperClass().isAnonymous()) {
                    // Just RHS is anon
                    subClassWeighting = 0.5;
                    superClassWeighting = 2.0;
                } else {
                    // LHS and RHS named
                    subClassWeighting = 0.5;
                    superClassWeighting = 0.5;
                }
            }

            DescriptionVisitor subClassDescriptionVisitor = new DescriptionVisitor();
            axiom.getSubClass().accept(subClassDescriptionVisitor);
            double subClassComplexity = subClassDescriptionVisitor.getComplexity();

            DescriptionVisitor superClassDescriptionVisitor = new DescriptionVisitor();
            axiom.getSuperClass().accept(superClassDescriptionVisitor);
            double superClassComplexity = superClassDescriptionVisitor.getComplexity();

            complexity = subClassComplexity * subClassWeighting + superClassComplexity * superClassWeighting;

        }

        public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
        }

        public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
            // Transitivity is bad because it implies that it's necessary to follow
            // restrictions (or property assertions) over a depth of more than one
            complexity += 7.0;
        }

        public void visit(SWRLRule rule) {
        }

        public void visit(OWLDatatypeDefinitionAxiom axiom) {
        }
    }

    private class DescriptionVisitor implements OWLClassExpressionVisitor, OWLEntityVisitor {

        private int nesting = 0;

        private int modalDepth = 0;

        private int maxModalDepth = 0;

        private int maxNesting = 0;

        private double complexity;

        public int getMaxModalDepth() {
            return maxModalDepth;
        }

        public int getMaxNesting() {
            return maxNesting;
        }

        public int getModalDepth() {
            return modalDepth;
        }

        public int getNesting() {
            return nesting;
        }

        public double getComplexity() {
            return complexity;
        }

        private void decrementNesting() {
            nesting--;
        }

        private void incrementNesting() {
            nesting++;
            if (nesting > maxNesting) {
                maxNesting = nesting;
            }
        }

        private void decrementModalDepth() {
            modalDepth--;
        }

        private void incrementModalDepth() {
            modalDepth++;
            if (modalDepth > maxModalDepth) {
                maxModalDepth = modalDepth;
            }
        }

        private void addModalDepthPenalty() {
            complexity += modalDepth * 4.0;
        }

        private void addNestingPenalty() {
            complexity += nesting * 2.0;
        }

        public void visit(OWLClass desc) {
            if (!desc.isOWLThing() || !desc.isOWLNothing()) {
                addModalDepthPenalty();
                addNestingPenalty();
            }
        }

        public void visit(OWLDataAllValuesFrom desc) {

        }

        public void visit(OWLDataExactCardinality desc) {
        }

        public void visit(OWLDataMaxCardinality desc) {
        }

        public void visit(OWLDataMinCardinality desc) {
        }

        public void visit(OWLDataSomeValuesFrom desc) {
        }

        public void visit(OWLDataHasValue desc) {
        }

        public void visit(OWLObjectAllValuesFrom desc) {
            // Generally bad - the greater the modal depth, the worse the situation
            addModalDepthPenalty();
            addNestingPenalty();
            complexity += 5.0;
            incrementModalDepth();
            incrementNesting();
            desc.getFiller().accept(this);
            decrementNesting();
            decrementModalDepth();
        }

        public void visit(OWLObjectComplementOf desc) {
            addModalDepthPenalty();
            addNestingPenalty();
            complexity += 2.0;
            incrementNesting();
            desc.getOperand().accept(this);
            decrementNesting();
        }

        public void visit(OWLObjectExactCardinality desc) {
            addModalDepthPenalty();
            addNestingPenalty();
            // Max cardinality part is bad
            complexity += 5.0;
            incrementModalDepth();
            incrementNesting();
            desc.getFiller().accept(this);
            decrementNesting();
            decrementModalDepth();
        }

        public void visit(OWLObjectIntersectionOf desc) {
            addModalDepthPenalty();
            addNestingPenalty();
            incrementNesting();
            // Increment complexity for each operand
            for (OWLClassExpression op : desc.getOperands()) {
                op.accept(this);
                complexity += 1.0;
            }
            decrementNesting();
        }

        public void visit(OWLObjectMaxCardinality desc) {
            addModalDepthPenalty();
            addNestingPenalty();
            incrementModalDepth();
            incrementNesting();
            desc.getFiller().accept(this);
            decrementNesting();
            decrementModalDepth();
        }

        public void visit(OWLObjectMinCardinality desc) {
            addModalDepthPenalty();
            addNestingPenalty();
            incrementModalDepth();
            incrementNesting();
            desc.getFiller().accept(this);
            decrementNesting();
            decrementModalDepth();
        }

        public void visit(OWLObjectOneOf desc) {
            addModalDepthPenalty();
            addNestingPenalty();
        }

        public void visit(OWLObjectHasSelf desc) {
            addModalDepthPenalty();
            addNestingPenalty();
        }

        public void visit(OWLObjectSomeValuesFrom desc) {
            addModalDepthPenalty();
            addNestingPenalty();
            incrementModalDepth();
            incrementNesting();
            desc.getFiller().accept(this);
            decrementNesting();
            decrementModalDepth();
        }

        public void visit(OWLObjectUnionOf desc) {
            addModalDepthPenalty();
            addNestingPenalty();
            // Disjunction is just bad!

            incrementNesting();
            for (OWLClassExpression op : desc.getOperands()) {
                op.accept(this);
            }
            decrementNesting();
        }

        public void visit(OWLObjectHasValue desc) {
            addModalDepthPenalty();
            addNestingPenalty();
        }

        public void visit(OWLDatatype dataType) {
        }

        public void visit(OWLIndividual individual) {
        }

        public void visit(OWLDataProperty property) {
        }

        public void visit(OWLObjectProperty property) {
        }


        public void visit(OWLNamedIndividual owlNamedIndividual) {
        }


        public void visit(OWLAnnotationProperty owlAnnotationProperty) {
        }
    }
}
