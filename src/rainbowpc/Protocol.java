package rainbowpc;

import com.google.gson.Gson;
import java.util.Queue;
import java.util.LinkedList;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import rainbowpc.Message;

public class Protocol {
	private final static int DEFAULT_PORT = 7001;

	public final static boolean WAIT = true;

	private Socket socket = null;
	private BufferedReader instream = null;
	private PrintWriter outstream = null;
	protected Gson translator = new Gson();

	public Protocol(String host) throws IOException {
		this(host, DEFAULT_PORT);
	}

	public Protocol(String host, int port) throws IOException {
		this(new Socket(host, port));
	}		

	public Protocol(Socket socket) throws IOException {
		this.initBuffers(socket);
	}

	protected void initBuffers(Socket socket) throws IOException {
		this.socket = socket;
		this.instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.outstream = new PrintWriter(socket.getOutputStream(), true);
	}

	protected String sendMessage(Message msg) throws IOException {
		return this.sendMessage(msg, false);
	}

	protected String sendMessage(Message msg, boolean waitForResponse) throws IOException {
		String result = null;
		outstream.println(msg.jsonEncode());
		if (waitForResponse) {
			result = instream.readLine();
		}
		return result;
	}

	public void shutdown() {
		try {
			this.instream.close();
			this.outstream.close();
			this.socket.close();
		}
		catch (IOException e) {
			// do nothing
		}
	}
}
