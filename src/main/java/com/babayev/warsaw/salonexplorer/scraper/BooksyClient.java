package com.babayev.warsaw.salonexplorer.scraper;

import com.babayev.warsaw.salonexplorer.entity.Salon;
import com.babayev.warsaw.salonexplorer.entity.Treatment;
import com.babayev.warsaw.salonexplorer.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BooksyClient {

    @Value("${salon.fetch.limit}")
    private int fetchLimit;

    private final SalonRepository salonRepository;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    public List<Salon> fetchAndParseSalons() {
        List<Salon> salons = new ArrayList<>();
        int page = 1;
        boolean hasMoreResults = true;

        while (salons.size() < fetchLimit && hasMoreResults) {
            Document doc = fetchPage(page);
            if (doc == null) break;

            Element container = doc.getElementById("search-results");
            if (container == null) break;

            for (Element card : container.select("ul > li")) {
                if (salons.size() >= fetchLimit) break;

                Salon salon = processCard(card);
                if (salon != null) {
                    salons.add(salon);
                }
            }
            page++;
            sleep(2500);
        }
        return salons;
    }

    private Salon processCard(Element card) {
        Element linkElement = card.selectFirst("a[href]");
        if (linkElement == null) return null;

        String fullUrl = linkElement.attr("href").startsWith("http") ? linkElement.attr("href") : "https://booksy.com" + linkElement.attr("href");
        if (salonRepository.existsByWebsite(fullUrl)) return null;

        Element nameElement = card.selectFirst("h2, h3");
        if (nameElement == null) return null;

        Salon salon = new Salon();
        salon.setName(nameElement.text().trim());
        salon.setAddress(extractAddress(card));
        salon.setWebsite(fullUrl);
        salon.setPhone("Not listed publicly");

        scrapeAdditionalHtmlData(fullUrl, salon);
        return salonRepository.save(salon);
    }

    private String extractAddress(Element card) {
        Element mapAddress = card.selectFirst("[data-testid=map-location-business-address]");
        if (mapAddress != null && !mapAddress.text().isBlank()) return mapAddress.text().trim();

        Element distanceSpan = card.selectFirst("[data-testid=business-distance]");
        if (distanceSpan != null) return distanceSpan.parent().text().replaceAll("^[0-9.,]+\\s*km\\s*", "").trim();

        return "Warszawa, Polska";
    }

    private void scrapeAdditionalHtmlData(String url, Salon salon) {
        Document doc = fetchDocument(url);
        if (doc == null) return;

        parseStructuredData(doc, salon);
        parseTreatments(doc, salon);
    }

    private void parseStructuredData(Document doc, Salon salon) {
        Element jsonScript = doc.select("script[type=application/ld+json]").stream()
                .filter(e -> e.html().contains("aggregateRating")).findFirst().orElse(null);

        if (jsonScript == null) return;

        salon.setRating(extractDouble(jsonScript.html(), "\"ratingValue\":(\\d+\\.?\\d*)"));
        salon.setReviewsCount((int) extractDouble(jsonScript.html(), "\"reviewCount\":(\\d+)"));
    }

    private void parseTreatments(Document doc, Salon salon) {
        Elements serviceNames = doc.select("[data-testid=service-name]");
        List<Treatment> treatments = new ArrayList<>();
        List<Double> prices = new ArrayList<>();

        for (Element nameEl : serviceNames) {
            Treatment t = new Treatment();
            t.setName(nameEl.text().trim());

            double price = parsePrice(nameEl.closest("li"));
            if (price > 0) prices.add(price);

            t.setPrice(price);
            t.setSalon(salon);
            treatments.add(t);
        }
        salon.setTreatments(treatments);
        salon.setPriceRange(calculatePriceRange(prices));
    }


    private Document fetchPage(int page) {
        String url = (page == 1) ? "https://booksy.com/pl-pl/s/barber-shop/3_warszawa"
                : "https://booksy.com/pl-pl/s/barber-shop/3_warszawa?businessesPage=" + page;
        return fetchDocument(url);
    }

    private Document fetchDocument(String url) {
        try {
            return Jsoup.connect(url).header("User-Agent", USER_AGENT).timeout(10000).get();
        } catch (Exception e) { return null; }
    }

    private double extractDouble(String text, String regex) {
        Matcher m = Pattern.compile(regex).matcher(text);
        return m.find() ? Double.parseDouble(m.group(1)) : 0.0;
    }

    private double parsePrice(Element serviceItem) {
        if (serviceItem == null) return 0.0;
        Element priceEl = serviceItem.selectFirst("[data-testid=service-price]");
        if (priceEl == null) return 0.0;
        try {
            return Double.parseDouble(priceEl.text().replaceAll("[^0-9.,]", "").replace(",", "."));
        } catch (Exception e) { return 0.0; }
    }

    private String calculatePriceRange(List<Double> prices) {
        if (prices.isEmpty()) return "N/A";
        double min = prices.stream().min(Double::compare).get();
        double max = prices.stream().max(Double::compare).get();
        double avg = prices.stream().mapToDouble(d -> d).average().orElse(0.0);
        return String.format("%.2f zł - %.2f zł (Avg: %.2f zł)", min, max, avg);
    }

    private void sleep(long ms) { try { Thread.sleep(ms); } catch (InterruptedException ignored) {} }
}