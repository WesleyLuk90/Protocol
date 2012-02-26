package rainbowpc.node;

import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import com.google.gson.JsonElement;
import rainbowpc.Protocol;
import rainbowpc.RainbowException;
import rainbowpc.node.NodeMessage;
import rainbowpc.node.WorkMessage;
import rainbowpc.RpcAction;

public class NodeProtocol extends Protocol {
	private final static int DEFAULT_CONTROL_PORT = 7001;

	public NodeProtocol(String host) throws IOException, RainbowException {
		super(host);
		register();
	}
	public NodeProtocol(String host, int port) throws IOException, RainbowException {
		super(host, port);
		register();
	}
	public NodeProtocol(Socket socket) throws IOException, RainbowException {
		super(socket);
		register();
	}

	protected void initRpcMap() {
		rpcMap = new TreeMap<String, RpcAction>();
		rpcMap.put("workOrder", new RpcAction() {
			public void action(String jsonRaw) {
				queueMessage(translator.fromJson(jsonRaw, WorkMessage.class));
			}
		});
		rpcMap.put("bootstrap", new RpcAction() {
			public void action(String jsonRaw) {
				queueMessage(translator.fromJson(jsonRaw, BootstrapMessage.class));
			}
		});
	}

	/////////////////////////////////////////////////////////////////
	// Bootstrapping code
	//
	private void register() throws IOException, RainbowException {
		sendMessage("register", new RegisterMessage());
	}

	//////////////////////////////////////////////////////////////
	// External interaction interface
	//

	//////////////////////////////////////////////////////////////
	// Query/Outgoing messages
	//
	private class RegisterMessage extends NodeMessage {
		private int cores;
		public RegisterMessage() {
			super();
			cores = Runtime.getRuntime().availableProcessors();
		}
	}
	
	//////////////////////////////////////////////////////////////
	// Response/Incoming messages
	//
	private class BootstrapMessage extends NodeMessage {
		public String id;
		public boolean accepted;
		public BootstrapMessage() {}
	}
}