/*
 * 2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.weather;

import com.datalingvo.examples.weather.apixu.*;
import com.datalingvo.examples.weather.apixu.beans.*;
import com.datalingvo.mdllib.*;
import com.datalingvo.mdllib.DLTokenSolver.*;
import com.datalingvo.mdllib.tools.builder.*;
import org.apache.commons.lang3.tuple.*;
import java.text.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * Weather example model provider.
 * <p>
 * This is relatively complete weather service with elaborative HTML output and a non-trivial
 * intent matching logic. It uses https://www.apixu.com REST service for the actual
 * weather information.
 */
@DLActiveModelProvider
public class WeatherProvider extends DLSingleModelProviderAdapter {
    // It is demo token and its usage has some restrictions (history data contains one day only, etc).
    // Please register your own account at https://www.apixu.com/pricing.aspx and
    // replace this demo token with your own.
    private ApixuWeatherService srv = new ApixuWeatherService("3f9b7de2d3894eb6b27150825171909");

    // Date formats.
    private final static DateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd");
    // We'll use string substitution here for '___' piece...
    private final static DateFormat outFmt = new SimpleDateFormat("EE'<br/><span style=___>'MMM dd'</span>'");

    // Base CSS.
    private static final String CSS = "style='display: inline-block; min-width: 120px'";

