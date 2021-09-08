package com.taidii.diibot.module.school_main;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gyf.immersionbar.ImmersionBar;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.mvp.BaseMvpActivity;
import com.taidii.diibot.entity.enums.VisitorJumpEnum;
import com.taidii.diibot.entity.school.VisitorsBean;
import com.taidii.diibot.module.school_main.adapter.VisitorListAdapter;
import com.taidii.diibot.module.school_main.contract.VisitorListContract;
import com.taidii.diibot.module.school_main.presenter.VisitorListPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VisitorListActivity extends BaseMvpActivity<VisitorListPresenter> implements VisitorListContract.View
            ,VisitorListAdapter.ItemClickListener{

    @BindView(R.id.recycler_visitor)
    RecyclerView recyclerVisitor;

    private VisitorListAdapter visitorListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_visitor_list;
    }

    @Override
    protected VisitorListPresenter createPresenter() {
        return new VisitorListPresenter();
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentBar()
                .init();
    }

    @Override
    protected void initView() {
        visitorListAdapter = new VisitorListAdapter(this);
        RecyclerView.LayoutManager layoutManagerStudent = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerVisitor.setLayoutManager(layoutManagerStudent);
        recyclerVisitor.setAdapter(visitorListAdapter);
    }

    @Override
    protected void init() { }

    @Override
    protected void onResume() {
        super.onResume();
        /*获取访客列表*/
        mPresenter.getVisitorList();
    }

    @OnClick({R.id.im_back, R.id.tv_add_visitor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.tv_add_visitor:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.TYPE, VisitorJumpEnum.ADD);
                openActivity(AddEditVisitorActivity.class,bundle);
                break;
        }
    }

    /*访客列表*/
    @Override
    public void visitorList(List<VisitorsBean> visitorList) {
        visitorListAdapter.setDataList(visitorList);
    }

    /*跳转详情*/
    @Override
    public void clickItem(VisitorsBean visitorsBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TYPE, VisitorJumpEnum.EDIT);
        bundle.putInt(Constant.ID,visitorsBean.getId());
        openActivity(AddEditVisitorActivity.class,bundle);
    }

}
