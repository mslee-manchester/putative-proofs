package org.semanticweb.owl.explanation.proofs;

import org.semanticweb.owl.explanation.api.*;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 28-Jun-2010
 */
public class ISWC2010Tests {

    public static void main(String[] args) {
//        try {
//            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//
//            SatisfiabilityEntailmentChecker.setTimeOut(60000);
//
////            OWLReasonerFactory reasonerFactory = new FaCTPlusPlusReasonerFactory();
//            OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();
//
//            KuduComplexityCalculator compCalc = new KuduComplexityCalculator(reasonerFactory);
//            EntailmentCheckerFactory<OWLAxiom> cf = new SatisfiabilityEntailmentCheckerFactory(reasonerFactory);
//
//            IRI generationsIRI = IRI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.owl-ontologies.com/generations.owl");
//            IRI economyIRI = IRI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://reliant.teknowledge.com/DAML/Economy.owl&format=RDF/XML");
//            IRI peopleIRI = IRI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=file:/Users/seanb/Desktop/Cercedilla2005/hands-on/people.owl&format=RDF/XML");
//            IRI nautilusIRI = IRI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.co-ode.org/nautilus&format=RDF/XML");
//            IRI transportIRI = IRI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://reliant.teknowledge.com/DAML/Transportation.owl&format=RDF/XML");
//            IRI universityIRI = IRI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.mindswap.org/ontologies/debugging/university.owl&format=RDF/XML");
//            IRI periodicTableIRI = IRI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://ontology.dumontierlab.com/periodic-table-complex&format=RDF/XML");
//            IRI chemicalIRI = IRI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.semanticweb.org/ontolgies/chemical&format=RDF/XML");
//            IRI dolceIRI = IRI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.loa-cnr.it/Files/DLPOnts/DOLCE_Lite_397.owl&format=RDF/XML");
//            IRI tambisIRI = IRI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.cs.manchester.ac.uk/owl/ontologies/tambis-patched.owl&format=RDF/XML");
//
//            IRI documentIRI = tambisIRI;
//
//            OWLOntology ont = manager.loadOntologyFromOntologyDocument(documentIRI);
//
//
//            for(OWLDatatype datatype : new ArrayList<OWLDatatype>(ont.getDatatypesInSignature())) {
//                manager.removeAxioms(ont, new HashSet<OWLAxiom>(ont.getReferencingAxioms(datatype)));
//            }
//
//            OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
//
//            ExplanationGeneratorFactory<OWLAxiom> expGenFac = ExplanationManager.createExplanationGeneratorFactory(reasonerFactory);
//            ExplanationGenerator<OWLAxiom> expGen = expGenFac.createExplanationGenerator(ont);
//
//
//            InferredSubClassAxiomGenerator subGen = new InferredSubClassAxiomGenerator();
//            Set<OWLAxiom> entailments = new HashSet<OWLAxiom>();
//            for (OWLSubClassOfAxiom sca : subGen.createAxioms(manager, reasoner)) {
//                if (!ont.containsAxiom(sca) && !sca.getSuperClass().isOWLThing()) {
//                    entailments.add(sca);
//                }
//            }
//            InferredClassAssertionAxiomGenerator assGen = new InferredClassAssertionAxiomGenerator();
//            for(OWLClassAssertionAxiom ax : assGen.createAxioms(manager, reasoner)) {
//                if(!ont.containsAxiom(ax) && !ax.getClassExpression().isOWLThing()) {
//                    entailments.add(ax);
//                }
//            }
//            for (OWLClass cls : reasoner.getUnsatisfiableClasses().getEntitiesMinusBottom()) {
//                OWLDataFactory df = manager.getOWLDataFactory();
//                entailments.add(df.getOWLSubClassOfAxiom(cls, df.getOWLNothing()));
//            }
//
//            for (OWLAxiom entailment : entailments) {
//                long expT0 = System.currentTimeMillis();
//                Set<Explanation<OWLAxiom>> expls = expGen.getExplanations(entailment, 5);
//                long expT1 = System.currentTimeMillis();
////                flushExplanationTime(ont, entailment, expls, (expT1 - expT0));
//                flushExplanations(ont, expls);
////                for (Explanation<OWLAxiom> expl : expls) {
////                    try {
////                        double originalComplexity = compCalc.computeComplexity(expl, expl.getEntailment(), expl.getAxioms());
////
////                        ProofGenerator proofGenerator = new ProofGenerator(reasonerFactory, compCalc, manager.getOWLDataFactory(), cf);
////                        long t0 = System.currentTimeMillis();
////                        Proof proof = proofGenerator.generateProof(expl);
////                        System.out.println(proof);
////                        Map<OWLAxiom, Explanation<OWLAxiom>> explanations = proof.getMap();
////                        long t1 = System.currentTimeMillis();
////                        long time = (t1 - t0);
////                        double totalComplexity = 0.0;
////                        for(OWLAxiom ax : explanations.keySet()) {
////                            Explanation<OWLAxiom> subExpl = explanations.get(ax);
////                            double complexity = compCalc.computeComplexity(expl, subExpl.getEntailment(), subExpl.getAxioms());
////                            totalComplexity = totalComplexity + complexity;
////                        }
////                        double averageComplexity = totalComplexity / explanations.keySet().size();
////                        flushProofTime(ont, entailment, time, originalComplexity, averageComplexity);
////                    }
////                    catch (RuntimeException e) {
////                        e.printStackTrace();
////                    }
////                }
//            }
//            reasoner.dispose();
//        }
//        catch (OWLOntologyCreationException e) {
//            e.printStackTrace();
//        }


    }

    private static void flushExplanations(OWLOntology ont, Set<Explanation<OWLAxiom>> expls) {
        try {
            File file = new File("/tmp/proof-experiments-expls.csv");
            FileWriter fileWriter = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fileWriter, true);
            for (Explanation<OWLAxiom> expl : expls) {
                pw.print("EXPL, ");
                pw.print(ont.getOntologyID().getOntologyIRI().toQuotedString());
                pw.print(", ");
                pw.println(expl.getAxioms().size());
            }
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void flushExplanationTime(OWLOntology ont, OWLAxiom entailment, Set<Explanation<OWLAxiom>> expls, long time) {
        try {
            File file = new File("/tmp/proof-experiments.csv");
            FileWriter fileWriter = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fileWriter, true);
            pw.print("EXPL, ");
            pw.print(ont.getOntologyID().getOntologyIRI().toQuotedString());
            pw.print(", ");
            pw.print(entailment);
            pw.print(", ");
            pw.print(expls.size());
            pw.print(", ");
            pw.println(time);
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void flushProofTime(OWLOntology ont, OWLAxiom entailment, long proofTime, double originalComplexity, double averageProofStepComplexity) {
        try {
            File file = new File("/tmp/proof-experiments.csv");
            FileWriter fileWriter = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fileWriter, true);
            pw.print("PROOF, ");
            pw.print(ont.getOntologyID().getOntologyIRI().toQuotedString());
            pw.print(", ");
            pw.print(entailment);
            pw.print(", ");
            pw.print(originalComplexity);
            pw.print(", ");
            pw.print(averageProofStepComplexity);
            pw.print(", ");
            pw.println(proofTime);
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


}
