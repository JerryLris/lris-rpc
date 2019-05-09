package com.lris.ain.rpc.rpc_server;

import com.lris.ain.rpc.rpc_interface.HelloService;

public class HelloServiceimple implements HelloService {

	@Override
	public String hello(String name) {
		System.out.println("收到消息："+name);
		return "你好,"+name;
	}

}
