package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorFactory;
import org.semanticweb.owl.explanation.api.ExplanationManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 09-Dec-2010
 */
public class LaconicGCIComplexityCalculator implements ComplexityCalculator {

    private static double weighting = 100.0;

    private OWLReasonerFactory rf;

    public LaconicGCIComplexityCalculator(OWLReasonerFactory rf) {
        this.rf = rf;
    }

    public static double getWeighting() {
        return weighting;
    }

    public static void setWeighting(double weighting) {
        LaconicGCIComplexityCalculator.weighting = weighting;
    }

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {


        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLDataFactory df = manager.getOWLDataFactory();
        Set<OWLAxiom> splitAxioms = new HashSet<OWLAxiom>();
        boolean containsGCI = false;
        for(OWLAxiom ax : axioms) {
            if(ax instanceof OWLEquivalentClassesAxiom) {
                OWLEquivalentClassesAxiom eca = (OWLEquivalentClassesAxiom) ax;
                for(OWLSubClassOfAxiom sca : eca.asOWLSubClassOfAxioms()) {
                    for(OWLClassExpression ce : sca.getSubClass().asDisjunctSet()) {
                        OWLSubClassOfAxiom splitSubClassAxiom = df.getOWLSubClassOfAxiom(ce, sca.getSuperClass());
                        splitAxioms.add(splitSubClassAxiom);
                        if(splitSubClassAxiom.isGCI()) {
                            containsGCI = true;
                        }
                    }
                }
            }
            else {
                if(ax instanceof OWLSubClassOfAxiom && ((OWLSubClassOfAxiom) ax).isGCI()) {
                    containsGCI = true;
                }
                splitAxioms.add(ax);
            }
        }
        if(!containsGCI) {
            return 0.0;
        }
        ExplanationGeneratorFactory<OWLAxiom> fac = ExplanationManager.createExplanationGeneratorFactory(rf);
        ExplanationGenerator<OWLAxiom> gen = fac.createExplanationGenerator(splitAxioms);
        long t0 = System.currentTimeMillis();
        System.out.println("Getting laconic justifications...");
        Set<Explanation<OWLAxiom>> expls = gen.getExplanations(entailment, 1);
        long t1 = System.currentTimeMillis();
        System.out.println("Got justifications in " + (t1 - t0) + " ms");
        if(!expls.isEmpty()) {
            double result = 0.0;
            Explanation<OWLAxiom> expl = expls.iterator().next();
            for(OWLAxiom ax : expl.getAxioms()) {
                if(ax instanceof OWLSubClassOfAxiom) {
                    OWLSubClassOfAxiom sca = (OWLSubClassOfAxiom) ax;
                    if(sca.getSubClass().isAnonymous()) {
                        result += weighting;
                    }
                }
            }
            return result;
        }
        else {
            // Something strange happened
            return 0;
        }
    }
}
