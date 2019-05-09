package com.lris.ain.rpc.rpc_server;

import java.io.IOException;
import java.net.ServerSocket;

import com.lris.ain.rpc.rpc_interface.HelloService;

public class RpcServerApp {

	public static void main(String[] args) throws IOException{
		
		RpcServer server = new RpcServer();
		//注册服务
		server.register(HelloService.class,HelloServiceimple.class);
		//启动并绑定端口
		server.start(8020);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
