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
package org.springframework.data.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.data.hazelcast.repository.config.Constants;
import org.springframework.data.keyvalue.core.AbstractKeyValueAdapter;
import org.springframework.data.keyvalue.core.ForwardingCloseableIterator;
import org.springframework.data.util.CloseableIterator;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author Christoph Strobl
 * @author Neil Stevenson
 */
public class HazelcastKeyValueAdapter extends AbstractKeyValueAdapter {

    private HazelcastInstance hzInstance;

    public HazelcastKeyValueAdapter() {
        this(Hazelcast.getOrCreateHazelcastInstance(new Config(Constants.HAZELCAST_INSTANCE_NAME)));
    }

    public HazelcastKeyValueAdapter(HazelcastInstance hzInstance) {
        super(new HazelcastQueryEngine());
        Assert.notNull(hzInstance, "hzInstance must not be 'null'.");
        this.hzInstance = hzInstance;
    }

    public void setHzInstance(HazelcastInstance hzInstance) {
        this.hzInstance = hzInstance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object put(Object id, Object item, String keyspace) {

        Assert.notNull(id, "Id must not be 'null' for adding.");
        Assert.notNull(item, "Item must not be 'null' for adding.");

        return getMap(keyspace).put(id, item);
    }

    @Override
    public boolean contains(Object id, String keyspace) {
        return getMap(keyspace).containsKey(id);
    }

    @Override
    public Object get(Object id, String keyspace) {
        return getMap(keyspace).get(id);
    }

    @Override
    public Object delete(Object id, String keyspace) {
        return getMap(keyspace).remove(id);
    }

    @Override
    public Collection<?> getAllOf(String keyspace) {
        return getMap(keyspace).values();
    }

    @SuppressWarnings("unchecked")
    @Override
    public CloseableIterator<Entry<Object, Object>> entries(String keyspace) {
        Iterator<Entry<Object, Object>> iterator = this.getMap(keyspace).entrySet().iterator();
        return new ForwardingCloseableIterator<>(iterator);
    }

    @Override
    public void deleteAllOf(String keyspace) {
        getMap(keyspace).clear();
    }

    @Override
    public void clear() {
        this.hzInstance.shutdown();
    }

    @Override
    public long count(String keyspace) {
        return this.getMap(keyspace).size();
    }

    @SuppressWarnings("rawtypes")
    protected IMap getMap(final Serializable keyspace) {
        Assert.isInstanceOf(String.class, keyspace, "Keyspace identifier must of type String.");
        return hzInstance.getMap((String) keyspace);
    }

    @Override
    public void destroy() throws Exception {
        this.clear();
    }

}
