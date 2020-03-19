/*
 * Copyright 2017 ZomboDB, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package llc.zombodb.fast_terms.collectors;

import com.carrotsearch.hppc.LongArrayList;
import org.apache.lucene.index.DocValuesType;

public class NumberCollector extends FastTermsCollector<long[]> {

    private class NumericDocValuesCollector implements InternalCollector {
        public void collect(int doc) {
            if (numeric == null)
                return;

            long value = numeric.get(doc);
            setMinMax(value);
            data.add(value);
        }
    }

    private class SortedNumericDocValuesCollector  implements InternalCollector {
        public void collect(int doc) {
            if (sortedNumeric == null)
                return;

            sortedNumeric.setDocument(doc);
            int cnt = sortedNumeric.count();
            for (int i = 0; i < cnt; i++) {
                long value = sortedNumeric.valueAt(i);
                setMinMax(value);
                data.add(value);
            }
        }
    }

    private final LongArrayList data = new LongArrayList();
    private InternalCollector collector;

    public NumberCollector(String fieldname) {
        super(fieldname);
    }

    public long[] getData() {
        return data.buffer;
    }

    public int getDataCount() {
        return data.size();
    }

    @Override
    public void internal_collect(int doc) {
        collector.collect(doc);
    }

    @Override
    protected void setDocValuesType(DocValuesType type) {
        switch (type) {
            case NUMERIC:
                this.collector = new NumericDocValuesCollector();
                break;
            case SORTED_NUMERIC:
                this.collector = new SortedNumericDocValuesCollector();
                break;
            case NONE:
                this.collector = null;
                break;
            default:
                throw new IllegalStateException("Unsupported doctype: " + type);
        }
    }
}
