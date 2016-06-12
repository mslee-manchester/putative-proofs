package man.ac.uk;

import java.io.File;
import java.io.IOException;

import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owl.explanation.lemmas.Proof;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class LemmatisedReasonerVerification {
	
	ReasonerFactory rfac;
	OWLOntologyManager ontoman;
	
	public LemmatisedReasonerVerification(ReasonerFactory rf, OWLOntologyManager onman){
		this.rfac = rf;
		this.ontoman = onman;
	}

	public void lemmaSaver(Proof proof) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException	{
		String entName = proof.getRoot().getEntailment().toString().replaceAll("<","").replaceAll(">","").replaceAll("[()]","").replaceAll(" ", "_").replaceAll("/","_");
		File dir1 = new File("/home/michael/lemmas/jfact/" + entName +"/");
		System.out.println("Made dir?" + dir1.mkdir());
		for(OWLAxiom ax:proof.getMap().keySet())
		{
			String axName = ax.toString().replaceAll("<","").replaceAll(">","").replaceAll("[()]","").replaceAll(" ", "_").replaceAll("/","_");
			File file = new File("/home/michael/lemmas/jfact/" + entName + "/" + axName + ".owl");
			file.createNewFile();
			OWLOntology lemma = ontoman.createOntology(proof.getMap().get(ax).getAxioms());
			ontoman.saveOntology(lemma, IRI.create(file.toURI()));
		}
	}
}
