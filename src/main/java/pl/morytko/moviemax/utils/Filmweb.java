package pl.morytko.moviemax.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Filmweb {
    private final Document doc;

    public Filmweb(String url) throws IOException {
        this.doc = Jsoup.connect(url).get();
    }

    public String getDirector() {
        return doc.select("a[itemprop=director]")
                .select("span[itemprop=name]")
                .text();
    }

    public String getGenre() {
        return doc.select("div[itemprop=genre]")
                .select("span").text();
    }

    public String getCountryOfOrigin() {
        return doc.select("div[itemprop=genre]")
                .first()
                .nextElementSibling()
                .nextElementSibling()
                .select("a").text();
    }

    public String getTitle() {
        return doc.select("h1[itemprop=name]")
                .first()
                .select("span").text();
    }

    public String getDescription() {
        String divDescription = doc.select("span[itemprop=description]").text();
        if (divDescription.isEmpty()){
            return "BŁĄD W TRAKCIE WCZYTYWANIA OPISU (UZUPEŁNIJ OPIS RĘCZNIE)";
        }else{
            return divDescription;
        }
    }

    public int getDuration() {
        return parseToMinuteInt(doc.select("span[itemprop=timeRequired]").text());
    }

    public String getPosterUrl() {
        return doc.select("img[id=filmPoster]").first().attr("src");
    }

    private int parseToMinuteInt(String duration) {
        String hours = "";
        boolean longerThanOneHour = false;
        if (duration.contains("godz")) {
            hours = duration.replaceAll(" ", "").split("godz")[0];
            longerThanOneHour = true;
        }
        String minutes = "";
        boolean hasMinutes = false;
        boolean minutesAlreadyDone = false;
        if (duration.contains("min") && longerThanOneHour) {
            minutes = duration.split("godz\\.")[1].replaceAll(" ", "").split("min\\.")[0];
            hasMinutes = true;
            minutesAlreadyDone = true;
        }

        if (duration.contains("min") && !minutesAlreadyDone) {
            minutes = duration.split("min\\.")[0].trim();
            hasMinutes = true;
        }


        if (longerThanOneHour && hasMinutes) {
            return Integer.parseInt(hours) * 60 + Integer.parseInt(minutes);
        } else if (longerThanOneHour) {
            return Integer.parseInt(hours) * 60;
        } else {
            return Integer.parseInt(minutes);
        }
    }
}
