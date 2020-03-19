package com.ldbc.impls.workloads.ldbc.snb.cypher;

import com.ldbc.impls.workloads.ldbc.snb.QueryStore;
import com.ldbc.impls.workloads.ldbc.snb.cypher.converter.CypherConverter;
import com.ldbc.impls.workloads.ldbc.snb.db.BaseDb;

public abstract class CypherDb<DbQueryStore extends QueryStore> extends BaseDb<DbQueryStore> {

    protected static final CypherConverter converter = new CypherConverter();

}
