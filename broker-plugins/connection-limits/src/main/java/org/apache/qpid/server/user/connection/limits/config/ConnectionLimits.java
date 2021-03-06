/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.qpid.server.user.connection.limits.config;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public interface ConnectionLimits extends CombinableLimit<ConnectionLimits>
{
    Integer getCountLimit();

    Map<Duration, Integer> getFrequencyLimits();

    boolean isUserBlocked();

    @Override
    default ConnectionLimits then(ConnectionLimits other)
    {
        if (other != null && isEmpty())
        {
            return other;
        }
        return this;
    }

    @Override
    default ConnectionLimits mergeWith(ConnectionLimits second)
    {
        if (second == null || isUserBlocked() || second.isEmpty())
        {
            return this;
        }
        if (second.isUserBlocked() || isEmpty())
        {
            return second;
        }
        return new ConnectionLimitsImpl(this, second);
    }

    static ConnectionLimits noLimits()
    {
        return NoLimits.INSTANCE;
    }

    final class NoLimits implements ConnectionLimits
    {
        static final NoLimits INSTANCE = new NoLimits();

        private NoLimits()
        {
            super();
        }

        @Override
        public Integer getCountLimit()
        {
            return null;
        }

        @Override
        public Map<Duration, Integer> getFrequencyLimits()
        {
            return Collections.emptyMap();
        }

        @Override
        public boolean isUserBlocked()
        {
            return false;
        }

        @Override
        public boolean isEmpty()
        {
            return true;
        }

        @Override
        public ConnectionLimits mergeWith(ConnectionLimits second)
        {
            return Optional.ofNullable(second).orElse(this);
        }

        @Override
        public ConnectionLimits then(ConnectionLimits other)
        {
            return Optional.ofNullable(other).orElse(this);
        }
    }
}
