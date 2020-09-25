package com.cherlshall.butterfly.util.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by htf on 2020/9/24.
 */
@Data
public class Change<T> {
    private String k;
    private T s;
    private T t;

    public Change(String k, T s, T t) {
        this.k = k;
        this.s = s;
        this.t = t;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        List<Change<T>> changes = new ArrayList<>();

        public Builder<T> add(String k, T s, T t) {
            changes.add(new Change<>(k, s, t));
            return this;
        }

        public List<Change<T>> build() {
            return changes;
        }
    }
}
