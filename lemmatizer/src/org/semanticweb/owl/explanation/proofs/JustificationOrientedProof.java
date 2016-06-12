package org.semanticweb.owl.explanation.proofs;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 19-Jun-2010
 */
public class JustificationOrientedProof {

    private Explanation<OWLAxiom> originalJustification;

    private Map<OWLAxiom, Explanation<OWLAxiom>> edgeMap;

    public JustificationOrientedProof(Explanation<OWLAxiom> originalJustification, Map<OWLAxiom, Explanation<OWLAxiom>> edgeMap) {
        this.originalJustification = originalJustification;
        this.edgeMap = edgeMap;
    }

    public OWLAxiom getRoot() {
        return originalJustification.getEntailment();
    }

    public Explanation<OWLAxiom> getRootJustification() {
        return edgeMap.get(originalJustification.getEntailment());
    }

    public Explanation<OWLAxiom> getJustification(OWLAxiom axiom) {
        return edgeMap.get(axiom);
    }

    public boolean wouldIntroduceCycle(Explanation<OWLAxiom> justification) {
        for(OWLAxiom ax : justification.getAxioms()) {
            if(edgeMap.containsKey(ax)) {
                // We might introduce a cycle
                
            }
        }
        return false;
    }

}
