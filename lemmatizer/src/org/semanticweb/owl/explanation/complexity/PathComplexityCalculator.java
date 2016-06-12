package org.semanticweb.owl.explanation.complexity;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 27-Jun-2010
 */
public class PathComplexityCalculator implements ComplexityCalculator {

    private static double pathWeight = 10.0;

    private static double pathLengthMax = 20;

    public double computeComplexity(Explanation<OWLAxiom> originalExplanation, OWLAxiom entailment, Set<OWLAxiom> axioms) {
        
        List<List<Edge>> paths = getPaths(axioms);
        // More paths - is bad!
//        System.out.println("THERE ARE " + paths.size() + " PATHS:");
        double sizeScore = paths.size() * pathWeight;
        int maxLength = 0;
        for(List<Edge> path : paths) {
//            System.out.print("\t");
//            dumpPath(path);
//            System.out.println();
            if(path.size() > maxLength) {
                maxLength = path.size();
            }
        }
        double lengthScore = 0.0;
//        if (maxLength > 1) {
            lengthScore = pathLengthMax / maxLength;
//        }
//        System.out.println("\t SCORE: " + (sizeScore + lengthScore) + "\tSIZE SCORE: " + sizeScore + "\tLENGTH SCORE: " + lengthScore);
        return sizeScore + lengthScore;
    }

    public static void setPathWeight(double pathWeight) {
        PathComplexityCalculator.pathWeight = pathWeight;
    }

    public static void setPathLengthMax(double pathLengthMax) {
        PathComplexityCalculator.pathLengthMax = pathLengthMax;
    }

    public static double getPathWeight() {
        return pathWeight;
    }

    public static double getPathLengthMax() {
        return pathLengthMax;
    }

    public static void dumpPath(List<Edge> path) {
        for(Edge edge : path) {
            System.out.print(edge.getLhs());
            System.out.print(" ---> ");
            System.out.print(edge.getRhs());
            System.out.print("  .  ");
        }
        System.out.println();
    }


    public List<List<Edge>> getPaths(Set<OWLAxiom> axioms) {
        PathVisitor pathVisitor = new PathVisitor();
        for(OWLAxiom ax : axioms) {
            ax.accept(pathVisitor);
        }
        Map<OWLObject, Set<Edge>> lhs2EdgeMap = new HashMap<OWLObject, Set<Edge>>();
        Set<OWLObject> rhs = new HashSet<OWLObject>();
        Set<OWLObject> lhs = new HashSet<OWLObject>();
        for(Edge edge : pathVisitor.edges) {
            lhs.add(edge.getLhs());
            rhs.add(edge.getRhs());
            Set<Edge> edgesByLHS = lhs2EdgeMap.get(edge.getLhs());
            if(edgesByLHS == null) {
                edgesByLHS = new HashSet<Edge>();
                lhs2EdgeMap.put(edge.getLhs(), edgesByLHS);
            }
            edgesByLHS.add(edge);
        }
        Set<OWLObject> roots = new HashSet<OWLObject>();
        roots.addAll(lhs);
        roots.removeAll(rhs);
        ArrayList<List<Edge>> paths = new ArrayList<List<Edge>>();
        Set<OWLObject> allVisited = new HashSet<OWLObject>();
        allVisited.addAll(lhs);
        allVisited.addAll(rhs);
        allVisited.removeAll(roots);
        for(OWLObject object : roots) {
            HashSet<OWLObject> visited = new HashSet<OWLObject>();
            toPath(lhs2EdgeMap, object, paths, new ArrayList<Edge>(), visited);
            allVisited.removeAll(visited);
        }
        for(OWLObject object : allVisited) {
            toPath(lhs2EdgeMap, object, paths, new ArrayList<Edge>(), new HashSet<OWLObject>());
        }
        return paths;
    }

    private void toPath(Map<OWLObject, Set<Edge>> lhs2EdgeMap, OWLObject lhs, List<List<Edge>> paths, List<Edge> currentPath, Set<OWLObject> visited) {
        if(visited.contains(lhs)) {
           return;
        }
        visited.add(lhs);
        Set<Edge> edges = lhs2EdgeMap.get(lhs);
        if (edges != null) {
            paths.remove(currentPath);
            for(Edge edge : edges) {
                List<Edge> nextPath = new ArrayList<Edge>(currentPath);
                nextPath.add(edge);
                paths.add(nextPath);
                toPath(lhs2EdgeMap, edge.getRhs(), paths, nextPath, visited);
            }
        }
    }

    private class Edge {
        
        private OWLObject lhs;

        private Object label;
        
        private OWLObject rhs;

        private Edge(OWLObject lhs, Object label, OWLObject rhs) {
            this.lhs = lhs;
            this.label = label;
            this.rhs = rhs;
        }

        public OWLObject getLhs() {
            return lhs;
        }

        public Object getLabel() {
            return label;
        }

        public OWLObject getRhs() {
            return rhs;
        }

