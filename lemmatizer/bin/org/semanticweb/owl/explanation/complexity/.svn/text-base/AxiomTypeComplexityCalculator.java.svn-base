package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.*;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;/*
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
 * Date: 24-Nov-2008
 */
public class AxiomTypeComplexityCalculator implements ComplexityCalculator {

    private double complexity;

    private Map<Object, Integer> types;

    private static double defaultTypeWeight = 20;

    public AxiomTypeComplexityCalculator() {
        types = new HashMap<Object, Integer>();
    }

    public static double getDefaultTypeWeight() {
        return defaultTypeWeight;
    }

    public static void setDefaultTypeWeight(double defaultTypeWeight) {
        AxiomTypeComplexityCalculator.defaultTypeWeight = defaultTypeWeight;
    }

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        complexity = 0;
        types.clear();
        for(OWLAxiom ax : axioms) {
            AxiomComplexityVisitor visitor = new AxiomComplexityVisitor();
            ax.accept(visitor);
        }
        return complexity;
    }

    private void add(Object type, double complexity) {
        if (!types.containsKey(type)) {
            types.put(type, 1);
//            this.complexity += complexity;
            this.complexity += defaultTypeWeight;
        }
//        else {
//            types.put(type, types.get(type) + 1);
//            this.complexity += 01.0;
//        }
//        else {
//            this.complexity += 0.2 * complexity;
//        }
    }

    private class AxiomComplexityVisitor implements OWLAxiomVisitor {

        public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
            add(axiom.getAxiomType(), 1.0);
        }

        public void visit(OWLClassAssertionAxiom axiom) {
            if (axiom.getClassExpression().isAnonymous()) {
                add(axiom.getAxiomType(), 3.0);
            }
            else {
                add(axiom.getAxiomType(), 1.0);
            }
        }


        public void visit(OWLHasKeyAxiom axiom) {
            add(axiom, 1.0);
        }


        public void visit(OWLDatatypeDefinitionAxiom axiom) {
            add(axiom, 1.0);
        }


        public void visit(OWLSubAnnotationPropertyOfAxiom owlSubAnnotationPropertyOfAxiom) {
        }


        public void visit(OWLAnnotationPropertyDomainAxiom owlAnnotationPropertyDomainAxiom) {
        }


        public void visit(OWLAnnotationPropertyRangeAxiom owlAnnotationPropertyRangeAxiom) {
        }


        public void visit(OWLDataPropertyAssertionAxiom axiom) {
            add(axiom.getAxiomType(), 4.0);
        }

        public void visit(OWLDataPropertyDomainAxiom axiom) {
            add(axiom.getAxiomType(), 7.0);
        }

        public void visit(OWLDataPropertyRangeAxiom axiom) {
            add(axiom.getAxiomType(), 3.0);
        }

        public void visit(OWLSubDataPropertyOfAxiom axiom) {
            add(axiom.getAxiomType(), 7.0);
        }

        public void visit(OWLDeclarationAxiom axiom) {
            add(axiom.getAxiomType(), 1.0);
        }

        public void visit(OWLDifferentIndividualsAxiom axiom) {
            add(axiom.getAxiomType(), 1.0);
        }

        public void visit(OWLDisjointClassesAxiom axiom) {
            for(OWLClassExpression desc : axiom.getClassExpressions()) {
                if(desc.isAnonymous()) {
                    add(axiom.getAxiomType(), 4.0);
                }
            }
            add(axiom.getAxiomType(), 1.0);
        }

        public void visit(OWLDisjointDataPropertiesAxiom axiom) {
            add(axiom.getAxiomType(), 1.0);
        }

        public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
            add(axiom.getAxiomType(), 1.0);
        }

        public void visit(OWLDisjointUnionAxiom axiom) {
            add(axiom.getAxiomType(), 1.0);
        }

        public void visit(OWLAnnotationAssertionAxiom axiom) {
            add(axiom.getAxiomType(), 1.0);
        }

        public void visit(OWLEquivalentClassesAxiom axiom) {
            for(OWLSubClassOfAxiom ax : axiom.asOWLSubClassOfAxioms()) {
                ax.accept(this);
            }
            add(axiom.getAxiomType(), 10.0);
        }

        public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
            add(axiom.getAxiomType(), 2.0);
        }

        public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
            add(axiom.getAxiomType(), 2.0);
        }

        public void visit(OWLFunctionalDataPropertyAxiom axiom) {
            add(axiom.getAxiomType(), 3.0);
        }

        public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
            add(axiom.getAxiomType(), 6.0);
        }

        public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
            add(axiom.getAxiomType(), 22.0);
        }

        public void visit(OWLInverseObjectPropertiesAxiom axiom) {
            add(axiom.getAxiomType(), 18.0);
        }

        public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
            add(axiom.getAxiomType(), 5.0);
        }

        public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
            add(axiom.getAxiomType(), 4.0);
        }

        public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
            add(axiom.getAxiomType(), 4.0);
        }

        public void visit(OWLObjectPropertyAssertionAxiom axiom) {
            add(axiom.getAxiomType(), 5.0);
        }

        public void visit(OWLSubPropertyChainOfAxiom axiom) {
            add(axiom.getAxiomType(), 10.0);
        }

        public void visit(OWLObjectPropertyDomainAxiom axiom) {
            add(axiom.getAxiomType(), 12.0);
        }

        public void visit(OWLObjectPropertyRangeAxiom axiom) {
            add(axiom.getAxiomType(), 10.0);
        }

        public void visit(OWLSubObjectPropertyOfAxiom axiom) {
            add(axiom.getAxiomType(), 10.0);
//            if(!checker.getSubProperty().isAnonymous()) {
//                if(!checker.getSuperProperty().isAnonymous()) {
//                    add(checker.getAxiomType()+ "NamedLHSComplexRHS", 5.0);
//                }
//                else {
//                    add(checker.getAxiomType() + "NamedLHSComplexRHS", 20.0);
//                }
//            }
//            else {
//                if(!checker.getSuperProperty().isAnonymous()) {
//                    add(checker.getAxiomType() + "ComplexLHSNamedRHS", 10.0);
//                }
//                else {
//                    add(checker.getAxiomType() + "ComplexLHSComplexRHS", 20.0);
//                }
//
//            }
        }

        public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
            add(axiom.getAxiomType(), 1.0);
        }

        public void visit(OWLSameIndividualAxiom axiom) {
            add(axiom.getAxiomType(), 1.0);
        }

        public void visit(OWLSubClassOfAxiom axiom) {
            if(axiom.getSubClass() instanceof OWLObjectComplementOf && axiom.getSuperClass() instanceof OWLObjectComplementOf) {
                add(axiom.getAxiomType(), 2.2);
            }
            else {
                add(axiom.getAxiomType(), 2.0);
            }



//            add(checker.getAxiomType(), 2.0);
//            if (!checker.getSubClass().isAnonymous()) {
//                if(!checker.getSuperClass().isAnonymous()) {
//                    add(checker.getAxiomType() + "NamedLHSNamedRHS", 0.5);
//                }
//                else {
//                    add(checker.getAxiomType() + "NamedLHSComplexRHS", 5.0);
//                }
//            }
//            else {
//                if (checker.getSuperClass().isAnonymous()) {
//                    add(checker.getAxiomType() + "ComplexLHSComplexRHS", 10.0);
//                }
//                else {
//                    add(checker.getAxiomType() + "ComplexLHSNamedRHS", 20.0);
//                }
//            }
        }

        public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
            add(axiom.getAxiomType(), 10.0);
        }

        public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
            add(axiom.getAxiomType(), 1.0);
        }

        public void visit(SWRLRule rule) {
            add(rule, 1.0);
        }
    }

}
