package com.common.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

/**
 * Created by Alick on 2015/5/23.
 */
public abstract class BasicExpandableListAdapter<T> extends BaseExpandableListAdapter {
    public List<List<T>> data;
    private LayoutInflater inflater;
    private Context context;

    public BasicExpandableListAdapter(List<List<T>> data, Context context) {
        this.data = data;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public abstract View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

    @Override
    public abstract View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 获取LayoutInflater实例
     * @return
     */
    public LayoutInflater getLayoutInflater(){
        return inflater;
    }

    /**
     * 获取当前上下文
     * @return
     */
    public Context getContext() {
        return context;
    }

    public void updateList(List<List<T>> data){
        this.data=data;
        notifyDataSetChanged();
    }
}
