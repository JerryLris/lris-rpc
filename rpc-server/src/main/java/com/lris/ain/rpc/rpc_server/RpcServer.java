package com.lris.ain.rpc.rpc_server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcServer {

	private static ExecutorService executor = Executors.newFixedThreadPool(10);

	private static final HashMap<String, Class> serviceRegistry = new HashMap<String, Class>();

	public void register(Class serviceInterface, Class impl) {

		// 注册服务
		serviceRegistry.put(serviceInterface.getName(), impl);

	}

	public void start(int port) throws IOException {

		final ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(port));
		System.out.println("服务已启动...");
		while (true) {
			executor.execute(new Runnable() {

				@Override
				public void run() {

					Socket socket = null;
					ObjectInputStream inputStream = null;
					ObjectOutputStream outputStream = null;
					try {
						socket = server.accept();
						// 接受到服务请求，将码流反序列化定位具体服务
						inputStream = new ObjectInputStream(socket.getInputStream());
						String serviceName = inputStream.readUTF();
						String methodName = inputStream.readUTF();
						Class<?>[] parameterTypes = (Class<?>[]) inputStream.readObject();
						Object[] arguments = (Object[]) inputStream.readObject();
						// 在服务注册表中根据调用的服务获取到具体实现类
						Class serviceClass = serviceRegistry.get(serviceName);
						if (serviceClass == null) {
							throw new ClassNotFoundException(serviceName + " 未找到...");
						}
						Method method = serviceClass.getMethod(methodName, parameterTypes);
						// 调用获取结果
						Object result = method.invoke(serviceClass.newInstance(), arguments);

						// 将结果序列化后发送回客户端
						outputStream = new ObjectOutputStream(socket.getOutputStream());
						outputStream.writeObject(result);

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							if (socket != null) {
								socket.close();
							}
							if (inputStream != null) {
								inputStream.close();
							}
							if(outputStream != null){
								outputStream.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}

			});

		}

	}

}
