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

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.qpid.server.exchange.ExchangeImpl;
import org.apache.qpid.server.queue.AMQQueue;
import org.apache.qpid.server.virtualhost.VirtualHostImpl;
import org.apache.qpid.test.utils.QpidTestCase;
import org.apache.qpid.transport.ExchangeDelete;
import org.apache.qpid.transport.ExecutionErrorCode;
import org.apache.qpid.transport.ExecutionException;
import org.apache.qpid.transport.Option;
import org.mockito.ArgumentMatcher;

public class ServerSessionDelegateTest extends QpidTestCase
{
    private VirtualHostImpl<?, AMQQueue<?>, ExchangeImpl<?>> _host;
    private ServerSession _session;
    private ServerSessionDelegate _delegate;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        _host = mock(VirtualHostImpl.class);

        ServerConnection serverConnection = mock(ServerConnection.class);
        doReturn(_host).when(serverConnection).getVirtualHost();

        _session = mock(ServerSession.class);
        when(_session.getConnection()).thenReturn(serverConnection);

        _delegate = new ServerSessionDelegate();
    }

    public void testExchangeDeleteWhenIfUsedIsSetAndExchangeHasBindings() throws Exception
    {
        ExchangeImpl<?> exchange = mock(ExchangeImpl.class);
        when(exchange.hasBindings()).thenReturn(true);

        doReturn(exchange).when(_host).getAttainedExchange(getTestName());

        final ExchangeDelete method = new ExchangeDelete(getTestName(), Option.IF_UNUSED);
        _delegate.exchangeDelete(_session, method);

        verify(_session).invoke(argThat(new ArgumentMatcher<ExecutionException>()
        {
            public boolean matches(Object object)
            {
                ExecutionException exception = (ExecutionException)object;
                return exception.getErrorCode() == ExecutionErrorCode.PRECONDITION_FAILED
                        && "Exchange has bindings".equals(exception.getDescription());
            }
        }));
    }

    public void testExchangeDeleteWhenIfUsedIsSetAndExchangeHasNoBinding() throws Exception
    {
        ExchangeImpl<?> exchange = mock(ExchangeImpl.class);
        when(exchange.hasBindings()).thenReturn(false);

        doReturn(exchange).when(_host).getAttainedExchange(getTestName());

        final ExchangeDelete method = new ExchangeDelete(getTestName(), Option.IF_UNUSED);
        _delegate.exchangeDelete(_session, method);

        verify(_host).removeExchange(exchange, false);
    }

}
