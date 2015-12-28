/*
 * This file is part of the Illarion project.
 *
 * Copyright © 2015 - Illarion e.V.
 *
 * Illarion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Illarion is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package illarion.client.util.account;

import com.google.common.util.concurrent.ListenableFuture;
import illarion.client.IllaClient;
import illarion.client.util.Lang;
import illarion.client.util.account.response.AccountGetResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class AccountSystem implements AutoCloseable {
    @Nullable
    private String endpoint;

    /**
     * The authenticator used to handle authenticated requests.
     */
    @Nullable
    private Authenticator authenticator;

    @Nonnull
    private final List<AccountSystemEndpoint> endPoints;

    @Nullable
    private RequestHandler requestHandler;

    public AccountSystem() {
        AccountSystemEndpoint official = new AccountSystemEndpoint(
                "https://illarion.org/app.php",
                Lang.getMsg("accountsystem.server.official"),
                "server.official");
        if (IllaClient.IS_DEVELOP) {
            // every client but the release client gets the selection of two servers.
            AccountSystemEndpoint local = new AccountSystemEndpoint(
                    "http://localhost/app.php",
                    Lang.getMsg("accountsystem.server.local"),
                    "server.local",
                    true, "test", "test");
            endPoints = Arrays.asList(official, local);
        } else {
            endPoints = Collections.singletonList(official);
        }
    }

    public void setEndpoint(@Nonnull AccountSystemEndpoint endpoint) {
        String newEndpoint = endpoint.getUrl();
        if (!Objects.equals(newEndpoint, this.endpoint)) {
            try {
                closeRequestHandler();
            } catch (Exception ignored) {
            }
            this.endpoint = newEndpoint;
        }
    }

    /**
     * Set the authentication that is used for the interaction with the account system.
     *
     * @param credentials the authentication credentials
     * @throws IllegalStateException in case the authentication is already set
     */
    public void setAuthentication(@Nullable Credentials credentials) {
        if (credentials == null) {
            authenticator = null;
            endpoint = null;
            try {
                closeRequestHandler();
            } catch (Exception ignored) {
            }
        } else {
            authenticator = new Authenticator(credentials.getUserName(), credentials.getPassword());
            setEndpoint(credentials.getEndpoint());
        }
    }

    @Nonnull
    private RequestHandler getRequestHandler() {
        if (endpoint == null) {
            throw new IllegalStateException("Communicating with the account system is not possible until the endpoint is set.");
        }

        RequestHandler handler = requestHandler;
        if (handler == null) {
            handler = new RequestHandler(endpoint);
            requestHandler = handler;
        }
        return handler;
    }

    @Nonnull
    private Authenticator getAuthenticator() {
        Authenticator authenticator = this.authenticator;
        if (authenticator == null) {
            throw new IllegalStateException("The request requires authentication credentials.");
        }
        return authenticator;
    }

    @Nonnull
    public ListenableFuture<AccountGetResponse> getAccountInformation() {
        RequestHandler handler = getRequestHandler();
        Authenticator authenticator = getAuthenticator();

        AccountGetRequest request = new AccountGetRequest(authenticator);

        return handler.sendRequestAsync(request);
    }

    /**
     * Fetch the list of endpoints that can be served by the account system.
     *
     * @return thel ist of endpoints
     */
    @Nonnull
    public List<AccountSystemEndpoint> getEndPoints() {
        return Collections.unmodifiableList(endPoints);
    }

    private void closeRequestHandler() throws Exception {
        RequestHandler handler = requestHandler;
        requestHandler = null;
        if (handler != null) {
            handler.close();
        }
    }

    @Override
    public void close() throws Exception {
        closeRequestHandler();
    }
}