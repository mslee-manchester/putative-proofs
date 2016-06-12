package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

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
 * Date: 30-Nov-2008
 */
public class ModelBasedComplexityCalculator implements ComplexityCalculator {


    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        // Things that increase the complexity of the model

        // We try to estimate the complexity of the model based on the kinds of axioms that
        // are in a justification

        // 1)  Expansion - min cardinality, existential
        // 2)  Things that add labels when the modal depth is greater than 0
        // 3)  Things that add roles to edge labels
        // 4)  Things that add inverse roles to edge labels
        // 5)  Things that cause nodes to be merged
        //      a) Functional properties
        //      b) Max cardinality restrictions
        //      c) Nominals
        // 6)  Non-explicit negation
        // 7)  Choices - non-determinism (disjunction, max cardinality, functionality



        //  Sub-sumptions between class names score lowest

        //   C -> D, D -> E, E -> F isn't so bad  (C -> F is better)


        //  Equivalent classes axioms are bad



        return 0;
    }
}
