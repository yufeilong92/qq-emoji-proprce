package com.lawyee.myface;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.widget.ActionMenuView;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2017/1/3 17:19
 * @Purpose :
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyEditText extends EditText implements ActionMenuView.OnMenuItemClickListener{

     private Context mContext;



    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
    }

    public MyEditText(Context context) {
        super(context);
        this.mContext=context;
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
    }

    /**
     *
     * @param menuItem 每个控件的id
     * @return 是否点击
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        return onTextContextMenuItem(menuItem.getItemId());
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);
    }

    /**
     *
     * @param id 点击的字体
     * @return 是否选中的itemd的
     */
    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean consumed = super.onTextContextMenuItem(id);
        switch(id)
//                剪切
        { case android.R.id.cut :
            onTextCut();
              break;
//             粘贴
        case android.R.id.paste:
          onTextPaste();
           break;
//        拷贝
        case android.R.id.copy:
            onTextCopy();
           break;
          default:
              break;
        }
      return consumed;

    }

    /**
     * 拷贝
     */
    private void onTextCopy() {
        Toast.makeText(mContext,"Copy",Toast.LENGTH_SHORT).show();
    }

    /**
     * 粘贴
     */
    private void onTextPaste() {
         Toast.makeText(mContext,"Paste",Toast.LENGTH_SHORT).show();
    }

    /**
     * 剪切
     */
    private void onTextCut() {
        Toast.makeText(mContext,"Cut",Toast.LENGTH_SHORT).show();
    }
}
