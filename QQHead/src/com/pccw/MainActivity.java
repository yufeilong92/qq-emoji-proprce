package com.pccw;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private EditText edit;
	private ListView show;
	private TextView title;
	private Button send;
	private Button close;
	private SimpleChatAdapter adapter;
	private Button queryPerson;
	private ImageView expression;
	private int[] imageIds = new int[107];
	private Dialog builder; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_layout_main_singlechat);
		adapter = new SimpleChatAdapter(this);

		edit = (EditText)findViewById(R.id.team_singlechat_id_edit);
		show = (ListView)findViewById(R.id.team_singlechat_id_showlist);
		title = (TextView)findViewById(R.id.team_singlechat_id_title);
		close = (Button)findViewById(R.id.team_singlechat_id_close);
		send = (Button)findViewById(R.id.team_singlechat_id_send);
		queryPerson = (Button)findViewById(R.id.team_singlechat_id_information);
		expression = (ImageView)findViewById(R.id.team_singlechat_id_expression);
		show.setAdapter(adapter);
		
		send.setOnClickListener(new MyClickListener());
		close.setOnClickListener(new MyClickListener());
		queryPerson.setOnClickListener(new MyClickListener());
		expression.setOnClickListener(new MyClickListener());
		
		title.setText("duancanmeng");
	}
	
	class MyClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.team_singlechat_id_send:
				send();
				break;
			case R.id.team_singlechat_id_close:
				close();
				break;
			case R.id.team_singlechat_id_information:
				Intent it = new Intent(MainActivity.this,UserInfoActivity.class);
				it.putExtra("isMyself", false);
				MainActivity.this.startActivity(it);
				break;
			case R.id.team_singlechat_id_expression:
				createExpressionDialog();
				break;
			default:
				break;
			}
		}

	}
	
	/**
	 * 创建一个表情选择对话框
	 */
	private void createExpressionDialog() {
		builder = new Dialog(MainActivity.this);
		GridView gridView = createGridView();
		builder.setContentView(gridView);
		builder.setTitle("默认表情");
		builder.show();
		gridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(), imageIds[arg2 % imageIds.length]);
				ImageSpan imageSpan = new ImageSpan(MainActivity.this, bitmap);
				String str = null;
				if(arg2<10){
					str = "f00"+arg2;
				}else if(arg2<100){
					str = "f0"+arg2;
				}else{
					str = "f"+arg2;
				}
				SpannableString spannableString = new SpannableString(str);
				spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				edit.append(spannableString);
				builder.dismiss();
			}
		});
	}
	
	/**
	 * 生成一个表情对话框中的gridview
	 * @return
	 */
	private GridView createGridView() {
		final GridView view = new GridView(this);
		List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
		//生成107个表情的id，封装
		for(int i = 0; i < 107; i++){
			try {
				if(i<10){
					Field field = R.drawable.class.getDeclaredField("f00" + i);
					int resourceId = Integer.parseInt(field.get(null).toString());
					imageIds[i] = resourceId;
				}else if(i<100){
					Field field = R.drawable.class.getDeclaredField("f0" + i);
					int resourceId = Integer.parseInt(field.get(null).toString());
					imageIds[i] = resourceId;
				}else{
					Field field = R.drawable.class.getDeclaredField("f" + i);
					int resourceId = Integer.parseInt(field.get(null).toString());
					imageIds[i] = resourceId;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	        Map<String,Object> listItem = new HashMap<String,Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.team_layout_single_expression_cell, new String[]{"image"}, new int[]{R.id.image});
		view.setAdapter(simpleAdapter);
		view.setNumColumns(6);
		view.setBackgroundColor(Color.rgb(214, 211, 214));
		view.setHorizontalSpacing(1);
		view.setVerticalSpacing(1);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		view.setGravity(Gravity.CENTER);
		return view;
	}

	/**
	 * 关闭当前界面
	 */
	private void close() {
		MainActivity.this.finish();
	}
	
	/**
	 * 发送聊天信息
	 */
	private void send() {
		if(edit.getText().toString().length()!=0){
			adapter.addMessage(new MessageInfo(DicqConstant.DEFAULTMAC,edit.getText().toString(),DicqConstant.prortait));
			edit.setText(null);
		}else{
			Toast.makeText(MainActivity.this, "不能发送空消息", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
