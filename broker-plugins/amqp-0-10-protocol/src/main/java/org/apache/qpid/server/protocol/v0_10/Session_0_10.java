/*
 *
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
 *
 */
package org.apache.qpid.server.protocol.v0_10;

import java.util.Collection;

import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.logging.LogSubject;
import org.apache.qpid.server.model.Connection;
import org.apache.qpid.server.model.Consumer;
import org.apache.qpid.server.model.Queue;
import org.apache.qpid.server.model.Session;
import org.apache.qpid.server.protocol.AMQSessionModel;
import org.apache.qpid.server.protocol.ConsumerListener;
import org.apache.qpid.server.session.AbstractAMQPSession;
import org.apache.qpid.server.transport.AMQPConnection;
import org.apache.qpid.server.util.Action;
import org.apache.qpid.transport.network.Ticker;

public class Session_0_10 extends AbstractAMQPSession<Session_0_10, ConsumerTarget_0_10>
        implements AMQSessionModel<Session_0_10, ConsumerTarget_0_10>, LogSubject
{
    private final AMQPConnection_0_10 _connection;
    private final ServerSession _serverSession;

    protected Session_0_10(final Connection<?> parent, final int sessionId, final ServerSession serverSession)
    {
        super(parent, sessionId);
        _connection = (AMQPConnection_0_10) parent;
        _serverSession = serverSession;
    }

    @Override
    public EventLogger getEventLogger()
    {
        return getConnection().getEventLogger();
    }

    @Override
    public String toLogString()
    {
        return _serverSession.toLogString();
    }

    @Override
    public AMQPConnection<?> getAMQPConnection()
    {
        return _connection;
    }

    @Override
    public void block(final Queue<?> queue)
    {
        _serverSession.block(queue);
    }

    @Override
    public void unblock(final Queue<?> queue)
    {
        _serverSession.unblock();
    }

    @Override
    public void block()
    {
        _serverSession.block();
    }

    @Override
    public void unblock()
    {
        _serverSession.unblock();
    }

    @Override
    public Object getConnectionReference()
    {
        return _serverSession.getConnectionReference();
    }

    @Override
    public void addConsumerListener(final ConsumerListener listener)
    {
        _serverSession.addConsumerListener(listener);
    }

    @Override
    public void removeConsumerListener(final ConsumerListener listener)
    {
        _serverSession.removeConsumerListener(listener);
    }

    @Override
    public void setModelObject(final Session<?> session)
    {
        _serverSession.setModelObject(this);
    }

    @Override
    public Session<?> getModelObject()
    {
        return this;
    }

    @Override
    public void transportStateChanged()
    {
        _serverSession.transportStateChanged();
    }

    @Override
    public boolean processPending()
    {
        return _serverSession.processPending();
    }

    @Override
    public void notifyWork(final ConsumerTarget_0_10 target)
    {
        _serverSession.notifyWork(target);
    }

    @Override
    public int compareTo(final AMQSessionModel o)
    {
        return getId().compareTo(o.getId());
    }

    @Override
    public void addDeleteTask(final Action<? super Session_0_10> task)
    {
        _serverSession.addDeleteTask((Action<? super ServerSession>) task);
    }

    @Override
    public void removeDeleteTask(final Action<? super Session_0_10> task)
    {
        _serverSession.removeDeleteTask((Action<? super ServerSession>) task);

    }

    @Override
    public int getChannelId()
    {
        return _serverSession.getChannelId();
    }

    @Override
    public boolean getBlocking()
    {
        return _serverSession.getBlocking();
    }

    @Override
    public Collection<Consumer<?, ConsumerTarget_0_10>> getConsumers()
    {
        return _serverSession.getConsumers();
    }

    @Override
    public long getConsumerCount()
    {
        return _serverSession.getConsumerCount();
    }

    @Override
    public long getTxnRejects()
    {
        return _serverSession.getTxnRejects();
    }

    @Override
    public long getTxnCommits()
    {
        return _serverSession.getTxnCommits();
    }

    @Override
    public long getTxnStart()
    {
        return _serverSession.getTxnStart();
    }

    @Override
    public int getUnacknowledgedMessageCount()
    {
        return _serverSession.getUnacknowledgedMessageCount();
    }

    @Override
    public void addTicker(final Ticker ticker)
    {
        _serverSession.addTicker(ticker);
    }

    @Override
    public void removeTicker(final Ticker ticker)
    {
        _serverSession.removeTicker(ticker);
    }

    @Override
    public void doTimeoutAction(final String idleTransactionTimeoutError)
    {
        _serverSession.doTimeoutAction(idleTransactionTimeoutError);
    }

    @Override
    public LogSubject getLogSubject()
    {
        return _serverSession.getLogSubject();
    }

    @Override
    public long getTransactionUpdateTimeLong()
    {
        return _serverSession.getTransactionUpdateTimeLong();
    }

    @Override
    public long getTransactionStartTimeLong()
    {
        return _serverSession.getTransactionStartTimeLong();
    }

    public AMQPConnection_0_10 getConnection()
    {
        return _connection;
    }
}