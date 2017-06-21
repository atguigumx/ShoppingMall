package com.atguigu.shop.controller.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.shop.R;
import com.atguigu.shop.base.BaseFragment;
import com.atguigu.shop.model.userbean.UserBean;
import com.atguigu.shop.utils.BitmapUtils;
import com.hankkin.gradationscroll.GradationScrollView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/20 10:22
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：用户Fragment
 */
public class UserFragment extends BaseFragment implements GradationScrollView.ScrollViewListener {
    private static final String TAG = UserFragment.class.getSimpleName();
    @BindView(R.id.ib_user_icon_avator)
    ImageButton ibUserIconAvator;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_all_order)
    TextView tvAllOrder;
    @BindView(R.id.tv_user_pay)
    TextView tvUserPay;
    @BindView(R.id.tv_user_receive)
    TextView tvUserReceive;
    @BindView(R.id.tv_user_finish)
    TextView tvUserFinish;
    @BindView(R.id.tv_user_drawback)
    TextView tvUserDrawback;
    @BindView(R.id.tv_user_location)
    TextView tvUserLocation;
    @BindView(R.id.tv_user_collect)
    TextView tvUserCollect;
    @BindView(R.id.tv_user_coupon)
    TextView tvUserCoupon;
    @BindView(R.id.tv_user_score)
    TextView tvUserScore;
    @BindView(R.id.tv_user_prize)
    TextView tvUserPrize;
    @BindView(R.id.tv_user_ticket)
    TextView tvUserTicket;
    @BindView(R.id.tv_user_invitation)
    TextView tvUserInvitation;
    @BindView(R.id.tv_user_callcenter)
    TextView tvUserCallcenter;
    @BindView(R.id.tv_user_feedback)
    TextView tvUserFeedback;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.scrollview)
    GradationScrollView scrollview;
    @BindView(R.id.tv_usercenter)
    TextView tvUsercenter;
    @BindView(R.id.ib_user_setting)
    ImageButton ibUserSetting;
    @BindView(R.id.ib_user_message)
    ImageButton ibUserMessage;
    /**
     * 滑动最大区域-相对布局的高
     */
    private  int height;
    //更换头像
    private static final int PICTURE = 100;
    private static final int CAMERA = 200;

    @Override
    public View initView() {
        Log.e(TAG, "用户Fragment初始化ui了");
        View view = View.inflate(mContext, R.layout.fragment_user, null);
        ButterKnife.bind(this, view);
        tvUsercenter.setBackgroundColor(Color.argb((int) 0, 255,0,0));
        tvUsercenter.setTextColor(Color.argb((int) 0, 255,255,255));
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e(TAG, "用户Fragment数据绑定了");

        //视图肯定初始化了
        initListeners();
    }

    private void readImage() {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = this.getActivity().getExternalFilesDir("");

        }else{//手机内部存储
            //路径：data/data/包名/files
            filesDir = this.getActivity().getFilesDir();
        }

        File file = new File(filesDir,"icon.png");
        if(file.exists()){
            //存储--->内存
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ibUserIconAvator.setImageBitmap(bitmap);
        }
    }

    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        //视图树观察者
        ViewTreeObserver vto = rlHeader.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //移除监听
                rlHeader.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);

                //得到相对布局的高
                height = rlHeader.getHeight();

                //设置ScrollView的滑动监听
                scrollview.setScrollViewListener(UserFragment.this);
            }
        });
    }


    @OnClick({R.id.tv_username, R.id.ib_user_setting, R.id.ib_user_message,R.id.ib_user_icon_avator})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ib_user_icon_avator:
                //查看本地是否用户登录过，如果没有，则不允许用户点击操作
                SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                String phone = sp.getString("phone", "");

                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(UserFragment.this.getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] items = new String[]{"图库","相机"};
                //提供一个AlertDialog
                new AlertDialog.Builder(mContext)
                        .setTitle("选择来源")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0 ://图库
                                        //启动其他应用的activity:使用隐式意图
                                        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        getActivity().startActivityForResult(picture, PICTURE);
                                        break;
                                    case 1://相机
                                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        getActivity().startActivityForResult(camera, CAMERA);
                                        break;
                                }
                            }
                        })
                        .setCancelable(false)
                        .show();
                break;

            case R.id.tv_username:
                Intent intent = new Intent(mContext,LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.ib_user_setting:
                Toast.makeText(mContext, "设置", Toast.LENGTH_SHORT).show();
                break;

            case R.id.ib_user_message:
                Toast.makeText(mContext, "消息", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {

        if (y <= 0) {   //设置标题的背景颜色
            tvUsercenter.setBackgroundColor(Color.argb((int) 0, 255,0,0));

        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变

            //滑动的距离：最大距离（相对布局高度） = 透明的改变 ： 最大透明度

            //透明的改变 =  (滑动的距离/最大距离)*255

            //(滑动的距离/最大距离)
            float scale = (float) y / height;
            //透明度
            float alpha = ( scale*255);
            tvUsercenter.setTextColor(Color.argb((int) alpha, 255,255,255));
            tvUsercenter.setBackgroundColor(Color.argb((int) alpha, 255,0,0));
        } else {    //滑动到banner下面设置普通颜色
            //y>height
            //透明度：0~255
            tvUsercenter.setBackgroundColor(Color.argb((int) 255, 255,0,0));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //读取用户数据
        UserBean user = readUser();

        if(user != null){
            //显示用户名
            tvUsername.setText(user.getBody().getUser().getUsername());

            //设置头像
            readImage();
        }
    }

    //读取用户信息
    public UserBean readUser(){

        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String username = sp.getString("name","");
        if(TextUtils.isEmpty(username)){
            return null;
        }

        UserBean user = new UserBean();
        UserBean.BodyEntity entity = user.new BodyEntity();
        user.setBody(entity);
        user.getBody().setUser(entity.new UserEntity());
        user.getBody().getUser().setUsername((sp.getString("name","")));
        user.getBody().getUser().setImgurl(sp.getString("imageurl", ""));
        user.getBody().getUser().setPassword(sp.getString("phone", ""));

        return user;
    }

    public void showCameraImage(Intent data) {
        //获取intent中的图片对象
        Bundle extras = data.getExtras();
        Bitmap bitmap = (Bitmap) extras.get("data");
        //对获取到的bitmap进行压缩、圆形处理
        bitmap = BitmapUtils.zoom(bitmap,ibUserIconAvator.getWidth(),ibUserIconAvator.getHeight());
        bitmap = BitmapUtils.circleBitmap(bitmap);

        //加载显示
        ibUserIconAvator.setImageBitmap(bitmap);
        //上传到服务器（省略）

        //保存到本地
        saveImage(bitmap);
    }

    public void showPictureImage(Intent data) {
        //图库
        Uri selectedImage = data.getData();
        //android各个不同的系统版本,对于获取外部存储上的资源，返回的Uri对象都可能各不一样,
        // 所以要保证无论是哪个系统版本都能正确获取到图片资源的话就需要针对各种情况进行一个处理了
        //这里返回的uri情况就有点多了
        //在4.4.2之前返回的uri是:content://media/external/images/media/3951或者file://....
        // 在4.4.2返回的是content://com.android.providers.media.documents/document/image

        String pathResult = getPath(selectedImage);
        //存储--->内存
        Bitmap decodeFile = BitmapFactory.decodeFile(pathResult);
        Bitmap zoomBitmap = BitmapUtils.zoom(decodeFile, ibUserIconAvator.getWidth(),ibUserIconAvator.getHeight());
        //bitmap圆形裁剪
        Bitmap circleImage = BitmapUtils.circleBitmap(zoomBitmap);

        //加载显示
        ibUserIconAvator.setImageBitmap(circleImage);
        //上传到服务器（省略）

        //保存到本地
        saveImage(circleImage);
    }

    //保存用户头像
    private void saveImage(Bitmap bitmap) {

        File filesDir;

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = this.getActivity().getExternalFilesDir("");

        }else{//手机内部存储
            //路径：data/data/包名/files
            filesDir = this.getActivity().getFilesDir();

        }

        FileOutputStream fos = null;
        try {
            File file = new File(filesDir,"icon.png");
            fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据系统相册选择的文件获取路径
     *
     * @param uri
     */
    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;

        //高于4.4.2的版本
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(mContext, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(mContext, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = ((Activity)mContext).managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(mContext, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    /**
     * uri路径查询字段
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
