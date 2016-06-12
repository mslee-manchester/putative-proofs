package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 28-Jun-2010
 */
public class KuduComplexityCalculator implements ComplexityCalculator {




    private List<ComplexityCalculator> complexityCalculators;

    private Map<String, Double> complexityBreakdownMap = new HashMap<String, Double>();


    public KuduComplexityCalculator(OWLReasonerFactory reasonerFactory) {
        complexityCalculators = new ArrayList<ComplexityCalculator>();
//        complexityCalculators.add(new AssertedAxiomComplexityCalculator());
        AxiomTypeComplexityCalculator.setDefaultTypeWeight(100);
        complexityCalculators.add(new AxiomTypeComplexityCalculator());
//        complexityCalculators.add(new CollapsingAxiomTypeComplexityCalculator());
//        complexityCalculators.add(new DataFlowComplexityCalculator());
//        DescriptionTypeComplexityCalculator.setWeighting(ClassExpressionType.OBJECT_SOME_VALUES_FROM, 20.0);
//        DescriptionTypeComplexityCalculator.setWeighting(ClassExpressionType.OBJECT_ALL_VALUES_FROM, 40.0);
//        for(ClassExpressionType type : ClassExpressionType.values()) {
//            DescriptionTypeComplexityCalculator.setWeighting(type, 100);
//        }
        complexityCalculators.add(new ClassExpressionTypeComplexityCalculator());

//        complexityCalculators.add(new ComplexityCalculator() {
//            public double computeComplexity(Explanation<OWLAxiom> explanation, OWLAxiom axiom, Set<OWLAxiom> axioms) {
//                for(OWLAxiom ax : axioms) {
//                    if(ax instanceof OWLInverseObjectPropertiesAxiom) {
//                        return 50;
//                    }
//                }
//                return 0;
//            }
//        });



//        complexityCalculators.add(new PolarityComplexityCalculator());
        SignatureDifferenceComplexityCalculator.setWeight(50.0);
        complexityCalculators.add(new SignatureDifferenceComplexityCalculator());
        ClassExpressionTypeDifferenceComplexityCalculator.setWeight(50.0);
        complexityCalculators.add(new ClassExpressionTypeDifferenceComplexityCalculator());
        ModalDepthComplexityCalculator.setWeight(50.0);
        complexityCalculators.add(new ModalDepthComplexityCalculator());
        PathComplexityCalculator.setPathWeight(10.0);
        complexityCalculators.add(new PathComplexityCalculator());
        complexityCalculators.add(new EmptyUniversalComplexityCalculator());
        complexityCalculators.add(new TrivialSatisfactionComplexityCalculator());
        complexityCalculators.add(new SynonymOfThingComplexityCalculator(reasonerFactory));
        complexityCalculators.add(new UnpairedDomainAxiomComplexityCalculator(reasonerFactory));
        complexityCalculators.add(new LaconicGCIComplexityCalculator(reasonerFactory));

//        complexityCalculators.add(new ComplexityCalculator() {
//            public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
//                for(OWLAxiom ax : axioms) {
//                    if(ax instanceof OWLSubObjectPropertyOfAxiom) {
//                        OWLSubObjectPropertyOfAxiom spa = (OWLSubObjectPropertyOfAxiom) ax;
//                        if(spa.getSuperProperty() instanceof OWLObjectInverseOf) {
//                            return 50.0;
//                        }
//                    }
//                }
//                return 0;
//            }
//        });
//        complexityCalculators.add(new ComplexityCalculator() {
//            public double computeComplexity(Explanation<OWLAxiom> explanation, OWLAxiom axiom, Set<OWLAxiom> axioms) {
//                double complexity = 0.0;
//                for(OWLAxiom ax : axioms) {
//                    for(OWLClassExpression ce : ax.getNestedClassExpressions()) {
//                        if(ce instanceof OWLRestriction) {
//                            OWLRestriction restriction = (OWLRestriction) ce;
//                            if(restriction.getProperty() instanceof OWLObjectPropertyExpression) {
//                                for(OWLAxiom ax2 : axioms) {
//                                    if(ax2 instanceof OWLInverseObjectPropertiesAxiom) {
//                                        if(((OWLInverseObjectPropertiesAxiom) ax2).getProperties().contains(restriction.getProperty())) {
//                                            complexity += 30.0;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                return complexity;
//            }
//        });


//        complexityCalculators.add(new ComplexityCalculator() {
//            public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
//                for(OWLAxiom ax : axioms) {
//                    if(ax instanceof OWLSubClassOfAxiom) {
//                        if(((OWLSubClassOfAxiom) ax).getSubClass().isAnonymous()) {
//                            return 40.0;
//                        }
//
//                    }
//                }
//                return 0;
//            }
//        });
    }


