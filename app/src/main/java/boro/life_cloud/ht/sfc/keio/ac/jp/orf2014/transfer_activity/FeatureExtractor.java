package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.transfer_activity;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.List;



public class FeatureExtractor {
	public static ArrayList<Double> getfeaturevector(List<Double> xList, List<Double> yList, List<Double> zList, String featureSet) {
		ArrayList<Double> FeatureVector = new ArrayList<Double>();
		int attr_index = 1;
		// convert list to double[]
		double[] x = new double[xList.size()];
		double[] y = new double[xList.size()];
		double[] z = new double[xList.size()];
		for (int j = 0; j < x.length; j++) {
		    x[j] = xList.get(j);
		}
		for (int j = 0; j < x.length; j++) {
		    y[j] = yList.get(j);
		}
		for (int j = 0; j < x.length; j++) {
		    z[j] = zList.get(j);
		}

		// Means
	    Mean mean = new Mean();
		double xmean = mean.evaluate(x);
		double ymean = mean.evaluate(y);
		double zmean = mean.evaluate(z);
		double[] mag_vec = mag_vec(x, y, z);
		FeatureVector.add(xmean);
		attr_index++;
		FeatureVector.add(ymean);
		attr_index++;
		FeatureVector.add(zmean);
		attr_index++;

		// Mags
		// inst.setValue(attr_index, euclidian(xmean, ymean, zmean));
		FeatureVector.add(euclidian(xmean, ymean, zmean));
		attr_index++;
		FeatureVector.add(mean.evaluate(mag_vec));
		attr_index++;

		// Variances
		Variance var = new Variance();
		FeatureVector.add(var.evaluate(x, xmean));
		attr_index++;
		FeatureVector.add(var.evaluate(y, ymean));
		attr_index++;
		FeatureVector.add(var.evaluate(z, zmean));
		attr_index++;

		// Covariances
		Covariance cov = new Covariance();
		FeatureVector.add(cov.covariance(x, y));
		attr_index++;
		FeatureVector.add(cov.covariance(x, z));
		attr_index++;
		FeatureVector.add(cov.covariance(y, z));
		attr_index++;

		// Correlation
		FeatureVector.add(cov.covariance(x, y)
				  / Math.sqrt(var.evaluate(x, xmean) * var.evaluate(y, ymean)));
		attr_index++;
		FeatureVector.add(cov.covariance(x, z)
				  / Math.sqrt(var.evaluate(x, xmean) * var.evaluate(z, zmean)));
		attr_index++;
		FeatureVector.add(cov.covariance(y, z)
				  / Math.sqrt(var.evaluate(y, ymean) * var.evaluate(z, zmean)));
		attr_index++;

		if(featureSet.indexOf("FD") > -1){
			// FFT
			int padlength = 1;
			int length = x.length;
			while (padlength < length) {
			    padlength = padlength << 1;
			}

			double[] pad_x = pad(x, padlength);
			double[] pad_y = pad(y, padlength);
			double[] pad_z = pad(z, padlength);
			double[] pad_mag_vec = pad(mag_vec, padlength);

		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] fft_x = fft.transform(pad_x, TransformType.FORWARD);
		Complex[] fft_y = fft.transform(pad_y, TransformType.FORWARD);
		Complex[] fft_z = fft.transform(pad_z, TransformType.FORWARD);
		Complex[] fft_mag = fft.transform(pad_mag_vec, TransformType.FORWARD);

		double[] fft_x_abs = new double[fft_x.length];
		for (int i = 0; i < fft_x_abs.length; i++) {
		    fft_x_abs[i] = fft_x[i].abs();
		}
		double[] fft_y_abs = new double[fft_y.length];
		for (int i = 0; i < fft_y_abs.length; i++) {
		    fft_y_abs[i] = fft_y[i].abs();
		}
		double[] fft_z_abs = new double[fft_z.length];
		for (int i = 0; i < fft_z_abs.length; i++) {
		    fft_z_abs[i] = fft_z[i].abs();
		}
		double[] fft_mag_abs = new double[fft_mag.length];
		for (int i = 0; i < fft_mag_abs.length; i++) {
		    fft_mag_abs[i] = fft_mag[i].abs();
		}
		// Get a DescriptiveStatistics instance
		double fft_x_abs_max = 0.0;
		DescriptiveStatistics stats_x = new DescriptiveStatistics();

		// Add the data from the array
		for( int i = 0; i < fft_x_abs.length; i++) {
		    stats_x.addValue(fft_x_abs[i]);
		    fft_x_abs_max = stats_x.getMax();
		}
		double fft_y_abs_max = 0.0;
		DescriptiveStatistics stats_y = new DescriptiveStatistics();

		// Add the data from the array
		for( int i = 0; i < fft_y_abs.length; i++) {
		    stats_y.addValue(fft_y_abs[i]);
		    fft_y_abs_max = stats_y.getMax();
		}
		double fft_z_abs_max = 0.0;
		DescriptiveStatistics stats_z = new DescriptiveStatistics();

		// Add the data from the array
		for( int i = 0; i < fft_z_abs.length; i++) {
		    stats_z.addValue(fft_z_abs[i]);
		    fft_z_abs_max = stats_z.getMax();
		}
		double fft_mag_abs_max = 0.0;
		DescriptiveStatistics stats_mag = new DescriptiveStatistics();

		// Add the data from the array
		for( int i = 0; i < fft_mag_abs.length; i++) {
		    stats_mag.addValue(fft_mag_abs[i]);
		    fft_mag_abs_max = stats_mag.getMax();
		}

		// energy
		FeatureVector.add(mean.evaluate(square(fft_x_abs)));
		attr_index++;
		FeatureVector.add(mean.evaluate(square(fft_y_abs)));
		attr_index++;
		FeatureVector.add(mean.evaluate(square(fft_z_abs)));
		attr_index++;
		FeatureVector.add(mean.evaluate(square(fft_mag_abs)));
		attr_index++;

		Frequency f_x = new Frequency();
		for (int i = 0; i < fft_x_abs.length; i++) {
		    f_x.addValue(fft_x_abs[i]);
		}
		Frequency f_y = new Frequency();
		for (int i = 0; i < fft_y_abs.length; i++) {
		    f_y.addValue(fft_y_abs[i]);
		}
		Frequency f_z = new Frequency();
		for (int i = 0; i < fft_z_abs.length; i++) {
		    f_z.addValue(fft_z_abs[i]);
		}
		// Entrophy
		double bin_size = 65535 / 255;// Assume 256 bins as in matlab's imhist
		// function
		double[] histogram_x = new double[256];
		double[] histogram_y = new double[256];
		double[] histogram_z = new double[256];
		double[] histogram_mag = new double[256];
		double entropy_x = 0;
		double entropy_y = 0;
		double entropy_z = 0;
		double entropy_mag = 0;
		int quotient = 0;
		double remainder = 0;
		double probability = 0;
		for (int i = 0; i < fft_x_abs.length; i++) {
		    quotient = (int) ((fft_x_abs[i] * 65025) / (bin_size*fft_x_abs_max));
		    remainder = ((fft_x_abs[i] * 65025) / (fft_x_abs_max)) % bin_size;
		    if (remainder < bin_size / 2) {
			histogram_x[quotient]++;
		    } else {
			histogram_x[quotient + 1]++;
		    }
		}
		for (int i = 0; i < fft_y_abs.length; i++) {
		    quotient = (int) ((fft_y_abs[i] * 65025) / (bin_size*fft_y_abs_max));
		    remainder = ((fft_y_abs[i] * 65025) / (fft_y_abs_max)) % bin_size;
		    if (remainder < bin_size / 2) {
			histogram_y[quotient]++;
		    } else {
			histogram_y[quotient + 1]++;
		    }
		}
		for (int i = 0; i < fft_z_abs.length; i++) {
		    quotient = (int) ((fft_z_abs[i] * 65025) / (bin_size*fft_z_abs_max));
		    remainder = ((fft_z_abs[i] * 65025) / (fft_z_abs_max)) % bin_size;
		    if (remainder < bin_size / 2) {
			histogram_z[quotient]++;
		    } else {
			histogram_z[quotient + 1]++;
		    }
		}
		for (int i = 0; i < fft_mag_abs.length; i++) {
		    quotient = (int) ((fft_mag_abs[i] * 65025) / (bin_size*fft_mag_abs_max));
		    remainder = ((fft_mag_abs[i] * 65025) / (fft_mag_abs_max)) % bin_size;
		    if (remainder < bin_size / 2) {
			histogram_mag[quotient]++;
		    } else {
			histogram_mag[quotient + 1]++;
		    }
		}
		for (int i = 0; i < 255; i++) {
		    probability = histogram_x[i] / fft_x_abs.length;
		    if (probability > 0) {
			entropy_x += (-1) * (probability)
			    * (Math.log(probability) / Math.log(2));
		    }
		    probability = histogram_y[i] / fft_y_abs.length;
		    if (probability > 0) {
			entropy_y += (-1) * (probability)
			    * (Math.log(probability) / Math.log(2));
		    }
		    probability = histogram_z[i] / fft_z_abs.length;
		    if (probability > 0) {
			entropy_z += (-1) * (probability)
			    * (Math.log(probability) / Math.log(2));
		    }
		    probability = histogram_mag[i] / fft_mag_abs.length;
		    if (probability > 0) {
			entropy_mag += (-1) * (probability)
			    * (Math.log(probability) / Math.log(2));
		    }
		}
		FeatureVector.add(entropy_x);
		attr_index++;
		FeatureVector.add(entropy_y);
		attr_index++;
		FeatureVector.add(entropy_z);
		attr_index++;
		FeatureVector.add(entropy_mag);
		attr_index++;

		    }//If including FD
		return (FeatureVector);
	    }

    private static double[] pad(double x[], int l) {
	double[] pad = x;
	if (x.length != l) {
	    pad = new double[l];
	    for (int i = 0; i < x.length; i++) {
		pad[i] = x[i];
	    }
	    for (int i = x.length; i < pad.length; i++) {
		pad[i] = 0;
	    }
	}
	return pad;
    }

    private static double euclidian(double x, double y, double z) {
	return Math.sqrt(x * x + y * y + z * z);
    }

    private static double[] mag_vec(double x[], double y[], double z[]) {
	double[] mag_vec = new double[x.length];
	for (int i = 0; i < mag_vec.length; i++) {
	    mag_vec[i] = euclidian(x[i], y[i], z[i]);
	}
	return mag_vec;
    }

    private static double[] square(double x[]) {
	double[] square = new double[x.length];
	for (int i = 0; i < square.length; i++) {
	    square[i] = x[i] * x[i];
	}
	return square;
    }

}