package com.RobX.MyHealth;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

// Adapter created for the expandable list view
public class MyExtendableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    public String[] groupString;
    public String[][] childString;

    public MyExtendableListViewAdapter(Context mContext)
    {
        context = mContext;
        groupString = new String[]{
                context.getString(R.string.FAQ_Q1),
                context.getString(R.string.FAQ_Q2),
                context.getString(R.string.FAQ_Q3),
                context.getString(R.string.FAQ_Q4),
                context.getString(R.string.FAQ_Q5),
                context.getString(R.string.FAQ_Q6),
                context.getString(R.string.FAQ_Q7),
                context.getString(R.string.FAQ_Q8)};
        childString = new String[][]{
                {context.getString(R.string.FAQ_A1)},
                {context.getString(R.string.FAQ_A2)},
                {context.getString(R.string.FAQ_A3)},
                {context.getString(R.string.FAQ_A4)},
                {context.getString(R.string.FAQ_A5)},
                {context.getString(R.string.FAQ_A6)},
                {context.getString(R.string.FAQ_A7)},
                {context.getString(R.string.FAQ_A8)}
        };
    }


    @Override
    public int getGroupCount() {
        return groupString.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childString[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupString[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childString[groupPosition][childPosition];
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.partent_item,parent,false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = convertView.findViewById(R.id.label_group_normal);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (GroupViewHolder)convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupString[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item,parent,false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = convertView.findViewById(R.id.expand_child);
            convertView.setTag(childViewHolder);

        }else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tvTitle.setText(childString[groupPosition][childPosition]);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView tvTitle;
    }

    static class ChildViewHolder {
        TextView tvTitle;
    }
}