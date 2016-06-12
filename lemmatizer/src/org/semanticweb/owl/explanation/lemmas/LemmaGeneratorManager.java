package org.semanticweb.owl.explanation.lemmas;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorFactory;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

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
 * Author: Matthew Horridge<br> The University of Manchester<br> Information Management Group<br>
 * Date: 19-Nov-2008
 */
public class LemmaGeneratorManager<E> {

    private OWLReasonerFactory fac;

    private OWLDataFactory dataFac;

    public LemmaGeneratorManager(OWLReasonerFactory reasonerFactory, OWLDataFactory dataFactory) {
        fac = reasonerFactory;
        dataFac = dataFactory;
    }

    public Set<Lemma> getLemmas(Explanation<E> explanation, ExplanationGeneratorFactory<E> genFac) {
        Set<Lemma> result = new HashSet<Lemma>();
        List<LemmaGenerator<E>> generators = getGenerators(explanation, genFac);
        for(LemmaGenerator<E> gen : generators) {
            Set<Lemma> lemmaSet = gen.getCandidateLemmas();
            result.addAll(lemmaSet);
        }
        return result;
    }

    public List<LemmaGenerator<E>> getGenerators(Explanation<E> exp, ExplanationGeneratorFactory<E> gen) {
        List<LemmaGenerator<E>> generators = new ArrayList<LemmaGenerator<E>>();
        generators.add(new NamedSubSumptionLemmaGenerator<E>(exp, gen, fac, dataFac));
        generators.add(new FunctionalObjectPropertyLemmaGenerator<E>(exp, gen, fac, dataFac));
        generators.add(new ExistentialRestrictionLemmaGenerator<E>(exp, gen, fac, dataFac));
        generators.add(new UniversalRestrictionLemmaGenerator<E>(exp, gen, fac, dataFac));
        generators.add(new DisjointClassesLemmaGenerator<E>(exp, gen, fac, dataFac));
        generators.add(new DomainLemmaGenerator<E>(exp, gen, fac, dataFac));
//        generators.add(new RangeLemmaGenerator<E>(exp, gen, fac, dataFac));
        generators.add(new NamedClassAssertionLemmaGenerator<E>(exp, gen, fac, dataFac));
        generators.add(new ObjectSubPropertyLemmaGenerator<E>(exp, gen, fac, dataFac));
        generators.add(new AnonymousClassAssertionLemmaGenerator<E>(exp, gen, fac, dataFac));
        generators.add(new NNFLemmaGenerator<E>(exp, gen, fac, dataFac));
        return generators;
    }
}
