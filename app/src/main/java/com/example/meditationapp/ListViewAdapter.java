package com.example.meditationapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] adi;
    private String[] link;
    private LayoutInflater inflater;

    public ListViewAdapter(Context m_contex, String[] m_adi, String[]  m_link){
        this.context = m_contex;
        this.adi = m_adi;
        this.link = m_link;

    }

    @Override
    public int getCount() { //kac eleman olusucak
        return adi.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        TextView adi_textview;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.menusatir,parent,false);
        adi_textview = (TextView) itemView.findViewById(R.id.menuAdiText);

        adi_textview.setText(adi[position]);

        return itemView;
    }
}
