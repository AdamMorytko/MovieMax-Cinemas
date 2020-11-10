package pl.morytko.moviemax.movies;

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
        return doc.select("div[itemprop=description]").text();
    }

    public int getDuration() {
        return parseToMinuteInt(doc.select("span[itemprop=timeRequired]").text());
    }

    public String getPosterUrl() {
        return doc.select("img[id=filmPoster]").first().attr("src");
    }

    private int parseToMinuteInt(String duration) {
        String hours = duration.replaceAll(" ", "")
                .split("godz")[0];
        String minutes = duration.split("godz\\.")[1].replaceAll(" ", "").split("min\\.")[0];
        return Integer.parseInt(hours) * 60 + Integer.parseInt(minutes);
    }
}
