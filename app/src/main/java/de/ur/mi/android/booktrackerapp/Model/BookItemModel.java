package de.ur.mi.android.booktrackerapp.Model;

public class BookItemModel {
    private int id;
    private String title;
    private String author;
    private String cover;
    private double rating;
    private int numPages;
    private String language;
    private String status;
    private int currPage;
    private String note;


    public BookItemModel(String title, String author, String cover,
                         double rating, int numPages, String language) {
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.rating = rating;
        this.numPages = numPages;
        this.language = language;
    }

    public BookItemModel(int id, String title, String author, String cover, double rating,
                         int numPages, String language, String status, int currPage, String note) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.rating = rating;
        this.numPages = numPages;
        this.language = language;
        this.status = status;
        this.currPage = currPage;
        this.note = note;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String authors) {
        this.author = authors;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
