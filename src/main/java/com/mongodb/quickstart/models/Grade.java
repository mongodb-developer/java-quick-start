package com.mongodb.quickstart.models;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

public class Grade {

    private ObjectId id;
    private Double student_id;
    private Double class_id;
    private List<Score> scores;

    public ObjectId getId() {
        return id;
    }

    public Grade setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public Double getStudent_id() {
        return student_id;
    }

    public Grade setStudent_id(Double student_id) {
        this.student_id = student_id;
        return this;
    }

    public Double getClass_id() {
        return class_id;
    }

    public Grade setClass_id(Double class_id) {
        this.class_id = class_id;
        return this;
    }

    public List<Score> getScores() {
        return scores;
    }

    public Grade setScores(List<Score> scores) {
        this.scores = scores;
        return this;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Grade{");
        sb.append("id=").append(id);
        sb.append(", student_id=").append(student_id);
        sb.append(", class_id=").append(class_id);
        sb.append(", scores=").append(scores);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Grade grade = (Grade) o;
        return Objects.equals(id, grade.id) && Objects.equals(student_id, grade.student_id) && Objects.equals(class_id,
                                                                                                              grade.class_id) && Objects
                .equals(scores, grade.scores);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, student_id, class_id, scores);
    }
}
