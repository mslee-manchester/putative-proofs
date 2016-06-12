package org.semanticweb.owl.explanation.lemmas;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.complexity.ComplexityCalculator;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeMap;
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
public class Proof {

    private Map<OWLAxiom, Explanation<OWLAxiom>> map;

    private Explanation<OWLAxiom> root;

    private Explanation<OWLAxiom> regularJustification;

    private Map<OWLAxiom, Explanation<OWLAxiom>> regularJustifications;

    public Proof(Explanation<OWLAxiom> regularJustification, Map<OWLAxiom, Explanation<OWLAxiom>> map, Map<OWLAxiom, Explanation<OWLAxiom>> regularJustifications) {
        this.map = map;
        this.root = map.get(regularJustification.getEntailment());
        this.regularJustification = regularJustification;
        this.regularJustifications = regularJustifications;
    }

    public Map<OWLAxiom, Explanation<OWLAxiom>> getMap() {
        return map;
    }
    
    public Map<OWLAxiom, Explanation<OWLAxiom>> getRegularJustifications(){
    	return regularJustifications;
    }

    protected void replace(Explanation<OWLAxiom> old, Explanation<OWLAxiom> with) {
        map.remove(old.getEntailment());
        map.put(with.getEntailment(), with);
    }

    public double getMaxComplexity(ComplexityCalculator complexityCalculator) {
        double maxComplexity = 0.0;
        for(OWLAxiom ax : map.keySet()) {
            Explanation<OWLAxiom> expl = map.get(ax);
            double complexity = complexityCalculator.computeComplexity(regularJustification, expl.getEntailment(), expl.getAxioms());
            if(complexity > maxComplexity) {
                maxComplexity = complexity;
            }
        }
        return maxComplexity;
    }

    public Explanation<OWLAxiom> getRoot() {
        return root;
    }

    public Explanation<OWLAxiom> getOriginalJustification() {
        return regularJustification;
    }

    public Explanation<OWLAxiom> getRegularJustification(OWLAxiom axiom) {
        if(regularJustification.contains(axiom)) {
            return regularJustification;
        }
        else {
            return regularJustifications.get(axiom);
        }
    }

    public Explanation<OWLAxiom> getExplanation(OWLAxiom axiom) {
        return map.get(axiom);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Proof: ");
        toString(sb, getRoot().getEntailment(), 0, new HashSet<OWLAxiom>());

        return sb.toString();
    }

    public void dump() {
        System.out.println("Proof map:");
        TreeMap<OWLAxiom, Explanation<OWLAxiom>> tm = new TreeMap<OWLAxiom, Explanation<OWLAxiom>>(map);
        for(OWLAxiom ax : tm.keySet()) {
            System.out.println(ax + " -> ");
            for(OWLAxiom expAx : tm.get(ax).getAxioms()) {
                System.out.println("    " + expAx);
            }
        }
    }

    private void toString(StringBuilder sb, OWLAxiom currentEntailment, int indent, Set<OWLAxiom> processed) {
        insertIndent(indent, sb);
        if(root.getAxioms().contains(currentEntailment)) {
            sb.append(" ");
        }
        else {
            sb.append("*");
        }
        sb.append(currentEntailment);
        sb.append("\n");
        Explanation<OWLAxiom> expl = getExplanation(currentEntailment);
        if(expl == null) {
            return;
        }
        if(processed.contains(currentEntailment)) {
            return;
        }
        processed.add(currentEntailment);
        for(OWLAxiom ax : expl.getAxioms()) {
            toString(sb, ax, indent + 1, processed);
        }

    }

    private void insertIndent(int indent, StringBuilder sb) {
        for(int i = 0; i < indent; i++) {
            sb.append("    ");
        }
    }
}
