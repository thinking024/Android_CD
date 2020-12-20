package com.yiyuanzhu.thinking.viewmodel;

public class GradeViewModel {
    private String id;
    private String term;
    private String name;
    private String score;
    private String credit;
    private String gradePoint;

    public GradeViewModel() {
    }

    public GradeViewModel(String id, String term, String name, String score, String credit, String gradePoint) {
        this.id = id;
        this.term = term;
        this.name = name;
        this.score = score;
        this.credit = credit;
        this.gradePoint = gradePoint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(String gradePoint) {
        this.gradePoint = gradePoint;
    }
}
