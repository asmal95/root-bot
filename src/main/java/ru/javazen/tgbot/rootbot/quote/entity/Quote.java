package ru.javazen.tgbot.rootbot.quote.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class Quote {

    private Long id;

    private String text;

    private Set<SimpleTheme> themes;

    private SimpleAuthor author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<SimpleTheme> getThemes() {
        return themes;
    }

    public void setThemes(Set<SimpleTheme> themes) {
        this.themes = themes;
    }

    public SimpleAuthor getAuthor() {
        return author;
    }

    public void setAuthor(SimpleAuthor author) {
        this.author = author;
    }

    public static class SimpleTheme {

        private Long id;

        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "SimpleTheme{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public static class SimpleAuthor {

        private Long id;

        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "SimpleAuthor{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Quote{" +
                "text='" + text + '\'' +
                ", themes=" + themes +
                ", author=" + author +
                '}';
    }
}
