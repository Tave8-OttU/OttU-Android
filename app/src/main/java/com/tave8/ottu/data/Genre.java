package com.tave8.ottu.data;

public class Genre {
    private int genreIdx;
    private String genreName = "";

    public Genre(int genreIdx) {
        this.genreIdx = genreIdx;
    }
    public Genre(int genreIdx, String genreName) {
        this.genreIdx = genreIdx;
        this.genreName = genreName;
    }

    public int getGenreIdx() {
        return genreIdx;
    }

    public String getGenreName() {
        return genreName;
    }
}
