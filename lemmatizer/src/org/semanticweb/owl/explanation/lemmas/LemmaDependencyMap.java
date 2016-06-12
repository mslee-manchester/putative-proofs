package org.semanticweb.owl.explanation.lemmas;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owl.explanation.api.Explanation;

import java.util.*;
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
 * 06-Nov-2008<br><br>
 */
public class LemmaDependencyMap<E> {

    private Set<Lemma> allLemmas = new HashSet<Lemma>();

    private Map<Lemma, Set<Lemma>> parent2ChildMap = new HashMap<Lemma, Set<Lemma>>();

    private Set<Lemma> roots = new HashSet<Lemma>();

    private Explanation<E> explanation;

    public LemmaDependencyMap(Explanation<E> explanation, Set<Lemma> lemmas) {
        allLemmas.addAll(lemmas);
        this.explanation = explanation;
        rebuild();
    }

    private void rebuild() {
        for(Lemma lemma : allLemmas) {
            if(lemma.getLemma().equals(explanation.getEntailment())) {
                continue;
            }
            parent2ChildMap.put(lemma, new HashSet<Lemma>());
            for(Lemma potChild : allLemmas) {
                if(!lemma.equals(potChild)) {
                    if(lemma.getAxioms().containsAll(potChild.getAxioms()) && !potChild.getAxioms().containsAll(lemma.getAxioms())) {
                        parent2ChildMap.get(lemma).add(potChild);
                    }
                }
            }
        }
        roots.addAll(allLemmas);
        for(Set<Lemma> children : parent2ChildMap.values()) {
            roots.removeAll(children);
        }
    }


    public Set<Lemma> getRoots() {
        return Collections.unmodifiableSet(roots);
    }

    public Set<Lemma> getChildren(Lemma lemma) {
        Set<Lemma> lemmaSet = parent2ChildMap.get(lemma);
        if(lemmaSet != null) {
            return Collections.unmodifiableSet(lemmaSet);
        }
        else {
            return Collections.emptySet();
        }
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Lemma root : roots) {
            render(root, 0, sb);    
        }
        return sb.toString();
    }

    private void render(Lemma lemma, int indent, StringBuilder sb) {
        insertIndent(indent, sb);
        sb.append("+<");
        sb.append(lemma.getLemma());
        sb.append(">\n");
        for(OWLAxiom ax : lemma.getAxioms()) {
//            insertIndent(indent, sb);
//            sb.append("  o ");
//            sb.append(ax);
//            sb.append("\n");
        }
        for(Lemma child : getChildren(lemma)) {
            render(child, indent+1, sb);
        }
    }

    private static void insertIndent(int indent, StringBuilder sb) {
        for(int i = 0; i < indent; i++) {
            sb.append("        ");
        }
    }
}
