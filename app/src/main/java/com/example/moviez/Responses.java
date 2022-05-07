package com.example.moviez;

import java.util.ArrayList;
import java.util.List;

public class Responses {

    class CastResult {
        public boolean adult;
        public byte gender; //1 for female, 2 for male.
        public int id;
        public String known_for_department;
        public String name;
        public String original_name;
        public double popularity;
        public String profile_path;
        public String character;
        public String credit_id;
    }

    class CrewResult {
        public String name;
        public int id;
        public String job;
        public String original_name;
        public String profile_path;
    }
    class Videos {
        public List<Models.Video> results = new ArrayList<>();
    }

    class RecommendationResponse {

    }

    class BillboardResponse {
        public Dates dates;
        public int page;
        public List<Models.Film> results;
        public int total_pages;
        public int total_results;
    }

    public class Dates {
        public String maximum;
        public String minimum;
    }

    class CountryResponse {

        public List<Country> getCountries() {
            return countries;
        }

        public void setCountries(List<Country> countries) {
            this.countries = countries;
        }

        List<Country> countries;

    }

    class Country {
        private String iso_3166_1;
        private String english_name;

        public Country(String iso_3166_1, String english_name) {
            this.iso_3166_1 = iso_3166_1;
            this.english_name = english_name;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public String getEnglish_name() {
            return english_name;
        }

        public String toString() {
            return english_name;
        }
    }

    class SearchResponse {
        public String page;
        public List<Models.Film> results = new ArrayList<>();
        public String total_pages;
        public String total_results;
    }

    class FullCastResponse {
        public String id;
        public List<CastResult> cast = new ArrayList<>();
        public List<CrewResult> crew = new ArrayList<>();
    }

}
