package man.ac.uk;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorFactory;
import org.semanticweb.owl.explanation.api.ExplanationManager;
import org.semanticweb.owl.explanation.complexity.DefaultComplexityCalculator;
import org.semanticweb.owl.explanation.complexity.KuduComplexityCalculator;
import org.semanticweb.owl.explanation.complexity.LaconicGCIComplexityCalculator;
import org.semanticweb.owl.explanation.complexity.ModalDepthComplexityCalculator;
import org.semanticweb.owl.explanation.complexity.StructuralComplexityCalculator;
import org.semanticweb.owl.explanation.impl.blackbox.EntailmentCheckerFactory;
import org.semanticweb.owl.explanation.impl.blackbox.checker.SatisfiabilityEntailmentCheckerFactory;
import org.semanticweb.owl.explanation.impl.laconic.LaconicExplanationGenerator;
import org.semanticweb.owl.explanation.impl.laconic.LaconicExplanationGeneratorFactory;
import org.semanticweb.owl.explanation.lemmas.DefaultCandidateLemmaGenerator;
import org.semanticweb.owl.explanation.lemmas.LemmaGeneratorManager;
import org.semanticweb.owl.explanation.lemmas.LemmatisedJustificationGenerator;
import org.semanticweb.owl.explanation.lemmas.Proof;
import org.semanticweb.owl.explanation.lemmas.ProofGenerator;
import org.semanticweb.owl.explanation.lemmas.PutativeProof;
import org.semanticweb.owl.explanation.lemmas.PutativeProofGenerator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.FreshEntityPolicy;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NullReasonerProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;
import uk.ac.manchester.cs.jfact.JFactFactory;


public final class Test {

	/**
	 * @param args
	 * @throws OWLException 
	 */
	
