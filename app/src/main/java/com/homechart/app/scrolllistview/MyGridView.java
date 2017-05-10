package com.homechart.app.scrolllistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 
 * ������һ��ӵ��Scrollbar�������Ƕ����һ��ӵ��Scrollbar���������Ϊ�ⲻ��ѧ������������¼�������ֻ��ʾһ���������ݡ���ô�ͻ�һ��˼·��
 * �������ӿؼ�������ȫ����ʾ���������������Ĺ�������������˸��ؼ��ķ�Χ����ʾ���ؼ���scrollbar������ʾ���ݣ�˼·��������һ���Ǵ��롣
 * ����ķ������Զ���GridView������̳���GridView������onMeasure������
 * 
 * 
 */
public class MyGridView extends GridView {

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGridView(Context context) {
		super(context);
	}

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/***
	 * 
	 * �ı�߶� ����onMeasure���������������ʾ�ĸ߶����ȣ�
	 * makeMeasureSpec�����е�һ�������������ֿռ�Ĵ�С���ڶ��������ǲ���ģʽ
	 * MeasureSpec.AT_MOST����˼�����ӿؼ���Ҫ���Ŀؼ�����չ�����Ŀռ�
	 * ֮����ScrollView�������������OK�ˣ�ͬ���ĵ���ListViewҲ���á�
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}