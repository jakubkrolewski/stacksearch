package pl.jkrolewski.stacksearch.base.ui;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class BaseRecyclerViewAdapterTest {
    private ExampleBaseRecyclerViewAdapter adapter;

    @Before
    public void setup() {
        adapter = spy(new ExampleBaseRecyclerViewAdapter());
    }

    @Test
    public void shouldSetItemsAndNotifyChanged() {
        // given
        List<Object> newItems = Arrays.asList(1, 2, 3);

        // when
        adapter.setItems(newItems);

        // then
        assertThat(adapter.getItem(0)).isEqualTo(1);
        assertThat(adapter.getItem(1)).isEqualTo(2);
        assertThat(adapter.getItem(2)).isEqualTo(3);
        verify(adapter).notifyDataSetChanged();
    }

    @Test
    public void shouldReturnItemsCount() {
        // given
        List<Object> newItems = Arrays.asList(1, 2, 3);

        // when
        adapter.setItems(newItems);

        // then
        assertThat(adapter.getItemCount()).isEqualTo(3);
    }

    private static class ExampleBaseRecyclerViewAdapter extends BaseRecyclerViewAdapter<Object, RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }
    }
}