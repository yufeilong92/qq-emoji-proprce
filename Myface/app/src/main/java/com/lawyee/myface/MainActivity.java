package com.lawyee.myface;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageFace;
    private MyEditText mInputSms;
    private Button mSendSms;
    private ViewPager mFaceViewpager;
    private LinearLayout mFaceDotsContainer;
    private LinearLayout mBottom;
    private LinearLayout mActivityMain;
    private LinearLayout mChat_face_container;
    private ArrayList<String> mStaticFacesList;
    // 7列3行
    private int columns = 6;
    private int rows = 4;
    //表情页
    private List<View> views = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStaticFaces();
        initView();
    }

    /**
     * 初始化表情staticFacesList
     */
    private void initStaticFaces() {
        mStaticFacesList = new ArrayList<>();
        try {
            String[] mFaces = getAssets().list("face/png");
            for (int i = 0; i < mFaces.length; i++) {
                mStaticFacesList.add(mFaces[i]);
            }
            mStaticFacesList.remove("emotion_del_normal.png");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initView() {

        //表情图标
        mImageFace = (ImageView) findViewById(R.id.image_face);
        //表情布局
        mChat_face_container = (LinearLayout) findViewById(R.id.chat_face_container);
        mInputSms = (MyEditText) findViewById(R.id.input_sms);
        mSendSms = (Button) findViewById(R.id.send_sms);
        mFaceViewpager = (ViewPager) findViewById(R.id.face_viewpager);
        mFaceViewpager.setOnPageChangeListener((ViewPager.OnPageChangeListener) new PageChange());
        //表情下满小园点
        mFaceDotsContainer = (LinearLayout) findViewById(R.id.face_dots_container);
        mBottom = (LinearLayout) findViewById(R.id.bottom);
        mActivityMain = (LinearLayout) findViewById(R.id.activity_main);

        InitViewPager();
        mSendSms.setOnClickListener(this);
        mInputSms.setOnClickListener(this);
        mImageFace.setOnClickListener(this);
    }

    /**
     * 初始化表情
     */
    private void InitViewPager() {
//    获取表情页数

        for (int i = 0; i < getPagerCount(); i++) {
            views.add(viewPagerItem(i));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(16, 16);
             mFaceDotsContainer.addView(dotsItem(i),params);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
        mFaceViewpager.setAdapter(mVpAdapter);
        mFaceDotsContainer.getChildAt(0).setSelected(true);


    }

    private ImageView dotsItem(int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dot_image, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }

    //添加布局
    private View viewPagerItem(int position) {
       LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.face_gridview, null);//表情布局
        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
        /**
         * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
         * */
        ArrayList<String> subList = new ArrayList<String>();
        subList.addAll(mStaticFacesList
                .subList(position * (columns * rows - 1),
                (columns * rows - 1) * (position + 1) > mStaticFacesList.size() ?
                        mStaticFacesList.size() : (columns * rows - 1) * (position + 1)));

        /**
         * 末尾添加删除图标
         */
        subList.add("emotion_del_normal.png");

        FaceVAdapter faceVAdapter = new FaceVAdapter(this, subList);
        gridview.setAdapter(faceVAdapter);
        gridview.setNumColumns(columns);
        //点击表情执行操作
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    String png = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
                    if (!png.contains("emotion_del_normal")) {//如果不是删除图标
                        insert(getFace(png));
                    } else {
                        delete();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return gridview;
    }

    /**
     * 删除图标执行事件
     * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
     * */
    private void delete() {
        if (mInputSms.getText().length() != 0) {
            int iCursorEnd = Selection.getSelectionEnd(mInputSms.getText());
            int iCursorStart = Selection.getSelectionStart(mInputSms.getText());
            if (iCursorEnd > 0) {
                if (iCursorEnd == iCursorStart) {
                    if (isDeletePng(iCursorEnd)) {
                        String st = "#[face/png/f_static_000.png]#";
                        ((Editable) mInputSms.getText()).delete(
                                iCursorEnd - st.length(), iCursorEnd);
                    } else {
                        ((Editable) mInputSms.getText()).delete(iCursorEnd - 1,
                                iCursorEnd);
                    }
                } else {
                    ((Editable) mInputSms.getText()).delete(iCursorStart,
                            iCursorEnd);
                }
            }
        }




    }
    /**
     * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
     * **/
    private boolean isDeletePng(int cursor) {
        String st = "#[face/png/f_static_000.png]#";
        String content = mInputSms.getText().toString().substring(0, cursor);
        if (content.length() >= st.length()) {
            String checkStr = content.substring(content.length() - st.length(),
                    content.length());
            String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;

    }

    private SpannableStringBuilder getFace(String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {

            /**
             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
             * 所以这里对这个tempText值做特殊处理
             * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
             * */
            String tempText = "#[" + png + "]#";
            sb.append(tempText);
            sb.setSpan(new ImageSpan(MainActivity.this, BitmapFactory.decodeStream(
                    getAssets().open(png))), sb.length() - tempText.length(), sb.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    /**
     * 想输入框添加表情
     */
    private void insert(CharSequence text) {
        //开始的位置
        int iCursoriStart = Selection.getSelectionStart((mInputSms.getText()));
        //结束位置
        int iCursorEnd = Selection.getSelectionEnd((mInputSms.getText()));
        if (iCursorEnd != iCursoriStart) {
            ((Editable) mInputSms.getText()).replace(iCursoriStart, iCursorEnd, "");
        }
        int iCuros = Selection.getSelectionEnd((mInputSms.getText()));
        ((Editable) mInputSms.getText()).insert(iCuros, text);
    }

    /**
     * 根据表情数量以及GridView设置的行数和列数计算Pager数量
     *
     * @return
     */

    private int getPagerCount() {
        int size = mStaticFacesList.size();
        return size % (columns * rows - 1) == 0 ? size / (columns * rows - 1) :
                size / (columns * rows - 1) + 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //表情按钮

            case R.id.image_face:
                hideSoftInputView();//隐藏键盘
                if (mChat_face_container.getVisibility() == View.GONE) {
                    mChat_face_container.setVisibility(View.VISIBLE);
                } else if (mChat_face_container.getVisibility() == View.VISIBLE) {
                    mChat_face_container.setVisibility(View.GONE);
                }
                break;
            case R.id.input_sms:
                if (mChat_face_container.getVisibility() == View.VISIBLE) {
                    mChat_face_container.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * .隐藏键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 表情页改变时，dots效果也要跟着
     */
    class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mFaceDotsContainer.getChildCount(); i++) {
                mFaceDotsContainer.getChildAt(i).setSelected(false);
            }
            mFaceDotsContainer.getChildAt(position).setSelected(true);
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
