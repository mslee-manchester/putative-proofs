package org.semanticweb.owl.explanation.lemmas;

import org.semanticweb.owl.explanation.complexity.ComplexityCalculator;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owl.explanation.api.*;
import org.semanticweb.owl.explanation.impl.blackbox.EntailmentCheckerFactory;
import org.semanticweb.owl.explanation.impl.blackbox.checker.SatisfiabilityEntailmentCheckerFactory;
import org.semanticweb.owl.explanation.complexity.DefaultComplexityCalculator;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.ToStringRenderer;

import java.util.*;

import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

/*
 * Copyright (C) 2009, University of Manchester
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
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Information Management Group<br>
 * Date: 10-Jun-2009
 */
public class ProofGenerator {

    private OWLReasonerFactory reasonerFactory;

    private EntailmentCheckerFactory<OWLAxiom> checkerFactory;

    private OWLDataFactory dataFactory;

    private ComplexityCalculator complexityCalculator;

    public ProofGenerator(OWLReasonerFactory reasonerFactory, ComplexityCalculator complexityCalculator, OWLDataFactory dataFactory, EntailmentCheckerFactory<OWLAxiom> checkerFactory) {
        this.reasonerFactory = reasonerFactory;
        this.dataFactory = dataFactory;
        this.checkerFactory = checkerFactory;
        this.complexityCalculator = complexityCalculator;
    }

    public Proof generateProof(Explanation<OWLAxiom> explanation) throws ExplanationException {

        Map<OWLAxiom, Explanation<OWLAxiom>> map = new HashMap<OWLAxiom, Explanation<OWLAxiom>>();
        Map<OWLAxiom, Explanation<OWLAxiom>> regularJustificationMap = new HashMap<OWLAxiom, Explanation<OWLAxiom>>();
        fill(explanation, map, explanation, regularJustificationMap);
        Proof proof = new Proof(explanation, map, regularJustificationMap);
        if(ProofGenerationPreferences.getInstances().isAttemptToCollapseProof()) {
            collapseProof(explanation, proof.getRoot(), proof, new HashSet<OWLAxiom>());
        }
        return proof;


    }


    private void fill(Explanation<OWLAxiom> mainExplanation, Map<OWLAxiom, Explanation<OWLAxiom>> map, Explanation<OWLAxiom> explanationToLemmatise, Map<OWLAxiom, Explanation<OWLAxiom>> regularJustificationMap) throws ExplanationException {
        if(map.containsKey(explanationToLemmatise.getEntailment())) {
            // Already done
            return;
        }
        LemmatisedJustificationGenerator lemJustGen = new LemmatisedJustificationGenerator(complexityCalculator, checkerFactory, reasonerFactory, dataFactory);
        Explanation<OWLAxiom> lemmatisedExplanation = lemJustGen.getLemmatisedExplanation(explanationToLemmatise, Collections.singleton(mainExplanation.getEntailment()));
        if(lemmatisedExplanation == null) {
            throw new RuntimeException("LEMMATISED EXPLANATION IS NULL (" + explanationToLemmatise + ")");
        }
//        System.out.println(lemmatisedExplanation);
//        System.out.println("--------------------------------------------------------------");
        map.put(explanationToLemmatise.getEntailment(), lemmatisedExplanation);
        for(OWLAxiom ax : lemmatisedExplanation.getAxioms()) {
            if(!mainExplanation.contains(ax)) {
                // We have a lemma
                ExplanationGeneratorFactory<OWLAxiom> explGenFac = ExplanationManager.createExplanationGeneratorFactory(checkerFactory);
                ExplanationGenerator<OWLAxiom> explanationGen = explGenFac.createExplanationGenerator(mainExplanation.getAxioms());
                Set<Explanation<OWLAxiom>> expls =  explanationGen.getExplanations(ax, 1);
                if (expls.size() > 0) {
                    Explanation<OWLAxiom> expl = expls.iterator().next();
                    regularJustificationMap.put(ax, expl);
                    if(expl.getAxioms().size() == 1 && mainExplanation.contains(expl.getAxioms().iterator().next())) {
                        map.put(ax, expl);
                    }
                    else {
                        fill(mainExplanation, map, expl, regularJustificationMap);
                    }
                }

            }
        }

    }

