package com.thrifleganger.alexa.scene.model.eventful.enumeration;

import lombok.Getter;

@Getter
public enum Category {

    MUSIC("music", "Concerts and Tour Dates", "music,gigs,shows"),
    CONFERENCE("conference", "Conferences and Tradeshows", "conferences,tradeshows"),
    COMEDY("comedy", "Comedy", "comedy,skits"),
    LEARNING("learning_education","Education", "learning,education"),
    FAMILY("family_fun", "Kids and Family", "family,kids,fun"),
    FESTIVALS("festivals_parades", "Festivals", ""),
    FILM("movies_film", "Film", "films,movies,theater,cinema"),
    FOOD("food", "Food and Wine", "food,eat,drink,wine");


    Category(String keyword, String description, String tags) {
    }


}
