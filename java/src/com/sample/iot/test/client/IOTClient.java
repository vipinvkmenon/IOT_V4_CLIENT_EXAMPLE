package com.sample.iot.test.client;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;


// A simple java client to send data to the SAP IOT 4.0 Platform

public class IOTClient {

    public static void main(String[] args) throws Exception{



        final String urlString = "https://<HOST>/iot/gateway/rest/measures/<ALTERNATE ID>";
        final String certificateLocationPath="Path to the .p12 file";
        final String password = "<PASSPHRASE>";
        final String jsonString = "{\"capabilityAlternateId\": \"100\", \"sensorAlternateId\": \"1B\", \"measures\": [{\"temperature\": \"1600\"}]}";  //Sample Data Payload




        final URL url = new URL(urlString);
        final SSLContext sc = getSSLContext(certificateLocationPath, password);


        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection)connection)
                    .setSSLSocketFactory(sc.getSocketFactory());
        }

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty( "Content-Type", "application/json" );
        OutputStream os = connection.getOutputStream();
        try (OutputStream output = connection.getOutputStream()) {
            output.write(jsonString.getBytes("UTF-8" ));
        }finally {
            os.flush();
            os.close();
        }


        int responseCode = connection.getResponseCode();
        System.out.println("Status Code : " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_ACCEPTED) { //Status 202

            printResponse(connection.getInputStream());
        } else {

            System.out.println("POST request error");
            printResponse(connection.getErrorStream());

        }

    }

    /*
    Provides the SSL context.
     */
    private static SSLContext getSSLContext(final String certificateLocation, final String password) throws Exception{

        final KeyStore keyStore = KeyStore.getInstance("PKCS12");
        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        final FileInputStream fileInputStream = new FileInputStream(certificateLocation);
        keyStore.load(fileInputStream, password.toCharArray());
        keyManagerFactory.init(keyStore, password.toCharArray());
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
        return sslContext;

    }

    private static void printResponse(final InputStream response) throws Exception{
        final BufferedReader in = new BufferedReader(new InputStreamReader(
                response));
        final StringBuffer stringBuffer = new StringBuffer();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            stringBuffer.append(inputLine);
        }
        in.close();

        // print result
        System.out.println(stringBuffer.toString());


    }

}
