package org.semanticweb.owl.explanation.lemmas;

import org.semanticweb.owl.explanation.api.*;
import org.semanticweb.owl.explanation.complexity.ComplexityCalculator;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.impl.blackbox.EntailmentCheckerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.*;

import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;/*
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
 * Date: 22-Dec-2008
 */
public class LemmatisedJustificationGenerator {

    private Explanation<OWLAxiom> originalExplanation;

    private EntailmentCheckerFactory<OWLAxiom> checkerFactory;

    private OWLReasonerFactory reasonerFactory;

    Collection<Explanation<OWLAxiom>> foundExplanations = new HashSet<Explanation<OWLAxiom>>();

//    private double lastFoundComplexity;

    private OWLDataFactory dataFactory;

    private ComplexityCalculator complexityCalculator;

    public LemmatisedJustificationGenerator(ComplexityCalculator complexityCalculator, EntailmentCheckerFactory<OWLAxiom> checkerFactory, OWLReasonerFactory reasonerFactory, OWLDataFactory dataFactory) {
        this.checkerFactory = checkerFactory;
        this.reasonerFactory = reasonerFactory;
        this.dataFactory = dataFactory;
        this.complexityCalculator = complexityCalculator;
    }

    public Explanation<OWLAxiom> getLemmatisedExplanation(Explanation<OWLAxiom> explanation, Set<OWLAxiom> excludeLemmas) throws ExplanationException {

        originalExplanation = explanation;

        foundExplanations.clear();
        foundExplanations.add(explanation);

        System.out.println("Getting lemmatised justification for " + explanation);
        System.out.println("        Search limit: " + LemmatisationPreferences.getInstance().getJustificationSearchLimit());
//        DefaultComplexityCalculator complexityCalculator = new DefaultComplexityCalculator(reasonerFactory);
        double compl = complexityCalculator.computeComplexity(explanation, (OWLAxiom) explanation.getEntailment(), explanation.getAxioms());
        System.out.println("        Start complexity: " + compl);
        Explanation<OWLAxiom> lemExp = findLemmatisedExplanation(explanation, excludeLemmas);
        int round = 0;
        while (true) {
            round++;
            Explanation<OWLAxiom> nLemExp = findLemmatisedExplanation(lemExp, excludeLemmas);
            if (nLemExp != null) {
                if (nLemExp.equals(lemExp)) {
                    System.out.println("RETURNED SAME LEMMATISED (Round " + round + ")");
                    break;
                }
                lemExp = nLemExp;
            }
            else {
                System.out.println("COULD NOT LEMMATISE");
                break;
            }
        }
        return lemExp;
    }

    private Set<Explanation<OWLAxiom>> getExplanations(Set<OWLAxiom> axioms, OWLAxiom entailment, int limit, final long timeout) {
        System.out.println("Getting regular explanations...");
        ExplanationGeneratorFactory<OWLAxiom> fac = ExplanationManager.createExplanationGeneratorFactory(reasonerFactory);
        final long start = System.currentTimeMillis();
        ExplanationGenerator<OWLAxiom> gen = fac.createExplanationGenerator(axioms, new ExplanationProgressMonitor<OWLAxiom>() {
            public void foundExplanation(ExplanationGenerator<OWLAxiom> owlAxiomExplanationGenerator, Explanation<OWLAxiom> explanation, Set<Explanation<OWLAxiom>> allFoundExplanations) {
                System.out.println("Found explanation " + allFoundExplanations.size());
            }

            public boolean isCancelled() {
                long time = System.currentTimeMillis();
                long diff = time - start;
                return diff > timeout;
            }
        });
        System.out.println("    .... Got regular explanation");
        System.out.println("Finding laconic explanations...");
        Set<Explanation<OWLAxiom>> expls = gen.getExplanations(entailment, limit);
        System.out.println("        .... got basic explanations");
        Set<Explanation<OWLAxiom>> lacExpls = new HashSet<Explanation<OWLAxiom>>();
        ExplanationGeneratorFactory<OWLAxiom> lacFac = ExplanationManager.createLaconicExplanationGeneratorFactory(reasonerFactory);
        for(Explanation<OWLAxiom> expl : expls) {
            ExplanationGenerator<OWLAxiom> lacGen = lacFac.createExplanationGenerator(expl.getAxioms(), new ConsoleExplanationProgressMonitor<OWLAxiom>());
            System.out.println("Getting explanation from: ");
            System.out.println(expl);
            lacExpls.addAll(lacGen.getExplanations(expl.getEntailment(), 1));
            System.out.println("    .... got it");
        }
        System.out.println("    .... found laconic explanations");
        return lacExpls;
    }

