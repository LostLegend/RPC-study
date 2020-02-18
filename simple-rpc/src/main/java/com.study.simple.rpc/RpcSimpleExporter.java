package com.study.simple.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Server task: receive client rpc call, and invoke method, then return to client.
 *
 * @author : lost legend  2020.02.18  18:06
 * @since V1.0
 */
public class RpcSimpleExporter {

    private static final int MAX_PROCESSOR = 4;

    private static final Executor executor =
            Executors.newFixedThreadPool(MAX_PROCESSOR);

    public static void handle0(String host, int port) throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(host, port));

        try {
            while (true) {
                executor.execute(new ExporterTask(serverSocket.accept()));
            }
        } finally {
            serverSocket.close();
        }



    }

    private static class ExporterTask implements Runnable {

        private Socket client = null;

        public ExporterTask(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            ObjectInputStream input = null;
            ObjectOutputStream output = null;

            try {
                input = new ObjectInputStream(client.getInputStream());

                // RPC invoke
                // get interface
                String interfaceName = input.readUTF();
                Class<?> service = Class.forName(interfaceName);
                // get method name
                String methodName = input.readUTF();
                // get method parameters type
                Class<?>[] parameterTypes = (Class<?>[])input.readObject();
                // get invoke arguments
                Object[] arguments = (Object[]) input.readObject();
                // execute method invoking
                Method method = service.getMethod(methodName, parameterTypes);
                Object result = method.invoke(service.newInstance(), arguments);

                output = new ObjectOutputStream(client.getOutputStream());
                output.writeObject(result);


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (client != null) {
                    try {
                        client.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
