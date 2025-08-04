package com.JCC.LeAtcoderAPI.services;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;


@Service
public class ScrapeService {
    public static org.bson.Document ScrapeUser (String userid){
        String url = "https://atcoder.jp/users/" + userid;
        try {
            Document userpage = Jsoup.connect(url).get();
            String region = extractRegion(userpage);
            int birthyear = extractBirthYear(userpage);
            String affiliation = extractAffiliation(userpage);
            int rank = extractRank(userpage);
            int rating = extractRating(userpage);
            double percentile = getPercentile(rank);

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

    public static boolean checkValidUserName(String userid){
        String url = "https://atcoder.jp/users/" + userid;
        try {
            Document userpage = Jsoup.connect(url).get();
            String pageTitle = userpage.title();
            return true;
        } catch (org.jsoup.HttpStatusException e) {
            if (e.getStatusCode() == 404) {
                System.out.println("User not found");
                return false;
            }
            if (e.getStatusCode() == 429 || e.getStatusCode() == 403) {
                System.out.println("Rate limited");
            }
        } catch (java.io.IOException e) { // Add this catch block
            System.out.println("IO error: " + e.getMessage());
        }
        return false;
    }

    private static int extractBirthYear(Document doc) {
        for (Element row : doc.select("table.dl-table tr")) {
            Element th = row.selectFirst("th");
            if (th != null && th.text().equalsIgnoreCase("Birth Year")) {
                Element td = row.selectFirst("td");
                return Integer.parseInt(td.text().replaceAll("[^\\d]", ""));
            }
        }
        return 0;
    }

    private static String extractAffiliation(Document doc) {
        for (Element row : doc.select("table.dl-table tr")) {
            Element th = row.selectFirst("th");
            if (th != null && th.text().equalsIgnoreCase("Affiliation")) {
                Element td = row.selectFirst("td");
                return td.text();
            }
        }
        return "No affiliation";
    }

    private static int extractRank(Document doc) {
        for (Element row : doc.select("table.dl-table.mt-2 tr")) {
            Element th = row.selectFirst("th");
            if (th != null && th.text().equalsIgnoreCase("Rank")) {
                Element td = row.selectFirst("td");
                return cleanToInt(td);
            }
        }
        return 0;
    }

    private static int extractRating(Document doc) {
        for (Element row : doc.select("table.dl-table.mt-2 tr")) {
            Element th = row.selectFirst("th");
            if (th != null && th.text().equalsIgnoreCase("Rating")) {
                Element td = row.selectFirst("td");
                if (td != null) {
                    td.select("img").remove();
                    return cleanToInt(td);
                }
            }
        }
        return 0;
    }

    private static double getPercentile (int userrank){
        String percentileurl = "https://atcoder.jp/ranking?desc=true&orderBy=rank";
        try {
            org.jsoup.nodes.Document percentilepage = Jsoup.connect(percentileurl).get();
            int usertotal = extractTotaluser(percentilepage);
            double percentile = 100 * ((double) userrank) / ((double) usertotal);

            return Math.round(percentile * 100.00) / 100.00;
        } catch (org.jsoup.HttpStatusException e) {
            if (e.getStatusCode() == 429 || e.getStatusCode() == 403) {
                System.out.println("Rate limited (429). Waiting 5 seconds before retry ");
                UtilityService.sleepInSeconds(5);
            }
        } catch (java.io.IOException e) {
            System.out.println("IO error: " + e.getMessage());
            UtilityService.sleepInSeconds(5);
        }
        return 0;
    }
    private static int extractTotaluser(Document doc) {
        for (Element row : doc.select("table.table.table-bordered.table-striped.th-center tr")) {
            Element td = row.selectFirst("td");
            if (td != null) {
                return Integer.parseInt(td.text().replaceAll("[^\\d]", ""));
            }
        }
        return -1;
    }

    private static int cleanToInt(Element input) {
        return Integer.parseInt((input.text().replaceAll("[^\\d]", "")).replaceAll("(st|nd|rd|th)$", ""));
    }
}