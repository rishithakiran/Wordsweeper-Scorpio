package com.scorpio.test.unit.server.protocol;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.protocol.response.BoardResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class BoardResponseTest {
	@Before
	public void resetGameManager() {
		GameManager.reset();
	}

	@Test
	public void functionality_Basic() throws WordSweeperException {
		UUID gameUUID = UUID.randomUUID();
		UUID reqUUID = UUID.randomUUID();
		String testPlayerName = "testPlayer";
		String password = null;
		CreateGameRequestController cgr = new CreateGameRequestController();
		Player newPlayer = new Player(testPlayerName, null);
		cgr.createGame(newPlayer, gameUUID.toString(), password);
		newPlayer.setLocation(new Coordinate(1, 1));
		Game g = GameManager.getInstance().findGameById(gameUUID.toString());
		g.getBoard().getTileAt(new Coordinate(1,1)).setBonus(true);

		String br = new BoardResponse(testPlayerName, gameUUID.toString(), reqUUID.toString(), false).toXML();

		String correctResp = String.format(
				"<response id='%s' success='true'>" + "<boardResponse gameId='%s' managingUser='%s' bonus='1,1'>"
						+ "<player name='%s' position='1,1' board='%s' score='0'/>" + "</boardResponse>"
						+ "</response>",
				reqUUID.toString(), gameUUID.toString(), testPlayerName, testPlayerName,
				g.getPlayerBoard(testPlayerName));
		assert (br.equals(correctResp));
	}

	@Test
	public void functionality_Admin() throws WordSweeperException {
		UUID gameUUID = UUID.randomUUID();
		UUID reqUUID = UUID.randomUUID();
		String testPlayerName = "testPlayer";
		String password = null;
		CreateGameRequestController cgr = new CreateGameRequestController();
		Player newPlayer = new Player(testPlayerName, null);
		cgr.createGame(newPlayer, gameUUID.toString(), password);
		newPlayer.setLocation(new Coordinate(1, 1));
		Game g = GameManager.getInstance().findGameById(gameUUID.toString());
		g.getBoard().getTileAt(new Coordinate(1,1)).setBonus(true);
		String br = new BoardResponse(testPlayerName, gameUUID.toString(), reqUUID.toString(), true).toXML();
		String correctResp = String.format("<response id='%s' success='true'>"
				+ "<boardResponse gameId='%s' managingUser='%s' bonus='1,1' size='%d' contents='%s'>"
				+ "<player name='%s' position='1,1' board='%s' score='0'/>" + "</boardResponse>" + "</response>",
				reqUUID.toString(), gameUUID.toString(), testPlayerName, g.getBoard().getSize(),
				g.getBoard().toString(), testPlayerName, g.getPlayerBoard(testPlayerName));
		assert (br.equals(correctResp));
	}
}
