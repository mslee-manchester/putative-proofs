package org.semanticweb.owl.explanation.proofs;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.lemmas.Proof;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 19-Jun-2010
 */
public class JustificationOrientedProofGenerator {

    private Explanation<OWLAxiom> originalExplanation;

    private Map<OWLAxiom, Explanation<OWLAxiom>> edgeMap = new HashMap<OWLAxiom, Explanation<OWLAxiom>>();

    public Proof getProof(Explanation<OWLAxiom> explanation) {
        this.originalExplanation = explanation;
        return null;
    }


    private OWLAxiom chooseLemma() {
        for(Explanation<OWLAxiom> explanation : edgeMap.values()) {
            for(OWLAxiom axiom : explanation.getAxioms()) {
                if(!edgeMap.containsKey(axiom)) {
                    // Lemma
                }
            }
        }
        return null;
    }


    private Explanation<OWLAxiom> getExplanationForLemma(OWLAxiom lemma) {
        Explanation<OWLAxiom> result = null;

        return null;
    }


}
