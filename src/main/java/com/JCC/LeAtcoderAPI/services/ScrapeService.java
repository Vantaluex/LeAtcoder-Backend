package com.JCC.LeAtcoderAPI.services;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;


@Service
public class ScrapeService {
    public static org.bson.Document ScrapeUser (String userid){
        String url = "https://atcoder.jp/users/" + userid;
        try {
            Document userpage = Jsoup.connect(url).get();
            String region = extractRegion(userpage);
            String birthyear = extractBirthYear(userpage);
            String affiliation = extractAffiliation(userpage);
            String rank = extractRank(userpage);
            String rating = extractRating(userpage);
            String percentile = getPercentile.fetch(Integer.parseInt(rank.replaceAll("(st|nd|rd|th)$", ""))) + "%";

            org.bson.Document userdata = new org.bson.Document("username", userid)
                    .append("Region", region)
                    .append("BirthYear", birthyear)
                    .append("Affiliation", affiliation)
                    .append("Rank", rank)
                    .append("Rating", rating)
                    .append("Percentile", percentile);
            return userdata;
        } catch (org.jsoup.HttpStatusException e) {
            if (e.getStatusCode() == 429 || e.getStatusCode() == 403) {
                System.out.println("Rate limited (429). Waiting 5 seconds before retry ");
                UtilityService.sleepInSeconds(5);
            }
        } catch (java.io.IOException e) { // Add this catch block
            System.out.println("IO error: " + e.getMessage());
            UtilityService.sleepInSeconds(5);
        }
        return null;
    }
    private static String extractRegion(Document doc) {
        for (Element row : doc.select("table.dl-table tr")) {
            Element th = row.selectFirst("th");
            if (th != null && th.text().equalsIgnoreCase("Country/Region")) {
                Element td = row.selectFirst("td");
                if (td != null) {
                    td.select("img").remove();
                    return td.text().trim();
                }
            }
        }
        return "Region not found";
    }

    private static String extractBirthYear(Document doc) {
        for (Element row : doc.select("table.dl-table tr")) {
            Element th = row.selectFirst("th");
            if (th != null && th.text().equalsIgnoreCase("Birth Year")) {
                Element td = row.selectFirst("td");
                return td.text();
            }
        }
        return "Region not found";
    }

    private static String extractAffiliation(Document doc) {
        for (Element row : doc.select("table.dl-table tr")) {
            Element th = row.selectFirst("th");
            if (th != null && th.text().equalsIgnoreCase("Affiliation")) {
                Element td = row.selectFirst("td");
                return td.text();
            }
        }
        return "Region not found";
    }

    private static String extractRank(Document doc) {
        for (Element row : doc.select("table.dl-table.mt-2 tr")) {
            Element th = row.selectFirst("th");
            if (th != null && th.text().equalsIgnoreCase("Rank")) {
                Element td = row.selectFirst("td");
                return td.text();
            }
        }
        return "Rank not found";
    }

    private static String extractRating(Document doc) {
        for (Element row : doc.select("table.dl-table.mt-2 tr")) {
            Element th = row.selectFirst("th");
            if (th != null && th.text().equalsIgnoreCase("Rating")) {
                Element td = row.selectFirst("td");
                if (td != null) {
                    td.select("img").remove();
                    return td.text().trim();
                }
            }
        }
        return "Rating not found";
    }
}