package org.enterprise.protocol.http;

import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.enterprise.constants.EnvironmentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author: albert.chen
 * @create: 2023-11-28
 * @description:
 */
public class HttpClientManage {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientManage.class);
    private static CloseableHttpClient closeableHttpClient = null;

    static {
        // connection pool
        // max_connect = 25;
        // init_connect = 5;
        // you can update for pcm.setDefaultMaxPerRoute() or pcm.setMaxTotal()
        PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager();

        // config timeout
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000, TimeUnit.MILLISECONDS)
                .setResponseTimeout(500, TimeUnit.MILLISECONDS)
                .build();

        //retry strategy ，default retry time=1，retry internal=1s
        HttpRequestRetryStrategy httpRequestRetryStrategy = new DefaultHttpRequestRetryStrategy();

        // httpClient
        closeableHttpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(pcm)
                .setRetryStrategy(httpRequestRetryStrategy)
                .build();
    }

    public static String postJson(String message) throws Exception {
        String dscheduleServerHost = System.getProperty(EnvironmentConfig.HTTP_HOST);
        if (dscheduleServerHost == null || dscheduleServerHost.isEmpty()) {
            throw new Exception("not found host");
        }

        String httpUrl = dscheduleServerHost.concat(EnvironmentConfig.HTTP_URL);
        return postJson(httpUrl, message);
    }


    /**
     * @param url
     * @param message
     * @return
     */
    public static String postJson(String url, String message) throws Exception {
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Accept", "application/json;charset=UTF-8");
            httpPost.setHeader("Content-Type", "application/json");
            StringEntity stringEntity = new StringEntity(message, ContentType.create("application/json", "UTF-8"));
            httpPost.setEntity(stringEntity);

            BasicHttpClientResponseHandler basicHttpClientResponseHandler = new BasicHttpClientResponseHandler();
            return closeableHttpClient.execute(httpPost, basicHttpClientResponseHandler);
        } catch (Exception e) {
            logger.error("post http error ,param {}", message, e);
            throw e;
        }
    }


}
