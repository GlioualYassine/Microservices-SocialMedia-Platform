package org.example.demo.model;

import lombok.*;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder @ToString
public class Article {
    int code;
    String designation;
    double prix;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return code == article.code;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