        @Override
        public int hashCode() {
            return lhs.hashCode() * 37 + rhs.hashCode() + label.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this) {
                return true;
            }
            if(!(obj instanceof Edge)) {
                return false;
            }
            Edge other = (Edge) obj;
            return other.lhs.equals(lhs) && other.label.equals(this.label) &&  other.rhs.equals(rhs);
        }
    }

    private class PathVisitor implements OWLAxiomVisitor {

        private Set<Edge> edges = new HashSet<Edge>();

        private void addEdge(Edge edge) {
            edges.add(edge);
        }

        private void addEdge(OWLObject lhs, Object label, OWLObject rhs) {
            addEdge(new Edge(lhs, label, rhs));
        }

        public void visit(OWLDeclarationAxiom owlDeclarationAxiom) {
            // None
        }

        public void visit(OWLSubClassOfAxiom ax) {
            // LHS - RHS - Obvious
            OWLClassExpression subClass = ax.getSubClass();
            OWLClassExpression supClass = ax.getSuperClass();
//            if(supClass instanceof OWLObjectSomeValuesFrom) {
//                OWLObjectSomeValuesFrom svf = (OWLObjectSomeValuesFrom) supClass;
//                addEdge(subClass, AxiomType.SUBCLASS_OF + "-SomeValuesFrom", svf.getFiller());
//                addEdge(subClass, AxiomType.SUBCLASS_OF + "-SomeValuesFromProp", svf.getProperty());
//            }
//            else {
                addEdge(subClass, AxiomType.SUBCLASS_OF, supClass);
//            }

        }

        public void visit(OWLNegativeObjectPropertyAssertionAxiom ax) {
            addEdge(ax.getSubject(), ax.getProperty(), ax.getObject());
        }

        public void visit(OWLAsymmetricObjectPropertyAxiom ax) {

        }

        public void visit(OWLReflexiveObjectPropertyAxiom ax) {
        }

        public void visit(OWLDisjointClassesAxiom ax) {
            for(OWLSubClassOfAxiom sca : ax.asOWLSubClassOfAxioms()) {
                sca.accept(this);
            }
        }

        public void visit(OWLDataPropertyDomainAxiom ax) {
            ax.asOWLSubClassOfAxiom().accept(this);
        }

        public void visit(OWLObjectPropertyDomainAxiom ax) {
            ax.asOWLSubClassOfAxiom().accept(this);
        }

        public void visit(OWLEquivalentObjectPropertiesAxiom ax) {
            for(OWLAxiom spa : ax.asSubObjectPropertyOfAxioms()) {
                spa.accept(this);
            }
        }

        public void visit(OWLNegativeDataPropertyAssertionAxiom ax) {
            addEdge(ax.getSubject(), ax.getProperty(), ax.getObject());
        }

        public void visit(OWLDifferentIndividualsAxiom ax) {

        }

        public void visit(OWLDisjointDataPropertiesAxiom ax) {
        }

        public void visit(OWLDisjointObjectPropertiesAxiom ax) {
        }

        public void visit(OWLObjectPropertyRangeAxiom ax) {
            ax.asOWLSubClassOfAxiom().accept(this);
        }

        public void visit(OWLObjectPropertyAssertionAxiom ax) {
            addEdge(ax.getSubject(), ax.getProperty(),  ax.getObject());
        }

        public void visit(OWLFunctionalObjectPropertyAxiom ax) {
            ax.asOWLSubClassOfAxiom().accept(this);
        }

        public void visit(OWLSubObjectPropertyOfAxiom ax) {
            addEdge(ax.getSubProperty(), AxiomType.SUB_OBJECT_PROPERTY, ax.getSuperProperty());
        }

        public void visit(OWLDisjointUnionAxiom ax) {

        }

        public void visit(OWLSymmetricObjectPropertyAxiom ax) {
            for(OWLAxiom spa : ax.asSubPropertyAxioms()) {
                spa.accept(this);
            }
        }

        public void visit(OWLDataPropertyRangeAxiom ax) {
            ax.asOWLSubClassOfAxiom().accept(this);
        }

        public void visit(OWLFunctionalDataPropertyAxiom ax) {
            ax.asOWLSubClassOfAxiom().accept(this);
        }

        public void visit(OWLEquivalentDataPropertiesAxiom ax) {
        }

        public void visit(OWLClassAssertionAxiom ax) {
            ax.asOWLSubClassOfAxiom().accept(this);
        }

        public void visit(OWLEquivalentClassesAxiom ax) {
            for(OWLAxiom sca : ax.asOWLSubClassOfAxioms()) {
                sca.accept(this);
            }
        }

        public void visit(OWLDataPropertyAssertionAxiom ax) {
            addEdge(ax.getSubject(), ax.getProperty(), ax.getObject());
        }

        public void visit(OWLTransitiveObjectPropertyAxiom ax) {

        }

        public void visit(OWLIrreflexiveObjectPropertyAxiom ax) {
            ax.asOWLSubClassOfAxiom().accept(this);
        }

        public void visit(OWLSubDataPropertyOfAxiom ax) {
            addEdge(ax.getSubProperty(), AxiomType.SUB_DATA_PROPERTY, ax.getSuperProperty());
        }

        public void visit(OWLInverseFunctionalObjectPropertyAxiom ax) {
            ax.asOWLSubClassOfAxiom().accept(this);
        }

        public void visit(OWLSameIndividualAxiom ax) {

        }

        public void visit(OWLSubPropertyChainOfAxiom ax) {

        }

        public void visit(OWLInverseObjectPropertiesAxiom ax) {
            for(OWLAxiom spa : ax.asSubObjectPropertyOfAxioms()) {
                spa.accept(this);
            }
        }

        public void visit(OWLHasKeyAxiom ax) {
        }

        public void visit(OWLDatatypeDefinitionAxiom ax) {
        }

        public void visit(SWRLRule swrlRule) {
        }

        public void visit(OWLAnnotationAssertionAxiom ax) {
        }

        public void visit(OWLSubAnnotationPropertyOfAxiom ax) {
        }

        public void visit(OWLAnnotationPropertyDomainAxiom ax) {
        }

        public void visit(OWLAnnotationPropertyRangeAxiom ax) {
        }
    }

}
