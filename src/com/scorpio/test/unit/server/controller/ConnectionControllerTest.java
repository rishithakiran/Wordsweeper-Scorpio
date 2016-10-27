package com.scorpio.test.unit.server.controller;

import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.test.util.TestHandler;
import com.scorpio.test.util.Trigger;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;
import org.junit.Test;
import java.util.HashMap;

public class ConnectionControllerTest {
	private XMLUtil xml = new XMLUtil();

	@Test
	/**
	 * Ensure that with a given mapping, our connection controller routes us to
	 * the correct place
	 */
	public void functionality_Basic() {
		HashMap<String, Class> mapping = new HashMap<String, Class>();
		mapping.put("createGameRequest", TestHandler.class);

		ConnectionController router = new ConnectionController(mapping);
		Message msg = xml.createMessageFromFile(
				"C:/Users/Apoorva/AppData/Local/GitHub/Wordsweeper-Scorpio/resources/testxml/createGameRequest.xml");
		TestHandler.t = new Trigger();

		router.process(null, msg);

		assert (TestHandler.t.isTripped());

	}

	@Test
	/**
	 * Ensure that if we somehow map to a controller that doesn't exist, the
	 * server doesn't crash and burn
	 */
	public void resiliency_BadClass() {
		HashMap<String, Class> mapping = new HashMap<String, Class>();

		ConnectionController router = new ConnectionController(mapping);
		Message msg = xml.createMessageFromFile("C:/Users/Apoorva/AppData/Local/GitHub/Wordsweeper-Scorpio/resources/testxml/createGameRequest.xml");

		assert (router.process(null, msg) == null);
	}

	@Test
	/**
	 * Ensure that mapping to a malformed class is handled gracefully
	 */
	public void resiliency_MalformedClass() {
		HashMap<String, Class> mapping = new HashMap<String, Class>();
		class BadClass implements IProtocolHandler {
			// Constructor not implemented, which is bad

			@Override
			public Message process(ClientState state, Message request) {
				return null;
			}
		}

		mapping.put("createGameRequest", BadClass.class);

		ConnectionController router = new ConnectionController(mapping);
		Message msg = xml.createMessageFromFile("C:/Users/Apoorva/AppData/Local/GitHub/Wordsweeper-Scorpio/resources/testxml/createGameRequest.xml");

		assert (null == router.process(null, msg));
	}

	@Test
	/**
	 * Validate that if we're passed an empty message, nothing bad happens
	 */
	public void resiliency_BadMessage() {
		ConnectionController router = new ConnectionController();
		assert (router.process(null, null) == null);
	}

}