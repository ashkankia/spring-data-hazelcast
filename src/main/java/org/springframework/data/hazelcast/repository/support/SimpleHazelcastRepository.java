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

import org.springframework.data.hazelcast.repository.HazelcastRepository;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.support.SimpleKeyValueRepository;
import org.springframework.data.repository.core.EntityInformation;

import java.io.Serializable;

/**
 * <P>A concrete implementation to instantiate directly rather than allow
 * Spring to generate.
 * </P>
 *
 * @param <T>  The domain object
 * @param <ID> The key of the domain object
 * @author Neil Stevenson
 */
public class SimpleHazelcastRepository<T extends Serializable, ID extends Serializable> extends SimpleKeyValueRepository<T, ID> implements HazelcastRepository<T, ID> {

    public SimpleHazelcastRepository(EntityInformation<T, ID> metadata, KeyValueOperations operations) {
        super(metadata, operations);
    }

}