    public Map<String, Double> getComplexityBreakdownMap() {
        return new HashMap<String, Double>(complexityBreakdownMap);
    }

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        double result = 0.0;
        complexityBreakdownMap.clear();
//        System.out.println("COMPLEXITY...");
        // Is it trivial?
        if(entailment instanceof OWLSubClassOfAxiom && originalExplanation.getSize() == 1) {
            OWLSubClassOfAxiom entailmentSCA = (OWLSubClassOfAxiom) entailment;
            OWLAxiom ax = originalExplanation.getAxioms().iterator().next();
            if(ax instanceof OWLSubClassOfAxiom) {
                OWLSubClassOfAxiom sca = (OWLSubClassOfAxiom) ax;
                if(sca.getSubClass().asDisjunctSet().contains(entailmentSCA.getSubClass())) {
                    if(sca.getSuperClass().asConjunctSet().contains(entailmentSCA.getSuperClass())) {
                        System.out.println("TRIVIAL");
                        return 20.0;
                    }
                }
            }
            else if(ax instanceof OWLEquivalentClassesAxiom) {
                for(OWLClassExpression ce : ((OWLEquivalentClassesAxiom) ax).getClassExpressions()) {
                    if(ce.asConjunctSet().contains(entailmentSCA.getSuperClass())) {
                        if(((OWLEquivalentClassesAxiom) ax).getClassExpressions().contains(entailmentSCA.getSubClass())) {
                            return 30.0;
                        }
                    }
                }
            }
        }

        for(ComplexityCalculator calculator : complexityCalculators) {
            double comp = calculator.computeComplexity(originalExplanation, entailment, axioms);
            result += comp;
            complexityBreakdownMap.put(calculator.getClass().getSimpleName(), comp);

//            System.out.println(calculator.getClass().getSimpleName() + ": " + comp);
        }
//        System.out.println("    .... TOTAL: " + result);
        return result;
    }
//
//
//    private List<ComplexityCalculator> complexityCalculators;
//
//    public KuduComplexityCalculator(OWLReasonerFactory reasonerFactory) {
//        complexityCalculators = new ArrayList<ComplexityCalculator>();
//
//        complexityCalculators.add(new AxiomTypeComplexityCalculator());
//        SpecialCaseComplexityCalculator.setNonPairedDomainWeighting(50.0);
//        complexityCalculators.add(new SpecialCaseComplexityCalculator(reasonerFactory));
//        ClassExpressionTypeComplexityCalculator.setWeighting(ClassExpressionType.OBJECT_SOME_VALUES_FROM, 20.0);
//        ClassExpressionTypeComplexityCalculator.setWeighting(ClassExpressionType.OBJECT_ALL_VALUES_FROM, 30.0);
//        complexityCalculators.add(new ClassExpressionTypeComplexityCalculator());
//
//        SignatureDifferenceComplexityCalculator.setWeight(50.0);
//        complexityCalculators.add(new SignatureDifferenceComplexityCalculator());
//        ClassExpressionTypeDifferenceComplexityCalculator.setWeight(50.0);
//        complexityCalculators.add(new ClassExpressionTypeDifferenceComplexityCalculator());
//        ModalDepthComplexityCalculator.setWeight(50.0);
//        complexityCalculators.add(new ModalDepthComplexityCalculator());
//        complexityCalculators.add(new PathComplexityCalculator());
//        complexityCalculators.add(new ComplexityCalculator() {
//            public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
//                for(OWLAxiom ax : axioms) {
//                    if(ax instanceof OWLSubObjectPropertyOfAxiom) {
//                        OWLSubObjectPropertyOfAxiom spa = (OWLSubObjectPropertyOfAxiom) ax;
//                        if(spa.getSuperProperty() instanceof OWLObjectInverseOf) {
//                            return 50.0;
//                        }
//                    }
//                }
//                return 0;
//            }
//        });
//    }
//
//    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
//        double result = 0.0;
//        for(ComplexityCalculator calculator : complexityCalculators) {
//            double comp = calculator.computeComplexity(originalExplanation, entailment, axioms);
//            result += comp;
//        }
//        return result;
//    }
}
