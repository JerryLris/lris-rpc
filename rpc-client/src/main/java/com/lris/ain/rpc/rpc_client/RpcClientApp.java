package com.lris.ain.rpc.rpc_client;

import java.net.InetSocketAddress;

import com.lris.ain.rpc.rpc_interface.HelloService;

/**
 * Hello world!
 *
 */
public class RpcClientApp 
{
    public static void main( String[] args )
    {
        HelloService service = RpcClient.get(HelloService.class, new InetSocketAddress("localhost", 8020));
        System.out.println(service.hello("RPC"));	
    }
}
