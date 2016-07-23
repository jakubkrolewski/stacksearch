package pl.jkrolewski.stacksearch.base.ui;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private List<T> items;

    protected BaseRecyclerViewAdapter() {
        this(Collections.emptyList());
    }

    protected BaseRecyclerViewAdapter(List<T> items) {
        this.items = new ArrayList<>(items);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public void setItems(List<T> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }
}