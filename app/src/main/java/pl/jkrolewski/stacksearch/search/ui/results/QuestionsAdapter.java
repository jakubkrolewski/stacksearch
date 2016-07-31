package pl.jkrolewski.stacksearch.search.ui.results;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lombok.NonNull;
import pl.jkrolewski.stacksearch.base.ui.BaseRecyclerViewAdapter;
import pl.jkrolewski.stacksearch.base.ui.EmptyOnItemClickListener;
import pl.jkrolewski.stacksearch.base.ui.OnItemClickListener;
import pl.jkrolewski.stacksearch.databinding.SearchQuestionItemBinding;
import pl.jkrolewski.stacksearch.search.model.Question;

class QuestionsAdapter extends BaseRecyclerViewAdapter<Question, QuestionsAdapter.QuestionViewHolder> {

    @NonNull
    private OnItemClickListener<Question> onItemClickListener = EmptyOnItemClickListener.instance();

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SearchQuestionItemBinding binding = SearchQuestionItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new QuestionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        holder.setQuestion(getItem(position));
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener<Question> onItemClickListener) {
        this.onItemClickListener = EmptyOnItemClickListener.emptyForNull(onItemClickListener);
    }

    class QuestionViewHolder extends ResultsView.ViewHolder implements View.OnClickListener {

        private final SearchQuestionItemBinding binding;

        public QuestionViewHolder(@NonNull SearchQuestionItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        void setQuestion(@NonNull Question question) {
            binding.setQuestion(question);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, binding.getQuestion());
        }
    }
}
