package cc.mallet.textnorm;

import java.util.BitSet;

import cc.mallet.types.FeatureSelection;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.FeatureVectorSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

public class Utils {
	public static FeatureSelection selectFeatures(InstanceList ilist, int minCount) {
		BitSet selectedFeatures = new BitSet();
		FeatureVector counts = countFeatures(ilist);
		System.out.println("Number of features: " + ilist.getDataAlphabet().size());
		for (int i = 0; i < counts.numLocations(); i++) {
			if (counts.valueAtLocation(i) >= minCount)
				selectedFeatures.set(counts.indexAtLocation(i));
		}
		System.out.println("Number of features with count > " +
				minCount + ": " + selectedFeatures.cardinality());
		return new FeatureSelection(ilist.getDataAlphabet(), selectedFeatures);
	}
	
	private static FeatureVector countFeatures(InstanceList ilist) {
		int numFeatures = ilist.getDataAlphabet().size();
		double[] counts = new double[numFeatures];
		for (Instance instance : ilist) {
			if (ilist.getInstanceWeight(instance) == 0)
				continue;	
			FeatureVectorSequence fvs = (FeatureVectorSequence) instance.getData();
			for (int i = 0; i < fvs.size(); i++) {
				FeatureVector fv = fvs.get(i);
				for (int j = 0; j < fv.numLocations(); j++) {
					counts[fv.indexAtLocation(j)] += 1;
				}
			}
		}
		
		return new FeatureVector(ilist.getDataAlphabet(), counts);
	}
}
