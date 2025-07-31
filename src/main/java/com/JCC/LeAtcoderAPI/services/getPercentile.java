package com.JCC.LeAtcoderAPI.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class getPercentile {
    public static String fetch (int userrank){
        String percentileurl = "https://atcoder.jp/ranking?desc=true&orderBy=rank";
        try {
            Document percentilepage = Jsoup.connect(percentileurl).get();
            int usertotal = extractTotaluser(percentilepage);
            double percentile = 100 * ((double) userrank) / ((double) usertotal);

            return String.format("%.2f", percentile) + "%";
        } catch (org.jsoup.HttpStatusException e) {
            if (e.getStatusCode() == 429 || e.getStatusCode() == 403) {
                System.out.println("Rate limited (429). Waiting 5 seconds before retry ");
                Sleeper.timeout(5000);
            }
        } catch (java.io.IOException e) {
            System.out.println("IO error: " + e.getMessage());
            Sleeper.timeout(5000);
        }
        return "Percentile getpage Error!";
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
}
