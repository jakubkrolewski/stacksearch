package pl.jkrolewski.stacksearch.search.ui.results;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.NonNull;
import pl.jkrolewski.stacksearch.R;
import pl.jkrolewski.stacksearch.base.ui.OnItemClickListener;
import pl.jkrolewski.stacksearch.base.ui.VerticalSpaceItemDecoration;
import pl.jkrolewski.stacksearch.search.model.Question;

import static pl.jkrolewski.stacksearch.base.util.UnitConverter.convertDpToPixel;

public class ResultsView extends SwipeRefreshLayout {

    private static final int VERTICAL_SPACE_DP = 8;
    private QuestionsAdapter adapter;

    @BindView(R.id.results_list)
    RecyclerView resultsList;

    public ResultsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.results_view_content, this);

        if (isInEditMode()) {
            return;
        }

        ButterKnife.bind(this);

        setUpResultsList();
    }

    private void setUpResultsList() {
        resultsList.setLayoutManager(new LinearLayoutManager(getContext()));
        resultsList.addItemDecoration(new VerticalSpaceItemDecoration(convertDpToPixel(getContext(), VERTICAL_SPACE_DP)));

        adapter = new QuestionsAdapter();
        resultsList.setAdapter(adapter);
    }

    public void setQuestions(@NonNull List<Question> questions) {
        adapter.setItems(questions);
        notifyRefreshFinished();
    }

    public void notifyRefreshError() {
        Toast.makeText(getContext(), R.string.search_error, Toast.LENGTH_SHORT).show();
        notifyRefreshFinished();
    }

    private void notifyRefreshFinished() {
        setRefreshing(false);
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener<Question> onItemClickListener) {
        adapter.setOnItemClickListener(onItemClickListener);
    }
}
