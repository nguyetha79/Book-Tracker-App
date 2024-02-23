package de.ur.mi.android.booktrackerapp.Model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BookViewModel extends ViewModel {
    MutableLiveData<BookItemModel> mutableLiveData = new MutableLiveData<>();

    public void setBook(BookItemModel book) {
        mutableLiveData.setValue(book);
    }

    public MutableLiveData<BookItemModel> getBook(){
        return mutableLiveData;
    }
}
