package com.study.simple.rpc;

/**
 * The simplest rpc service function.Only can echo some content.
 *
 * @author : lost legend  2020.02.15  15:00
 * @since V1.0
 */
public interface RpcService {
    String echo(String info);
}
