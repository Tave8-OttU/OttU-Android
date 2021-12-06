package com.tave8.ottu.data;

public enum Genre {
    DRAMA(1, "드라마"),
    MELO(2, "멜로/로맨스"),
    CRIME(3, "범죄"),
    THRILLER(4, "스릴러"),
    HISTORY(5, "사극"),
    ADULT(6, "성인/애로"),
    ANIMATION(7, "애니메이션"),
    ACTION(8, "액션"),
    COMEDY(9, "코미디"),
    FANTASY(10, "판타지"),
    SF(11, "SF"),
    ETC(12, "기타");

    private int genreId;
    private String genreName;

    Genre(int genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }

    public int getGenreId() {
        return genreId;
    }

    public String getGenreName() {
        return genreName;
    }
}
