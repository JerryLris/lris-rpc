package com.lris.ain.rpc.rpc_client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RpcClient<T> {

	@SuppressWarnings("unchecked")
	public static <T> T get(final Class<?> serviceInterfase,final InetSocketAddress addr) {
		T instance = (T) Proxy.newProxyInstance(serviceInterfase.getClassLoader(),
				new Class<?>[] {serviceInterfase},
				new InvocationHandler() {
					
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						
							Socket socket = null;
							ObjectOutputStream  output= null;
							ObjectInputStream input = null;
							try {
							//链接服务端
							socket = new Socket();
							socket.connect(addr);
							//将调用的接口类、方法名、参数列表等系列化后发送给服务提供者
							output = new ObjectOutputStream(socket.getOutputStream());
							output.writeUTF(serviceInterfase.getName());;
							output.writeUTF(method.getName());
							output.writeObject(method.getParameterTypes());
							output.writeObject(args);
							
							//同步阻塞等待服务器返回应答，获取应答后返回
							input = new ObjectInputStream(socket.getInputStream());
							return input.readObject();
						}finally {
							if(socket != null) {
								socket.close();
							}
							if(output != null) {
								output.close();
							}
							if(input != null) {
								input.close();
							}
						}
						
					}
				}
				
			
		);
		
		
		return instance;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
