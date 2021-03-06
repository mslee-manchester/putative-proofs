package org.semanticweb.owl.explanation.lemmas;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationException;
import org.semanticweb.owl.explanation.impl.laconic.OPlusGenerator;
import org.semanticweb.owl.explanation.impl.laconic.TauGenerator;
import org.semanticweb.owl.explanation.proofs.SubConceptGenerator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.model.*;

import java.util.*;

import com.clarkparsia.owlapi.explanation.SatisfiabilityConverter;/*
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
public abstract class CandidateLemmaGenerator<E> {

    private OWLDataFactory dataFactory;

    private Explanation<E> lastExplanation;

    private OWLReasonerFactory reasonerFactory;

    private OWLOntologyManager man;

    private List<OWLReasoner> reasoners;

    private OWLOntology ont;
    private OWLReasoner mainReasoner;

    protected CandidateLemmaGenerator(OWLReasonerFactory reasonerFactory, OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
        this.reasonerFactory = reasonerFactory;
        this.reasoners = new ArrayList<OWLReasoner>();
        man = OWLManager.createOWLOntologyManager();

    }

    public OWLReasonerFactory getReasonerFactory() {
        return reasonerFactory;
    }

    public abstract Set<OWLAxiom> getCandidateLemmas(Explanation<E> explanation);


    protected OWLDataFactory getDataFactory() {
        return dataFactory;
    }

    public Set<OWLObjectProperty> getObjectPropertiesInSignature(Explanation<E> explanation) {
        Set<OWLObjectProperty> props = new HashSet<OWLObjectProperty>();
        for (OWLAxiom ax : explanation.getAxioms()) {
            props.addAll(ax.getObjectPropertiesInSignature());
        }
        return props;
    }


    public Set<OWLClassExpression> getClassesInSignature(Explanation<E> explanation) {
//        SubConceptGenerator subConceptGenerator = new SubConceptGenerator(getDataFactory());
//        return  subConceptGenerator.getExpandedSubConcepts((Explanation<OWLAxiom>) explanation);

        Set<OWLClassExpression> clses = new HashSet<OWLClassExpression>();
        clses.add(getDataFactory().getOWLThing());
        for (OWLAxiom ax : explanation.getAxioms()) {
            clses.addAll(ax.getClassesInSignature());
            if (LemmatisationPreferences.getInstance().isUseSubConcepts()) {
                Set<OWLClassExpression> nestedClassExpressions = ax.getNestedClassExpressions();
                clses.addAll(nestedClassExpressions);
//                for(OWLClassExpression ce : new ArrayList<OWLClassExpression>(nestedClassExpressions)) {
//                    TauGenerator tauGenerator = new TauGenerator(dataFactory);
//                    Set<OWLClassExpression> weakenedClassExpressions = ce.accept(tauGenerator);
//                    clses.addAll(weakenedClassExpressions);
//                }
            }
        }
        return clses;
    }

    public Set<OWLIndividual> getIndividualsInSignature(Explanation<E> explanation) {
        Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();
        for (OWLAxiom ax : explanation.getAxioms()) {
            individuals.addAll(ax.getIndividualsInSignature());
        }
        return individuals;
    }

    protected boolean isSatisfiable(OWLClassExpression desc, Explanation<E> explanation) {
        return !isEntailedBy(getDataFactory().getOWLSubClassOfAxiom(desc, getDataFactory().getOWLNothing()), explanation);
    }

    private static boolean isDirectTopSyn(OWLAxiom ax) {
        if (ax instanceof OWLSubClassOfAxiom) {
            return ((OWLSubClassOfAxiom) ax).getSubClass().isOWLThing();
        }
        else if (ax instanceof OWLEquivalentClassesAxiom) {
            return ((OWLEquivalentClassesAxiom) ax).containsOWLThing();
        }
        else {
            return false;
        }
    }

    /**
     * Checks to see if an checker is entailed by at least one TIDY subset of the set of axioms
     * @param ax          The checker to check
     * @param explanation The explanation which determines the set of axioms from which tidy subsets will be drawn
     * @return <code>true</code> if the checker is entailed by at least one TIDY subsets of the set of axioms specified
     *         by the specified explanation
     */
    protected boolean isEntailedBy(OWLAxiom ax, Explanation<E> explanation) {
        try {
//            System.out.println("Checking entailment...");
            if (isReasonerRebuildRequired(explanation)) {
                rebuildReasoner(explanation);
            }
            OWLClassExpression desc = null;
            if (!(ax instanceof OWLSubObjectPropertyOfAxiom)) {
                SatisfiabilityConverter satisfiabilityConverter = new SatisfiabilityConverter(dataFactory);
                desc = satisfiabilityConverter.convert(ax);
            }

            if (isDirectTopSyn(ax) && desc != null) {
                return !mainReasoner.isSatisfiable(desc);
            }

            // All of the reasoners contain tidy subsets
            int i = 1;
                        
            for (OWLReasoner r : reasoners) {
            	OWLClass thing = man.getOWLDataFactory().getOWLThing();
                Set<OWLClass> topSyns = r.getEquivalentClasses(thing).getEntities();
                topSyns.remove(thing);
                OWLClass nothing = man.getOWLDataFactory().getOWLNothing();
                Set<OWLClass> botSyns = r.getEquivalentClasses(nothing).getEntities();
                botSyns.remove(nothing);
                if(!topSyns.isEmpty() || !botSyns.isEmpty()) {
                    throw new IllegalStateException();
                }
                i++;
                boolean containsTopOrBotSyns = false;
                Set<OWLClass> sig = ax.getClassesInSignature();
                for (OWLClass cls : sig) {
                    if (!cls.isOWLThing() && !cls.isOWLNothing()) {
                        if (topSyns.contains(cls)) {
                            containsTopOrBotSyns = true;
                            break;
                        }
                        else if (botSyns.contains(cls)) {
                            containsTopOrBotSyns = true;
                            break;
                        }
                    }
                }
                if(ax instanceof OWLSubObjectPropertyOfAxiom) {
                    OWLSubObjectPropertyOfAxiom spa = (OWLSubObjectPropertyOfAxiom) ax;
                    if(!spa.getSubProperty().isAnonymous()) {
                        Set<OWLObjectPropertyExpression> sups = r.getSuperObjectProperties(spa.getSubProperty().asOWLObjectProperty(), false).getFlattened();
                        if(sups.contains(spa.getSuperProperty())) {
                        	//System.out.println("Checking axiom: " + ax + "It is true.");
                            return true;
                        }
                    }
                    else if(!spa.getSuperProperty().isAnonymous()) {
                        Set<OWLObjectPropertyExpression> descs = r.getSubObjectProperties(spa.getSuperProperty().asOWLObjectProperty(), false).getFlattened();
                        if(descs.contains(spa.getSubProperty())) {
                        	//System.out.println("Checking axiom: " + ax + "It is true.");
                            return true;
                        }
                    }

                }
                else {
                    if (!containsTopOrBotSyns && r.isEntailed(ax)) {
                    	//System.out.println("Checking axiom: " + ax + "It is true.");
                    	//System.out.println("For axiom " + ax + " Jfact believes " + r.isEntailed(ax));
                        return true;
                    }
                    if(r.isEntailed(ax)) {              
                    	//System.out.println("Checking axiom: " + ax + "It is true.");
                        return true;
                    }
                }

            }
            //System.out.println("Checking axiom: " + ax + "It is false.");
            return false;
        }
        catch (OWLOntologyCreationException e) {
            throw new OWLRuntimeException(e);
        }
        catch (OWLOntologyChangeException e) {
            throw new OWLRuntimeException(e);
        }
        catch (ExplanationException e) {
            throw new OWLRuntimeException(e);
        }
        finally {
//            System.out.println("    .... done");
        }

    }

    /**
     * Determines if a rebuild of the reasoners is required.
     * @param explanation The explanation to test against
     * @return <code>true</code> if a rebuild is required, otherwise, <code>false</code>
     */
    private boolean isReasonerRebuildRequired(Explanation<E> explanation) {
        return lastExplanation == null || !explanation.equals(lastExplanation);
    }

    private void rebuildReasoner(Explanation<E> explanation) throws OWLOntologyCreationException, OWLOntologyChangeException, ExplanationException {
        lastExplanation = explanation;
        for (OWLOntology ont : man.getOntologies()) {
            man.removeOntology(ont);
        }
        for (OWLReasoner r : reasoners) {
            r.dispose();
        }
        reasoners.clear();
        if (mainReasoner != null) {
            mainReasoner.dispose();
        }
        ont = man.createOntology(explanation.getAxioms());
        Set<OWLDeclarationAxiom> decls = new HashSet<OWLDeclarationAxiom>();
        for (OWLAxiom ax : explanation.getAxioms()) {
            for (OWLEntity ent : ax.getSignature()) {
                decls.add(man.getOWLDataFactory().getOWLDeclarationAxiom(ent));
            }
        }
        man.addAxioms(ont, decls);
        OWLReasonerConfiguration config = new OWLReasonerConfiguration() {
            public ReasonerProgressMonitor getProgressMonitor() {
            	return new ConsoleProgressMonitor();
            }

            public long getTimeOut() {
                return 20000;
            }

            public FreshEntityPolicy getFreshEntityPolicy() {
                return FreshEntityPolicy.ALLOW;
            }

            public IndividualNodeSetPolicy getIndividualNodeSetPolicy() {
                return IndividualNodeSetPolicy.BY_SAME_AS;
            }
        };
        mainReasoner = reasonerFactory.createReasoner(ont, config);
        boolean inconsistentOrCohrentOrTopSyns = false;
        if (!mainReasoner.isConsistent()) {
            inconsistentOrCohrentOrTopSyns = true;
        }
        else {
            for (OWLClass cls : ont.getClassesInSignature()) {
                if (!cls.isOWLNothing() && !mainReasoner.isSatisfiable(cls)) {
                    inconsistentOrCohrentOrTopSyns = true;
                    break;
                }
            }
        }
        OWLClass thing = man.getOWLDataFactory().getOWLThing();
        if (!inconsistentOrCohrentOrTopSyns) {
            Set<OWLClass> topSyns = mainReasoner.getEquivalentClasses(thing).getEntities();
            topSyns.remove(thing);
            inconsistentOrCohrentOrTopSyns = !topSyns.isEmpty();
        }
        Set<OWLEntity> explanationSignature = new HashSet<OWLEntity>();
        for (OWLAxiom expAx : explanation.getAxioms()) {
            explanationSignature.addAll(expAx.getSignature());
        }
        explanationSignature.addAll(((OWLAxiom) explanation.getEntailment()).getSignature());
        System.out.println("Reasoner list before search: " + reasoners);
        if (inconsistentOrCohrentOrTopSyns) {
            Set<OWLAxiom> transformedAxioms = new HashSet<OWLAxiom>(explanation.getAxioms());
            for (OWLAxiom ax : new HashSet<OWLAxiom>(transformedAxioms)) {
                transformedAxioms.remove(ax);
                Set<OWLAxiom> augmentedTransformed = new HashSet<OWLAxiom>(transformedAxioms);
                for (OWLEntity ent : explanationSignature) {
                    augmentedTransformed.add(man.getOWLDataFactory().getOWLDeclarationAxiom(ent));
                }
                OWLOntologyManager reasonerMan = OWLManager.createOWLOntologyManager(man.getOWLDataFactory());
                OWLOntology transformedOnt = reasonerMan.createOntology(augmentedTransformed);
                final OWLReasoner r = reasonerFactory.createReasoner(transformedOnt);
                OWLClass nothing = man.getOWLDataFactory().getOWLNothing();
                Set<OWLClass> synsOfBot = new HashSet<OWLClass>(r.getEquivalentClasses(nothing).getEntities());
                synsOfBot.remove(getDataFactory().getOWLNothing());
                Set<OWLClass> synsOfTop = r.getEquivalentClasses(thing).getEntities();
                synsOfTop.remove(thing);

                boolean tidy = synsOfBot.isEmpty() && synsOfTop.isEmpty() && r.isConsistent();

                if (!tidy) {
                	System.out.println("Not a respective tidy subset: " + augmentedTransformed);
                    r.dispose();
                }
                else {
                	System.out.println("Found a respective tidy subset: " + augmentedTransformed);
                    reasoners.add(r);
                }

                transformedAxioms.add(ax);
            }
        }
        else {
            reasoners.add(mainReasoner);
        }
//        System.out.println("Rebuilt reasoner: " + reasoners.size());
//        for(OWLReasoner r : reasoners) {
//            System.out.println("Reasoner@" + System.identityHashCode(r));
//            System.out.println("\t" + r.getUnsatisfiableClasses());
//            Set<OWLOntology> loadedOnts = r.getLoadedOntologies();
//            System.out.println("\tLoaded: " + loadedOnts);
//            OWLOntology loadedOnt = loadedOnts.iterator().next();
//            System.out.println("    Ont@" + System.identityHashCode(loadedOnt));
//            for(OWLAxiom ax : loadedOnt.getAxioms()) {
//                System.out.println("\t\t" + ax);
//            }
//        }
        System.out.println("Reasoner list after search: " + reasoners);
    }


    protected void addIfEntailed(OWLAxiom ax, Explanation<E> explanation, Set<OWLAxiom> axioms) {
        if (isEntailedBy(ax, explanation)) {
            axioms.add(ax);
        }
    }

}
