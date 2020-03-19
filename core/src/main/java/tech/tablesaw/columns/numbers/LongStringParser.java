package tech.tablesaw.columns.numbers;

import com.google.common.collect.Lists;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.columns.StringParser;
import tech.tablesaw.io.csv.CsvReadOptions;

public class LongStringParser extends StringParser<Long> {

    public LongStringParser(ColumnType columnType) {
        super(columnType);
    }

    public LongStringParser(LongColumnType columnType, CsvReadOptions readOptions) {
        super(columnType);
        if (readOptions.missingValueIndicator() != null) {
            missingValueStrings = Lists.newArrayList(readOptions.missingValueIndicator());
        }
    }

    @Override
    public boolean canParse(String str) {
        if (isMissing(str)) {
            return true;
        }
        String s = str;
        try {
            if (s.endsWith(".0")) {
                s = s.substring(0, s.length() - 2);
            }
            Long.parseLong(StringParser.remove(s, ','));
            return true;
        } catch (NumberFormatException e) {
            // it's all part of the plan
            return false;
        }
    }

    @Override
    public Long parse(String s) {
        return parseLong(s);
    }

    @Override
    public double parseDouble(String str) {
        return parseLong(str);
    }

    @Override
    public long parseLong(String str) {
        if (isMissing(str)) {
            return LongColumnType.missingValueIndicator();
        }
        String s = str;
        if (s.endsWith(".0")) {
            s = s.substring(0, s.length() - 2);
        }
        return Long.parseLong(StringParser.remove(s, ','));
    }
}
