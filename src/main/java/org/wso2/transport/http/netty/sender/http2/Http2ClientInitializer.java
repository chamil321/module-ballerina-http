/*
 *   Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.transport.http.netty.sender.http2;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpClientUpgradeHandler;
import io.netty.handler.codec.http2.DefaultHttp2Connection;
import io.netty.handler.codec.http2.DelegatingDecompressorFrameListener;
import io.netty.handler.codec.http2.Http2ClientUpgradeCodec;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2ConnectionHandler;
import io.netty.handler.codec.http2.Http2ConnectionHandlerBuilder;
import io.netty.handler.codec.http2.Http2FrameListener;
import io.netty.handler.codec.http2.Http2FrameLogger;
import org.wso2.transport.http.netty.config.SenderConfiguration;

import static io.netty.handler.logging.LogLevel.DEBUG;

/**
 * This is responsible for initializing a connection with a backend service
 */
public class Http2ClientInitializer extends ChannelInitializer<SocketChannel> {

    private static final Http2FrameLogger logger =
            new Http2FrameLogger(DEBUG,  //change mode to INFO for logging frames
                                 Http2ClientInitializer.class);

    private Http2ClientHandler http2ClientHandler;
    private UpgradeRequestHandler upgradeRequestHandler;
    private Http2ConnectionHandler connectionHandler;
    private Http2FrameListener listener;
    private boolean skipHttpToHttp2Upgrade = false;
    private Http2Connection connection;
    private Http2FrameListenerAdapter clientFrameListener;

    public Http2ClientInitializer(SenderConfiguration senderConfiguration) {
        skipHttpToHttp2Upgrade = senderConfiguration.skipHttpToHttp2Upgrade();
        connection = new DefaultHttp2Connection(false);
        clientFrameListener = new Http2FrameListenerAdapter();
        listener = new DelegatingDecompressorFrameListener(connection, clientFrameListener);
        connectionHandler =
                new Http2ConnectionHandlerBuilder().connection(connection).frameLogger(logger)
                        .frameListener(listener).build();
        http2ClientHandler = new Http2ClientHandler(connection, connectionHandler.encoder());
        upgradeRequestHandler = new UpgradeRequestHandler(http2ClientHandler);
    }

    public void setTargetChannel(TargetChannel targetChannel) {
        http2ClientHandler.setTargetChannel(targetChannel);
        upgradeRequestHandler.setTargetChannel(targetChannel);
    }

    /**
     * Initiate Http2 connection as clearText or TCP secured
     *
     * @param socketChannel
     * @throws Exception
     */
    @Override
    public void initChannel(SocketChannel socketChannel) throws Exception {

        if (skipHttpToHttp2Upgrade) {
            socketChannel.pipeline().addLast(connectionHandler, http2ClientHandler);
        } else {
            configureClearTextUpgrade(socketChannel);
        }
    }

    public Http2ClientHandler getHttp2ClientHandler() {
        return http2ClientHandler;
    }

    private void configureClearTextUpgrade(SocketChannel socketChannel) {
        HttpClientCodec sourceCodec = new HttpClientCodec();
        Http2ClientUpgradeCodec upgradeCodec = new Http2ClientUpgradeCodec(connectionHandler);
        HttpClientUpgradeHandler upgradeHandler =
                new HttpClientUpgradeHandler(sourceCodec, upgradeCodec, 65536);
        socketChannel.pipeline()
                .addLast(sourceCodec, upgradeHandler, upgradeRequestHandler);
    }

}
