package com.thrifleganger.alexa.scene.model.eventful.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

    MUSIC("music", "Music and Concerts", "music,gigs,shows,concerts,performances"),
    FILM("movies_film", "Film and Movies", "films,movies,theater,cinema"),
    COMEDY("comedy", "Comedy and skits", "comedy,skits,standup,stand-up,stand up"),
    LEARNING("learning_education","Learning and Education", "learning,education"),
    FAMILY("family_fun", "Kids and Family", "family,kids,fun"),
    FOOD("food", "Food and Wine", "food,eat,drink,wine,lunch,breakfast,dinner"),
    FESTIVALS("festivals_parades", "Festivals", "festivals,parades"),
    ART("art", "Art galleries and exhibits", "art,galleries,gallery,exhibits"),
    BOOKS("books", "Literary and books", "books,bookclub,literary"),
    CLUBS("clubs_associations", "Clubs and meetups", "clubs,organizations,organisations,meetups,associations"),
    POLITICS("politics_activism", "Politics and activism", "politics,activism,rally,march"),
    CONFERENCE("conference", "Conferences and Tradeshows", "conferences,tradeshows"),
    SCIENCE("science", "Science", "science"),
    TECHNOLOGY("technology", "Technology", "technology,programming,computer");

    private final String keyword;
    private final String description;
    private final String tags;
}
