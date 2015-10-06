/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.hazelcast;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map.Entry;

import org.springframework.data.hazelcast.repository.query.HazelcastCriteriaAccessor;
import org.springframework.data.hazelcast.repository.query.HazelcastSortAccessor;
import org.springframework.data.keyvalue.core.QueryEngine;

import com.hazelcast.query.PagingPredicate;
import com.hazelcast.query.Predicate;

/**
 * @author Christoph Strobl
 */
public class HazelcastQueryEngine extends QueryEngine<HazelcastKeyValueAdapter, Predicate<?, ?>, Comparator<Entry<?, ?>>> {

    public HazelcastQueryEngine() {
        super(new HazelcastCriteriaAccessor(), new HazelcastSortAccessor());
    }

    @Override
    public Collection<?> execute(Predicate<?, ?> criteria, Comparator<Entry<?, ?>> sort, int offset, int rows,
            Serializable keyspace) {

        Predicate<?, ?> predicateToUse = criteria;

        if (sort != null || offset > 0 || rows > 0) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            PagingPredicate pp = new PagingPredicate(criteria, ((Comparator<Entry>) (Comparator) sort), rows);
            if (offset > 0 && rows > 0) {
                int x = offset / rows;
                while (x > 0) {
                    pp.nextPage();
                    x--;
                }
            }
            predicateToUse = pp;
        }

        return this.getAdapter().getMap(keyspace).values(predicateToUse);

    }

    @Override
    public long count(Predicate<?, ?> criteria, Serializable keyspace) {
        return this.getAdapter().getMap(keyspace).keySet(criteria).size();
    }

}
