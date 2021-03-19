package pl.morytko.moviemax.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

public class Filmweb {
    private final Document doc;

    public Filmweb(String url) throws IOException {
        this.doc = Jsoup.connect(url).get();
    }

    public Optional<String> getDirector() {
        String text = doc.select("a[itemprop=director]")
                .select("span[itemprop=name]")
                .text();
        if (text.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(text);
        }
    }

    public Optional<String> getGenre() {
        String span = doc.select("div[itemprop=genre]")
                .select("span").text();
        if (span.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(span);
        }

    }

    public Optional<String> getCountryOfOrigin() {
        Element first = doc.select("div[itemprop=genre]").first();
        if (first == null){
            return Optional.empty();
        }else{
            return Optional.ofNullable(first
                    .nextElementSibling()
                    .nextElementSibling()
                    .select("a").text());
        }
    }

    public Optional<String> getTitle() {
        Element first = doc.select("h1[itemprop=name]").first();
        if (first == null){
            return Optional.empty();
        }else{
            return Optional.ofNullable(first
                    .select("span").text());
        }
    }

    public Optional<String> getDescription() {
        String text = doc.select("span[itemprop=description]").text();
        if (text.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(text);
        }
    }

    public Optional<Integer> getDuration() {
        return parseToMinuteInt(doc.select("span[itemprop=timeRequired]").text());
    }

    public Optional<String> getPosterUrl() {
        if (doc.select("img[id=filmPoster]").first() == null){
            return Optional.empty();
        }else{
            return Optional.ofNullable(doc.select("img[id=filmPoster]").first().attr("src"));
        }
    }

    private Optional<Integer> parseToMinuteInt(String duration) {
        if (duration == null || duration.isEmpty()){
            return Optional.empty();
        }else{
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
                return Optional.of(Integer.parseInt(hours) * 60 + Integer.parseInt(minutes));
            } else if (longerThanOneHour) {
                return Optional.of(Integer.parseInt(hours) * 60);
            } else {
                return Optional.of(Integer.parseInt(minutes));
            }
        }
    }
}