    /**
     * A shortcut to convert millis to the local date object.
     *
     * @param ms Millis to convert.
     * @return Local date object.
     */
    private LocalDate toLocalDate(long ms) {
        return Instant.ofEpochMilli(ms).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Makes Google static map fragment for given coordinates.
     *
     * @param lat Latitude.
     * @param lon Longitude.
     * @return Query result fragment.
     */
    private DLQueryResult makeMap(String lat, String lon) {
        double dLat = Double.parseDouble(lat);
        double dLon = Double.parseDouble(lon);

        return DLQueryResult.jsonGmap(
            String.format(
                "{" +
                    "\"cssStyle\": {" +
                        "\"width\": \"600px\", " +
                        "\"height\": \"300px\"" +
                    "}," +
                    "\"gmap\": {" +
                        "\"center\": \"%f,%f\"," +
                        "\"zoom\": 4," +
                        "\"scale\": 2," +
                        "\"size\": \"600x300\", " +
                        "\"maptype\": \"terrain\", " +
                        "\"markers\": \"color:red|%f,%f\"" +
                    "}" +
                "}",
                dLat,
                dLon,
                dLat,
                dLon
            )
        );
    }

    /**
     * Utility to prepare HTML table header with given date.
     *
     * @param date Date for the header.
     * @return HTML fragment.
     */
    private String prepHeader(String date) {
        try {
            return
                "\"<center style='color: #add'>" +
                outFmt.format(inFmt.parse(date)).replace("___", "'font-weight: 200; font-size: 80%'") +
                "</center>\"";
        }
        catch (ParseException e) {
            // No-op.

            return date;
        }
    }

    /**
     * Utility to prepare HTML table cell with given day data.
     *
     * @param day Day's weather holder.
     * @return HTML fragment.
     */
    private String prepCell(Day day) {
        String css1 = "font-size: 90%; color: #fff; font-weight: 200; letter-spacing: 0.05em";
        String css2 = "font-size: 100%; color: #fff; font-weight: 400; letter-spacing: 0.05em";

        return String.format("\"" +
            "<center><img style='margin-bottom: 5px;' title='%s' src='%s'><br/>" +
            "<span style='%s font-wei'>%s F <span style='font-size: 80%%'> - %s F</span></span><br/>" +
            "<span style='%s'>Humidity %d%%</span><br/>" +
            "<span style='%s'>Wind %s mph</span></center>\"",
            day.getCondition().getText(), day.getCondition().getIcon(),
            css2, Math.round(day.getMaxTempF()), Math.round(day.getMinTempF()),
            css1, day.getAvgHumidity(),
            css1, Math.round(day.getMaxWindMph())
        );
    }

    /**
     * Makes query multipart result for given date range weather.
     *
     * @param res Weather holder for the range of dates.
     * @return Query result.
     */
    private DLQueryResult makeRangeResult(RangeResponse res) {
        Location loc = res.getLocation();

        String headers = Arrays.stream(res.getForecast().getForecastDay()).map(day ->
            prepHeader(day.getDate())).collect(Collectors.joining(","));
        String rows = Arrays.stream(res.getForecast().getForecastDay()).map(day ->
            prepCell(day.getDay())).collect(Collectors.joining(","));

        // Multipart result (HTML block + HTML table + Google map).
        return DLQueryResult.jsonMultipart(
            // HTML fragment.
            DLQueryResult.html(
                String.format(
                    "<b %s>City:</b> <span style='color: #F1C40F'>%s</span><br/>" +
                    "<b %s>Local Time:</b> %s",
                    CSS, loc.getName(),
                    CSS, loc.getLocaltime()
                )
            ),
            // HTML table fragment.
            DLQueryResult.jsonTable(
                String.format(
                    "{" +
                        "\"border\": true," +
                        "\"background\": \"#2f4963\"," +
                        "\"borderColor\": \"#607d8b\"," +
                        "\"columns\": [%s]," +
                        "\"rows\": [[%s]]" +
                    "}",
                    headers,
                    rows
                )
            ),
            // Static Google map.
            makeMap(loc.getLatitude(), loc.getLongitude())
        );
    }

    /**
     * Makes query multipart result for single date.
     *
     * @param res Weather holder for single date.
     * @return Query result.
     */
    private DLQueryResult makeCurrentResult(CurrentResponse res) {
        Location loc = res.getLocation();
        Current cur = res.getCurrent();

        // Multipart result (HTML block + Google map).
        return DLQueryResult.jsonMultipart(
            // HTML block fragment.
            DLQueryResult.html(
                String.format(
                    "<div style='font-weight: 200; letter-spacing: 0.02em'>" +
                        "<b %s>Conditions:</b> <b><span style='color: #F1C40F'>%s, %s F</span></b><br/>" +
                        "<b %s>City:</b> %s<br/>" +
                        "<b %s>Humidity:</b> %d%%<br/>" +
                        "<b %s>Wind:</b> %s %s mph<br/>" +
                        "<b %s>Local Time:</b> %s<br/>" +
                    "</div>",
                    CSS, cur.getCondition().getText(), Math.round(cur.getTempF()),
                    CSS, loc.getName(),
                    CSS, cur.getHumidity(),
                    CSS, cur.getWindDir(), cur.getWindMph(),
                    CSS, loc.getLocaltime()
                )
            ),
            // Google map fragment.
            makeMap(loc.getLatitude(), loc.getLongitude())
        );
    }

    /**
     * Extracts date range from given list of tokens.
     *
     * @param toks 'dl:date' tokens.
     * @return Pair of dates or {@code null} if not found.
     */
    private Pair<LocalDate, LocalDate> prepDate(List<DLToken> toks) {
        if (!toks.isEmpty()) {
            DLMetadata meta = toks.get(0).getMetadata();

            return Pair.of(toLocalDate(meta.getLong("DATE_FROM")), toLocalDate(meta.getLong("DATE_TO")));
        }
        else
            return null;
    }

    /**
     * Extracts geo location suitable for APIXU service.
     *
     * @param toks 'dl:geo' tokens.
     * @param senMeta Sentence metadata.
     * @return Geo location.
     */
    private String prepGeo(List<DLToken> toks, DLMetadata senMeta) throws DLRejection {
        Optional<DLToken> cityOpt =
            toks.stream().filter(g -> g.getMetadata().get("GEO_KIND").equals("CITY")).findAny();

        if (cityOpt.isPresent()) {
            DLMetadata cityMeta = cityOpt.get().getMetadata();

            return cityMeta.getString("GEO_CITY") + ',' + cityMeta.getString("GEO_COUNTRY");
        }

        if (!toks.isEmpty())
            throw new DLRejection("Only cities can be used for location. Misspelled name?");
        else
            // Weather service understands this format too.
            return senMeta.getDouble("LATITUDE") + "," + senMeta.getDouble("LONGITUDE");
    }

    /**
     * Makes query result for given date range.
     *
     * @param ctx Token solver context.
     * @param from From date.
     * @param to To date.
     * @return Query result.
     */
    private DLQueryResult onRangeMatch(DLTokenSolverContext ctx, LocalDate from, LocalDate to) {
        Pair<LocalDate, LocalDate> date = prepDate(ctx.getTokens().get(1));

        if (date == null)
            date = Pair.of(from, to);

        String geo = prepGeo(ctx.getTokens().get(2), ctx.getSentence().getMetadata());

        return makeRangeResult(srv.getWeather(geo, date));
    }

    /**
     * Callback on forecast intent match.
     *
     * @param ctx Token solver context.
     * @return Query result.
     */
    private DLQueryResult onForecastMatch(DLTokenSolverContext ctx) {
        // Look 5 days ahead by default.
        return onRangeMatch(ctx, LocalDate.now(), LocalDate.now().plusDays(5));
    }

    /**
     * Callback on history intent match.
     *
     * @param ctx Token solver context.
     * @return Query result.
     */
    private DLQueryResult onHistoryMatch(DLTokenSolverContext ctx) {
        // Look 5 days back by default.
        return onRangeMatch(ctx, LocalDate.now().minusDays(5), LocalDate.now());
    }

    /**
     * Callback on current date intent match.
     *
     * @param ctx Token solver context.
     * @return Query result.
     */
    private DLQueryResult onCurrentMatch(DLTokenSolverContext ctx) {
        Pair<LocalDate, LocalDate> date = prepDate(ctx.getTokens().get(1));
        String geo = prepGeo(ctx.getTokens().get(2), ctx.getSentence().getMetadata());

        return date != null ? makeRangeResult(srv.getWeather(geo, date)): makeCurrentResult(srv.getCurrentWeather(geo));
    }

    // Shortcut method for creating a intent with given weather token.
    private INTENT makeMatch(String tokId) {
        return new CONV_INTENT(
            new TERM("id == " + tokId, 1, 1),      // Index 0. Mandatory weather token.
            new TERM("id == dl:date", 0, 1),  // Index 1. Optional date.
            new TERM("id == dl:geo", 0, 1)    // Index 2. Optional location.
        );
    }

    /**
     * Initializes model provider.
     */
    public WeatherProvider() {
        String modelPath = DLModelBuilder.classPathFile("weather_model.json");

        DLTokenSolver solver = new DLTokenSolver("solver", false, () -> { throw new DLCuration(); });

        // Match exactly one weather token and optional 'dl:geo' and 'dl:date' tokens including
        // looking into conversation context.
        solver.addIntent(makeMatch("wt:hist"), this::onHistoryMatch);
        solver.addIntent(makeMatch("wt:fcast"), this::onForecastMatch);
        solver.addIntent(makeMatch("wt:curr"), this::onCurrentMatch);

        setup("dl.weather.ex", DLModelBuilder.newJsonModel(modelPath).setQueryFunction(solver::solve).build());
    }
}
