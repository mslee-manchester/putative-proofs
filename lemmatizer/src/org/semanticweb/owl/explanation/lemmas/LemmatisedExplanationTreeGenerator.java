package org.semanticweb.owl.explanation.lemmas;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorFactory;
import org.semanticweb.owl.explanation.api.ExplanationManager;
import org.semanticweb.owl.explanation.api.ExplanationException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import uk.ac.manchester.cs.bhig.util.Tree;
import uk.ac.manchester.cs.bhig.util.MutableTree;

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
 * Date: 20-Nov-2008
 */
public class LemmatisedExplanationTreeGenerator<E> {

    private ExplanationGeneratorFactory<OWLAxiom> genFac;

    private Explanation<OWLAxiom> source;

    private OWLReasonerFactory reasonerFactory;

    private OWLDataFactory dataFactory;

    public LemmatisedExplanationTreeGenerator(OWLDataFactory dataFactory, OWLReasonerFactory reasonerFactory, ExplanationGeneratorFactory<OWLAxiom> genFac, Explanation<OWLAxiom> source) {
        this.genFac = genFac;
        this.source = source;
        this.reasonerFactory = reasonerFactory;
        this.dataFactory = dataFactory;
    }

    public Tree<OWLAxiom> getTree() throws ExplanationException {
        // Root node is the entailment itself
        MutableTree<OWLAxiom> root = new MutableTree<OWLAxiom>(null);
        // Get initial lemmatised explanation
//        LemmatisedJustificationGenerator<OWLAxiom> lemGen = new LemmatisedJustificationGenerator<OWLAxiom>(new SatisfiabilityEntailmentCheckerFactory(reasonerFactory), reasonerFactory, dataFactory);
//        Explanation<OWLAxiom> expl = lemGen.getLemmatisedExplanation(source);

//        LemmaGeneratorManager<E> lemmaGeneratorManager = new LemmaGeneratorManager<E>(reasonerFactory, dataFactory);
//        LemmatisedExplanation<E> lemmatisedExplanation = new LemmatisedExplanation<E>(lemmaGeneratorManager, genFac);
//        Explanation<E> expl = lemmatisedExplanation.getLemmatisedExplanation(source);
//        for (OWLAxiom ax : expl.getAxioms()) {
//            MutableTree<OWLAxiom> node = new MutableTree<OWLAxiom>(ax);
//            root.addChild(node);
//            if (!source.getAxioms().contains(ax)) {
//                fillTree(node, 1);
//            }
//        }
        return root;
    }

    private void fillTree(MutableTree<OWLAxiom> parent, int depth) throws ExplanationException {
        System.out.println("Filling depth: " + depth);
        if(depth > 10) {
            return;
        }
        OWLAxiom ax = parent.getUserObject();

//        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        ExplanationGeneratorFactory<OWLAxiom> genFac = ExplanationManager.createExplanationGeneratorFactory(reasonerFactory);
        Set<Explanation<OWLAxiom>> expls = genFac.createExplanationGenerator(source.getAxioms()).getExplanations(ax, 1);

        // Just take the first one?
        Explanation<OWLAxiom> selectedExplanation = expls.iterator().next();
//        for(Explanation<OWLAxiom> expl : expls) {
//            if(!expl.getAxioms().equals(Collections.singleton(ax))) {
//                selectedExplanation = expl;
//            }
//        }


//        LemmatisedJustificationGenerator<OWLAxiom> lemGen = new LemmatisedJustificationGenerator<OWLAxiom>(new SatisfiabilityEntailmentCheckerFactory(reasonerFactory), reasonerFactory, dataFactory);
//        Explanation<OWLAxiom> lemExpl = lemGen.getLemmatisedExplanation(selectedExplanation);
//
//         Blocking condition
//        if(lemExpl.getAxioms().equals(Collections.singleton(ax))) {
//            return;
//        }
//
//        for (OWLAxiom lemAx : lemExpl.getAxioms()) {
//            MutableTree<OWLAxiom> node = new MutableTree<OWLAxiom>(lemAx);
//            parent.addChild(node);
//            if (!source.getAxioms().contains(lemAx)) {
//                fillTree(node, depth + 1);
//            }
//        }
    }


}