    private void collapseProof(Explanation<OWLAxiom> original, Explanation<OWLAxiom> explanation, Proof proof, Set<OWLAxiom> processed) {
        OWLAxiom entailment = explanation.getEntailment();
        if(processed.contains(entailment)) {
            return;
        }
        processed.add(entailment);
        Set<OWLAxiom> collapsed = new HashSet<OWLAxiom>();
        for(OWLAxiom ax : explanation.getAxioms()) {
            Explanation<OWLAxiom> childExplanation = proof.getExplanation(ax);
            if(childExplanation != null) {
                // ax is a lemma
                collapsed.addAll(childExplanation.getAxioms());
            }
            else {
                collapsed.add(ax);
            }
        }
        // Is the collapsed explanation any more complex than the explanation? If not, replace.
        double originalComplexity = complexityCalculator.computeComplexity(original, explanation.getEntailment(), explanation.getAxioms());
        double collapsedComplexity = complexityCalculator.computeComplexity(original, entailment, collapsed);
        System.out.println("ORIGINAL : " + originalComplexity);
        System.out.println("COLLAPSED: " + collapsedComplexity);
        if(!(collapsedComplexity > originalComplexity)) {
            Explanation<OWLAxiom> collapsedExplanation = new Explanation<OWLAxiom>(entailment, collapsed);
            proof.replace(explanation, collapsedExplanation);
            collapseProof(original, collapsedExplanation, proof, processed);
        }
        else {
            for(OWLAxiom ax : explanation.getAxioms()) {
                Explanation<OWLAxiom> childExplanation = proof.getExplanation(ax);
                if(childExplanation != null) {
                    collapseProof(original, childExplanation, proof, processed);
                }
            }
        }

        // Move on
    }

    private void collapse(Explanation<OWLAxiom> originalExplanation, OWLAxiom axiom, Proof proof) {
        System.out.println("ATTEMPTING TO COLLAPSE EXPL:");
        if(originalExplanation.contains(axiom)) {
            return;
        }
        Explanation<OWLAxiom> explanation = proof.getExplanation(axiom);
        System.out.println(explanation);
        for(OWLAxiom childAx : explanation.getAxioms()) {
            Set<OWLAxiom> candidate = new HashSet<OWLAxiom>();
            if(originalExplanation.contains(childAx)) {
                candidate.add(childAx);
            }
            else {
                for(OWLAxiom grandChildAx : proof.getExplanation(childAx).getAxioms()) {
                    candidate.add(grandChildAx);
                }
            }
            ExplanationGenerator<OWLAxiom> gen = ExplanationManager.createExplanationGeneratorFactory(reasonerFactory).createExplanationGenerator(candidate);
            Set<Explanation<OWLAxiom>> expls = gen.getExplanations(explanation.getEntailment());
            if(expls.size() == 1) {
                // We can collapse
                // TODO: Check contains all axioms?
                Explanation<OWLAxiom> collapsedExpl = expls.iterator().next();
                double orginalComplexity = complexityCalculator.computeComplexity(originalExplanation, explanation.getEntailment(), explanation.getAxioms());
                double collapsedComplexity = complexityCalculator.computeComplexity(originalExplanation, explanation.getEntailment(), collapsedExpl.getAxioms());
                if(collapsedComplexity <= orginalComplexity) {
                    System.out.println("Collapsing is possible");
                    System.out.println("ORIGINAL: ");
                    System.out.println(explanation);
                    System.out.println("COLLAPSED:");
                    System.out.println(collapsedExpl);
                    // Replace
                    proof.replace(explanation, collapsedExpl);
                    collapse(originalExplanation, explanation.getEntailment(), proof);
                }
                else {
                    for(OWLAxiom ax : explanation.getAxioms()) {
                        collapse(originalExplanation, ax, proof);
                    }
                }
            }

        }
    }

