package de.ur.mi.android.booktrackerapp.Adapters;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.ur.mi.android.booktrackerapp.Fragments.Home.ShowAllBooksFragment;
import de.ur.mi.android.booktrackerapp.Fragments.Home.ShowDetailFragment;
import de.ur.mi.android.booktrackerapp.Model.BookItemModel;
import de.ur.mi.android.booktrackerapp.R;

public class ShowAllBooksAdapter extends RecyclerView.Adapter<ShowAllBooksAdapter.MyViewHolder> implements Filterable {

    ArrayList<BookItemModel> bookItemsList;
    ArrayList<BookItemModel> bookItemsListFull;
    ShowAllBooksFragment showAllBooksFragment;

    public ShowAllBooksAdapter(ShowAllBooksFragment showAllBooksFragment, ArrayList<BookItemModel> bookItemsList) {
        this.showAllBooksFragment = showAllBooksFragment;
        this.bookItemsList = bookItemsList;
        bookItemsListFull = new ArrayList<>(bookItemsList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(showAllBooksFragment.getContext());
        View view = inflater.inflate(R.layout.book_item_model_show, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        BookItemModel currBookItemModel = bookItemsList.get(position);

        String title = currBookItemModel.getTitle();
        String author = currBookItemModel.getAuthor();
        String coverLink = currBookItemModel.getCover();

        holder.tvBookTitle.setText(title);
        holder.tvBookAuthors.setText(author);

        if (coverLink.equals("No cover")){
            String uri = "@drawable/no_book_cover_available";
            int imageResource = showAllBooksFragment.getContext().getResources()
                    .getIdentifier(uri, null, showAllBooksFragment.getContext().getPackageName());
            Drawable res = showAllBooksFragment.getContext().getResources().getDrawable(imageResource);
            holder.ivBookCover.setImageDrawable(res);
        } else {
            Picasso.get().load(coverLink).into(holder.ivBookCover);
        }

        holder.btnDetail.setOnClickListener(view -> {

            int progress = 0;
            if (currBookItemModel.getNumPages() != 0) {
                if (currBookItemModel.getCurrPage() <= currBookItemModel.getNumPages()) {
                    progress = (currBookItemModel.getCurrPage() * 100) /currBookItemModel.getNumPages();
                } else {
                    Toast.makeText(showAllBooksFragment.getContext(), "Invalid Number", Toast.LENGTH_SHORT).show();
                }
            }

            Bundle bundle = new Bundle();
            bundle.putInt("id", currBookItemModel.getId());
            bundle.putString("title", currBookItemModel.getTitle());
            bundle.putString("author", currBookItemModel.getAuthor());
            bundle.putString("cover", currBookItemModel.getCover());
            bundle.putDouble("rating", currBookItemModel.getRating());
            bundle.putInt("numPages", currBookItemModel.getNumPages());
            bundle.putString("language", currBookItemModel.getLanguage());
            bundle.putString("status", currBookItemModel.getStatus());
            bundle.putInt("currPage", currBookItemModel.getCurrPage());
            bundle.putString("note", currBookItemModel.getNote());
            bundle.putInt("progress", progress);

            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            ShowDetailFragment showDetailFragment = new ShowDetailFragment();
            showDetailFragment.setArguments(bundle);

            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, showDetailFragment)
                    .commit();

        });
    }

    @Override
    public int getItemCount() {
        return bookItemsList.size();
    }

    @Override
    public Filter getFilter() {
        return bookItemFilter;
    }

    private Filter bookItemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BookItemModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(bookItemsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (BookItemModel bookItem : bookItemsListFull) {
                    if (bookItem.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(bookItem);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            bookItemsList.clear();
            bookItemsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvBookTitle, tvBookAuthors;
        private ImageView ivBookCover;
        private Button btnDetail;
        private CheckBox btnCheckBox;
        private View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBookTitle = itemView.findViewById(R.id.tv_title_book_item_show);
            tvBookAuthors = itemView.findViewById(R.id.tv_author_book_item_show);
            ivBookCover = itemView.findViewById(R.id.iv_cover_book_item_show);

            btnCheckBox = itemView.findViewById(R.id.checkBox);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            view = itemView;

            view.setOnLongClickListener(showAllBooksFragment);
            btnCheckBox.setOnClickListener(view -> showAllBooksFragment.setUpSelectionList(view, getAdapterPosition()));
        }

    }
}
