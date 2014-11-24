package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.R;
import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.RingerModeView;

/**
 * Created by yuuki on 14/11/16.
 */
public class TransferActivityFragment extends Fragment {

    private RingerModeView ringerModeView;
    private ImageView transferImageView;
    private TextView transferTextView;
    private PieGraph pieGraph;

    private float translateX = 140;

    private ImageView tmpHistoryImageView;
    private List<ImageView> historyImageViews = new ArrayList<ImageView>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transfer_activity, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ringerModeView = (RingerModeView) getActivity().findViewById(R.id.ringer_mode_view);
        transferImageView = (ImageView) getActivity().findViewById(R.id.transferImageView);
        transferTextView = (TextView) getActivity().findViewById(R.id.transferTextView);
        pieGraph = (PieGraph) getActivity().findViewById(R.id.pie_graph);

//        historyImageViews.add((ImageView) getActivity().findViewById(R.id.transferImageView1));
//        historyImageViews.add((ImageView) getActivity().findViewById(R.id.transferImageView2));
//        historyImageViews.add((ImageView) getActivity().findViewById(R.id.transferImageView3));
//        historyImageViews.add((ImageView) getActivity().findViewById(R.id.transferImageView4));
//        historyImageViews.add((ImageView) getActivity().findViewById(R.id.transferImageView5));
        historyImageViews.add(new ImageView(getActivity()));
        historyImageViews.add(new ImageView(getActivity()));
        historyImageViews.add(new ImageView(getActivity()));
        historyImageViews.add(new ImageView(getActivity()));
        historyImageViews.add(new ImageView(getActivity()));

        super.onActivityCreated(savedInstanceState);
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#FF4444"));
        slice.setValue(1);
        pieGraph.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#FF8800"));
        slice.setValue(1);
        pieGraph.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#FFBB33"));
        slice.setValue(1);
        pieGraph.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#99CC00"));
        slice.setValue(1);
        pieGraph.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#33B5E5"));
        slice.setValue(1);
        pieGraph.addSlice(slice);
        pieGraph.setInnerCircleRatio(230);
        pieGraph.setPadding(1);

    }

    public void updateTransferActivity(String transferActivity) {
        transferTextView.setText(transferActivity);

        int drawable = 0;

        if (transferActivity.equals("Bicycle")) {

            drawable = R.drawable.bicycle;

        } else if (transferActivity.equals("Bus")) {

            drawable = R.drawable.bus;

        } else if (transferActivity.equals("Car")) {

            drawable = R.drawable.car;

        } else if (transferActivity.equals("Other")) {

            drawable = R.drawable.other;

        } else if (transferActivity.equals("Run")) {

            drawable = R.drawable.run;

        } else if (transferActivity.equals("Train")) {

            drawable = R.drawable.train;

        } else if (transferActivity.equals("Walk")) {

            drawable = R.drawable.walk;

        }

        transferImageView.setImageResource(drawable);
        addHistory(drawable);
    }


    private boolean isRunning = false;

    public synchronized void addHistory(int drawable) {

        if (!isRunning) {
            isRunning = true;
            List<Animator> animators = new ArrayList<Animator>();
            ListIterator<ImageView> it = historyImageViews.listIterator(historyImageViews.size());

            RelativeLayout layout = (RelativeLayout) getActivity().findViewById(R.id.history_layout);

            tmpHistoryImageView = new ImageView(getActivity());
            tmpHistoryImageView.setId(View.generateViewId());
            tmpHistoryImageView.setImageResource(drawable);
            tmpHistoryImageView.setVisibility(View.INVISIBLE);
            int dp = (int) (50 * getActivity().getResources().getDisplayMetrics().densityDpi / 160f);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dp, dp);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.transferImageView1);

            layout.addView(tmpHistoryImageView, params);


            while (it.hasPrevious()) {
                ImageView imageView = it.previous();
                ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationX", imageView.getTranslationX(), imageView.getTranslationX() + translateX);
                animator.setDuration(300);
                animators.add(animator);
            }

            PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("translationY", 0, -83f);
            PropertyValuesHolder holderAlpha = PropertyValuesHolder.ofFloat("alpha", 0, 1.0f);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(tmpHistoryImageView, holderX, holderAlpha);
            animator.setDuration(300);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    tmpHistoryImageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animators.add(animator);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(animators);
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    for (int i = historyImageViews.size() - 1; i > 0; i--) {
                        historyImageViews.set(i, historyImageViews.get(i - 1));
                    }
                    historyImageViews.set(0, tmpHistoryImageView);
                    tmpHistoryImageView = null;
                    isRunning = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            animatorSet.start();
        }

    }

    public void showRingerModeView(int ringerMode) {
        ringerModeView.show(ringerMode);
    }

}
