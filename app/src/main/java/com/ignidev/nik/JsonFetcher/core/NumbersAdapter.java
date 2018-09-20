package com.ignidev.nik.JsonFetcher.core;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ignidev.nik.JsonFetcher.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.NumberDataHolder> {

    private static final int SECTION = 0;
    private static final int NUMBER = 1;

    private final List<Number> numbersWithSections;
    private final Context context;

    public NumbersAdapter(Context context, List<Number> numbers) {
        this.context = context;
        numbersWithSections = new ArrayList<>();
        int i = -1;
        Collections.sort(numbers);
        for (Number n : numbers) {
            if (i != n.getSection()) {//add fake items denoting section headers
                i = n.getSection();
                numbersWithSections.add(new FakeNumber(i));
            }
            numbersWithSections.add(n);
        }
    }

    private boolean isFakeNumber(Number num) {
        return num.getValue() == -1;
    }

    private String getString(int resId, Object... args) {
        return context.getString(resId, args);
    }

    @Override
    public NumberDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == SECTION) {
            return new SectionHolder(inflater.inflate(R.layout.section_item, parent, false));
        }
        return new NumberHolder(inflater.inflate(R.layout.number_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NumberDataHolder holder, int position) {
        holder.setData(numbersWithSections.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (isFakeNumber(numbersWithSections.get(position))) {
            return SECTION;
        }
        return NUMBER;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return numbersWithSections.size();
    }


    /**
     * Holders.
     */

    abstract class NumberDataHolder extends RecyclerView.ViewHolder {

        NumberDataHolder(View itemView) {
            super(itemView);
        }

        public abstract void setData(Number number);
    }

    private class NumberHolder extends NumberDataHolder {

        private TextView textView;
        private ImageView checkMark;

        private NumberHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.value_field);
            checkMark = itemView.findViewById(R.id.check_mark);
        }

        @Override
        public void setData(Number number) {
            textView.setText(getString(R.string.item_format, number.getValue()+1));
            checkMark.setVisibility(number.isCheckMark() ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private class SectionHolder extends NumberDataHolder {

        private TextView sectionNameView;

        private SectionHolder(View itemView) {
            super(itemView);
            sectionNameView = (TextView)itemView;
        }

        @Override
        public void setData(Number number) {
            sectionNameView.setText(getString(R.string.section_format, number.getSection()+1));
        }
    }

    /**
     * Fake number, used to identify the section header in adapter.
     */

    private class FakeNumber extends Number {

        private FakeNumber(int section) {
            super(section, -1, false);
        }
    }
}
