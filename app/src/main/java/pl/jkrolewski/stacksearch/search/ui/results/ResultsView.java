package pl.jkrolewski.stacksearch.search.ui.results;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

import lombok.NonNull;
import pl.jkrolewski.stacksearch.base.ui.VerticalSpaceItemDecoration;
import pl.jkrolewski.stacksearch.base.util.UnitConverter;
import pl.jkrolewski.stacksearch.search.model.Question;

public class ResultsView extends RecyclerView {

    private static final int VERTICAL_SPACE_DP = 8;
    private QuestionsAdapter adapter;

    public ResultsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ResultsView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext()));
        addItemDecoration(new VerticalSpaceItemDecoration(UnitConverter.convertDpToPixel(getContext(), VERTICAL_SPACE_DP)));

        adapter = new QuestionsAdapter();
        setAdapter(adapter);
    }

    public void setQuestions(@NonNull List<Question> questions) {
        adapter.setItems(questions);
    }
}