    private Explanation<OWLAxiom> findLemmatisedExplanation(final Explanation<OWLAxiom> explanation, Set<OWLAxiom> excludeLemmas) throws ExplanationException {
        if (explanation == null || explanation.isEmpty()) {
            return explanation;
        }

        // We generate lemmatised explanations in the following way
        // 1) Compute a laconic justification to eliminate redundant entailments
        // 2) Compute candidate lemmas
        // 3) Compute justifications from the candidate lemmas
        // 4) Choose a justification using the complexity model

        // Generate laconic justification - we do this so that we eliminate the redundant entailments
//        foundExplanation = explanation;
//        final DefaultComplexityCalculator cc = new DefaultComplexityCalculator(reasonerFactory);
        double lastFoundComplexity = complexityCalculator.computeComplexity(originalExplanation, (OWLAxiom) explanation.getEntailment(), explanation.getAxioms());

        ExplanationGeneratorFactory<OWLAxiom> explanationGeneratorFactory;

//        if (LemmatisationPreferences.getInstance().isUseLaconicJustifications()) {
//            explanationGeneratorFactory = ExplanationManager.createLaconicExplanationGeneratorFactory(checkerFactory);
//        }
//        else {
//            explanationGeneratorFactory = ExplanationManager.createExplanationGeneratorFactory(checkerFactory);
//        }


//        ExplanationGenerator<OWLAxiom> explanationGenerator = explanationGeneratorFactory.createExplanationGenerator(explanation.getAxioms());
//        Set<Explanation<OWLAxiom>> laconicExplanations = explanationGenerator.getExplanations(explanation.getEntailment(), 1);
//        Set<Explanation<OWLAxiom>> laconicExplanations = getExplanations(explanation.getAxioms(), explanation.getEntailment(), Integer.MAX_VALUE, Long.MAX_VALUE);
        Set<Explanation<OWLAxiom>> laconicExplanations = getExplanations(explanation.getAxioms(), explanation.getEntailment(), 1, 20000);

        Explanation<OWLAxiom> smallest = null;
        int subConceptSize = Integer.MAX_VALUE;
        for(Explanation<OWLAxiom> expl : laconicExplanations) {
            if(smallest == null) {
                smallest = expl;
                subConceptSize = expl.getNestedClassExpressions().size();
            }
            else {
                int size = expl.getNestedClassExpressions().size();
                if(size < subConceptSize) {
                    smallest = expl;
                    subConceptSize = size;
                }
            }
        }


        final Explanation<OWLAxiom> laconEx;
        if (laconicExplanations.isEmpty()) {
            laconEx = explanation;
        }
        else {
            laconEx = smallest;
        }

        // Generate lemmas
        DefaultCandidateLemmaGenerator lemmaGenerator = new DefaultCandidateLemmaGenerator(reasonerFactory, dataFactory);
        Set<OWLAxiom> lemmas = new HashSet<OWLAxiom>();
        lemmas.addAll(lemmaGenerator.getCandidateLemmas(laconEx));
        lemmas.remove(explanation.getEntailment());


        // Find justifications
        for (Explanation<OWLAxiom> laconicExplanation : laconicExplanations) {
            lemmas.addAll(laconicExplanation.getAxioms());
        }

        lemmas.removeAll(excludeLemmas);

        System.out.println("THE LEMMAS ARE:");
        for (OWLAxiom lemma : lemmas) {
            System.out.println("\t" + lemma);
        }


        ExplanationGeneratorFactory<OWLAxiom> explanationGeneratorFactory2;

//        if (LemmatisationPreferences.getInstance().isUseLaconicJustifications()) {
//            explanationGeneratorFactory2 = ExplanationManager.createLaconicExplanationGeneratorFactory(checkerFactory);
//        }
//        else {
//            explanationGeneratorFactory2 = ExplanationManager.createExplanationGeneratorFactory(checkerFactory);
//        }


//        ExplanationGenerator<OWLAxiom> gen2 = explanationGeneratorFactory2.createExplanationGenerator(lemmas, new ExplanationProgressMonitor<OWLAxiom>() {
//
//            private long start = System.currentTimeMillis();
//
//            public void foundExplanation(ExplanationGenerator<OWLAxiom> owlAxiomExplanationGenerator, Explanation<OWLAxiom> explanation, Set<Explanation<OWLAxiom>> allFoundExplanations) {
//            }
//
//            public boolean isCancelled() {
//
//                long t1 = System.currentTimeMillis();
//                long diff = t1 - start;
//                boolean b = diff > 10000;
//                if (b) {
//                    System.out.println("TIMING OUT: Found " + foundExplanations.size());
//                }
//                return b;
//            }
//        });
        int limit = LemmatisationPreferences.getInstance().getJustificationSearchLimit();
        System.out.println("Trying to find justifications....");
        Set<Explanation<OWLAxiom>> expls = getExplanations(lemmas, explanation.getEntailment(), limit, 10000);

//        ExplanationGeneratorFactory<OWLAxiom> laconicExplanationGeneratorFactory = ExplanationManager.createLaconicExplanationGeneratorFactory(reasonerFactory);

//        Set<Explanation<OWLAxiom>> lacExpls = new HashSet<Explanation<OWLAxiom>>();
//        lacExpls.addAll(expls);
//        for(Explanation<OWLAxiom> expl : expls) {
//            ExplanationGenerator<OWLAxiom> lacExpGen = laconicExplanationGeneratorFactory.createExplanationGenerator(expl.getAxioms());
//            lacExpls.addAll(lacExpGen.getExplanations(expl.getEntailment()));
//        }


//        lacExpls = addMergedJustifications(lacExpls);


        Set<Explanation<OWLAxiom>> lowestComplexityCandidates = new HashSet<Explanation<OWLAxiom>>();

        double lowestComplexity = lastFoundComplexity;

        Map<Explanation<OWLAxiom>, Double> complexityMap = new HashMap<Explanation<OWLAxiom>, Double>();

        System.out.println("THE CANDIDATES ARE:");
        for (Explanation<OWLAxiom> expl : expls) {
            System.out.println(expl);
            double comp = complexityCalculator.computeComplexity(originalExplanation, (OWLAxiom) explanation.getEntailment(), expl.getAxioms());
            System.out.println("HAS A COMPLEXITY OF " + comp);
            complexityMap.put(expl, comp);
            System.out.println();
            if (comp < lowestComplexity) {
                lowestComplexity = comp;
            }
        }

        for (Explanation<OWLAxiom> expl : complexityMap.keySet()) {
            double comp = complexityMap.get(expl);
            if (comp == lowestComplexity) {
                lowestComplexityCandidates.add(expl);
            }
        }

        Explanation<OWLAxiom> candidate = null;
        System.out.println("/////////////////////////////////////////////////////////////////");
        System.out.println("GOT SOME CANDIDATES OF EQUAL COMPLEXITY:");
        for (Explanation<OWLAxiom> expl : lowestComplexityCandidates) {
            System.out.println(expl);
            if (candidate == null) {
                candidate = expl;
            }
            else {
                if (expl.getAxioms().size() < candidate.getAxioms().size()) {
                    candidate = expl;
                }
            }
        }
        System.out.println("/////////////////////////////////////////////////////////////////");

        System.out.println("/////////////////////////////////////////////////////////////////");
        System.out.println("EQUAL COMPLEXITY CANDIDATE INSPECTION:");
        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        });
        double lowestCompl = Double.MAX_VALUE;
        Explanation<OWLAxiom> lowestExpl = candidate;
        for(Explanation<OWLAxiom> expl : lowestComplexityCandidates) {
            System.out.println(expl);
            System.out.println("\tExplanation Size: " + expl.getSize());
            for(OWLAxiom ax : expl.getAxioms()) {
                double totalComl = 0.0;
                if (!explanation.contains(ax)) {
                    System.out.println("\t\tINSPECTION OF " + ax);
                    Set<Explanation<OWLAxiom>> inspecExpls = getExplanations(explanation.getAxioms(), ax, 5, 60000);
                    System.out.println("\t\t\tThere are " + inspecExpls.size() + " regular explanations");
                    int count = 0;
                    for(Explanation<OWLAxiom> inspecExpl : inspecExpls) {
                        count++;
                        System.out.println("\t\t\t" + count + ": Size = " + inspecExpl.getSize());
                        PrintStream out = System.out;
                        System.setOut(ps);
                        double inspCompl = complexityCalculator.computeComplexity(originalExplanation, inspecExpl.getEntailment(), inspecExpl.getAxioms());
                        totalComl = totalComl + inspCompl;
                        System.setOut(out);
                        System.out.println("\t\t\tWHICH HAS A COMPLEXITY OF: " + inspCompl);

                    }
                }
                double aveCompl = totalComl / expl.getSize();
                if(aveCompl < lowestCompl) {
                    lowestCompl = aveCompl;
                    lowestExpl = expl;
                }
            }
        }
        System.out.println("/////////////////////////////////////////////////////////////////");


