package com.atguigu.shop.controller.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atguigu.shop.R;
import com.atguigu.shop.controller.home.adapter.ExpandableListViewAdapter;
import com.atguigu.shop.controller.home.adapter.GoodsListAdapter;
import com.atguigu.shop.controller.home.view.SpaceItemDecoration;
import com.atguigu.shop.controller.main.MainActivity;
import com.atguigu.shop.model.homebean.GoodsBean;
import com.atguigu.shop.model.homebean.GoodsListBean;
import com.atguigu.shop.model.NetManager;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.atguigu.shop.controller.home.adapter.HomeAdapter.GOODS_BEAN;

// 商品列表
public class GoodsListActivity extends AppCompatActivity {

    @BindView(R.id.ib_goods_list_back)
    ImageButton ibGoodsListBack;
    @BindView(R.id.tv_goods_list_search)
    TextView tvGoodsListSearch;
    @BindView(R.id.ib_goods_list_home)
    ImageButton ibGoodsListHome;
    @BindView(R.id.tv_goods_list_sort)
    TextView tvGoodsListSort;
    @BindView(R.id.tv_goods_list_price)
    TextView tvGoodsListPrice;
    @BindView(R.id.iv_goods_list_arrow)
    ImageView ivGoodsListArrow;
    @BindView(R.id.ll_goods_list_price)
    LinearLayout llGoodsListPrice;
    @BindView(R.id.tv_goods_list_select)
    TextView tvGoodsListSelect;
    @BindView(R.id.ll_goods_list_head)
    LinearLayout llGoodsListHead;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.dl_left)
    DrawerLayout dlLeft;
    @BindView(R.id.ib_drawer_layout_back)
    ImageButton ibDrawerLayoutBack;
    @BindView(R.id.tv_ib_drawer_layout_title)
    TextView tvIbDrawerLayoutTitle;
    @BindView(R.id.tv_drawer_price)
    TextView tvDrawerPrice;
    @BindView(R.id.tv_drawer_recommend)
    TextView tvDrawerRecommend;
    @BindView(R.id.rl_select_recommend_theme)
    RelativeLayout rlSelectRecommendTheme;
    @BindView(R.id.tv_drawer_type)
    TextView tvDrawerType;
    @BindView(R.id.rl_select_type)
    RelativeLayout rlSelectType;
    @BindView(R.id.ll_select_root)
    LinearLayout llSelectRoot;
    @BindView(R.id.btn_drawer_layout_cancel)
    Button btnDrawerLayoutCancel;
    @BindView(R.id.btn_drawer_layout_confirm)
    Button btnDrawerLayoutConfirm;
    @BindView(R.id.rl_select_price)
    RelativeLayout rlSelectPrice;
    @BindView(R.id.ll_price_root)
    LinearLayout llPriceRoot;
    @BindView(R.id.btn_drawer_theme_cancel)
    Button btnDrawerThemeCancel;
    @BindView(R.id.btn_drawer_theme_confirm)
    Button btnDrawerThemeConfirm;
    @BindView(R.id.ll_theme_root)
    LinearLayout llThemeRoot;
    @BindView(R.id.btn_drawer_type_cancel)
    Button btnDrawerTypeCancel;
    @BindView(R.id.btn_drawer_type_confirm)
    Button btnDrawerTypeConfirm;
    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;
    @BindView(R.id.ll_type_root)
    LinearLayout llTypeRoot;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refreshLayout;
    @BindView(R.id.rb_price_seletor)
    RadioGroup rbPriceSelector;

    @BindView(R.id.rb_theme_selector)
    RadioGroup rbThemeSelector;
    /**
     * 点击GridView的位置-取列表中的url
     */
    private int position;

    private boolean isLoadMore = false;

    /**
     * 请求网络
     */
    private String[] urls = new String[]{
            NetManager.CLOSE_STORE,
            NetManager.GAME_STORE,
            NetManager.COMIC_STORE,
            NetManager.COSPLAY_STORE,
            NetManager.GUFENG_STORE,
            NetManager.STICK_STORE,
            NetManager.WENJU_STORE,
            NetManager.FOOD_STORE,
            NetManager.SHOUSHI_STORE,
    };
    /**
     * 请求得到的数据
     */
    private GoodsListAdapter goodsListAdapter;
    /**
     * 点击价格的次数
     */
    private int click_count;
    private ExpandableListViewAdapter expandableListViewAdapter;
    /**
     * 搜索关键字
     */
    private String searchKeycode;
    private List<GoodsListBean.ResultBean.PageDataBean> result;
    private boolean priceSort = false;


    /**
     * 最终价格
     */
    private String tempPrice = "nolimit";
    private String surePrice = tempPrice;


    /**
     * 主题
     */
    private String  tempTheme = "全部";;
    private String sureTheme = tempTheme;



    /**
     * 类别
     */
    private String tempType = "";
    private String sureType = tempType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        ButterKnife.bind(this);
        //设置监听
        setListenter();
        getData();//前面
        if (searchKeycode != null) {
            Toast.makeText(this, "来自搜索的关键字==" + searchKeycode, Toast.LENGTH_SHORT).show();
            getDataFromNet(urls[position]);
        } else {
            getDataFromNet(urls[position]);
        }


    }

    private void setListenter() {
        //下拉刷新和上拉刷新
        refreshLayout.setMaterialRefreshListener(new MyMaterialRefreshListener());

        //设置筛选价格监听
        rbPriceSelector.setOnCheckedChangeListener(new MyPriceOnCheckedChangeListener());

        //设置主题监听
        rbThemeSelector.setOnCheckedChangeListener(new MyThemeOnCheckedChangeListener());


    }

    class MyThemeOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_theme_all:
                    tempTheme = "全部";
                    break;
                case R.id.rb_theme_funko:
                    tempTheme = "FUNKO";
                    break;
                case R.id.rb_theme_gress:
                    tempTheme = "长草颜文字";
                    break;
                case R.id.rb_theme_gsc:
                    tempTheme = "GSC";
                    break;
                case R.id.rb_theme_moon:
                    tempTheme = "秦时明月";
                    break;
                case R.id.rb_theme_note:
                    tempTheme = "盗墓笔记";
                    break;
                case R.id.rb_theme_quanzhi:
                    tempTheme = "全职高手";
                    break;
                case R.id.rb_theme_sword:
                    tempTheme = "剑侠情愿叁";
                    break;

            }

            Toast.makeText(GoodsListActivity.this, "" + tempTheme, Toast.LENGTH_SHORT).show();
        }
    }

    class MyPriceOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_price_nolimit:
                    tempPrice = "nolimit";
                    break;
                case R.id.rb_price_0_15:
                    tempPrice = "0-15";
                    break;
                case R.id.rb_price_15_30:
                    tempPrice = "15-30";
                    break;
                case R.id.rb_price_30_50:
                    tempPrice = "30-50";
                    break;
                case R.id.rb_price_50_70:
                    tempPrice = "50-70";
                    break;
                case R.id.rb_price_70_100:
                    tempPrice = "70-100";
                    break;
                case R.id.rb_price_100:
                    tempPrice = "100";
                    break;

            }

            Toast.makeText(GoodsListActivity.this, "" + tempPrice, Toast.LENGTH_SHORT).show();
        }
    }


    class MyMaterialRefreshListener extends MaterialRefreshListener {

        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            isLoadMore = false;
            getDataFromNet(urls[position]);
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            super.onRefreshLoadMore(materialRefreshLayout);
            isLoadMore = true;
            getDataFromNet(urls[position]);
        }
    }

    private void getData() {
        position = getIntent().getIntExtra("position", 0);
        searchKeycode = getIntent().getStringExtra(SearchActivity.SEARCH_KEYWORD);

        //把综合设置高亮
        tvGoodsListSort.setTextColor(Color.parseColor("#ed4141"));
    }

    /**
     * 根据url联网请求数据
     *
     * @param url
     */
    private void getDataFromNet(String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "GoodsListActivity联网失败" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "GoodsListActivity" + response);
                        processData(response);
                        if (!isLoadMore) {
                            refreshLayout.finishRefresh();
                        } else {
                            refreshLayout.finishRefreshLoadMore();
                            isLoadMore = false;
                        }


                    }


                });

    }

    private void processData(String response) {
        GoodsListBean goodsListBean = JSON.parseObject(response, GoodsListBean.class);
        if (!isLoadMore) {
            result = goodsListBean.getResult().getPage_data();

            if (result != null && result.size() > 0) {
                SpaceItemDecoration itemDecoration = new SpaceItemDecoration(10);

                if (goodsListAdapter == null) {
                    //设置RecyclerView适配器
                    goodsListAdapter = new GoodsListAdapter(this, result);

                    recyclerview.setAdapter(goodsListAdapter);
                    recyclerview.removeItemDecoration(itemDecoration);
                    //设置分割线
                    recyclerview.addItemDecoration(itemDecoration);
                    //设置布局管理器
                    GridLayoutManager manager = new GridLayoutManager(this, 2);
                    recyclerview.setLayoutManager(manager);
                    //设置item的点击事件
                    goodsListAdapter.setOnItemClickListener(new GoodsListAdapter.OnItemClickListener() {
                        @Override
                        public void setOnItemClickListener(GoodsListBean.ResultBean.PageDataBean data) {

                            //商品或者产品，货物
                            GoodsBean goodsBean = new GoodsBean();
                            goodsBean.setProduct_id(data.getProduct_id());//产品id
                            goodsBean.setCover_price(data.getCover_price());//价格
                            goodsBean.setName(data.getName());//名称
                            goodsBean.setFigure(data.getFigure());//图片地址

                            Intent intent = new Intent(GoodsListActivity.this, GoodsInfoActivity.class);
                            intent.putExtra(GOODS_BEAN, goodsBean);
                            startActivity(intent);

                        }
                    });
                } else {

                    goodsListAdapter.update(result);
                    goodsListAdapter.notifyDataSetChanged();//刷新
                }


                showSelectorLayout();

            }

        } else {
            //添加并且刷新
            goodsListAdapter.addAll(goodsListBean.getResult().getPage_data());


        }


    }


    @OnClick({R.id.ib_drawer_layout_back,
            R.id.rl_select_price, R.id.rl_select_recommend_theme,
            R.id.rl_select_type, R.id.btn_drawer_layout_cancel,
            R.id.btn_drawer_layout_confirm, R.id.btn_drawer_theme_cancel,
            R.id.btn_drawer_theme_confirm, R.id.btn_drawer_type_cancel,
            R.id.btn_drawer_type_confirm, R.id.ib_goods_list_back,
            R.id.tv_goods_list_search, R.id.ib_goods_list_home,
            R.id.tv_goods_list_sort, R.id.tv_goods_list_price,
            R.id.tv_goods_list_select,R.id.btn_select_sure

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_goods_list_back:
                finish();
                break;
            case R.id.tv_goods_list_search:
//                Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                //关闭
                finish();
                break;
            case R.id.ib_goods_list_home:
//                Toast.makeText(this, "返回主页", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("checkedid", R.id.rb_home);//跳转到主页面
                startActivity(intent);
                finish();
                break;
            case R.id.tv_goods_list_sort:

                click_count = 0;
                //设置价格箭头向下
                ivGoodsListArrow.setBackgroundResource(R.drawable.new_price_sort_normal);
//                Toast.makeText(this, "综合排序", Toast.LENGTH_SHORT).show();
                //把综合设置高亮
                tvGoodsListSort.setTextColor(Color.parseColor("#ed4141"));


                //其他设置默认-价格和筛选
                tvGoodsListSelect.setTextColor(Color.parseColor("#333538"));
                //价格文字变成默认黑色
                tvGoodsListPrice.setTextColor(Color.parseColor("#333538"));

                isLoadMore = false;
                getDataFromNet(urls[position]);

                break;
            case R.id.tv_goods_list_price:

                click_count++;

                if (click_count % 2 == 1) {
                    // 箭头向下红
                    ivGoodsListArrow.setBackgroundResource(R.drawable.new_price_sort_desc);
                    priceSort = false;
                } else {
                    // 箭头向上红
                    ivGoodsListArrow.setBackgroundResource(R.drawable.new_price_sort_asc);
                    priceSort = true;
                }

//                Toast.makeText(this, "价格排序", Toast.LENGTH_SHORT).show();
                //价格设置高亮
                tvGoodsListPrice.setTextColor(Color.parseColor("#ed4141"));

                //其他设置默认-综合和筛选
                tvGoodsListSort.setTextColor(Color.parseColor("#333538"));
                tvGoodsListSelect.setTextColor(Color.parseColor("#333538"));


                //根据价格排序
                Collections.sort(goodsListAdapter.getDatas(), new Comparator<GoodsListBean.ResultBean.PageDataBean>() {
                    @Override
                    public int compare(GoodsListBean.ResultBean.PageDataBean o1, GoodsListBean.ResultBean.PageDataBean o2) {
                        if (!priceSort) {
                            if (Double.parseDouble(o1.getCover_price()) < Double.parseDouble(o2.getCover_price())) {
                                return 1;
                            } else if (Double.parseDouble(o1.getCover_price()) > Double.parseDouble(o2.getCover_price())) {
                                return -1;
                            } else {
                                return 0;
                            }

                        } else {
                            if (Double.parseDouble(o1.getCover_price()) < Double.parseDouble(o2.getCover_price())) {
                                return -1;
                            } else if (Double.parseDouble(o1.getCover_price()) > Double.parseDouble(o2.getCover_price())) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }


                    }
                });

                goodsListAdapter.notifyItemRangeChanged(0, goodsListAdapter.getItemCount());


                break;
            case R.id.tv_goods_list_select://筛选

                click_count = 0;
                //设置价格箭头向下
                ivGoodsListArrow.setBackgroundResource(R.drawable.new_price_sort_normal);
//                Toast.makeText(this, "筛选排序", Toast.LENGTH_SHORT).show();

                //筛选设置高亮
                tvGoodsListSelect.setTextColor(Color.parseColor("#ed4141"));

                //其他设置默认-综合和价格
                tvGoodsListPrice.setTextColor(Color.parseColor("#333538"));
                tvGoodsListSort.setTextColor(Color.parseColor("#333538"));


                //显示筛选菜单
                dlLeft.openDrawer(Gravity.RIGHT);
                showSelectorLayout();//显示筛选-隐藏其他

                break;
            case R.id.ib_drawer_layout_back:
                //关闭筛选菜单
                dlLeft.closeDrawers();
                break;
            case R.id.rl_select_price://筛选-价格
                llPriceRoot.setVisibility(View.VISIBLE);
                showPriceLayout();
                break;
            case R.id.rl_select_recommend_theme://筛选-推荐主题
                llThemeRoot.setVisibility(View.VISIBLE);
                showThemeLayout();
                break;
            case R.id.rl_select_type://筛选-类别
                llTypeRoot.setVisibility(View.VISIBLE);
                showTypeLayout();
                break;
            case R.id.btn_drawer_layout_cancel:

                showSelectorLayout();
                llSelectRoot.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_drawer_layout_confirm:
//                Toast.makeText(this, "价格-确定", Toast.LENGTH_SHORT).show();
                surePrice = tempPrice;
                //设置确定的价格
                tvDrawerPrice.setText(surePrice);
                showSelectorLayout();
                llSelectRoot.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_drawer_theme_cancel:
                showSelectorLayout();
                llSelectRoot.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_drawer_theme_confirm:
//                Toast.makeText(this, "主题-确定", Toast.LENGTH_SHORT).show();
//                tv_drawer_recommend.
                sureTheme = tempTheme;
                tvDrawerRecommend.setText(sureTheme);
                showSelectorLayout();
                llSelectRoot.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_drawer_type_cancel:
                showSelectorLayout();
                llSelectRoot.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_drawer_type_confirm:
//                Toast.makeText(this, "分类-确定", Toast.LENGTH_SHORT).show();
                sureType = tempType;
//                tv_drawer_type
                tvDrawerType.setText(sureType);
                showSelectorLayout();
                llSelectRoot.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_select_sure://筛选-确定
                Toast.makeText(this, "Price=="+surePrice+",Theme="+tempTheme+",sureType=="+sureType, Toast.LENGTH_SHORT).show();
                dlLeft.closeDrawers();
                getDataFromNet(urls[2]);
                break;
        }
    }

    //筛选页面
    private void showSelectorLayout() {
        llPriceRoot.setVisibility(View.GONE);
        llThemeRoot.setVisibility(View.GONE);
        llTypeRoot.setVisibility(View.GONE);
    }


    //价格页面
    private void showPriceLayout() {
        llSelectRoot.setVisibility(View.GONE);
        llThemeRoot.setVisibility(View.GONE);
        llTypeRoot.setVisibility(View.GONE);
    }

    //主题页面
    private void showThemeLayout() {
        llSelectRoot.setVisibility(View.GONE);
        llPriceRoot.setVisibility(View.GONE);
        llTypeRoot.setVisibility(View.GONE);
    }

    //类别页面
    private void showTypeLayout() {
        llSelectRoot.setVisibility(View.GONE);
        llPriceRoot.setVisibility(View.GONE);
        llThemeRoot.setVisibility(View.GONE);

        //初始化ExpandableListView
        initExpandableListView();
    }

    private ArrayList<String> group;
    private ArrayList<List<String>> child;

    private void initExpandableListView() {


        group = new ArrayList<>();
        child = new ArrayList<>();
        //去掉默认箭头

        //添加数据
        addInfo("全部", new String[]{});
        addInfo("上衣", new String[]{"古风", "和风", "lolita", "日常"});
        addInfo("下装", new String[]{"日常", "泳衣", "汉风", "lolita", "创意T恤"});
        addInfo("外套", new String[]{"汉风", "古风", "lolita", "胖次", "南瓜裤", "日常"});


        //设置适配器
        expandableListViewAdapter = new ExpandableListViewAdapter(this, group, child);
        expandableListView.setAdapter(expandableListViewAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                expandableListViewAdapter.isChildSelectable(groupPosition, childPosition);
                expandableListViewAdapter.notifyDataSetChanged();

                tempType = child.get(groupPosition).get(childPosition);
                Toast.makeText(GoodsListActivity.this, "tempType="+tempType, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // 这里是控制如果列表没有孩子菜单不展开的效果
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent,
                                        View v, int groupPosition, long id) {
                if (child.get(groupPosition).isEmpty()) {// isEmpty没有
                    return true;
                } else {
                    return false;
                }
            }
        });


    }

    /**
     * 添加数据信息
     *
     * @param g
     * @param c
     */
    private void addInfo(String g, String[] c) {
        group.add(g);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < c.length; i++) {
            list.add(c[i]);
        }
        child.add(list);
    }


}
