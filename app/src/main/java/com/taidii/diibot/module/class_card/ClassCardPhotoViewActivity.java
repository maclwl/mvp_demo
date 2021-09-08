package com.taidii.diibot.module.class_card;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.taidii.diibot.R;
import com.taidii.diibot.base.BaseActivity;
import com.taidii.diibot.entity.event_bus.Event;
import com.taidii.diibot.module.class_card.viewJzd.ResizeImageView;
import com.taidii.diibot.utils.ImageViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ClassCardPhotoViewActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_photoTitle)
    TextView tvPhotoTitle;
    private List<View> views = new ArrayList<>();
    private List<String> photoList = new ArrayList<>();
    private int index = 0;
    private MyPagerAdapter myPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_list_record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    public class MyPagerAdapter extends PagerAdapter {
        private List<View> views;

        public MyPagerAdapter(List<View> views) {
            this.views = views;
        }

        private void setViews(List<View> views) {
            this.views = views;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // return super.instantiateItem(container, position);
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView(views.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
//            return "标题" + position;
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void setStatusBar() {
//        ImmersionBar.with(this)
//                .transparentBar()
//                .init();
    }

    @Override
    protected void initView() {
        photoList.addAll(getIntent().getStringArrayListExtra("photos"));
        index = getIntent().getIntExtra("index", 0);


        for (String str : photoList) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View v = layoutInflater.inflate(R.layout.activity_class_card_photo, null);
            ImageView iv_pic = v.findViewById(R.id.imageView2);
//            ImageViewUtils.loadImage(act, str,iv_pic, R.drawable.avatar_loading2);
            RequestOptions options = new RequestOptions()
                    .centerInside()
                    .error( R.drawable.avatar_loading2)
                    .placeholder(R.color.color_f6)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(act)
                    .load(str)
                    .apply(options)
                    .into(iv_pic);
            views.add(v);
        }

        myPagerAdapter = new MyPagerAdapter(views);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                tvPhotoTitle.setText((position + 1) + "/" + photoList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tvPhotoTitle.setText((index + 1) + "/" + photoList.size());
        viewPager.setCurrentItem(index);
        myPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void init() {


    }

    @OnClick({R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
        }
    }


    /*详情签到成功，刷新列表数据*/
    @Override
    protected void receiveEvent(Event event) {
    }


}
