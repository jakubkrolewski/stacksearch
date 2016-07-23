package pl.jkrolewski.stacksearch.search.ui.results;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import lombok.NonNull;
import pl.jkrolewski.stacksearch.base.ui.BaseRecyclerViewAdapter;
import pl.jkrolewski.stacksearch.databinding.SearchQuestionItemBinding;
import pl.jkrolewski.stacksearch.search.model.Question;

public class QuestionsAdapter extends BaseRecyclerViewAdapter<Question, QuestionsAdapter.QuestionViewHolder> {

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

    static class QuestionViewHolder extends ResultsView.ViewHolder {

        private final SearchQuestionItemBinding binding;

        public QuestionViewHolder(@NonNull SearchQuestionItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void setQuestion(@NonNull Question question) {
            binding.setQuestion(question);
            binding.executePendingBindings();
        }
    }
}