	public static void p(String s) {
		System.out.println(s);
		try {
			System.in.read();
			//System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws OWLException {
		// TODO Auto-generated method stub
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		OWLReasonerFactory pelletfac = new PelletReasonerFactory();
		OWLReasonerFactory factfac = new FaCTPlusPlusReasonerFactory();
		OWLReasonerFactory jfactfac = new JFactFactory();
		
		
		File file = new File("/home/michael/corpus/cao/just_5_hermit_CAO_0000324_CAO_0000323_1429892451144.owl");
		OWLOntology onto = ontoman.loadOntologyFromOntologyDocument(file);
		//Set<OWLAxiom> JUST = onto.getAxioms();
		
		/**OWLSubClassOfAxiom ax = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://www.semanticweb.org/michael/ontologies/2015/9/untitled-ontology-41#A")), df.getOWLObjectSomeValuesFrom(df.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/michael/ontologies/2015/9/untitled-ontology-41#R")), df.getOWLClass(IRI.create("http://www.semanticweb.org/michael/ontologies/2015/9/untitled-ontology-41#A"))));
		OWLTransitiveObjectPropertyAxiom trans = df.getOWLTransitiveObjectPropertyAxiom(df.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/michael/ontologies/2015/9/untitled-ontology-41#R")));
		ontoman.addAxiom(onto, ax);
		Set<OWLAxiom> JUST = onto.getAxioms();
		OWLReasoner hermit = hermitfac.createReasoner(onto);
		System.out.println("Transitivity of R follows? " + hermit.isEntailed(trans));
		Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(trans,JUST);
		**/
		
		
		OWLClass SUB = df.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/CAO_0000324"));
		OWLClass SUP = df.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/CAO_0000323"));
		OWLSubClassOfAxiom ENT = df.getOWLSubClassOfAxiom(SUB,SUP);
		Set<OWLAxiom> JUST = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:onto.getAxioms()){
			if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION))
			{
				JUST.add(ax);
			}
		}
		Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(ENT,JUST);
		//OWLReasoner jfact = jfactfac.createNonBufferingReasoner(onto);
		//System.out.println(jfact.isEntailed(ENT));
		//jfact.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		//System.out.println(jfact.isEntailed(ENT));
		/**
		Set<OWLDeclarationAxiom> decls = new HashSet<OWLDeclarationAxiom>();
        for (OWLAxiom ax : JUST) {
            for (OWLEntity ent : ax.getSignature()) {
                decls.add(ontoman.getOWLDataFactory().getOWLDeclarationAxiom(ent));
            }
        }
        ontoman.addAxioms(onto, decls);
        OWLReasoner jfact1 = jfactfac.createReasoner(onto);
        **/
		/**
        OWLReasoner jfact1 = jfactfac.createReasoner(onto,
        		new OWLReasonerConfiguration() {
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
            }});
        
       OWLReasoner jfact2 = jfactfac.createReasoner(onto);
       OWLReasoner hermit1 = hermitfac.createReasoner(onto,
       		new OWLReasonerConfiguration() {
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
           }});
       OWLReasoner hermit2 = hermitfac.createReasoner(onto);
       **/
       /**OWLReasoner factplus1 = factfac.createReasoner(onto,
       		new OWLReasonerConfiguration() {
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
           }});
       //OWLReasoner factplus2 = factfac.createReasoner(onto);
        * 
        */
        /**
        System.out.println(jfact1.getFreshEntityPolicy());
        System.out.println(jfact1.getIndividualNodeSetPolicy());
        System.out.println(jfact1.getTimeOut());
        System.out.println(jfact2.getTimeOut());
        System.out.println(jfact2.getFreshEntityPolicy());
        System.out.println(jfact2.getIndividualNodeSetPolicy());
        System.out.println("Ontology with Declarations:");
        System.out.println(onto.getAxioms());
		**/
		
		/**
		OWLReasoner jfact = jfactfac.createNonBufferingReasoner(onto);
		System.out.println(ENT.toString() + ": " + jfact.isEntailed(ENT));
		
		
		Set<OWLClass> classes = onto.getClassesInSignature();
		for(OWLClass cl1:classes)
		{
			for(OWLClass cl2:classes)
			{
				OWLSubClassOfAxiom toCheck = df.getOWLSubClassOfAxiom(cl1,cl2);
				System.out.println(toCheck.toString() + ": " + jfact.isEntailed(toCheck));
			}
		}
		**/
		
		/**
		IRI subnnf = IRI.create("http://abstract.com/A");
		IRI sup1nnf = IRI.create("http://abstract.com/B");
		IRI sup2nnf = IRI.create("http://abstract.com/C");
		OWLClass A = df.getOWLClass(subnnf);
		OWLClass B = df.getOWLClass(sup1nnf);
		OWLClass C = df.getOWLClass(sup2nnf);
		OWLClass D = df.getOWLClass(IRI.create("http://abstract.com/D"));
		OWLClass E = df.getOWLClass(IRI.create("http://abstract.com/E"));
		OWLObjectUnionOf OR = df.getOWLObjectUnionOf(B,C);
		
		OWLSubClassOfAxiom SUB = df.getOWLSubClassOfAxiom(A,B);
		OWLSubClassOfAxiom SUB2 = df.getOWLSubClassOfAxiom(B,C);
		OWLSubClassOfAxiom SUB3 = df.getOWLSubClassOfAxiom(C,D);
		OWLObjectProperty R = df.getOWLObjectProperty(IRI.create("http://abstract.com/R"));
		OWLObjectProperty P = df.getOWLObjectProperty(IRI.create("http://abstract.com/P"));
		OWLObjectPropertyDomainAxiom DOM = df.getOWLObjectPropertyDomainAxiom(R,E);
		OWLObjectPropertyRangeAxiom RAN = df.getOWLObjectPropertyRangeAxiom(R,D);
		OWLInverseObjectPropertiesAxiom INV = df.getOWLInverseObjectPropertiesAxiom(R,P);
		OWLSubClassOfAxiom EX = df.getOWLSubClassOfAxiom(E,df.getOWLObjectSomeValuesFrom(R,df.getOWLThing()));
		
		HashSet<OWLAxiom> JUST = new HashSet<OWLAxiom>();
		JUST.add(SUB);
		JUST.add(SUB2);
		JUST.add(SUB3);
		JUST.add(DOM);
		JUST.add(RAN);
		JUST.add(INV);
		JUST.add(EX);
		OWLSubClassOfAxiom ENT = df.getOWLSubClassOfAxiom(A, df.getOWLObjectSomeValuesFrom(P, E));
		System.out.println("Check NNF form via convinence:");
		System.out.println("Original: " + SUB);
		System.out.println("NNF: " + SUB.getNNF());
		Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(ENT,JUST);
		**/
		
		/**
		OWLClass C1 = df.getOWLClass(IRI.create("http://abstract.com/C1"));
		OWLClass C2 = df.getOWLClass(IRI.create("http://abstract.com/C2"));
		OWLClass C3 = df.getOWLClass(IRI.create("http://abstract.com/C3"));
		OWLClass C4 = df.getOWLClass(IRI.create("http://abstract.com/C4"));
		OWLClass C5 = df.getOWLClass(IRI.create("http://abstract.com/C5"));
		OWLObjectProperty Prop1 = df.getOWLObjectProperty(IRI.create("http://abstract.com/Prop1"));
		OWLSubClassOfAxiom ENT = df.getOWLSubClassOfAxiom(C1, C2);
		OWLObjectPropertyDomainAxiom DOM = df.getOWLObjectPropertyDomainAxiom(Prop1,C4);
		OWLSubClassOfAxiom SUB1 = df.getOWLSubClassOfAxiom(C3, C4);
		OWLSubClassOfAxiom SUB2 = df.getOWLSubClassOfAxiom(C4, C2);
		OWLEquivalentClassesAxiom EQU = df.getOWLEquivalentClassesAxiom(C3,df.getOWLObjectUnionOf(df.getOWLObjectSomeValuesFrom(Prop1,C5),df.getOWLObjectAllValuesFrom(Prop1,C5)));
		HashSet<OWLAxiom> JUST = new HashSet<OWLAxiom>();
		JUST.add(EQU);
		JUST.add(SUB1);
		JUST.add(SUB2);
		JUST.add(DOM);
		Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(ENT,JUST);
		**/
		
		
	
		//ExplanationGeneratorFactory<OWLAxiom> egf = ExplanationManager.createExplanationGeneratorFactory(jfactfac);
		//LaconicExplanationGeneratorFactory<OWLAxiom> legf = new LaconicExplanationGeneratorFactory<OWLAxiom>(egf);
		//ExplanationGenerator<OWLAxiom> leg = legf.createExplanationGenerator(onto);
		//System.out.println(leg.getExplanations(ENT));
		
		/**
		Set<OWLClass> sig = onto.getClassesInSignature();
		sig.add(df.getOWLNothing());
		sig.add(df.getOWLThing());
		
		jfact1.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		hermit1.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		//factplus1.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		Set<OWLAxiom> jfactent1 = new HashSet<OWLAxiom>();
		Set<OWLAxiom> factent1 = new HashSet<OWLAxiom>();
		Set<OWLAxiom> hermitent1 = new HashSet<OWLAxiom>();
		Set<OWLAxiom> jfactent2 = new HashSet<OWLAxiom>();
		Set<OWLAxiom> factent2 = new HashSet<OWLAxiom>();
		Set<OWLAxiom> hermitent2 = new HashSet<OWLAxiom>();
		
		for(OWLClass cl:sig)
		{
			for(OWLClass cl2:sig)
			{
				boolean h1 = false;
				boolean h2 = false;
				boolean j1 = false;
				boolean j2 = false;
				
				OWLSubClassOfAxiom ent = df.getOWLSubClassOfAxiom(cl,cl2);
				if(hermit1.isEntailed(ent))
				{
					hermitent1.add(ent);
					h1 = true;
				}
				if(hermit2.isEntailed(ent))
				{
					hermitent2.add(ent);
					h2 = true;
				}
				if(jfact1.isEntailed(ent))
				{
					jfactent1.add(ent);
					j1 = true;
				}
				if(jfact2.isEntailed(ent))
				{
					jfactent2.add(ent);
					j2 = true;
				}
				boolean agree = h1 && h2 && j1 && j2;				
				System.out.println(ent + ": " + h1 + " " + h2 + " " +  j1 + " "+ j2 + " " + agree);
			}
		}
		**/
		/**
		Set<OWLAxiom> union = new HashSet<OWLAxiom>();
		union.addAll(hermitent1);
		union.addAll(hermitent2);
		union.addAll(factent1);
		union.addAll(factent2);
		union.addAll(jfactent1);
		union.addAll(jfactent2);
		Set<OWLAxiom> intersect = new HashSet<OWLAxiom>(hermitent1);
		intersect.retainAll(hermitent2);
		intersect.retainAll(jfactent1);
		intersect.retainAll(jfactent2);
		intersect.retainAll(factent1);
		intersect.retainAll(factent2);
		
		Set<OWLAxiom> difference = new HashSet<OWLAxiom>(union);
		difference.removeAll(intersect);
		
		ExplanationGeneratorFactory<OWLAxiom> hermitexFac = ExplanationManager.createExplanationGeneratorFactory(hermitfac);
		ExplanationGeneratorFactory<OWLAxiom> factexFac = ExplanationManager.createExplanationGeneratorFactory(factfac);
		ExplanationGeneratorFactory<OWLAxiom> jfactexFac = ExplanationManager.createExplanationGeneratorFactory(jfactfac);
		
		
		
		if(!difference.isEmpty())
		{
			ExplanationGenerator<OWLAxiom> hermitex = hermitexFac.createExplanationGenerator(onto);
			ExplanationGenerator<OWLAxiom> factex = factexFac.createExplanationGenerator(onto);
			ExplanationGenerator<OWLAxiom> jfactex = jfactexFac.createExplanationGenerator(onto);
			for(OWLAxiom ax:difference)
			{
				Set<Explanation<OWLAxiom>> hermitexplan = hermitex.getExplanations(ax, 1);
				Set<Explanation<OWLAxiom>> factexplan = factex.getExplanations(ax, 1);
				Set<Explanation<OWLAxiom>> jfactexplan = jfactex.getExplanations(ax, 1);
				if(!hermitexplan.isEmpty())
				{
					for(Explanation<OWLAxiom> exp1:hermitexplan)
					{
						OWLOntology onto1 = ontoman.createOntology(exp1.getAxioms());
						ontoman.saveOntology(onto1, arg1)
					}
				}
				
			}
		}
		**/
		
		//DefaultCandidateLemmaGenerator<OWLAxiom> dclg = new DefaultCandidateLemmaGenerator<OWLAxiom>(jfactfac, df);
		//System.out.println(dclg.getCandidateLemmas(exp));
		/**
		LaconicGCIComplexityCalculator lgcc = new LaconicGCIComplexityCalculator(pelletfac);
		ModalDepthComplexityCalculator mdcc = new ModalDepthComplexityCalculator();
		**/
		
		
		KuduComplexityCalculator kcc = new KuduComplexityCalculator(hermitfac);
		//HashSet<OWLAxiom> empty = new HashSet<OWLAxiom>();
		//System.out.println(kcc.computeComplexity(exp, ENT, empty));
		//System.out.println(kcc.getComplexityBreakdownMap());
		//StructuralComplexityCalculator scc = new StructuralComplexityCalculator();
		EntailmentCheckerFactory<OWLAxiom> ecf = new SatisfiabilityEntailmentCheckerFactory(hermitfac);
		//LemmatisedJustificationGenerator ljg = new LemmatisedJustificationGenerator(kcc,ecf,jfactfac,df);
		//p("proof"+kcc.computeComplexity(exp, ENT, JUST));
		
		
		
		//LemmaGeneratorManager<OWLAxiom> lgm = new LemmaGeneratorManager<OWLAxiom>(jfactfac, df);
		
		//System.out.println(lgm.getLemmas(exp, egf));
		
		//System.out.println(mdcc.computeComplexity(exp, ENT, JUST));
		//System.out.println(ljg.getLemmatisedExplanation(exp, JUST));
		
		
		ProofGenerator pg = new ProofGenerator(hermitfac,kcc, df, ecf);
		
		Proof pr = pg.generateProof(exp);		
		System.out.print(pr);
		
		
		Set<OWLReasonerFactory> set = new HashSet<OWLReasonerFactory>();
		set.add(hermitfac);
		set.add(pelletfac);
		set.add(jfactfac);
		//set.add(factfac);
		PutativeProofGenerator ppg = new PutativeProofGenerator(set);
		PutativeProof ppr = ppg.producePutativeProof(pr);
		ppr.produceDisagreements();
		/**
		System.out.println("");
		for(OWLAxiom ax:pr.getMap().keySet()){
		System.out.println(ax);
		System.out.println("");
		System.out.println("get Explanation");
		System.out.println(pr.getExplanation(ax));
		System.out.println("get Reg Exp");
		System.out.println(pr.getRegularJustification(ax));
		System.out.println("");
		}
		

		
		/**
		System.out.println(pr);
		String root = pr.getRoot().getEntailment().toString();
		System.out.println(root.replaceAll("<","").replaceAll(">","").replaceAll("[(]","_").replaceAll(" ", "_"));
		
		LemmatisedReasonerVerification lrv = new LemmatisedReasonerVerification(jfactfac, ontoman);
		try {
			lrv.lemmaSaver(pr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		**/
		
		//Explanation<OWLAxiom> lemmaexp = ljg.getLemmatisedExplanation(exp, JUST);
		//System.out.println(lemmaexp.getAxioms());
		/**
		IRI subIRI = IRI.create("http://purl.obolibrary.org/obo/OBI_0001573");
		IRI supIRI = IRI.create("http://purl.obolibrary.org/obo/BFO_0000004");
		OWLClass sub = df.getOWLClass(subIRI);
		OWLClass sup = df.getOWLClass(supIRI);
		OWLSubClassOfAxiom sb = df.getOWLSubClassOfAxiom(sub, sup);
		File file = new File ("/home/michael/justifications/bco/OBI_0001573_BFO_0000004/hermit/just_8_hermit_OBI_0001573_BFO_0000004_1429882212138.owl");
		try {
			OWLOntology justonto = ontoman.loadOntologyFromOntologyDocument(file);
			Set<OWLAxiom> just = justonto.getAxioms();
			System.out.println("Printing original just:");
			for(OWLAxiom as:just)
			{
				System.out.println(as);
			}
			System.out.println("");
			Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(sb,just);
			ExplanationGeneratorFactory<OWLAxiom> egf = ExplanationManager.createExplanationGeneratorFactory(hermitfac);
			LaconicExplanationGeneratorFactory<OWLAxiom> legf = new LaconicExplanationGeneratorFactory<OWLAxiom>(egf);
			//LemmaGeneratorManager<OWLAxiom> lgm = new LemmaGeneratorManager<OWLAxiom>(hermitfac,df);
			//java.util.List<LemmaGenerator<OWLAxiom>> lg = lgm.getGenerators(exp, legf);
			ExistentialRestrictionLemmaGenerator<OWLAxiom> nlg = new ExistentialRestrictionLemmaGenerator<OWLAxiom>(exp,egf,hermitfac,df);
			Set<Lemma> lemmaset = nlg.getCandidateLemmas();
			System.out.println("Printing candidate lemmas of just:");
			System.out.println(lemmaset);
			/** Set<OWLAxiom> lemmas = dclg.getCandidateLemmas(exp);
			System.out.println("Printing candidate lemmas of just:");
			for(OWLAxiom ax:lemmas)
			{
				System.out.println(ax);
			}
			System.out.println("");
			lemmas.removeAll(just);
			System.out.println("Printing lemmatised just without orignal axioms:");
			for(OWLAxiom ax: lemmas)
			{
				System.out.println(ax);
			}
			System.out.println("");
			System.out.println("Entailment:");
			System.out.println(exp.getEntailment());		**/	
			
			/**
			ExplanationGeneratorFactory<OWLAxiom> egf = ExplanationManager.createExplanationGeneratorFactory(hermitfac);
			LaconicExplanationGeneratorFactory<OWLAxiom> legf = new LaconicExplanationGeneratorFactory<OWLAxiom>(egf);
			ComplexityCalculator calc = new LaconicGCIComplexityCalculator(hermitfac);
			LemmaGeneratorManager<OWLAxiom> lgm = new LemmaGeneratorManager<OWLAxiom>(hermitfac, df);
			System.out.println("Generating lemmas: ");
			Set<Lemma> lemmaset = lgm.getLemmas(exp, legf);
			**/
			
			//LaconicExplanationGenerator<OWLAxiom> leg = new LaconicExplanationGenerator<OWLAxiom>(just,legf,cepm);
			//Set<Explanation<OWLAxiom>> lexpset = leg.computePreciseJusts(sb, 1);
		/**
			for(Lemma ex: lemmaset)
			{
				System.out.println("Lemma: "+ ex.getLemma());
				System.out.println("Axiom: "+ ex.getAxioms());
			}
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		**/
	}

}
