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
package org.springframework.data.hazelcast.repository.query;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;

import org.springframework.data.keyvalue.core.CriteriaAccessor;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;

/**
 * TODO Javadoc
 */
public class HazelcastCriteriaAccessor implements CriteriaAccessor<Predicate<?, ?>> {

    public Predicate<?, ?> resolve(KeyValueQuery<?> query) {

        if (query == null || query.getCritieria() == null) {
            return null;
        }

        if (query.getCritieria() instanceof Predicate) {
            return (Predicate<?, ?>) query.getCritieria();
        }

        if (query.getCritieria() instanceof PredicateBuilder) {
            return (PredicateBuilder) query.getCritieria();
        }

        throw new UnsupportedOperationException();
    }

}
