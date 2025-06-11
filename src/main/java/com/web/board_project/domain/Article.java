package com.web.board_project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(name = "Article", indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter @Column(nullable = false) private String title;
    @Setter @Column(nullable = false, length = 10000) private String content;

    @Setter private String hashtag; // @Column(nullable = false) true가 기본 값

    @ToString.Exclude
    @OrderBy("id")
    // articleComment와 양방향 바인딩 > 실무에서는 양방향 바인딩을 잘 쓰지 않음
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // 자식 Entity에도 해당 엔티티의 작업을 전파하겠다는 의미 All
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();;

    // @NoArgsConstructor(access = AccessLevel.PROTECTED) 와 같음
    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id == article.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