    public static void main(String[] args) {
        try {
//                   OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();
            OWLReasonerFactory reasonerFactory = null;//new FaCTPlusPlusReasonerFactory();
//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=file:/Users/seanb/Desktop/Cercedilla2005/hands-on/people.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://owl.man.ac.uk/2005/07/sssw/people#");


//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://miniTambis&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://miniTambis#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.owl-ontologies.com/generations.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.owl-ontologies.com/generations.owl#");
//

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://reliant.teknowledge.com/DAML/Economy.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://reliant.teknowledge.com/DAML/Economy.owl#");
//            pm.setPrefix("m:", "http://reliant.teknowledge.com/DAML/Mid-level-ontology.owl#");
//            pm.setPrefix("s:", "http://reliant.teknowledge.com/DAML/SUMO.owl#");
//
//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.co-ode.org/nautilus&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.co-ode.org/nautilus#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://reliant.teknowledge.com/DAML/Transportation.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://reliant.teknowledge.com/DAML/Mid-level-ontology.owl#");
//            pm.setPrefix("t:", "http://reliant.teknowledge.com/DAML/Transportation.owl#");
//            pm.setPrefix("s:", "http://reliant.teknowledge.com/DAML/SUMO.owl#");


//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.mindswap.org/ontologies/debugging/university.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.mindswap.org/ontologies/debugging/university.owl#");


//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://ontology.dumontierlab.com/periodic-table-complex&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://ontology.dumontierlab.com/");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://sweet.jpl.nasa.gov/1.1/earthrealm.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://sweet.jpl.nasa.gov/1.1/earthrealm.owl#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.semanticweb.org/ontolgies/chemical&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.semanticweb.org/ontolgies/chemical#");
//

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.loa-cnr.it/Files/DLPOnts/DOLCE_Lite_397.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.loa-cnr.it/ontologies/DOLCE-Lite#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.mygrid.org.uk/ontology/mygrid-unclassified&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.mygrid.org.uk/ontology#");



//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.owl-ontologies.com/Movie.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.owl-ontologies.com/Movie.owl#");


            IRI ontologyIRI = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.owl-ontologies.com/Movie.owl#");


//            URI ontologyURI = URI.create("file:/Users/matthewhorridge/Desktop/smallmovie.owl");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.semanticweb.org/ontologies/smallmovie#");

//            URI ontologyURI = URI.create("file:/Users/matthewhorridge/Desktop/MolcOnto_Protege4.owl");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.semanticweb.org/ontologies/2009/6/21/Ontology1248147637258.owl#");


//            URI ontologyURI = URI.create("file:/Users/matthewhorridge/Desktop/complexitytest.owl");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.semanticweb.org/ontologies/2009/6/21/Ontology1248147637258.owl#");


//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://protege.stanford.edu/plugins/owl/owl-library/koala.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://owl.man.ac.uk/2005/07/sssw/people#");

//            URI ontologyURI = URI.create("file:/Users/matthewhorridge/Desktop/MolcOnto_Protege4.owl");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.semanticweb.org/ontologies/2009/6/21/Ontology1248147637258.owl#");



            SimpleRenderer simpleRenderer = new SimpleRenderer();
            simpleRenderer.setShortFormProvider(pm);
            ToStringRenderer.getInstance().setRenderer(simpleRenderer);

             DLSyntaxObjectRenderer dlRen = new DLSyntaxObjectRenderer();
//
            ToStringRenderer.getInstance().setRenderer(dlRen);

            OWLOntologyManager man = OWLManager.createOWLOntologyManager();
            OWLOntology ont = man.loadOntologyFromOntologyDocument(ontologyIRI);


            System.out.println("Loaded ontology");

            OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
            reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

            Set<OWLAxiom> entailments = new HashSet<OWLAxiom>();


            InferredAxiomGenerator<OWLSubClassOfAxiom> iag = new InferredSubClassAxiomGenerator();
            Set<OWLSubClassOfAxiom> scas = iag.createAxioms(man, reasoner);
            for (OWLSubClassOfAxiom sca : scas) {
                if (!ont.containsAxiom(sca)) {
                    entailments.add(sca);
                }
            }
            InferredAxiomGenerator<OWLClassAssertionAxiom> iag2 = new InferredClassAssertionAxiomGenerator();
            for (OWLClassAssertionAxiom ax : iag2.createAxioms(man, reasoner)) {
                if (!ont.containsAxiom(ax)) {
                    entailments.add(ax);
                }
            }
            for (OWLClass cls : reasoner.getUnsatisfiableClasses()) {
                entailments.add(man.getOWLDataFactory().getOWLSubClassOfAxiom(cls, man.getOWLDataFactory().getOWLNothing()));
            }

//            entailments.add(man.getOWLDataFactory().getOWLSubClassOfAxiom("owl:Thing", "PublishedWork", new DefaultNamespaceManager(ont.getURI() + "#")));

            ExplanationGeneratorFactory<OWLAxiom> explanationGeneratorFactory = ExplanationManager.createExplanationGeneratorFactory(reasonerFactory);
            ExplanationGenerator<OWLAxiom> expGen = explanationGeneratorFactory.createExplanationGenerator(ont.getLogicalAxioms());


            int count = 0;
            for (OWLAxiom ent : entailments) {
                count++;
                int expCount = 0;
                for (Explanation<OWLAxiom> exp : expGen.getExplanations(ent)) {


                    expCount++;
                    System.out.println("Entailment " + count + " of " + entailments.size() + "  Explanation " + expCount);
                    try {

                        System.out.println("***********************************************************************");
                        System.out.println("Basic explanation: ");
                        DefaultComplexityCalculator calc = new DefaultComplexityCalculator(reasonerFactory);
                        System.out.println(exp);

                        long t0 = System.currentTimeMillis();
                        ProofGenerator proofGenerator = new ProofGenerator(reasonerFactory, calc, man.getOWLDataFactory(), new SatisfiabilityEntailmentCheckerFactory(reasonerFactory));
                        Proof proof = proofGenerator.generateProof(exp);
                        long t1 = System.currentTimeMillis();
                        System.out.println(proof);
//                        proof.writeTreeML();
                        System.out.println("Computed in " + (t1 - t0) + " ms");
                        System.out.println("Jst Complexity: " + calc.computeComplexity(exp, exp.getEntailment(), exp.getAxioms()));                        
                        System.out.println("Max complexity: " + proof.getMaxComplexity(calc));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
