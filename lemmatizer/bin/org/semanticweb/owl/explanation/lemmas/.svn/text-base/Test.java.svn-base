package org.semanticweb.owl.explanation.lemmas;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owl.explanation.api.*;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;

import java.util.Set;
/*
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
 * Author: Matthew Horridge<br> The University Of Manchester<br> Information Management Group<br> Date:
 * 05-Nov-2008<br><br>
 */
public class Test {

    public static void main(String[] args) {
        try {
            OWLOntologyManager man = OWLManager.createOWLOntologyManager();
//            OWLOntology ont = man.loadOntologyFromOntologyDocument(URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.owl-ontologies.com/Movie.owl&version=0&format=RDF/XML"));
//            OWLOntology ont = man.loadOntologyFromOntologyDocument(URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl&version=0&format=RDF/XML"));
//            OWLOntology ont = man.loadOntologyFromOntologyDocument(URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=http://www.semanticweb.org/ontolgies/chemical&version=0&format=RDF/XML"));
//            OWLOntology ont = man.loadOntologyFromOntologyDocument(URI.create("http://owl.cs.manchester.ac.uk/repository/download?ontology=file:/Users/seanb/Desktop/Cercedilla2005/hands-on/people.owl&version=0&format=RDF/XML"));
            OWLOntology ont = man.loadOntologyFromOntologyDocument(IRI.create("file:/tmp/testontology.owl"));
            OWLDataFactory df = man.getOWLDataFactory();
//            OWLClass person = df.getOWLClass(URI.create("http://www.owl-ontologies.com/Movie.owl#Person"));
//            OWLClass movie = df.getOWLClass(URI.create("http://www.owl-ontologies.com/Movie.owl#Movie"));
//            OWLClass employee = man.getOWLDataFactory().getOWLClass(URI.create("http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#Employee"));
//            OWLAxiom ent = df.getOWLSubClassOfAxiom(person, movie);
//            PelletReasonerFactory reasonerFactory = new PelletReasonerFactory();
//            ExplanationGeneratorFactory<OWLAxiom> gen = ExplanationManager.createExplanationGeneratorFactory(reasonerFactory);
//            ExplanationGenerator<OWLAxiom> ax = gen.createExplanationGenerator(ont.getLogicalAxioms());
//
//            OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
//            reasoner.prepareReasoner();



            InferredAxiomGenerator<OWLSubClassOfAxiom> scg = new InferredSubClassAxiomGenerator();
//            for (OWLSubClassOfAxiom sca : scg.createAxioms(man, reasoner)) {


//                if (ont.containsAxiom(sca) || sca.getSuperClass().isOWLNothing()) {
//                    continue;
//                }

//            OWLAxiom sca = df.getOWLClassAssertionAxiom(df.getOWLNamedIndividual(URI.create("http://owl.man.ac.uk/2005/07/sssw/people#Daily_Mirror")),
//                    df.getOWLClass(URI.create("http://owl.man.ac.uk/2005/07/sssw/people#tabloid")));


            OWLAxiom sca = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://www.semanticweb.org/ontologies/Ontology1236249842828.owl#B")),
                    df.getOWLClass(IRI.create("http://www.semanticweb.org/ontologies/Ontology1236249842828.owl#A")));
                System.out.println("--------------------------------------------------------------------------");
                System.out.println(sca);
                System.out.println("--------------------------------------------------------------------------");
//                Set<Explanation<OWLAxiom>> expls = ax.getExplanations(sca, Integer.MAX_VALUE);

//                for (Explanation<OWLAxiom> exp : expls) {
//
//
//                    LemmatisedExplanationTreeGenerator<OWLAxiom> tree = new LemmatisedExplanationTreeGenerator<OWLAxiom>(df, reasonerFactory, gen, exp);
//                    Tree<OWLAxiom> t = tree.getTree();
//                    t.writeTreeML(new PrintWriter(System.out));
//                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//                    break;
//                }
//
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();

//        catch (OWLOntologyChangeException e) {
//            e.printStackTrace();
//        }
//            }
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        } catch (ExplanationException e) {
            e.printStackTrace();
        }


    }


    private static <E> void dumpCandidateLemmas(LemmaGenerator<E> lemmaGenerator, Set<Lemma> runningLemmas) {
        Set<Lemma> lemmas = lemmaGenerator.getCandidateLemmas();
        for (Lemma lemma : lemmas) {
            System.out.println(lemma);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
        runningLemmas.addAll(lemmas);
    }
}
