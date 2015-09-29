package com.zuehlke.arquillian;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.RollbackException;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GamePersistenceTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Game.class.getPackage())
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("jbossas-ds.xml");
	}

	private static final String[] GAME_TITLES = { "Super Mario Brothers", "Mario Kart", "F-Zero" };

	@PersistenceContext
	EntityManager em;

	@Inject
	UserTransaction utx;

	@Before
	public void preparePersistenceTest() throws Exception {
		clearData();
		insertData();
		startTransaction();
	}

	private void clearData() throws Exception {
		utx.begin();
		em.joinTransaction();
		em.createQuery("delete from Game").executeUpdate();
		utx.commit();
	}

	private void insertData() throws Exception {
		utx.begin();
		em.joinTransaction();
		for (String title : GAME_TITLES) {
			Game game = new Game(title);
			em.persist(game);
		}
		utx.commit();
		// clear the persistence context (first-level cache)
		em.clear();
	}

	private void startTransaction() throws Exception {
		utx.begin();
		em.joinTransaction();
	}

	@Test
	public void shouldFindAllGamesUsingCriteriaApi() throws Exception {
		// given
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Game> criteria = builder.createQuery(Game.class);

		Root<Game> game = criteria.from(Game.class);
		criteria.select(game);
		criteria.orderBy(builder.asc(game.get("id")));

		// when
		List<Game> games = em.createQuery(criteria).getResultList();

		// then
		assertContainsAllGames(games);
		// utx.commit();
	}

	@Test(expected = RollbackException.class)
	public void enterTooShortTitle() throws Exception {
		Game game = new Game("sh");
		em.persist(game);
		utx.commit();
	}

	@Test(expected = RollbackException.class)
	public void enterTooLongTitle() throws Exception {

		String tooLongTitle = repeatString("a", 51);
		Game game = new Game(tooLongTitle);
		em.persist(game);
		utx.commit();
	}

	@Test(expected = RollbackException.class)
	public void enterNullTitle() throws Exception {

		Game game = new Game(null);
		em.persist(game);
		utx.commit();
	}

	private static void assertContainsAllGames(Collection<Game> retrievedGames) {
		Assert.assertEquals(GAME_TITLES.length, retrievedGames.size());
		final Set<String> retrievedGameTitles = new HashSet<String>();
		for (Game game : retrievedGames) {
			retrievedGameTitles.add(game.getTitle());
		}
		Assert.assertTrue(retrievedGameTitles.containsAll(Arrays.asList(GAME_TITLES)));
	}

	private String repeatString(String s, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(s);
		}
		return sb.toString();
	}
}
