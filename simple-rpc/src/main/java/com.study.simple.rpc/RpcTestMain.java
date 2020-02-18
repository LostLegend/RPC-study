package com.study.simple.rpc;

import java.net.InetSocketAddress;

/**
 * @author : lost legend  2020.02.18  22:11
 * @since V1.0
 */
public class RpcTestMain {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RpcSimpleExporter.handle0("localhost", 8000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        RpcClient<RpcService> client = new RpcClient<>();

        RpcService service = client.invoker(RpcSimpleServiceImpl.class, new InetSocketAddress("localhost", 8000));

        System.out.println(service.echo("Hello, world"));

    }
}