//        if (candidate == null) {
//            candidate = lowestExpl;
//        }
        if(lowestExpl == null) {
            lowestExpl = explanation;
        }
        System.out.println("CHOSEN:");
        System.out.println(lowestExpl);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return lowestExpl;
    }

    private Set<Explanation<OWLAxiom>> addMergedJustifications(Set<Explanation<OWLAxiom>> expls) {
        return expls;
//        Set<Explanation<OWLAxiom>> mergedExpls = new HashSet<Explanation<OWLAxiom>>();
//        for (Explanation<OWLAxiom> expl : expls) {
//            Explanation<OWLAxiom> mergedExpl = getMergedJustification(expl);
//            if (!mergedExpl.isJustificationEntailment()) {
//                mergedExpls.add(mergedExpl);
//            }
//        }
//        return mergedExpls;
    }

    private Explanation<OWLAxiom> getMergedJustification(Explanation<OWLAxiom> expl) {
        Map<OWLClassExpression, Set<OWLClassExpression>> classExpressionsByLHS = new HashMap<OWLClassExpression, Set<OWLClassExpression>>();
        Set<OWLAxiom> otherAxioms = new HashSet<OWLAxiom>();
        Set<OWLAxiom> newAxioms = new HashSet<OWLAxiom>();

        for (OWLAxiom ax : expl.getAxioms()) {
            if (ax instanceof OWLSubClassOfAxiom) {
                OWLSubClassOfAxiom sca = (OWLSubClassOfAxiom) ax;
                OWLClassExpression lhs = sca.getSubClass();
                Set<OWLClassExpression> rhsConjuncts = classExpressionsByLHS.get(lhs);
                if (rhsConjuncts == null) {
                    rhsConjuncts = new HashSet<OWLClassExpression>();
                    classExpressionsByLHS.put(lhs, rhsConjuncts);
                }
                rhsConjuncts.addAll(sca.getSuperClass().asConjunctSet());
            }
            else {
                otherAxioms.add(ax);
            }
        }
        newAxioms.addAll(otherAxioms);
        // Now combine the rhs
        for (OWLClassExpression lhs : classExpressionsByLHS.keySet()) {
            Set<OWLClassExpression> rhsConjuncts = classExpressionsByLHS.get(lhs);
            OWLSubClassOfAxiom newSca;
            if (rhsConjuncts.size() == 1) {
                OWLClassExpression rhs = rhsConjuncts.iterator().next();
                newSca = dataFactory.getOWLSubClassOfAxiom(lhs, rhs);
            }
            else {
                OWLObjectIntersectionOf rhs = dataFactory.getOWLObjectIntersectionOf(rhsConjuncts);
                newSca = dataFactory.getOWLSubClassOfAxiom(lhs, rhs);
            }
            newAxioms.add(newSca);
        }
        return new Explanation<OWLAxiom>(expl.getEntailment(), newAxioms);
    }

    //

    public static void main(String[] args) {
//        try {
//            OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();
//            OWLReasonerFactory reasonerFactory = new FaCTPlusPlusReasonerFactory();
//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=file:/Users/seanb/Desktop/Cercedilla2005/hands-on/people.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://owl.man.ac.uk/2005/07/sssw/people#");

        IRI ontologyURI = IRI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://miniTambis&format=RDF/XML");
        DefaultPrefixManager pm = new DefaultPrefixManager("http://miniTambis#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.owl-ontologies.com/generations.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.owl-ontologies.com/generations.owl#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://reliant.teknowledge.com/DAML/Economy.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://reliant.teknowledge.com/DAML/Economy.owl#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.co-ode.org/nautilus&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.co-ode.org/nautilus#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://reliant.teknowledge.com/DAML/Transportation.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://reliant.teknowledge.com/DAML/Mid-level-ontology.owl#");
//            pm.setPrefix("tr:", "http://reliant.teknowledge.com/DAML/Transportation.owl#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.mindswap.org/ontologies/debugging/university.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.mindswap.org/ontologies/debugging/university.owl#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://ontology.dumontierlab.com/periodic-table-complex&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://ontology.dumontierlab.com/");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://sweet.jpl.nasa.gov/1.1/earthrealm.owl&format=RDF/XML");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.semanticweb.org/ontolgies/chemical&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.semanticweb.org/ontolgies/chemical#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.loa-cnr.it/Files/DLPOnts/DOLCE_Lite_397.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.loa-cnr.it/ontologies/DOLCE-Lite#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.mygrid.org.uk/ontology/mygrid-unclassified&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.mygrid.org.uk/ontology#");

//            URI ontologyURI = URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.owl-ontologies.com/Movie.owl&format=RDF/XML");
//            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.owl-ontologies.com/Movie.owl#");


        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
//            OWLOntology ont = man.loadOntologyFromOntologyDocument(ontologyURI);
//            SimpleRenderer simpleRenderer = new SimpleRenderer();
//            simpleRenderer.setShortFormProvider(pm);
        DLSyntaxObjectRenderer dlRen = new DLSyntaxObjectRenderer();

        ToStringRenderer.getInstance().setRenderer(dlRen);

        System.out.println("Loaded ontology");

//            OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
//            reasoner.prepareReasoner();
//
//            Set<OWLAxiom> entailments = new HashSet<OWLAxiom>();
//
//
//            InferredAxiomGenerator<OWLSubClassOfAxiom> iag = new InferredSubClassAxiomGenerator();
//            Set<OWLSubClassOfAxiom> scas = iag.createAxioms(man, reasoner);
//            for (OWLSubClassOfAxiom sca : scas) {
//                if (!ont.containsAxiom(sca)) {
//                    entailments.add(sca);
//                }
//            }
//            InferredAxiomGenerator<OWLClassAssertionAxiom> iag2 = new InferredClassAssertionAxiomGenerator();
//            for (OWLClassAssertionAxiom ax : iag2.createAxioms(man, reasoner)) {
//                if (!ont.containsAxiom(ax)) {
//                    entailments.add(ax);
//                }
//            }
//            for (OWLClass cls : reasoner.getUnsatisfiableClasses()) {
//                entailments.add(man.getOWLDataFactory().getOWLSubClassOfAxiom(cls, man.getOWLDataFactory().getOWLNothing()));
//            }
//
//            entailments.add(man.getOWLDataFactory().getOWLSubClassOfAxiom("owl:Thing", "PublishedWork", new DefaultNamespaceManager(ont.getURI() + "#")));
//
//            ExplanationGeneratorFactory<OWLAxiom> explanationGeneratorFactory = ExplanationManager.createExplanationGeneratorFactory(reasonerFactory);
//            ExplanationGenerator<OWLAxiom> expGen = explanationGeneratorFactory.createExplanationGenerator(ont.getLogicalAxioms());
//
//
//            int count = 0;
//            for (OWLAxiom ent : entailments) {
//                count++;
//                int expCount = 0;
//                for (Explanation<OWLAxiom> exp : expGen.getExplanations(ent)) {
//
//
//                    expCount++;
//                    System.out.println("Entailment " + count + " of " + entailments.size() + "  Explanation " + expCount);
//                    try {
//
//                        System.out.println("***********************************************************************");
//                        System.out.println("Basic explanation: ");
//
////                        double complexity = complexityCalculator.computeComplexity(exp.getEntailment(), exp.getAxioms());
////                        System.out.println("ESTIMATED COMPLEXITY: " + complexity);
//                        System.out.println(exp);
////                        System.out.println("");
////                        System.out.println("");
//
////                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
////                    System.out.println("Got explanation: " + exp);
////                        LemmatisedJustificationGenerator<OWLAxiom> lemmatisedJustificationGenerator = new LemmatisedJustificationGenerator(new SatisfiabilityEntailmentCheckerFactory(reasonerFactory), reasonerFactory, man.getOWLDataFactory());
//                        long t0 = System.currentTimeMillis();
////                        Explanation<OWLAxiom> lemExp = lemmatisedJustificationGenerator.getLemmatisedExplanation(exp);
//                        long t1 = System.currentTimeMillis();
////                        System.out.println("................................................................................");
//                        System.out.println("................................................................................");
//                        System.out.println("Got lemmatised explanation in " + (t1 - t0));
////                        DefaultComplexityCalculator complexityCalculator = new DefaultComplexityCalculator(reasonerFactory);
////                        double complexity = complexityCalculator.computeComplexity(lemExp.getEntailment(), lemExp.getAxioms());
//
//
////                        System.out.println("ESTIMATED COMPLEXITY: " + complexity);
//
////                        System.out.println(lemExp);
//                        System.out.println("................................................................................");
////                        System.out.println("................................................................................");
//                        System.out.println("");
//                        System.out.println("");
//
////                        System.out.println("***********************************************************************");
//                    }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}

//
//ExplanationGenerator<OWLAxiom> gen2 = explanationGeneratorFactory2.createExplanationGenerator(lemmas, new ExplanationProgressMonitor<OWLAxiom>() {
//
//            boolean cancelled = false;
//
//            public void foundExplanation(ExplanationGenerator<OWLAxiom> explanationGenerator, Explanation<OWLAxiom> eExplanation, Set<Explanation<OWLAxiom>> allFoundExplanations) {
//                System.out.println("FOUND LEMMATISATION: ");
//                System.out.println(eExplanation);
//                if (!eExplanation.getAxioms().equals(Collections.singleton(eExplanation.getEntailment()))) {
//                    if (!eExplanation.equals(explanation)) {
//                        double comp = complexityCalculator.computeComplexity((OWLAxiom) eExplanation.getEntailment(), eExplanation.getAxioms());
//                        System.out.println("^^^^^ COMPLEXITY = " + comp + " ^^^^^");
//                        int limit = LemmatisationPreferences.getInstance().getJustificationSearchLimit();
//                        if (allFoundExplanations.size() >= limit && !foundExplanations.isEmpty()) {
//                            cancelled = true; // FOR OPTIMISATION
//                        }
//                        // Add now and filter later
////                        foundExplanations.add(eExplanation);
////                        if (comp < lastFoundComplexity) {
////                            lastFoundComplexity = comp;
////                        }
//                    }
//                }
//            }
//
//            public boolean isCancelled() {
//                return cancelled;
//            }
//        });