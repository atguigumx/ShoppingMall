package com.atguigu.shop.controller.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atguigu.shop.R;
import com.atguigu.shop.app.ShopApplication;
import com.atguigu.shop.model.cartbean.HotSearchBean;
import com.atguigu.shop.model.greendao.Note;
import com.atguigu.shop.model.greendao.NoteDao;
import com.atguigu.shop.model.NetManager;
import com.atguigu.shop.utils.DensityUtil;
import com.atguigu.shop.utils.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

// 搜索页面
public class SearchActivity extends Activity {
    /**
     * 搜索关键字
     */
    public static final String SEARCH_KEYWORD = "search_keyword";
    @BindView(R.id.tv_search)
    EditText tvSearch;
    @BindView(R.id.iv_search_voice)
    ImageView ivSearchVoice;
    @BindView(R.id.tv_search_go)
    TextView tvSearchGo;
    @BindView(R.id.lv_search)
    ListView lvSearch;
    @BindView(R.id.ll_hot_search)
    LinearLayout llHotSearch;
    @BindView(R.id.hsl_hot_search)
    HorizontalScrollView hslHotSearch;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.ll_history)
    LinearLayout llHistory;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        getDataFromNet();
    }

    /**
     * 从历史中得到数据
     */
    private void getDataFromHistory() {
        String textColumn = NoteDao.Properties.Text.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        cursor = getDb().query(getNoteDao().getTablename(), getNoteDao().getAllColumns(), null, null, null, null, orderBy);

        if(cursor.getCount()>0){
            llHistory.setVisibility(View.VISIBLE);
            String[] from = {textColumn};
            int[] to = {android.R.id.text1};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to);
            lvSearch.setAdapter(adapter);
        }else{
            llHistory.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDataFromHistory();
    }

    private SQLiteDatabase getDb() {
        // 通过 BaseApplication 类提供的 getDb() 获取具体 db
        return ((ShopApplication) this.getApplicationContext()).getDb();
    }

    private void voiceToText() {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        //2.设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//中文
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");//普通话
        //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
        //结果
        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener());
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    private void getDataFromNet() {
        //连接+关键字
        String url = NetManager.HOT_SEARCH;

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
                    }
                });
    }

    private void processData(String response) {
        HotSearchBean searchBean = JSON.parseObject(response, HotSearchBean.class);
        List<HotSearchBean.ResultBean> resultBeanList = searchBean.getResult();

        if (resultBeanList != null && resultBeanList.size() > 0) {
            //设置适配器

            for (int i = 0; i < resultBeanList.size(); i++) {
                //内容
                final String title = resultBeanList.get(i).getTitle();

                int margins = DensityUtil.dip2px(SearchActivity.this, 5);

                LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                tvParams.setMargins(margins, margins, margins, margins);

                TextView textView = new TextView(SearchActivity.this);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setPadding(margins, margins, margins, margins);
                textView.setBackgroundResource(R.drawable.hot_search_selector);
                textView.setClickable(true);
                textView.setText(title);   //设置点击，按下变色
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        addHistory(title);
                        Intent intent = new Intent(SearchActivity.this, GoodsListActivity.class);
                        intent.putExtra(SEARCH_KEYWORD, title);
                        startActivity(intent);
                    }
                });

                //添加到线性布局里面
                llHotSearch.addView(textView, tvParams);
            }
        }
    }

    private String getString() {
        //关键字
        String word = tvSearch.getText().toString().trim();

        return word;
    }


    @OnClick({R.id.iv_search_voice, R.id.tv_search_go,R.id.btn_clear})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_search_voice:
                //Toast.makeText(this, "语音输入", Toast.LENGTH_SHORT).show();
                voiceToText();
                break;

            case R.id.tv_search_go:
                Intent intent = new Intent(this, GoodsListActivity.class);
                String word = getString();
                //添加到搜索历史里面
                addHistory(word);
                intent.putExtra(SEARCH_KEYWORD, word);
                startActivity(intent);
                break;

            case R.id.btn_clear:
                deleteData();
                llHistory.setVisibility(View.GONE);
                break;
        }
    }

    private void deleteData() {
        getNoteDao().deleteAll();
        cursor.requery();
    }

    /**
     * 添加数据
     * @param word
     */
    private void addHistory(String word) {
        String noteText = word;
        tvSearch.setText("");

        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment =  df.format(new Date());

        if (noteText != null && !noteText.equals("")) {
            // 插入操作，简单到只要你创建一个 Java 对象
            Note note = new Note(null, noteText, comment, new Date());
            getNoteDao().insert(note);
        }
    }

    private NoteDao getNoteDao() {
        // 通过 ShopApplication 类提供的 getDaoSession() 获取具体 Dao
        return ((ShopApplication) this.getApplicationContext()).getDaoSession().getNoteDao();
    }

    class MyInitListener implements InitListener {

        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS) {
                Toast.makeText(SearchActivity.this, "初始化失败了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {

        /**
         * 返回的结果
         * @param recognizerResult
         * @param b   是否说话结束
         */
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String result = recognizerResult.getResultString();
            printResult(recognizerResult);
        }

        /**
         * 出错的回调
         * @param speechError
         */
        @Override
        public void onError(SpeechError speechError) {

        }
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;

        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        String reuslt = resultBuffer.toString().replace("。", "");

        tvSearch.setText(reuslt);
        tvSearch.setSelection(tvSearch.length());
    }
}
