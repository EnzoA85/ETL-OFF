package com.example.etl_off.etl;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Parses and cleans Open Food Facts rows.
 */
@Component
public class OpenFoodFactsCsvParser {

    private static final int MIN_EXPECTED_COLUMNS = 30;
    private static final int MAX_BUSINESS_NAME_LENGTH = 255;
    private static final Pattern PARENTHESIS_CONTENT = Pattern.compile("\\([^)]*\\)");
    private static final Pattern PERCENTAGE = Pattern.compile("\\b\\d+(?:[,.]\\d+)?\\s*%");
    private static final Pattern INGREDIENT_SEPARATOR = Pattern.compile("\\s*(?:,|;|/|:|\\.|\\s+-\\s+|\\bet\\b)\\s*");
    private static final Pattern DEFAULT_SEPARATOR = Pattern.compile("\\s*(?:,|;)\\s*");
    private static final Pattern PARASITE_CHARS = Pattern.compile("[*_•·\"'`´]+");
    private static final Pattern EXTRA_SPACES = Pattern.compile("\\s+");

    /**
     * Converts a CSV line into a cleaned product row.
     *
     * @param line physical CSV line
     * @return parsed row when mandatory fields are present
     */
    public Optional<ProductCsvRow> parseLine(String line) {
        String[] columns = line.split("\\|", -1);
        if (columns.length < MIN_EXPECTED_COLUMNS) {
            return Optional.empty();
        }

        String categorie = cleanRequired(columns[0], "Inconnue");
        String marque = cleanRequired(columns[1], "Sans marque");
        String nomProduit = cleanRequired(columns[2], "Produit sans nom");

        return Optional.of(new ProductCsvRow(
                categorie,
                marque,
                nomProduit,
                cleanNutritionScore(columns[3]),
                parseDecimal(columns[5]),
                parseDecimal(columns[6]),
                cleanList(columns[4], INGREDIENT_SEPARATOR),
                cleanList(columns[28], DEFAULT_SEPARATOR),
                cleanList(columns[29], DEFAULT_SEPARATOR)));
    }

    private Set<String> cleanList(String rawValue, Pattern separator) {
        if (rawValue == null) {
            return Set.of();
        }
        String withoutParenthesis = PARENTHESIS_CONTENT.matcher(rawValue).replaceAll(" ");
        return Arrays.stream(separator.split(withoutParenthesis))
                .map(this::cleanOptional)
                .filter(StringUtils::hasText)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
    }

    private String cleanRequired(String value, String fallback) {
        String cleaned = cleanOptional(value);
        return StringUtils.hasText(cleaned) ? cleaned : fallback;
    }

    private String cleanOptional(String value) {
        if (value == null) {
            return "";
        }
        String cleaned = PERCENTAGE.matcher(value).replaceAll(" ");
        cleaned = PARASITE_CHARS.matcher(cleaned).replaceAll(" ");
        cleaned = cleaned.replaceAll("^[\\p{Punct}\\s]+|[\\p{Punct}\\s]+$", " ");
        cleaned = EXTRA_SPACES.matcher(cleaned).replaceAll(" ").trim();
        if (cleaned.length() > MAX_BUSINESS_NAME_LENGTH) {
            return "";
        }
        return cleaned;
    }

    private String cleanNutritionScore(String value) {
        String cleaned = stripAccents(cleanOptional(value)).toUpperCase(Locale.ROOT);
        if (cleaned.length() != 1 || cleaned.charAt(0) < 'A' || cleaned.charAt(0) > 'F') {
            return null;
        }
        return cleaned;
    }

    private BigDecimal parseDecimal(String value) {
        String cleaned = cleanOptional(value).replace(',', '.');
        if (!StringUtils.hasText(cleaned)) {
            return null;
        }
        try {
            return new BigDecimal(cleaned);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String stripAccents(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }
}
