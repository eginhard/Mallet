package cc.mallet.textnorm;


import cc.mallet.pipe.Pipe;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.FeatureVectorSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.util.PropertyList;

public class Instance2FeatureVectorSequence extends Pipe {
	public Instance2FeatureVectorSequence() {
		super(new Alphabet(), new LabelAlphabet());
	}
	
	@Override
	public Instance pipe(Instance carrier) {
		// Get instance and its current data.
		Instance instance = (Instance) carrier;
		String[] candidates = (String[]) instance.getData();
		
		// There will be a FeatureVector for each candidate.
		FeatureVector fvs[] = new FeatureVector[candidates.length];
		
		for (int i = 0; i < candidates.length; i++) {
			PropertyList pl = null;
			// Python combined all features into one big string. Split that and
			// add each individual feature to a PropertyList.
			String[] features = candidates[i].split(" ");
			for (int j = 0; j < features.length; j++) {
				// All features must be binary and get the same value (1.0).
				pl = PropertyList.add(features[j], 1.0, pl);
			}
			fvs[i] = new FeatureVector(getDataAlphabet(), pl, true, true);
		}
		
		// Set target label ("0" for every instance, which was already set in
		// Python, but still need to add it to the LabelAlphabet).
		LabelAlphabet ldict = (LabelAlphabet) getTargetAlphabet();
		carrier.setTarget(ldict.lookupLabel(String.valueOf(instance.getTarget())));
		carrier.setData(new FeatureVectorSequence(fvs));
		
		return carrier;
	}
}
