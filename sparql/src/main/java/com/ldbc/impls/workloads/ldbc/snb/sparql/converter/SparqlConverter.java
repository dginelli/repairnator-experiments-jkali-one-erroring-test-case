package com.ldbc.impls.workloads.ldbc.snb.sparql.converter;

import com.ldbc.impls.workloads.ldbc.snb.converter.Converter;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class SparqlConverter extends Converter {

    public static final String SPARQL_DATETIME_QUERY_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String SPARQL_DATETIME_RETURN_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String SPARQL_DATE_QUERY_FORMAT = "yyyy-MM-dd";

    /**
     * Converts epoch seconds to SPARQL timestamps.
     *
     * @param timestamp
     * @return
     */
    @Override
    public String convertDateTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(SPARQL_DATETIME_QUERY_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return "\"" + sdf.format(new Date(timestamp)) + "\"";
    }

    @Override
    public String convertDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(SPARQL_DATE_QUERY_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return "\"" + sdf.format(new Date(timestamp)) + "\"";
    }

    public long convertTimestampToEpoch(String timestamp) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat(SPARQL_DATETIME_RETURN_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.parse(timestamp).toInstant().toEpochMilli();
    }

    public String convertString(String value) {
        return "\"" + value + "\"";
    }

    @Override
    public String convertStringList(List<String> values) {
        return values
                .stream()
                .map(v -> "(\"" + v + "\")")
                .collect(Collectors.joining(" "));
    }

    @Override
    public String convertBlacklist(List<String> words) {
        return "\"" +
                words.stream()
                        .map(v -> "(" + v + ")")
                        .collect(Collectors.joining("|")) +
                "\"";
    }

    @Override
    public String convertId(long value) {
        return String.format("%020d", value);
    }

}
