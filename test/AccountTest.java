import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class AccountTest {
	
	Account me, her, another;
	
	@Before
	public void setUp() {
		me = new Account("Hakan");
		her = new Account("Serra");
		another = new Account("Cecile");
	}

	@Test
	public void updateFriendsIncomingRequestsUponMyRequest() {
		me.requestFriendship(her);
		assertTrue(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void noFriendRequests() {
		assertEquals(0, me.getIncomingRequests().size());
	}
	
	@Test
	public void testMultipleFriendRequests() {
		me.requestFriendship(her);
		me.requestFriendship(another);
		assertEquals(2, me.getIncomingRequests().size());
		assertTrue(me.getIncomingRequests().contains(another.getUserName()));
		assertTrue(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void doubleFriendRequestsAreOk() {
		me.requestFriendship(her);
		me.requestFriendship(her);
		assertEquals(1, me.getIncomingRequests().size());
	}
	
	@Test
	public void incomingRequestsUpdatedAfterAcceptingFriendRequest() {
		me.requestFriendship(her);
		her.friendshipAccepted(me);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
	}

	@Test
	public void incomingRequestsUpdatedAfterRejectingFriendRequest() {
		me.requestFriendship(her);
		her.friendshipRejected(me);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void threeWayFriendshipsWork() {
		me.requestFriendship(her);
		me.requestFriendship(another);
		her.requestFriendship(another);
		her.friendshipAccepted(me);
		another.friendshipAccepted(her);
		another.friendshipAccepted(me);
		assertTrue(me.hasFriend(her.getUserName()));
		assertTrue(me.hasFriend(another.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
		assertTrue(her.hasFriend(another.getUserName()));
		assertTrue(another.hasFriend(her.getUserName()));
		assertTrue(another.hasFriend(me.getUserName()));
	}
	
	@Test
	public void cannotBeFriendsWithAnExistingFriend() {
		me.requestFriendship(her);
		her.friendshipAccepted(me);
		assertTrue(her.hasFriend(me.getUserName()));
		me.requestFriendship(her);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
		assertFalse(her.getIncomingRequests().contains(me.getUserName()));
	}

	@Test
	public void testAddOutgoingResponse() {
		me.addOutgoingRequest("Cecile");
		assertTrue(me.getOutgoingRequests().contains("Cecile"));
	}

	@Test
	public void autoAcceptFriendshipsSetsCorespondingBoolean() {
		me.autoAcceptFriendships();
		assertTrue(me.autoAcceptFriendRequests);
	}

	@Test
	public void cancelingFriendshipsRemovesFriends() {
		me.requestFriendship(her);
		her.friendshipAccepted(me);
		me.cancelFriendship(her);
		assertFalse(her.hasFriend(me.getUserName()));
		assertFalse(me.hasFriend(her.getUserName()));
	}
}
