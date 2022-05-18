package com.example.moviez;

import java.util.ArrayList;
import java.util.List;

public class Responses {

    public static class CastResult {
        public int id;
        public String name;
    }

    public static class CrewResult {
        public String name;
        public int id;
        public String job;
    }
    static class Videos {
    }

    public static class BillboardResponse {
        public Dates dates;
        public List<Models.Film> results;
    }

    public static class Dates {
        public String maximum;
        public String minimum;
    }

    public static class SearchResponse {
        public List<Models.Film> results = new ArrayList<>();
    }

    public static class FullCastResponse {
        public String id;
        public List<CastResult> cast = new ArrayList<>();
        public List<CrewResult> crew = new ArrayList<>();
    }

}
