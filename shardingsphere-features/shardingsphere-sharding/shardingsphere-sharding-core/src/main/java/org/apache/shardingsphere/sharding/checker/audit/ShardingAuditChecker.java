/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sharding.checker.audit;

import org.apache.shardingsphere.infra.check.SQLCheckResult;
import org.apache.shardingsphere.infra.executor.check.SQLChecker;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.infra.metadata.user.Grantee;
import org.apache.shardingsphere.sharding.constant.ShardingAuditOrder;
import org.apache.shardingsphere.sharding.rule.ShardingRule;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public final class ShardingAuditChecker implements SQLChecker<ShardingRule> {
    
    @Override
    public boolean check(final String databaseName, final Grantee grantee, final ShardingRule rule) {
        return true;
    }
    
    @Override
    public SQLCheckResult check(final SQLStatement sqlStatement, final List<Object> parameters, final Grantee grantee,
                                final String currentDatabase, final Map<String, ShardingSphereDatabase> databases,
                                final ShardingRule rule) {
        for (String shardingAudit : rule.getShardingAudits()) {
            SQLCheckResult sqlCheckResult = rule.getShardingAuditAlgorithms().get(shardingAudit).check(sqlStatement, parameters, grantee, currentDatabase, databases, rule);
            if (!sqlCheckResult.isPassed()) {
                return sqlCheckResult;
            }
        }
        return new SQLCheckResult(true, "");
    }
    
    @Override
    public boolean check(final Grantee grantee, final ShardingRule rule) {
        return true;
    }
    
    @Override
    public boolean check(final Grantee grantee, final BiPredicate<Object, Object> validator, final Object cipher,
                         final ShardingRule rule) {
        return true;
    }
    
    @Override
    public int getOrder() {
        return ShardingAuditOrder.ORDER;
    }
    
    @Override
    public Class<ShardingRule> getTypeClass() {
        return ShardingRule.class;
    }
}
