package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectWalker;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;/*
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
 * Date: 24-Nov-2008
 */
public class ClassExpressionTypeComplexityCalculator implements ComplexityCalculator {

    private static final double DEFAULT_WEIGHTING = 100.0;


    private static Map<ClassExpressionType, Double> weightingMap = new HashMap<ClassExpressionType, Double>();

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {

        Set<ClassExpressionType> types = new HashSet<ClassExpressionType>();
        for (OWLClassExpression classExpression : entailment.getNestedClassExpressions()) {
            types.add(classExpression.getClassExpressionType());
        }

        for (OWLAxiom ax : axioms) {
            for (OWLClassExpression classExpression : ax.getNestedClassExpressions()) {
                if (classExpression.isOWLThing() || classExpression.isOWLNothing()) {

                }
                else
                    types.add(classExpression.getClassExpressionType());
            }
        }

        double score = 0.0;
        for (ClassExpressionType type : types) {
            score = score + getWeighting(type);
        }
        return score;
    }

    public static void setWeighting(ClassExpressionType type, double weighting) {
        weightingMap.put(type, weighting);
    }

    public static double getWeighting(ClassExpressionType type) {
        Double weighting = weightingMap.get(type);
        if (weighting == null) {
            weighting = DEFAULT_WEIGHTING;
        }
        return weighting;
    }


}
