/*
 * Copyright 2014-2016 the original author or authors.
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
package org.springframework.data.hazelcast.repository.support;

import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactory;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;

import java.io.Serializable;

/**
 * <p>
 * Extend {@link KeyValueRepositoryFactoryBean} purely to be able to return {@link HazelcastRepositoryFactory} from
 * {@link #createRepositoryFactory(KeyValueOperations, Class, Class)} method.
 * </P>
 * <p>
 * This is necessary as the default implementation does not implement querying in a manner consistent with Hazelcast.
 * More details of this are in {@link HazelcastRepositoryFactory}.
 * </P>
 * <p>
 * This class is only called as repositories are needed. Rather than try to optimise called methods, allow to delegate
 * to {@code super} to ease future changes.
 * </P>
 * <p>
 * The end goal of this bean is for {@link org.springframework.data.hazelcast.repository.query.HazelcastPartTreeQuery} to be used for query preparation.
 * </P>
 *
 * @param <T>  Repository type, {@link org.springframework.data.hazelcast.repository.HazelcastRepository}
 * @param <S>  Domain object class
 * @param <ID> Domain object key, super expects {@link Serializable}
 * @author Neil Stevenson
 */
public class HazelcastRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends KeyValueRepositoryFactoryBean<T, S, ID> {

    /**
     * <p>
     * Default Spring Data KeyValue constructor {@link KeyValueRepositoryFactoryBean}
     * Creates a new {@link HazelcastRepositoryFactoryBean} for the given repository interface.
     * </P>
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public HazelcastRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    /**
     * <p>
     * Return a {@link HazelcastRepositoryFactory}.
     * </P>
     * <p>
     * {@code super} would return {@link KeyValueRepositoryFactory} which in turn builds {@link org.springframework.data.keyvalue.repository.KeyValueRepository}
     * instances, and these have a private method that implement querying in a manner that does not fit with Hazelcast.
     * More details are in {@link HazelcastRepositoryFactory}.
     * </P>
     *
     * @param operations
     * @param queryCreator         Creator
     * @param repositoryQueryType, not used
     * @return A {@link HazelcastRepositoryFactory} that creates {@link org.springframework.data.hazelcast.repository.HazelcastRepository} instances.
     */
    @Override
    protected KeyValueRepositoryFactory createRepositoryFactory(KeyValueOperations operations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator, Class<? extends RepositoryQuery> repositoryQueryType) {
        return new HazelcastRepositoryFactory(operations, queryCreator);
    }

}
