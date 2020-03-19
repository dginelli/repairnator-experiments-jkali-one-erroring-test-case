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
package llc.zombodb.query_parser.rewriters;

import llc.zombodb.cross_join.CrossJoinQueryBuilder;
import llc.zombodb.query_parser.ASTExpansion;
import llc.zombodb.query_parser.ASTIndexLink;
import llc.zombodb.query_parser.QueryParserNode;
import llc.zombodb.query_parser.metadata.IndexMetadata;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ConstantScoreQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * ZomboDB's stock {@link QueryRewriter} that resolves joins during construction
 */
class ZomboDBQueryRewriter extends QueryRewriter {

    public ZomboDBQueryRewriter(Client client, String indexName, NamedXContentRegistry contentRegistry, String input, boolean canDoSingleIndex, boolean needVisibilityOnTopLevel) {
        super(client, indexName, contentRegistry, input, canDoSingleIndex, needVisibilityOnTopLevel);
    }

    @Override
    protected QueryBuilder build(ASTExpansion node) {
        ASTIndexLink link = node.getIndexLink();
        ASTIndexLink myIndex = metadataManager.getMyIndex();
        QueryBuilder qb;

        if (link == myIndex && !node.isGenerated()) {
            return super.build(node);
        } else {
            if ("(null)".equals(link.getLeftFieldname()))
                return super.build(node);

            if (_isBuildingAggregate)
                return matchAllQuery();

            ASTIndexLink parentLink = null;
            QueryParserNode parentNode = (QueryParserNode) node.jjtGetParent();
            while (parentNode != null) {
                if ((parentLink = parentNode.getIndexLink()) != null)
                    break;
                parentNode = (QueryParserNode) parentNode.jjtGetParent();
            };

            if (parentLink == null)
                parentLink = myIndex;

            IndexMetadata leftMetadata = metadataManager.getMetadataForIndexName(parentLink.getIndexName());
            IndexMetadata rightMetadata = metadataManager.getMetadataForIndexName(link.getIndexName());

            boolean canOptimizeForJoins = link.getLeftFieldname().equals(leftMetadata.getBlockRoutingField()) &&
                    link.getRightFieldname().equals(rightMetadata.getBlockRoutingField()) &&
                    leftMetadata.getNumberOfShards() == rightMetadata.getNumberOfShards();

            qb = new ConstantScoreQueryBuilder(new CrossJoinQueryBuilder()
                    .index(link.getIndexName())
                    .type("data")
                    .leftFieldname(link.getLeftFieldname())
                    .rightFieldname(link.getRightFieldname())
                    .canOptimizeJoins(canOptimizeForJoins)
                    .query(applyVisibility(build(node.getQuery()))));
        }

        if (node.getFilterQuery() != null) {
            BoolQueryBuilder bqb = boolQuery();

            bqb.must(qb);
            bqb.filter(build(node.getFilterQuery()));

            qb = bqb;
        }

        return qb;
    }
}
