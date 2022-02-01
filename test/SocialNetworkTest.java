import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SocialNetworkTest {

	SocialNetwork sn;
	Account me;
	
	@Before
	public void setUp() {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test 
	public void accountIsCreated() {
		assertNotNull(me);
		assertEquals("Hakan", me.getUserName());
	}
	
	@Test 
	public void canListSingleMemberOfSocialNetworkAfterOnePersonJoining() {
		Collection<String> members = sn.listMembers();
		assertTrue(members.contains("Hakan"));
	}

	@Test 
	public void sizeOfNetworkEqualsOneAfterOnePersonJoining() {
		Collection<String> members = sn.listMembers();
		assertEquals(1, members.size());
	}

	@Test 
	public void canListTwoPeopleWhoJoinSocialNetwork() {
		sn.join("Cecile");
		Collection<String> members = sn.listMembers();
		assertTrue(members.contains("Hakan"));
		assertTrue(members.contains("Cecile"));
	}
	
	@Test 
	public void sizeOfNetworkEqualsTwoAfterTwoPeopleJoinSocialNetwork() {
		sn.join("Cecile");
		Collection<String> members = sn.listMembers();
		assertEquals(2, members.size());
	}
	
	@Test 
	public void sendFriendRequest() {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		assertTrue(her.getIncomingRequests().contains("Hakan"));
	}

	@Test 
	public void acceptFriendRequest() {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(her.hasFriend("Hakan"));
	}
}
