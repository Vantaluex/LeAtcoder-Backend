package com.JCC.LeAtcoderAPI.services;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import org.jsoup.nodes.Element;


public class scrapeUser {
    public static org.bson.Document fetch (String userid){
        String url = "https://atcoder.jp/users/" + userid;
        try {
            Document userpage = Jsoup.connect(url).get();
            String region = extractRegion(userpage);
            String birthyear = extractBirthYear(userpage);
            String affiliation = extractAffiliation(userpage);
            String rank = extractRank(userpage);
            String rating = extractRating(userpage);

            System.out.println("region: " + region);
            System.out.println("BirthYear: " + birthyear);
            System.out.println("Affiliation: " + affiliation);
            System.out.println("Rank: " + rank);
            System.out.println("Rating: " + rating);

            org.bson.Document userdata = new org.bson.Document("username", userid)
                    .append("Region", region)
                    .append("BirthYear", birthyear)
                    .append("Affiliation", affiliation)
                    .append("Rank", rank)
                    .append("Rating", rating);
            return userdata;
        } catch (org.jsoup.HttpStatusException e) {
            if (e.getStatusCode() == 429 || e.getStatusCode() == 403) {
                System.out.println("Rate limited (429). Waiting 5 seconds before retry ");
                Sleeper.timeout(5000);
            }
        } catch (java.io.IOException e) { // Add this catch block
            System.out.println("IO error: " + e.getMessage());
            Sleeper.timeout(5000);
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