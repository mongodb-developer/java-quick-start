package com.mongodb.quickstart.models;

import java.util.Objects;

public class Score {

    private String type;
    private Double score;

    public String getType() {
        return type;
    }

    public Score setType(String type) {
        this.type = type;
        return this;
    }

    public Double getScore() {
        return score;
    }

    public Score setScore(Double score) {
        this.score = score;
        return this;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Score{");
        sb.append("type='").append(type).append('\'');
        sb.append(", score=").append(score);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Score score1 = (Score) o;
        return Objects.equals(type, score1.type) && Objects.equals(score, score1.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, score);
    }
}
