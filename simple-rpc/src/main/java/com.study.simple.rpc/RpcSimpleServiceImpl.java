package com.study.simple.rpc;

import java.net.InetAddress;

/**
 * Only echo server information.
 *
 * @author : lost legend  2020.02.18  18:02
 * @since V1.0
 */
public class RpcSimpleServiceImpl implements RpcService {
    @Override
    public String echo(String info) {
        try {
            return
                    "Received info: " + info
                            + ", Host : " + InetAddress.getLocalHost().getHostName()
                            + " Ip: " + InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "Get server info failed.";
        }
    }
}
