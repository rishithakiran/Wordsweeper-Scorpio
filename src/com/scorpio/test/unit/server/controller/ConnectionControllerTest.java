package com.scorpio.test.unit.server.controller;

import com.scorpio.server.accessory.Session;
import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.controller.JoinGameRequestController;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.test.util.FakeClientState;
import com.scorpio.test.util.TestHandler;
import com.scorpio.test.util.Trigger;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.serverbase.xml.Message;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

import static org.junit.Assert.fail;

/**
 * Test case for Connection Controller.
 * @author Josh
 *
 */
public class ConnectionControllerTest {
	private XMLUtil xml = new XMLUtil();

	@Before
	public void reset(){
		GameManager.reset();
	}

	@Test
	/**
	 * Ensure that with a given mapping, our connection controller routes us to
	 * the correct place.
	 */
	public void functionality_Basic() {
		HashMap<String, Class> mapping = new HashMap<String, Class>();
		mapping.put("createGameRequest", TestHandler.class);

		ConnectionController router = new ConnectionController(mapping);
		Message msg = xml.createMessageFromFile("testxml/createGameRequest.xml");
		TestHandler.t = new Trigger();

		router.process(null, msg);

		assert (TestHandler.t.isTripped());

	}

	@Test
	/**
	 * Ensure that if we somehow map to a controller that doesn't exist, the
	 * server doesn't crash and burn.
	 */
	public void resiliency_BadClass() {
		HashMap<String, Class> mapping = new HashMap<String, Class>();

		ConnectionController router = new ConnectionController(mapping);
		Message msg = xml.createMessageFromFile("testxml/createGameRequest.xml");

		assert (router.process(null, msg) == null);
	}

	@Test
	/**
	 * Ensure that mapping to a malformed class is handled gracefully.
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
		Message msg = xml.createMessageFromFile("testxml/createGameRequest.xml");

		assert (null == router.process(null, msg));
	}

	@Test
	/**
	 * Validate that if we're passed an empty message, nothing bad happens.
	 */
	public void resiliency_BadMessage() {
		ConnectionController router = new ConnectionController();
		assert (router.process(null, null) == null);
	}

	@Test
	/**
	 * Test a logout when the player was nice and closed out their session
	 */
	public void functionality_LogoutNobody(){
		FakeClientState fsc = new FakeClientState("a");
		fsc.setData(null);


		ConnectionController router = new ConnectionController();
		router.logout(fsc);

		// This test is okay if no exception is thrown
	}

	@Test
	/**
	 * Test a logout where the client just died and left themself in a game, and
	 * they are the last person
	 */
	public void functionality_LogoutClientLastPlayer(){
		CreateGameRequestController cgr = new CreateGameRequestController();
		FakeClientState fsc = new FakeClientState("a");
		Player p = new Player("a", fsc);
		try{
			cgr.createGame(p, "mygame", null);
		}catch(WordSweeperException ex){
			fail();
		}


		Game g = GameManager.getInstance().findGameById("mygame");
		assert(g != null);
		Session s = new Session(p, g);
		fsc.setData((Object)s);


		ConnectionController router = new ConnectionController();
		router.logout(fsc);

		// The game was dismantled
		assert(GameManager.getInstance().findGameById("mygane") == null);
	}

	@Test
	/**
	 * Test a logout where the client just died and left themself in a game, and
	 * they are not the last person
	 */
	public void functionality_LogoutClientNotLastPlayer(){
		CreateGameRequestController cgr = new CreateGameRequestController();
		JoinGameRequestController jgr = new JoinGameRequestController();
		FakeClientState fsc = new FakeClientState("a");
		Player p = new Player("a", fsc);
		Player p2 = new Player("b", new FakeClientState("b"));
		try{
			cgr.createGame(p, "mygame", null);
			jgr.joinGame(p2,"mygame", null);
		}catch(WordSweeperException ex){
			fail();
		}


		Game g = GameManager.getInstance().findGameById("mygame");
		assert(g != null);
		assert(g.getPlayers().size() == 2);
		Session s = new Session(p, g);
		fsc.setData((Object)s);


		ConnectionController router = new ConnectionController();
		router.logout(fsc);

		// The game was not dismantled and control was handed over correctly
		assert(GameManager.getInstance().findGameById("mygame") != null);
		assert(GameManager.getInstance().findGameById("mygame").getPlayers().size() == 1);
		assert(GameManager.getInstance().findGameById("mygame").getManagingPlayer().getName().equals("b"));
		assert(GameManager.getInstance().findGameById("mygame").getPlayers().get(0).getName().equals("b"));


	}

}