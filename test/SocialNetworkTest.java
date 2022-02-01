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

	@Test 
	public void rejectFriendRequest() {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.rejectFriendshipFrom("Hakan", her);
		assertFalse(me.hasFriend("Cecile"));
		assertFalse(her.hasFriend("Hakan"));
	}

	@Test 
	public void cancelFriendship() {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		sn.sendFriendshipCancellationTo("Cecile", me);
		assertFalse(me.hasFriend("Cecile"));
		assertFalse(her.hasFriend("Hakan"));
	}

	@Test
	public void sendFriendRequestToNonexistantUserDoesNotAddToOutgoingRequests() {
		sn.sendFriendshipTo("Cecile", me);
		assertFalse(me.getOutgoingRequests().contains("Cecile"));
	}

	@Test
	public void sendFriendRequestAddsAnOutgoingResponse() {
		sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		assertTrue(me.getOutgoingRequests().contains("Cecile"));
	}

	@Test
	public void testAcceptAllFriendships() {
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		sn.acceptAllFriendshipsTo(me);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(me.hasFriend("Aditi"));
		assertTrue(Cecile.hasFriend("Hakan"));
		assertTrue(Aditi.hasFriend("Hakan"));
	}

	@Test
	public void testRejectAllFriendships() {
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		sn.rejectAllFriendshipsTo(me);
		assertFalse(me.hasFriend("Cecile"));
		assertFalse(me.hasFriend("Aditi"));
		assertFalse(Cecile.hasFriend("Hakan"));
		assertFalse(Aditi.hasFriend("Hakan"));
	}

	@Test
	public void testAutoAcceptAllFriendships() {
		me.autoAcceptFriendships();
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(me.hasFriend("Aditi"));
		assertTrue(Cecile.hasFriend("Hakan"));
		assertTrue(Aditi.hasFriend("Hakan"));
	}

	@Test
	public void testRemoveMeFromOnePersonSocialNetwork() {
		sn.leave(me);
		assertTrue(sn.listMembers().isEmpty());
	}

	@Test
	public void removeMeFromSocialNetworkWithTwoFriendsRemovesMeFromMyFriendsRecords() {
		me.autoAcceptFriendships();
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		sn.leave(me);
		assertFalse(Cecile.hasFriend("Hakan"));
		assertFalse(Aditi.hasFriend("Hakan"));
	}
}
