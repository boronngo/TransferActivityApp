package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.transfer_activity;

import org.apache.commons.lang3.ArrayUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.App;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

/**
 * Created by yuuki on 14/10/30.
 */
public class TransferActivityClassifier {

    private Instances instances;
    private Classifier classifier;



    private static final String[] transferActivityName = {
            "Bicycle", "Bus", "Car", "Other", "Run", "Train", "Walk"
    };

    private Attribute[] params;

    public TransferActivityClassifier() {
        try {

            InputStream is = App.getInstance().getAssets().open("WekaModel128_TD_old.csv");
            CSVLoader loader = new CSVLoader();
            loader.setSource(is);

            instances = loader.getDataSet();
            instances.setClassIndex(instances.numAttributes() - 1);

            classifier = new J48();
            classifier.buildClassifier(instances);


            params = new Attribute[instances.numAttributes() - 1];

            for (int i = 0; i < instances.numAttributes() - 1; i++) {
                params[i] = instances.attribute(i);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getTransferActivity(List<Double> featureVector) {

        // instanceを作ってリアルタイムに取得した特徴量を入れる。
        Instance instance = new DenseInstance(14);
        for (int i = 0; i < featureVector.size(); i++) {
            instance.setValue(params[i], featureVector.get(i));
        }

        instance.setDataset(instances);

        try {
            // double型の配列で分類結果が返ってくる。
            double[] result = classifier.distributionForInstance(instance);

            // どのActivityが最大かを調べる
            List<Double> resultList = Arrays.asList(ArrayUtils.toObject(result));
            double max = Collections.max(resultList);

            int index = resultList.indexOf(max);

            return transferActivityName[index];

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
